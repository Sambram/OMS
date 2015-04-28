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
package com.hybris.oms.facade.conversion.impl.order;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.ContactVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.preference.TenantPreferenceService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converter for order data to dto.
 */
public class OrderPopulator extends AbstractPopulator<OrderData, Order>
{

	private Converters converters;

	private Converter<OrderLineData, OrderLine> orderLineConverter;

	private Converter<PaymentInfoData, PaymentInfo> paymentInfoConverter;

	private Converter<ShippingAndHandlingData, ShippingAndHandling> shippingAndHandlingConverter;

	private Converter<AddressVT, Address> addressConverter;

	private Converter<ContactVT, Contact> contactConverter;

	private OrderService orderService;

	private TenantPreferenceService tenantPreferenceService;

	@Override
	public void populate(final OrderData source, final Order target) throws ConversionException
	{
		target.setState(source.getState());
		target.setCurrencyCode(source.getCurrencyCode());
		target.setContact(contactConverter.convert(source.getContact()));
		target.setCustomerLocale(source.getCustomerLocale());
		target.setEmailid(source.getEmailid());
		target.setFirstName(source.getFirstName());
		target.setIssueDate(source.getIssueDate());
		target.setLastName(source.getLastName());
		target.setOrderId(source.getOrderId());
		target.setScheduledShippingDate(source.getScheduledShippingDate());

		this.populateOrderLines(source, target);
		target.setPaymentInfos(this.converters.convertAll(source.getPaymentInfos(), this.paymentInfoConverter));

		target.setPriorityLevelCode(source.getPriorityLevelCode());
		this.populateShipping(source, target);
		target.setUsername(source.getUsername());
		target.setLocationIds(source.getStockroomLocationIds());

		target.setCancellable(orderService.isOrderCancellable(source));

		populateBaseStore(source, target);
	}


	protected void populateBaseStore(final OrderData source, final Order target)
	{
		final BaseStoreData baseStoreData = source.getBaseStore();
		if (baseStoreData != null)
		{
			target.setBaseStoreName(baseStoreData.getName());
		}
	}

	protected void populateShipping(final OrderData source, final Order target)
	{
		target.setShippingAddress(this.addressConverter.convert(source.getShippingAddress()));
		target.setShippingAndHandling(this.shippingAndHandlingConverter.convert(source.getShippingAndHandling()));
		target.setShippingFirstName(source.getShippingFirstName());
		target.setShippingLastName(source.getShippingLastName());
		target.setShippingMethod(source.getShippingMethod());
		target.setShippingTaxCategory(source.getShippingTaxCategory());
	}

	protected void populateOrderLines(final OrderData source, final Order target)
	{
		target.setOrderLines(this.converters.convertAll(source.getOrderLines(), this.orderLineConverter));
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setOrderLineConverter(final Converter<OrderLineData, OrderLine> orderLineConverter)
	{
		this.orderLineConverter = orderLineConverter;
	}

	@Required
	public void setPaymentInfoConverter(final Converter<PaymentInfoData, PaymentInfo> paymentInfoConverter)
	{
		this.paymentInfoConverter = paymentInfoConverter;
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
	public void setContactConverter(final Converter<ContactVT, Contact> contactConverter)
	{
		this.contactConverter = contactConverter;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

}
