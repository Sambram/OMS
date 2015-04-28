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
package com.hybris.oms.rest.web.inventory;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Location;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class StockRoomLocationsTest
{

	@Mock
	private InventoryFacade inventoryServiceApi;
	@InjectMocks
	private StockroomLocationResource stockRoomLocation;

	@InjectMocks
	private StockroomLocationsResource stockRoomLocations;

	@Test
	public void createUpdateStockRoomLocation()
	{
		final Location location = new Location();

		// call tested method
		final Response response = this.stockRoomLocations.createUpdateStockRoomLocation(location);
		// verify if façade was called
		Mockito.verify(this.inventoryServiceApi).createUpdateStockRoomLocation(location);
		// verify if response was OK(200).
		Assert.assertEquals(Response.ok().build().getStatus(), response.getStatus());
	}

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateStockRoomLocation()
	{
		final Location location = new Location();
		Mockito.when(this.inventoryServiceApi.createStockRoomLocation(location)).thenReturn(location);

		final Response response = this.stockRoomLocations.createStockRoomLocation(location);
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(location, response.getEntity());

		Mockito.verify(this.inventoryServiceApi).createStockRoomLocation(location);
	}

	@Test
	public void testFindStockRoomLocationByLocationId()
	{
		final Response response = this.stockRoomLocation.findStockRoomLocationByLocationId("locationId");
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Mockito.verify(this.inventoryServiceApi).getStockRoomLocationByLocationId("locationId");
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateInexistentStockRoomLocation()
	{
		final String locationId = "locationId";
		final Location location = new Location();

		Mockito.doThrow(new EntityNotFoundException("")).when(this.inventoryServiceApi).updateStockRoomLocation(location);

		// call tested method
		this.stockRoomLocation.updateStockRoomLocation(locationId, location);
		Mockito.verify(this.inventoryServiceApi.updateStockRoomLocation(location));
	}

	@Test
	public void updateStockRoomLocation()
	{
		final String locationId = "locationId";
		final Location location = new Location();

		// call tested method
		final Response response = this.stockRoomLocation.updateStockRoomLocation(locationId, location);
		// verify if façade was called
		Mockito.verify(this.inventoryServiceApi).updateStockRoomLocation(location);
		// verify if response was OK(200).
		Assert.assertEquals(Response.ok().build().getStatus(), response.getStatus());
	}
}
