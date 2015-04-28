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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.facade.validation.ValidationContext;


public class OrderLineQuantityStatusValidatorTest
{
	private static final String ITEM_STATUS = "ON_HAND";
	private static final String DESCRIPTION = "DESCRIPTION";

	private final OrderLineQuantityStatusValidator orderLineQuantityValidator = new OrderLineQuantityStatusValidator();
	private ValidationContext context;

	@Before
	public void prepareContext()
	{
		this.context = new ValidationContext();
	}

	@Test
	public void shouldValidate()
	{
		this.orderLineQuantityValidator.validate("OrderLineQuantityStatus", this.context, buildOrderLineQuantityStatus());
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailBlanks()
	{
		final OrderLineQuantityStatus orderLineQuantityStatus = buildOrderLineQuantityStatus();
		orderLineQuantityStatus.setStatusCode(null);
		orderLineQuantityStatus.setDescription("  ");
		this.orderLineQuantityValidator.validate("OrderLineQuantityStatus", this.context, orderLineQuantityStatus);
		Assert.assertEquals(2, this.context.getFailureCount());
	}

	/**
	 * Build a fully valid {@link OrderLineQuantityStatus}.
	 */
	private OrderLineQuantityStatus buildOrderLineQuantityStatus()
	{
		final OrderLineQuantityStatus orderLineQuantityStatus = new OrderLineQuantityStatus();
		orderLineQuantityStatus.setStatusCode(ITEM_STATUS);
		orderLineQuantityStatus.setDescription(DESCRIPTION);
		return orderLineQuantityStatus;
	}
}
