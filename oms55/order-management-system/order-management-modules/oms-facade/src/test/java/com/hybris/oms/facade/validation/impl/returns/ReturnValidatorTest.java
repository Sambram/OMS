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

import static com.hybris.oms.facade.conversion.impl.returns.ReturnTestUtils.RETURNREASON_LATEDELIVERY;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.returns.impl.DefaultReturnService;
import com.hybris.oms.service.shipment.ShipmentService;


@RunWith(MockitoJUnitRunner.class)
public class ReturnValidatorTest
{
	public static final String RETURN = "Return";
	public static final String ID = "123";
	public static final String CURRENCY_CODE = "USD";
	public static final String STATUS = "status";
	public static final String UNIT = "UNIT";


	@Spy
	private ReturnValidator spyReturnValidator;

	@Spy
	private DefaultReturnService returnService;

	@Mock
	private OrderService mockedOrderService;

	@Mock
	private ShipmentService mockedShipmentService;

	@Mock
	private Validator<ReturnOrderLine> returnOrderLineValidator;

	@Mock
	private Validator<ReturnPaymentInfo> returnPaymentInfoValidator;

	private ValidationContext validationCtxt;

	private Return aReturnDto;

	@Before
	public void setUp()
	{
		this.validationCtxt = new ValidationContext();
		this.aReturnDto = createReturnDto();
		spyReturnValidator.setReturnService(returnService);

		when(returnService.getReturnReasonCodes()).thenReturn(
				Arrays.asList("OTHER", "DAMAGED", "WRONGSIZE", "CHANGMIND", "LATEDELIVERY"));

		doNothing().when(spyReturnValidator).validateReturnItems(this.aReturnDto, validationCtxt);
		doNothing().when(spyReturnValidator).validateShipmentExistence(this.aReturnDto, validationCtxt);
		doNothing().when(spyReturnValidator).validateRefundableShippingCost(this.aReturnDto, validationCtxt);
		spyReturnValidator.setReturnOrderLineValidator(this.returnOrderLineValidator);
		spyReturnValidator.setReturnPaymentInfoValidator(this.returnPaymentInfoValidator);
	}

	@Test
	public void shouldValidate()
	{
		// when
		spyReturnValidator.validate(RETURN, validationCtxt, this.aReturnDto);

		// then
		assertFalse(this.validationCtxt.containsFailures());

		verify(returnOrderLineValidator, atLeast(1)).validate(anyString(), (ValidationContext) anyObject(),
				(ReturnOrderLine) anyObject(), (FailureHandler) anyObject());
	}

	@Test
	public void shouldFailValidationForUnsupportedReturnReason()
	{
		this.aReturnDto.setReturnReasonCode("BLABLABLA");
		spyReturnValidator.validate(RETURN, validationCtxt, this.aReturnDto);
		Assert.assertEquals(1, validationCtxt.getFailureCount());
	}

	@Test
	public void shouldFailValidationBlanks()
	{
		this.aReturnDto.setOrderId("");
		spyReturnValidator.validate(RETURN, validationCtxt, this.aReturnDto);
		Assert.assertEquals(1, validationCtxt.getFailureCount());
	}

	protected static Return createReturnDto()
	{
		final Return aReturn = new Return();
		aReturn.setOrderId(ID);
		aReturn.setReturnId(ID);
		aReturn.setCalculatedTotalRefundAmount(new Amount(CURRENCY_CODE, 5.0));
		aReturn.setReturnReasonCode(RETURNREASON_LATEDELIVERY);
		aReturn.setReturnOrderLines(ImmutableList.of(createReturnOrderLineDto()));
		aReturn.setReturnPaymentInfos(new ReturnPaymentInfo());

		return aReturn;
	}

	protected static ReturnOrderLine createReturnOrderLineDto()
	{
		final ReturnOrderLine returnOrderLine = new ReturnOrderLine();
		returnOrderLine.setReturnOrderLineStatus(STATUS);

		final OrderLine orderLine = new OrderLine();
		orderLine.setSkuId(ID);
		returnOrderLine.setOrderLine(orderLine);
		returnOrderLine.setQuantity(new Quantity(UNIT, 5));
		returnOrderLine.setReturnOrderLineId(ID);

		return returnOrderLine;
	}
}
