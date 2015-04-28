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
package com.hybris.oms.domain.order.jaxb;

import com.hybris.oms.domain.order.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class OrdersList.
 */
@XmlRootElement(name = "orders")
public class OrdersList implements Serializable
{
	private static final long serialVersionUID = -1224077139130354024L;
	/**
	 * The orders.
	 */
	@XmlElement(name = "order")
	private List<Order> orders = new ArrayList<>();

	/**
	 * Adds the order.
	 * 
	 * @param order the order
	 */
	public void addOrder(final Order order)
	{
		if (this.orders == null)
		{
			this.orders = new ArrayList<>();
		}
		this.orders.add(order);
	}

	/**
	 * Gets the orders.
	 * 
	 * @return the orders
	 */
	public List<Order> getOrders()
	{
		return Collections.unmodifiableList(this.orders);
	}

	/**
	 * Gets the number of orders.
	 * 
	 * @return the number of orders
	 */
	public int getNumberOfOrders()
	{
		return this.orders.size();
	}

	/**
	 * Initialize orders.
	 * 
	 * @param newOrders the orders
	 */
	public void initializeOrders(final List<Order> newOrders)
	{
		assert this.orders.isEmpty();
		for (final Order order : newOrders)
		{
			this.addOrder(order);
		}
	}

	/**
	 * Removes the order.
	 * 
	 * @param order the order
	 */
	public void removeOrder(final Order order)
	{
		this.orders.remove(order);
	}


	@SuppressWarnings("unused")
	private void setOrders(final List<Order> orders)
	{
		this.orders = orders;
	}

}
