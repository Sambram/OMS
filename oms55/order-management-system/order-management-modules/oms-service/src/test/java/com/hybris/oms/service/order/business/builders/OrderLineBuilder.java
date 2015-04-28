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

import com.hybris.oms.service.managedobjects.order.OrderLineData;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class OrderLineBuilder.
 * 
 * @author plaguemorin Date: 03/05/12 Time: 3:15 PM
 */
public final class OrderLineBuilder
{

	/**
	 * The olqs.
	 */
	private final List<OrderLineQuantityBuilder> olqs = new ArrayList<>();


	/**
	 * The attributes.
	 */
	private final List<OrderLineAttributeBuilder> attributes = new ArrayList<>();

	/**
	 * Instantiates a new order line builder.
	 */
	private OrderLineBuilder()
	{
	}

	/**
	 * An order line.
	 * 
	 * @return the order line builder
	 */
	public static OrderLineBuilder anOrderLine()
	{
		return new OrderLineBuilder();
	}


	/**
	 * With ol qs.
	 * 
	 * @param build
	 *           the build
	 * @return the order line builder
	 */
	public OrderLineBuilder withOLQs(final OrderLineQuantityBuilder build)
	{
		this.olqs.add(build);

		return this;
	}

	/**
	 * With order line attribute.
	 * 
	 * @param build
	 *           the order line attribute builder
	 * @return the order line builder
	 */
	public OrderLineBuilder withOrderLineAttribute(final OrderLineAttributeBuilder build)
	{
		this.attributes.add(build);
		return this;
	}

	public static void setQuantitiesAndUnits(final OrderLineData orderLine, final String unitCode, final int value)
	{
		orderLine.setQuantityUnassignedUnitCode(unitCode);
		orderLine.setQuantityValue(value);
		orderLine.setQuantityUnassignedUnitCode(unitCode);
		orderLine.setQuantityUnassignedValue(value);
		orderLine.setUnitPriceCurrencyCode(unitCode);
		orderLine.setUnitPriceValue(value);
		orderLine.setUnitTaxCurrencyCode(unitCode);
		orderLine.setUnitTaxValue(value);
	}
}
