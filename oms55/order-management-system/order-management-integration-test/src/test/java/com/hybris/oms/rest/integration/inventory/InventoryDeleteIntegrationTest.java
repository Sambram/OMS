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
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;


public class InventoryDeleteIntegrationTest extends RestClientIntegrationTest
{

	@Autowired
	private InventoryFacade inventoryFacade;

	@Test
	public void testDeleteInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();
		final Location location = this.buildLocation(locationId);
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, 5);
		this.buildLocation(locationId);
		this.inventoryFacade.createUpdateInventory(inventory);

		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
		Pageable<ItemLocation> itemLocations = this.inventoryFacade.findItemLocationsByQuery(queryObject);
		Assert.assertEquals(Long.valueOf(1), itemLocations.getTotalRecords());
		Assert.assertEquals(1, itemLocations.getResults().get(0).getItemQuantities().size());
		Assert.assertEquals(ON_HAND, itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next().getStatusCode());

		this.inventoryFacade.deleteInventory(inventory);
		itemLocations = this.inventoryFacade.findItemLocationsByQuery(queryObject);
		Assert.assertEquals(Long.valueOf(0), itemLocations.getTotalRecords());
	}
}
