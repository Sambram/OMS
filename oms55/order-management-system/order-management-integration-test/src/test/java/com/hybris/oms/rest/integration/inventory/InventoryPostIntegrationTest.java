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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.hybris.oms.api.Pageable;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.FutureItemQuantity;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.ItemQuantity;
import com.hybris.oms.domain.inventory.ItemQuantityStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import static org.junit.Assert.assertEquals;


/**
 * End-to-end integration tests.
 * This class should contain only methods that manipulate data (POST, PUT, DELETE).
 */
public class InventoryPostIntegrationTest extends RestClientIntegrationTest
{
	@Autowired
	private InventoryFacade inventoryFacade;

	@Test
	public void testCreateCurrentInventoryNoBin()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, 5);
		final Location location = this.buildLocation(locationId);

		try
		{
			inventoryFacade.createStockRoomLocation(location);
			inventoryFacade.createInventory(inventory);

			final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
			final Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			assertEquals(Long.valueOf(1), itemLocations.getTotalRecords());
			assertEquals(skuId, itemLocations.getResults().get(0).getItemId());
			assertEquals(locationId, itemLocations.getResults().get(0).getLocation().getId());
			assertEquals(1, itemLocations.getResults().get(0).getItemQuantities().size());
			
			final ItemQuantityStatus status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(ON_HAND, status.getStatusCode());
			assertEquals(5, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue());
		}
		finally
		{
            cleanUpData(inventory);
		}
	}

	@Test
	public void testCreateFutureInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

        final Date deliveryDate = getDeliveryDate();

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate, 5);
		final Location location = this.buildLocation(locationId);

		try
		{
			inventoryFacade.createStockRoomLocation(location);
			inventoryFacade.createInventory(inventory);

			final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
			final Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			assertEquals(Long.valueOf(1), itemLocations.getTotalRecords());
			assertEquals(skuId, itemLocations.getResults().get(0).getItemId());
			assertEquals(locationId, itemLocations.getResults().get(0).getLocation().getId());
			assertEquals(1, itemLocations.getResults().get(0).getItemQuantities().size());
			
			final ItemQuantityStatus status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(ON_HAND, status.getStatusCode());
			assertEquals(5, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue());
		}
		finally
		{
            cleanUpData(inventory);
		}
	}

	@Test
	public void testUpdateCurrentInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final int quantity1 = 3;
		final int quantity2 = 5;

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, quantity1);
		final Location location = this.buildLocation(locationId);

		try
		{
			inventoryFacade.createStockRoomLocation(location);
			inventoryFacade.createInventory(inventory);

			final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
			Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			final ItemQuantityStatus status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(quantity1, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);

			inventory.setQuantity(quantity2);
			inventoryFacade.updateInventory(inventory);

			itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			assertEquals(quantity2, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);
		}
		finally
		{
            cleanUpData(inventory);
		}
	}

	@Test
	public void testUpdateFutureInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final Date deliveryDate = this.getDeliveryDate();
		final int quantity1 = 3;
		final int quantity2 = 5;

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate, quantity1);
		final Location location = this.buildLocation(locationId);

		try
		{
			inventoryFacade.createStockRoomLocation(location);
			inventoryFacade.createInventory(inventory);

			final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
			Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			ItemQuantityStatus status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(quantity1, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);

			inventory.setQuantity(quantity2);
			inventoryFacade.updateInventory(inventory);

			itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
            status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(quantity2, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);
		}
		finally
		{
            cleanUpData(inventory);
		}
	}

	private Date getDeliveryDate()
	{
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, 1);
        return c.getTime();
	}

	@Test
	public void testUpdateFutureInventoryIncremental()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final Date deliveryDate = getDeliveryDate();
		final int quantity1 = 3;
		final int quantity2 = 5;

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate, quantity1);
		final Location location = this.buildLocation(locationId);

		try
		{
			inventoryFacade.createStockRoomLocation(location);
			inventoryFacade.createInventory(inventory);

			final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
			Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			ItemQuantityStatus status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(quantity1, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);

			inventory.setQuantity(quantity2);
			inventoryFacade.updateIncrementalInventory(inventory);

			itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
            status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(quantity1 + quantity2, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);
		}
		finally
		{
            cleanUpData(inventory);
		}
	}

	@Test
	public void testDeleteCurrentInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, 5);
		final Location location = this.buildLocation(locationId);

		inventoryFacade.createStockRoomLocation(location);
		inventoryFacade.createInventory(inventory);

		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
		Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
		assertEquals(Long.valueOf(1), itemLocations.getTotalRecords());
		assertEquals(1, itemLocations.getResults().get(0).getItemQuantities().size());

		inventoryFacade.deleteInventory(inventory);

		itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
		assertEquals(Long.valueOf(0), itemLocations.getTotalRecords());
	}

	@Test
	public void testDeleteFutureInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final Date deliveryDate = getDeliveryDate();
		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate, 5);
		final Location location = this.buildLocation(locationId);

		inventoryFacade.createStockRoomLocation(location);
		inventoryFacade.createInventory(inventory);

		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
		Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
		assertEquals(Long.valueOf(1), itemLocations.getTotalRecords());
		assertEquals(1, itemLocations.getResults().get(0).getItemQuantities().size());

		inventoryFacade.deleteInventory(inventory);

		itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
		assertEquals(Long.valueOf(0), itemLocations.getTotalRecords());
	}

	@Test
	public void testCreateUpdateCurrentInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final int quantity1 = 3;
		final int quantity2 = 5;

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, null, quantity1);
		final Location location = this.buildLocation(locationId);

		try
		{
			inventoryFacade.createStockRoomLocation(location);
			inventoryFacade.createUpdateInventory(inventory);

			final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
			Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			final ItemQuantityStatus status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(quantity1, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);

			inventory.setQuantity(quantity2);
			inventoryFacade.createUpdateInventory(inventory);

			itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			assertEquals(quantity1 + quantity2, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);
		}
		finally
		{
            cleanUpData(inventory);
		}
	}

	@Test
	public void testCreateUpdateFutureInventory()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final Date deliveryDate = getDeliveryDate();
		final int quantity1 = 3;
		final int quantity2 = 5;

		// Create inventory + location for test
		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate, quantity1);
		final Location location = this.buildLocation(locationId);

		try
		{
			inventoryFacade.createStockRoomLocation(location);
			inventoryFacade.createUpdateInventory(inventory);

			final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
			Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
			ItemQuantityStatus status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(quantity1, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);

			inventory.setQuantity(quantity2);
			inventoryFacade.createUpdateInventory(inventory);

			itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
            status = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
			assertEquals(quantity1 + quantity2, itemLocations.getResults().get(0).getItemQuantities().get(status).getQuantity().getValue(), 0.1);
		}
		finally
		{
            cleanUpData(inventory);
		}
	}

	@Test
	public void testUpdatefutureInventoryDates() throws EntityValidationException, ParseException
	{
		final String skuId = this.generateSku();

		final String locationId1 = this.generateLocationId();
		final String locationId2 = this.generateLocationId();

		final Location location1 = this.buildLocation(locationId1);
		final Location location2 = this.buildLocation(locationId2);

		inventoryFacade.createStockRoomLocation(location1);
		inventoryFacade.createStockRoomLocation(location2);

		final Calendar deliveryDate = Calendar.getInstance();
		deliveryDate.set(2020, 10, 01);

		final OmsInventory inventory = this.buildInventory(skuId, locationId1, ON_HAND, deliveryDate.getTime(), 5);
		inventoryFacade.createInventory(inventory);

		final OmsInventory futureInventory = this.buildInventory(skuId, locationId2, ON_HAND, deliveryDate.getTime(), 10);
		inventoryFacade.createInventory(futureInventory);

		final Calendar newDate = Calendar.getInstance();
		final Calendar updatedDeliveryDateInventory = Calendar.getInstance();
		newDate.set(2080, 10, 01);

		inventoryFacade.updateFutureInventoryDate(Lists.newArrayList(inventory, futureInventory), newDate.getTime());

		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId),
				Lists.newArrayList(locationId1, locationId2));

		final Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);

		for (ItemLocation itemLocation : itemLocations.getResults())
		{
			Map<ItemQuantityStatus, ItemQuantity> quantitiesMap = itemLocation.getItemQuantities();

			Set<ItemQuantityStatus> itemQuantityStatuses = quantitiesMap.keySet();

			for (ItemQuantityStatus itemQuantityStatus : itemQuantityStatuses)
			{
				final FutureItemQuantity futureItemQuantity = (FutureItemQuantity) quantitiesMap.get(itemQuantityStatus);

				updatedDeliveryDateInventory.setTime(futureItemQuantity.getExpectedDeliveryDate());

				assertEquals(newDate.get(Calendar.YEAR), updatedDeliveryDateInventory.get(Calendar.YEAR));

			}
		}
	}

	@Test
	public void testUpdateFutureInventoryDate() throws EntityValidationException, ParseException
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final Location location = this.buildLocation(locationId);
		inventoryFacade.createStockRoomLocation(location);

		final Calendar deliveryDate = Calendar.getInstance();
		deliveryDate.set(2050, 10, 01);

		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate.getTime(), 5);
		inventoryFacade.createInventory(inventory);

		final Calendar newDate = Calendar.getInstance();
		newDate.set(2070, 10, 01);

		// When
		inventory.setQuantity(6);

		inventoryFacade.updateFutureInventoryDate(Lists.newArrayList(inventory), newDate.getTime());
		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
		final Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
		final ItemQuantityStatus itemQuantityStatus = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
		final FutureItemQuantity futureItemQuantity = (FutureItemQuantity) itemLocations.getResults().get(0).getItemQuantities().get(itemQuantityStatus);

		// Then
		final Calendar updatedDeliveryDate = Calendar.getInstance();
		updatedDeliveryDate.setTime(futureItemQuantity.getExpectedDeliveryDate());

		assertEquals(newDate.get(Calendar.YEAR), updatedDeliveryDate.get(Calendar.YEAR));
		assertEquals(newDate.get(Calendar.MONTH), updatedDeliveryDate.get(Calendar.MONTH));
		assertEquals(newDate.get(Calendar.DAY_OF_MONTH), updatedDeliveryDate.get(Calendar.DAY_OF_MONTH));
		assertEquals(6, futureItemQuantity.getQuantity().getValue());
	}

	@Test
	public void testUpdateExistingFutureInventoryDate() throws EntityValidationException, ParseException
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final Location location = this.buildLocation(locationId);
		inventoryFacade.createStockRoomLocation(location);
		final Calendar deliveryDate = Calendar.getInstance();
		deliveryDate.set(2050, 10, 01);
		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate.getTime(), 5);
		inventoryFacade.createInventory(inventory);


		deliveryDate.set(2070, 10, 01);
		final OmsInventory futureInventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate.getTime(), 10);
		inventoryFacade.createInventory(futureInventory);

		final Calendar newDate = Calendar.getInstance();
		newDate.set(2070, 10, 01);

		// When
		inventory.setQuantity(6);
		inventoryFacade.updateFutureInventoryDate(Lists.newArrayList(inventory), newDate.getTime());
		
		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(Collections.singletonList(skuId), Collections.singletonList(locationId));
		final Pageable<ItemLocation> itemLocations = inventoryFacade.findItemLocationsByQuery(queryObject);
		final ItemQuantityStatus itemQuantityStatus = itemLocations.getResults().get(0).getItemQuantities().keySet().iterator().next();
		final FutureItemQuantity futureItemQuantity = (FutureItemQuantity) itemLocations.getResults().get(0).getItemQuantities().get(itemQuantityStatus);
		
		// Then
		final Calendar updatedDeliveryDate = Calendar.getInstance();
		updatedDeliveryDate.setTime(futureItemQuantity.getExpectedDeliveryDate());

		assertEquals(newDate.get(Calendar.YEAR), updatedDeliveryDate.get(Calendar.YEAR));
		assertEquals(newDate.get(Calendar.MONTH), updatedDeliveryDate.get(Calendar.MONTH));
		assertEquals(newDate.get(Calendar.DAY_OF_MONTH), updatedDeliveryDate.get(Calendar.DAY_OF_MONTH));
		assertEquals(16, futureItemQuantity.getQuantity().getValue());
	}

	@Test
	public void testUpdateFutureInventoryQuantityForNonExistentStatus()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();

		final Location location = this.buildLocation(locationId);
		inventoryFacade.createStockRoomLocation(location);
		final Calendar deliveryDate = Calendar.getInstance();
		deliveryDate.set(2050, 10, 01);
		final OmsInventory inventory = this.buildInventory(skuId, locationId, ON_HAND, deliveryDate.getTime(), 5);
		inventoryFacade.createInventory(inventory);

		// When
		final OmsInventory newInventoryStatus = this.buildInventory(skuId, locationId, NOT_AVAILABLE, deliveryDate.getTime(), 10);
		final OmsInventory createdInventory = inventoryFacade.createUpdateInventory(newInventoryStatus);

		// Then
		assertEquals(10, createdInventory.getQuantity());
	}

	@Test
	public void shouldCRUDinventoryWithBins()
	{
		final String locationId = this.generateLocationId();
		final String skuId = this.generateSku();
		final String binCode = this.generateBinCode();

		final OmsInventory inventory = this.buildInventory(skuId, locationId, binCode, ON_HAND, null, 0);
		final Bin bin = this.buildBin(binCode, locationId);
		final Location location = this.buildLocation(locationId);
		location.getAddress().setLatitudeValue(45.501707);
		location.getAddress().setLongitudeValue(-73.574105);

		inventoryFacade.createStockRoomLocation(location);
		inventoryFacade.createBin(bin);

		final OmsInventory created = inventoryFacade.createInventory(inventory);
		assertEquals(ON_HAND, created.getStatus());
		assertEquals(skuId, created.getSkuId());
		assertEquals(locationId, created.getLocationId());
		assertEquals(binCode, created.getBinCode());
		assertEquals(0, created.getQuantity());

		inventory.setQuantity(10);
		inventoryFacade.updateInventory(inventory);

		inventory.setQuantity(10);
		inventoryFacade.updateIncrementalInventory(inventory);

		inventoryFacade.deleteInventory(created);
		try
		{
			inventoryFacade.deleteInventory(created);
		}
		catch (final EntityNotFoundException e)
		{
			// Delete was successful.
		}
	}

    private void cleanUpData(OmsInventory inventory)
    {
        inventoryFacade.deleteInventory(inventory);
    }

}
