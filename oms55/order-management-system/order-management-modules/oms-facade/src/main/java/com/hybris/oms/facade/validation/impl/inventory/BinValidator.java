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
package com.hybris.oms.facade.validation.impl.inventory;

import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Validator for {@link Bin}.
 */
public class BinValidator extends AbstractValidator<Bin>
{
	private static final Logger LOG = LoggerFactory.getLogger(BinValidator.class);

	private Validator<String> locationIdValidator;

	@Override
	protected void validateInternal(final ValidationContext context, final Bin bin)
	{
		LOG.debug("Validating bin.");

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("Bin.binCode", bin.getBinCode());
		this.locationIdValidator.validate("Bin.locationId", context, bin.getLocationId());
	}

	@Required
	public void setLocationIdValidator(final Validator<String> locationIdValidator)
	{
		this.locationIdValidator = locationIdValidator;
	}

	protected Validator<String> getLocationIdValidator()
	{
		return locationIdValidator;
	}
}
