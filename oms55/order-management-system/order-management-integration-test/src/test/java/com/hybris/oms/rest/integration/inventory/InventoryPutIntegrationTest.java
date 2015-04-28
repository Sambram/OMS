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

import java.util.Collections;

import com.hybris.oms.api.Pageable;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.ItemQuantityStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;


public class InventoryPutIntegrationTest extends RestClientIntegrationTest
{
	@Autowired
	private InventoryFacade inventoryFacade;

	@Test
	public void testCreateUpdateInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, 5);
		final Location location = this.buildLocation(locationId);

		try
		{
			this.inventoryFacade.createStockRoomLocation(location);
			final OmsInventory createdInventory = this.inventoryFacade.createUpdateInventory(inventory);

			Assert.assertNotNull(createdInventory);
			Assert.assertEquals(createdInventory.getLocationId(), locationId);
			Assert.assertEquals(createdInventory.getSkuId(), skuId);
			Assert.assertEquals(createdInventory.getStatus(), ON_HAND);

			inventory.setSkuId("UpdatedSKUId");
			inventory.setStatus(NOT_AVAILABLE);
			final OmsInventory updatedInventory = this.inventoryFacade.createUpdateInventory(inventory);

			Assert.assertNotNull(updatedInventory);
			Assert.assertEquals(updatedInventory.getSkuId(), "UpdatedSKUId");
			Assert.assertEquals(updatedInventory.getStatus(), NOT_AVAILABLE);

		}
		finally
		{
			this.inventoryFacade.deleteInventory(inventory);
		}

	}

	@Test
	public void testUpdateCurrentInventoryIncremental()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final int quantity1 = 3;
		final int quantity2 = 5;

		// Create inventory + location for test
		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, quantity1);
		final Location location = this.buildLocation(locationId);

		try
		{
			this.inventoryFacade.createStockRoomLocation(location);
			final OmsInventory created = this.inventoryFacade.createInventory(inventory);

			// search to check if inventory is in DB
			final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
			Pageable<ItemLocation> itemLocations = this.inventoryFacade.findItemLocationsByQuery(queryObject);
			final ItemQuantityStatus status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			Assert.assertEquals(quantity1, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);

			// update inventory
			created.setQuantity(quantity2);
			this.inventoryFacade.updateIncrementalInventory(created);

			// search to check if inventory is in DB
			itemLocations = this.inventoryFacade.findItemLocationsByQuery(queryObject);
			Assert.assertEquals(quantity1 + quantity2, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);
		}
		finally
		{
			// clean up data
			this.inventoryFacade.deleteInventory(inventory);
		}
	}

}
