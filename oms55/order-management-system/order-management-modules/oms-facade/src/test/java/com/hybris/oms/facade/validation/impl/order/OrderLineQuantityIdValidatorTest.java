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

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.order.OrderService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(value = MockitoJUnitRunner.class)
public class OrderLineQuantityIdValidatorTest
{

	private static final String OLQ_ID_STR = "5";
	private static final Long OLQ_ID_INT = 5L;
	private static final String NAN = "NAN";

	@InjectMocks
	private final OrderLineQuantityIdValidator olqIdValidator = new OrderLineQuantityIdValidator();

	@Mock
	private OrderService mockedOrderService;

	@Mock
	private OrderLineQuantityData mockedOlq;

	private ValidationContext context;

	@Before
	public void prepareContext()
	{
		this.context = new ValidationContext();
	}

	@Test
	public void shouldValidate()
	{
		Mockito.when(this.mockedOrderService.getOrderLineQuantityByOlqId(OLQ_ID_INT)).thenReturn(this.mockedOlq);
		this.olqIdValidator.validate("OrderLineQuantityId", this.context, OLQ_ID_STR);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidation_DoesNotExist()
	{
		Mockito.when(this.mockedOrderService.getOrderLineQuantityByOlqId(OLQ_ID_INT)).thenThrow(
				new EntityNotFoundException("OLQ not found."));
		this.olqIdValidator.validate("OrderLineQuantityId", this.context, OLQ_ID_STR);
		Assert.assertEquals(1, this.context.getFailureCount());
	}

	@Test
	public void shouldFailValidation_NotANumber()
	{
		this.olqIdValidator.validate("OrderLineQuantityStatusCode", this.context, NAN);
		Assert.assertEquals(1, this.context.getFailureCount());
	}

}
