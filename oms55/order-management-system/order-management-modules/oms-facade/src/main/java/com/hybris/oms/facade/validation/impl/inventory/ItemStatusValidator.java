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

import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Item Status validator.
 */
public class ItemStatusValidator extends AbstractValidator<ItemStatus>
{

	private static final Logger LOG = LoggerFactory.getLogger(ItemStatusValidator.class);

	@Override
	public void validateInternal(final ValidationContext context, final ItemStatus itemStatus)
	{
		LOG.debug("Validating item status.");

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("ItemStatus.statusCode", itemStatus.getStatusCode())
				.notBlank("ItemStatus.description", itemStatus.getDescription());
	}

}
