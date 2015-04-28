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
package com.hybris.oms.service.returns.strategy.impl;

import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.preference.impl.DefaultTenantPreferenceService;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.returns.strategy.RefundCalculationStrategy;
import com.hybris.oms.service.util.AmountVtUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * Hybris default implementation for the refund calculation strategy.
 * 
 * we calculate the amount to be refunded by summing up the price of the unit and the unit tax and multiply it with the
 * quantity returned.
 */
public class DefaultRefundCalculationStrategy implements RefundCalculationStrategy
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRefundCalculationStrategy.class);

	private TenantPreferenceService tenantPreferenceService;
	private OrderService orderService;
	private ReturnService returnService;

	@Override
	public AmountVT calculateRefundAmount(final ReturnData returnData)
	{

		LOGGER.trace("calculateRefundAmount");
		Preconditions.checkArgument(returnData != null, "Cannot calculate subtotal if Return is null.");
		Preconditions.checkArgument(returnData.getReturnOrderLines().size() != 0,
				"Cannot calculate subtotal if there is no return order line.");
		// make sure at least one unit price exists and create a result amount that have the same currency code
		Preconditions.checkArgument(returnData.getReturnOrderLines().get(0).getOrderLineId() != null,
				"Cannot calculate subtotal if no order line is linked to the return order order line.");

		final TenantPreferenceData tenantPreference = this.tenantPreferenceService
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_RETURN_INCL_SHIPPING_COST);
		AmountVT subTotal;
		AmountVT amountWithoutShipping;
		final OrderLineData firstOrderLineData = orderService.getOrderLineByOrderIdAndOrderLineId(returnData.getOrder()
				.getOrderId(), returnData.getReturnOrderLines().get(0).getOrderLineId());
		amountWithoutShipping = new AmountVT(firstOrderLineData.getUnitPriceCurrencyCode(), 0.0);

		for (final ReturnOrderLineData returnOrderLineData : returnData.getReturnOrderLines())
		{
			final OrderLineData orderLineData = orderService.getOrderLineByOrderIdAndOrderLineId(returnData.getOrder().getOrderId(),
					returnOrderLineData.getOrderLineId());

			final AmountVT lineAmount = calculateLineAmount(returnOrderLineData,
					returnService.getQuantityRejected(returnOrderLineData), orderLineData);
			amountWithoutShipping = AmountVtUtils.add(amountWithoutShipping, lineAmount);
		}

		if (!shippingIncludedAndNotRefundedYet(returnData, tenantPreference))
		{
			subTotal = amountWithoutShipping;
		}
		else
		{
			AmountVT shippingAmount = null;
			final ShippingAndHandlingData shippingAndHandlingData = returnData.getOrder().getShippingAndHandling();
			if (shippingAndHandlingData != null)
			{
				final PriceVT shippingPrice = shippingAndHandlingData.getShippingPrice();

				if (shippingPrice != null)
				{
					shippingAmount = new AmountVT(firstOrderLineData.getUnitPriceCurrencyCode(), shippingPrice.getSubTotalValue()
							+ shippingPrice.getTaxValue());
				}
			}
			subTotal = AmountVtUtils.add(amountWithoutShipping, shippingAmount);
		}
		return subTotal;
	}

	protected AmountVT calculateLineAmount(final ReturnOrderLineData returnOrderLineData, final int sumRejectedQuantity,
			final OrderLineData orderLineData)
	{
		return AmountVtUtils.multiply(returnOrderLineData.getQuantity().getValue() - sumRejectedQuantity, AmountVtUtils.add(
				orderLineData.getUnitTaxValue(),
				new AmountVT(orderLineData.getUnitPriceCurrencyCode(), orderLineData.getUnitPriceValue())));
	}


	// include shipping cost only if:
	// the Tenant Preference set to include shipping + shipping was not previously refunded +current return wants to
	// include shipping
	protected boolean shippingIncludedAndNotRefundedYet(final ReturnData aReturn, final TenantPreferenceData tenantPreference)
	{
		return Boolean.valueOf(tenantPreference.getValue())
				&& !(this.returnService.shippingPreviouslyRefunded(aReturn, aReturn.getOrder())) && aReturn.isShippingRefunded();
	}

	@Required
	public void setTenantPreferenceService(final DefaultTenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}

	public OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	public ReturnService getReturnService()
	{
		return returnService;
	}

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}
}
