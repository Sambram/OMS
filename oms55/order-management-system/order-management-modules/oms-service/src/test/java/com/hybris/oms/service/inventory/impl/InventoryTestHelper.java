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
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.common.collect.ImmutableSet;



/**
 * Helper class for creating sample Inventory objects.
 */
public class InventoryTestHelper
{
	private final PersistenceManager pmgr;

	public InventoryTestHelper(final PersistenceManager persistenceManager)
	{
		this.pmgr = persistenceManager;
	}

	public List<StockroomLocationData> createLocationData(final int count)
	{

		final List<StockroomLocationData> result = new ArrayList<>();

		for (int i = 0; i < count; i++)
		{
			final StockroomLocationData loc = this.pmgr.create(StockroomLocationData.class);
			loc.setDescription("loc_" + String.format("%02d", i));
			loc.setLocationId("loc_" + String.format("%02d", i));
			loc.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
			result.add(loc);
		}

		return result;
	}

	public void createItemLocations(final List<StockroomLocationData> locations, final List<String> skus)
	{
		for (final StockroomLocationData location : locations)
		{
			final BinData bin = this.pmgr.create(BinData.class);
			bin.setStockroomLocation(location);
			bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
			bin.setPriority(1);
		}
		this.pmgr.flush();
		int i = 0;
		for (final String sku : skus)
		{
			for (final StockroomLocationData location : locations)
			{
				final BinData bin = this.pmgr.getByIndex(BinData.UQ_BIN_BINCODESKULOC, InventoryServiceConstants.DEFAULT_BIN,
						location);
				if (i++ % 2 == 0)
				{
					this.createItemLocationCurrent(sku, location, i, bin);
				}
				else
				{
					this.createItemLocationFuture(sku, location, i, bin);
				}
			}
		}
	}

	public void createItemLocationCurrent(final String sku, final StockroomLocationData location, final int i, final BinData bin)
	{
		final ItemLocationData current = this.pmgr.create(ItemLocationData.class);
		current.setItemId(sku);
		current.setStockroomLocation(location);
		current.setFuture(false);
		current.getItemQuantities().add(this.createCurrentItemQuantityData(current, "STATUS_A_" + String.format("%02d", i)));
		current.getItemQuantities().add(this.createCurrentItemQuantityData(current, "STATUS_A_" + String.format("%02d", 10 * i)));
		current.setBin(bin);
	}

	public void createItemLocationFuture(final String sku, final StockroomLocationData location, final int i, final BinData bin)
	{
		final ItemLocationData future = this.pmgr.create(ItemLocationData.class);
		future.setItemId(sku);
		future.setStockroomLocation(location);
		future.setFuture(true);
		future.getItemQuantities().add(this.createFutureItemQuantityData(future, "STATUS_B_" + String.format("%02d", i)));
		future.getItemQuantities().add(this.createFutureItemQuantityData(future, "STATUS_B_" + String.format("%02d", 10 * i)));
		future.setBin(bin);
	}

	public BinData createDefaultBin(final StockroomLocationData location)
	{
		final BinData bin = this.pmgr.create(BinData.class);
		bin.setStockroomLocation(location);
		bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
		bin.setPriority(1);
		return bin;
	}

	public CurrentItemQuantityData createCurrentItemQuantityData(final ItemLocationData owner, final String code)
	{
		// 1..365
		final int intValue = code.hashCode() % 365 + 1;

		final CurrentItemQuantityData result = this.pmgr.create(CurrentItemQuantityData.class);
		result.setOwner(owner);
		result.setQuantityUnitCode("metric tons");
		result.setStatusCode(code);
		result.setQuantityValue(intValue);
		return result;
	}

	public FutureItemQuantityData createFutureItemQuantityData(final ItemLocationData owner, final String code)
	{
		// 1..365
		final int intValue = code.hashCode() % 365 + 1;

		final FutureItemQuantityData result = this.pmgr.create(FutureItemQuantityData.class);
		result.setOwner(owner);
		result.setQuantityUnitCode("metric tons");
		result.setStatusCode(code);
		result.setQuantityValue(intValue);
		result.setExpectedDeliveryDate(this.getDate(intValue));
		return result;
	}

	public Date getDate(final int intValue)
	{
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2114);
		cal.set(Calendar.DAY_OF_YEAR, intValue);
		cal.set(Calendar.HOUR, 1);
		cal.set(Calendar.MINUTE, 1);
		cal.set(Calendar.SECOND, 1);
		cal.set(Calendar.MILLISECOND, 1);
		return cal.getTime();
	}

	public void createBinData(final List<StockroomLocationData> locations)
	{
		int counter = 1;
		for (final StockroomLocationData location : locations)
		{
			final BinData bin = this.pmgr.create(BinData.class);
			bin.setStockroomLocation(location);
			bin.setBinCode("bin" + counter);
			bin.setDescription("bin" + counter);
			bin.setPriority(counter);
			counter++;
		}
	}
}
