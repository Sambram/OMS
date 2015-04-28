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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class BinValidatorTest
{
	private static final String LOCATION_ID_VALID = "123";

	@InjectMocks
	private final BinValidator binValidator = new BinValidator();

	@Mock
	private Validator<String> locationIdValidator;

	private ValidationContext context;
	private Bin bin;

	@Before
	public void setUp()
	{
		this.context = new ValidationContext();
		this.bin = this.buildBin();

		Mockito.doNothing().when(locationIdValidator)
				.validate(Mockito.anyString(), Mockito.any(ValidationContext.class), Mockito.any(String.class));
	}

	@Test
	public void shouldValidate()
	{
		this.binValidator.validate("Bin", context, this.bin);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidationBlanks()
	{
		this.bin.setBinCode(" ");
		this.binValidator.validate("Bin", context, this.bin);
		Assert.assertEquals(1, context.getFailureCount());
	}

	private Bin buildBin()
	{
		final Bin b = new Bin();
		b.setLocationId(LOCATION_ID_VALID);
		b.setBinCode("BIN_CODE");
		return b;
	}
}
