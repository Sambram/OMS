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
package com.hybris.oms.facade.validation.impl.preference;

import com.hybris.oms.facade.validation.ValidationContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(value = MockitoJUnitRunner.class)
public class PositiveIntegerValidatorTest
{
	private static final String VALID_VALUE = "1";
	private static final String NEGATIVE_VALUE = "-1";
	private static final String EXCEEDS_MAX_VALUE = " 2147483648";

	@InjectMocks
	private final PositiveIntegerValidator positiveIntegerValidator = new PositiveIntegerValidator();

	private ValidationContext context;

	@Before
	public void prepareContext()
	{
		this.context = new ValidationContext();
	}

	@Test
	public void shouldValidate()
	{
		this.positiveIntegerValidator.validate("ats.global.threshold", this.context, VALID_VALUE);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidationOnNegativeValue()
	{
		this.positiveIntegerValidator.validate("ats.global.threshold", this.context, NEGATIVE_VALUE);
		Assert.assertTrue(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidationValueExceedsMax()
	{
		this.positiveIntegerValidator.validate("ats.global.threshold", this.context, EXCEEDS_MAX_VALUE);
		Assert.assertTrue(this.context.containsFailures());
	}
}
