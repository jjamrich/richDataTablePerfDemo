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

import java.util.Date;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Filter providing filter fields for select dates from interval
 * @author jjamrich
 *
 */
public class DateIntervalFilter implements SimpleFilter {
	
	private String propertyName;
	
	private Date from;
	private Date to;
	
	/**
	 * 
	 * @param propertyName - Bean's property name used as filter property
	 */
	public DateIntervalFilter(String propertyName) {
		this.propertyName = propertyName;
	}
	
	/**
	 * Check if filter should be considered as empty 
	 * and should be ignored when create Criterion
	 * @return isEmpty: 
	 *  <b>true</b> if all filter's properties are null
	 *  <b>false</b> if at least one property is non-null
	 */
	public boolean getFilterEmpty() {
		return from == null && to == null;
	}
	
	/** 
	 * Prepare criteria according to filter values 
	 * @return
	 */
	public Criterion getCriteria() {
		
		if (getFilterEmpty()) 
			return null;
		
		if (!getFilterEmpty()) 
			return Restrictions.between(propertyName, from, to);
		
		Criterion crit = null;
		if (from != null)
			crit = Restrictions.gt(propertyName, from);
		else				
			crit = Restrictions.lt(propertyName, to);
		
		return crit;
	}
	
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	
	

}
