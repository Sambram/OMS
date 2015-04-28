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
package com.hybris.oms.service.order.impl;

import static org.junit.Assert.assertEquals;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderDataPojo;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineDataPojo;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.rule.RuleService;
import com.hybris.oms.service.shipment.ShipmentService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceUnitTest
{

	private static final int ZERO = 0;
	private static final int THREE = 3;
	private static final int SEVEN = 7;
	private static final int TEN = 10;

	@InjectMocks
	private final DefaultOrderService orderService = new DefaultOrderService();

	@Mock
	private OrderQueryFactory orderQueries;
	@Mock
	private TenantPreferenceService tenantPreferenceService;
	@Mock
	private RuleService ruleService;
	@Mock
	private ShipmentService shipmentService;
	@Mock
	private PersistenceManager persistenceManager;

	private OrderData order;
	private OrderLineData orderLine1;
	private OrderLineData orderLine2;

	@Before
	public void setUp()
	{
		order = new OrderDataPojo();
		orderLine1 = new OrderLineDataPojo();
		orderLine2 = new OrderLineDataPojo();

		order.setOrderLines(Lists.newArrayList(orderLine1, orderLine2));
	}

	@Test
	public void shouldRemoveUnassignedQuantities_NoneFulfilled()
	{
		orderLine1.setQuantityUnassignedValue(TEN);
		orderLine1.setQuantityValue(TEN);
		orderLine2.setQuantityUnassignedValue(TEN);
		orderLine2.setQuantityValue(TEN);

		orderService.removeUnassignedQuantities(order);

		for (final OrderLineData orderLine : order.getOrderLines())
		{
			assertEquals(ZERO, orderLine.getQuantityUnassignedValue());
			assertEquals(ZERO, orderLine.getQuantityValue());
		}
	}

	@Test
	public void shouldRemoveUnassignedQuantities_PartiallyFulfilled()
	{
		orderLine1.setQuantityUnassignedValue(THREE);
		orderLine1.setQuantityValue(TEN);
		orderLine2.setQuantityUnassignedValue(THREE);
		orderLine2.setQuantityValue(TEN);

		orderService.removeUnassignedQuantities(order);

		assertEquals(ZERO, orderLine1.getQuantityUnassignedValue());
		assertEquals(SEVEN, orderLine1.getQuantityValue());
		assertEquals(ZERO, orderLine2.getQuantityUnassignedValue());
		assertEquals(SEVEN, orderLine2.getQuantityValue());
	}

	@Test
	public void shouldRemoveUnassignedQuantities_AllFulfilled()
	{
		orderLine1.setQuantityUnassignedValue(ZERO);
		orderLine1.setQuantityValue(TEN);
		orderLine2.setQuantityUnassignedValue(ZERO);
		orderLine2.setQuantityValue(TEN);

		orderService.removeUnassignedQuantities(order);

		assertEquals(ZERO, orderLine1.getQuantityUnassignedValue());
		assertEquals(TEN, orderLine1.getQuantityValue());
		assertEquals(ZERO, orderLine2.getQuantityUnassignedValue());
		assertEquals(TEN, orderLine2.getQuantityValue());
	}
}
