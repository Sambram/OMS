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
package com.hybris.oms.facade.validation.impl.order;

import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.PaymentInfo;
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
public class PaymentInfoValidatorTest
{
	private static final String COUNTRY_CODE_VALID = "US";

	@InjectMocks
	private final PaymentInfoValidator paymentInfoValidator = new PaymentInfoValidator();

	@Mock
	private Validator<Address> addressValidator;

	private ValidationContext context;
	private PaymentInfo paymentInfo;

	@Before
	public void setUp()
	{
		this.context = new ValidationContext();
		this.paymentInfo = this.buildPaymentInfo();

		Mockito.doNothing().when(addressValidator)
				.validate(Mockito.anyString(), Mockito.any(ValidationContext.class), Mockito.any(Address.class));
	}

	@Test
	public void shouldValidate()
	{
		this.paymentInfoValidator.validate("PaymentInfo", this.context, this.paymentInfo);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailBlanks()
	{
		this.paymentInfo.setAuthUrl("");
		this.paymentInfo.setPaymentInfoType(null);

		this.paymentInfoValidator.validate("PaymentInfo", this.context, this.paymentInfo);
		Assert.assertEquals(2, this.context.getFailureCount());
	}

	/**
	 * Build a fully valid payment info.
	 * 
	 * @return payment info
	 */
	private PaymentInfo buildPaymentInfo()
	{
		this.paymentInfo = new PaymentInfo();
		this.paymentInfo.setAuthUrl("http://www.hybris.com");
		this.paymentInfo.setPaymentInfoType("VISA");
		this.paymentInfo.setBillingAddress(new Address("2207 7th Avenue", null, "New York", "NY", "10027", 40.812356, -73.945857,
				COUNTRY_CODE_VALID, "U.S.A.", null, null));
		return this.paymentInfo;
	}
}
