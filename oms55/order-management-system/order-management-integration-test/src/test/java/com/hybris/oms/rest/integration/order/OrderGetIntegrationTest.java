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
package com.hybris.oms.rest.integration.order;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.order.UpdatedSinceList;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;


/**
 * End-to-end integration tests.
 * This class should contain only methods to retrieve data (GET).
 */

public class OrderGetIntegrationTest extends RestClientIntegrationTest
{
	private static final String ORDER_INVALID_ID = "INVALID";

	@Autowired
	private OrderFacade orderFacade;

	@Test
	public void testGetOrderWithOrderId()
	{
		Order order = this.buildOrder();
		order = this.orderFacade.createOrder(order);
		final String orderId = order.getOrderId();
		final Order orderRetrieved = this.orderFacade.getOrderByOrderId(orderId);
		Assert.assertEquals(orderId, orderRetrieved.getOrderId());
		Assert.assertEquals(orderId, orderRetrieved.getId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testNotFoundGetOrderWithOrderId()
	{
		this.orderFacade.getOrderByOrderId(ORDER_INVALID_ID);
	}

	@Test
	public void testFindAllOrderLineQuantityStatuses()
	{
		final List<OrderLineQuantityStatus> orderLineQuantityStatusList = this.orderFacade.findAllOrderLineQuantityStatuses();
		Assert.assertFalse(orderLineQuantityStatusList.isEmpty());
	}

    @Test
    public void testGetOrderIdsUpdatedAfter()
    {
    	final Date now = new Date();
    	final Order order = this.buildOrder();
		this.orderFacade.createOrder(order);
		
		final UpdatedSinceList<String> orderIds = this.orderFacade.getOrderIdsUpdatedAfter(now);
		Assert.assertFalse(orderIds.isEmpty());
		
		boolean found = false;
		for(final String orderId : orderIds)
		{
			if(order.getOrderId().equals(orderId))
			{
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
    }

}
