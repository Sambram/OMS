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
package com.hybris.oms.service.inventory.jobs;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.JobWorkerBean;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.inventory.impl.DefaultInventoryService;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml", "/META-INF/oms-service-jobs-spring.xml"})
public class InventoryRolloverJobTest
{
	private static final String LOCATION_ID = "1";
	private static final String SKU_ID = "1";
	private static final String ITEM_ID = "1";
	private static final int DAY_OF_MONTH = 30;
	private static final int YEAR = 2012;
	private final Calendar calendar = Calendar.getInstance();
	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	@Autowired
	private ImportService importService;
	@Autowired
	private DefaultInventoryService inventoryService;
	@Autowired
	private PersistenceManager persistenceManager;
	@Autowired
	@Qualifier("inventoryRolloverWorker")
	private JobWorkerBean rolloverJob;

	@Test
	@Transactional
	public void testExecute()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/inventory/test-inventory-data-import.mcsv")[0]);

		// given
		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);

		final Date currentDate = this.calendar.getTime();
		final List<FutureItemQuantityData> futureItemQuantityData = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(3, futureItemQuantityData.size());
		final List<ItemLocationData> itemLocationFutures = this.inventoryService.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(3, itemLocationFutures.size());

		final List<ItemLocationData> allItemLocations = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(69, allItemLocations.size());
		this.checkLocationsCount(allItemLocations, 12, 57);

		// when
		this.rolloverJob.execute(null);

		// then
		final List<ItemLocationData> itemLocationFuturesAfterRollover = this.inventoryService
				.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(0, itemLocationFuturesAfterRollover.size());
		final List<FutureItemQuantityData> futureItemQuantityAfterRollover = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(0, futureItemQuantityAfterRollover.size());

		final List<ItemLocationData> allItemLocationsAfterRollover = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(68, allItemLocationsAfterRollover.size());
		this.checkLocationsCount(allItemLocationsAfterRollover, 9, 59);
	}

	@Test
	@Transactional
	public void testRolloverKeepItemLocationFutureAndCreateCurrent()
	{
		this.prepareData3();

		// given
		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);

		final Date currentDate = this.calendar.getTime();
		final List<FutureItemQuantityData> futureItemQuantityData = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(1, futureItemQuantityData.size());
		final List<ItemLocationData> itemLocationFutures = this.inventoryService.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(1, itemLocationFutures.size());

		final List<ItemLocationData> allItemLocations = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(1, allItemLocations.size());
		this.checkLocationsCount(allItemLocations, 1, 0);

		this.rolloverJob.execute(null);

		final List<FutureItemQuantityData> futureItemQuantityAfterRollover = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(0, futureItemQuantityAfterRollover.size());
		final List<ItemLocationData> itemLocationFuturesAfterRollover = this.inventoryService
				.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(0, itemLocationFuturesAfterRollover.size());

		final List<ItemLocationData> allItemLocationsAfterRollover = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(2, allItemLocationsAfterRollover.size());
		this.checkLocationsCount(allItemLocationsAfterRollover, 1, 1);

		final ItemLocationData itemLocationCurrentData = this.inventoryService.findItemLocationCurrentBySkuIdAndLocationId(SKU_ID,
				LOCATION_ID);
		Assert.assertTrue(itemLocationCurrentData != null);
		Assert.assertEquals(1, itemLocationCurrentData.getItemQuantities().size());
		Assert.assertTrue(itemLocationCurrentData.getItemQuantities().get(0) instanceof CurrentItemQuantityData);
		Assert.assertEquals(1, itemLocationCurrentData.getItemQuantities().get(0).getQuantityValue());
	}

	@Test
	@Transactional
	public void testRolloverKeepItemLocationFutureAndUpdateCurrent()
	{
		this.prepareData4();

		// given
		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);

		final Date currentDate = this.calendar.getTime();
		final List<FutureItemQuantityData> futureItemQuantityData = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(1, futureItemQuantityData.size());
		final List<ItemLocationData> itemLocationFutures = this.inventoryService.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(1, itemLocationFutures.size());

		final List<ItemLocationData> allItemLocations = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(2, allItemLocations.size());
		this.checkLocationsCount(allItemLocations, 1, 1);

		this.rolloverJob.execute(null);

		final List<FutureItemQuantityData> futureItemQuantityAfterRollover = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(0, futureItemQuantityAfterRollover.size());
		final List<ItemLocationData> itemLocationFuturesAfterRollover = this.inventoryService
				.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(0, itemLocationFuturesAfterRollover.size());

		final List<ItemLocationData> allItemLocationsAfterRollover = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(2, allItemLocationsAfterRollover.size());
		this.checkLocationsCount(allItemLocationsAfterRollover, 1, 1);

		final ItemLocationData itemLocationCurrentData = this.inventoryService.findItemLocationCurrentBySkuIdAndLocationId(SKU_ID,
				LOCATION_ID);
		Assert.assertTrue(itemLocationCurrentData != null);
		Assert.assertEquals(1, itemLocationCurrentData.getItemQuantities().size());
		Assert.assertTrue(itemLocationCurrentData.getItemQuantities().get(0) instanceof CurrentItemQuantityData);
		Assert.assertEquals(2, itemLocationCurrentData.getItemQuantities().get(0).getQuantityValue());
	}

	@Test
	@Transactional
	public void testRolloverRemoveItemLocationFutureAndCreateCurrent()
	{
		this.prepareData1();

		// given
		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);

		final Date currentDate = this.calendar.getTime();
		final List<FutureItemQuantityData> futureItemQuantityData = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(2, futureItemQuantityData.size());
		final List<ItemLocationData> itemLocationFutures = this.inventoryService.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(1, itemLocationFutures.size());

		final List<ItemLocationData> allItemLocations = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(1, allItemLocations.size());
		this.checkLocationsCount(allItemLocations, 1, 0);

		this.rolloverJob.execute(null);

		final List<FutureItemQuantityData> futureItemQuantityAfterRollover = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(0, futureItemQuantityAfterRollover.size());
		final List<ItemLocationData> itemLocationFuturesAfterRollover = this.inventoryService
				.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(0, itemLocationFuturesAfterRollover.size());

		final List<ItemLocationData> allItemLocationsAfterRollover = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(1, allItemLocationsAfterRollover.size());
		this.checkLocationsCount(allItemLocationsAfterRollover, 0, 1);

		Assert.assertEquals(1, allItemLocationsAfterRollover.get(0).getItemQuantities().size());
		Assert.assertTrue(allItemLocationsAfterRollover.get(0).getItemQuantities().get(0) instanceof CurrentItemQuantityData);
		Assert.assertEquals(2, allItemLocationsAfterRollover.get(0).getItemQuantities().get(0).getQuantityValue());
	}

	@Test
	@Transactional
	public void testRolloverRemoveItemLocationFutureAndUpdateCurrent()
	{
		this.prepareData2();

		// given
		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);

		final Date currentDate = this.calendar.getTime();
		final List<FutureItemQuantityData> futureItemQuantityData = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(2, futureItemQuantityData.size());
		final List<ItemLocationData> itemLocationFutures = this.inventoryService.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(1, itemLocationFutures.size());

		final List<ItemLocationData> allItemLocations = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(2, allItemLocations.size());
		this.checkLocationsCount(allItemLocations, 1, 1);

		this.rolloverJob.execute(null);

		final List<FutureItemQuantityData> futureItemQuantityAfterRollover = this.inventoryService.findAllExpectedDeliveryDate();
		Assert.assertEquals(0, futureItemQuantityAfterRollover.size());
		final List<ItemLocationData> itemLocationFuturesAfterRollover = this.inventoryService
				.findAllFutureItemLocationsByDate(currentDate);
		Assert.assertEquals(0, itemLocationFuturesAfterRollover.size());

		final List<ItemLocationData> allItemLocationsAfterRollover = this.inventoryService.findAllItemLocations();
		Assert.assertEquals(1, allItemLocationsAfterRollover.size());
		this.checkLocationsCount(allItemLocationsAfterRollover, 0, 1);

		Assert.assertEquals(1, allItemLocationsAfterRollover.get(0).getItemQuantities().size());
		Assert.assertTrue(allItemLocationsAfterRollover.get(0).getItemQuantities().get(0) instanceof CurrentItemQuantityData);
		Assert.assertEquals(3, allItemLocationsAfterRollover.get(0).getItemQuantities().get(0).getQuantityValue());
	}

	private void checkLocationsCount(final List<ItemLocationData> itemLocations, final int futureLocationsCount,
			final int currentLocationsCount)
	{
		int futureLocationsCounter = 0;
		int currentLocationsCounter = 0;
		for (final ItemLocationData itemLocationData : itemLocations)
		{
			if (itemLocationData.isFuture())
			{
				futureLocationsCounter++;
			}
			else
			{
				currentLocationsCounter++;
			}
		}
		Assert.assertEquals(futureLocationsCount, futureLocationsCounter);
		Assert.assertEquals(currentLocationsCount, currentLocationsCounter);
	}

	private AddressVT createAddress()
	{
		return new AddressVT("a", "b", "A", "ON", null, 45.42349, -75.69793, null, null, null, null);
	}

	private CurrentItemQuantityData createCurrentItemQuantityData(final int itemQuantityValue)
	{
		final CurrentItemQuantityData currentItemQuantityData = this.persistenceManager.create(CurrentItemQuantityData.class);
		currentItemQuantityData.setQuantityValue(itemQuantityValue);
		currentItemQuantityData.setQuantityUnitCode("U");
		currentItemQuantityData.setStatusCode("ON_HAND");

		return currentItemQuantityData;
	}

	private FutureItemQuantityData createFutureItemQuantityData(final int itemQuantityValue, final Date date)
	{
		final FutureItemQuantityData futureItemQuantityData = this.persistenceManager.create(FutureItemQuantityData.class);
		futureItemQuantityData.setQuantityValue(itemQuantityValue);
		futureItemQuantityData.setQuantityUnitCode("U");
		futureItemQuantityData.setStatusCode("ON_HAND");
		futureItemQuantityData.setExpectedDeliveryDate(date);

		return futureItemQuantityData;
	}

	private ItemLocationData createItemLocationData(final String itemId, final boolean isFuture)
	{
		final ItemLocationData itemLocationCurrentData = this.persistenceManager.create(ItemLocationData.class);
		itemLocationCurrentData.setItemId(itemId);
		itemLocationCurrentData.setFuture(isFuture);

		return itemLocationCurrentData;
	}

	private ItemStatusData createItemStatusData()
	{
		final ItemStatusData itemStatusData = this.persistenceManager.create(ItemStatusData.class);
		itemStatusData.setStatusCode("ON_HAND");
		itemStatusData.setDescription("On Hand");

		return itemStatusData;
	}

	private StockroomLocationData createLocationData(final String locationId, final int priority)
	{
		final StockroomLocationData locationData = this.persistenceManager.create(StockroomLocationData.class);
		locationData.setLocationId(locationId);
		locationData.setPriority(priority);
		locationData.setAddress(this.createAddress());
		locationData.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));

		return locationData;
	}

	private BinData createBinData(final String binCode, final int priority)
	{
		final BinData binData = this.persistenceManager.create(BinData.class);
		binData.setBinCode(binCode);
		binData.setPriority(priority);

		return binData;
	}

	private void prepareData1()
	{
		final StockroomLocationData locationData = this.createLocationData(LOCATION_ID, 1);
		this.createItemStatusData();

		final BinData binData = this.createBinData(InventoryServiceConstants.DEFAULT_BIN, 1);
		binData.setStockroomLocation(locationData);

		final ItemLocationData itemLocationFutureData = this.createItemLocationData(ITEM_ID, true);
		itemLocationFutureData.setStockroomLocation(locationData);
		itemLocationFutureData.setBin(binData);

		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);
		final FutureItemQuantityData futureItemQuantityData = this.createFutureItemQuantityData(1, this.calendar.getTime());
		futureItemQuantityData.setOwner(itemLocationFutureData);
		this.calendar.add(Calendar.DAY_OF_YEAR, 1);
		final FutureItemQuantityData futureItemQuantityData2 = this.createFutureItemQuantityData(1, this.calendar.getTime());
		futureItemQuantityData2.setOwner(itemLocationFutureData);

		this.persistenceManager.flush();
	}

	private void prepareData2()
	{
		final StockroomLocationData locationData = this.createLocationData(LOCATION_ID, 1);
		this.createItemStatusData();

		final BinData binData = this.createBinData(InventoryServiceConstants.DEFAULT_BIN, 1);
		binData.setStockroomLocation(locationData);

		final ItemLocationData itemLocationFutureData = this.createItemLocationData(ITEM_ID, true);
		itemLocationFutureData.setStockroomLocation(locationData);
		itemLocationFutureData.setBin(binData);
		final ItemLocationData itemLocationCurrentData = this.createItemLocationData(ITEM_ID, false);
		itemLocationCurrentData.setStockroomLocation(locationData);
		itemLocationCurrentData.setBin(binData);

		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);
		final FutureItemQuantityData futureItemQuantityData = this.createFutureItemQuantityData(1, this.calendar.getTime());
		futureItemQuantityData.setOwner(itemLocationFutureData);
		this.calendar.add(Calendar.DAY_OF_YEAR, 1);
		final FutureItemQuantityData futureItemQuantityData2 = this.createFutureItemQuantityData(1, this.calendar.getTime());
		futureItemQuantityData2.setOwner(itemLocationFutureData);

		final CurrentItemQuantityData currentItemQuantityData = this.createCurrentItemQuantityData(1);
		currentItemQuantityData.setOwner(itemLocationCurrentData);

		this.persistenceManager.flush();
	}

	private void prepareData3()
	{
		final StockroomLocationData locationData = this.createLocationData(LOCATION_ID, 1);
		this.createItemStatusData();

		final BinData binData = this.createBinData(InventoryServiceConstants.DEFAULT_BIN, 1);
		binData.setStockroomLocation(locationData);

		final ItemLocationData itemLocationFutureData = this.createItemLocationData(ITEM_ID, true);
		itemLocationFutureData.setStockroomLocation(locationData);
		itemLocationFutureData.setBin(binData);

		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);
		final FutureItemQuantityData futureItemQuantityData = this.createFutureItemQuantityData(1, this.calendar.getTime());
		futureItemQuantityData.setOwner(itemLocationFutureData);

		final int yearInFuture = Calendar.getInstance().get(Calendar.YEAR) + 1;
		this.calendar.set(yearInFuture, Calendar.DECEMBER, DAY_OF_MONTH);
		final FutureItemQuantityData futureItemQuantityData2 = this.createFutureItemQuantityData(1, this.calendar.getTime());
		futureItemQuantityData2.setOwner(itemLocationFutureData);

		this.persistenceManager.flush();
	}

	private void prepareData4()
	{
		final StockroomLocationData locationData = this.createLocationData(LOCATION_ID, 1);
		this.createItemStatusData();

		final BinData binData = this.createBinData(InventoryServiceConstants.DEFAULT_BIN, 1);
		binData.setStockroomLocation(locationData);

		final ItemLocationData itemLocationFutureData = this.createItemLocationData(ITEM_ID, true);
		itemLocationFutureData.setStockroomLocation(locationData);
		itemLocationFutureData.setBin(binData);
		final ItemLocationData itemLocationCurrentData = this.createItemLocationData(ITEM_ID, false);
		itemLocationCurrentData.setStockroomLocation(locationData);
		itemLocationCurrentData.setBin(binData);

		this.calendar.set(YEAR, Calendar.DECEMBER, DAY_OF_MONTH);
		final FutureItemQuantityData futureItemQuantityData = this.createFutureItemQuantityData(1, this.calendar.getTime());
		futureItemQuantityData.setOwner(itemLocationFutureData);

		final int yearInFuture = Calendar.getInstance().get(Calendar.YEAR) + 1;
		this.calendar.set(yearInFuture, Calendar.DECEMBER, DAY_OF_MONTH);
		final FutureItemQuantityData futureItemQuantityData2 = this.createFutureItemQuantityData(1, this.calendar.getTime());
		futureItemQuantityData2.setOwner(itemLocationFutureData);

		final CurrentItemQuantityData currentItemQuantityData = this.createCurrentItemQuantityData(1);
		currentItemQuantityData.setOwner(itemLocationCurrentData);

		this.persistenceManager.flush();
	}
}
