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
package com.hybris.oms.facade.validation.impl.shipment;

import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default validator for {@link Shipment}.
 */
public class ShipmentValidator extends AbstractValidator<Shipment>
{
	private static final Logger LOG = LoggerFactory.getLogger(ShipmentValidator.class);

	@Override
	public void validateInternal(final ValidationContext context, final Shipment shipment)
	{
		LOG.debug("Validating shipment with shipment id: {}", shipment.getShipmentId());

		this.validateBusinessRules(context, shipment);

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("Shipment.shipmentId", shipment.getShipmentId())
				.notBlank("Shipment.shippingMethod", shipment.getShippingMethod())
				.notBlank("Shipment.orderId", shipment.getOrderId());
	}

	protected void validateBusinessRules(final ValidationContext context, final Shipment shipment)
	{
		validateDelivery(context, shipment);
		validateShippingAndHandling(context, shipment);
	}

	protected void validateDelivery(final ValidationContext context, final Shipment shipment)
	{
		if (shipment.getDelivery() != null)
		{
			FieldValidatorFactory.getStringFieldValidator(context).notBlank("Shipment.delivery.deliveryId",
					shipment.getDelivery().getDeliveryId());
		}
	}

	protected void validateShippingAndHandling(final ValidationContext context, final Shipment shipment)
	{
		FieldValidatorFactory.getGenericFieldValidator(context).notNull("Shipment.shippingAndHandling",
				shipment.getShippingAndHandling());
	}
}
