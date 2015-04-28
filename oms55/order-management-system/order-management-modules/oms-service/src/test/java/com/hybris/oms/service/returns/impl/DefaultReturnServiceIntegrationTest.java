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
package com.hybris.oms.service.returns.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.returns.ReturnService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-service-spring-test.xml")
@SuppressWarnings({"PMD.ExcessiveImports"})
public class DefaultReturnServiceIntegrationTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultReturnServiceIntegrationTest.class);

	private static final long NOT_EXISTING_Return_ID = 555555l;
	private static final String SKU_ID = "1";

	private static final long RETURN_ID = 21l;
	private static final long RETURN_ID_2 = 1l;
	private static final long RETURN_ID_3 = 29l;

	private static final long RETURN_ORDER_LINE_ID = 3l;

	private static final String ORDER_ID = "23";
	private static final String ORDER_ID_2 = "84";

	// The Order to test contains 5 items and there is another existing return with 3 returned items
	private static final int QUANTITY = 2;

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	@Autowired
	private ReturnService returnService;
	@Autowired
	private ImportService importService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ReturnQueryFactory returnQueryFactory;

	@Before
	public void setUp()
	{
		LOG.debug("Load testing values");
		this.importService
		.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/returns/test-return-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void shouldFindReturnId()
	{
		final ReturnData aReturn = this.returnService.findReturnById(RETURN_ID);
		assertNotNull(aReturn);
		assertEquals(RETURN_ID, aReturn.getReturnId());
	}

	@Test
	@Transactional
	public void shouldFindReturnOrderLineById()
	{
		final ReturnOrderLineData rolData = this.returnService.findReturnOrderLineById(RETURN_ORDER_LINE_ID);
		assertNotNull(rolData);
		assertEquals(RETURN_ORDER_LINE_ID, rolData.getReturnOrderLineId());
	}

	@Test
	@Transactional
	public void shouldFindReturnsByOrderId()
	{
		final ReturnQueryObject queryObject = new ReturnQueryObject();
		queryObject.setOrderIds(Lists.newArrayList(ORDER_ID));

		final Page<ReturnData> pagedResult = this.returnService.findPagedReturnsByQuery(queryObject);

		assertEquals(2, pagedResult.getTotalElements());
	}

	@Test
	@Transactional
	public void shouldFindPagedReturnsByQuery()
	{
		final ReturnQueryObject returnQueryObject = new ReturnQueryObject();
		returnQueryObject.setOrderIds(ImmutableList.of(ORDER_ID));
		final List<ReturnData> returns = returnQueryFactory.findReturnsByQuery(returnQueryObject).resultList();

		// 2 returns exists for this order id
		assertEquals(2, returns.size());
		assertEquals(ORDER_ID, returns.get(0).getOrder().getOrderId());
	}

	@Test
	@Transactional
	public void shouldGetReturnableQuantity()
	{
		final ReturnData aReturn = this.returnService.findReturnById(RETURN_ID);

		for (final ReturnOrderLineData rold : aReturn.getReturnOrderLines())
		{
			final OrderLineData olData = orderService.getOrderLineByOrderIdAndOrderLineId(aReturn.getOrder().getOrderId(),
					rold.getOrderLineId());
			final QuantityVT qty = returnService.getReturnableQuantity(aReturn, aReturn.getOrder(), olData.getOrderLineId(),
					olData.getSkuId());

			assertNotNull(qty);
			assertEquals(QUANTITY, qty.getValue());
		}
	}

	@Test
	@Transactional
	public void shouldGetReturnableQuantity_TwoOrderLinesForSameSKU()
	{
		final ReturnData aReturn = this.returnService.findReturnById(RETURN_ID_3);

		for (final ReturnOrderLineData rold : aReturn.getReturnOrderLines())

		{
			final OrderLineData olData = orderService.getOrderLineByOrderIdAndOrderLineId(aReturn.getOrder().getOrderId(),
					rold.getOrderLineId());
			final QuantityVT qty = returnService.getReturnableQuantity(aReturn, aReturn.getOrder(), olData.getOrderLineId(),
					olData.getSkuId());

			assertNotNull(qty);
			// first line should return quantity 25 and second line should return 5
			if (rold.getOrderLineId().equals("25"))
			{
				assertEquals(25, qty.getValue());
			}
			else if (rold.getOrderLineId().equals("26"))
			{
				assertEquals(5, qty.getValue());
			}
		}
	}

	// Order 24 -> Return 23 - refunded = false
	// Order 24 -> Return 20 - refunded = true
	@Test
	@Transactional
	public void previouslyRefundedShipping()
	{
		final ReturnData aReturn = this.returnService.findReturnById(23);
		final OrderData order = this.orderService.getOrderByOrderId(ORDER_ID_2);
		assertTrue(returnService.shippingPreviouslyRefunded(aReturn, order));
	}

	// OrderData 24 (id: "84") -> Return 23 - refunded = false
	// OrderData 24 (id: "84") -> Return 20 - refunded = true
	@Test
	@Transactional
	public void previouslyDidntRefundedShipping()
	{
		final ReturnData aReturn = this.returnService.findReturnById(20);
		final OrderData order = this.orderService.getOrderByOrderId(ORDER_ID_2);
		assertFalse(returnService.shippingPreviouslyRefunded(aReturn, order));
	}

	// OrderData 24 (id: "84") -> Return 23 - refunded = false
	// OrderData 24 (id: "84")-> Return 20 - refunded = true
	@Test
	@Transactional
	public void nullReturnIdRefundedShipping()
	{
		final OrderData order = this.orderService.getOrderByOrderId(ORDER_ID_2);
		assertTrue(returnService.shippingPreviouslyRefunded(null, order));
	}

	// OrderData 25 (id: "86")-> return 24 (true for shipping cost but it is in canceled state)
	// OrderData 25 (id: "86")-> return 25 , return 22, return 21
	@Test
	@Transactional
	public void previouslyRefundedShippingWithCanceledState()
	{
		final ReturnData aReturn = this.returnService.findReturnById(25l);
		final OrderData order = this.orderService.getOrderByOrderId("86");
		assertFalse(returnService.shippingPreviouslyRefunded(aReturn, order));
	}

	@Test
	@Transactional
	public void testReturnExistence()
	{
		final ReturnData return1 = this.returnService.findReturnById(RETURN_ID_2);

		Assert.assertNotNull(return1);
		Assert.assertEquals(RETURN_ID_2, return1.getReturnId());
		Assert.assertNotNull(return1.getReturnOrderLines());
		Assert.assertEquals(2, return1.getReturnOrderLines().size());
		Assert.assertEquals(
				SKU_ID,
				orderService.getOrderLineByOrderIdAndOrderLineId(return1.getOrder().getOrderId(),
						return1.getReturnOrderLines().get(0).getOrderLineId()).getSkuId());
		final ReturnData return2 = this.returnService.findReturnById(NOT_EXISTING_Return_ID);
		Assert.assertNull(return2);
	}
}
