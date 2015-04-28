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
package com.hybris.oms.service.inventory.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.BinQuerySupport;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.domain.inventory.LocationQuerySortSupport;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultInventoryServiceIntegrationTest
{

	private static final String CURRENT_ITEM_QUANTITY_HYBRIS_ID = "single|CurrentItemQuantityData|1";
	private static final String FUTURE_ITEM_QUANTITY_HYBRIS_ID = "single|FutureItemQuantityData|1";

	private static final String INVALID_STRING = "INVALID";
	private static final String ITEM_ID = "0000000001";

	private static final String ITEM_ID_FUTURE = "0000000015";
	private static final String ITEM_LOCATION_CURRENT_HYBRIS_ID = "single|ItemLocationData|1";
	private static final String ITEM_LOCATION_FUTURE_HYBRIS_ID = "single|ItemLocationData|62";

	private static final String ITEM_STATUS_STATUS_CODE = "ON_HAND";

	private static final String LOCATION_HYBRIS_ID = "single|StockroomLocationData|1";
	private static final String LOCATION_ID = "1";
	private static final String LOCATION_ID_NEW = "190";
	private static final String LOCATION_ID_FUTURE = "3";
	private static final String LOCATION_STORE_NAME = "STORE_NAME";

	private static final String BIN_LOCATION_ID = "1";
	private static final String BIN_CODE = InventoryServiceConstants.DEFAULT_BIN;
	private static final String BIN_CODE_NEW = "11";
	private static final String BIN_CODE_FUTURE = "12";

	private Calendar calendar;

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Autowired
	private ImportService importService;

	@Autowired
	private final DefaultInventoryService inventoryService = new DefaultInventoryService();

	@Autowired
	private PersistenceManager pmgr;

	@Before
	public void setUp() throws InterruptedException
	{
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		importService.loadMcsvResource(fetcher.fetchResources("/inventory/test-inventory-data-import.mcsv")[0]);
		calendar = Calendar.getInstance();
		calendar.set(2012, 0, 1);
	}

	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void createCurrentExistentItemQuantity()
	{
		inventoryService.createItemQuantity(ITEM_ID, LOCATION_ID, BIN_CODE, ITEM_STATUS_STATUS_CODE, "unitCode", 5, null);
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void createCurrentItemQuantityMissingStatus()
	{
		inventoryService.createItemQuantity(null, LOCATION_ID, BIN_CODE, null, "unitCode", 5, null);
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void createItemQuantityInvalidBinLocation()
	{
		inventoryService.createItemQuantity(ITEM_ID, LOCATION_ID, "INVALID_BIN_CODE", ITEM_STATUS_STATUS_CODE,
                "unitCode", 5, null);
	}

	@Test
	@Transactional
	public void createCurrentItemQuantityAndItemLocation()
	{
		ItemQuantityData qty = inventoryService.createItemQuantity(ITEM_ID, LOCATION_ID_NEW, BIN_CODE_NEW,
				ITEM_STATUS_STATUS_CODE, "unitCode", 1, null);
		assertEquals(1, qty.getQuantityValue());
	}

	@Test
	@Transactional
	public void createUpdateCurrentItemQuantityAndItemLocation()
	{
		ItemQuantityData qty = inventoryService.createUpdateItemQuantity(ITEM_ID, LOCATION_ID_NEW, BIN_CODE_NEW,
				ITEM_STATUS_STATUS_CODE, "unitCode", 1, null);
	    assertEquals(1, qty.getQuantityValue());
	}

	@Test
	@Transactional
	public void createUpdateCurrentExistentItemQuantity()
	{
		ItemQuantityData qty = inventoryService.createUpdateItemQuantity(ITEM_ID, LOCATION_ID, BIN_CODE,
				ITEM_STATUS_STATUS_CODE, "unitCode", 6, null);
		assertEquals(7, qty.getQuantityValue());
        assertFalse(qty.getOwner().isBanned());
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void createUpdateExistentItemQuantityMissingStatus()
	{
		inventoryService.createUpdateItemQuantity(ITEM_ID, LOCATION_ID, BIN_CODE, null, "unitCode", 6, null);
	}

	@Test
	@Transactional
	public void createCurrentItemQuantity()
	{
		String newStatusCode = "newStatusCode";
		ItemQuantityData iqData = inventoryService.createItemQuantity(ITEM_ID, LOCATION_ID, BIN_CODE, newStatusCode,
				"unitCode", 5, null);
		assertNotNull(iqData);
	}

	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void createFutureExistentItemQuantity()
	{
		inventoryService.createItemQuantity(ITEM_ID_FUTURE, LOCATION_ID_FUTURE, BIN_CODE_FUTURE, ITEM_STATUS_STATUS_CODE,
				"unitCode", 5, calendar.getTime());

		inventoryService.createItemQuantity(ITEM_ID_FUTURE, LOCATION_ID_FUTURE, BIN_CODE_FUTURE, ITEM_STATUS_STATUS_CODE,
				"unitCode", 5, calendar.getTime());
		fail();
	}

	@Test
	@Transactional
	public void createFutureItemQuantity()
	{
		String newStatusCode = "newStatusCode";
		ItemQuantityData iqData = inventoryService.createItemQuantity(ITEM_ID_FUTURE, LOCATION_ID_FUTURE,
				BIN_CODE_FUTURE, newStatusCode, "unitCode", 5, calendar.getTime());
		assertNotNull(iqData);
	}

	@Test(expected = ManagedObjectNotFoundException.class)
	@Transactional
	public void deleteCurrentItemQuantity()
	{
		CurrentItemQuantityData iqData = pmgr.get(HybrisId.valueOf(CURRENT_ITEM_QUANTITY_HYBRIS_ID));
		assertNotNull(iqData);

		inventoryService.deleteItemQuantity(iqData.getOwner().getItemId(), iqData.getOwner().getStockroomLocation()
				.getLocationId(), iqData.getOwner().getBin().getBinCode(), iqData.getStatusCode(), null);
		pmgr.flush();
		// search for deleted ItemQuantityData should throw a ManagedObjectNotFoundException
		pmgr.get(HybrisId.valueOf(CURRENT_ITEM_QUANTITY_HYBRIS_ID));
		fail();
	}

	@Test
	@Transactional
	public void shouldFindItemLocationsByBinCode()
	{
		List<ItemLocationData> inventory = inventoryService.findItemlocationsByLocationIdBinCode("1", "1");
		assertFalse(inventory.isEmpty());
		assertEquals(3, inventory.size());

		assertEquals("1", inventory.get(0).getStockroomLocation().getLocationId());
		assertEquals("1", inventory.get(0).getBin().getBinCode());
		assertEquals(1, inventory.get(0).getItemQuantities().size());
		assertTrue(inventory.get(0).getItemQuantities().get(0) instanceof CurrentItemQuantityData);
		assertEquals(5, inventory.get(0).getItemQuantities().get(0).getQuantityValue());

		assertEquals("1", inventory.get(1).getStockroomLocation().getLocationId());
		assertEquals("1", inventory.get(1).getBin().getBinCode());
		assertEquals(1, inventory.get(1).getItemQuantities().size());
		assertTrue(inventory.get(1).getItemQuantities().get(0) instanceof FutureItemQuantityData);
		assertEquals(5, inventory.get(1).getItemQuantities().get(0).getQuantityValue());
		assertEquals(2529705600000L, inventory.get(1).getItemQuantities().get(0).getExpectedDeliveryDate().getTime());

		assertEquals("1", inventory.get(2).getStockroomLocation().getLocationId());
		assertEquals("1", inventory.get(2).getBin().getBinCode());
		assertEquals(1, inventory.get(2).getItemQuantities().size());
		assertTrue(inventory.get(2).getItemQuantities().get(0) instanceof CurrentItemQuantityData);
		assertEquals(5, inventory.get(2).getItemQuantities().get(0).getQuantityValue());

		inventory = inventoryService.findItemlocationsByLocationIdBinCode("2", "1");
		assertFalse(inventory.isEmpty());
		assertEquals(1, inventory.size());

		assertEquals("2", inventory.get(0).getStockroomLocation().getLocationId());
		assertEquals("1", inventory.get(0).getBin().getBinCode());
		assertEquals(1, inventory.get(0).getItemQuantities().size());
		assertTrue(inventory.get(0).getItemQuantities().get(0) instanceof CurrentItemQuantityData);
		assertEquals(5, inventory.get(0).getItemQuantities().get(0).getQuantityValue());
	}

	@Test
	@Transactional
	public void shouldDeleteItemLocationsByLocationIdBinCode()
	{
		String itemLocationToDelete1 = "single|ItemLocationData|101";
		String itemLocationToDelete2 = "single|ItemLocationData|102";
		String currentItemQuantityToDelete = "single|CurrentItemQuantityData|101";
		String futureItemQuantityToDelete = "single|FutureItemQuantityData|102";

		// Assert that all of the inventory that should be removed currently exists.
		pmgr.get(HybrisId.valueOf(itemLocationToDelete1));
		pmgr.get(HybrisId.valueOf(itemLocationToDelete2));
		pmgr.get(HybrisId.valueOf(currentItemQuantityToDelete));
		pmgr.get(HybrisId.valueOf(futureItemQuantityToDelete));

		inventoryService.deleteInventoryForLocationIdBinCode("1", "1");
		pmgr.flush();

		try
		{
			pmgr.get(HybrisId.valueOf(itemLocationToDelete1));
			fail("Item Location should have been removed");
		}
		catch (ManagedObjectNotFoundException ignored) { }

		try
		{
			pmgr.get(HybrisId.valueOf(itemLocationToDelete2));
			fail("Item Location should have been removed");
		}
		catch (ManagedObjectNotFoundException ignored) { }

		try
		{
			pmgr.get(HybrisId.valueOf(currentItemQuantityToDelete));
			fail("Current item quantity should have been removed");
		}
		catch (ManagedObjectNotFoundException ignored) { }

		try
		{
			pmgr.get(HybrisId.valueOf(futureItemQuantityToDelete));
			fail("Future item quantity should have been removed");
		}
		catch (ManagedObjectNotFoundException ignored) { }
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void deleteNonExistingItemQuantity()
	{
		inventoryService.deleteItemQuantity(ITEM_ID, LOCATION_ID_NEW, null, ITEM_STATUS_STATUS_CODE, null);
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void deleteFutureItemQuantity()
	{
		FutureItemQuantityData iqData = pmgr.get(HybrisId.valueOf(FUTURE_ITEM_QUANTITY_HYBRIS_ID));
		assertNotNull(iqData);

		inventoryService.deleteItemQuantity(iqData.getOwner().getItemId(), iqData.getOwner().getStockroomLocation()
				.getLocationId(), null, iqData.getStatusCode(), iqData.getExpectedDeliveryDate());
		pmgr.flush();

		// search for deleted ItemQuantityData should throw a ManagedObjectNotFoundException
		inventoryService.findFutureItemQuantityBySkuLocationBinCodeStatusDate(iqData.getOwner().getItemId(), iqData.getOwner()
				.getStockroomLocation().getLocationId(), InventoryServiceConstants.DEFAULT_BIN, iqData.getStatusCode(),
				iqData.getExpectedDeliveryDate());
		fail();
	}

	@Test(expected = ManagedObjectNotFoundException.class)
	@Transactional
	public void deleteLastItemQuantity()
	{
		inventoryService.findFutureItemQuantityBySkuLocationBinCodeStatusDate(ITEM_ID_FUTURE, LOCATION_ID_FUTURE,
				InventoryServiceConstants.DEFAULT_BIN, ITEM_STATUS_STATUS_CODE, calendar.getTime());
		inventoryService.deleteItemQuantity(ITEM_ID_FUTURE, LOCATION_ID_FUTURE, InventoryServiceConstants.DEFAULT_BIN,
				ITEM_STATUS_STATUS_CODE, calendar.getTime());
		pmgr.flush();
		boolean iqWasDeleted = false;
		try
		{
			inventoryService.findFutureItemQuantityBySkuLocationBinCodeStatusDate(ITEM_ID_FUTURE, LOCATION_ID_FUTURE,
					InventoryServiceConstants.DEFAULT_BIN, ITEM_STATUS_STATUS_CODE, calendar.getTime());
		}
		catch (EntityNotFoundException e)
		{
			iqWasDeleted = true;
		}

		assertTrue(iqWasDeleted);

		// the ItemLocation should have been deleted, along with the ItemQuantity - ManagedObjectNotFoundException should
		// be thrown
		pmgr.get(HybrisId.valueOf(ITEM_LOCATION_FUTURE_HYBRIS_ID));
		fail();
	}

	@Test
	@Transactional
    /*
     * If there is more than one ItemQuantity inside an ItemLocation, only the ItemQuantity should be deleted.
     * The ItemLocation should persist with the remaining ItemQuantities
     */
	public void deleteNotLastItemQuantity()
	{
		ItemLocationData ilData = pmgr.get(HybrisId.valueOf(ITEM_LOCATION_CURRENT_HYBRIS_ID));
		assertNotNull(ilData);

		assertTrue(ilData.getItemQuantities().size() > 1);

		CurrentItemQuantityData iqData = (CurrentItemQuantityData) ilData.getItemQuantities().get(0);
		String iqHybrisId = iqData.getId().toString();

		inventoryService.deleteItemQuantity(iqData.getOwner().getItemId(), iqData.getOwner().getStockroomLocation()
				.getLocationId(), iqData.getOwner().getBin().getBinCode(), iqData.getStatusCode(), null);
		pmgr.flush();

		boolean iqWasDeleted = false;
		try
		{
			pmgr.get(HybrisId.valueOf(iqHybrisId));
		}
		catch (ManagedObjectNotFoundException e)
		{
			iqWasDeleted = true;
		}

		assertTrue(iqWasDeleted);

		ItemLocationData ilDataUpdated = pmgr.get(HybrisId.valueOf(ITEM_LOCATION_CURRENT_HYBRIS_ID));
		assertNotNull(ilDataUpdated);
	}

	@Test
	@Transactional
	public void findCurrentItemQuantityBySkuLocationBinCodeStatus()
	{
		CurrentItemQuantityData itemQuantity = inventoryService.findCurrentItemQuantityBySkuLocationBinCodeStatus(
				ITEM_ID, LOCATION_ID, InventoryServiceConstants.DEFAULT_BIN, ITEM_STATUS_STATUS_CODE);
		assertNotNull(itemQuantity);
	}

	@Test
	@Transactional
	public void findFutureItemQuantityBySkuLocationBinCodeStatusDate()
	{
		FutureItemQuantityData itemQuantity = inventoryService.findFutureItemQuantityBySkuLocationBinCodeStatusDate(
				ITEM_ID_FUTURE, LOCATION_ID_FUTURE, InventoryServiceConstants.DEFAULT_BIN, ITEM_STATUS_STATUS_CODE,
				calendar.getTime());
		assertNotNull(itemQuantity);
	}

	@Test
	@Transactional
	public void testCreateLocation()
	{
		StockroomLocationData location = pmgr.create(StockroomLocationData.class);
		location.setStoreName(LOCATION_STORE_NAME);
		location.setLocationId("1243");
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		inventoryService.createLocation(location);

		assertNotNull(location.getId());
	}
	
	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void testCreateDuplicateLocation()
	{
		StockroomLocationData location = pmgr.create(StockroomLocationData.class);
		location.setStoreName(LOCATION_STORE_NAME);
		location.setLocationId("1243");
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		inventoryService.createLocation(location);
		inventoryService.createLocation(location);
	}

	@Test
	@Transactional
	public void testFindAllItemStatuses()
	{
		List<ItemStatusData> itemStatuses = inventoryService.findAllItemStatuses();
		assertEquals(9, itemStatuses.size());
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void testFindLocationsByLocationIdsEmpty()
	{
		inventoryService.findLocationsByLocationIds(Collections.<String>emptySet());
	}

	@Test
	@Transactional
	public void testFindLocationsByLocationIds()
	{
		List<StockroomLocationData> locations = inventoryService.findLocationsByLocationIds(Sets.newHashSet("16", "17"));
		Set<String> foundLocationIds = new HashSet<>();
		for (StockroomLocationData loc : locations)
		{
			foundLocationIds.add(loc.getLocationId());
		}
		assertThat(foundLocationIds).containsOnly("16", "17");
	}

	@Test
	@Transactional
	public void testFindLocationsByBaseStores()
	{
		LocationQueryObject locationQueryObject = new LocationQueryObject();
		locationQueryObject.setBaseStores(Arrays.asList("baseStore1", "baseStore2"));
		List<StockroomLocationData> locations = inventoryService.findPagedLocations(locationQueryObject).getContent();
		Set<String> foundLocationIds = new HashSet<>();
		for (StockroomLocationData loc : locations)
		{
			foundLocationIds.add(loc.getLocationId());
		}
		assertThat(foundLocationIds).containsOnly("1", "2", "3", "4");
	}

	@Test
	@Transactional
	public void testFindLocationsByCountries()
	{
		LocationQueryObject locationQueryObject = new LocationQueryObject();
		locationQueryObject.setCountries(Arrays.asList("PL", "BR", "US"));
		List<StockroomLocationData> locations = inventoryService.findPagedLocations(locationQueryObject).getContent();
		Set<String> foundLocationIds = new HashSet<>();
		for (StockroomLocationData loc : locations)
		{
			foundLocationIds.add(loc.getLocationId());
		}
		assertThat(foundLocationIds).containsOnly("3", "136", "181", "26");
	}

	@Test
	@Transactional
	public void testFindLocationsBySingleLocationRole()
	{
		// given
		LocationQueryObject locationQueryObject = new LocationQueryObject();
		locationQueryObject.setLocationRoles(Arrays.asList("PICKUP"));

		// when
		List<StockroomLocationData> locations = inventoryService.findPagedLocations(locationQueryObject).getContent();

		// then
		Set<String> foundLocationIds = new HashSet<>();
		for (StockroomLocationData loc : locations)
		{
			foundLocationIds.add(loc.getLocationId());
		}
		assertThat(foundLocationIds).containsOnly("OMS97_location2");
	}

	@Test
	@Transactional
    @Ignore
	public void testFindLocationsByMultipleLocationRole()
	{
		// given
		LocationQueryObject locationQueryObject = new LocationQueryObject();
		locationQueryObject.setLocationRoles(Arrays.asList("PICKUP", "SHIPPING"));
		locationQueryObject.setPageSize(300);

		// when
		List<StockroomLocationData> locations = inventoryService.findPagedLocations(locationQueryObject).getContent();

		// then
		assertThat(locations.size()).isEqualTo(191);
	}

	@Test
	@Transactional
	public void testFindItemLocationCurrentBySkuIdAndLocationId()
	{
		ItemLocationData itemLocationCurrent = inventoryService.findItemLocationCurrentBySkuIdAndLocationId(ITEM_ID,
				LOCATION_ID);
		assertNotNull(itemLocationCurrent);
		assertEquals(ITEM_ID, itemLocationCurrent.getItemId());
		assertEquals(LOCATION_ID, itemLocationCurrent.getStockroomLocation().getLocationId());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testFindItemLocationCurrentBySkuIdAndLocationIdNotFound()
	{
		inventoryService.findItemLocationCurrentBySkuIdAndLocationId(INVALID_STRING, LOCATION_ID);
	}

	@Transactional
	@Test
	public void testFindLocationsByQuerySortByLocationDescription()
	{
		LocationQueryObject itemLocationQuery = new LocationQueryObject();
		itemLocationQuery.setLocationName("ore");
		LocationQuerySortSupport sorting = LocationQuerySortSupport.LOCATION_DESCRIPTION;

		itemLocationQuery.setSorting(sorting, SortDirection.ASC);
		List<StockroomLocationData> resultsAsc = inventoryService.findPagedLocations(itemLocationQuery).getContent();

		itemLocationQuery.setSorting(sorting, SortDirection.DESC);
		List<StockroomLocationData> resultsDesc = inventoryService.findPagedLocations(itemLocationQuery).getContent();

		verifySortingResults(3, resultsAsc, resultsDesc);
	}

	@Test
	@Transactional
	public void testFindLocationsByQuerySortByLocationId()
	{
		LocationQueryObject itemLocationQuery = new LocationQueryObject();
		itemLocationQuery.setLocationName("ore");
		LocationQuerySortSupport sorting = LocationQuerySortSupport.LOCATION_ID;

		itemLocationQuery.setSorting(sorting, SortDirection.ASC);
		List<StockroomLocationData> resultsAsc = inventoryService.findPagedLocations(itemLocationQuery).getContent();

		itemLocationQuery.setSorting(sorting, SortDirection.DESC);
		List<StockroomLocationData> resultsDesc = inventoryService.findPagedLocations(itemLocationQuery).getContent();

		verifySortingResults(3, resultsAsc, resultsDesc);
	}

	@Test
	@Transactional
	public void testFindLocationsByQuerySortByLocationPriority()
	{
		LocationQueryObject itemLocationQuery = new LocationQueryObject();
		itemLocationQuery.setLocationId("OMS");
		LocationQuerySortSupport sorting = LocationQuerySortSupport.LOCATION_PRIORITY;

		itemLocationQuery.setSorting(sorting, SortDirection.ASC);
		List<StockroomLocationData> resultsAsc = inventoryService.findPagedLocations(itemLocationQuery).getContent();

		itemLocationQuery.setSorting(sorting, SortDirection.DESC);
		List<StockroomLocationData> resultsDesc = inventoryService.findPagedLocations(itemLocationQuery).getContent();

		verifySortingResults(2, resultsAsc, resultsDesc);
	}

	@Test
	@Transactional
	public void testFindLocationsByQuerySortByLocationStoreName()
	{
		LocationQueryObject itemLocationQuery = new LocationQueryObject();
		itemLocationQuery.setLocationName("ore");
		LocationQuerySortSupport sorting = LocationQuerySortSupport.LOCATION_STORENAME;

		itemLocationQuery.setSorting(sorting, SortDirection.ASC);
		List<StockroomLocationData> resultsAsc = inventoryService.findPagedLocations(itemLocationQuery).getContent();

		itemLocationQuery.setSorting(sorting, SortDirection.DESC);
		List<StockroomLocationData> resultsDesc = inventoryService.findPagedLocations(itemLocationQuery).getContent();

		verifySortingResults(3, resultsAsc, resultsDesc);
	}

	@Test
	@Transactional
	public void testGetItemStatusByStatusCode()
	{
		ItemStatusData itemStatus = inventoryService.getItemStatusByStatusCode(ITEM_STATUS_STATUS_CODE);
		assertNotNull(itemStatus);
		assertEquals(ITEM_STATUS_STATUS_CODE, itemStatus.getStatusCode());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetItemStatusByStatusCodeNotFound()
	{
		inventoryService.getItemStatusByStatusCode(INVALID_STRING);
	}

	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void testCreateDuplicateItemStatus()
	{
		ItemStatusData itemStatus = pmgr.create(ItemStatusData.class);
		itemStatus.setStatusCode(ITEM_STATUS_STATUS_CODE);
		itemStatus.setDescription("");
		inventoryService.createItemStatus(itemStatus);
	}
	
	@Test
	@Transactional
	public void testGetLocationByLocationId()
	{
		StockroomLocationData location = inventoryService.getLocationByLocationId(LOCATION_ID);
		assertNotNull(location);
		assertEquals(LOCATION_ID, location.getLocationId());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetLocationByLocationIdNotFound()
	{
		inventoryService.getLocationByLocationId(INVALID_STRING);
	}

	@Test
	@Transactional
	public void testUpdateLocation()
	{

		StockroomLocationData location = pmgr.get(HybrisId.valueOf(LOCATION_HYBRIS_ID));
		location.setStoreName(LOCATION_STORE_NAME);
		inventoryService.updateLocation(location);

		location = pmgr.get(HybrisId.valueOf(LOCATION_HYBRIS_ID));
		assertEquals(LOCATION_STORE_NAME, location.getStoreName());
	}

	@Test
	@Transactional
	public void updateCurrentItemQuantity()
	{
		ItemQuantityData itemQuantity = pmgr.get(HybrisId.valueOf(CURRENT_ITEM_QUANTITY_HYBRIS_ID));
		itemQuantity.getQuantityValue();
		int quantity = 5;
		inventoryService.updateCurrentItemQuantity(itemQuantity.getOwner().getStockroomLocation().getLocationId(),
				InventoryServiceConstants.DEFAULT_BIN, itemQuantity.getOwner().getItemId(), ITEM_STATUS_STATUS_CODE, quantity);

		itemQuantity = pmgr.get(HybrisId.valueOf(CURRENT_ITEM_QUANTITY_HYBRIS_ID));
		assertEquals(quantity, itemQuantity.getQuantityValue());
	}

	@Test
	@Transactional
	public void updateCurrentItemQuantityIncremental()
	{
		ItemQuantityData itemQuantity = pmgr.get(HybrisId.valueOf(CURRENT_ITEM_QUANTITY_HYBRIS_ID));
		int oldQuantity = itemQuantity.getQuantityValue();
		int incrementQuantity = 5;
		inventoryService.updateCurrentItemQuantityIncremental(itemQuantity.getOwner().getStockroomLocation().getLocationId(),
				InventoryServiceConstants.DEFAULT_BIN, itemQuantity.getOwner().getItemId(), ITEM_STATUS_STATUS_CODE,
				incrementQuantity);

		itemQuantity = pmgr.get(HybrisId.valueOf(CURRENT_ITEM_QUANTITY_HYBRIS_ID));
		assertEquals((oldQuantity + incrementQuantity), itemQuantity.getQuantityValue());
	}

	@Test
	@Transactional
	public void updateFutureItemQuantity()
	{
		ItemQuantityData itemQuantity = pmgr.get(HybrisId.valueOf(FUTURE_ITEM_QUANTITY_HYBRIS_ID));

		int quantity = 5;
		inventoryService.updateFutureItemQuantity(itemQuantity.getOwner().getStockroomLocation().getLocationId(),
				InventoryServiceConstants.DEFAULT_BIN, itemQuantity.getOwner().getItemId(), ITEM_STATUS_STATUS_CODE, quantity,
				calendar.getTime());

		itemQuantity = pmgr.get(HybrisId.valueOf(FUTURE_ITEM_QUANTITY_HYBRIS_ID));
		assertEquals(quantity, itemQuantity.getQuantityValue());
	}

	@Test
	@Transactional
	public void updateFutureItemQuantityIncremental()
	{
		ItemQuantityData itemQuantity = pmgr.get(HybrisId.valueOf(FUTURE_ITEM_QUANTITY_HYBRIS_ID));
		int oldQuantity = itemQuantity.getQuantityValue();
		int incrementQuantity = 5;
		inventoryService.updateFutureItemQuantityIncremental(itemQuantity.getOwner().getStockroomLocation().getLocationId(),
				InventoryServiceConstants.DEFAULT_BIN, itemQuantity.getOwner().getItemId(), ITEM_STATUS_STATUS_CODE,
				incrementQuantity, calendar.getTime());

		itemQuantity = pmgr.get(HybrisId.valueOf(FUTURE_ITEM_QUANTITY_HYBRIS_ID));
		assertEquals((oldQuantity + incrementQuantity), itemQuantity.getQuantityValue());
	}

	@Test
	@Transactional
	public void testCreateBin()
	{
		StockroomLocationData location = pmgr.create(StockroomLocationData.class);
		location.setStoreName(LOCATION_STORE_NAME);
		location.setLocationId("loc100");
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		pmgr.flush();

		assertNotNull(location.getId());

		BinData bin = pmgr.create(BinData.class);
		bin.setBinCode("bin100");
		bin.setStockroomLocation(location);
		bin.setDescription("bin description");
		bin.setPriority(10);
		inventoryService.createBin(bin);

		assertNotNull(bin.getId());
	}

	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void testCreateBinDuplicateBinCode()
	{
		StockroomLocationData location = pmgr.create(StockroomLocationData.class);
		location.setStoreName(LOCATION_STORE_NAME);
		location.setLocationId("loc100");
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		pmgr.flush();

		assertNotNull(location.getId());

		BinData bin = pmgr.create(BinData.class);
		bin.setBinCode("bin100");
		bin.setStockroomLocation(location);
		bin.setDescription("bin description");
		bin.setPriority(10);
		inventoryService.createBin(bin);

		assertNotNull(bin.getId());

		BinData duplicate = pmgr.create(BinData.class);
		duplicate.setBinCode("bin100");
		duplicate.setStockroomLocation(location);
		duplicate.setDescription("bin description");
		duplicate.setPriority(10);
		inventoryService.createBin(duplicate);
		fail();
	}

	@Test(expected = ValidationException.class)
	@Transactional
	public void testCreateBinInvalidBinCode()
	{
		StockroomLocationData location = pmgr.create(StockroomLocationData.class);
		location.setStoreName(LOCATION_STORE_NAME);
		location.setLocationId("loc100");
		pmgr.flush();

		assertNotNull(location.getId());

		BinData bin = pmgr.create(BinData.class);
		bin.setBinCode(null);
		bin.setStockroomLocation(location);
		bin.setDescription("bin description");
		bin.setPriority(10);
		inventoryService.createBin(bin);
		fail();
	}

	private void verifySortingResults(int size, List<? extends ManagedObject> list1,
			List<? extends ManagedObject> list2)
	{
		assertEquals(size, list1.size());
		assertEquals(size, list2.size());
		Iterator<? extends ManagedObject> it1 = list1.iterator();
		ListIterator<? extends ManagedObject> it2 = list2.listIterator(list2.size());

		while (it1.hasNext())
		{
			ManagedObject elem1 = it1.next();
			ManagedObject elem2 = it2.previous();
			assertEquals(elem1.getId(), elem2.getId());
		}
	}

	@Test
	@Transactional
	public void testFindBinsByQuery()
	{
		BinQueryObject binQuery = new BinQueryObject();
		binQuery.setLocationId(BIN_LOCATION_ID);

		List<BinData> resultsAsc = inventoryService.findPagedBinsByQuery(binQuery).getContent();

		assertEquals(2, resultsAsc.size());
	}

	@Test
	@Transactional
	public void testFindBinsByQuerySortByBinCode()
	{
		BinQueryObject binQuery = new BinQueryObject();
		binQuery.setLocationId(BIN_LOCATION_ID);
		BinQuerySupport sorting = BinQuerySupport.DEFAULT;

		binQuery.setSorting(sorting, SortDirection.ASC);
		List<BinData> resultsAsc = inventoryService.findPagedBinsByQuery(binQuery).getContent();

		binQuery.setSorting(sorting, SortDirection.DESC);
		List<BinData> resultsDesc = inventoryService.findPagedBinsByQuery(binQuery).getContent();

		verifySortingResults(2, resultsAsc, resultsDesc);
	}

	@Test
	@Transactional
	public void testFindBinsByQuerySortByPriority()
	{
		BinQueryObject binQuery = new BinQueryObject();
		binQuery.setLocationId(BIN_LOCATION_ID);
		BinQuerySupport sorting = BinQuerySupport.BIN_PRIORITY;

		binQuery.setSorting(sorting, SortDirection.ASC);
		List<BinData> resultsAsc = inventoryService.findPagedBinsByQuery(binQuery).getContent();

		binQuery.setSorting(sorting, SortDirection.DESC);
		List<BinData> resultsDesc = inventoryService.findPagedBinsByQuery(binQuery).getContent();

		verifySortingResults(2, resultsAsc, resultsDesc);
	}

	@Test
	@Transactional
	public void testFindBinsByBinCodeQuery()
	{
		BinQueryObject binQuery = new BinQueryObject();
		binQuery.setBinCode("1");

		List<BinData> resultsAsc = inventoryService.findPagedBinsByQuery(binQuery).getContent();

		assertEquals(2, resultsAsc.size());
	}

	@Test
	@Transactional
	public void testGetBinByBinCodeLocationId()
	{
		BinData bin = inventoryService.getBinByBinCodeLocationId(BIN_CODE, BIN_LOCATION_ID);

		assertNotNull(bin);
		assertEquals(BIN_LOCATION_ID, bin.getStockroomLocation().getLocationId());
		assertEquals(BIN_CODE, bin.getBinCode());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetBinByBinCodeLocationIdNotFoundInvalidLocationId()
	{
		inventoryService.getBinByBinCodeLocationId(BIN_CODE, new Date().toString());
		fail();
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetBinByBinCodeLocationIdNotFoundInvalidBinCode()
	{
		inventoryService.getBinByBinCodeLocationId(new Date().toString(), BIN_LOCATION_ID);
		fail();
	}

	@Test
	@Transactional
    /* Assert that all of the inventory that should be removed currently exists. */
	public void testDeleteBin()
	{
		String itemLocationToDelete = "single|ItemLocationData|1";
		String currentItemQuantityToDelete = "single|CurrentItemQuantityData|1";

		pmgr.get(HybrisId.valueOf(itemLocationToDelete));
		pmgr.get(HybrisId.valueOf(currentItemQuantityToDelete));

		BinData binData = inventoryService.getBinByBinCodeLocationId(BIN_CODE, BIN_LOCATION_ID);
		assertNotNull(binData);

		inventoryService.deleteBin(binData);

		try
		{
			inventoryService.getBinByBinCodeLocationId(BIN_CODE, BIN_LOCATION_ID);
			fail("Bin should have been deleted");
		}
		catch (EntityNotFoundException ignored) { }

		try
		{
			pmgr.get(HybrisId.valueOf(itemLocationToDelete));
			fail("Item location should have been removed.");
		}
		catch (ManagedObjectNotFoundException e) { }

		try
		{
			pmgr.get(HybrisId.valueOf(currentItemQuantityToDelete));
			fail("Current item quantity should have been removed.");
		}
		catch (ManagedObjectNotFoundException e) { }
	}

	@Test
	@Transactional
	public void testUpdateBin()
	{
		String locationId = "loc" + new Date().toString();
		String binCode = "bin" + new Date().toString();

		StockroomLocationData location = pmgr.create(StockroomLocationData.class);
		location.setStoreName(LOCATION_STORE_NAME);
		location.setLocationId(locationId);
		inventoryService.createLocation(location);

		assertNotNull(location.getId());

		BinData bin = pmgr.create(BinData.class);
		bin.setBinCode(binCode);
		bin.setStockroomLocation(location);
		bin.setDescription("bin description");
		bin.setPriority(10);
		inventoryService.createBin(bin);

		assertNotNull(bin.getId());

		bin.setDescription("updated description");
		bin.setPriority(100);
		BinData updated = inventoryService.updateBin(bin);

		assertNotNull(updated);
		assertEquals("updated description", updated.getDescription());
		assertEquals(100, updated.getPriority());
	}

	@Test(expected = ValidationException.class)
	@Transactional
	public void testUpdateBinValidation()
	{
		String locationId = "loc" + new Date().toString();
		String binCode = "bin" + new Date().toString();

		StockroomLocationData location = pmgr.create(StockroomLocationData.class);
		location.setStoreName(LOCATION_STORE_NAME);
		location.setLocationId(locationId);
		inventoryService.createLocation(location);

		assertNotNull(location.getId());

		BinData bin = pmgr.create(BinData.class);
		bin.setBinCode(binCode);
		bin.setStockroomLocation(location);
		bin.setDescription("bin description");
		bin.setPriority(10);
		inventoryService.createBin(bin);

		assertNotNull(bin.getId());

		bin.setDescription("updated description");
		bin.setPriority(100);
		bin.setBinCode("bin" + new Date().toString());
		inventoryService.updateBin(bin);
	}
}
