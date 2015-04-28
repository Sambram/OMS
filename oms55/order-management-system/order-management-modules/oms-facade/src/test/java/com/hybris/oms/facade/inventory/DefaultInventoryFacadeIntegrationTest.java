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

import com.hybris.commons.conversion.ConversionException;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.facade.utils.DateUtils;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.inventory.impl.InventoryTestHelper;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


/**
 * Integration test for {@link DefaultInventoryFacade}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/oms-facade-spring-test.xml"})
@SuppressWarnings("PMD.ExcessiveImports")
public class DefaultInventoryFacadeIntegrationTest
{
	private static final Address ADDRESS = new Address();
	private static final String BIN_LOCATION_ID = "loc_00";
	private static final String BIN_CODE_2 = "BIN1";
	private static final Date DELIVERY_DATE = DateUtils.getTomorrowsDate();
	private static final String LOCATION_ID = "loc666";
	private static final String ADDRESS_LINE1 = "addressline1";
	private static final String SKU_ID = "sku666";
	private static final String BIN_CODE = "bin666";
	private static final String STATUS_CODE = "ON_HAND";
	private static final String INVALID = "INVALID";
	private static final String BIN_CODE_2_LOWER_CASE = "bin1";
	private static final String BASESTORE1_NAME = "baseStore1";
	private static final String BASESTORE2_NAME = "baseStore2";

	@Autowired
	private ImportService importService;

	@Resource
	private InventoryFacade inventoryFacade;

	@Autowired
	private BaseStoreFacade baseStoreFacade;

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setUp()
	{
		ADDRESS.setAddressLine1(ADDRESS_LINE1);
		importService.loadMcsvResource(new ClassPathResource("/META-INF/essential-data-import.mcsv"));
	}

	@Test
	@Transactional
	public void testCreateCurrentInventory()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, null, 0);

		// create inventory in DB
		this.inventoryFacade.createInventory(inventory);

		final List<ItemLocationData> itemLocations = this.persistenceManager.search(this.persistenceManager
				.createCriteriaQuery(ItemLocationData.class));
		Assert.assertNotNull(itemLocations);
		Assert.assertEquals(1, itemLocations.size());
		Assert.assertEquals(inventory.getSkuId(), itemLocations.get(0).getItemId());
		Assert.assertEquals(inventory.getLocationId(), itemLocations.get(0).getStockroomLocation().getLocationId());
	}

	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void testCreateDuplicateInventory()
	{
		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, null, 0);
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		inventoryFacade.createStockRoomLocation(location);
		final Bin bin = new Bin();
		bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
		bin.setLocationId(LOCATION_ID);

		inventoryFacade.createBin(bin);
		inventoryFacade.createInventory(inventory);
		inventoryFacade.createInventory(inventory);
	}

	@Test
	@Transactional
	public void testCreateFutureInventory()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, DELIVERY_DATE, 0);

		// create inventory in DB
		this.inventoryFacade.createInventory(inventory);

		final List<ItemLocationData> itemLocations = this.persistenceManager.search(this.persistenceManager
				.createCriteriaQuery(ItemLocationData.class));
		Assert.assertNotNull(itemLocations);
		Assert.assertEquals(1, itemLocations.size());
		Assert.assertEquals(inventory.getSkuId(), itemLocations.get(0).getItemId());
		Assert.assertEquals(inventory.getLocationId(), itemLocations.get(0).getStockroomLocation().getLocationId());
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testCreateInvalidInventory()
	{
		// inventory with no sku/location/statusCode
		final OmsInventory inventory = new OmsInventory();

		// Create valid bin before creating item location.
		final Bin bin = new Bin();
		bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
		bin.setPriority(1);
		bin.setLocationId(LOCATION_ID);
		this.inventoryFacade.createBin(bin);

		// create inventory in DB
		this.inventoryFacade.createInventory(inventory);
	}

	@Test
	@Transactional
	public void testCreateStockrooms()
	{
		final BaseStore baseStore1 = new BaseStore();
		baseStore1.setName(BASESTORE1_NAME);
		baseStoreFacade.createBaseStore(baseStore1);

		final BaseStore baseStore2 = new BaseStore();
		baseStore2.setName(BASESTORE2_NAME);
		baseStoreFacade.createBaseStore(baseStore2);

		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId("testId");
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setBaseStores(ImmutableSet.of(BASESTORE1_NAME, BASESTORE2_NAME));
		final Location result = this.inventoryFacade.createStockRoomLocation(location);
		Assert.assertNotNull(result);
		Assert.assertEquals(location.getLocationId(), result.getLocationId());
		final Set<String> baseStoreNames = result.getBaseStores();
		Assert.assertEquals(2, baseStoreNames.size());
		Assert.assertTrue(baseStoreNames.contains(BASESTORE1_NAME));
		Assert.assertTrue(baseStoreNames.contains(BASESTORE2_NAME));
	}

	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void testCreateStockroomsDuplicate()
	{
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId("testId");
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);
		this.inventoryFacade.createStockRoomLocation(location);
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testCreateStockroomsMissingLocationId()
	{
		final Location location = new Location();
		this.inventoryFacade.createStockRoomLocation(location);
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testCreateStockroomsMissingParentLocationId()
	{
		final Location location = new Location();
		this.inventoryFacade.createStockRoomLocation(location);
	}

	@Test
	@Transactional
	public void testCreateStockroomsInvalidBaseStore()
	{
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId("testId");
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setBaseStores(ImmutableSet.of(BASESTORE1_NAME, BASESTORE2_NAME));
		try
		{
			this.inventoryFacade.createStockRoomLocation(location);
			Assert.fail("ConversionException should be thrown");
		}
		catch (final ConversionException ex)
		{
			// Success
		}
	}

	@Test
	@Transactional
	public void testCreateUpdateCurrentInventory()
	{
		final int initialQty = 5;
		final int updateQty = 7;

		// need to create a location for the inventory
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, null, initialQty);

		// create a valid inventory
		this.inventoryFacade.createUpdateInventory(inventory);

		// update the inventory
		inventory.setQuantity(updateQty);
		final OmsInventory updated = this.inventoryFacade.createUpdateInventory(inventory);
		// verify if the quantity was updated
		Assert.assertNotNull(updated);
		Assert.assertEquals(initialQty + updateQty, updated.getQuantity());
	}

	@Test
	@Transactional
	public void testCreateUpdateFutureInventory()
	{
		final int initialQty = 5;
		final int updateQty = 7;

		// need to create a location for the inventory
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, DELIVERY_DATE,
				initialQty);

		// create a valid inventory
		this.inventoryFacade.createUpdateInventory(inventory);

		// update the inventory
		inventory.setQuantity(updateQty);
		final OmsInventory updated = this.inventoryFacade.createUpdateInventory(inventory);
		// verify if the quantity was updated
		Assert.assertNotNull(updated);
		Assert.assertEquals(initialQty + updateQty, updated.getQuantity());
	}

	@Test
	@Transactional
	public void testCreateUpdateStockRoomLocation()
	{
		final String id = "testId";
		final String oldDescription = "old description";
		final String newDescription = "new description";

		// first, create a valid location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(id);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setDescription(oldDescription);
		final Location created = this.inventoryFacade.createUpdateStockRoomLocation(location);
		Assert.assertNotNull(created);
		Assert.assertEquals(location.getLocationId(), created.getLocationId());

		// update the location description
		location.setDescription(newDescription);
		final Location updated = this.inventoryFacade.createUpdateStockRoomLocation(location);
		Assert.assertNotNull(updated);
		Assert.assertEquals(newDescription, updated.getDescription());
	}

	@Test
	@Transactional
	public void testDeleteCurrentInventory()
	{
		final int quantity = 5;

		// need to create a location for the inventory
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, null, quantity);

		// create a valid inventory
		this.inventoryFacade.createInventory(inventory);

		// delete inventory
		try
		{
			this.inventoryFacade.deleteInventory(inventory);
		}
		catch (final EntityNotFoundException e)
		{
			Assert.fail("exception not expected here");
		}
		// verify if inventory was deleted - search should throw ItemQuantityNotFoundException
		final List<ItemLocationData> itemLocations = this.persistenceManager.search(this.persistenceManager
				.createCriteriaQuery(ItemLocationData.class));
		Assert.assertTrue(itemLocations.isEmpty());
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testDeleteCurrentInventoryMissingLocation()
	{
		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, null, 5);
		this.inventoryFacade.deleteInventory(inventory);
	}

	@Test
	@Transactional
	public void testDeleteFutureInventory()
	{
		final int quantity = 5;

		// need to create a location for the inventory
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, DELIVERY_DATE,
				quantity);

		// create a valid inventory
		this.inventoryFacade.createInventory(inventory);

		// delete inventory
		this.inventoryFacade.deleteInventory(inventory);
		// verify if inventory was deleted
		final List<ItemLocationData> itemLocations = this.persistenceManager.search(this.persistenceManager
				.createCriteriaQuery(ItemLocationData.class));
		Assert.assertTrue(itemLocations.isEmpty());
	}

	@Test
	@Transactional
	public void testFindPageableMultipleItemLocationsByQuery()
	{
		// Create initial data
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);

		// 20 locations
		final List<StockroomLocationData> locations = helper.createLocationData(15);
		this.persistenceManager.flush();

		final List<String> skus = Arrays.asList("sku1", "sku2", "sku3");
		helper.createItemLocations(locations, skus);
		this.persistenceManager.flush();

		final int pageSize = 4;
		final List<String> desiredSkus = Arrays.asList("sku2", "sku3");
		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject();
		queryObject.setSkuIds(desiredSkus);
		queryObject.setPageSize(pageSize);

		final Pageable<ItemLocation> results = this.inventoryFacade.findItemLocationsByQuery(queryObject);

		final int totalExpected = locations.size() * desiredSkus.size();
		final double pagesExpected = Math.ceil(totalExpected / (double) pageSize);

		Assert.assertNotNull(results);
		Assert.assertEquals(totalExpected, results.getTotalRecords().intValue());
		Assert.assertEquals(results.getTotalPages().intValue(), pagesExpected, 0.1);

		for (final ItemLocation it : results.getResults())
		{
			Assert.assertNotNull(it.getItemId());
			Assert.assertTrue("sku2".equals(it.getItemId()) || "sku3".equals(it.getItemId()));
		}
	}

	@Test
	@Transactional
	public void testFindPageableStockroomLocations()
	{
		// Create initial data
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);

		// 20 locations
		final List<StockroomLocationData> locations = helper.createLocationData(20);

		helper.createItemLocations(locations, Arrays.asList("sku1", "sku2", "sku3"));

		this.persistenceManager.flush();

		final LocationQueryObject queryObject = new LocationQueryObject();
		queryObject.setPageNumber(0);
		queryObject.setPageSize(5);

		final Pageable<Location> results = this.inventoryFacade.findStockRoomLocationsByQuery(queryObject);

		Assert.assertEquals(results.getTotalPages().intValue(), 4);

		Assert.assertNotNull(results);

		Assert.assertEquals(20, results.getTotalRecords().intValue());

		for (final Location location : results.getResults())
		{
			Assert.assertNotNull(location);
			Assert.assertNotNull(location.getLocationId());
			Assert.assertNotNull(location.getDescription());
		}
	}

	@Test
	@Transactional
	public void testUpdateCurrentInventory()
	{
		final int initialQty = 5;
		final int incrementQty = 7;

		// need to create a location for the inventory
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, null, initialQty);

		// create a valid inventory
		this.inventoryFacade.createInventory(inventory);

		// update the inventory
		inventory.setQuantity(incrementQty);
		final OmsInventory updated = this.inventoryFacade.updateIncrementalInventory(inventory);
		// verify if the quantity was updated
		Assert.assertNotNull(updated);
		Assert.assertEquals(initialQty + incrementQty, updated.getQuantity());
	}

	@Test
	@Transactional
	public void testUpdateFutureInventory()
	{
		final int initialQty = 5;
		final int incrementQty = 7;

		// need to create a location for the inventory
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, DELIVERY_DATE,
				initialQty);

		// create a valid inventory
		this.inventoryFacade.createInventory(inventory);

		// update the inventory
		inventory.setQuantity(incrementQty);
		final OmsInventory updated = this.inventoryFacade.updateIncrementalInventory(inventory);
		// verify if the quantity was updated
		Assert.assertNotNull(updated);
		Assert.assertEquals(initialQty + incrementQty, updated.getQuantity());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testUpdateInexistentInventory()
	{
		final int quantity = 5;

		// need to create a location for the inventory
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = InventoryHelper.buildInventory(SKU_ID, LOCATION_ID, null, STATUS_CODE, null, quantity);

		// try to update inventory that is not in DB - should throw an ItemQuantityNotFoundException
		this.inventoryFacade.updateIncrementalInventory(inventory);
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testUpdateInexistentStockRoomLocation()
	{
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId("testId");
		location.setDescription("description");
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));

		this.inventoryFacade.updateStockRoomLocation(location);
	}

	@Test
	@Transactional
	public void testUpdateStockRoomLocation()
	{
		final String id = "testId";
		final String oldDescription = "old description";
		final String newDescription = "new description";

		// prepare BaseStore instances
		final BaseStore baseStore1 = new BaseStore();
		baseStore1.setName(BASESTORE1_NAME);
		baseStoreFacade.createBaseStore(baseStore1);

		final BaseStore baseStore2 = new BaseStore();
		baseStore2.setName(BASESTORE2_NAME);
		baseStoreFacade.createBaseStore(baseStore2);

		// create a valid location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(id);
		location.setDescription(oldDescription);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setBaseStores(ImmutableSet.of(BASESTORE1_NAME));
		this.inventoryFacade.createStockRoomLocation(location);
		final Location created = this.inventoryFacade.getStockRoomLocationByLocationId(id);
		Assert.assertEquals("Invalid number of BaseStore instances in response", 1, created.getBaseStores().size());
		Assert.assertTrue("Invalid BaseStore instance in response: " + created.getBaseStores().iterator().next(), created
				.getBaseStores().contains(BASESTORE1_NAME));

		// update the location description
		location.setDescription(newDescription);
		// update the location baseStores
		location.setBaseStores(ImmutableSet.of(BASESTORE2_NAME));
		final Location updated = this.inventoryFacade.updateStockRoomLocation(location);
		Assert.assertNotNull(updated);
		Assert.assertEquals(newDescription, updated.getDescription());
		Assert.assertNotSame(created.getDescription(), updated.getDescription());
		Assert.assertEquals("Invalid number of BaseStore instances in response", 1, updated.getBaseStores().size());
		Assert.assertTrue("Invalid BaseStore instance in response: " + updated.getBaseStores().iterator().next(), updated
				.getBaseStores().contains(BASESTORE2_NAME));
	}

	@Test
	@Transactional
	public void testUpdateStockRoomLocationInvalidBaseStore()
	{
		final String id = "testId";
		final String description = "description";

		// try to create a location with invalid BaseStore id
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(id);
		location.setDescription(description);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setBaseStores(ImmutableSet.of(BASESTORE1_NAME));
		try
		{
			this.inventoryFacade.createStockRoomLocation(location);
			Assert.fail("ConversionException should be thrown");
		}
		catch (final ConversionException ex)
		{
			// Success
		}
	}

	@Test
	@Transactional
	public void testCreateBin()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(BIN_CODE, LOCATION_ID, description, priority);

		final Bin created = this.inventoryFacade.createBin(bin);

		Assert.assertNotNull(created);
		Assert.assertEquals(BIN_CODE, created.getBinCode());
		Assert.assertEquals(LOCATION_ID, created.getLocationId());
		Assert.assertEquals(description, created.getDescription());
		Assert.assertEquals(priority, created.getPriority());
	}

	@Test(expected = DuplicateEntityException.class)
	@Transactional
	public void testCreateBinDuplicateBinCode()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(BIN_CODE, LOCATION_ID, description, priority);

		final Bin created = this.inventoryFacade.createBin(bin);
		Assert.assertNotNull(created);

		final Bin duplicate = InventoryHelper.buildBin(BIN_CODE, LOCATION_ID, description, priority);
		this.inventoryFacade.createBin(duplicate);
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testCreateBinCodeNull()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(null, LOCATION_ID, description, priority);

		this.inventoryFacade.createBin(bin);
		Assert.fail();
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testCreateBinLocationNull()
	{
		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(BIN_CODE, null, description, priority);

		this.inventoryFacade.createBin(bin);
		Assert.fail();
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testCreateBinInvalidLocation()
	{
		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(BIN_CODE, LOCATION_ID, description, priority);

		this.inventoryFacade.createBin(bin);
		Assert.fail();
	}

	@Test
	@Transactional
	public void testUpdateBin()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(BIN_CODE, LOCATION_ID, description, priority);

		final Bin created = this.inventoryFacade.createBin(bin);

		Assert.assertNotNull(created);

		final String updatedDescription = "updated description";
		final int updatedPriority = 100;

		bin.setDescription(updatedDescription);
		bin.setPriority(updatedPriority);

		final Bin updated = this.inventoryFacade.updateBin(bin);

		Assert.assertEquals(BIN_CODE, updated.getBinCode());
		Assert.assertEquals(LOCATION_ID, updated.getLocationId());
		Assert.assertEquals(updatedDescription, updated.getDescription());
		Assert.assertEquals(updatedPriority, updated.getPriority());
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testUpdateBinInvalidLocation()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(BIN_CODE, LOCATION_ID, description, priority);

		final Bin created = this.inventoryFacade.createBin(bin);

		Assert.assertNotNull(created);

		bin.setLocationId("loc777");
		this.inventoryFacade.updateBin(bin);
		Assert.fail();
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testUpdateBinInvalidLocationAndBincCode()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(BIN_CODE, LOCATION_ID, description, priority);

		final Bin created = this.inventoryFacade.createBin(bin);

		Assert.assertNotNull(created);

		bin.setLocationId("bin777");
		bin.setLocationId("loc777");
		this.inventoryFacade.updateBin(bin);
		Assert.fail();
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testUpdateBinLocation()
	{
		// first, create a location
		final Location location = new Location();
		location.setAddress(ADDRESS);
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(location);

		final Location anotherLocation = new Location();
		anotherLocation.setAddress(ADDRESS);
		anotherLocation.setLocationId("loc777");
		anotherLocation.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		this.inventoryFacade.createStockRoomLocation(anotherLocation);

		final String description = "bin description";
		final int priority = 10;
		final Bin bin = InventoryHelper.buildBin(BIN_CODE, LOCATION_ID, description, priority);

		final Bin created = this.inventoryFacade.createBin(bin);

		Assert.assertNotNull(created);

		bin.setLocationId("loc777");
		this.inventoryFacade.updateBin(bin);
		Assert.fail();
	}

	@Test
	@Transactional
	public void testFindPageableBinsByQuery()
	{
		// Create initial data
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);

		// 10 locations
		final List<StockroomLocationData> locations = helper.createLocationData(10);

		helper.createBinData(locations);

		this.persistenceManager.flush();

		final BinQueryObject queryObject = new BinQueryObject();
		queryObject.setPageSize(5);

		final Pageable<Bin> results = this.inventoryFacade.findBinsByQuery(queryObject);

		Assert.assertEquals(results.getTotalPages().intValue(), 2);

		Assert.assertNotNull(results);
		Assert.assertEquals(5, results.getResults().size());

	}

	@Test
	@Transactional
	public void testGetBinByBinCodeLocationId()
	{
		// Create initial data
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);

		// 10 locations
		final List<StockroomLocationData> locations = helper.createLocationData(10);

		helper.createBinData(locations);

		this.persistenceManager.flush();

		final Bin bin = this.inventoryFacade.getBinByBinCodeLocationId(BIN_CODE_2_LOWER_CASE, BIN_LOCATION_ID);

		Assert.assertNotNull(bin);
		Assert.assertEquals(BIN_CODE_2_LOWER_CASE, bin.getBinCode());
		Assert.assertEquals(BIN_LOCATION_ID, bin.getLocationId());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetBinByBinCodeLocationIdInvalidLocationId()
	{
		// Create initial data
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);

		// 10 locations
		final List<StockroomLocationData> locations = helper.createLocationData(10);

		helper.createBinData(locations);

		this.persistenceManager.flush();

		this.inventoryFacade.getBinByBinCodeLocationId(BIN_CODE_2, INVALID);
		Assert.fail();
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetBinByBinCodeLocationIdInvalidBinCode()
	{
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);
		final List<StockroomLocationData> locations = helper.createLocationData(10);

		helper.createBinData(locations);

		this.persistenceManager.flush();

		this.inventoryFacade.getBinByBinCodeLocationId(INVALID, BIN_LOCATION_ID);
		Assert.fail();
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testDeleteBin()
	{
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);
		final List<StockroomLocationData> locations = helper.createLocationData(10);

		helper.createBinData(locations);

		this.persistenceManager.flush();

		this.inventoryFacade.deleteBinByBinCodeLocationId(BIN_CODE_2, BIN_LOCATION_ID);

		// this should throw an EntityNotFoundException because the entity has been deleted
		this.inventoryFacade.deleteBinByBinCodeLocationId(BIN_CODE_2, BIN_LOCATION_ID);
		Assert.fail();
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testDeleteBinNotFoundInvalidLocationId()
	{
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);
		// 10 locations
		final List<StockroomLocationData> locations = helper.createLocationData(10);

		helper.createBinData(locations);

		this.persistenceManager.flush();

		this.inventoryFacade.deleteBinByBinCodeLocationId(BIN_CODE_2, new Date().toString());
		Assert.fail();
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testDeleteBinNotFoundInvalidBinCode()
	{
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);
		final List<StockroomLocationData> locations = helper.createLocationData(10);

		helper.createBinData(locations);

		this.persistenceManager.flush();

		this.inventoryFacade.deleteBinByBinCodeLocationId(new Date().toString(), BIN_LOCATION_ID);
		Assert.fail();
	}

	@Test
	@Transactional
	public void testGetBinCaseInsensitiveForBinCode()
	{
		final InventoryTestHelper helper = new InventoryTestHelper(this.persistenceManager);
		final List<StockroomLocationData> locations = helper.createLocationData(10);

		helper.createBinData(locations);

		this.persistenceManager.flush();


		final Bin binLowerCase = this.inventoryFacade.getBinByBinCodeLocationId(BIN_CODE_2, BIN_LOCATION_ID);
		final Bin binUpperCase = this.inventoryFacade.getBinByBinCodeLocationId(BIN_CODE_2_LOWER_CASE, BIN_LOCATION_ID);

		Assert.assertEquals(binLowerCase.getBinCode(), binUpperCase.getBinCode());

	}



}
