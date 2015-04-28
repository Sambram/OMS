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
package com.hybris.oms.rest.integration.inventory;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * End-to-end integration tests.
 * This class should contain only methods to retrieve data (GET).
 */
public class StockroomLocationGetIntegrationTest extends RestClientIntegrationTest
{
	@Autowired
	private InventoryFacade inventoryFacade;

	@Test
	public void testGetLocationByLocationId()
	{
		final String locationId = this.generateLocationId();
		final Location location = this.buildLocation(locationId);
		this.inventoryFacade.createStockRoomLocation(location);
		final Location createdLocation = this.inventoryFacade.getStockRoomLocationByLocationId(locationId);
		Assert.assertNotNull(createdLocation);
		Assert.assertEquals(locationId, createdLocation.getLocationId());
		Assert.assertEquals(locationId, createdLocation.getId());
	}

	@Test
	public void testFindPageableStockroomLocations()
	{
		final String locationId = this.generateLocationId();
		final String locationId2 = this.generateLocationId();

		// Create location for test
		final Location location1 = this.buildLocation(locationId);
		final Location location2 = this.buildLocation(locationId2);

		// Add location via REST
		this.inventoryFacade.createStockRoomLocation(location1);
		this.inventoryFacade.createStockRoomLocation(location2);

		final LocationQueryObject locationQueryObject = new LocationQueryObject();
		locationQueryObject.setPageNumber(0);
		locationQueryObject.setPageSize(1);

		final Pageable<Location> pagedLocations = this.inventoryFacade.findStockRoomLocationsByQuery(locationQueryObject);

		Assert.assertTrue(pagedLocations.getTotalPages().intValue() > 0);
		Assert.assertEquals(0, pagedLocations.getPreviousPage().intValue());

	}

}
