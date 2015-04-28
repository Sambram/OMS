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
 * Generated managedobject class for type YTenantProperty first defined at extension <code>kernel-metadata</code>.
 * <p/>
 * .
 */
public interface YTenantProperty extends ManagedObject
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "YTenantProperty";
	
	/** <i>Generated constant</i> - Attribute key of <code>YTenantProperty.value</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YTenantProperty, String> VALUE = new AttributeType<>("value");
	/** <i>Generated constant</i> - Attribute key of <code>YTenantProperty.key</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YTenantProperty, String> KEY = new AttributeType<>("key");


	/**
	 * <i>Generated method</i> - Getter of the <code>YTenantProperty.key</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the key
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getKey();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YTenantProperty.value</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the value
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getValue();
	

	/**
	 * <i>Generated method</i> - Setter of <code>YTenantProperty.key</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the key
	 */
	void setKey(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YTenantProperty.value</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the value
	 */
	void setValue(final java.lang.String value);
	
}
