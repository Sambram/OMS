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
package com.hybris.oms.facade.basestore;

import com.hybris.oms.api.Pageable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.basestore.BaseStoreQueryObject;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.facade.conversion.impl.order.OrderTestUtils;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


/**
 * Integration test for {@link DefaultBaseStoreFacade}.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class DefaultBaseStoreFacadeTest
{

	private static final String LOCATION_LOCATION_ID = "locationId";
	private static final String BASESTORE_DESCRIPTION = "description";
	private static final String BASESTORE_DESCRIPTION_UPDATED = "description updated";
	private static final String BASESTORE_NAME = "baseStoreName";

	private static final String NOT_EXISTING_BASESTORE_NAME = "xxxName";

	@Autowired
	private BaseStoreFacade baseStoreFacade;

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setUp()
	{
		final OrderTestUtils orderUtil = new OrderTestUtils();
		orderUtil.createObjects(persistenceManager);

		final StockroomLocationData locationData = this.persistenceManager.create(StockroomLocationData.class);
		locationData.setLocationId(LOCATION_LOCATION_ID);
		locationData.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		persistenceManager.flush();
	}

	@Transactional
	@Test
	public void testCreateBaseStore()
	{
		final BaseStore input = createBaseStore();
		baseStoreFacade.createBaseStore(input);
		final BaseStore baseStore = baseStoreFacade.getBaseStoreByName(input.getName());

		assertNotNull(baseStore);
		Assert.assertEquals(input.getName(), baseStore.getName());
		Assert.assertEquals(input.getDescription(), baseStore.getDescription());
	}
	
	@Transactional
	@Test(expected = DuplicateEntityException.class)
	public void testCreateDuplicateBaseStore()
	{
		final BaseStore input = createBaseStore();
		baseStoreFacade.createBaseStore(input);
		baseStoreFacade.createBaseStore(input);
	}

	@Transactional
	@Test(expected = EntityValidationException.class)
	public void shouldThrowWhenCreateWithBlankBaseStoreName()
	{
		final BaseStore input = createBaseStore();
		input.setName(null);
		baseStoreFacade.createBaseStore(input);
	}

	@Transactional
	@Test
	public void testUpdateBaseStore()
	{
		final BaseStore input = createBaseStore();
		baseStoreFacade.createBaseStore(input);
		input.setDescription(BASESTORE_DESCRIPTION_UPDATED);
		baseStoreFacade.updateBaseStore(input);

		final BaseStore baseStore = baseStoreFacade.getBaseStoreByName(input.getName());

		assertNotNull(baseStore);
		Assert.assertEquals(input.getName(), baseStore.getName());
		Assert.assertEquals(input.getDescription(), baseStore.getDescription());
	}

	@Transactional
	@Test(expected = EntityValidationException.class)
	public void shouldThrowWhenUpdateWithBlankBaseStoreName()
	{
		final BaseStore input = createBaseStore();
		input.setName(null);
		baseStoreFacade.updateBaseStore(input);
	}

	@Transactional
	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowWhenUpdateWithNotExistingDBBaseStore()
	{
		final BaseStore input = createBaseStore();
		baseStoreFacade.updateBaseStore(input);
	}

	@Transactional
	@Test
	public void testGetBaseStoreByName()
	{
		createBaseStoreData();

		final BaseStore baseStore = baseStoreFacade.getBaseStoreByName(BASESTORE_NAME);

		assertNotNull(baseStore);
		Assert.assertEquals(BASESTORE_NAME, baseStore.getName());
		Assert.assertEquals(BASESTORE_DESCRIPTION, baseStore.getDescription());
	}

	@Transactional
	@Test
	public void testGetBaseStores()
	{
		createBaseStoreData();

		final BaseStoreQueryObject baseStoreQueryObject = new BaseStoreQueryObject();

		final Pageable<BaseStore> baseStores = baseStoreFacade.findAllBaseStoresByQuery(baseStoreQueryObject);

		assertNotNull(baseStores);

		assertFalse(baseStores.getResults().isEmpty());
	}

	@Transactional
	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowWhenGetNotExistingBaseStore()
	{
		baseStoreFacade.getBaseStoreByName(NOT_EXISTING_BASESTORE_NAME);
	}

	private void createBaseStoreData()
	{
		final BaseStoreData baseStoreData = persistenceManager.create(BaseStoreData.class);
		baseStoreData.setName(BASESTORE_NAME);
		baseStoreData.setDescription(BASESTORE_DESCRIPTION);
		final OrderData orderData = persistenceManager.getByIndex(OrderData.UX_ORDERS_ORDERID, OrderTestUtils.ORDER_ID);
		orderData.setBaseStore(baseStoreData);

		persistenceManager.flush();
	}

	private BaseStore createBaseStore()
	{
		final BaseStore baseStore = new BaseStore();
		baseStore.setName(BASESTORE_NAME);
		baseStore.setDescription(BASESTORE_DESCRIPTION);

		return baseStore;
	}
}
