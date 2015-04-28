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

import com.google.common.collect.ImmutableSet;
import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class BaseStoreConverterTest
{

	private static final String BASESTORE_DESCRIPTION = "description";
	private static final String BASESTORE_NAME = "baseStoreName";
	private static final String LOCATION_LOCATION_ID = "locationId";

	@Autowired
	private Converter<BaseStoreData, BaseStore> baseStoreConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedBaseStoreData()
	{
		// given
		final BaseStoreData baseStoreData = this.createBaseStoreData();

		// when
		final BaseStore baseStore = this.baseStoreConverter.convert(baseStoreData);

		// then
		Assert.assertEquals(BASESTORE_NAME, baseStore.getName());
		Assert.assertEquals(BASESTORE_DESCRIPTION, baseStore.getDescription());
	}

	@Transactional
	@Test
	public void convertingFlushedBaseStoreData()
	{
		// given
		final BaseStoreData baseStoreData = this.createBaseStoreData();

		this.persistenceManager.flush();

		// when
		final BaseStore baseStore = this.baseStoreConverter.convert(baseStoreData);

		// then
		Assert.assertEquals(BASESTORE_NAME, baseStore.getName());
		Assert.assertEquals(BASESTORE_DESCRIPTION, baseStore.getDescription());
	}

	private BaseStoreData createBaseStoreData()
	{
		final StockroomLocationData locationData = this.persistenceManager.create(StockroomLocationData.class);
		locationData.setLocationId(LOCATION_LOCATION_ID);
		locationData.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));

		final BaseStoreData baseStoreData = this.persistenceManager.create(BaseStoreData.class);
		baseStoreData.setName(BASESTORE_NAME);
		baseStoreData.setDescription(BASESTORE_DESCRIPTION);
		baseStoreData.setOrders(null);

		return baseStoreData;
	}
}
