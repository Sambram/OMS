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
package com.hybris.oms.facade.validation.impl.returns;

import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReturnOrderLineValidator extends AbstractValidator<ReturnOrderLine>
{
	private static final Logger LOG = LoggerFactory.getLogger(ReturnOrderLineValidator.class);

	@Override
	protected void validateInternal(final ValidationContext context, final ReturnOrderLine objectToValidate)
	{
		LOG.debug("Validating Retrun Order Line with return order line id: {}", objectToValidate.getReturnOrderLineId());

		FieldValidatorFactory.getNumberFieldValidator(context).equalOrGreaterThan("Quantity.value",
				objectToValidate.getQuantity().getValue(), 0);
	}
}
