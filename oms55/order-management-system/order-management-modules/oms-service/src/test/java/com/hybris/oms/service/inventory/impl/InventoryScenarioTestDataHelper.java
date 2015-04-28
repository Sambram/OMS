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

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableSet;


/**
 * Helper class for creating sample Inventory objects for testing SKU_ONLY inventory scenario.
 */
public class InventoryScenarioTestDataHelper
{
	@Autowired
	private final PersistenceManager persistenceManager;

	public InventoryScenarioTestDataHelper(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	public StockroomLocationData createStockRoomLocation(final String locationId)
	{
		final StockroomLocationData location = this.persistenceManager.create(StockroomLocationData.class);
		location.setDescription("Test Location");
		location.setLocationId(locationId);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		this.persistenceManager.flush();
		return location;
	}

	public BinData createBin(final StockroomLocationData location, final String binCode)
	{
		return this.createBin(location, binCode, 1);
	}

	public BinData createBin(final StockroomLocationData location, final String binCode, final int priority)
	{
		return createBin(location, binCode, priority, "Bin Description");
	}

	public BinData createBin(final StockroomLocationData location, final String binCode, final int priority,
			final String description)
	{
		final BinData bin = this.persistenceManager.create(BinData.class);
		bin.setBinCode(binCode);
		bin.setStockroomLocation(location);
		bin.setPriority(priority);
		bin.setDescription(description);
		this.persistenceManager.flush();
		return bin;
	}

	public ItemLocationData createItemLocation(final String itemId, final StockroomLocationData location, final BinData bin,
			final boolean future)
	{
		final ItemLocationData itemLocation = this.persistenceManager.create(ItemLocationData.class);
		itemLocation.setItemId(itemId);
		itemLocation.setStockroomLocation(location);
		itemLocation.setBin(bin);
		itemLocation.setFuture(future);
		this.persistenceManager.flush();
		return itemLocation;
	}

	public ItemLocationData createItemLocation(final String itemId, final StockroomLocationData location, final BinData bin)
	{
		final ItemLocationData itemLocation = this.persistenceManager.create(ItemLocationData.class);
		itemLocation.setItemId(itemId);
		itemLocation.setStockroomLocation(location);
		itemLocation.setBin(bin);
		this.persistenceManager.flush();
		return itemLocation;
	}

	public ItemQuantityData createItemQuantityData(final ItemStatusData status, final ItemLocationData itemLocation,
			final Date expectedDeliveryDate)
	{
		final FutureItemQuantityData data = this.persistenceManager.create(FutureItemQuantityData.class);
		data.setQuantityUnitCode("unitCode");
		data.setQuantityValue(5);
		data.setStatusCode(status.getStatusCode());
		data.setOwner(itemLocation);
		data.setExpectedDeliveryDate(DateUtils.truncate(expectedDeliveryDate, Calendar.DATE));
		this.persistenceManager.flush();
		return data;
	}

	public void createInventory(final Date expectedDeliveryDate)
	{
		final InventoryScenarioTestDataHelper helper = new InventoryScenarioTestDataHelper(this.persistenceManager);
		final StockroomLocationData location = helper.createStockRoomLocation("L1");
		this.persistenceManager.flush();
		final BinData bin1 = helper.createBin(location, "B1");
		final BinData bin2 = helper.createBin(location, "B2");
		helper.createBin(location, "B3");
		this.persistenceManager.flush();

		final ItemLocationData il1 = helper.createItemLocation("SKU1", location, bin1, true);
		final ItemLocationData il2 = helper.createItemLocation("SKU1", location, bin2, true);

		this.persistenceManager.flush();
		final ItemQuantityData iqd1 = this.persistenceManager.create(expectedDeliveryDate == null ? CurrentItemQuantityData.class
				: FutureItemQuantityData.class);
		iqd1.setQuantityUnitCode("unitCode");
		iqd1.setQuantityValue(5);
		iqd1.setStatusCode("SOMECODE");
		iqd1.setOwner(il1);
		if (expectedDeliveryDate != null)
		{
			iqd1.setExpectedDeliveryDate(DateUtils.truncate(expectedDeliveryDate, Calendar.DATE));
		}
		this.persistenceManager.flush();
		final ItemQuantityData iqd2 = this.persistenceManager.create(expectedDeliveryDate == null ? CurrentItemQuantityData.class
				: FutureItemQuantityData.class);
		iqd2.setQuantityUnitCode("unitCode");
		iqd2.setQuantityValue(5);
		iqd2.setStatusCode("SOMECODE");
		iqd2.setOwner(il2);
		if (expectedDeliveryDate != null)
		{
			iqd2.setExpectedDeliveryDate(DateUtils.truncate(expectedDeliveryDate, Calendar.DATE));
		}

	}

	public void createTenantPreference(final String property, final String value)
	{
		final TenantPreferenceData tenantPreferenceData = this.persistenceManager.create(TenantPreferenceData.class);
		tenantPreferenceData.setProperty(property);
		tenantPreferenceData.setValue(value);
		tenantPreferenceData.setEnabled(true);
		this.persistenceManager.flush();
	}
}
