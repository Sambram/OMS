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
package com.hybris.oms.rest.integration.basestore;

import com.hybris.oms.api.Pageable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;


/**
 * End-to-end integration tests.
 * This class should contain only methods that manipulate data (POST, PUT, GET).
 */
public class BaseStoreIntegrationTest extends RestClientIntegrationTest
{
	@Autowired
	private BaseStoreFacade baseStoreFacade;

	@Autowired
	private OrderFacade orderFacade;

	private String baseStoreName;

	@Before
	public void setUp()
	{
		final Order order = this.buildOrder();

		orderFacade.createOrder(order);
		baseStoreName = generateRandomString();
	}

	@Test
	public void testCreateBaseStore()
	{
		BaseStore baseStore = this.buildBaseStore(baseStoreName);

		baseStore = this.baseStoreFacade.createBaseStore(baseStore);

		Assert.assertEquals(baseStoreName, baseStore.getName());
		Assert.assertEquals("description", baseStore.getDescription());
	}

	@Test
	public void testUpdateBaseStore()
	{
		BaseStore baseStore = this.buildBaseStore(baseStoreName);
		baseStore = this.baseStoreFacade.createBaseStore(baseStore);
		baseStore.setDescription("aaa");

		baseStore = this.baseStoreFacade.updateBaseStore(baseStore);

		Assert.assertEquals(baseStoreName, baseStore.getName());
		Assert.assertEquals("aaa", baseStore.getDescription());
	}

	@Test
	public void testGetBaseStoreByName()
	{
		BaseStore baseStore = this.buildBaseStore(baseStoreName);
		baseStore = this.baseStoreFacade.createBaseStore(baseStore);

		baseStore = this.baseStoreFacade.getBaseStoreByName(baseStoreName);

		Assert.assertEquals(baseStoreName, baseStore.getName());
		Assert.assertEquals("description", baseStore.getDescription());
	}

	@Test
	public void testGetBaseStores()
	{
		final BaseStore baseStore = this.buildBaseStore(baseStoreName);
		this.baseStoreFacade.createBaseStore(baseStore);

		final BaseStoreQueryObject baseStoreQueryObject = new BaseStoreQueryObject();

		final Pageable<BaseStore> baseStores = this.baseStoreFacade.findAllBaseStoresByQuery(baseStoreQueryObject);

		Assert.assertNotNull(baseStores);
		Assert.assertFalse(baseStores.getResults().isEmpty());
	}

}
