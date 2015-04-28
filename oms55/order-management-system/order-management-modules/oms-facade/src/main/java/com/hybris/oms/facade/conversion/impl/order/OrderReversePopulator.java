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
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.ContactVT;
import com.hybris.oms.service.order.OrderScheduledShippingDateUtils;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converter for order dto to data.
 */
public class OrderReversePopulator implements Populator<Order, OrderData>
{
	private PersistenceManager persistenceManager;

	private Converter<OrderLine, OrderLineData> orderLineReverseConverter;

	private Converter<PaymentInfo, PaymentInfoData> paymentInfoReverseConverter;

	private Converter<ShippingAndHandling, ShippingAndHandlingData> shippingAndHandlingReverseConverter;

	private Converters converters;

	private Converter<Address, AddressVT> addressReverseConverter;

	private Converter<Contact, ContactVT> contactReverseConverter;
	private OrderScheduledShippingDateUtils orderScheduledShippingDateUtils;

	@Override
	public void populateFinals(final Order source, final OrderData target) throws ConversionException
	{
		target.setOrderId(source.getOrderId());
	}

	@Override
	public void populate(final Order source, final OrderData target) throws ConversionException
	{
		target.setCurrencyCode(source.getCurrencyCode());
		target.setContact(contactReverseConverter.convert(source.getContact()));
		target.setCustomerLocale(source.getCustomerLocale());
		target.setEmailid(source.getEmailid());
		target.setFirstName(source.getFirstName());
		target.setIssueDate(source.getIssueDate());
		target.setLastName(source.getLastName());
		target.setPriorityLevelCode(source.getPriorityLevelCode());
		target.setUsername(source.getUsername());
		target.setStockroomLocationIds(source.getLocationIds());
		target.setScheduledShippingDate(orderScheduledShippingDateUtils.populateScheduledShippingDate(source));
		this.populateOrderLines(source, target);
		this.populatePaymentInfos(source, target);
		this.populateShipping(source, target);
		this.populateShipmentsOrderFKAndShippingAndHandling(target);
		this.populateBaseStore(source, target);
	}

	protected void populateBaseStore(final Order source, final OrderData target)
	{
		final String baseStoreName = source.getBaseStoreName();
		if (baseStoreName != null)
		{
			try
			{
				final BaseStoreData baseStoreData = persistenceManager.getByIndex(BaseStoreData.UX_BASESTORES_NAME, baseStoreName);
				target.setBaseStore(baseStoreData);
			}
			catch (final ManagedObjectNotFoundException e)
			{
				throw new ConversionException("baseStoreName=" + baseStoreName + " doesn't exist!", e);
			}
		}
	}

	protected void populateShipping(final Order source, final OrderData target)
	{
		fillOutOrderCurrencyCode(source);
		target.setShippingAddress(this.addressReverseConverter.convert(source.getShippingAddress()));
		target.setShippingAndHandling(this.shippingAndHandlingReverseConverter.convert(source.getShippingAndHandling()));
		target.setShippingFirstName(source.getShippingFirstName());
		target.setShippingLastName(source.getShippingLastName());
		target.setShippingMethod(source.getShippingMethod());
		target.setShippingTaxCategory(source.getShippingTaxCategory());
	}

	protected void populateShipmentsOrderFKAndShippingAndHandling(final OrderData target)
	{
		for (final OrderLineData orderLine : target.getOrderLines())
		{
			for (final OrderLineQuantityData olq : orderLine.getOrderLineQuantities())
			{
				if (olq.getShipment() != null)
				{
					olq.getShipment().setOrderFk(target);
					olq.getShipment().setShippingAndHandling(target.getShippingAndHandling());
				}
			}
		}
	}

	/**
	 * OMS is designed to have an currencyCode on every amount, so this method fills the currency code on every place
	 * needed inside the order.
	 */
	protected void fillOutOrderCurrencyCode(final Order order)
	{
		if (order.getShippingAndHandling() != null && order.getShippingAndHandling().getShippingPrice() != null)
		{
			fillOutAmountCurrencyCode(order.getShippingAndHandling().getShippingPrice().getSubTotal(), order.getCurrencyCode());
			fillOutAmountCurrencyCode(order.getShippingAndHandling().getShippingPrice().getTax(), order.getCurrencyCode());
			fillOutAmountCurrencyCode(order.getShippingAndHandling().getShippingPrice().getTaxCommitted(), order.getCurrencyCode());
		}
	}

	protected void fillOutAmountCurrencyCode(final Amount amount, final String currencyCode)
	{
		if (amount != null && amount.getValue() != null && amount.getCurrencyCode() == null)
		{
			amount.setCurrencyCode(currencyCode);
		}
	}

	protected void populatePaymentInfos(final Order source, final OrderData target)
	{
		target.setPaymentInfos(this.converters.convertAll(source.getPaymentInfos(), this.paymentInfoReverseConverter));
		for (final PaymentInfoData piData : target.getPaymentInfos())
		{
			piData.setMyOrder(target);
		}
	}

	protected void populateOrderLines(final Order source, final OrderData target)
	{
		target.setOrderLines(this.converters.convertAll(source.getOrderLines(), this.orderLineReverseConverter));
		for (final OrderLineData olData : target.getOrderLines())
		{
			olData.setMyOrder(target);
		}
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	@Required
	public void setOrderLineReverseConverter(final Converter<OrderLine, OrderLineData> orderLineReverseConverter)
	{
		this.orderLineReverseConverter = orderLineReverseConverter;
	}

	@Required
	public void setPaymentInfoReverseConverter(final Converter<PaymentInfo, PaymentInfoData> paymentInfoReverseConverter)
	{
		this.paymentInfoReverseConverter = paymentInfoReverseConverter;
	}

	@Required
	public void setShippingAndHandlingReverseConverter(
			final Converter<ShippingAndHandling, ShippingAndHandlingData> shippingAndHandlingReverseConverter)
	{
		this.shippingAndHandlingReverseConverter = shippingAndHandlingReverseConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setAddressReverseConverter(final Converter<Address, AddressVT> addressReverseConverter)
	{
		this.addressReverseConverter = addressReverseConverter;
	}

	@Required
	public void setContactReverseConverter(final Converter<Contact, ContactVT> contactReverseConverter)
	{
		this.contactReverseConverter = contactReverseConverter;
	}

	public void setOrderScheduledShippingDateUtils(final OrderScheduledShippingDateUtils orderScheduledShippingDateUtils)
	{
		this.orderScheduledShippingDateUtils = orderScheduledShippingDateUtils;
	}
}
