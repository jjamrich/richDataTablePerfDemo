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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Implementation of service for performing phonecall database operations.
 * @author <a href="mailto:vrockai@redhat.com">Viliam Rockai</a>
 */
public class PhoneCallHibernateServiceImpl implements PhoneCallHibernateService {

    private static final Log log = LogFactory.getLog(PhoneCallHibernateServiceImpl.class);
    private static final Object factoryMutex = new Object();
    private static SessionFactory sessionFactory;

    /**
     * Perstits PhoneCallBean
     * @param pcb - bean to be persisted
     * @throws Exception
     */
    @Override
    public void savePhoneCall(PhoneCallBean pcb) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = getHibernateSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(pcb);
            // log.info("PhoneCall " + pcb + " saved.");
            transaction.commit();
        } catch (HibernateException e) {
            transaction.rollback();
            throw new HibernateException("Cannot save PhoneCall.", e);
        }
    }

    /**
     * Gets all PhoneCallBeans from the database.
     * @return List of all phone calls from the database
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<PhoneCallBean> getPhoneCallList(Integer startId, int numberOfRows, List<Criterion> criterions, List<Order> order) 
    throws Exception {
    	
        List<PhoneCallBean> phoneCallList = null;
        Session session = null;
        Transaction transaction = null;
        try {
            session = getHibernateSession();
            transaction = session.beginTransaction();
            
            Criteria crit = session.createCriteria(PhoneCallBean.class);
            if (startId != null) {
            	crit.setFirstResult(startId.intValue());
            }            
            crit.setMaxResults(numberOfRows);
            
            if (criterions!=null) {
            	Iterator<Criterion> it = criterions.iterator();
            	while (it.hasNext()){
            		crit.add(it.next());
            	}
            }
            
            if (order != null){
            	Iterator<Order> it = order.iterator();
            	while (it.hasNext()) {
            		crit.addOrder(it.next());
            	}
            }
            
            phoneCallList = crit.list();
            
            transaction.commit();
            
        } catch (Exception e) {
        	
        	e.printStackTrace();
        	
        	// close transaction in every case
            transaction.rollback();
            throw new HibernateException("Cannot get phone calls. ", e);
        } 

        return phoneCallList;
    }

    // hibernate session used to access the database.
    private static Session getHibernateSession() {
        if (sessionFactory == null) {
            synchronized (factoryMutex) {
                sessionFactory = new AnnotationConfiguration().configure("/conf/hibernate.cfg.xml").buildSessionFactory();                
            }
        }
        return sessionFactory.getCurrentSession();
    }

	@Override
	public int getRowCount() {
		Session s = getHibernateSession();
		Transaction t = s.beginTransaction();
		try {
			Number count = (Number)s.createCriteria(PhoneCallBean.class).setProjection(Projections.rowCount()).uniqueResult();			
			t.commit();
			
			log.debug(" ### available row count is = " + (count!=null?count.intValue():count));
			return count != null ? count.intValue() : 0;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			t.rollback();
		}
		return -1;
	}

	@Override
	public List<String> getUniquePhoneNo() {
		Session s = getHibernateSession();
		Transaction t = s.beginTransaction();
		
		try {
			// s.createQuery(" phoneNumber from @PhoneCallBean");
			@SuppressWarnings("unchecked")
			List<String> phoneNumbers = (List<String>)s.createCriteria(PhoneCallBean.class).setProjection(
					Projections.distinct(Projections.property("phoneNumber"))).list();
			
			t.commit();
			
			return phoneNumbers;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			t.rollback();
		}
		
		return null;
	}
	
	public List<Object[]> getChartDataForPhoneNumber(String phoneNumber) {
		Session s = getHibernateSession();
		Transaction t = s.beginTransaction();
		/*	select des_phone_number, count(*) from pcall_recs 
			 where phone_number = ':phoneNumber'
			group by des_phone_number  */
		
		try {
			Criteria c = s.createCriteria(PhoneCallBean.class).setProjection(
					Projections.projectionList()
						.add(Projections.groupProperty("desPhoneNumber"))
						.add(Projections.count("callerName"), "callsCount"))							
					.add(Restrictions.eq("phoneNumber", phoneNumber));
			
			/* Result of Hibernate Query with aggregate function is List<Object[]> */
			@SuppressWarnings("unchecked")
			List<Object[]> l = c.list();			
			t.commit();
		
			return l;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
}
