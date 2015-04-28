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
package com.hybris.oms.service.order;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.inventory.impl.DefaultInventoryService;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * This tests correctness of the inventory after olq status update.
 * 
 * @See {@link OrderService#updateOrderLineQuantityStatus(OrderLineQuantityData, OrderLineQuantityStatusData)}
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class OLQStatusUpdateCorrectInventoryTest
{
	private static final String OLQ_HYBRIS_ID = "single|OrderLineQuantityData|18";
	private static final String OLQS_HYBRIS_ID_ON_HOLD = "single|OrderLineQuantityStatusData|2";
	private static final String OLQS_HYBRIS_ID_SHIPPED = "single|OrderLineQuantityStatusData|3";

	private static final String ON_HAND = "ON_HAND";

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Autowired
	private ImportService importService;

	@Autowired
	private DefaultInventoryService inventoryService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/inventory/test-inventory-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/rule/test-rule-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void testDecreaseInventoryOnHandIfOLQStatusShipped()
	{
		// given
		this.assertValidInventory(9);

		// when
		final OrderLineQuantityData olq = persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID));
		final OrderLineQuantityStatusData newOlqs = persistenceManager.get(HybrisId.valueOf(OLQS_HYBRIS_ID_SHIPPED));

		orderService.updateOrderLineQuantityStatus(olq, newOlqs);

		// then
		this.assertValidInventory(4);
	}

	@Test
	@Transactional
	public void testRigidInventoryOnHandIfOLQStatusNotShipped()
	{
		// given
		this.assertValidInventory(9);

		final OrderLineQuantityData olq = OLQStatusUpdateCorrectInventoryTest.this.persistenceManager.get(HybrisId
				.valueOf(OLQ_HYBRIS_ID));
		final OrderLineQuantityStatusData newOlqs = OLQStatusUpdateCorrectInventoryTest.this.persistenceManager.get(HybrisId
				.valueOf(OLQS_HYBRIS_ID_ON_HOLD));

		orderService.updateOrderLineQuantityStatus(olq, newOlqs);

		// then
		this.assertValidInventory(9);
	}

	private void assertValidInventory(final int stockValue)
	{
		Assert.assertEquals(stockValue, OLQStatusUpdateCorrectInventoryTest.this.inventoryService
				.getItemQuantityBySkuAndStatusCode("2", ON_HAND).getQuantityValue());
	}

}
