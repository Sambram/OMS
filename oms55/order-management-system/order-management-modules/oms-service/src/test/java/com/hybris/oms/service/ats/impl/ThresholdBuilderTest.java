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
package com.hybris.oms.service.ats.impl;


import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.preference.TenantPreferenceService;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Mock test for {@link ThresholdBuilder}.
 */
public class ThresholdBuilderTest
{
	private static final String LOC_ID = "1";

	@Mock
	private InventoryService inventoryService;

	@Mock
	private TenantPreferenceService preferenceService;

	@Mock
	private StockroomLocationData location;

	@Mock
	private TenantPreferenceData pref;

	@InjectMocks
	private final ThresholdBuilder builder = new ThresholdBuilder();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(this.inventoryService.findLocationsByLocationIds(Mockito.anySetOf(String.class))).thenReturn(
				Collections.singletonList(this.location));
		Mockito.when(this.location.getLocationId()).thenReturn(LOC_ID);
	}

	@Test
	public void shouldRetrieveGlobal()
	{
		Mockito
				.when(this.preferenceService.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_ATS_GLOBAL_THRHOLD))
				.thenReturn(this.pref);
		Mockito.when(this.pref.getValue()).thenReturn("1");
		final Threshold threshold = this.builder.getGlobalThreshold();
		Assert.assertEquals(1, threshold.getValue());
		Assert.assertFalse(threshold.isPercentage());
	}

	@Test
	public void shouldIgnoreMissingGlobalPreference()
	{
		Mockito
				.when(this.preferenceService.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_ATS_GLOBAL_THRHOLD))
				.thenReturn(null);
		final Threshold threshold = this.builder.getGlobalThreshold();
		Assert.assertEquals(0, threshold.getValue());
		Assert.assertFalse(threshold.isPercentage());
	}

	@Test
	public void shouldIgnoreInvalidGlobalPreference()
	{
		Mockito
				.when(this.preferenceService.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_ATS_GLOBAL_THRHOLD))
				.thenReturn(this.pref);
		Mockito.when(this.pref.getValue()).thenReturn("A");
		final Threshold threshold = this.builder.getGlobalThreshold();
		Assert.assertEquals(0, threshold.getValue());
		Assert.assertFalse(threshold.isPercentage());
	}

	@Test
	public void shouldRetrieveLocalAbsolute()
	{
		Mockito.when(this.location.getAbsoluteInventoryThreshold()).thenReturn(Integer.valueOf(1));
		Mockito.when(this.location.isUsePercentageThreshold()).thenReturn(Boolean.FALSE);
		final LocationThreshold result = this.builder.getLocalThreshold(Collections.singleton(LOC_ID));
		Assert.assertEquals(99, result.calculateAtsWithThreshold(100, LOC_ID));
	}

	@Test
	public void shouldRetrieveLocalPercentage()
	{
		Mockito.when(this.location.getPercentageInventoryThreshold()).thenReturn(Integer.valueOf(10));
		Mockito.when(this.location.isUsePercentageThreshold()).thenReturn(Boolean.TRUE);
		final LocationThreshold result = this.builder.getLocalThreshold(Collections.singleton(LOC_ID));
		Assert.assertEquals(9, result.calculateAtsWithThreshold(10, LOC_ID));
	}

	@Test
	public void shouldIgnoreInvalidPercentage()
	{
		Mockito.when(this.location.getPercentageInventoryThreshold()).thenReturn(Integer.valueOf(-1));
		Mockito.when(this.location.isUsePercentageThreshold()).thenReturn(Boolean.TRUE);
		final LocationThreshold result = this.builder.getLocalThreshold(Collections.singleton(LOC_ID));
		Assert.assertEquals(10, result.calculateAtsWithThreshold(10, LOC_ID));
	}

}
