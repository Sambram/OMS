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

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.impl.AbstractValidator;
import com.hybris.oms.service.order.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Order Line Quantity ID validator.
 */
public class OrderLineQuantityIdValidator extends AbstractValidator<String>
{

	private static final Logger LOG = LoggerFactory.getLogger(OrderLineQuantityIdValidator.class);

	private OrderService orderService;

	@Override
	public void validateInternal(final ValidationContext context, final String olqId)
	{
		LOG.debug("Validating order line quantity id: {}", olqId);

		try
		{
			this.orderService.getOrderLineQuantityByOlqId(Long.valueOf(olqId));
		}
		catch (final NumberFormatException e)
		{
			context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.NOT_A_NUMBER, "olqId", olqId, null));
		}
		catch (final EntityNotFoundException e)
		{
			context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.INVALID, "olqId", olqId, null));
		}
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}
}
