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

import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;


/**
 * Default order line quantity validator.
 */
public class OrderLineQuantityValidator extends AbstractValidator<OrderLineQuantity>
{
	private static final Logger LOG = LoggerFactory.getLogger(OrderLineQuantityValidator.class);

	private Validator<OrderLineQuantityStatus> orderLineQuantityStatusValidator;

	private Validator<Shipment> shipmentValidator;

	private InventoryService inventoryService;

	@Override
	protected void validateInternal(final ValidationContext context, final OrderLineQuantity orderLineQuantity)
	{
		LOG.debug("Validating order line quantity with id: {}", orderLineQuantity.getOlqId());

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("OrderLineQuantity.olqId", orderLineQuantity.getOlqId());

		this.orderLineQuantityStatusValidator.validate("OrderLineQuantity.orderLineQuantityStatus", context,
				orderLineQuantity.getStatus());

		validateLocation(context, orderLineQuantity);
		validateShipment(context, orderLineQuantity);
	}

	protected void validateLocation(final ValidationContext context, final OrderLineQuantity orderLineQuantity)
	{
		final List<StockroomLocationData> locations = inventoryService.findLocationsByLocationIds(new HashSet<String>(Lists
				.newArrayList(orderLineQuantity.getLocation())));

		if (CollectionUtils.isEmpty(locations))
		{
			context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.INVALID, "OrderLineQuantity.location",
					orderLineQuantity.getLocation(), "null"));
		}

	}

	protected void validateShipment(final ValidationContext context, final OrderLineQuantity orderLineQuantity)
	{
		// shipment is not mandatory, therefore it will only be validated when it exists
		if (orderLineQuantity.getShipment() != null)
		{
			this.shipmentValidator.validate("OrderLineQuantity.shipment", context, orderLineQuantity.getShipment());
		}
	}

	@Required
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	@Required
	public void setOrderLineQuantityStatusValidator(final Validator<OrderLineQuantityStatus> orderLineQuantityStatusValidator)
	{
		this.orderLineQuantityStatusValidator = orderLineQuantityStatusValidator;
	}

	@Required
	public void setShipmentValidator(final Validator<Shipment> shipmentValidator)
	{
		this.shipmentValidator = shipmentValidator;
	}

	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}

	protected Validator<OrderLineQuantityStatus> getOrderLineQuantityStatusValidator()
	{
		return orderLineQuantityStatusValidator;
	}

	protected Validator<Shipment> getShipmentValidator()
	{
		return shipmentValidator;
	}

}
