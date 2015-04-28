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
package com.hybris.oms.ui.api.preference;

import java.util.Locale;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.hybris.commons.dto.Dto;
import com.hybris.oms.ui.api.preference.adapter.LocalizedStringAdapter;


/**
 * DTO containing a map of localized descriptions.
 * 
 */
@XmlRootElement
public class LocalizedString implements Dto
{
	private static final long serialVersionUID = -4214962076423625726L;
	private Map<Locale, String> localeStringMap;

	/**
	 * Get Localized Descriptions of a Tenant Preference by key.
	 * 
	 * @return A Response object containing a map of localized descriptions..
	 */
	@XmlJavaTypeAdapter(LocalizedStringAdapter.class)
	public Map<Locale, String> getLocaleStringMap()
	{
		return localeStringMap;
	}

	/**
	 * Get Localized Descriptions of a Tenant Preference by key.
	 * 
	 * @param localeStringMap
	 */
	public void setLocaleStringMap(final Map<Locale, String> localeStringMap)
	{
		this.localeStringMap = localeStringMap;
	}


}
