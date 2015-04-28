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
package com.hybris.oms.facade.validation.impl.order;

import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;
import com.hybris.oms.service.basestore.BaseStoreService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default order validator.
 */
public class OrderValidator extends AbstractValidator<Order>
{
	private static final Logger LOG = LoggerFactory.getLogger(OrderValidator.class);

	private Validator<String> currencyValidator;

	private Validator<OrderLine> orderLineValidator;

	private Validator<PaymentInfo> paymentInfoValidator;

	private Validator<Address> addressValidator;

	private InventoryService inventoryService;

	private BaseStoreService baseStoreService;

	@Override
	public void validateInternal(final ValidationContext context, final Order order)
	{
		LOG.debug("Validating order with order id: {}", order.getOrderId());

		this.validateBusinessRules(context, order);

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("Order.orderId", order.getOrderId())
				.notBlank("Order.userName", order.getUsername());

		FieldValidatorFactory.getDateFieldValidator(context).notNull("Order.issueDate", order.getIssueDate());
		this.validateScheduledShippingDate(context, order);

		FieldValidatorFactory.getGenericFieldValidator(context).validateCollection("Order.orderLines", order.getOrderLines(),
				this.orderLineValidator);

		this.currencyValidator.validate("Order.currencyCode", context, order.getCurrencyCode());
	}

	protected void validateScheduledShippingDate(final ValidationContext context, final Order order)
	{
		if ((order.getScheduledShippingDate() != null) && (order.getScheduledShippingDate().before(order.getIssueDate())))
		{
			context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.LESS_THAN,
					"Order.scheduledShippingDate", order.getScheduledShippingDate().toString(),
					"order scheduled shipping date is before the issue date"));
		}

	}

	protected void validateBusinessRules(final ValidationContext context, final Order order)
	{
		this.validatePaymentInfo(context, order);
		this.validateShippingInfo(context, order);
		this.validateShippingTaxCategory(context, order);
		this.validateBaseStoreName(context, order);
		this.validateOrderLineIdsAreUnique(context, order);
		this.validateShipmentOlqIds(context, order);
	}


	protected void validateOrderLineIdsAreUnique(final ValidationContext context, final Order order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderLines()))
		{
			final Collection<String> orderLineIdlist = new HashSet<>();

			for (final OrderLine orderLine : order.getOrderLines())
			{
				if (!orderLineIdlist.add(orderLine.getOrderLineId()))
				{
					context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.INVALID, "Order.orderLines",
							order.getOrderLines().toString(), "order have more than one orderline with the same orderLineId"));
				}
			}
		}
	}

	protected void validatePaymentInfo(final ValidationContext context, final Order order)
	{
		FieldValidatorFactory.getGenericFieldValidator(context).validateCollection("Order.paymentInfos", order.getPaymentInfos(),
				this.paymentInfoValidator);
	}

	protected void validateShippingInfo(final ValidationContext context, final Order order)
	{
		if (!this.allOrderLinesArePickup(order))
		{

			FieldValidatorFactory.getGenericFieldValidator(context).notNull("Order.shippingAddress", order.getShippingAddress())
					.notNull("Order.shippingAndHandling", order.getShippingAndHandling());

			FieldValidatorFactory.getStringFieldValidator(context).notBlank("Order.shippingFirstName", order.getShippingFirstName())
					.notBlank("Order.shippingLastName", order.getShippingLastName())
					.notBlank("Order.shippingMethod", order.getShippingMethod());

			this.addressValidator.validate("Order.shippingAddress", context, order.getShippingAddress());
		}
	}

	protected boolean allOrderLinesArePickup(final Order order)
	{
		if (CollectionUtils.isNotEmpty(order.getOrderLines()))
		{
			for (final OrderLine orderLine : order.getOrderLines())
			{
				if (orderLine.getPickupStoreId() == null)
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	protected void validateShippingTaxCategory(final ValidationContext context, final Order order)
	{
		FieldValidatorFactory.getStringFieldValidator(context)
				.notBlank("Order.shippingTaxCategory", order.getShippingTaxCategory());
	}

	protected void validateBaseStoreName(final ValidationContext context, final Order order)
	{
		final String baseStoreName = order.getBaseStoreName();
		if (baseStoreName != null)
		{
			final BaseStoreData baseStoreData = this.baseStoreService.findBaseStoreByName(baseStoreName);
			if (baseStoreData == null)
			{
				context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.INVALID, "Order.baseStoreName",
						baseStoreName, "Base store name doesn't exist!"));
			}
		}
	}

	protected void validateShipmentOlqIds(final ValidationContext context, final Order order)
	{
		final HashMap<String, OrderLineQuantity> orderOlqs = new HashMap<String, OrderLineQuantity>();
		final HashMap<String, Shipment> orderShipments = new HashMap<String, Shipment>();

		if (CollectionUtils.isNotEmpty(order.getOrderLineQuantities()))
		{
			for (final OrderLineQuantity olq : order.getOrderLineQuantities())
			{
				orderOlqs.put(olq.getOlqId(), olq);
				if (olq.getShipment() != null)
				{
					orderShipments.put(olq.getShipment().getShipmentId(), olq.getShipment());
				}
			}

			final Set<String> shipmentSet = orderShipments.keySet();
			final Iterator<String> it = shipmentSet.iterator();
			while (it.hasNext())
			{
				final Shipment tempShipment = orderShipments.get(it.next());
				if (CollectionUtils.isNotEmpty(tempShipment.getOlqIds()) && !orderOlqs.keySet().containsAll(tempShipment.getOlqIds()))
				{
					context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.INVALID, "Shipment.olqIds",
							tempShipment.getOlqIds().toString(), "null", "Referenced shipment olq id not found on order."));
				}
			}
		}
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Required
	public void setCurrencyValidator(final Validator<String> currencyValidator)
	{
		this.currencyValidator = currencyValidator;
	}

	@Required
	public void setAddressValidator(final Validator<Address> addressValidator)
	{
		this.addressValidator = addressValidator;
	}

	@Required
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	@Required
	public void setOrderLineValidator(final Validator<OrderLine> orderLineValidator)
	{
		this.orderLineValidator = orderLineValidator;
	}

	@Required
	public void setPaymentInfoValidator(final Validator<PaymentInfo> paymentInfoValidator)
	{
		this.paymentInfoValidator = paymentInfoValidator;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	protected Validator<String> getCurrencyValidator()
	{
		return currencyValidator;
	}

	protected Validator<Address> getAddressValidator()
	{
		return addressValidator;
	}

	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}

	protected Validator<OrderLine> getOrderLineValidator()
	{
		return orderLineValidator;
	}

	protected Validator<PaymentInfo> getPaymentInfoValidator()
	{
		return paymentInfoValidator;
	}

}
