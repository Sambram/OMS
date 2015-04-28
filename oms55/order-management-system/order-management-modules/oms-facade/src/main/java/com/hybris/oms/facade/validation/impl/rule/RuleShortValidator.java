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
package com.hybris.oms.facade.validation.impl.rule;

import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default validator for {@link RuleShort}.
 */
public class RuleShortValidator extends AbstractValidator<RuleShort>
{

	private static final Logger LOG = LoggerFactory.getLogger(RuleShortValidator.class);

	private Validator<String> orderLineQuantityStatusCodeValidator;

	private Validator<String> itemStatusCodeValidator;

	@Override
	protected void validateInternal(final ValidationContext context, final RuleShort rule)
	{
		LOG.debug("Validating rule.");
		if (rule.getChange() != null)
		{
			FieldValidatorFactory.getStringFieldValidator(context).isNumber("change", rule.getChange());
		}
		FieldValidatorFactory.getStringFieldValidator(context).notBlank("updateStrategy", rule.getUpdateStrategy());
		this.orderLineQuantityStatusCodeValidator.validate("Rule.olqFromStatus", context, rule.getOlqFromStatus());
		this.orderLineQuantityStatusCodeValidator.validate("Rule.olqToStatus", context, rule.getOlqToStatus());
		this.itemStatusCodeValidator.validate("Rule.inventoryStatus", context, rule.getInventoryStatus());

	}

	@Required
	public void setItemStatusCodeValidator(final Validator<String> itemStatusCodeValidator)
	{
		this.itemStatusCodeValidator = itemStatusCodeValidator;
	}

	@Required
	public void setOrderLineQuantityStatusCodeValidator(final Validator<String> orderLineQuantityStatusCodeValidator)
	{
		this.orderLineQuantityStatusCodeValidator = orderLineQuantityStatusCodeValidator;
	}

	protected Validator<String> getItemStatusCodeValidator()
	{
		return itemStatusCodeValidator;
	}

	protected Validator<String> getOrderLineQuantityStatusCodeValidator()
	{
		return orderLineQuantityStatusCodeValidator;
	}
}
