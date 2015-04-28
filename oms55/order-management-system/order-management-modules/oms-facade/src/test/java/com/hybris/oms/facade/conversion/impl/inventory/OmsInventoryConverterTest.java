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
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OmsInventoryConverterTest
{

	@Autowired
	private Converter<ItemQuantityData, OmsInventory> omsInventoryConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedInventoryData()
	{
		// given
		final CurrentItemQuantityData currentItemQuantityData = this.persistenceManager.create(CurrentItemQuantityData.class);
		currentItemQuantityData.setQuantityValue(1);
		currentItemQuantityData.setQuantityUnitCode("kg");
		final ItemLocationData itemLoc = persistenceManager.create(ItemLocationData.class);
		final StockroomLocationData loc = persistenceManager.create(StockroomLocationData.class);
		loc.setLocationId("123");
		itemLoc.setStockroomLocation(loc);
		final BinData bin = persistenceManager.create(BinData.class);
		bin.setBinCode("ABC");
		itemLoc.setBin(bin);
		itemLoc.setItemId("sku");
		currentItemQuantityData.setOwner(itemLoc);

		// when
		final OmsInventory omsInventory = this.omsInventoryConverter.convert(currentItemQuantityData);

		// then
		Assert.assertEquals("kg", omsInventory.getUnitCode());
		Assert.assertEquals(1, omsInventory.getQuantity());
		Assert.assertEquals("123", omsInventory.getLocationId());
		Assert.assertEquals("ABC", omsInventory.getBinCode());
		Assert.assertEquals("sku", omsInventory.getSkuId());

	}

	@Transactional
	@Test(expected = ValidationException.class)
	public void convertingFlushedInventoryData()
	{
		// given
		final CurrentItemQuantityData currentItemQuantityData = this.persistenceManager.create(CurrentItemQuantityData.class);
		currentItemQuantityData.setQuantityValue(1);
		currentItemQuantityData.setQuantityUnitCode("kg");
		final ItemLocationData itemLoc = persistenceManager.create(ItemLocationData.class);
		final StockroomLocationData loc = persistenceManager.create(StockroomLocationData.class);
		loc.setLocationId("123");
		itemLoc.setStockroomLocation(loc);
		final BinData bin = persistenceManager.create(BinData.class);
		bin.setBinCode("ABC");
		itemLoc.setBin(bin);
		itemLoc.setItemId("sku");
		currentItemQuantityData.setOwner(itemLoc);
		this.persistenceManager.flush();
	}

}
