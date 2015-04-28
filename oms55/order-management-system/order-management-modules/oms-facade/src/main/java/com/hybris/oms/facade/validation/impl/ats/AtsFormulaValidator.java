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
package com.hybris.oms.facade.validation.impl.ats;

import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;
import com.hybris.oms.service.ats.FormulaSyntaxException;
import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.ats.impl.FormulaParser;
import com.hybris.oms.service.ats.impl.ParsedFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Ats formula validator.
 */
public class AtsFormulaValidator extends AbstractValidator<AtsFormula>
{
	private static final Logger LOG = LoggerFactory.getLogger(AtsFormulaValidator.class);

	private FormulaParser parser;

	private Validator<String> orderLineQuantityStatusCodeValidator;

	private Validator<String> itemStatusCodeValidator;

	@Override
	public void validateInternal(final ValidationContext context, final AtsFormula atsFormula)
	{
		LOG.debug("Validating ATS Formula. Formula: {}", atsFormula.getFormula());

		FieldValidatorFactory.getStringFieldValidator(context).notBlank("atsId", atsFormula.getAtsId())
				.notBlank("name", atsFormula.getName()).notBlank("formula", atsFormula.getFormula());

		try
		{
			final ParsedFormula formula = this.parser.parseFormula(atsFormula.getAtsId(), atsFormula.getFormula());
			this.validateInventoryStatusCodes(context, formula);
			this.validateOrderStatusCodes(context, formula);
		}
		catch (final FormulaSyntaxException e)
		{
			context.reportFailure(this.getClass().getName(),
					new Failure(FieldValidationType.INVALID, e.getMessage(), atsFormula.getFormula(), null));
		}
	}

	/**
	 * Validates each inventory status in the formula.
	 * 
	 * @param context
	 * @param parsedFormula
	 */
	protected void validateInventoryStatusCodes(final ValidationContext context, final ParsedFormula parsedFormula)
	{
		for (final String status : parsedFormula.getStatusCodes(StatusRealm.INVENTORY))
		{
			this.itemStatusCodeValidator.validate("AtsFormula.itemStatusCode", context, status);
		}
	}

	/**
	 * Validates each order line quantity status in the formula.
	 * 
	 * @param context
	 * @param parsedFormula
	 */
	protected void validateOrderStatusCodes(final ValidationContext context, final ParsedFormula parsedFormula)
	{
		for (final String status : parsedFormula.getStatusCodes(StatusRealm.ORDER))
		{
			this.orderLineQuantityStatusCodeValidator.validate("AtsFormula.orderLineQuantityStatusCode", context, status);
		}
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

	@Required
	public void setParser(final FormulaParser parser)
	{
		this.parser = parser;
	}

	protected Validator<String> getItemStatusCodeValidator()
	{
		return itemStatusCodeValidator;
	}

	protected Validator<String> getOrderLineQuantityStatusCodeValidator()
	{
		return orderLineQuantityStatusCodeValidator;
	}

	protected FormulaParser getParser()
	{
		return parser;
	}
}
