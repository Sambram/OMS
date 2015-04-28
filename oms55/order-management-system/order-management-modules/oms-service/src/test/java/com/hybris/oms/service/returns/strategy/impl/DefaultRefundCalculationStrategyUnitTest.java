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
package com.hybris.oms.service.returns.strategy.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.returns.ReturnService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;



@RunWith(MockitoJUnitRunner.class)
public class DefaultRefundCalculationStrategyUnitTest
{
	public static final int REJECTED_LINE_QUANTITY = 5;
	public static final String CAD = "CAD";

	@InjectMocks
	@Spy
	private DefaultRefundCalculationStrategy mockDefaultRefundCalculationStrategy;
	@Mock
	private ReturnService defaultReturnService;
	@Mock
	private TenantPreferenceData tpData;
	@Mock
	private ReturnOrderLineData returnOrderLineData;
	@Mock
	private OrderLineData orderLineData;
	@Mock
	private ReturnLineRejectionData returnLineRejectionData;
	@Mock
	private ReturnData aReturn;

	private static final String TRUE = "TRUE";
	private static final String FALSE = "FALSE";

	@Test
	public void testCalculateLineAmountNoRejections()
	{
		mockRequiredData();
		assertEquals(Double.valueOf(10 * (10 + 1.5)),
				mockDefaultRefundCalculationStrategy.calculateLineAmount(returnOrderLineData, 0, orderLineData).getValue());
	}

	@Test
	public void testCalculateLineAmountHalfRejected()
	{
		mockRequiredData();
		assertEquals(Double.valueOf((10 - 5) * (10 + 1.5)),
				mockDefaultRefundCalculationStrategy.calculateLineAmount(returnOrderLineData, 5, orderLineData).getValue());
	}

	protected void mockRequiredData()
	{
		when(returnOrderLineData.getReturnLineRejections()).thenReturn(ImmutableList.of(returnLineRejectionData));
		when(returnOrderLineData.getQuantity()).thenReturn(new QuantityVT(CAD, 10));
		when(orderLineData.getUnitTaxValue()).thenReturn(1.5);
		when(orderLineData.getUnitPriceCurrencyCode()).thenReturn(CAD);
		when(orderLineData.getUnitPriceValue()).thenReturn(10.0);
	}

	@Test
	public void testShippingIncludedAndNotRefundedYet()
	{
		when(tpData.getValue()).thenReturn(FALSE);
		doReturn(Boolean.TRUE).when(defaultReturnService).shippingPreviouslyRefunded(any(ReturnData.class), any(OrderData.class));
		when(aReturn.isShippingRefunded()).thenReturn(Boolean.FALSE);
		assertFalse(mockDefaultRefundCalculationStrategy.shippingIncludedAndNotRefundedYet(aReturn, tpData));

		when(tpData.getValue()).thenReturn(TRUE);
		assertFalse(mockDefaultRefundCalculationStrategy.shippingIncludedAndNotRefundedYet(aReturn, tpData));

		doReturn(Boolean.FALSE).when(defaultReturnService).shippingPreviouslyRefunded(any(ReturnData.class), any(OrderData.class));
		assertFalse(mockDefaultRefundCalculationStrategy.shippingIncludedAndNotRefundedYet(aReturn, tpData));

		when(aReturn.isShippingRefunded()).thenReturn(Boolean.TRUE);
		assertTrue(mockDefaultRefundCalculationStrategy.shippingIncludedAndNotRefundedYet(aReturn, tpData));
	}

}
