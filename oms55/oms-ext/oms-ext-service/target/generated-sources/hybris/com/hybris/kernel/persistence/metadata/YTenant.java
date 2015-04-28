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
 
package com.hybris.kernel.persistence.metadata;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

  
/**
 * Generated managedobject class for type YTenant first defined at extension <code>kernel-metadata</code>.
 * <p/>
 * .
 */
public interface YTenant extends ManagedObject
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "YTenant";
	
	/** <i>Generated constant</i> - Attribute key of <code>YTenant.name</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YTenant, String> NAME = new AttributeType<>("name");


	/**
	 * <i>Generated method</i> - Getter of the <code>YTenant.name</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	

	/**
	 * <i>Generated method</i> - Setter of <code>YTenant.name</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
}
