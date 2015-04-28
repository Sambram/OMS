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

import static org.junit.Assert.assertNotNull;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * End-to-end integration tests.
 * This class should contain only methods that manipulate data (POST, PUT, DELETE).
 */
public class StockroomLocationPostIntegrationTest extends RestClientIntegrationTest
{

	@Autowired
	private InventoryFacade inventoryFacade;

	@Test
	public void testCreateStockRoomLocation()
	{
		final String locationId = this.generateLocationId();
		final Location location = this.buildLocation(locationId);
		final Address address = new Address("1000 Broadway", null, "New York City", "NY", "10001", null, null, "US",
				"Unites States", null, null);
		location.setAddress(address);

		// create location
		this.inventoryFacade.createStockRoomLocation(location);
		// search to check if location is in DB
		final Location locationDB = this.inventoryFacade.getStockRoomLocationByLocationId(locationId);
		assertNotNull(locationDB);
		Assert.assertEquals(location.getLocationId(), locationDB.getLocationId());
		Assert.assertEquals(location.getDescription(), locationDB.getDescription());
		assertNotNull(locationDB.getAddress());
		Assert.assertEquals(location.getAddress().getCityName(), locationDB.getAddress().getCityName());
	}

	@Test
	public void testUpdateStockRoomLocation()
	{
		final String locationId = this.generateLocationId();
		final Location location = this.buildLocation(locationId);
		this.inventoryFacade.createStockRoomLocation(location);

		// update the description
		final String newDescription = "modified description";
		location.setDescription(newDescription);
		this.inventoryFacade.updateStockRoomLocation(location);

		// search to check if location is in DB
		final Location locationDB = this.inventoryFacade.getStockRoomLocationByLocationId(locationId);
		assertNotNull(locationDB);
		Assert.assertEquals(newDescription, locationDB.getDescription());
	}

	@Test
	public void testCreateUpdateStockRoomLocation()
	{
		final String locationId = this.generateLocationId();
		final Location created = this.buildLocation(locationId);
		// add country
		created.getShipToCountriesCodes().add("FR");
		// create location
		this.inventoryFacade.createUpdateStockRoomLocation(created);

		// search to check if location is in DB
		final Location createdDB = this.inventoryFacade.getStockRoomLocationByLocationId(locationId);
		assertNotNull(createdDB);
		Assert.assertEquals(created.getLocationId(), createdDB.getLocationId());
		Assert.assertEquals(created.getDescription(), createdDB.getDescription());
		assertNotNull(createdDB.getAddress());
		Assert.assertEquals(created.getAddress().getCityName(), createdDB.getAddress().getCityName());
		Assert.assertEquals(2, created.getShipToCountriesCodes().size());

		// update the description and country codes list
		final String newDescription = "modified description";
		created.setDescription(newDescription);
		created.getShipToCountriesCodes().remove(created.getShipToCountriesCodes().toArray()[0]);
		this.inventoryFacade.createUpdateStockRoomLocation(created);

		// search to check if location is in DB
		final Location updatedDB = this.inventoryFacade.getStockRoomLocationByLocationId(locationId);
		assertNotNull(updatedDB);
		Assert.assertEquals(newDescription, updatedDB.getDescription());
		Assert.assertEquals(1, updatedDB.getShipToCountriesCodes().size());
	}

}
