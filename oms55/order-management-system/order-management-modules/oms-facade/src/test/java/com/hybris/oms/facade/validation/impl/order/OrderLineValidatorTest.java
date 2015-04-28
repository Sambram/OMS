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

import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableSet;


@RunWith(MockitoJUnitRunner.class)
public class OrderLineValidatorTest
{
	private ValidationContext context;
	private OrderLine orderLine;
	@InjectMocks
	private OrderLineValidator orderLineValidator;
	@Mock
	private Validator<Amount> amountWithPositiveValueValidator;
	@Mock
	private Validator<Quantity> quantityWithPositiveValueValidator;

	@Before
	public void setUp()
	{
		context = new ValidationContext();
		buildOrderLine();
	}

	@Test
	public void shouldFailWhenLocationRoleisNull()
	{
		// Given
		this.orderLine.setLocationRoles(null);

		// When
		this.orderLineValidator.validate("OrderLine", context, this.orderLine);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals("OrderLine.locationRoles", this.context.getFailures().get(0).getFieldName());
		Assert.assertEquals(FieldValidationType.IS_BLANK, this.context.getFailures().get(0).getFailureType());

	}



	private void buildOrderLine()
	{
		this.orderLine = new OrderLine();
		this.orderLine.setOrderLineId("Order Line Id 1234");
		this.orderLine.setSkuId("Sku ABC");
		this.orderLine.setQuantity(new Quantity("unit", 2));
		this.orderLine.setQuantityUnassigned(new Quantity("unit", 2));
		this.orderLine.setUnitPrice(new Amount("USD", 1d));
		this.orderLine.setUnitTax(new Amount("USD", 0.15d));
		this.orderLine.setTaxCategory("AE514");
		this.orderLine.setLocationRoles(ImmutableSet.of(LocationRole.PICKUP));
	}

}
