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
package com.hybris.oms.service.ats.impl;

import com.hybris.oms.service.ats.FormulaSyntaxException;
import com.hybris.oms.service.ats.StatusRealm;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class FormulaParserIntegrationTest
{
	private static final String ATS_ID = "test";

	@Autowired
	private FormulaParser parser;

	@Test(expected = FormulaSyntaxException.class)
	public void testBlank()
	{
		this.parser.parseFormula(ATS_ID, "   ");
	}

	@Test(expected = FormulaSyntaxException.class)
	public void testMissingPrefix()
	{
		this.parser.parseFormula(ATS_ID, "ON_HAND");
	}

	@Test(expected = FormulaSyntaxException.class)
	public void testInvalidPrefix()
	{
		this.parser.parseFormula(ATS_ID, "A[ON_HAND]");
	}

	@Test(expected = FormulaSyntaxException.class)
	public void testMissingClosingBracket()
	{
		this.parser.parseFormula(ATS_ID, "I[ON_HAND");
	}

	@Test(expected = FormulaSyntaxException.class)
	public void testTwoOpeningBrackets()
	{
		this.parser.parseFormula(ATS_ID, "I[[ON_HAND]]");
	}

	@Test(expected = FormulaSyntaxException.class)
	public void testMissingStatus()
	{
		this.parser.parseFormula(ATS_ID, "I[]");
	}

	@Test(expected = FormulaSyntaxException.class)
	public void testSyntaxSecondOperand()
	{
		this.parser.parseFormula(ATS_ID, "I:ON_HAND+O:");
	}

	@Test
	public void shouldParseThreshold()
	{
		final ParsedFormula formula = this.parser.parseFormula(ATS_ID, "I[ON_HAND]-T");
		this.assertValidFormula(formula, false);
		Assert.assertTrue(formula.isThreshold());
	}

	@Test
	public void shouldAllowThresholdAnywhere()
	{
		final ParsedFormula formula = this.parser.parseFormula(ATS_ID, "-T+I[ON_HAND]");
		this.assertValidFormula(formula, false);
		Assert.assertTrue(formula.isThreshold());
	}

	@Test(expected = FormulaSyntaxException.class)
	public void shouldValidateMultipleThreshold()
	{
		this.parser.parseFormula(ATS_ID, "-T+I[ON_HAND]-T");
	}

	@Test(expected = FormulaSyntaxException.class)
	public void shouldValidatePositiveThreshold()
	{
		this.parser.parseFormula(ATS_ID, "I[ON_HAND]+T");
	}

	@Test(expected = FormulaSyntaxException.class)
	public void shouldValidateThresholdOnly()
	{
		this.parser.parseFormula(ATS_ID, "-T");
	}

	private void assertValidFormula(final ParsedFormula formula, final boolean negative)
	{
		Assert.assertEquals(1, formula.getOperands().size());
		final Operand first = formula.getOperands().get(0);
		Assert.assertEquals(negative, first.isNegativeSign());
		Assert.assertEquals(StatusRealm.INVENTORY, first.getStatusRealm());
		Assert.assertEquals("ON_HAND", first.getStatusCode());
	}

	@Test
	public void shouldParseNegativeFirstOperand()
	{
		final ParsedFormula formula = this.parser.parseFormula(ATS_ID, "-I[ON_HAND]");
		this.assertValidFormula(formula, true);
	}

	@Test
	public void testParse()
	{
		final ParsedFormula formula = this.parser.parseFormula(ATS_ID, "I[ON_HAND]-O[PICKED]");
		Assert.assertEquals(2, formula.getOperands().size());
		final Operand first = formula.getOperands().get(0);
		Assert.assertFalse(first.isNegativeSign());
		Assert.assertEquals(StatusRealm.INVENTORY, first.getStatusRealm());
		Assert.assertEquals("ON_HAND", first.getStatusCode());
		final Operand second = formula.getOperands().get(1);
		Assert.assertTrue(second.isNegativeSign());
		Assert.assertEquals(StatusRealm.ORDER, second.getStatusRealm());
		Assert.assertEquals("PICKED", second.getStatusCode());
		Assertions.assertThat(formula.getStatusCodes(StatusRealm.INVENTORY)).containsOnly("ON_HAND");
		Assertions.assertThat(formula.getStatusCodes(StatusRealm.ORDER)).containsOnly("PICKED");
	}
}
