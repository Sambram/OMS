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
package com.hybris.oms.facade.validation.impl.basestore;

import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Validator for {@link BaseStore}.
 */
public class BaseStoreValidator extends AbstractValidator<BaseStore>
{
	private static final Logger LOG = LoggerFactory.getLogger(BaseStoreValidator.class);

	@Override
	protected void validateInternal(final ValidationContext context, final BaseStore baseStore)
	{
		LOG.debug("Validating base store.");
		FieldValidatorFactory.getStringFieldValidator(context).notBlank("BaseStore.name", baseStore.getName());
	}

}
