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
package org.jboss.example.portlet.jasper.db;

import java.util.Date;
import java.util.Random;

/**
 * @author jjamrich
 *
 */
public class InitDbHelper {
	
	/** Records count we want add to DB */
	public static final int RECORDS_COUNT = 30;
	
	public static final void initDb(PhoneCallHibernateService hibernateService, int maxRecords) throws Exception{

        String[] uids = {"vrockai", "mvanco", "mposolda", "pjha", "jjamrich", "mcupak"};
        String[] pnms = {"775983321", "112394839", "883047384", "993485234", "334332349", "443990493"};

        // List<PhoneCallBean> pcl = hibernateService.getPhoneCallList();
        for (int i = 0; i < maxRecords; i++) {
            PhoneCallBean pcb = new PhoneCallBean();
            Random generator = new Random();

            int uid1 = generator.nextInt(uids.length);

            int uid2 = uid1;
            do {
                uid2 = generator.nextInt(uids.length);
            } while (uid1 == uid2);

            long ph_s = System.currentTimeMillis() - generator.nextInt(1000000);
            long ph_e = ph_s + generator.nextInt(1000000);

            pcb.setCallerName(uids[uid1]);
            pcb.setCallStart(new Date(ph_s));
            pcb.setCallEnd(new Date(ph_e));
            pcb.setPhoneNumber(pnms[uid1]);
            pcb.setDesPhoneNumber(pnms[uid2]);

            hibernateService.savePhoneCall(pcb);
        }
	}

}
