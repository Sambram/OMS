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
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ItemStatusConverterTest
{

	@Autowired
	private Converter<ItemStatusData, ItemStatus> itemStatusConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedItemStatusData()
	{
		// given
		final ItemStatusData itemStatusData = this.persistenceManager.create(ItemStatusData.class);
		itemStatusData.setDescription("ON HAND");
		itemStatusData.setStatusCode("ON_HAND");

		// when
		final ItemStatus itemStatus = this.itemStatusConverter.convert(itemStatusData);

		// then
		Assert.assertEquals("ON HAND", itemStatus.getDescription());
		Assert.assertEquals("ON_HAND", itemStatus.getStatusCode());
	}

	@Transactional
	@Test
	public void convertingFlushedItemStatusData()
	{
		// given
		final ItemStatusData itemStatusData = this.persistenceManager.create(ItemStatusData.class);
		itemStatusData.setDescription("ON HAND");
		itemStatusData.setStatusCode("ON_HAND");

		this.persistenceManager.flush();

		// when
		final ItemStatus itemStatus = this.itemStatusConverter.convert(itemStatusData);

		// then
		Assert.assertEquals("ON HAND", itemStatus.getDescription());
		Assert.assertEquals("ON_HAND", itemStatus.getStatusCode());
	}

}
