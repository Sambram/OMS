/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
 
package com.hybris.oms.domain;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

  
/**
 * Generated managedobject class for type Emp first defined at extension <code>oms-ext</code>.
 * <p/>
 * .
 */
public interface Emp extends ManagedObject
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "Emp";
	
	/** <i>Generated constant</i> - Attribute key of <code>Emp.empId</code> attribute defined at extension <code>oms-ext</code>. */
	AttributeType<Emp, String> EMPID = new AttributeType<>("empId");
	/** <i>Generated constant</i> - Attribute key of <code>Emp.firstName</code> attribute defined at extension <code>oms-ext</code>. */
	AttributeType<Emp, String> FIRSTNAME = new AttributeType<>("firstName");
	/** <i>Generated constant</i> - Attribute key of <code>Emp.lastName</code> attribute defined at extension <code>oms-ext</code>. */
	AttributeType<Emp, String> LASTNAME = new AttributeType<>("lastName");


	/**
	 * <i>Generated method</i> - Getter of the <code>Emp.empId</code> attribute defined at extension <code>oms-ext</code>.
	 * <p/>
	 * .
	 * 
	 * @return the empId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getEmpId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Emp.firstName</code> attribute defined at extension <code>oms-ext</code>.
	 * <p/>
	 * .
	 * 
	 * @return the firstName
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getFirstName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Emp.lastName</code> attribute defined at extension <code>oms-ext</code>.
	 * <p/>
	 * .
	 * 
	 * @return the lastName
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLastName();
	

	/**
	 * <i>Generated method</i> - Setter of <code>Emp.empId</code> attribute defined at extension <code>oms-ext</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the empId
	 */
	void setEmpId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>Emp.firstName</code> attribute defined at extension <code>oms-ext</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the firstName
	 */
	void setFirstName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>Emp.lastName</code> attribute defined at extension <code>oms-ext</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the lastName
	 */
	void setLastName(final java.lang.String value);
	
}
