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
package com.hybris.oms.service.cis.conversion;

import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisAddressType;
import com.hybris.cis.api.model.CisLineItem;
import com.hybris.cis.api.model.CisOrder;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.preference.impl.DefaultTenantPreferenceService;
import com.hybris.oms.service.returns.ReturnService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConversionException;
import org.springframework.beans.factory.annotation.Required;


public class CisReturnConverter
{
	private CisAddressConverter cisAddressConverter;
	private TenantPreferenceService tenantPreferenceService;
	private ReturnService returnService;
	private OrderService orderService;

	public CisOrder convertOmsReturnToCisOrder(final ReturnData aReturn)
	{
		int shippingLineId = 0;
		if (aReturn.getReturnOrderLines() == null || aReturn.getReturnOrderLines().isEmpty())
		{
			throw new IllegalArgumentException("Return orderLines cannot be null or empty.");
		}
		final CisOrder cisOrder = new CisOrder();
		try
		{
			cisOrder.setId(aReturn.getOrder().getOrderId().concat(".").concat(String.valueOf(aReturn.getReturnId())));
			cisOrder.setDate(aReturn.getCreationTime());

			final OrderLineData orderLineData = orderService.getOrderLineByOrderIdAndOrderLineId(aReturn.getOrder().getOrderId(),
					aReturn.getReturnOrderLines().get(0).getOrderLineId());
			cisOrder.setCurrency(orderLineData.getUnitPriceCurrencyCode());
			cisOrder.getAddresses().add(
					convertOmsAddressToCisAddress(CisAddressType.SHIP_TO, aReturn.getOrder().getShippingAddress()));


			// Get RoL and set line items
			final List<CisLineItem> cisLineItems = new ArrayList<>();
			CisLineItem cisLineItem;

			for (final ReturnOrderLineData returnOrderLineData : aReturn.getReturnOrderLines())
			{
				cisLineItem = new CisLineItem();
				cisLineItem.setId((int) returnOrderLineData.getReturnOrderLineId());
				cisLineItem.setItemCode(orderLineData.getSkuId());
				cisLineItem.setTaxCode(orderLineData.getTaxCategory());
				cisLineItem.setQuantity(returnOrderLineData.getQuantity().getValue());
				cisLineItem.setUnitPrice(BigDecimal.valueOf(orderLineData.getUnitPriceValue()));

				cisLineItems.add(cisLineItem);
				shippingLineId = (int) returnOrderLineData.getReturnOrderLineId();
			}

			final TenantPreferenceData tenantPreference = this.tenantPreferenceService
					.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_RETURN_INCL_SHIPPING_COST);
			if (Boolean.valueOf(tenantPreference.getValue())
					&& !(returnService.shippingPreviouslyRefunded(aReturn, aReturn.getOrder())))
			{
				final CisLineItem shippingCisLineItem = new CisLineItem();
				shippingCisLineItem.setId(shippingLineId + 1);
				shippingCisLineItem.setItemCode("delivery line item");
				shippingCisLineItem.setTaxCode(aReturn.getOrder().getShippingTaxCategory());
				shippingCisLineItem.setQuantity(1);
				shippingCisLineItem.setUnitPrice(BigDecimal.valueOf(aReturn.getOrder().getShippingAndHandling().getShippingPrice()
						.getSubTotalValue()));
				cisLineItems.add(shippingCisLineItem);
			}

			cisOrder.setLineItems(cisLineItems);
		}
		catch (final Exception e)
		{
			throw new ConversionException("Something went wrong while converting Return to CisOrder.");
		}
		return cisOrder;
	}

	protected CisAddress convertOmsAddressToCisAddress(final CisAddressType addressType, final AddressVT address)
	{
		final CisAddress cisAddress = new CisAddress();
		cisAddress.setType(addressType);
		cisAddress.setAddressLine1(address.getAddressLine1());
		cisAddress.setAddressLine2(address.getAddressLine2());
		cisAddress.setZipCode(address.getPostalZone());
		cisAddress.setCity(address.getCityName());
		cisAddress.setState(address.getCountrySubentity());
		cisAddress.setCountry(address.getCountryIso3166Alpha2Code());
		return cisAddress;
	}

	@Required
	protected CisAddressConverter getCisAddressConverter()
	{
		return cisAddressConverter;
	}

	public void setCisAddressConverter(final CisAddressConverter cisAddressConverter)
	{
		this.cisAddressConverter = cisAddressConverter;
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

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

	public ReturnService getReturnService()
	{
		return returnService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	public OrderService getOrderService()
	{
		return orderService;
	}
}
