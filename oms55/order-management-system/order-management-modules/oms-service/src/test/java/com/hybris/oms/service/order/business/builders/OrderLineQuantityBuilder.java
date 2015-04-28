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



/**
 * The Class OrderLineQuantityBuilder.
 * 
 * @author plaguemorin Date: 03/05/12 Time: 3:24 PM
 */
public final class OrderLineQuantityBuilder
{
	/**
	 * Instantiates a new order line quantity builder.
	 */
	private OrderLineQuantityBuilder()
	{

	}

	/**
	 * An order line quantity.
	 * 
	 * @return the order line quantity builder
	 */
	public static OrderLineQuantityBuilder anOrderLineQuantity()
	{
		return new OrderLineQuantityBuilder();
	}
}
