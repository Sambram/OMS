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
package com.hybris.oms.rest.web.order;

import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.order.Order;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class OrdersTest
{
	private static final String LOCATION_ID1 = "locationId";
	private static final String LOCATION_ID2 = "locationId2";
	private static final String ORDER_ID = "orderId";

	private final List<String> locations = new ArrayList<String>();
	private Order order;

	@InjectMocks
	private OrdersResource orders;
	@Mock
	private OrderFacade orderServicesApi;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.order = new Order();
		Mockito.when(this.orderServicesApi.getOrderByOrderId(ORDER_ID)).thenReturn(this.order);
		this.locations.add(LOCATION_ID1);
		this.locations.add(LOCATION_ID2);
	}

	@Test
	public void testCreateOrder()
	{
		Mockito.when(this.orderServicesApi.createOrder(this.order)).thenReturn(this.order);
		final Response createdOrder = this.orders.createOrder(this.order);

		Assert.assertSame(this.order, createdOrder.getEntity());
	}

}
