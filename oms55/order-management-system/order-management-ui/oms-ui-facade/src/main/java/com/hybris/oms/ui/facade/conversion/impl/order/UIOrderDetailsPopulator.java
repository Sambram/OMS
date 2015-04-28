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
package com.hybris.oms.ui.facade.conversion.impl.order;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.ui.api.order.UIOrderDetails;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converter for order data to dto.
 */
public class UIOrderDetailsPopulator extends AbstractPopulator<OrderData, UIOrderDetails>
{

	private OrderService orderService;

	private Converters converters;

	private Converter<ShippingAndHandlingData, ShippingAndHandling> shippingAndHandlingConverter;

	private Converter<AddressVT, Address> addressConverter;

	private Converter<PaymentInfoData, PaymentInfo> paymentInfoConverter;

	@Override
	public void populate(final OrderData source, final UIOrderDetails target) throws ConversionException
	{
		target.setCurrencyCode(source.getCurrencyCode());
		target.setCustomerLocale(source.getCustomerLocale());
		target.setEmailid(source.getEmailid());
		target.setFirstName(source.getFirstName());
		target.setIssueDate(source.getIssueDate());
		target.setScheduledShippingDate(source.getScheduledShippingDate());
		target.setLastName(source.getLastName());
		target.setOrderId(source.getOrderId());

		target.setPriorityLevelCode(source.getPriorityLevelCode());
		this.populateShipping(source, target);
		target.setUsername(source.getUsername());
		target.setLocationIds(source.getStockroomLocationIds());
		target.setCancellable(orderService.isOrderCancellable(source));
		target.setPickUpOnlyOrder(orderService.isPickUpOnlyOrder(source));

		target.setPaymentInfos(this.converters.convertAll(source.getPaymentInfos(), this.paymentInfoConverter));

		this.populateShipping(source, target);

		populateBaseStore(source, target);
	}

	protected void populateBaseStore(final OrderData source, final UIOrderDetails target)
	{
		final BaseStoreData baseStoreData = source.getBaseStore();
		if (baseStoreData != null)
		{
			target.setBaseStoreName(baseStoreData.getName());
		}
	}

	protected void populateShipping(final OrderData source, final UIOrderDetails target)
	{
		target.setShippingAddress(this.addressConverter.convert(source.getShippingAddress()));
		target.setShippingAndHandling(this.shippingAndHandlingConverter.convert(source.getShippingAndHandling()));
		target.setShippingFirstName(source.getShippingFirstName());
		target.setShippingLastName(source.getShippingLastName());
		target.setShippingMethod(source.getShippingMethod());
		target.setShippingTaxCategory(source.getShippingTaxCategory());
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setShippingAndHandlingConverter(
			final Converter<ShippingAndHandlingData, ShippingAndHandling> shippingAndHandlingConverter)
	{
		this.shippingAndHandlingConverter = shippingAndHandlingConverter;
	}

	@Required
	public void setAddressConverter(final Converter<AddressVT, Address> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

	@Required
	public void setPaymentInfoConverter(final Converter<PaymentInfoData, PaymentInfo> paymentInfoConverter)
	{
		this.paymentInfoConverter = paymentInfoConverter;
	}


}
