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
package com.hybris.oms.service.sourcing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.builder.SourcingLocationBuilder;
import com.hybris.oms.service.sourcing.builder.SourcingMatrixRowBuilder;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.strategy.LocationsFilterStrategy;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;


public class SourcingMatrixRowBuilderTest
{
	private static final String OL_ID = "ol";
	private static final String ATS_ID = "ats";
	private static final String LOC_ID = "loc";
	private static final String SKU = "SKU";

	private final SourcingLine LINE = new SourcingLine(SKU, 1, OL_ID);

	private static final Long MILLISTIME = null;

	@InjectMocks
	private final SourcingMatrixRowBuilder builder = new SourcingMatrixRowBuilder();
	@Mock
	ItemLocationData itemLocation;
	@Mock
	private LocationsFilterStrategy<SourcingLine, List<StockroomLocationData>> filter;
	@Mock
	private AtsService atsService;
	@Mock
	private InventoryService inventoryService;
	@Mock
	private SourcingLocationBuilder sourcingLocationBuilder;
	@Mock
	private StockroomLocationData location;
	@Mock
	private SourcingLocation sourcingLocation;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnNullRow()
	{
		when(filter.filter(LINE, null)).thenReturn(null);
		assertNull(builder.getMatrixRow(LINE, ATS_ID, null, null, new AtsResult(), null));
	}

	@Test
	public void shouldAddSourcingLocationForAllFilterResults()
	{
		final AtsResult localAtsResult = new AtsResult();

		when(filter.filter(LINE, null)).thenReturn(Collections.singletonList(location));
		when(location.getLocationId()).thenReturn(LOC_ID);
		when(atsService.getLocalAts(argThat(new ArgumentMatcher<Set<String>>()
				{
			@Override
			public boolean matches(final Object argument)
			{
				return argument instanceof Collection && ((Collection<?>) argument).contains(SKU);
			}
				}), argThat(new ArgumentMatcher<Set<String>>()
						{
					@Override
					public boolean matches(final Object argument)
					{
						return argument instanceof Collection && ((Collection<?>) argument).contains(LOC_ID);
					}
						}), argThat(new ArgumentMatcher<Set<String>>()
								{
							@Override
							public boolean matches(final Object argument)
							{
								return argument instanceof Collection && ((Collection<?>) argument).contains(ATS_ID);
							}
								}))).thenReturn(localAtsResult);
		localAtsResult.addResult(LOC_ID, SKU, ATS_ID, 1, MILLISTIME);

		when(sourcingLocationBuilder.build(LINE, location, 1, null, null)).thenReturn(sourcingLocation);
		when(inventoryService.findAllItemLocationsBySkuAndLocation(anyString(), anyString())).thenReturn(
				ImmutableList.of(itemLocation));

		final AtsResult atsResult = new AtsResult();
		final List<SourcingLocation> row = builder.getMatrixRow(LINE, ATS_ID, null, null, atsResult, null);

		verify(location, times(4)).getLocationId();

		assertEquals(1, row.size());
		assertEquals(sourcingLocation, row.get(0));
		assertEquals(Integer.valueOf(1), atsResult.getResult(SKU, ATS_ID, LOC_ID, MILLISTIME));
	}

	@Test
	public void shouldSkipRowWithEmptyAts()
	{
		final AtsResult localAtsResult = new AtsResult();

		when(filter.filter(LINE, null)).thenReturn(Collections.singletonList(location));
		when(location.getLocationId()).thenReturn(LOC_ID);
		when(atsService.getLocalAts(anySetOf(String.class), anySetOf(String.class), anySetOf(String.class))).thenReturn(
				localAtsResult);
		when(inventoryService.findAllItemLocationsBySkuAndLocation(anyString(), anyString())).thenReturn(
				ImmutableList.of(itemLocation));

		final AtsResult atsResult = new AtsResult();
		final List<SourcingLocation> row = builder.getMatrixRow(LINE, ATS_ID, null, null, atsResult, null);

		verify(location, times(2)).getLocationId();

		assertNull(row);
		assertTrue("empty ATS", atsResult.isEmpty());
	}
}
