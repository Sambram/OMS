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
package com.hybris.oms.service.sourcing.strategy.impl;


import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.SourcingLine;

import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableSet;


/**
 * Unit test for {@link PickupLocationsFilterStrategy}.
 */
public class PickupLocationsFilterStrategyTest
{

	@Mock
	private InventoryService inventoryService;

	@Mock
	private StockroomLocationData location;

	@InjectMocks
	private final PickupLocationsFilterStrategy filter = new PickupLocationsFilterStrategy();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldFulfillFromPickupStore()
	{
		// given
		final SourcingLine line = new SourcingLine("SKU", 1, "ol2", "loc", null, null);
		Mockito.when(inventoryService.getLocationByLocationId("loc")).thenReturn(location);
		Mockito.when(location.getLocationRoles()).thenReturn(ImmutableSet.of(LocationRole.PICKUP.getCode()));

		// when
		final List<StockroomLocationData> result = filter.filter(line, null);

		// then
		Assertions.assertThat(result).containsOnly(location);
	}


	/**
	 * Should not return the location when the locationRole is not PICKUP
	 */
	@Test
	public void shouldNotFulfillWhenPickUpLocationIdIsNotPickUpStore()
	{
		// given
		final SourcingLine line = new SourcingLine("SKU", 1, "ol2", "loc", null, null);
		Mockito.when(inventoryService.getLocationByLocationId("loc")).thenReturn(location);
		Mockito.when(location.getLocationRoles()).thenReturn(ImmutableSet.of(LocationRole.SHIPPING.getCode()));

		// when
		final List<StockroomLocationData> result = filter.filter(line, null);

		// then
		Assert.assertNull(result);
	}


	@Test
	public void shouldIgnoreRegularLine()
	{
		// given
		final SourcingLine line = new SourcingLine("SKU", 1, "ol2");

		// when
		final List<StockroomLocationData> result = filter.filter(line, null);

		// then
		Assert.assertNull(result);
	}

}
