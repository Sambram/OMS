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
import com.hybris.commons.conversion.Populator;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ItemStatusReverseConverterTest
{

	@Autowired
	private Converter<ItemStatus, ItemStatusData> itemStatusReverseConverter;

	@Autowired
	private Populator<ItemStatus, ItemStatusData> itemStatusReversePopulator;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Transactional
	@Test
	public void reverseConvertingNotFlushedItemStatusData()
	{
		// given
		final ItemStatus itemStatus = new ItemStatus();
		itemStatus.setDescription("ON HAND");
		itemStatus.setStatusCode("ON_HAND");

		// when
		final ItemStatusData itemStatusData = this.itemStatusReverseConverter.convert(itemStatus);

		// then
		Assert.assertTrue(itemStatusData.getId() == null);
		Assert.assertEquals("ON HAND", itemStatusData.getDescription());
		Assert.assertEquals("ON_HAND", itemStatusData.getStatusCode());
	}

	@Transactional
	@Test
	public void reverseConvertingFlushedItemStatusData()
	{
		// given
		final ItemStatus itemStatus = new ItemStatus();
		itemStatus.setDescription("ON HAND");
		itemStatus.setStatusCode("ON_HAND");

		this.persistenceManager.flush();

		// when
		final ItemStatusData itemStatusData = this.itemStatusReverseConverter.convert(itemStatus);

		// then
		Assert.assertTrue(itemStatusData.getId() == null);
		Assert.assertEquals("ON HAND", itemStatusData.getDescription());
		Assert.assertEquals("ON_HAND", itemStatusData.getStatusCode());
	}

	@Test
	public void shouldFailPopulatingFinals()
	{
		// given
		final ItemStatus itemStatus = new ItemStatus();
		itemStatus.setDescription("ON HAND");
		itemStatus.setStatusCode("ON_HAND");

		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			public void doInTransactionWithoutResult(final TransactionStatus status)
			{
				itemStatusReverseConverter.convert(itemStatus);
			}
		});

		transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				final ItemStatusData itemStatusData = persistenceManager.getByIndex(ItemStatusData.UX_ITEMSTATUSES_STATUSCODE,
						"ON_HAND");

				try
				{
					itemStatusReversePopulator.populateFinals(itemStatus, itemStatusData);
					Assert.fail("Should have failed on final field modification.");
				}
				catch (final ValidationException e)
				{
					persistenceManager.remove(itemStatusData);
				}
			}
		});
	}

}
