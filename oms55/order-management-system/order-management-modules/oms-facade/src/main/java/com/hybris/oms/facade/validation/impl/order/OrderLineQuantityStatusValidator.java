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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;


/**
 * Default order line quantity status validator.
 */
public class OrderLineQuantityStatusValidator extends AbstractValidator<OrderLineQuantityStatus>
{
	private static final Logger LOG = LoggerFactory.getLogger(OrderLineQuantityStatusValidator.class);

	@Override
	protected void validateInternal(final ValidationContext context, final OrderLineQuantityStatus olqStatus)
	{
		LOG.debug("Validating order line quantity status with status code: {}", olqStatus.getStatusCode());

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("ItemStatus.statusCode", olqStatus.getStatusCode())
				.notBlank("ItemStatus.description", olqStatus.getDescription());
	}

}
