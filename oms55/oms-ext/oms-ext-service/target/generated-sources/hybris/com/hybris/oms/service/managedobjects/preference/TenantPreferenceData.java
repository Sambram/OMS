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
 
package com.hybris.oms.service.managedobjects.preference;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type TenantPreferenceData first defined at extension <code>tenantPreference</code>.
 * <p/>
 * Contains all tenant preference data.
 */
public interface TenantPreferenceData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "TenantPreferenceData";
	
	/** <i>Generated constant</i> - Attribute key of <code>TenantPreferenceData.version</code> attribute defined at extension <code>tenantPreference</code>. */
	AttributeType<TenantPreferenceData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>TenantPreferenceData.description</code> attribute defined at extension <code>tenantPreference</code>. */
	LocalizedAttributeType<TenantPreferenceData, String> DESCRIPTION = new LocalizedAttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>TenantPreferenceData.enabled</code> attribute defined at extension <code>tenantPreference</code>. */
	AttributeType<TenantPreferenceData, Boolean> ENABLED = new AttributeType<>("enabled");
	/** <i>Generated constant</i> - Attribute key of <code>TenantPreferenceData.type</code> attribute defined at extension <code>tenantPreference</code>. */
	AttributeType<TenantPreferenceData, String> TYPE = new AttributeType<>("type");
	/** <i>Generated constant</i> - Attribute key of <code>TenantPreferenceData.property</code> attribute defined at extension <code>tenantPreference</code>. */
	AttributeType<TenantPreferenceData, String> PROPERTY = new AttributeType<>("property");
	/** <i>Generated constant</i> - Attribute key of <code>TenantPreferenceData.value</code> attribute defined at extension <code>tenantPreference</code>. */
	AttributeType<TenantPreferenceData, String> VALUE = new AttributeType<>("value");

	/** <i>Generated constant</i> - Index of <code>TenantPreferenceData</code> type defined at extension <code>tenantPreference</code>. */
	UniqueIndexSingle<TenantPreferenceData, String> UX_TENANTPREFERENCES_PROPERTY = new UniqueIndexSingle<>("UX_tenantPreferences_property", TenantPreferenceData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>TenantPreferenceData.property</code> attribute defined at extension <code>tenantPreference</code>.
	 * <p/>
	 * key name of the preference.
	 * 
	 * @return the property
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getProperty();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TenantPreferenceData.description</code> attribute defined at extension <code>tenantPreference</code>.
	 * <p/>
	 * description of the preference.
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TenantPreferenceData.description</code> attribute defined at extension <code>tenantPreference</code>.
	 * <p/>
	 * description of the preference.
	 * 
	 * @return the description
	 */
	java.lang.String getDescription(final java.util.Locale locale);	
	/**
	 * <i>Generated method</i> - Getter of the <code>TenantPreferenceData.value</code> attribute defined at extension <code>tenantPreference</code>.
	 * <p/>
	 * value of the preference.
	 * 
	 * @return the value
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TenantPreferenceData.type</code> attribute defined at extension <code>tenantPreference</code>.
	 * <p/>
	 * type of the preference.
	 * 
	 * @return the type
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getType();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TenantPreferenceData.enabled</code> attribute defined at extension <code>tenantPreference</code>.
	 * <p/>
	 * Indicated whether the TenantPreference is active.
	 * 
	 * @return the enabled
	 */
	java.lang.Boolean getEnabled();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>TenantPreferenceData.version</code> attribute defined at extension <code>tenantPreference</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>TenantPreferenceData.property</code> attribute defined at extension <code>tenantPreference</code>.  
	 * <p/>
	 * key name of the preference.
	 *
	 * @param value the property
	 */
	void setProperty(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>TenantPreferenceData.description</code> attribute defined at extension <code>tenantPreference</code>.  
	 * <p/>
	 * description of the preference.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>TenantPreferenceData.description</code> attribute defined at extension <code>tenantPreference</code>.  
	 * <p/>
	 * description of the preference.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value, final java.util.Locale locale);	
	/**
	 * <i>Generated method</i> - Setter of <code>TenantPreferenceData.value</code> attribute defined at extension <code>tenantPreference</code>.  
	 * <p/>
	 * value of the preference.
	 *
	 * @param value the value
	 */
	void setValue(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>TenantPreferenceData.type</code> attribute defined at extension <code>tenantPreference</code>.  
	 * <p/>
	 * type of the preference.
	 *
	 * @param value the type
	 */
	void setType(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>TenantPreferenceData.enabled</code> attribute defined at extension <code>tenantPreference</code>.  
	 * <p/>
	 * Indicated whether the TenantPreference is active.
	 *
	 * @param value the enabled
	 */
	void setEnabled(final java.lang.Boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>TenantPreferenceData.version</code> attribute defined at extension <code>tenantPreference</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
