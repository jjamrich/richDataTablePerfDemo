/**
 *
 */
package org.jboss.example.portlet.jasper.db.test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.docs.richfaces.demo.DateIntervalFilter;
import org.docs.richfaces.demo.chart.PieChart;
import org.hibernate.criterion.Criterion;
import org.jboss.example.portlet.jasper.db.PhoneCallBean;
import org.jboss.example.portlet.jasper.db.PhoneCallHibernateService;
import org.jboss.example.portlet.jasper.db.ServiceFactory;

public class AccessHibernateServiceTest extends TestCase {

	private static final Log log = LogFactory
			.getLog(AccessHibernateServiceTest.class);

	private PhoneCallHibernateService hibernateService = ServiceFactory
			.getPHibernateServiceInstance();

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testHibernateConnection() {
		log.debug(" hibernateConnectionTest()-->");

		// TODO JJa 2010-11-01: change test to be repetable
		try {
			Iterator<PhoneCallBean> callsIterator = hibernateService
					.getPhoneCallList(0, 10, null, null).iterator();
			while (callsIterator.hasNext()) {
				PhoneCallBean callBean = callsIterator.next();
				log.debug(" " + callBean.getCallerName() + " ["
						+ callBean.getPhoneNumber() + "] called "
						+ callBean.getDesPhoneNumber() + " from "
						+ callBean.getCallStart() + " to "
						+ callBean.getCallEnd());
			}

			// check if something found in DB. If HSQLDB used, there should be
			// DB empty
			// Assert.assertTrue(hibernateService.getRowCount() > 0);

		} catch (Exception ex) {
			// Logger.getLogger(AccessHibernateServiceTest.class.getName()).log(Level.SEVERE,
			// null, ex);
			log.error(" \n\n\n############ An error occured during the test: "
					+ ex);
			ex.printStackTrace(System.out);
		}

		log.debug("Ending method hibernateConnectionTest()<--");
	}

	public void testHibernateFiltering() {
		log.debug(" testHibernateFiltering()-->");

		int SEPTEMBER = 8; // due to calendar understanding month numbering :)

		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2010, SEPTEMBER, 29, 1, 0, 0); // 2010-09-29 1:0:0
		Date from = cal.getTime();
		cal.set(2010, SEPTEMBER, 29, 2, 0, 0); // 2010-09-29 2:0:0
		Date to = cal.getTime();

		DateIntervalFilter filterStartCalls = new DateIntervalFilter(
				"callStart");
		filterStartCalls.setFrom(from);
		filterStartCalls.setTo(to);

		List<Criterion> filters = new ArrayList<Criterion>();
		filters.add(filterStartCalls.getCriteria());

		try {
			List<PhoneCallBean> calls = hibernateService.getPhoneCallList(0,
					20, filters, null);

			/*
			 * Iterator<PhoneCallBean> callsIterator = calls.iterator(); while
			 * (callsIterator.hasNext()) { PhoneCallBean callBean =
			 * callsIterator.next(); log.debug(" " + callBean.getCallerName() +
			 * " [" + callBean.getPhoneNumber() + "] called " +
			 * callBean.getDesPhoneNumber() + " from " + callBean.getCallStart()
			 * + " to " + callBean.getCallEnd()); }
			 */

			log.info(" ### Call count between " + from + " and " + to + " is "
					+ calls.size());
			// Assert.assertEquals(10, calls.size());

		} catch (Exception ex) {
			// Logger.getLogger(AccessHibernateServiceTest.class.getName()).log(Level.SEVERE,
			// null, ex);
			log.error(" \n\n\n############ An error occured during the test: "
					+ ex);
			ex.printStackTrace(System.out);
		}

		log.debug("Ending method testHibernateFiltering()<--");
	}

	public void testGetUniquePhoneNumbers() {
		log.debug("testGetUniquePhoneNumbers() -->");

		List<String> pn = hibernateService.getUniquePhoneNo();

		Iterator<String> it = pn.iterator();
		while (it.hasNext())
			log.info(" ### " + it.next());

		log.info(" Total count of phone number found is " + pn.size());

		log.debug("testGetUniquePhoneNumbers() <--");
	}

	public void testGetData4Chart() {
		/*
		 * Retrieve data for chart creation. Idea for chart is: how much time
		 * spent owner of selected phone number calling other numbers. So select
		 * times spent by calling everyone phone number
		 */
		/*
		 * select des_phone_number, count(*) from pcall_recs 
		 * where phone_number = ':phoneNumber' group by des_phone_number
		 */
		
		String[] phoneNumber = {"993485234", "443990493", "883047384", "112394839", "775983321", "334332349", };
		for (int index=0; index<phoneNumber.length; ++index) {
			List l = hibernateService.getChartDataForPhoneNumber(phoneNumber[index]);

			log.info(" testGetData4Chart() --> ");

			Iterator it = l.iterator();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				StringBuffer sb = new StringBuffer();
				sb.append("row[");
				for (int i = 0; i < row.length; ++i) {
					sb.append(row[i] + ", ");
				}
				sb.append("]");
				log.info(sb.toString());
			}
			
			PieChart chart = new PieChart("test title for " + phoneNumber[index], l);
			
			try {
				FileOutputStream fos = new FileOutputStream(
						new File("target/resultPieChart_"+phoneNumber[index]+".png"));
				chart.writeChartAsPng(fos);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}		

	}
}