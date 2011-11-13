/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.docs.richfaces.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.el.Expression;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.docs.richfaces.demo.chart.PieChart;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.example.portlet.jasper.db.InitDbHelper;
import org.jboss.example.portlet.jasper.db.PhoneCallBean;
import org.jboss.example.portlet.jasper.db.PhoneCallHibernateService;
import org.jboss.example.portlet.jasper.db.ServiceFactory;
import org.richfaces.model.ExtendedFilterField;
import org.richfaces.model.FilterField;
import org.richfaces.model.Modifiable;
import org.richfaces.model.Ordering;
import org.richfaces.model.SortField2;

/**
 * @author jjamrich@redhat.com
 *
 */
public class PhoneCallsDataTableModel extends SerializableDataModel implements Modifiable {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
	
	private static final Log log = LogFactory.getLog(PhoneCallsDataTableModel.class);

	/** Generated UID */
	private static final long serialVersionUID = 6965889598877781565L;
	
	private PhoneCallHibernateService hibernateService;
	
	private PhoneCallBean dataItem; 
	
	private Integer rowCount; // better to buffer row count locally	
	private int rowsPerPage = 5;
	private int pageScroller = 1;
	
	private DateIntervalFilter callStartFilter = new DateIntervalFilter("callStart");
	private DateIntervalFilter callEndFilter = new DateIntervalFilter("callEnd");
	private PhoneNumberFilter phoneFilter = new PhoneNumberFilter("phoneNumber");
	
	private List<SelectItem> telNumList;
	
	private List<FilterField> filterFields;
	private List<SortField2> sortFields;
	
	public PhoneCallsDataTableModel(){		
		super();
		log.info(" ### Initializing model and appropriate DB");
		hibernateService = ServiceFactory.getPHibernateServiceInstance();
		
		try {			
			// firstly check if there is any records in DB
			List<PhoneCallBean> l = hibernateService.getPhoneCallList(0, 1, null, null);
			
			// and if DB is really empty, fill some random data
			if (l.isEmpty()) {
				InitDbHelper.initDb(hibernateService, InitDbHelper.RECORDS_COUNT);
			}
			
			telNumList = SimpleSelectItemConverter.convertToSelectItemList(
								hibernateService.getUniquePhoneNo());
			
		} catch (Exception e) {
			log.error(" Following exception appeared during DB initialization: ", e);
		}
	}

	@Override
	public void update() {		
						
	}

	@Override
	public Object getRowKey() {		
		return dataItem;
	}

	/**
	 * This method normally called by Visitor before request Data Row.
	 */
	@Override
	public void setRowKey(Object key) {
		this.dataItem = (PhoneCallBean) key;
	}

	/**
	 * This is main part of Visitor pattern. Method called by framework many times during request processing. 
	 */
	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {
		int firstRow = ((SequenceRange)range).getFirstRow();
		int numberOfRows = ((SequenceRange)range).getRows();
		
		if (log.isDebugEnabled()) log.debug(" ### walk() --> firstRow=" + firstRow + ", numberOfRows=" + numberOfRows 
				+ ", pageScroller=" + pageScroller);
		
		List<Order> orders = new ArrayList<Order>();
		if (sortFields != null) {
			for (SortField2 sortField : sortFields) {
				Ordering ordering = sortField.getOrdering();
				String propertyName = getPropertyName(sortField.getExpression());				
				if (propertyName != null) {
					if (Ordering.ASCENDING.equals(ordering) || Ordering.DESCENDING.equals(ordering)) {	
						orders.add(Ordering.ASCENDING.equals(ordering) ?
								Order.asc(propertyName) : Order.desc(propertyName));
					}
				}
			}
		}
		
		List<Criterion> filters = new ArrayList<Criterion>();
		if (filterFields != null) {
			for (FilterField filterField : filterFields) {
				String propertyName = getPropertyName(filterField.getExpression());
				String filterValue = ((ExtendedFilterField) filterField).getFilterValue();
				if (filterValue != null && filterValue.length() != 0) {
					Criterion c = Restrictions.like(propertyName, filterValue, MatchMode.START).ignoreCase();
					filters.add(c);
				}
			}
		}
		
		// apply filter form
		if (!callStartFilter.getFilterEmpty()) {
			filters.add(callStartFilter.getCriteria());
		}		
		if (!callEndFilter.getFilterEmpty()) {
			filters.add(callEndFilter.getCriteria());
		}
		if (!phoneFilter.getFilterEmpty()){
			filters.add(phoneFilter.getCriteria());
		}
		
		List<PhoneCallBean> phoneList = null;
		try {
			phoneList = hibernateService.getPhoneCallList(new Integer(firstRow), numberOfRows, filters, orders);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (PhoneCallBean item : phoneList) {
			visitor.process(context, item, argument);
		}
	}

	@Override
	public int getRowCount() {
		if (rowCount==null) {
			rowCount = new Integer(hibernateService.getRowCount());
			return rowCount.intValue();
		} else {
			return rowCount.intValue();
		}
	}

	@Override
	public Object getRowData() {
		return dataItem;
	}

	@Override
	public int getRowIndex() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getWrappedData() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRowAvailable() {
		return (this.dataItem != null);
	}
	
	/**
	 * Provide inputs need for sorting and filtering outside RF
	 */
	public void modify(List<FilterField> filterFields, List<SortField2> sortFields) {
		this.filterFields = filterFields;
		this.sortFields = sortFields;
	}
	
	private String getPropertyName(Expression expression) {
		// expression from "sortBy" attribute
		String exprStr = expression.getExpressionString();
		String result = null;
		if (exprStr != null) {
			if ( exprStr.startsWith("{", 1) && exprStr.endsWith("}")) {
				result = exprStr.substring(exprStr.indexOf("{")+1, exprStr.indexOf("}"));
				// wanna get only property name - without 'item.' prefix
				result = result.substring(result.lastIndexOf(".")+1);
			}
		}
		return result;
	}
	
	 public void paint(OutputStream out, Object data) throws IOException{

		 @SuppressWarnings("unchecked")
			PieChart chart = new PieChart("Calls performed by selected number", 
					(List<Object[]>)hibernateService.getChartDataForPhoneNumber(phoneFilter.getPhoneNumber()));
            chart.writeChartAsPng(out);
	    }

	@Override
	public void setRowIndex(int arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setWrappedData(Object arg0) {		
	}

	public int getPageScroller() {
		return pageScroller;
	}

	public void setPageScroller(int pageScroller) {
		this.pageScroller = pageScroller;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public DateIntervalFilter getCallStartFilter() {
		return callStartFilter;
	}

	public void setCallStartFilter(DateIntervalFilter callStartFilter) {
		this.callStartFilter = callStartFilter;
	}

	public DateIntervalFilter getCallEndFilter() {
		return callEndFilter;
	}

	public void setCallEndFilter(DateIntervalFilter callEndFilter) {
		this.callEndFilter = callEndFilter;
	}

	public String getDateFormat() {
		return DATE_FORMAT;
	}

	public List<SelectItem> getTelNumList() {
		return telNumList;
	}

	public void setTelNumList(List<SelectItem> telNumList) {
		this.telNumList = telNumList;
	}

	public PhoneNumberFilter getPhoneFilter() {
		return phoneFilter;
	}

	public void setPhoneFilter(PhoneNumberFilter phoneFilter) {
		this.phoneFilter = phoneFilter;
	}

}
