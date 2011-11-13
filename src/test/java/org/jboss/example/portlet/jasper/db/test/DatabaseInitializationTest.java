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
package org.jboss.example.portlet.jasper.db.test;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.example.portlet.jasper.db.InitDbHelper;
import org.jboss.example.portlet.jasper.db.PhoneCallHibernateService;
import org.jboss.example.portlet.jasper.db.ServiceFactory;

/**
 * @author jjamrich
 *
 */
public class DatabaseInitializationTest extends TestCase {
	
	private static final Log log = LogFactory.getLog(DatabaseInitializationTest.class);
	
	private PhoneCallHibernateService hibernateService = ServiceFactory.getPHibernateServiceInstance();
	
	/**
     * Initialisation of the phone call DB (30 rows) with some predefined callers. Times and durations are randomly generated.
     */
    public void testWithInitPhoneCallDB() {
        log.info("Calls DB initialisation.");
        try {
        	InitDbHelper.initDb(hibernateService, InitDbHelper.RECORDS_COUNT);
        } catch (Exception ex) {
            log.error("Problem with default phone call DB initialisation", ex);
        }
    }

}
