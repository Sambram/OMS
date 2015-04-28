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


import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class TenantPreferencePutIntegrationTest extends RestClientIntegrationTest
{

	private static final String TENANT_PREFERENCE_KEY = "workflow.execution.task.taxInvoice";
	private static final String UPDATED_ATS_ID = "test";
	@Autowired
	private PreferenceFacade preferenceFacade;

	@Test
	public void testUpdateTenantPreferences()
	{
		final TenantPreference tenantPreference = this.preferenceFacade.getTenantPreferenceByKey(TENANT_PREFERENCE_KEY);
		final String previousDefault = tenantPreference.getValue();
		tenantPreference.setValue(UPDATED_ATS_ID);
		try
		{
			this.preferenceFacade.updateTenantPreference(tenantPreference);
			final TenantPreference tenantPreferenceUpdated = this.preferenceFacade.getTenantPreferenceByKey(TENANT_PREFERENCE_KEY);
			Assert.assertEquals(UPDATED_ATS_ID, tenantPreferenceUpdated.getValue());
		}
		finally
		{
			tenantPreference.setValue(previousDefault);
			this.preferenceFacade.updateTenantPreference(tenantPreference);
		}
	}

	@Test
	public void testEnableTenantPreference()
	{
		final TenantPreference tenantPreferenceBeforeDisabled = this.preferenceFacade
				.getTenantPreferenceByKey(TENANT_PREFERENCE_KEY);
		Assert.assertTrue(tenantPreferenceBeforeDisabled.getEnabled());
		this.preferenceFacade.enableTenantPreference(TENANT_PREFERENCE_KEY, false);
		final TenantPreference tenantPreferenceAfterDisabled = this.preferenceFacade
				.getTenantPreferenceByKey(TENANT_PREFERENCE_KEY);
		Assert.assertFalse(tenantPreferenceAfterDisabled.getEnabled());
	}
}
