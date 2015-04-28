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

import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.util.AmountVtUtils;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;


public final class OlqDataStaticUtils
{
	private OlqDataStaticUtils()
	{
		// avoid instantiation
	}

	/**
	 * Order Line Quantity Subtotal Amount = olq quantity * order line unit
	 * price.
	 * 
	 * @param olq
	 *           the olq <dt><b>Preconditions:</b>
	 *           <dd>olq must not be null.
	 *           <dd>olq.orderLine must not be null.
	 * @return Amount
	 */
	public static AmountVT calculateOrderLineQuantitySubtotalAmount(final OrderLineQuantityData olq)
			throws IllegalArgumentException
	{
		Preconditions.checkArgument(olq != null, "Cannot calculate olq subtotal if olq is null.");
		final OrderLineData olqData = olq.getOrderLine();
		Preconditions.checkArgument(olqData != null, "Cannot calculate olq subtotal without assigned OrderLine");
		final AmountVT unitPrice = new AmountVT(olqData.getUnitPriceCurrencyCode(), olqData.getUnitPriceValue());
		return AmountVtUtils.multiply(olq.getQuantityValue(), unitPrice);
	}

	/**
	 * Order Line Quantity Tax Amount = olq quantity * order line unit tax.
	 * 
	 * @param olq
	 *           the olq
	 * @return AmountVT
	 */
	public static AmountVT calculateOrderLineQuantityTaxAmount(final OrderLineQuantityData olq)
	{

		if (olq == null)
		{
			throw new IllegalArgumentException("Cannot calculate olq tax if olq is null.");
		}
		// Multiply the olq's quantity with the order line's unit tax
		final AmountVT original = new AmountVT(olq.getOrderLine().getUnitTaxCurrencyCode(), olq.getOrderLine().getUnitTaxValue());
		return AmountVtUtils.multiply(olq.getQuantityValue(), original);
	}

	/**
	 * Get an array of olq id from a list of OrderLineQuantity.
	 * 
	 * @param orderLineQuantities
	 *           the order line quantities
	 * @return Array of String
	 */
	public static List<Long> getOlqIds(final List<OrderLineQuantityData> orderLineQuantities)
	{
		final List<Long> olqIds = new ArrayList<>();
		for (final OrderLineQuantityData orderLineQuantity : orderLineQuantities)
		{
			olqIds.add(orderLineQuantity.getOlqId());
		}
		return olqIds;
	}

}
