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
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * End-to-end integration tests. This class should contain only methods to retrieve data (GET).
 */
public class InventoryGetIntegrationTest extends RestClientIntegrationTest
{

	@Autowired
	private InventoryFacade inventoryFacade;

	@Test
	public void validateItemLocationIdNullTest()
	{

		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();
		final String sku2Id = this.generateSku();

		final OmsInventory inventory1 = this.buildInventory(skuId, locationId, ON_HAND, null, 5);
		final OmsInventory inventory2 = this.buildInventory(sku2Id, locationId, ON_HAND, null, 5);
		final Location location = this.buildLocation(locationId);

		try
		{
			this.inventoryFacade.createStockRoomLocation(location);
			this.inventoryFacade.createInventory(inventory1);
			this.inventoryFacade.createInventory(inventory2);

			final ItemLocationsQueryObject itemLocationQueryObject = new ItemLocationsQueryObject();
			itemLocationQueryObject.setPageNumber(0);
			itemLocationQueryObject.setPageSize(1);
			itemLocationQueryObject.setLocationIds(Collections.singletonList(locationId));

			final Pageable<ItemLocation> pagedItemLocations = this.inventoryFacade.findItemLocationsByQuery(itemLocationQueryObject);

			Assert.assertTrue(pagedItemLocations.getResults().get(0).getItemId() != null);
			Assert.assertTrue(pagedItemLocations.getResults().get(0).getId() != null);
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory1);
			this.inventoryFacade.deleteInventory(inventory2);
		}
	}

	/**
	 * This method is just testing the deprecated facade method
	 * {@link InventoryFacade#findItemLocationsByQuery(ItemLocationsQueryObject)}.
	 * For the current implementation test, see {@link #testFindPageableMultipleItemLocationsByQuery()}.
	 */
	@Test
	public void testFindPageableItemLocationsByQuery()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();
		final String sku2Id = this.generateSku();

		final OmsInventory inventory1 = this.buildInventory(skuId, locationId, ON_HAND, null, 5);
		final OmsInventory inventory2 = this.buildInventory(sku2Id, locationId, ON_HAND, null, 5);
		final Location location = this.buildLocation(locationId);

		try
		{
			this.inventoryFacade.createStockRoomLocation(location);
			this.inventoryFacade.createInventory(inventory1);
			this.inventoryFacade.createInventory(inventory2);

			final ItemLocationsQueryObject itemLocationQueryObject = new ItemLocationsQueryObject();
			itemLocationQueryObject.setPageNumber(0);
			itemLocationQueryObject.setPageSize(1);
			itemLocationQueryObject.setLocationIds(Collections.singletonList(locationId));

			final Pageable<ItemLocation> pagedItemLocations = this.inventoryFacade.findItemLocationsByQuery(itemLocationQueryObject);

			Assert.assertEquals(2, pagedItemLocations.getTotalRecords().intValue());

			Assert.assertEquals(2, pagedItemLocations.getTotalPages().intValue());

			Assert.assertEquals(1, pagedItemLocations.getNextPage().intValue());
			Assert.assertEquals(0, pagedItemLocations.getPreviousPage().intValue());
			Assert.assertTrue(pagedItemLocations.getResults().get(0).getItemId() != null);
			Assert.assertTrue(pagedItemLocations.getResults().get(0).getId() != null);
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory1);
			this.inventoryFacade.deleteInventory(inventory2);
		}
	}

	@Test
	public void testFindPageableMultipleItemLocationsByQuery()
	{
		final String locationId = this.generateLocationId();
		final String locationId2 = this.generateLocationId();
		final String locationId3 = this.generateLocationId();
		final String skuId = this.generateSku();
		final String sku2Id = this.generateSku();

		Date futureDate = null;
		try
		{
			final DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			futureDate = format.parse("01-02-2020");
		}
		catch (final ParseException e)
		{
			// should not happen.
		}

		// the first four should show on search
		final OmsInventory inventory1 = this.buildInventory(skuId, locationId, ON_HAND, null, 1);
		final OmsInventory inventory2 = this.buildInventory(sku2Id, locationId, ON_HAND, null, 2);
		final OmsInventory inventory3 = this.buildInventory(skuId, locationId2, ON_HAND, null, 3);
		final OmsInventory inventory4 = this.buildInventory(sku2Id, locationId2, ON_HAND, futureDate, 4);
		// this last one will not be on search
		final OmsInventory inventory5 = this.buildInventory(sku2Id, locationId3, ON_HAND, futureDate, 5);

		final Location location = this.buildLocation(locationId);
		final Location location2 = this.buildLocation(locationId2);
		final Location location3 = this.buildLocation(locationId3);

		try
		{
			this.inventoryFacade.createStockRoomLocation(location);
			this.inventoryFacade.createStockRoomLocation(location2);
			this.inventoryFacade.createStockRoomLocation(location3);
			this.inventoryFacade.createInventory(inventory1);
			this.inventoryFacade.createInventory(inventory2);
			this.inventoryFacade.createInventory(inventory3);
			this.inventoryFacade.createInventory(inventory4);
			this.inventoryFacade.createInventory(inventory5);

			final ItemLocationsQueryObject itemLocationQueryObject = new ItemLocationsQueryObject();
			itemLocationQueryObject.setPageNumber(0);
			itemLocationQueryObject.setPageSize(2);
			itemLocationQueryObject.setLocationIds(Arrays.asList(locationId, locationId2));

			final Pageable<ItemLocation> pagedItemLocations = this.inventoryFacade.findItemLocationsByQuery(itemLocationQueryObject);

			Assert.assertEquals(4, pagedItemLocations.getTotalRecords().intValue());
			Assert.assertEquals(2, pagedItemLocations.getTotalPages().intValue());
			Assert.assertEquals(1, pagedItemLocations.getNextPage().intValue());
			Assert.assertEquals(0, pagedItemLocations.getPreviousPage().intValue());
			final ItemLocation itemLocation0 = pagedItemLocations.getResults().get(0);
			Assert.assertTrue(itemLocation0.getItemId() != null);
			Assert.assertTrue(itemLocation0.getId() != null);
			Assert.assertNotEquals(itemLocation0.getLocation().getLocationId(), inventory5.getLocationId());
		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory1);
			this.inventoryFacade.deleteInventory(inventory2);
			this.inventoryFacade.deleteInventory(inventory3);
			this.inventoryFacade.deleteInventory(inventory4);
			this.inventoryFacade.deleteInventory(inventory5);
		}
	}

	@Test
	public void testFindAllItemStatuses()
	{
		final List<ItemStatus> listOfItemStatuses = this.inventoryFacade.findAllItemStatuses();

		Assert.assertNotNull(listOfItemStatuses);
		Assert.assertFalse(listOfItemStatuses.isEmpty());
	}

}
