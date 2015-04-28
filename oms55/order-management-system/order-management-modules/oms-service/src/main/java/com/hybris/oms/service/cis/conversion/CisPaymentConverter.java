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
package com.hybris.oms.service.cis.conversion;

import com.hybris.cis.api.payment.model.CisPaymentRequest;
import com.hybris.cis.api.payment.model.CisPaymentTransactionResult;
import com.hybris.oms.service.managedobjects.types.AmountVT;

import java.math.BigDecimal;


public class CisPaymentConverter
{
	public AmountVT convertCisPaymentRequestToOmsAmount(final CisPaymentRequest payment)
	{

		if (payment == null)
		{
			throw new IllegalArgumentException("Missing required field payment");
		}

		return new AmountVT(payment.getCurrency(), payment.getAmount().doubleValue());
	}

	public AmountVT convertCisPaymentTransactionResultToOmsAmount(final CisPaymentTransactionResult result)
	{
		return new AmountVT(result.getRequest().getCurrency(), result.getAmount().doubleValue());
	}

	public CisPaymentRequest convertOmsAmountToCisPaymentRequest(final AmountVT amount)
	{
		if (amount == null)
		{
			throw new IllegalArgumentException("Missing required field amount");
		}

		final CisPaymentRequest cisPaymentRequest = new CisPaymentRequest();
		cisPaymentRequest.setAmount(BigDecimal.valueOf(amount.getValue()));
		cisPaymentRequest.setCurrency(amount.getCurrencyCode());
		return cisPaymentRequest;
	}

}
