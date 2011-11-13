/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
 */
package org.jboss.example.portlet.jasper.db;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * Service for performing phonecall database operations.
 * @author <a href="mailto:vrockai@redhat.com">Viliam Rockai</a>
 */
public interface PhoneCallHibernateService {

    /**
     * Perstits PhoneCallBean
     * @param pcb - bean to be persisted
     * @throws Exception
     */
    public void savePhoneCall(PhoneCallBean pcb) throws Exception;

    /**
     * Gets all PhoneCallBeans from the database.
     * @return List of all phone calls from the database
     * @throws Exception
     */
    // public List<PhoneCallBean> getPhoneCallList() throws Exception;
    
    public List<PhoneCallBean> getPhoneCallList(Integer startId, int numberOfRows, 
    		List<Criterion> criterions, List<Order> orders) throws Exception;
    
    /**
     * Gets unique list of all available phone numbers in current DB
     * @return
     * @throws Exception
     */
    public List<String> getUniquePhoneNo();
    
    /**
     * @return count of all available rows
     */
    public int getRowCount();
    
    /**
     * Retrieve count of calls performed by phone number given 
     * as parameter. Result format is 
     *  Object[(String) phoneNumber, (Integer) count]
     */
    public List<Object[]> getChartDataForPhoneNumber(String phoneNumber);
    
}
