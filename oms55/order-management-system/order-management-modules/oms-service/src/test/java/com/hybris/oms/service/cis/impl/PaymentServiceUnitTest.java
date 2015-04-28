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
package com.hybris.oms.service.cis.impl;

import com.hybris.cis.api.payment.model.CisPaymentRequest;
import com.hybris.cis.api.payment.model.CisPaymentTransactionResult;
import com.hybris.cis.client.rest.payment.PaymentClient;
import com.hybris.commons.client.RestResponse;
import com.hybris.oms.service.cis.CisConverter;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;


public class PaymentServiceUnitTest
{
	@Mock
	private PaymentClient paymentClient;

	@Mock
	private CisConverter cisConverter;

	@Mock
	private ShipmentData aShipmentData;

	@Mock
	private ReturnData aReturnData;

	@Mock
	private RestResponse<CisPaymentTransactionResult> paymentResponse;

	@InjectMocks
	@Spy
	private PaymentService paymentService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		Mockito.doReturn("http://fakeUrl").when(paymentService).buildRefundAuthUrl(this.aReturnData);

		Mockito.doReturn(Arrays.asList("http://fake.url")).when(aShipmentData).getAuthUrls();
		Mockito.doNothing().when(paymentService).setAmountCapturedInTheShipment(aShipmentData, paymentResponse);

	}


	private CisPaymentRequest getCisPaymentRequest(BigDecimal amount)
	{
		final CisPaymentRequest cisPaymentRequest = new CisPaymentRequest();
		cisPaymentRequest.setAmount(amount);
		cisPaymentRequest.setCurrency("USD");
		return cisPaymentRequest;
	}

	@Test
	public void skipCapturePaymentForFreeItems()
	{

		// Given:
		final CisPaymentRequest freePaymentRequest = getCisPaymentRequest(BigDecimal.ZERO);
		Mockito.doReturn(freePaymentRequest).when(this.cisConverter).convertOmsShipmentToCisPaymentRequest(aShipmentData);

		// When:
		this.paymentService.capturePayment(aShipmentData);

		// Then:
		Mockito.verify(this.paymentClient, Mockito.times(0)).capture(anyString(),any(URI.class), any(CisPaymentRequest.class));
	}

	@Test
	public void capturePaymentForNonFreeItems()
	{

		// Given:
		final CisPaymentRequest freePaymentRequest = getCisPaymentRequest(BigDecimal.ONE);
		Mockito.doReturn(freePaymentRequest).when(this.cisConverter).convertOmsShipmentToCisPaymentRequest(aShipmentData);

		// When:
		this.paymentService.capturePayment(aShipmentData);

		// Then:
		Mockito.verify(this.paymentClient, Mockito.times(1)).capture(anyString(),any(URI.class), any(CisPaymentRequest.class));
	}


	@Test
	public void SkipRefundPaymentTest()
	{
		// Given:
		final CisPaymentRequest cisPaymentRequest = getCisPaymentRequest(BigDecimal.ZERO);

		Mockito.when(this.cisConverter.convertOmsReturnToCisPaymentRequest(aReturnData)).thenReturn(cisPaymentRequest);

		// When:
		this.paymentService.refundPayment(aReturnData);

		// Then:
		Mockito.verify(this.paymentClient, Mockito.times(0)).refund(anyString(), any(URI.class), any(CisPaymentRequest.class));
	}

	@Test
	public void refundPaymentTest()
	{
		// Given:
		final CisPaymentRequest cisPaymentRequest = getCisPaymentRequest(BigDecimal.ONE);

		Mockito.when(this.cisConverter.convertOmsReturnToCisPaymentRequest(aReturnData)).thenReturn(cisPaymentRequest);

		// When:
		this.paymentService.refundPayment(aReturnData);

		// Then:
		Mockito.verify(this.paymentClient, Mockito.times(1)).refund(anyString(), any(URI.class), any(CisPaymentRequest.class));
	}


}
