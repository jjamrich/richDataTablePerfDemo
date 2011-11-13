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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent EJB for phone call data
 * @author <a href="mailto:vrockai@redhat.com">Viliam Rockai</a>
 */
@Entity
@Table(name = "pcall_recs")
public class PhoneCallBean implements Serializable {

    /** Id */
	private static final long serialVersionUID = 2083645952301200618L;
	
	private Long id;
    private String phone_number;
    private String des_phone_number;
    // private Long call_start;
    private Date call_start;
    // private Long call_end;
    private Date call_end;
    private String caller_name;
    
    public String toString(){
    	StringBuffer sb = new StringBuffer();
    	sb.append("PhoneCallBean[")
    	.append("id=").append(id)
    	//.append(", phoneNumber=").append(phone_number)
    	//.append(", callerName=").append(caller_name)
    	//.append(", desPhoneNumber=").append(des_phone_number)
    	//.append(", callStart=").append(call_start)
    	//.append(", callEnd=").append(call_end)
    	.append("]");
    	return sb.toString();
    }

    @Basic
    @Column(name = "call_end")
    public Date getCallEnd() {
        return call_end;
    }

    public void setCallEnd(Date call_end) {
        this.call_end = call_end;
    }

    @Basic
    @Column(name = "call_start")
    public Date getCallStart() {
        return call_start;
    }

    public void setCallStart(Date call_start) {
        this.call_start = call_start;
    }

    @Basic
    @Column(name = "caller_name")
    public String getCallerName() {
        return caller_name;
    }

    public void setCallerName(String caller_name) {
        this.caller_name = caller_name;
    }

    @Basic
    @Column(name = "des_phone_number")
    public String getDesPhoneNumber() {
        return des_phone_number;
    }

    public void setDesPhoneNumber(String des_phone_number) {
        this.des_phone_number = des_phone_number;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
