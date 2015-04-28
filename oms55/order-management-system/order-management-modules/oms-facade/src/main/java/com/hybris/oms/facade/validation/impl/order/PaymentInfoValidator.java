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
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default validator for {@link PaymentInfo}.
 */
public class PaymentInfoValidator extends AbstractValidator<PaymentInfo>
{

	private static final Logger LOG = LoggerFactory.getLogger(PaymentInfoValidator.class);

	private Validator<Address> defaultAddressValidator;

	@Override
	public void validateInternal(final ValidationContext context, final PaymentInfo paymentInfo)
	{
		LOG.debug("Validating payment info.");

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("PaymentInfo.authUrl", paymentInfo.getAuthUrl())
				.notBlank("PaymentInfo.paymentInfoType", paymentInfo.getPaymentInfoType());

		// Address Validator will validate billing address country code.
		this.defaultAddressValidator.validate("PaymentInfo.billingAddress", context, paymentInfo.getBillingAddress());
	}

	@Required
	public void setDefaultAddressValidator(final Validator<Address> defaultAddressValidator)
	{
		this.defaultAddressValidator = defaultAddressValidator;
	}

	protected Validator<Address> getDefaultAddressValidator()
	{
		return defaultAddressValidator;
	}

}
