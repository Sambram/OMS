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

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.sourcing.strategy.impl.StockRoomLocationsFilterStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;


public class StockRoomLocationsFilterTest
{

	private final StockRoomLocationsFilterStrategy filter = new StockRoomLocationsFilterStrategy();

	@Test
	public void shouldFulfilResultLocationIdsFromStockroomLocationIds()
	{
		// given
		final List<String> locationIds = Lists.newArrayList("1", "2");
		final OrderData order = createOrderWithStockroomLocations(locationIds);

		// when
		final Set<String> filteredLocations = new HashSet<>();
		final Set<String> result = filter.filter(order, filteredLocations);

		// then
		Assertions.assertThat(result).hasSize(2).contains(locationIds.toArray());
		Mockito.verify(order).getStockroomLocationIds();
	}

	@Test
	public void shouldFilterAndFulfilResultLocationIdsFromStockroomLocationIds()
	{
		// given
		final List<String> locationIds = Lists.newArrayList("1", "2");
		final OrderData order = createOrderWithStockroomLocations(locationIds);

		// when
		final Set<String> result = filter.filter(order, Collections.singleton("2"));

		// then
		Assertions.assertThat(result).containsOnly("2");
		Mockito.verify(order).getStockroomLocationIds();
	}

	@Test
	public void shouldNotFulfilResultLocationIdsFromEmptyStockroomLocationIds()
	{
		// given
		final OrderData order = createOrderWithStockroomLocations(Collections.<String>emptyList());

		// when
		final Set<String> filteredLocations = new HashSet<>();
		final Set<String> result = filter.filter(order, filteredLocations);

		// then
		Assertions.assertThat(result).isNull();
		Mockito.verify(order).getStockroomLocationIds();
	}

	@Test
	public void shouldNotFulfilResultLocationIdsIfStockroomLocationIdsNotProvided()
	{
		// given
		final OrderData order = createOrderWithoutStockroomLocations();

		// when
		final Set<String> filteredLocations = new HashSet<>();
		final Set<String> result = filter.filter(order, filteredLocations);

		// then
		Assertions.assertThat(result).isNull();
		Assertions.assertThat(filteredLocations).isEmpty();
		Mockito.verify(order).getStockroomLocationIds();
	}

	private OrderData createOrderWithStockroomLocations(final List<String> givenLocationIds)
	{
		final OrderData order = Mockito.mock(OrderData.class);

		Mockito.when(order.getStockroomLocationIds()).thenReturn(givenLocationIds);

		return order;
	}

	private OrderData createOrderWithoutStockroomLocations()
	{
		final OrderData order = Mockito.mock(OrderData.class);
		Mockito.when(order.getStockroomLocationIds()).thenReturn(null);
		return order;
	}

}
