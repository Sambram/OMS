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
package com.hybris.oms.rest.integration.ui.preference;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;
import com.hybris.oms.ui.api.preference.LocalizedString;
import com.hybris.oms.ui.api.preference.UiPreferenceFacade;


/**
 * End-to-end integration tests for {@link UiPreferenceFacade}.
 * This class should contain only methods to retrieve data (GET).
 */
public class UIPreferenceIntegrationTest extends RestClientIntegrationTest
{
	private static final String TENANT_PREFERENCE_KEY_ATTRIBUTE = "attribute.attribute1";
	private static final String ENGLISH_DESCRIPTION = "I am english";
	private static final String FRENCH_DESCRIPTION = "Je suis francais";
	private static final String TENANT_PREFERENCE_KEY_ATS = "ats.calculator";
	
	@Autowired
	private UiPreferenceFacade uiPreferenceFacade;

	@Test
	public void testGetLocalizedTenantPreferenceDescription()
	{
		if (this.uiPreferenceFacade != null)
		{
			final LocalizedString localizedDescriptions = this.uiPreferenceFacade
					.getLocalizedTenantPreferenceDescription(TENANT_PREFERENCE_KEY_ATS);
			Assert.assertNotNull(localizedDescriptions);
			Assert.assertNotNull(localizedDescriptions.getLocaleStringMap());
			Assert.assertFalse(localizedDescriptions.getLocaleStringMap().get(Locale.ENGLISH).isEmpty());
		}
	}
	
	@Test
	public void shouldFindAllSourcingSplitOptions()
	{
		final Collection<String> options = this.uiPreferenceFacade.findAllSourcingSplitOptions();
		Assert.assertEquals(3, options.size());
	}
	
	@Test
	public void shouldFindAllSourcingLocationComparators()
	{
		final Collection<String> options = this.uiPreferenceFacade.findAllSourcingLocationComparators();
		Assert.assertEquals(3, options.size());
	}
	
	@Test
	public void testUpdateLocalizedTenantPreferenceDescription()
	{
		final LocalizedString localizedStringPrevious = new LocalizedString();
		final LocalizedString localizedStringNew = new LocalizedString();

		final String previousEnglishDescription = this.uiPreferenceFacade
				.getLocalizedTenantPreferenceDescription(TENANT_PREFERENCE_KEY_ATTRIBUTE).getLocaleStringMap().get(Locale.ENGLISH);
		final String previousFrenchDescription = this.uiPreferenceFacade
				.getLocalizedTenantPreferenceDescription(TENANT_PREFERENCE_KEY_ATTRIBUTE).getLocaleStringMap().get(Locale.FRENCH);

		final Map<Locale, String> previousMap = new HashMap<Locale, String>();
		previousMap.put(Locale.ENGLISH, previousEnglishDescription);
		previousMap.put(Locale.FRENCH, previousFrenchDescription);
		localizedStringPrevious.setLocaleStringMap(previousMap);

		try
		{
			final Map<Locale, String> newMap = new HashMap<Locale, String>();
			newMap.put(Locale.ENGLISH, ENGLISH_DESCRIPTION);
			newMap.put(Locale.FRENCH, FRENCH_DESCRIPTION);
			localizedStringNew.setLocaleStringMap(newMap);

			this.uiPreferenceFacade.updateLocalizedTenantPreferenceDescription(TENANT_PREFERENCE_KEY_ATTRIBUTE, localizedStringNew);

			final String newEnglishDescription = this.uiPreferenceFacade
					.getLocalizedTenantPreferenceDescription(TENANT_PREFERENCE_KEY_ATTRIBUTE).getLocaleStringMap().get(Locale.ENGLISH);
			final String newFrenchDescription = this.uiPreferenceFacade
					.getLocalizedTenantPreferenceDescription(TENANT_PREFERENCE_KEY_ATTRIBUTE).getLocaleStringMap().get(Locale.FRENCH);

			Assert.assertEquals(newEnglishDescription, ENGLISH_DESCRIPTION);
			Assert.assertEquals(newFrenchDescription, FRENCH_DESCRIPTION);
		}
		finally
		{
			this.uiPreferenceFacade.updateLocalizedTenantPreferenceDescription(TENANT_PREFERENCE_KEY_ATTRIBUTE, localizedStringPrevious);
		}
	}
}
