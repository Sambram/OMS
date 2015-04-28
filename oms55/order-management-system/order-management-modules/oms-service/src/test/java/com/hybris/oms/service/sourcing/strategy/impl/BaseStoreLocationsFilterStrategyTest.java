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


import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Sets;


/**
 * Unit test for {@link BaseStoreLocationsFilterStrategy}.
 */
public class BaseStoreLocationsFilterStrategyTest
{

	private final BaseStoreLocationsFilterStrategy baseStoreLocationsFilterStrategy = new BaseStoreLocationsFilterStrategy();

	@Test
	public void shouldFulfilResultLocationIdsFromBaseStore()
	{
		// given
		final Set<String> locationIds = Sets.newHashSet("1", "2");
		final OrderData order = createOrderWithBaseStore(locationIds);

		// when
		final Set<String> filteredLocations = new HashSet<>();
		final Set<String> result = baseStoreLocationsFilterStrategy.filter(order, filteredLocations);

		// then
		Assertions.assertThat(result).isNotEmpty();
		Assertions.assertThat(result).hasSize(2).contains(locationIds.toArray());
		Mockito.verify(order).getBaseStore();
		Mockito.verify(order.getBaseStore(), Mockito.times(2)).getStockroomLocations();
	}

	@Test
	public void shouldFilterAndFulfilResultLocationIdsFromBaseStore()
	{
		// given
		final Set<String> locationIds = Sets.newHashSet("1", "2");
		final OrderData order = createOrderWithBaseStore(locationIds);

		// when
		final Set<String> result = baseStoreLocationsFilterStrategy.filter(order, Collections.singleton("1"));

		// then
		Assertions.assertThat(result).isNotEmpty();
		Assertions.assertThat(result).containsOnly("1");
		Mockito.verify(order).getBaseStore();
		Mockito.verify(order.getBaseStore(), Mockito.times(2)).getStockroomLocations();
	}

	@Test
	public void shouldNotFulfilResultLocationIdsFromEmptyBaseStore()
	{
		// given
		final OrderData order = createOrderWithBaseStore(Collections.<String>emptySet());

		// when
		final Set<String> filteredLocations = new HashSet<>();
		final Set<String> result = baseStoreLocationsFilterStrategy.filter(order, filteredLocations);

		// then
		Assertions.assertThat(result).isNull();
		Mockito.verify(order).getBaseStore();
		Mockito.verify(order.getBaseStore()).getStockroomLocations();
	}

	@Test
	public void shouldNotFulfilResultLocationIdsIfBaseStoreNotProvided()
	{
		// given
		final OrderData order = createOrderWithoutBaseStore();

		// when
		final Set<String> filteredLocations = new HashSet<>();
		final Set<String> result = baseStoreLocationsFilterStrategy.filter(order, filteredLocations);

		// then
		Assertions.assertThat(result).isNull();
		Assertions.assertThat(filteredLocations).isEmpty();
		Mockito.verify(order).getBaseStore();
	}

	private OrderData createOrderWithBaseStore(final Set<String> givenLocationIds)
	{
		final OrderData order = Mockito.mock(OrderData.class);

		final BaseStoreData baseStore = Mockito.mock(BaseStoreData.class);
		final Set<StockroomLocationData> stockroomLocations = new HashSet<>();
		for (final String locationId : givenLocationIds)
		{
			final StockroomLocationData locationData = Mockito.mock(StockroomLocationData.class);
			stockroomLocations.add(locationData);
			Mockito.when(locationData.getLocationId()).thenReturn(locationId);
		}

		Mockito.when(order.getBaseStore()).thenReturn(baseStore);
		Mockito.when(baseStore.getStockroomLocations()).thenReturn(stockroomLocations);

		return order;
	}

	private OrderData createOrderWithoutBaseStore()
	{
		final OrderData order = Mockito.mock(OrderData.class);
		Mockito.when(order.getBaseStore()).thenReturn(null);
		return order;
	}

}
