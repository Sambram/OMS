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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class RuleShortValidatorTest
{

	private static final String OLQ_STATUS = "OPEN";
	private static final String ITEM_STATUS = "ON_HAND";
	private static final String INVALID = "INVALID";
	private static final String UPDATE_STRATEGY = "UPDATE_STRATEGY";

	@InjectMocks
	private final RuleShortValidator ruleShortValidator = new RuleShortValidator();

	@Mock
	private Validator<String> orderLineQuantityStatusCodeValidator;

	@Mock
	private Validator<String> itemStatusCodeValidator;

	private ValidationContext context;
	private RuleShort rule;

	@Before
	public void setUp()
	{
		this.context = new ValidationContext();
		this.rule = this.buildRuleShort();

		Mockito.doNothing().when(orderLineQuantityStatusCodeValidator)
				.validate(Mockito.anyString(), Mockito.any(ValidationContext.class), Mockito.any(String.class));
		Mockito.doNothing().when(itemStatusCodeValidator)
				.validate(Mockito.anyString(), Mockito.any(ValidationContext.class), Mockito.any(String.class));
	}

	@Test
	public void shouldValidate()
	{
		this.ruleShortValidator.validate("Rule", this.context, this.rule);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldValidateRuleShortWithNegativeChange()
	{
		this.rule.setPositive(false);
		this.rule.setChange("-1");
		this.ruleShortValidator.validate("Rule", this.context, this.rule);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailValidationChange()
	{
		rule.setChange(INVALID);
	}

	@Test
	public void shouldValidateInventoryStrategies()
	{
		rule.setUpdateStrategy("");
		this.ruleShortValidator.validate("Rule", this.context, rule);
		Assert.assertEquals(1, this.context.getFailureCount());
	}

	/**
	 * Build a fully valid {@link RuleShort}.
	 */
	private RuleShort buildRuleShort()
	{
		final RuleShort ruleShort = new RuleShort();
		ruleShort.setUpdateStrategy(UPDATE_STRATEGY);
		ruleShort.setChange("1");
		ruleShort.setInventoryStatus(ITEM_STATUS);
		ruleShort.setOlqFromStatus(OLQ_STATUS);
		ruleShort.setOlqToStatus(OLQ_STATUS);
		return ruleShort;
	}
}
