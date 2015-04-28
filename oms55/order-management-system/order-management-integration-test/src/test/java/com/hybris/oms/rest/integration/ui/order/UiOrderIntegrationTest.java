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
package com.hybris.oms.rest.integration.ui.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hybris.oms.api.Pageable;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;
import com.hybris.oms.ui.api.order.UIOrder;
import com.hybris.oms.ui.api.order.UIOrderDetails;
import com.hybris.oms.ui.api.order.UiOrderFacade;


/**
 * End-to-end integration tests.
 */
public class UiOrderIntegrationTest extends RestClientIntegrationTest
{
	private static final String ORDER_INVALID_ID = "INVALID";

	@Autowired
	private OrderFacade orderFacade;
	
	@Autowired
	private UiOrderFacade uiOrderFacade;

	@Test
	public void testFindOrdersByQuery()
	{
		Order order = this.buildOrder();
		order = this.orderFacade.createOrder(order);

		final OrderQueryObject query = new OrderQueryObject();
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startCalendar = new Date();
		startCalendar = null;

		Date endCalendar = new Date();
		try
		{
			endCalendar = formatter.parse("2040-12-31");
		}
		catch (final ParseException e)
		{
			endCalendar = null;
		}

		// set a date range that covering every date possible
		query.setStartDate(startCalendar);
		query.setEndDate(endCalendar);
		final Pageable<UIOrder> results = this.uiOrderFacade.findOrdersByQuery(query);

		// Validate that we received a pageable order
		Assert.assertTrue(results.getTotalRecords() > 0);
	}

    @Test
    public void testGetUIOrderWithOrderId()
    {
        Order order = this.buildOrder();
        order = this.orderFacade.createOrder(order);
        final String orderId = order.getOrderId();
        final UIOrder orderRetrieved = this.uiOrderFacade.getUIOrderByOrderId(orderId);
        Assert.assertEquals(orderId, orderRetrieved.getOrderId());
        Assert.assertEquals(orderId, orderRetrieved.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testNotFoundGetUIOrderWithOrderId()
    {
        this.uiOrderFacade.getUIOrderByOrderId(ORDER_INVALID_ID);
    }

	@Test
    public void testGetUIOrderDetailsWithOrderId()
    {
        Order order = this.buildOrder();
        order = this.orderFacade.createOrder(order);
        final String orderId = order.getOrderId();
        final UIOrderDetails orderRetrieved = this.uiOrderFacade.getUIOrderDetailsByOrderId(orderId);
        Assert.assertEquals(orderId, orderRetrieved.getOrderId());
        Assert.assertEquals(orderId, orderRetrieved.getId());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testNotFoundGetUIOrderDetailsWithOrderId()
    {
        this.uiOrderFacade.getUIOrderDetailsByOrderId(ORDER_INVALID_ID);
    }

}
