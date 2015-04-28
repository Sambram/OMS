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
import com.hybris.oms.domain.inventory.FutureItemQuantity;
import com.hybris.oms.domain.inventory.FutureItemQuantityStatus;
import com.hybris.oms.domain.inventory.ItemLocationFuture;
import com.hybris.oms.domain.inventory.ItemQuantity;
import com.hybris.oms.domain.inventory.ItemQuantityStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ItemLocationFutureConverterTest
{
	private static final String SEPARATOR = "-";

	private static final boolean LOC_USE_PERC_THRSHLD = true;
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
	private Converter<ItemLocationData, ItemLocationFuture> itemLocationFutureConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedFutureItemLocationData()
	{
		// given
		final Date date = new Date();
		final StockroomLocationData locationData = this.createLocationData();

		final FutureItemQuantityData futureItemQuantityData = this.persistenceManager.create(FutureItemQuantityData.class);
		futureItemQuantityData.setQuantityValue(1);
		futureItemQuantityData.setQuantityUnitCode("kg");
		futureItemQuantityData.setStatusCode("ON_HAND");
		futureItemQuantityData.setExpectedDeliveryDate(date);

		final ItemLocationData itemLocationData = this.persistenceManager.create(ItemLocationData.class);
		itemLocationData.setFuture(true);
		itemLocationData.setItemId("itemId");
		itemLocationData.setStockroomLocation(locationData);
		itemLocationData.setItemQuantities(ImmutableList.of((ItemQuantityData) futureItemQuantityData));

		futureItemQuantityData.setOwner(itemLocationData);

		// when
		final ItemLocationFuture itemLocationFuture = this.itemLocationFutureConverter.convert(itemLocationData);

		// then
		Assert.assertEquals(true, itemLocationFuture.isFuture());
		Assert.assertEquals("itemId", itemLocationFuture.getItemId());
		Assert.assertEquals(itemLocationData.getItemId() + SEPARATOR + itemLocationData.getStockroomLocation().getLocationId()
				+ SEPARATOR + itemLocationData.isFuture(), itemLocationFuture.getId());

		final Location location = itemLocationFuture.getLocation();
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
		Assert.assertEquals(LOC_USE_PERC_THRSHLD, location.getUsePercentageThreshold());

		Assert.assertEquals(1, itemLocationFuture.getItemQuantities().size());

		final ItemQuantityStatus itemQuantityStatus = itemLocationFuture.getItemQuantities().keySet().iterator().next();
		Assert.assertTrue(itemQuantityStatus instanceof FutureItemQuantityStatus);
		Assert.assertEquals("ON_HAND", itemQuantityStatus.getStatusCode());
		Assert.assertEquals(date, ((FutureItemQuantityStatus) itemQuantityStatus).getExpectedDeliveryDate());

		final ItemQuantity itemQuantity = itemLocationFuture.getItemQuantities().values().iterator().next();
		Assert.assertTrue(itemQuantity instanceof FutureItemQuantity);
		Assert.assertEquals("kg", ((FutureItemQuantity) itemQuantity).getQuantity().getUnitCode());
		Assert.assertEquals(1, ((FutureItemQuantity) itemQuantity).getQuantity().getValue());
		Assert.assertEquals(date, ((FutureItemQuantity) itemQuantity).getExpectedDeliveryDate());
	}

	@Transactional
	@Test
	public void convertingFlushedFutureItemLocationData()
	{
		// given
		final Date date = new Date();
		final StockroomLocationData locationData = this.createLocationData();

		final FutureItemQuantityData futureItemQuantityData = this.persistenceManager.create(FutureItemQuantityData.class);
		futureItemQuantityData.setQuantityValue(1);
		futureItemQuantityData.setQuantityUnitCode("kg");
		futureItemQuantityData.setStatusCode("ON_HAND");
		futureItemQuantityData.setExpectedDeliveryDate(date);

		final ItemLocationData itemLocationData = this.persistenceManager.create(ItemLocationData.class);
		itemLocationData.setFuture(true);
		itemLocationData.setItemId("itemId");
		itemLocationData.setStockroomLocation(locationData);
		itemLocationData.setItemQuantities(ImmutableList.of((ItemQuantityData) futureItemQuantityData));

		futureItemQuantityData.setOwner(itemLocationData);

		// when
		final ItemLocationFuture itemLocationFuture = this.itemLocationFutureConverter.convert(itemLocationData);

		// then
		Assert.assertEquals(true, itemLocationFuture.isFuture());
		Assert.assertEquals("itemId", itemLocationFuture.getItemId());

		final Location location = itemLocationFuture.getLocation();
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
		Assert.assertEquals(LOC_USE_PERC_THRSHLD, location.getUsePercentageThreshold());

		Assert.assertEquals(1, itemLocationFuture.getItemQuantities().size());

		final ItemQuantityStatus itemQuantityStatus = itemLocationFuture.getItemQuantities().keySet().iterator().next();
		Assert.assertTrue(itemQuantityStatus instanceof FutureItemQuantityStatus);
		Assert.assertEquals("ON_HAND", itemQuantityStatus.getStatusCode());
		Assert.assertEquals(date, ((FutureItemQuantityStatus) itemQuantityStatus).getExpectedDeliveryDate());

		final ItemQuantity itemQuantity = itemLocationFuture.getItemQuantities().values().iterator().next();
		Assert.assertTrue(itemQuantity instanceof FutureItemQuantity);
		Assert.assertEquals("kg", ((FutureItemQuantity) itemQuantity).getQuantity().getUnitCode());
		Assert.assertEquals(1, ((FutureItemQuantity) itemQuantity).getQuantity().getValue());
		Assert.assertEquals(date, ((FutureItemQuantity) itemQuantity).getExpectedDeliveryDate());
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
		locationData.setUsePercentageThreshold(LOC_USE_PERC_THRSHLD);

		return locationData;
	}

}
