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

import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.impl.AbstractValidator;
import com.hybris.oms.facade.validation.impl.order.OrderLineQuantityIdValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Validate that the given olqs exist and do belong to the given shipment.
 */
public class ShipmentOlqValidator extends AbstractValidator<ShipmentOlqDto>
{

	private static final Logger LOG = LoggerFactory.getLogger(ShipmentOlqValidator.class);
	private static final String MESSAGE_OLQ_NOT_IN_SHIPMENT = "This olq does not belong to the given shipment.";

	private OrderLineQuantityIdValidator orderLineQuantityIdValidator;

	@Override
	protected void validateInternal(final ValidationContext context, final ShipmentOlqDto objectToValidate)
	{
		LOG.debug("Validating olqs belong to shipment with shipment id [{}] and olqs [{}].", objectToValidate.getShipment()
				.getShipmentId(), objectToValidate.getOlqIds());

		for (final String olqId : objectToValidate.getOlqIds())
		{
			orderLineQuantityIdValidator.validate("olqId", context, olqId);
			if (!objectToValidate.getShipment().getOlqIds().contains(olqId))
			{
				context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.INVALID, "olqId", olqId,
						MESSAGE_OLQ_NOT_IN_SHIPMENT));
			}
		}
	}

	@Required
	public void setOrderLineQuantityIdValidator(final OrderLineQuantityIdValidator orderLineQuantityIdValidator)
	{
		this.orderLineQuantityIdValidator = orderLineQuantityIdValidator;
	}

	protected OrderLineQuantityIdValidator getOrderLineQuantityIdValidator()
	{
		return this.orderLineQuantityIdValidator;
	}
}
