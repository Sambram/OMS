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
 * Generated managedobject class for type YSchemalessAttribute first defined at extension <code>kernel-metadata</code>.
 * <p/>
 * .
 */
public interface YSchemalessAttribute extends ManagedObject
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "YSchemalessAttribute";
	
	/** <i>Generated constant</i> - Attribute key of <code>YSchemalessAttribute.name</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YSchemalessAttribute, String> NAME = new AttributeType<>("name");
	/** <i>Generated constant</i> - Attribute key of <code>YSchemalessAttribute.localized</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YSchemalessAttribute, Boolean> LOCALIZED = new AttributeType<>("localized");
	/** <i>Generated constant</i> - Attribute key of <code>YSchemalessAttribute.parentTypeCode</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YSchemalessAttribute, String> PARENTTYPECODE = new AttributeType<>("parentTypeCode");
	/** <i>Generated constant</i> - Attribute key of <code>YSchemalessAttribute.type</code> attribute defined at extension <code>kernel-metadata</code>. */
	AttributeType<YSchemalessAttribute, String> TYPE = new AttributeType<>("type");

	/** <i>Generated constant</i> - Index of <code>YSchemalessAttribute</code> type defined at extension <code>kernel-metadata</code>. */
	UniqueIndexDiadic<YSchemalessAttribute, String, String> UNIQUE_IDX_YSCHEMALESS = new UniqueIndexDiadic<>("UNIQUE_IDX_YSCHEMALESS", YSchemalessAttribute.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>YSchemalessAttribute.parentTypeCode</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the parentTypeCode
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getParentTypeCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YSchemalessAttribute.name</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YSchemalessAttribute.type</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the type
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getType();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>YSchemalessAttribute.localized</code> attribute defined at extension <code>kernel-metadata</code>.
	 * <p/>
	 * .
	 * 
	 * @return the localized
	 */
	java.lang.Boolean getLocalized();
	

	/**
	 * <i>Generated method</i> - Setter of <code>YSchemalessAttribute.parentTypeCode</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the parentTypeCode
	 */
	void setParentTypeCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YSchemalessAttribute.name</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YSchemalessAttribute.type</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the type
	 */
	void setType(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>YSchemalessAttribute.localized</code> attribute defined at extension <code>kernel-metadata</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the localized
	 */
	void setLocalized(final java.lang.Boolean value);
	
}
