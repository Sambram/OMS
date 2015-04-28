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
package com.hybris.oms.ui.rest.client.preference;

import java.util.Set;

import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.domain.JaxbBaseSet;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.rest.client.web.DefaultRestClient;
import com.hybris.oms.ui.api.preference.LocalizedString;
import com.hybris.oms.ui.api.preference.UiPreferenceFacade;
import com.sun.jersey.api.client.GenericType;


/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */


/**
 * The internal TenantPreference rest client.
 */
public class UiPreferenceRestClient extends DefaultRestClient implements UiPreferenceFacade
{
			
	/**
	 * Gets the tenant preference by key.
	 * 
	 * @param tenantPreferenceKey
	 *           the tenant preference key
	 * @return the LocalizedString containing a map<locale,String>
	 */
	@Override
	public LocalizedString getLocalizedTenantPreferenceDescription(final String tenantPreferenceKey)
	{
		try
		{
			return getClient().call("tenantPreferences/%s/descriptions", tenantPreferenceKey).get(new GenericType<LocalizedString>()
			{// emtpy
					}).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	/**
	 * Gets the tenant preference by key.
	 * 
	 * @param tenantPreferenceKey
	 *           the tenant preference key
	 * @param mapOfLocalizedDescriptions
	 *           the map of localized descriptions
	 */
	@Override
	public TenantPreference updateLocalizedTenantPreferenceDescription(final String tenantPreferenceKey,
			final LocalizedString mapOfLocalizedDescriptions)
	{
		try
		{
			return getClient().call("tenantPreferences/%s/descriptions", tenantPreferenceKey)
					.put(TenantPreference.class, mapOfLocalizedDescriptions).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> findAllSourcingSplitOptions() 
	{
		try
		{
			final JaxbBaseSet<String> optionsSet = getClient().call("tenantPreferences/splitoptions").get(JaxbBaseSet.class).result();
			return optionsSet.getSet();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> findAllSourcingLocationComparators() 
	{
		try
		{
			final JaxbBaseSet<String> optionsSet = getClient().call("tenantPreferences/locationcomparators").get(JaxbBaseSet.class).result();
			return optionsSet.getSet();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap();
		}
	}
}
