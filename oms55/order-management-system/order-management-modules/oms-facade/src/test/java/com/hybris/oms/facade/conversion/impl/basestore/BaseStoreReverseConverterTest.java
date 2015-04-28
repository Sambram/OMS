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
package com.hybris.oms.facade.conversion.impl.basestore;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class BaseStoreReverseConverterTest
{
	private static final String BASESTORE_DESCRIPTION = "description";
	private static final String BASESTORE_NAME = "baseStoreName";

	@Autowired
	private Converter<BaseStore, BaseStoreData> baseStoreReverseConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void reverseConvertingNotFlushedBaseStoreData()
	{
		// given
		final BaseStore baseStore = this.createBaseStore();

		// when
		final BaseStoreData baseStoreData = this.baseStoreReverseConverter.convert(baseStore);

		// then
		Assert.assertEquals(BASESTORE_NAME, baseStoreData.getName());
		Assert.assertEquals(BASESTORE_DESCRIPTION, baseStoreData.getDescription());
		Assert.assertNotNull(baseStoreData.getOrders());
	}

	@Transactional
	@Test
	public void reverseConvertingFlushedBaseStoreData()
	{
		// given
		final BaseStore baseStore = this.createBaseStore();

		persistenceManager.flush();

		// when
		final BaseStoreData baseStoreData = this.baseStoreReverseConverter.convert(baseStore);

		// then
		Assert.assertEquals(BASESTORE_NAME, baseStoreData.getName());
		Assert.assertEquals(BASESTORE_DESCRIPTION, baseStoreData.getDescription());
		Assert.assertNotNull(baseStoreData.getOrders());
	}

	private BaseStore createBaseStore()
	{
		final BaseStore baseStore = new BaseStore();
		baseStore.setName(BASESTORE_NAME);
		baseStore.setDescription(BASESTORE_DESCRIPTION);

		return baseStore;
	}
}
