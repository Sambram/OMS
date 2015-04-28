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

import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ReturnOrderLineValidatorTest
{
	@InjectMocks
	private ReturnOrderLineValidator returnOLValidator;

	private ValidationContext context;
	private ReturnOrderLine returnOrderLine;
	@Mock
	private Validator<Quantity> quantityWithPositiveValueValidator;

	@Before
	public void setUp()
	{
		this.context = new ValidationContext();
		this.returnOrderLine = createReturnOrderLineDto();

	}

	@Test
	public void shouldValidate()
	{
		this.returnOLValidator.validate("ReturnOrderLine", context, this.returnOrderLine);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidationBlanks()
	{
		this.returnOrderLine.getQuantity().setValue(-1);
		this.returnOLValidator.validate("Return", context, this.returnOrderLine);
		Assert.assertEquals(1, context.getFailureCount());
	}

	public static ReturnOrderLine createReturnOrderLineDto()
	{

		final ReturnOrderLine returnOrderLine = new ReturnOrderLine();
		final OrderLine orderLine = new OrderLine();
		returnOrderLine.setOrderLine(orderLine);
		returnOrderLine.setReturnOrderLineStatus("STATUS");
		returnOrderLine.getOrderLine().setSkuId("ID");
		returnOrderLine.setQuantity(new Quantity("USD", 5));
		returnOrderLine.setReturnOrderLineId("ID");

		return returnOrderLine;
	}


}
