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

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.BinQuerySupport;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultInventorySKUScenarioIntegrationTest
{
	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private final DefaultInventoryService inventoryService = new DefaultInventoryService();

	@Test
	@Transactional
	public void createAndFindStockRoomLocationWithoutBin()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		final Set<String> inputLocations = new HashSet<String>();
		inputLocations.add(location.getLocationId());
		final List<StockroomLocationData> locations = inventoryService.findLocationsByLocationIds(inputLocations);
		Assert.assertNotNull(locations);
		Assert.assertEquals(1, locations.size());
		Assert.assertNotNull(locations.get(0));
		Assert.assertEquals(location.getLocationId(), locations.get(0).getLocationId());
	}

	@Test
	@Transactional
	public void createAndFindStockRoomLocationWithoutBinExCase()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		final Set<String> inputLocations = new HashSet<String>();
		inputLocations.add(location.getLocationId());

		final List<StockroomLocationData> locations = inventoryService.findLocationsByLocationIds(inputLocations);
		Assert.assertNotNull(locations);
		Assert.assertEquals(1, locations.size());
		Assert.assertNotNull(locations.get(0));
		Assert.assertEquals(location.getLocationId(), locations.get(0).getLocationId());

		final BinQueryObject qb = new BinQueryObject();
		qb.setBinCode("default_bin");
		qb.setLocationId(location.getLocationId());

		final List<BinData> bins = this.inventoryService.findPagedBinsByQuery(qb).getContent();
		Assert.assertNotNull(bins);
		Assert.assertEquals(0, bins.size());

		qb.setBinCode("B1");
		final List<BinData> mbins = this.inventoryService.findPagedBinsByQuery(qb).getContent();
		Assert.assertNotNull(mbins);
		Assert.assertEquals(0, mbins.size());
	}

	@Test
	@Transactional
	public void createAndFindStockRoomLocationWithBin()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		final BinData bin = helper.createBin(location, "B1");
		this.persistenceManager.flush();

		final Set<String> inputLocations = new HashSet<String>();
		inputLocations.add(location.getLocationId());
		final List<StockroomLocationData> locations = inventoryService.findLocationsByLocationIds(inputLocations);

		final BinQueryObject qb = new BinQueryObject();
		qb.setBinCode("B1");
		qb.setLocationId(location.getLocationId());

		final List<BinData> bins = this.inventoryService.findPagedBinsByQuery(qb).getContent();
		Assert.assertNotNull(locations);
		Assert.assertEquals(1, locations.size());
		Assert.assertNotNull(locations.get(0));
		Assert.assertEquals(location.getLocationId(), locations.get(0).getLocationId());
		Assert.assertNotNull(bins);
		Assert.assertEquals(1, bins.size());
		Assert.assertNotNull(bins.get(0));
		Assert.assertEquals(bin.getBinCode(), bins.get(0).getBinCode());
	}

	@Test
	@Transactional
	public void createAndFindStockRoomLocationWithDefaultBin()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		final BinData bin = helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		this.persistenceManager.flush();

		final Set<String> inputLocations = new HashSet<String>();
		inputLocations.add(location.getLocationId());
		final List<StockroomLocationData> locations = inventoryService.findLocationsByLocationIds(inputLocations);

		final List<BinData> bins = this.inventoryService.findPagedBinsByQuery(
				new BinQueryObject("B1", location.getLocationId(), "")).getContent();
		Assert.assertNotNull(locations);
		Assert.assertEquals(1, locations.size());
		Assert.assertNotNull(locations.get(0));
		Assert.assertEquals(location.getLocationId(), locations.get(0).getLocationId());
		Assert.assertNotNull(bins);
		Assert.assertNotNull(bin);
		Assert.assertEquals(0, bins.size());
	}

	@Test
	@Transactional
	public void createAndFindStockRoomLocationWithMultipleBins()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		helper.createBin(location, "default_bin");
		helper.createBin(location, "B1");
		helper.createBin(location, "B2");
		helper.createBin(location, "B3");

		this.persistenceManager.flush();

		final Set<String> inputLocations = new HashSet<String>();
		inputLocations.add(location.getLocationId());
		final List<StockroomLocationData> locations = inventoryService.findLocationsByLocationIds(inputLocations);
		Assert.assertNotNull(locations);
		Assert.assertEquals(1, locations.size());
		Assert.assertNotNull(locations.get(0));
		Assert.assertEquals(location.getLocationId(), locations.get(0).getLocationId());

		final BinQueryObject query = new BinQueryObject();
		query.setLocationId(location.getLocationId());
		final List<BinData> bins = this.inventoryService.findPagedBinsByQuery(query).getContent();

		Assert.assertNotNull(bins);
		Assert.assertEquals(3, bins.size());
		// To cover addBinsToExistingStockRoomLocation
		helper.createBin(location, "B4");
		helper.createBin(location, "B5");
		helper.createBin(location, "B6");
		this.persistenceManager.flush();

		final List<BinData> mbins = this.inventoryService.findPagedBinsByQuery(query).getContent();
		Assert.assertNotNull(mbins);
		Assert.assertEquals(6, mbins.size());
	}

	@Test
	@Transactional
	public void createAndFindCurrentItemLocationDataWithBin()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		final BinData bin = helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		this.persistenceManager.flush();

		final String skuId = "S1";
		final boolean future = false;

		final ItemLocationData itemLocation = helper.createItemLocation(skuId, location, bin, future);
		final List<ItemLocationData> data = this.inventoryService.findAllItemLocations();
		Assert.assertNotNull(data);
		Assert.assertEquals(1, data.size());

		final ItemLocationData resultILD = this.inventoryService.findItemLocationCurrentBySkuIdAndLocationId(skuId,
				location.getLocationId());
		Assert.assertNotNull(resultILD);
		Assert.assertNotNull(itemLocation);
		Assert.assertEquals(itemLocation.isFuture(), resultILD.isFuture());
		Assert.assertEquals(itemLocation.getItemId(), resultILD.getItemId());
		Assert.assertEquals(itemLocation.getBin().getStockroomLocation().getLocationId(), resultILD.getBin().getStockroomLocation()
				.getLocationId());
		Assert.assertEquals(itemLocation.getBin().getBinCode(), resultILD.getBin().getBinCode());
	}

	@Test
	@Transactional
	@Ignore
	public void testFindItemLocationCurrentBySkuIdAndLocationId()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		final BinData bin = helper.createBin(location, "B1");
		final BinData bin1 = helper.createBin(location, "B2");
		helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		this.persistenceManager.flush();

		final String skuId = "S1";
		final boolean future = false;

		final ItemLocationData itemLocation = helper.createItemLocation(skuId, location, bin, future);
		final ItemLocationData itemLocation1 = helper.createItemLocation(skuId, location, bin1, future);

		final ItemLocationData resultILD = this.inventoryService.findItemLocationCurrentBySkuIdAndLocationId(skuId,
				location.getLocationId());
		Assert.assertNotNull(resultILD);

		Assert.assertNotNull(itemLocation);
		Assert.assertEquals(itemLocation.isFuture(), resultILD.isFuture());
		Assert.assertEquals(itemLocation.getItemId(), resultILD.getItemId());
		Assert.assertEquals(itemLocation.getBin().getStockroomLocation().getLocationId(), resultILD.getBin().getStockroomLocation()
				.getLocationId());
		Assert.assertEquals(itemLocation.getBin().getBinCode(), resultILD.getBin().getBinCode());

		Assert.assertNotNull(itemLocation1);
		Assert.assertEquals(itemLocation1.isFuture(), resultILD.isFuture());
		Assert.assertEquals(itemLocation1.getItemId(), resultILD.getItemId());
		Assert.assertEquals(itemLocation1.getBin().getStockroomLocation().getLocationId(), resultILD.getBin()
				.getStockroomLocation().getLocationId());
		Assert.assertEquals(itemLocation1.getBin().getBinCode(), resultILD.getBin().getBinCode());
	}

	@Test
	@Transactional
	public void createAndFindCurrentItemLocationDataWithMultipleBins()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		final BinData bin = helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		final BinData bin1 = helper.createBin(location, "B1");
		this.persistenceManager.flush();

		final String skuId = "S1";
		final boolean future = false;

		final ItemLocationData itemLocation = helper.createItemLocation(skuId, location, bin, future);
		helper.createItemLocation(skuId, location, bin1, future);

		final List<ItemLocationData> data = this.inventoryService.findAllItemLocations();
		Assert.assertNotNull(data);
		Assert.assertEquals(1, data.size());

		Assert.assertNotNull(itemLocation);
		Assert.assertEquals(itemLocation.isFuture(), data.get(0).isFuture());
		Assert.assertEquals(itemLocation.getItemId(), data.get(0).getItemId());
		Assert.assertEquals(itemLocation.getBin().getStockroomLocation().getLocationId(), data.get(0).getBin()
				.getStockroomLocation().getLocationId());
	}

	@Test(expected = ValidationException.class)
	@Transactional
	public void createAndFindCurrentItemLocationDataWithNullBin()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		this.persistenceManager.flush();
		final String skuId = "S1";
		final boolean future = false;
		final ItemLocationData itemLocation = helper.createItemLocation(skuId, location, null, future);
		Assert.assertNotNull(itemLocation);
	}

	@Test
	@Transactional
	public void createAndFindCurrentItemLocationDataWithDefaultBin()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		this.persistenceManager.flush();
		final String skuId = "S1";
		final boolean future = false;
		// Create Default Bin. This is the responsibility of Facade layer to prepare the data.
		final BinData bin = helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		final ItemLocationData itemLocation = helper.createItemLocation(skuId, location, bin, future);
		Assert.assertNotNull(itemLocation);
	}

	@Test
	@Transactional
	public void createFutureItemLocationDataWithBin()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		this.persistenceManager.flush();
		final String skuId = "S1";
		final boolean future = true;
		// Create Default Bin. This is the responsibility of Facade layer to prepare the data.
		final BinData bin = helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		final ItemLocationData itemLocation = helper.createItemLocation(skuId, location, bin, future);
		Assert.assertNotNull(itemLocation);
	}

	@Test(expected = ValidationException.class)
	@Transactional
	public void createFutureItemLocationDataWithNullBin()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		this.persistenceManager.flush();
		final String skuId = "S1";
		final boolean future = true;
		final ItemLocationData itemLocation = helper.createItemLocation(skuId, location, null, future);
		Assert.assertNotNull(itemLocation);
	}

	@Test(expected = ValidationException.class)
	@Transactional
	public void createItemLocationDataWithoutLocation()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final String skuId = "S1";
		final boolean future = true;
		final ItemLocationData itemLocation = helper.createItemLocation(skuId, null, null, future);
		Assert.assertNotNull(itemLocation);
	}

	@Test
	@Transactional
	public void findAllLocationsWithoutBinCreation()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		this.persistenceManager.flush();

		final CriteriaQuery<StockroomLocationData> query = this.persistenceManager.createCriteriaQuery(StockroomLocationData.class);
		final List<StockroomLocationData> data = this.persistenceManager.search(query);
		Assert.assertNotNull(data);
		Assert.assertEquals(1, data.size());
	}

	@Test
	@Transactional
	public void testFindLocationsByLocationIds()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		helper.createBin(location, "B1");
		helper.createBin(location, "B2");
		helper.createBin(location, "B3");

		final StockroomLocationData location1 = helper.createStockRoomLocation("L2");
		Assert.assertNotNull(location1);
		helper.createBin(location1, "B1");
		helper.createBin(location1, "B2");
		helper.createBin(location1, "B3");
		helper.createBin(location1, "B4");
		this.persistenceManager.flush();

		final List<StockroomLocationData> locations = this.inventoryService.findLocationsByLocationIds(Sets.newHashSet("L1", "L2"));
		Assert.assertNotNull(locations);
		Assert.assertEquals(2, locations.size());

		final Set<String> foundLocationIds = new HashSet<>();
		for (final StockroomLocationData loc : locations)
		{
			foundLocationIds.add(loc.getLocationId());
		}
		Assertions.assertThat(foundLocationIds).containsOnly("L1", "L2");
	}

	@Test
	@Transactional
	public void findItemLocationFutureBySkuLocation()
	{

		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		// Create StockRoomLocation
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		this.persistenceManager.flush();

		// Create Bins and assign to stockroom location
		final BinData defaultBin = helper.createBin(location, InventoryServiceConstants.DEFAULT_BIN);
		final BinData bin1 = helper.createBin(location, "B1");
		final BinData bin2 = helper.createBin(location, "B2");
		this.persistenceManager.flush();

		// Create Item Locations
		final ItemLocationData ilD1 = helper.createItemLocation("SKU1", location, defaultBin, true);
		final ItemLocationData il1 = helper.createItemLocation("SKU1", location, bin1, true);

		final ItemLocationData ilD2 = helper.createItemLocation("SKU2", location, defaultBin, true);
		final ItemLocationData il2 = helper.createItemLocation("SKU2", location, bin2, true);

		this.persistenceManager.flush();

		// Create Item Status
		final ItemStatusData status = this.persistenceManager.create(ItemStatusData.class);
		status.setStatusCode("ON_HAND");
		status.setDescription("ON_HAND");
		this.persistenceManager.flush();

		// Create ItemQuantities with item status
		final Date expectedDeliveryDate = Calendar.getInstance().getTime();
		helper.createItemQuantityData(status, ilD1, expectedDeliveryDate);
		helper.createItemQuantityData(status, ilD2, expectedDeliveryDate);
		helper.createItemQuantityData(status, il1, expectedDeliveryDate);
		helper.createItemQuantityData(status, il2, expectedDeliveryDate);
		this.persistenceManager.flush();

		// Test all the inventory find methods for the above test data (default bin + non-default bin)
		final List<FutureItemQuantityData> qdata = inventoryService.findAllExpectedDeliveryDate();
		Assert.assertNotNull(qdata);
		Assert.assertEquals(4, qdata.size());

		final List<ItemStatusData> statusData = inventoryService.findAllItemStatuses();
		Assert.assertNotNull(statusData);
		Assert.assertEquals(1, statusData.size());

		final List<ItemLocationData> qdata1 = inventoryService.findAllFutureItemLocationsByDate(expectedDeliveryDate);
		Assert.assertNotNull(qdata1);
		Assert.assertEquals(4, qdata1.size());

		// Returns only default-bin item quantity
		final ItemLocationData data = inventoryService.findItemLocationFutureBySkuLocation("SKU1", location.getLocationId());
		Assert.assertNotNull(data);

		// Returns only default-bin item quantity
		final ItemLocationData data2 = inventoryService.findItemLocationFutureBySkuLocation("SKU2", location.getLocationId());
		Assert.assertNotNull(data2);

	}

	@Test
	@Transactional
	public void findItemLocationCurrentBySkuIdAndLocationIdSortBinCodeAsc()
	{

		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		this.persistenceManager.flush();

		final BinData bin1 = helper.createBin(location, "B1");
		final BinData bin2 = helper.createBin(location, "B2");
		final BinData bin3 = helper.createBin(location, "B3");
		this.persistenceManager.flush();

		final ItemLocationData il1 = helper.createItemLocation("SKU1", location, bin1, false);
		final ItemLocationData il2 = helper.createItemLocation("SKU1", location, bin2, false);
		final ItemLocationData il3 = helper.createItemLocation("SKU1", location, bin3, false);

		final ItemLocationData il4 = helper.createItemLocation("SKU2", location, bin1, false);
		final ItemLocationData il5 = helper.createItemLocation("SKU2", location, bin2, false);
		final ItemLocationData il6 = helper.createItemLocation("SKU2", location, bin3, false);
		this.persistenceManager.flush();

		final ItemQuantityData iqd1 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd1.setQuantityUnitCode("unitCode");
		iqd1.setQuantityValue(5);
		iqd1.setStatusCode("SOMECODE");
		iqd1.setOwner(il1);

		this.persistenceManager.flush();
		final ItemQuantityData iqd2 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd2.setQuantityUnitCode("unitCode");
		iqd2.setQuantityValue(5);
		iqd2.setStatusCode("SOMECODE");
		iqd2.setOwner(il2);

		this.persistenceManager.flush();
		Assert.assertNotNull(il3);
		Assert.assertNotNull(il4);
		Assert.assertNotNull(il5);
		Assert.assertNotNull(il6);

		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING, BinQuerySupport.BIN_CODE.toString());
		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION, SortDirection.ASC.toString());

		final List<BinData> data = inventoryService.findBinsBySkuAndLocationId("SKU1", location.getLocationId());
		Assert.assertNotNull(data);
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("B1", data.get(0).getBinCode());
		Assert.assertEquals("B2", data.get(1).getBinCode());
		Assert.assertEquals("B3", data.get(2).getBinCode());
	}

	@Test
	@Transactional
	public void findItemLocationCurrentBySkuIdAndLocationIdSortBinCodeDesc()
	{

		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		this.persistenceManager.flush();

		final BinData bin1 = helper.createBin(location, "B1");
		final BinData bin2 = helper.createBin(location, "B2");
		final BinData bin3 = helper.createBin(location, "B3");
		this.persistenceManager.flush();

		final ItemLocationData il1 = helper.createItemLocation("SKU1", location, bin1, false);
		final ItemLocationData il2 = helper.createItemLocation("SKU1", location, bin2, false);
		final ItemLocationData il3 = helper.createItemLocation("SKU1", location, bin3, false);

		final ItemLocationData il4 = helper.createItemLocation("SKU2", location, bin1, false);
		final ItemLocationData il5 = helper.createItemLocation("SKU2", location, bin2, false);
		final ItemLocationData il6 = helper.createItemLocation("SKU2", location, bin3, false);
		this.persistenceManager.flush();

		final ItemQuantityData iqd1 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd1.setQuantityUnitCode("unitCode");
		iqd1.setQuantityValue(5);
		iqd1.setStatusCode("SOMECODE");
		iqd1.setOwner(il1);

		this.persistenceManager.flush();
		final ItemQuantityData iqd2 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd2.setQuantityUnitCode("unitCode");
		iqd2.setQuantityValue(5);
		iqd2.setStatusCode("SOMECODE");
		iqd2.setOwner(il2);

		this.persistenceManager.flush();
		Assert.assertNotNull(il3);
		Assert.assertNotNull(il4);
		Assert.assertNotNull(il5);
		Assert.assertNotNull(il6);

		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING, BinQuerySupport.BIN_CODE.toString());
		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION, SortDirection.DESC.toString());

		final List<BinData> data = inventoryService.findBinsBySkuAndLocationId("SKU1", location.getLocationId());
		Assert.assertNotNull(data);
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("B1", data.get(2).getBinCode());
		Assert.assertEquals("B2", data.get(1).getBinCode());
		Assert.assertEquals("B3", data.get(0).getBinCode());
	}

	@Test
	@Transactional
	public void findItemLocationCurrentBySkuIdAndLocationIdSortPriorityAsc()
	{

		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		this.persistenceManager.flush();

		final BinData bin1 = helper.createBin(location, "B1", 10);
		final BinData bin2 = helper.createBin(location, "B2", 15);
		final BinData bin3 = helper.createBin(location, "B3", 5);
		this.persistenceManager.flush();

		final ItemLocationData il1 = helper.createItemLocation("SKU1", location, bin1, false);
		final ItemLocationData il2 = helper.createItemLocation("SKU1", location, bin2, false);
		final ItemLocationData il3 = helper.createItemLocation("SKU1", location, bin3, false);

		final ItemLocationData il4 = helper.createItemLocation("SKU2", location, bin1, false);
		final ItemLocationData il5 = helper.createItemLocation("SKU2", location, bin2, false);
		final ItemLocationData il6 = helper.createItemLocation("SKU2", location, bin3, false);
		this.persistenceManager.flush();

		final ItemQuantityData iqd1 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd1.setQuantityUnitCode("unitCode");
		iqd1.setQuantityValue(5);
		iqd1.setStatusCode("SOMECODE");
		iqd1.setOwner(il1);

		this.persistenceManager.flush();
		final ItemQuantityData iqd2 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd2.setQuantityUnitCode("unitCode");
		iqd2.setQuantityValue(5);
		iqd2.setStatusCode("SOMECODE");
		iqd2.setOwner(il2);

		this.persistenceManager.flush();
		Assert.assertNotNull(il3);
		Assert.assertNotNull(il4);
		Assert.assertNotNull(il5);
		Assert.assertNotNull(il6);

		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING, BinQuerySupport.BIN_PRIORITY.toString());
		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION, SortDirection.ASC.toString());

		final List<BinData> data = inventoryService.findBinsBySkuAndLocationId("SKU1", location.getLocationId());
		Assert.assertNotNull(data);
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("B3", data.get(0).getBinCode());
		Assert.assertEquals("B1", data.get(1).getBinCode());
		Assert.assertEquals("B2", data.get(2).getBinCode());
	}

	@Test
	@Transactional
	public void findItemLocationCurrentBySkuIdAndLocationIdDefaultSort()
	{

		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		this.persistenceManager.flush();

		final BinData bin1 = helper.createBin(location, "B1", 10);
		final BinData bin2 = helper.createBin(location, "B2", 15);
		final BinData bin3 = helper.createBin(location, "B3", 5);
		this.persistenceManager.flush();

		final ItemLocationData il1 = helper.createItemLocation("SKU1", location, bin1, false);
		final ItemLocationData il2 = helper.createItemLocation("SKU1", location, bin2, false);
		final ItemLocationData il3 = helper.createItemLocation("SKU1", location, bin3, false);

		final ItemLocationData il4 = helper.createItemLocation("SKU2", location, bin1, false);
		final ItemLocationData il5 = helper.createItemLocation("SKU2", location, bin2, false);
		final ItemLocationData il6 = helper.createItemLocation("SKU2", location, bin3, false);
		this.persistenceManager.flush();

		final ItemQuantityData iqd1 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd1.setQuantityUnitCode("unitCode");
		iqd1.setQuantityValue(5);
		iqd1.setStatusCode("SOMECODE");
		iqd1.setOwner(il1);

		this.persistenceManager.flush();
		final ItemQuantityData iqd2 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd2.setQuantityUnitCode("unitCode");
		iqd2.setQuantityValue(5);
		iqd2.setStatusCode("SOMECODE");
		iqd2.setOwner(il2);

		this.persistenceManager.flush();
		Assert.assertNotNull(il3);
		Assert.assertNotNull(il4);
		Assert.assertNotNull(il5);
		Assert.assertNotNull(il6);

		// Since tenant preference data is not provided, bins should sorted using the default strategy (priority) and the
		// default direction (asc)
		final List<BinData> data = inventoryService.findBinsBySkuAndLocationId("SKU1", location.getLocationId());
		Assert.assertNotNull(data);
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("B3", data.get(0).getBinCode());
		Assert.assertEquals("B1", data.get(1).getBinCode());
		Assert.assertEquals("B2", data.get(2).getBinCode());
	}

	@Test
	@Transactional
	public void findItemLocationCurrentBySkuIdAndLocationIdSortDescriptionAsc()
	{

		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		this.persistenceManager.flush();

		final BinData bin1 = helper.createBin(location, "B1", 15, "Y1");
		final BinData bin2 = helper.createBin(location, "B2", 10, "F1");
		final BinData bin3 = helper.createBin(location, "B3", 5, "Z1");
		this.persistenceManager.flush();

		final ItemLocationData il1 = helper.createItemLocation("SKU1", location, bin1, false);
		final ItemLocationData il2 = helper.createItemLocation("SKU1", location, bin2, false);
		final ItemLocationData il3 = helper.createItemLocation("SKU1", location, bin3, false);

		final ItemLocationData il4 = helper.createItemLocation("SKU2", location, bin1, false);
		final ItemLocationData il5 = helper.createItemLocation("SKU2", location, bin2, false);
		final ItemLocationData il6 = helper.createItemLocation("SKU2", location, bin3, false);
		this.persistenceManager.flush();

		final ItemQuantityData iqd1 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd1.setQuantityUnitCode("unitCode");
		iqd1.setQuantityValue(5);
		iqd1.setStatusCode("SOMECODE");
		iqd1.setOwner(il1);

		this.persistenceManager.flush();
		final ItemQuantityData iqd2 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd2.setQuantityUnitCode("unitCode");
		iqd2.setQuantityValue(5);
		iqd2.setStatusCode("SOMECODE");
		iqd2.setOwner(il2);

		this.persistenceManager.flush();
		Assert.assertNotNull(il3);
		Assert.assertNotNull(il4);
		Assert.assertNotNull(il5);
		Assert.assertNotNull(il6);

		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING, BinQuerySupport.BIN_DESCRIPTION.toString());
		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION, SortDirection.ASC.toString());

		final List<BinData> data = inventoryService.findBinsBySkuAndLocationId("SKU1", location.getLocationId());
		Assert.assertNotNull(data);
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("B2", data.get(0).getBinCode());
		Assert.assertEquals("B1", data.get(1).getBinCode());
		Assert.assertEquals("B3", data.get(2).getBinCode());
	}

	@Test
	@Transactional
	public void findItemLocationCurrentBySkuIdAndLocationIdSortDescriptionDesc()
	{

		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		Assert.assertNotNull(location);
		this.persistenceManager.flush();

		final BinData bin1 = helper.createBin(location, "B1", 15, "Y1");
		final BinData bin2 = helper.createBin(location, "B2", 10, "F1");
		final BinData bin3 = helper.createBin(location, "B3", 5, "Z1");
		this.persistenceManager.flush();

		final ItemLocationData il1 = helper.createItemLocation("SKU1", location, bin1, false);
		final ItemLocationData il2 = helper.createItemLocation("SKU1", location, bin2, false);
		final ItemLocationData il3 = helper.createItemLocation("SKU1", location, bin3, false);

		final ItemLocationData il4 = helper.createItemLocation("SKU2", location, bin1, false);
		final ItemLocationData il5 = helper.createItemLocation("SKU2", location, bin2, false);
		final ItemLocationData il6 = helper.createItemLocation("SKU2", location, bin3, false);
		this.persistenceManager.flush();

		final ItemQuantityData iqd1 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd1.setQuantityUnitCode("unitCode");
		iqd1.setQuantityValue(5);
		iqd1.setStatusCode("SOMECODE");
		iqd1.setOwner(il1);

		this.persistenceManager.flush();
		final ItemQuantityData iqd2 = this.persistenceManager.create(CurrentItemQuantityData.class);
		iqd2.setQuantityUnitCode("unitCode");
		iqd2.setQuantityValue(5);
		iqd2.setStatusCode("SOMECODE");
		iqd2.setOwner(il2);

		this.persistenceManager.flush();
		Assert.assertNotNull(il3);
		Assert.assertNotNull(il4);
		Assert.assertNotNull(il5);
		Assert.assertNotNull(il6);

		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING, BinQuerySupport.BIN_DESCRIPTION.toString());
		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION, SortDirection.DESC.toString());

		final List<BinData> data = inventoryService.findBinsBySkuAndLocationId("SKU1", location.getLocationId());
		Assert.assertNotNull(data);
		Assert.assertEquals(3, data.size());
		Assert.assertEquals("B3", data.get(0).getBinCode());
		Assert.assertEquals("B1", data.get(1).getBinCode());
		Assert.assertEquals("B2", data.get(2).getBinCode());
	}

	@Test
	@Transactional
	public void findPagedBinByQueryTest()
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");

		this.persistenceManager.flush();

		final BinData bin1 = helper.createBin(location, "B1", 10);
		final BinData bin2 = helper.createBin(location, "B2", 5);
		this.persistenceManager.flush();

		helper.createItemLocation("S1", location, bin1, false);
		helper.createItemLocation("S1", location, bin2, false);

		this.persistenceManager.flush();

		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING, BinQuerySupport.BIN_CODE.toString());
		helper.createTenantPreference(TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION, SortDirection.ASC.toString());

		final List<BinData> result = this.inventoryService.findBinsBySkuAndLocationId("S1", "L1");
		Assert.assertEquals(2L, result.size());
		Assert.assertEquals(10, result.get(0).getPriority());
	}

}
