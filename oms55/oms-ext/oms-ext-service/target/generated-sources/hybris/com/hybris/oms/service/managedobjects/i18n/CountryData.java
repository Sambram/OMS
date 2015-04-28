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
 
package com.hybris.oms.service.managedobjects.i18n;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type CountryData first defined at extension <code>i18n</code>.
 * <p/>
 * Contains data for each country.
 */
public interface CountryData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "CountryData";
	
	/** <i>Generated constant</i> - Attribute key of <code>CountryData.name</code> attribute defined at extension <code>i18n</code>. */
	AttributeType<CountryData, String> NAME = new AttributeType<>("name");
	/** <i>Generated constant</i> - Attribute key of <code>CountryData.version</code> attribute defined at extension <code>i18n</code>. */
	AttributeType<CountryData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>CountryData.code</code> attribute defined at extension <code>i18n</code>. */
	AttributeType<CountryData, String> CODE = new AttributeType<>("code");

	/** <i>Generated constant</i> - Index of <code>CountryData</code> type defined at extension <code>i18n</code>. */
	UniqueIndexSingle<CountryData, String> UX_COUNTRIES_CODE = new UniqueIndexSingle<>("UX_countries_code", CountryData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>CountryData.code</code> attribute defined at extension <code>i18n</code>.
	 * <p/>
	 * Country code.
	 * 
	 * @return the code
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CountryData.name</code> attribute defined at extension <code>i18n</code>.
	 * <p/>
	 * Country name.
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CountryData.version</code> attribute defined at extension <code>i18n</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>CountryData.code</code> attribute defined at extension <code>i18n</code>.  
	 * <p/>
	 * Country code.
	 *
	 * @param value the code
	 */
	void setCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>CountryData.name</code> attribute defined at extension <code>i18n</code>.  
	 * <p/>
	 * Country name.
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>CountryData.version</code> attribute defined at extension <code>i18n</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
