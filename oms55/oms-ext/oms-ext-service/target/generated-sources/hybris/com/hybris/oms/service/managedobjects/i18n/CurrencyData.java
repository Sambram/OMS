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
 * Generated managedobject class for type CurrencyData first defined at extension <code>i18n</code>.
 * <p/>
 * Contains data for each currency.
 */
public interface CurrencyData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "CurrencyData";
	
	/** <i>Generated constant</i> - Attribute key of <code>CurrencyData.version</code> attribute defined at extension <code>i18n</code>. */
	AttributeType<CurrencyData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>CurrencyData.name</code> attribute defined at extension <code>i18n</code>. */
	AttributeType<CurrencyData, String> NAME = new AttributeType<>("name");
	/** <i>Generated constant</i> - Attribute key of <code>CurrencyData.code</code> attribute defined at extension <code>i18n</code>. */
	AttributeType<CurrencyData, String> CODE = new AttributeType<>("code");

	/** <i>Generated constant</i> - Index of <code>CurrencyData</code> type defined at extension <code>i18n</code>. */
	UniqueIndexSingle<CurrencyData, String> UX_CURRENCIES_CODE = new UniqueIndexSingle<>("UX_currencies_code", CurrencyData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>CurrencyData.code</code> attribute defined at extension <code>i18n</code>.
	 * <p/>
	 * Currency code.
	 * 
	 * @return the code
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CurrencyData.name</code> attribute defined at extension <code>i18n</code>.
	 * <p/>
	 * Currency name.
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CurrencyData.version</code> attribute defined at extension <code>i18n</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>CurrencyData.code</code> attribute defined at extension <code>i18n</code>.  
	 * <p/>
	 * Currency code.
	 *
	 * @param value the code
	 */
	void setCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>CurrencyData.name</code> attribute defined at extension <code>i18n</code>.  
	 * <p/>
	 * Currency name.
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>CurrencyData.version</code> attribute defined at extension <code>i18n</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
