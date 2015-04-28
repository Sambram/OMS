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
package com.hybris.oms.ui.facade.preference;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.LocalizationConverter;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.sourcing.SourcingService;
import com.hybris.oms.ui.api.preference.LocalizedString;
import com.hybris.oms.ui.api.preference.UiPreferenceFacade;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;


/**
 * Default implementation of {@link UiPreferenceFacade}.
 */
public class DefaultUiPreferenceFacade implements UiPreferenceFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUiPreferenceFacade.class);

	private Converter<TenantPreferenceData, TenantPreference> tenantPreferenceConverter;
	private TenantPreferenceService tenantPreferenceService;
	private LocalizationConverter localizationConverter;
	private SourcingService sourcingService;

	@Override
	@Transactional
	public LocalizedString getLocalizedTenantPreferenceDescription(final String tenantPreferenceKey)
	{
		LOGGER.trace("getTenantPreferenceLocalizedDescription");

		final TenantPreferenceData tenantPreferenceData = this.tenantPreferenceService
				.getTenantPreferenceByKey(tenantPreferenceKey);

		final Map<Locale, Object> localizedObjects = this.localizationConverter.getLocalizedAttributeMap(tenantPreferenceData,
				TenantPreferenceData.DESCRIPTION.getAttribute());

		final Map<Locale, String> descriptions = this.localizationConverter.convertToStringMap(localizedObjects);

		final LocalizedString localizedString = new LocalizedString();
		localizedString.setLocaleStringMap(descriptions);

		return localizedString;
	}

	@Override
	@Transactional
	public TenantPreference updateLocalizedTenantPreferenceDescription(final String tenantPreferenceKey,
			final LocalizedString localizedString)
	{
		LOGGER.trace("updateLocalizedTenantPreferenceDescription");

		final Map<Locale, String> mapOfLocalizedDescriptions = localizedString.getLocaleStringMap();

		final TenantPreferenceData target = this.tenantPreferenceService.getTenantPreferenceByKey(tenantPreferenceKey);

		for (final Entry<Locale, String> mapEntry : mapOfLocalizedDescriptions.entrySet())
		{
			final String value = mapOfLocalizedDescriptions.get(mapEntry.getKey());
			target.setDescription(value, mapEntry.getKey());
		}
		return this.tenantPreferenceConverter.convert(target);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> findAllSourcingSplitOptions()
	{
		return this.sourcingService.findAllSourcingSplitOptions();
	}

	@Override
	@Transactional(readOnly = true)
	public Set<String> findAllSourcingLocationComparators()
	{
		return this.sourcingService.findAllSourcingLocationComparators();
	}

	@Required
	public void setTenantPreferenceConverter(final Converter<TenantPreferenceData, TenantPreference> tenantPreferenceConverter)
	{
		this.tenantPreferenceConverter = tenantPreferenceConverter;
	}

	@Required
	public void setTenantPreferenceService(final TenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

	@Required
	public void setLocalizationConverter(final LocalizationConverter localizationConverter)
	{
		this.localizationConverter = localizationConverter;
	}

	@Required
	public void setSourcingService(final SourcingService sourcingService)
	{
		this.sourcingService = sourcingService;
	}

	protected Converter<TenantPreferenceData, TenantPreference> getTenantPreferenceConverter()
	{
		return tenantPreferenceConverter;
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}

	protected LocalizationConverter getLocalizationConverter()
	{
		return localizationConverter;
	}

	protected SourcingService getSourcingService()
	{
		return sourcingService;
	}

}
