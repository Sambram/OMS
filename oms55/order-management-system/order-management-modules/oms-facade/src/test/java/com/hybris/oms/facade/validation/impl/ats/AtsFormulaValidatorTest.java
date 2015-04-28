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
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.ats.FormulaSyntaxException;
import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.ats.impl.FormulaParser;
import com.hybris.oms.service.ats.impl.Operand;
import com.hybris.oms.service.ats.impl.ParsedFormula;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AtsFormulaValidatorTest
{

	private static final String ATS_ID = "123";
	private static final String OLQ_STATUS = "OPEN";
	private static final String ITEM_STATUS = "ON_HAND";
	private static final String FORMULA = "I[" + ITEM_STATUS + "]-O[" + OLQ_STATUS + "]-T";
	private static final String INVALID = "INVALID";

	@InjectMocks
	private final AtsFormulaValidator atsFormulaValidator = new AtsFormulaValidator();

	@Mock
	private FormulaParser parser;

	@Mock
	private Validator<String> orderLineQuantityStatusCodeValidator;

	@Mock
	private Validator<String> itemStatusCodeValidator;

	private ValidationContext context;
	private AtsFormula formula;

	@Before
	public void setUp()
	{
		this.context = new ValidationContext();
		this.formula = this.buildAtsFormula();

		Mockito.doNothing().when(orderLineQuantityStatusCodeValidator)
				.validate(Mockito.anyString(), Mockito.any(ValidationContext.class), Mockito.any(String.class));
		Mockito.doNothing().when(itemStatusCodeValidator)
				.validate(Mockito.anyString(), Mockito.any(ValidationContext.class), Mockito.any(String.class));
	}

	@Test
	public void shouldValidateAtsFormula()
	{
		Mockito.when(this.parser.parseFormula(ATS_ID, FORMULA)).thenReturn(this.buildParsedFormula());

		this.atsFormulaValidator.validate("AtsFormula", this.context, formula);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailBlanks()
	{
		final AtsFormula atsFormula = buildAtsFormula();
		atsFormula.setAtsId(null);
		atsFormula.setName("");
		atsFormula.setFormula(null);

		Mockito.when(this.parser.parseFormula(null, null)).thenReturn(this.buildParsedFormula());

		this.atsFormulaValidator.validate("AtsFormula", this.context, atsFormula);
		Assert.assertEquals(3, this.context.getFailureCount());
	}

	@Test
	public void shouldFailParser()
	{
		final AtsFormula atsFormula = buildAtsFormula();
		atsFormula.setFormula(INVALID);
		Mockito.when(this.parser.parseFormula(ATS_ID, INVALID)).thenThrow(new FormulaSyntaxException("Invalid Formula"));

		this.atsFormulaValidator.validate("AtsFormula", this.context, atsFormula);
		Assert.assertEquals(1, this.context.getFailureCount());
	}

	/**
	 * Build a fully valid {@link AtsFormula}.
	 */
	private AtsFormula buildAtsFormula()
	{
		final AtsFormula atsFormula = new AtsFormula();
		atsFormula.setAtsId(ATS_ID);
		atsFormula.setName("NAME");
		atsFormula.setFormula(FORMULA);
		return atsFormula;
	}

	/**
	 * Parse formula using real {@link FormulaParser}.
	 */
	private ParsedFormula buildParsedFormula()
	{
		return new ParsedFormula(ATS_ID, Arrays.asList(new Operand(false, StatusRealm.INVENTORY, "ITEM_STATUS"), new Operand(true,
				StatusRealm.ORDER, "OLQ_STATUS")), true);
	}
}
