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


import java.util.Set;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.preference.TenantPreference;


/**
 * The Interface for internal usage of TenantPreferenceServices Api.
 */
public interface UiPreferenceFacade
{
	
	/**
	 * Find all Localized descriptions of a TenantPreference.
	 * 
	 * @category OMS-UI
	 * 
	 * @param tenantPreferenceKey the key to a tenant preference
	 * @return LocalizedString containing a Map<Locale,String>
	 * @throws EntityNotFoundException - if the tenant preference key does not exist
	 */
	LocalizedString getLocalizedTenantPreferenceDescription(final String tenantPreferenceKey) throws EntityNotFoundException;

	/**
	 * Updates localized descriptions of a TenantPreference.
	 * 
	 * @category OMS-UI
	 * 
	 * @param tenantPreferenceKey the key to a tenant preference
	 * @param mapOfLocalizedDescriptions map of localized descriptions
	 * @return the updated {@link TenantPreference}
	 * @throws EntityNotFoundException - if the tenant preference key does not exist
	 */
	TenantPreference updateLocalizedTenantPreferenceDescription(final String tenantPreferenceKey,
			final LocalizedString mapOfLocalizedDescriptions) throws EntityNotFoundException;
	
	/**
	 * Find all options available for splitting orders and order lines during sourcing.
	 * 
	 * @category OMS-UI
	 * 
	 * @return list of String representing available splitting options
	 */
	Set<String> findAllSourcingSplitOptions();
	
	/**
	 * Find all options available for comparing locations during sourcing.
	 * 
	 * @category OMS-UI
	 * 
	 * @return list of String representing available sourcing location comparators
	 */
	Set<String> findAllSourcingLocationComparators();

}
