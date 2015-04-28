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
import com.hybris.oms.domain.inventory.CurrentItemQuantity;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
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
public class CurrentItemQuantityConverterTest
{
	private static final String SEPARATOR = "-";

	@Autowired
	private Converter<CurrentItemQuantityData, CurrentItemQuantity> currentItemQuantityConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedCurrentItemQuantityData()
	{
		// given
		final CurrentItemQuantityData currentItemQuantityData = this.persistenceManager.create(CurrentItemQuantityData.class);
		currentItemQuantityData.setQuantityValue(1);
		currentItemQuantityData.setQuantityUnitCode("kg");
		final ItemLocationData itemLoc = persistenceManager.create(ItemLocationData.class);
		final StockroomLocationData loc = persistenceManager.create(StockroomLocationData.class);
		loc.setLocationId("123");
		itemLoc.setStockroomLocation(loc);
		itemLoc.setItemId("sku");
		currentItemQuantityData.setOwner(itemLoc);

		// when
		final CurrentItemQuantity currentItemQuantity = this.currentItemQuantityConverter.convert(currentItemQuantityData);

		// then
		Assert.assertEquals("kg", currentItemQuantity.getQuantity().getUnitCode());
		Assert.assertEquals(1, currentItemQuantity.getQuantity().getValue());
		Assert.assertEquals(currentItemQuantityData.getOwner().getStockroomLocation().getLocationId() + SEPARATOR
				+ currentItemQuantityData.getStatusCode(), currentItemQuantity.getId());
	}

	@Transactional
	@Test(expected = ValidationException.class)
	public void convertingFlushedCurrentItemQuantityData()
	{
		// given
		final CurrentItemQuantityData currentItemQuantityData = this.persistenceManager.create(CurrentItemQuantityData.class);
		currentItemQuantityData.setQuantityValue(1);
		currentItemQuantityData.setQuantityUnitCode("kg");

		this.persistenceManager.flush();
	}
}
