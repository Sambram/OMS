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
package com.hybris.oms.domain.order;

import com.hybris.oms.domain.order.jaxb.OrdersList;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class OrdersListTest
{

	@Test
	public void testAddOrder()
	{
		final OrdersList theList = new OrdersList();
		theList.addOrder(new Order());

		Assert.assertEquals(1, theList.getNumberOfOrders());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testGetOrders()
	{
		final OrdersList theList = new OrdersList();
		theList.addOrder(new Order());

		final List<Order> list = theList.getOrders();
		list.add(new Order());
	}

	@Test
	public void testInitializeOrders()
	{
		final OrdersList theList = new OrdersList();
		final List<Order> list = new ArrayList<>();
		list.add(new Order());
		theList.initializeOrders(list);
		Assert.assertEquals(1, theList.getNumberOfOrders());
	}

	@Test
	public void testRemoveOrder()
	{
		final OrdersList theList = new OrdersList();
		final Order order = new Order();
		theList.addOrder(order);
		theList.addOrder(order);
		theList.addOrder(order);
		theList.removeOrder(order);

		Assert.assertEquals(2, theList.getNumberOfOrders());
	}

}
