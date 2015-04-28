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

import java.math.BigDecimal;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.cis.api.payment.model.CisPaymentRequest;
import com.hybris.cis.api.payment.model.CisPaymentTransactionResult;
import com.hybris.cis.client.rest.payment.PaymentClient;
import com.hybris.cis.client.rest.payment.mock.PaymentMockClientImpl;
import com.hybris.commons.client.RestResponse;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.service.cis.CisConverter;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AmountVT;


public class PaymentService
{
	private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

	private PaymentClient paymentClient;

	private CisConverter cisConverter;

	/**
	 * Capture Payment for a Shipment (based on a previous authorization). Sets the amount captured in the shipment.
	 * 
	 * @param shipment
	 * @throws RemoteRequestException
	 */
	public void capturePayment(final ShipmentData shipment)
	{

		final CisPaymentRequest cisPaymentRequest = this.cisConverter.convertOmsShipmentToCisPaymentRequest(shipment);

		final URI cisURI = UriBuilder.fromPath(shipment.getAuthUrls().get(0)).build();

		LOG.trace("Using CIS Uri properties template: {}", cisURI.toString());

		RestResponse<CisPaymentTransactionResult> response = null;

		// If amount equals 0 then this item is a free promotion item and therefore no need to capture payment
		if (cisPaymentRequest.getAmount().compareTo(BigDecimal.ZERO) > 0)
		{
			try
			{
				response = this.paymentClient.capture(Long.toString(shipment.getShipmentId()), cisURI, cisPaymentRequest);
			}
			catch (final RuntimeException e)
			{
				throw new RemoteRequestException("An error occurred during communication with CIS :" + e.getMessage(), e);
			}
		}

		if (response != null)
		{
			setAmountCapturedInTheShipment(shipment, response);
			setCaptureId(shipment, response);
		}
	}

	protected void setCaptureId(final ShipmentData shipment, final RestResponse<CisPaymentTransactionResult> result)
	{
		String captureId = result.getResult().getId();
		shipment.getOrderFk().getPaymentInfos().get(0).setCaptureId(captureId);
	}

	protected void setAmountCapturedInTheShipment(final ShipmentData shipment,
			final RestResponse<CisPaymentTransactionResult> result)
	{
		final AmountVT amount = this.cisConverter.convertCisPaymentTransactionResultToOmsAmount(result.getResult());
		shipment.setAmountCapturedCurrencyCode(amount.getCurrencyCode());
		shipment.setAmountCapturedValue(amount.getValue());
	}

	/**
	 * Refund Payment for a Return. Sets the amount captured in the Return.ReturnPaymentInfo.
	 * 
	 * @param aReturn
	 * @throws RemoteRequestException
	 */
	public void refundPayment(final ReturnData aReturn)
	{
		final RestResponse<CisPaymentTransactionResult> result = convertAndRefundPayment(aReturn);

		if (result != null)
		{
			setAmountRefundedInTheReturnPaymentInfo(aReturn, result);
		}
	}

	protected RestResponse<CisPaymentTransactionResult> convertAndRefundPayment(final ReturnData aReturn)
	{
		final CisPaymentRequest cisPaymentRequest = this.cisConverter.convertOmsReturnToCisPaymentRequest(aReturn);

		final URI cisURI = UriBuilder.fromPath(buildRefundAuthUrl(aReturn)).build();

		LOG.trace("Using CIS Uri properties template: {}", cisURI.toString());

		RestResponse<CisPaymentTransactionResult> result = null;

		// If amount equals 0 then there is no need to call CIS to refund it
		if (cisPaymentRequest.getAmount().compareTo(BigDecimal.ZERO) > 0)
		{
			try
			{
				result = this.paymentClient.refund(String.valueOf(aReturn.getReturnId()), cisURI, cisPaymentRequest);
			}
			catch (final RuntimeException e)
			{
				throw new RemoteRequestException("An error occurred during communication with CIS :" + e.getMessage(), e);
			}
		}
		else
		{
			LOG.trace("no Refund because cisPaymentRequest has an amount of Zero");
		}

		return result;
	}

	protected String buildRefundAuthUrl(final ReturnData aReturn)
	{
		String refundUrl;
		if (getPaymentInfoFromReturn(aReturn).getCaptureId() != null)
		{
			refundUrl = getPaymentInfoFromReturn(aReturn).getAuthUrl() + "captures/"
					+ getPaymentInfoFromReturn(aReturn).getCaptureId();
		}
		else
			refundUrl = "";
		return refundUrl;
	}

	protected PaymentInfoData getPaymentInfoFromReturn(final ReturnData aReturn)
	{
		return aReturn.getOrder().getPaymentInfos().get(0);
	}

	protected void setAmountRefundedInTheReturnPaymentInfo(ReturnData aReturn, RestResponse<CisPaymentTransactionResult> result)
	{
		final AmountVT amount = new AmountVT(aReturn.getCalculatedRefundAmount().getCurrencyCode(), result.getResult().getAmount()
				.doubleValue());
		aReturn.getReturnPaymentInfos().setReturnPaymentAmount(amount);
	}


	protected CisConverter getCisConverter()
	{
		return cisConverter;
	}

	protected PaymentClient getPaymentClient()
	{
		return paymentClient;
	}

	@Required
	public void setCisConverter(final CisConverter cisConverter)
	{
		this.cisConverter = cisConverter;
	}

	@Required
	public void setPaymentClient(final PaymentClient paymentClient)
	{
		this.paymentClient = paymentClient;
	}


	public boolean useClientMock()
	{
		boolean b = false;

		try
		{
			b = (this.paymentClient instanceof PaymentMockClientImpl);

		}
		catch (Exception e)
		{
			LOG.warn("exception while checking if PaymentService use a clientMock. Return false.", e);
		}
		return b;
	}
}
