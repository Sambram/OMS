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
package com.hybris.oms.facade.ats;

import com.hybris.commons.conversion.Converter;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.ats.AtsQuantity;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsResult.AtsRow;
import com.hybris.oms.service.ats.AtsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Mock-based {@link DefaultAtsFacade} tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAtsFacadeMockTest
{
	@InjectMocks
	private final DefaultAtsFacade atsFacade = new DefaultAtsFacade();
	@Mock
	private InventoryFacade inventoryFacade;
	@Mock
	private Validator<AtsRequest> atsRequestValidator;
	@Mock
	private AtsService atsService;
	@SuppressWarnings("PMD.UnusedPrivateField")
	@Mock
	private Converter<AtsResult, List<AtsLocalQuantities>> atsLocalQuantitiesConverter;
	@SuppressWarnings("PMD.UnusedPrivateField")
	@Mock
	private Converter<AtsRow, AtsQuantity> atsQuantityConverter;

	/**
	 * Test {@link AtsFacade#findAtsByBaseStore(Set, Set, String, Set, Set)} method.
	 * Verifies if paged list of locations are assembled into one list and passed into AtsService for further processing.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void testFindAtsByBaseStore()
	{
		final List<Location> locations0 = new ArrayList<>();
		for (int i = 0; i < 100; i++)
		{
			final Location mockedLocation = mock(Location.class);
			when(mockedLocation.getLocationId()).thenReturn("location" + i);
			locations0.add(mockedLocation);
		}

		final List<Location> locations1 = new ArrayList<>();
		for (int i = 0; i < 100; i++)
		{
			final Location mockedLocation = mock(Location.class);
			when(mockedLocation.getLocationId()).thenReturn("location" + (i + 100));
			locations1.add(mockedLocation);
		}

		final List<Location> locations2 = new ArrayList<>();
		for (int i = 0; i < 100; i++)
		{
			final Location mockedLocation = mock(Location.class);
			when(mockedLocation.getLocationId()).thenReturn("location" + (i + 200));
			locations2.add(mockedLocation);
		}

		final Pageable<Location> page0 = mock(Pageable.class);
		final Pageable<Location> page1 = mock(Pageable.class);
		final Pageable<Location> page2 = mock(Pageable.class);

		when(page0.getTotalPages()).thenReturn(3);

		when(page0.getResults()).thenReturn(locations0);
		when(page1.getResults()).thenReturn(locations1);
		when(page2.getResults()).thenReturn(locations2);

		when(this.inventoryFacade.findStockRoomLocationsByQuery(Mockito.any(LocationQueryObject.class))).thenReturn(page0)
				.thenReturn(page1).thenReturn(page2);

		final AtsResult mockedAtsResult = mock(AtsResult.class);
		when(mockedAtsResult.getResults()).thenReturn(Collections.EMPTY_LIST);

		when(this.atsService.getLocalAts(Mockito.any(Set.class), Mockito.any(Set.class), Mockito.any(Set.class)))
				.thenReturn(mockedAtsResult);


		final HashSet<String> countryCodes = new HashSet<String>();
		countryCodes.add("country");

		// Call the method
		this.atsFacade.findAtsByBaseStore(Collections.EMPTY_SET, Collections.EMPTY_SET, "baseStore", countryCodes,
				Collections.EMPTY_SET);


		// Asserts
		Mockito.verify(this.inventoryFacade, Mockito.times(3))
				.findStockRoomLocationsByQuery(Mockito.any(LocationQueryObject.class));

		final Set<String> locationsId = new HashSet<>();
		for (final Location loc : locations0)
		{
			locationsId.add(loc.getLocationId());
		}
		for (final Location loc : locations1)
		{
			locationsId.add(loc.getLocationId());
		}
		for (final Location loc : locations2)
		{
			locationsId.add(loc.getLocationId());
		}

		Mockito.verify(this.atsService).getLocalAts(Collections.EMPTY_SET, locationsId, Collections.EMPTY_SET);
		Mockito.verify(atsRequestValidator).validate(Mockito.anyString(), Mockito.any(AtsRequest.class),
				Mockito.any(FailureHandler.class));
	}
}
