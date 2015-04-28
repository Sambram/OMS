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
package com.hybris.oms.facade.validation.impl.returns;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.facade.validation.ValidationContext;


public class ReturnPaymentInfoValidatorTest
{

	private final ReturnPaymentInfoValidator returnPaymentInfoValidator = new ReturnPaymentInfoValidator();

	private ValidationContext context;
	private ReturnPaymentInfo returnPaymentInfo;

	@Before
	public void setUp()
	{
		this.context = new ValidationContext();
		this.returnPaymentInfo = createReturnPaymentInfoDto();
	}

	@Test
	public void shouldValidate()
	{
		this.returnPaymentInfoValidator.validate("ReturnPaymentInfo", context, this.returnPaymentInfo);
		Assert.assertFalse(this.context.containsFailures());
	}

	public static ReturnPaymentInfo createReturnPaymentInfoDto()
	{

		final ReturnPaymentInfo returnPaymentInfo = new ReturnPaymentInfo();
		returnPaymentInfo.setReturnPaymentAmount(new Amount("USD", 5.2));
		returnPaymentInfo.setReturnPaymentInfoId(String.valueOf("555"));
		returnPaymentInfo.setReturnPaymentType("TYPE");
		return returnPaymentInfo;
	}
}
