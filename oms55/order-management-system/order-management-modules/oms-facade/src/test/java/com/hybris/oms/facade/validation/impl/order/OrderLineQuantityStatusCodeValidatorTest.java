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
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
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
public class OrderLineQuantityStatusCodeValidatorTest
{

	private static final String OLQ_STATUS_CODE = "OPEN";
	private static final String INVALID = "INVALID";

	@InjectMocks
	private final OrderLineQuantityStatusCodeValidator olqStatusCodeValidator = new OrderLineQuantityStatusCodeValidator();

	@Mock
	private OrderService mockedOrderService;

	@Mock
	private OrderLineQuantityStatusData mockedOlqStatus;

	private ValidationContext context;

	@Before
	public void prepareContext()
	{
		this.context = new ValidationContext();
	}

	@Test
	public void shouldValidate()
	{
		Mockito.when(this.mockedOrderService.getOrderLineQuantityStatusByStatusCode(OLQ_STATUS_CODE)).thenReturn(
				this.mockedOlqStatus);
		this.olqStatusCodeValidator.validate("OrderLineQuantityStatusCode", this.context, OLQ_STATUS_CODE);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidation()
	{
		Mockito.when(this.mockedOrderService.getOrderLineQuantityStatusByStatusCode(INVALID)).thenThrow(
				new EntityNotFoundException("OLQ status not found."));
		this.olqStatusCodeValidator.validate("OrderLineQuantityStatusCode", this.context, INVALID);
		Assert.assertEquals(1, this.context.getFailureCount());
	}

}
