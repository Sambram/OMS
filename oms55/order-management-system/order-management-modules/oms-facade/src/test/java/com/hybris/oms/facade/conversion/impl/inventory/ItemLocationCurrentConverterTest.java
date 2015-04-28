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
package com.hybris.oms.facade.conversion.impl.inventory;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.CurrentItemQuantity;
import com.hybris.oms.domain.inventory.CurrentItemQuantityStatus;
import com.hybris.oms.domain.inventory.ItemLocationCurrent;
import com.hybris.oms.domain.inventory.ItemQuantity;
import com.hybris.oms.domain.inventory.ItemQuantityStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ItemLocationCurrentConverterTest
{
	private static final String SEPARATOR = "-";

	private static final boolean LOC_USE_PERC_THRSLD = true;
	private static final int LOC_PERC_INV_THRSHLD = 1;
	private static final int LOC_ABS_INV_THRSHLD = 1;
	private static final boolean LOCATION_ACTIVE = true;
	private static final Address LOCATION_ADDRESS = AddressBuilder.anAddress().buildAddressDTO();
	private static final int LOCATION_PRIORITY = 1;
	private static final String LOCATION_TAX_AREA_ID = "taxAreaId";
	private static final String LOCATION_STORE_NAME = "storeName";
	private static final String LOCATION_DESCRIPTION = "description";
	private static final String LOCATION_LOCATION_ID = "locationId";

	@Autowired
	private Converter<ItemLocationData, ItemLocationCurrent> itemLocationCurrentConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedCurrentItemLocationData()
	{
		// given
		final StockroomLocationData locationData = this.createLocationData();

		final CurrentItemQuantityData currentItemQuantityData = this.persistenceManager.create(CurrentItemQuantityData.class);
		currentItemQuantityData.setQuantityValue(1);
		currentItemQuantityData.setQuantityUnitCode("kg");
		currentItemQuantityData.setStatusCode("ON_HAND");

		final ItemLocationData itemLocationData = this.persistenceManager.create(ItemLocationData.class);
		itemLocationData.setFuture(false);
		itemLocationData.setItemId("itemId");
		itemLocationData.setStockroomLocation(locationData);
		itemLocationData.setItemQuantities(ImmutableList.of((ItemQuantityData) currentItemQuantityData));

		currentItemQuantityData.setOwner(itemLocationData);

		// when
		final ItemLocationCurrent itemLocationCurrent = this.itemLocationCurrentConverter.convert(itemLocationData);

		// then
		Assert.assertEquals(false, itemLocationCurrent.isFuture());
		Assert.assertEquals("itemId", itemLocationCurrent.getItemId());
		Assert.assertEquals(itemLocationData.getItemId() + SEPARATOR + itemLocationData.getStockroomLocation().getLocationId(),
				itemLocationCurrent.getId());

		final Location location = itemLocationCurrent.getLocation();
		Assert.assertNotNull(location);
		Assert.assertEquals(LOCATION_LOCATION_ID, location.getLocationId());
		Assert.assertEquals(LOCATION_DESCRIPTION, location.getDescription());
		Assert.assertEquals(LOCATION_STORE_NAME, location.getStoreName());
		Assert.assertEquals(LOCATION_TAX_AREA_ID, location.getTaxAreaId());
		Assert.assertEquals(LOCATION_PRIORITY, location.getPriority());
		Assert.assertEquals(LOCATION_ADDRESS, location.getAddress());
		Assert.assertEquals(LOCATION_ACTIVE, location.getActive());
		Assert.assertEquals(LOC_ABS_INV_THRSHLD, location.getAbsoluteInventoryThreshold());
		Assert.assertEquals(LOC_PERC_INV_THRSHLD, location.getPercentageInventoryThreshold());
		Assert.assertEquals(LOC_USE_PERC_THRSLD, location.getUsePercentageThreshold());

		Assert.assertEquals(1, itemLocationCurrent.getItemQuantities().size());

		final ItemQuantityStatus itemQuantityStatus = itemLocationCurrent.getItemQuantities().keySet().iterator().next();
		Assert.assertTrue(itemQuantityStatus instanceof CurrentItemQuantityStatus);
		Assert.assertEquals("ON_HAND", itemQuantityStatus.getStatusCode());

		final ItemQuantity itemQuantity = itemLocationCurrent.getItemQuantities().values().iterator().next();
		Assert.assertTrue(itemQuantity instanceof CurrentItemQuantity);
		Assert.assertEquals("kg", ((CurrentItemQuantity) itemQuantity).getQuantity().getUnitCode());
		Assert.assertEquals(1, ((CurrentItemQuantity) itemQuantity).getQuantity().getValue());
	}

	@Transactional
	@Test
	public void convertingFlushedCurrentItemLocationData()
	{
		// given
		final StockroomLocationData locationData = this.createLocationData();

		final BinData defaultBin = this.createDefaultBin(locationData);

		final ItemLocationData itemLocationData = this.persistenceManager.create(ItemLocationData.class);
		itemLocationData.setFuture(false);
		itemLocationData.setItemId("itemId");
		itemLocationData.setStockroomLocation(locationData);
		itemLocationData.setBin(defaultBin);

		final CurrentItemQuantityData currentItemQuantityData = this.persistenceManager.create(CurrentItemQuantityData.class);
		currentItemQuantityData.setQuantityValue(1);
		currentItemQuantityData.setQuantityUnitCode("kg");
		currentItemQuantityData.setOwner(itemLocationData);
		currentItemQuantityData.setStatusCode("ON_HAND");

		this.persistenceManager.flush();

		// when
		final ItemLocationCurrent itemLocationCurrent = this.itemLocationCurrentConverter.convert(itemLocationData);

		// then
		Assert.assertEquals(false, itemLocationCurrent.isFuture());
		Assert.assertEquals("itemId", itemLocationCurrent.getItemId());

		final Location location = itemLocationCurrent.getLocation();
		Assert.assertNotNull(location);
		Assert.assertEquals(LOCATION_LOCATION_ID, location.getLocationId());
		Assert.assertEquals(LOCATION_DESCRIPTION, location.getDescription());
		Assert.assertEquals(LOCATION_STORE_NAME, location.getStoreName());
		Assert.assertEquals(LOCATION_TAX_AREA_ID, location.getTaxAreaId());
		Assert.assertEquals(LOCATION_PRIORITY, location.getPriority());
		Assert.assertEquals(LOCATION_ADDRESS, location.getAddress());
		Assert.assertEquals(LOCATION_ACTIVE, location.getActive());
		Assert.assertEquals(LOC_ABS_INV_THRSHLD, location.getAbsoluteInventoryThreshold());
		Assert.assertEquals(LOC_PERC_INV_THRSHLD, location.getPercentageInventoryThreshold());
		Assert.assertEquals(LOC_USE_PERC_THRSLD, location.getUsePercentageThreshold());

		Assert.assertEquals(1, itemLocationCurrent.getItemQuantities().size());

		final ItemQuantityStatus itemQuantityStatus = itemLocationCurrent.getItemQuantities().keySet().iterator().next();
		Assert.assertTrue(itemQuantityStatus instanceof CurrentItemQuantityStatus);
		Assert.assertEquals("ON_HAND", itemQuantityStatus.getStatusCode());

		final ItemQuantity itemQuantity = itemLocationCurrent.getItemQuantities().values().iterator().next();
		Assert.assertTrue(itemQuantity instanceof CurrentItemQuantity);
		Assert.assertEquals("kg", ((CurrentItemQuantity) itemQuantity).getQuantity().getUnitCode());
		Assert.assertEquals(1, ((CurrentItemQuantity) itemQuantity).getQuantity().getValue());
	}

	private StockroomLocationData createLocationData()
	{
		final StockroomLocationData locationData = this.persistenceManager.create(StockroomLocationData.class);
		locationData.setLocationId(LOCATION_LOCATION_ID);
		locationData.setDescription(LOCATION_DESCRIPTION);
		locationData.setStoreName(LOCATION_STORE_NAME);
		locationData.setTaxAreaId(LOCATION_TAX_AREA_ID);
		locationData.setPriority(LOCATION_PRIORITY);
		locationData.setAddress(AddressBuilder.anAddress().buildAddressVT());
		locationData.setActive(LOCATION_ACTIVE);
		locationData.setAbsoluteInventoryThreshold(LOC_ABS_INV_THRSHLD);
		locationData.setPercentageInventoryThreshold(LOC_PERC_INV_THRSHLD);
		locationData.setUsePercentageThreshold(LOC_USE_PERC_THRSLD);
		locationData.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));

		return locationData;
	}

	private BinData createDefaultBin(final StockroomLocationData location)
	{
		final BinData bin = this.persistenceManager.create(BinData.class);
		bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
		bin.setStockroomLocation(location);
		bin.setPriority(1);
		return bin;
	}
}
