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
import com.hybris.oms.domain.inventory.FutureItemQuantity;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class FutureItemQuantityConverterTest
{
	private static final String SEPARATOR = "-";

	@Autowired
	private Converter<FutureItemQuantityData, FutureItemQuantity> futureItemQuantityConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedCurrentItemQuantityData()
	{
		final Date date = new Date();

		// given
		final FutureItemQuantityData futureItemQuantityData = this.persistenceManager.create(FutureItemQuantityData.class);
		futureItemQuantityData.setQuantityValue(1);
		futureItemQuantityData.setQuantityUnitCode("kg");
		futureItemQuantityData.setExpectedDeliveryDate(date);
		final ItemLocationData itemLoc = persistenceManager.create(ItemLocationData.class);
		final StockroomLocationData loc = persistenceManager.create(StockroomLocationData.class);
		loc.setLocationId("123");
		itemLoc.setStockroomLocation(loc);
		itemLoc.setItemId("sku");
		itemLoc.setFuture(true);
		futureItemQuantityData.setOwner(itemLoc);

		// when
		final FutureItemQuantity futureItemQuantity = this.futureItemQuantityConverter.convert(futureItemQuantityData);

		// then
		Assert.assertEquals("kg", futureItemQuantity.getQuantity().getUnitCode());
		Assert.assertEquals(1, futureItemQuantity.getQuantity().getValue());
		Assert.assertEquals(date, futureItemQuantity.getExpectedDeliveryDate());
		Assert.assertEquals(futureItemQuantityData.getOwner().getStockroomLocation().getLocationId() + SEPARATOR
				+ futureItemQuantityData.getStatusCode() + SEPARATOR + futureItemQuantityData.getExpectedDeliveryDate().getTime(),
				futureItemQuantity.getId());

	}

	@Transactional
	@Test(expected = ValidationException.class)
	public void convertingFlushedCurrentItemQuantityData()
	{
		final Date date = new Date();

		// given
		final FutureItemQuantityData futureItemQuantityData = this.persistenceManager.create(FutureItemQuantityData.class);
		futureItemQuantityData.setQuantityValue(1);
		futureItemQuantityData.setQuantityUnitCode("kg");
		futureItemQuantityData.setExpectedDeliveryDate(date);

		this.persistenceManager.flush();
	}
}
