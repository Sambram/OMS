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
package com.hybris.oms.rest.integration.tenant.preference;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;


/**
 * End-to-end integration tests.
 * This class should contain only methods to retrieve data (GET).
 */
public class TenantPreferenceGetIntegrationTest extends RestClientIntegrationTest
{
	private static final String TENANT_PREFERENCE_KEY = "ats.calculator";
	private static final String TENANT_PREFERENCE_TYPE_ATTRIBUTE1 = "attribute.attribute1";

	@Autowired
	private PreferenceFacade preferenceFacade;

	@Test
	public void testFindAllTenantPreferences()
	{
		final List<TenantPreference> tenantPreferences = this.preferenceFacade.findAllTenantPreferences();
		Assert.assertFalse(tenantPreferences.isEmpty());
	}

	@Test
	public void testFindAllTenantPreferencesByType()
	{
		final List<TenantPreference> tenantPreferences = this.preferenceFacade
				.findAllTenantPreferencesByType(TenantPreferenceConstants.PREF_TYPE_ATTRIBUTES);
		Assert.assertFalse(tenantPreferences.isEmpty());
		Assert.assertEquals(3, tenantPreferences.size());
		Assert.assertEquals(TENANT_PREFERENCE_TYPE_ATTRIBUTE1, tenantPreferences.get(0).getProperty());
	}

	@Test
	public void testGetTenantPreferenceByKey()
	{
		final TenantPreference tenantPreference = this.preferenceFacade.getTenantPreferenceByKey(TENANT_PREFERENCE_KEY);
		Assert.assertEquals(TENANT_PREFERENCE_KEY, tenantPreference.getProperty());
		Assert.assertEquals(TENANT_PREFERENCE_KEY, tenantPreference.getId());
	}

}
