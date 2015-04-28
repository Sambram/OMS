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
 * Generated managedobject class for type DummyObjectData first defined at extension <code>oms-ext</code>.
 * <p/>
 * .
 */
public interface DummyObjectData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "DummyObjectData";
	
	/** <i>Generated constant</i> - Attribute key of <code>DummyObjectData.dummyProperty</code> attribute defined at extension <code>oms-ext</code>. */
	AttributeType<DummyObjectData, String> DUMMYPROPERTY = new AttributeType<>("dummyProperty");
	/** <i>Generated constant</i> - Attribute key of <code>DummyObjectData.name</code> attribute defined at extension <code>oms-ext</code>. */
	AttributeType<DummyObjectData, String> NAME = new AttributeType<>("name");

	/** <i>Generated constant</i> - Index of <code>DummyObjectData</code> type defined at extension <code>oms-ext</code>. */
	UniqueIndexSingle<DummyObjectData, String> UX_DUMMYOBJECTDATA_NAME = new UniqueIndexSingle<>("UX_DummyObjectData_name", DummyObjectData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>DummyObjectData.name</code> attribute defined at extension <code>oms-ext</code>.
	 * <p/>
	 * .
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DummyObjectData.dummyProperty</code> attribute defined at extension <code>oms-ext</code>.
	 * <p/>
	 * .
	 * 
	 * @return the dummyProperty
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDummyProperty();
	

	/**
	 * <i>Generated method</i> - Setter of <code>DummyObjectData.name</code> attribute defined at extension <code>oms-ext</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>DummyObjectData.dummyProperty</code> attribute defined at extension <code>oms-ext</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the dummyProperty
	 */
	void setDummyProperty(final java.lang.String value);
	
}
