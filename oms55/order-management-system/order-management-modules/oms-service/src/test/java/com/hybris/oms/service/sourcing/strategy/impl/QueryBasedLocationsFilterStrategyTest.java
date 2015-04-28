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


import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.SourcingLine;

import java.util.Collections;
import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link QueryBasedLocationsFilterStrategy}.
 */
public class QueryBasedLocationsFilterStrategyTest
{

	@Mock
	private InventoryService inventoryService;

	@Mock
	private StockroomLocationData location;

	@InjectMocks
	private final QueryBasedLocationsFilterStrategy filter = new QueryBasedLocationsFilterStrategy();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldQueryForLocation()
	{
		// given
		final SourcingLine line = new SourcingLine("SKU", 1, "ol2", null, Collections.singleton("SHIPPING"), "DE");
		Mockito.when(inventoryService.findAllLocationsByQueryObject(Mockito.any(LocationQueryObject.class), Mockito.eq(false)))
				.thenReturn(Collections.singletonList(location));

		// when
		final List<StockroomLocationData> result = filter.filter(line, Collections.singleton("loc"));
		Mockito.verify(inventoryService).findAllLocationsByQueryObject(Mockito.argThat(new ArgumentMatcher<LocationQueryObject>()
		{

			@Override
			public boolean matches(final Object query)
			{
				return query instanceof LocationQueryObject && ((LocationQueryObject) query).getLocationIds().contains("loc")
						&& ((LocationQueryObject) query).getLocationRoles().contains("SHIPPING")
						&& ((LocationQueryObject) query).getCountries().contains("DE");
			}
		}), Mockito.eq(false));

		// then
		Assertions.assertThat(result).containsOnly(location);
	}

}
