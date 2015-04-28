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
package com.hybris.oms.service.order.business.builders;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.managedobjects.order.OrderData;


/**
 * The Class OrderDataBuilder.
 * 
 * @author plaguemorin Date: 03/05/12 Time: 3:02 PM
 */
public final class OrderDataBuilder
{

	/**
	 * The order id.
	 */
	private String orderId;

	/**
	 * The user name.
	 */
	private String userName;

	/**
	 * The persistence manager.
	 */
	private PersistenceManager persistenceManager;

	/**
	 * Instantiates a new order data builder.
	 */
	private OrderDataBuilder()
	{

	}

	/**
	 * An order data.
	 * 
	 * @return the order data builder
	 */
	public static OrderDataBuilder anOrderData()
	{
		return new OrderDataBuilder();
	}

	/**
	 * With id.
	 * 
	 * @param newOrderId
	 *           the order id
	 * @return the order data builder
	 */
	public OrderDataBuilder withId(final String newOrderId)
	{
		this.orderId = newOrderId;
		return this;
	}

	/**
	 * With user name.
	 * 
	 * @param newUserName
	 *           the user name
	 * @return the order data builder
	 */
	public OrderDataBuilder withUserName(final String newUserName)
	{
		this.userName = newUserName;
		return this;
	}

	/**
	 * Builds the.
	 * 
	 * @return the order data
	 */
	public OrderData build()
	{
		final OrderData dummy;

		if (this.persistenceManager != null)
		{
			dummy = this.persistenceManager.create(OrderData.class);
		}
		else
		{
			throw new IllegalArgumentException("please set a persistence manager or implement a mock");
		}

		dummy.setOrderId(this.orderId);
		dummy.setUsername(this.userName);

		return dummy;
	}

	/**
	 * With persistence manager.
	 * 
	 * @param newPersistenceManager
	 *           the persistence manager
	 * @return the order data builder
	 */
	public OrderDataBuilder withPersistenceManager(final PersistenceManager newPersistenceManager)
	{
		this.persistenceManager = newPersistenceManager;
		return this;
	}

}
