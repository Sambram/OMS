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
package com.hybris.oms.facade.preference;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.preference.TenantPreferenceService;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DefaultPreferenceFacadeMockTest
{
	public static final String TENANT_PREFERENCE_TYPE_1 = "type1";
	@InjectMocks
	private final DefaultPreferenceFacade preferenceApi = new DefaultPreferenceFacade();

	@Mock
	private TenantPreferenceService tenantPreferenceService;

	@Mock
	private TenantPreferenceData tenantPreferenceData1;

	@Mock
	private TenantPreferenceData tenantPreferenceData2;

	@Mock
	private Converter<TenantPreferenceData, TenantPreference> tenantPreferenceConverter;

	@Mock
	private final Converters converters = new Converters();

	@Before
	public void initializeMocks()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindAllTenantPreferences()
	{
		// given
		final List<TenantPreferenceData> tenantPreferencesData =
				ImmutableList.of(this.tenantPreferenceData1,
				this.tenantPreferenceData2);
		when(this.tenantPreferenceService.findAllTenantPreferences()).thenReturn(tenantPreferencesData);

		final List<TenantPreference> stubTenantPreferences = Arrays.asList(new TenantPreference(), new TenantPreference());
		when(this.converters.convertAll(tenantPreferencesData, this.tenantPreferenceConverter)).thenReturn(
				stubTenantPreferences);

		// when
		final List<TenantPreference> tenantPreferences = this.preferenceApi.findAllTenantPreferences();


		//then
		assertNotNull(tenantPreferences);
		assertThat(tenantPreferences.size(), equalTo(2));
		assertThat(tenantPreferences, equalTo(stubTenantPreferences));
		verify(this.tenantPreferenceService).findAllTenantPreferences();
	}

	@Test
	public void testFindAllTenantPreferencesByType()
	{
		//given
		final List<TenantPreferenceData> tenantPreferencesData =
				ImmutableList.of(this.tenantPreferenceData1,	this.tenantPreferenceData2);
		when(this.tenantPreferenceService.findAllTenantPreferencesByType(TENANT_PREFERENCE_TYPE_1)).
				thenReturn(tenantPreferencesData);
		final List<TenantPreference> stubTenantPreferences =
				Arrays.asList(new TenantPreference(), new TenantPreference());
		when(this.converters.convertAll(tenantPreferencesData, this.tenantPreferenceConverter)).
				thenReturn(stubTenantPreferences);

		//when
		final List<TenantPreference> tenantPreferences =
				this.preferenceApi.findAllTenantPreferencesByType(TENANT_PREFERENCE_TYPE_1);

		//then
		assertNotNull(tenantPreferences);
		assertThat(tenantPreferences.size(), equalTo(2));
		assertThat(tenantPreferences, equalTo(stubTenantPreferences));
		verify(this.tenantPreferenceService).findAllTenantPreferencesByType(TENANT_PREFERENCE_TYPE_1);
	}

	@Test
	public void testGetTenantPreferenceByKey()
	{
		//given
		when(this.tenantPreferenceService.getTenantPreferenceByKey("key1")).
				thenReturn(this.tenantPreferenceData1);
		final TenantPreference stubTenantPreference = new TenantPreference();
		when(this.tenantPreferenceConverter.convert(this.tenantPreferenceData1)).
				thenReturn(stubTenantPreference);

		//when
		final TenantPreference tenantPreference = this.preferenceApi.getTenantPreferenceByKey("key1");

		//then
		assertThat(tenantPreference, equalTo(stubTenantPreference));
		verify(this.tenantPreferenceService).getTenantPreferenceByKey("key1");
		verify(this.tenantPreferenceConverter, Mockito.times(1)).convert(this.tenantPreferenceData1);
	}
}
