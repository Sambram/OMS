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
package com.hybris.oms.facade.inventory;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.hybris.oms.api.Pageable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.facade.utils.DateUtils;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Integration test for {@link DefaultInventoryFacade} to verify that CRUD on current and future inventory works with
 * and without bins.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/oms-facade-spring-test.xml"})
public class DefaultInventoryFacadeBinIntegrationTest
{
	private static final String SKU_ID = "sku666";
	private static final String STATUS_CODE = "ON_HAND";
	private static final String LOCATION_ID = "1";
	private static final String BIN_CODE_1 = "1";
	private static final String BIN_CODE_2 = "2";

	private static final Date DELIVERY_DATE = DateUtils.getTomorrowsDate();

	@Autowired
	private PersistenceManager persistenceManager;

	@Resource
	private InventoryFacade inventoryFacade;

	@Test
	@Transactional
	public void createCurrentInventory()
	{
		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final Bin bin = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_ID, null, 0);
		inventoryFacade.createBin(bin);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		final ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, BIN_CODE_1, STATUS_CODE, null, 0);

		inventoryFacade.createInventory(inventory);

		final List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		final ItemQuantityData itemQuantity = itemLocations.get(0).getItemQuantities().get(0);

		assertEquals(inventory.getSkuId(), itemLocations.get(0).getItemId());
		assertEquals(BIN_CODE_1, itemLocations.get(0).getBin().getBinCode());
		assertEquals(LOCATION_ID, itemLocations.get(0).getStockroomLocation().getLocationId());
		assertEquals(STATUS_CODE, itemQuantity.getStatusCode());
		assertEquals(inventory.getQuantity(), itemQuantity.getQuantityValue());
	}

	@Test
	@Transactional
	public void createDeleteCurrentInventoryWithDefaultBin()
	{
		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID,
				InventoryServiceConstants.DEFAULT_BIN, STATUS_CODE, null, 0);

		inventoryFacade.createInventory(inventory);

		List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		assertNotNull(itemLocations);
		assertEquals(1, itemLocations.size());
		assertEquals(inventory.getSkuId(), itemLocations.get(0).getItemId());
		assertEquals(inventory.getLocationId(), itemLocations.get(0).getStockroomLocation().getLocationId());

		inventoryFacade.deleteInventory(inventory);
		itemLocations = persistenceManager.search(persistenceManager.createCriteriaQuery(ItemLocationData.class));
		assertTrue(itemLocations.isEmpty());
	}

	@Test
	@Transactional
	public void createDeleteCurrentInventoryWithBinAndQuantity()
	{
		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final Bin bin = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_ID, null, 0);
		inventoryFacade.createBin(bin);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		final ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, BIN_CODE_1, STATUS_CODE, null, 0);
		inventory = inventoryFacade.createInventory(inventory);
		assertEquals(0, inventory.getQuantity());

		inventoryFacade.deleteInventory(inventory);
		final List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		assertTrue(itemLocations.isEmpty());
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void createCurrentInventoryInvalidBin()
	{
		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final Bin bin = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_ID, null, 0);
		inventoryFacade.createBin(bin);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(
                SKU_ID, LOCATION_ID, BIN_CODE_2, STATUS_CODE, null, 10);

		inventoryFacade.createInventory(inventory);
	}

    @Test
	@Transactional
	public void updateCurrentInventoryIncrementalAtBinLevelWithQuantity()
	{
		final int initialQty = 0;
		final int incrementQty = 7;

		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final Bin bin = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_ID, null, 0);
		inventoryFacade.createBin(bin);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		final ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(
                SKU_ID, LOCATION_ID, BIN_CODE_1, STATUS_CODE, null, initialQty);

		inventoryFacade.createInventory(inventory);

		final List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		final ItemQuantityData itemQuantity = itemLocations.get(0).getItemQuantities().get(0);
		assertEquals(initialQty, itemQuantity.getQuantityValue());

		inventory.setQuantity(incrementQty);
		inventoryFacade.updateIncrementalInventory(inventory);

        final List<ItemLocationData> itemLocationsAfterUpdate = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

        final ItemQuantityData itemQuantityAfterUpdate = itemLocationsAfterUpdate.get(0).getItemQuantities().get(0);
        assertEquals(incrementQty, itemQuantityAfterUpdate.getQuantityValue());
    }

	@Transactional
	public void testUpdateCurrentInventoryAtBinLevelWithQuantity()
	{
		final int initialQty = 0;
		final int incrementQty = 7;

		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final Bin bin = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_ID, null, 0);
		inventoryFacade.createBin(bin);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		final ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);

		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(
                SKU_ID, LOCATION_ID, BIN_CODE_1, STATUS_CODE, null, initialQty);

		inventoryFacade.createInventory(inventory);

		final List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		final ItemQuantityData itemQuantity = itemLocations.get(0).getItemQuantities().get(0);
		assertEquals(initialQty, itemQuantity.getQuantityValue());

		inventory.setQuantity(incrementQty);
		inventoryFacade.updateInventory(inventory);

        final List<ItemLocationData> itemLocationsAfterUpdate = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

        final ItemQuantityData itemQuantityAfterUpdate = itemLocationsAfterUpdate.get(0).getItemQuantities().get(0);
        assertEquals(incrementQty, itemQuantityAfterUpdate.getQuantityValue());
    }

	@Transactional
	public void testUpdateFutureInventoryIncrementalAtBinLevelWithQuantity()
	{
		final int initialQty = 0;
		final int incrementQty = 7;

		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final Bin bin = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_ID, null, 0);
		inventoryFacade.createBin(bin);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		final ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(
                SKU_ID, LOCATION_ID, BIN_CODE_1, STATUS_CODE, DELIVERY_DATE, initialQty);

		inventoryFacade.createInventory(inventory);

		final List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		final ItemQuantityData itemQuantity = itemLocations.get(0).getItemQuantities().get(0);
		assertEquals(initialQty, itemQuantity.getQuantityValue());

		inventory.setQuantity(incrementQty);
		inventoryFacade.updateInventory(inventory);

        final List<ItemLocationData> itemLocationsAfterUpdate = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

        final ItemQuantityData itemQuantityAfterUpdate = itemLocationsAfterUpdate.get(0).getItemQuantities().get(0);
        assertEquals(incrementQty, itemQuantityAfterUpdate.getQuantityValue());
    }

	@Transactional
	public void testUpdateFutureInventoryAtBinLevelWithQuantity()
	{
		final int initialQty = 0;
		final int incrementQty = 7;

		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final Bin bin = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_ID, null, 0);
		inventoryFacade.createBin(bin);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		final ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(
                SKU_ID, LOCATION_ID, BIN_CODE_1, STATUS_CODE, DELIVERY_DATE, initialQty);

		inventoryFacade.createInventory(inventory);

		final List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		final ItemQuantityData itemQuantity = itemLocations.get(0).getItemQuantities().get(0);
		assertEquals(initialQty, itemQuantity.getQuantityValue());

		inventory.setQuantity(incrementQty);
		inventoryFacade.updateInventory(inventory);


        final List<ItemLocationData> itemLocationsAfterUpdate = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

        final ItemQuantityData itemQuantityAfterUpdate = itemLocationsAfterUpdate.get(0).getItemQuantities().get(0);
        assertEquals(incrementQty, itemQuantityAfterUpdate.getQuantityValue());
    }

	@Test
	@Transactional
	public void testUpdateFutureInventoryIncrementalAtStockroomLocationLevel()
	{
		final int initialQty = 5;
		final int incrementQty = 7;

		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(
                SKU_ID, LOCATION_ID, null, STATUS_CODE, DELIVERY_DATE, initialQty);

		inventoryFacade.createInventory(inventory);
		
		final List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		final ItemQuantityData itemQuantity = itemLocations.get(0).getItemQuantities().get(0);
		assertEquals(initialQty, itemQuantity.getQuantityValue());

		inventory.setQuantity(incrementQty);
		final OmsInventory updated = inventoryFacade.updateIncrementalInventory(inventory);

		assertNotNull(updated);
		assertEquals(initialQty + incrementQty, updated.getQuantity());
	}

	@Test
	@Transactional
	public void testUpdateFutureInventoryAtStockroomLocationLevel()
	{
		final int initialQty = 5;
		final int incrementQty = 7;

		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory = InventoryHelper.buildInventory(
                SKU_ID, LOCATION_ID, null, STATUS_CODE, DELIVERY_DATE, initialQty);

		inventoryFacade.createInventory(inventory);
		
		final List<ItemLocationData> itemLocations = persistenceManager.search(
                persistenceManager.createCriteriaQuery(ItemLocationData.class));

		final ItemQuantityData itemQuantity = itemLocations.get(0).getItemQuantities().get(0);
		assertEquals(initialQty, itemQuantity.getQuantityValue());

		inventory.setQuantity(incrementQty);
		final OmsInventory updated = inventoryFacade.updateInventory(inventory);

		assertNotNull(updated);
		assertEquals(incrementQty, updated.getQuantity());
	}

	@Test
	@Transactional
	public void testFindItemLocationsByQuery()
	{
		final Location location = InventoryHelper.buildLocation(LOCATION_ID, ImmutableSet.of(LocationRole.SHIPPING));

		inventoryFacade.createStockRoomLocation(location);

		final Bin bin1 = InventoryHelper.buildBin(BIN_CODE_1, LOCATION_ID, "", 1);
		inventoryFacade.createBin(bin1);

		final String itemStatusDesc = "INTEGRATION_TEST_DESC";
		ItemStatus itemStatus = InventoryHelper.buildItemStatus(STATUS_CODE, itemStatusDesc);
		inventoryFacade.createItemStatus(itemStatus);

		final OmsInventory inventory1 = InventoryHelper.buildInventory(
                SKU_ID, LOCATION_ID, BIN_CODE_1, STATUS_CODE, null, 0);

		inventoryFacade.createInventory(inventory1);

		final OmsInventory inventory2 = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID,
				InventoryServiceConstants.DEFAULT_BIN, STATUS_CODE, null, 0);

		inventoryFacade.createInventory(inventory2);

		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject(
                Collections.singletonList(SKU_ID), Collections.singletonList(LOCATION_ID));
		final Pageable<ItemLocation> result = inventoryFacade.findItemLocationsByQuery(queryObject);

		assertEquals(Long.valueOf(1), result.getTotalRecords());
	}
}
