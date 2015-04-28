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
 * The Class OrderLineBuilder.
 * 
 * @author plaguemorin Date: 03/05/12 Time: 3:15 PM
 */
public final class OrderLineAttributeBuilder
{
	/**
	 * Instantiates a new order line builder.
	 */
	private OrderLineAttributeBuilder()
	{
	}

	/**
	 * An order line.
	 * 
	 * @return the order line builder
	 */
	public static OrderLineAttributeBuilder anOrderAttribute()
	{
		return new OrderLineAttributeBuilder();
	}
}
