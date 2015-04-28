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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.impl.AbstractValidator;


public class ReturnPaymentInfoValidator extends AbstractValidator<ReturnPaymentInfo>
{
	private static final Logger LOG = LoggerFactory.getLogger(ReturnPaymentInfoValidator.class);

	@Override
	protected void validateInternal(final ValidationContext context, final ReturnPaymentInfo objectToValidate)
	{
		LOG.debug("Validating Return Payment info.");
	}
}
