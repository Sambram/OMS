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

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;
import com.hybris.cis.api.geolocation.model.CisLocationRequest;
import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisOrder;
import com.hybris.cis.api.payment.model.CisPaymentRequest;
import com.hybris.cis.api.payment.model.CisPaymentTransactionResult;
import com.hybris.cis.api.shipping.model.CisShipment;
import com.hybris.oms.service.cis.CisConverter;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.AmountVT;


/**
 * Default implementation of {@link CisConverter}
 */
public class DefaultCisConverter implements CisConverter
{
	private CisAddressConverter cisAddressConverter;

	private CisPaymentConverter cisPaymentConverter;

	private CisShipmentConverter cisShipmentConverter;

	private CisReturnConverter cisReturnConverter;


	public CisAddress convertOmsAddressToCisAddress(final AddressVT address)
	{
		return cisAddressConverter.convertOmsAddressToCisAddress(address);
	}

	@Override
	public Double[] convertCisAddressToOmsLocationCoordinates(final CisAddress cisAddress)
	{
		return this.cisAddressConverter.convertCisAddressToOmsLocationCoordinate(cisAddress);
	}

	@Override
	public AmountVT convertCisPaymentRequestToOmsAmount(final CisPaymentRequest cisPaymentRequest)
	{
		return this.cisPaymentConverter.convertCisPaymentRequestToOmsAmount(cisPaymentRequest);
	}

	@Override
	public AmountVT convertCisPaymentTransactionResultToOmsAmount(final CisPaymentTransactionResult result)
	{
		return this.cisPaymentConverter.convertCisPaymentTransactionResultToOmsAmount(result);
	}

	@Override
	public CisPaymentRequest convertOmsReturnToCisPaymentRequest(ReturnData aReturn)
	{
		AmountVT amount = aReturn.getCustomRefundAmount();
		final CisPaymentRequest cisPaymentRequest = new CisPaymentRequest();
		cisPaymentRequest.setAmount(BigDecimal.valueOf(amount.getValue()));
		cisPaymentRequest.setCurrency(amount.getCurrencyCode());
		return cisPaymentRequest;
	}

	@Override
	public CisOrder convertOmsReturnToCisOrder(final ReturnData aReturn)
	{
		return this.cisReturnConverter.convertOmsReturnToCisOrder(aReturn);
	}

	@Override
	public CisLocationRequest convertOmsAddressToCisLocationRequest(final AddressVT address)
	{
		final CisAddress cisAddress = convertOmsAddressToCisAddress(address);
		final CisLocationRequest cislocation = new CisLocationRequest();
		cislocation.setAddresses(Lists.newArrayList(cisAddress));
		return cislocation;
	}

	@Override
	public CisPaymentRequest convertOmsAmountToCisPaymentRequest(final AmountVT amount)
	{
		return this.cisPaymentConverter.convertOmsAmountToCisPaymentRequest(amount);
	}

	@Override
	public CisOrder convertOmsShipmentToCisOrder(final ShipmentData shipment)
	{
		return this.cisShipmentConverter.convertOmsShipmentToCisOrder(shipment);
	}

	@Override
	public CisPaymentRequest convertOmsShipmentToCisPaymentRequest(final ShipmentData shipment)
	{
		return this.cisShipmentConverter.convertOmsShipmentToCisPaymentRequest(shipment);
	}

	@Override
	public CisShipment convertOmsShipmentToCisShipment(final OrderData order, final ShipmentData shipment)
	{
		return this.cisShipmentConverter.convertOmsShipmentToCisShipment(order, shipment);
	}

	@Required
	public void setCisAddressConverter(final CisAddressConverter cisAddressConverter)
	{
		this.cisAddressConverter = cisAddressConverter;
	}

	@Required
	public void setCisPaymentConverter(final CisPaymentConverter cisPaymentConverter)
	{
		this.cisPaymentConverter = cisPaymentConverter;
	}

	@Required
	public void setCisShipmentConverter(final CisShipmentConverter cisShipmentConverter)
	{
		this.cisShipmentConverter = cisShipmentConverter;
	}

	@Required
	public void setCisReturnConverter(CisReturnConverter cisReturnConverter)
	{
		this.cisReturnConverter = cisReturnConverter;
	}

	protected CisAddressConverter getCisAddressConverter()
	{
		return cisAddressConverter;
	}

	protected CisPaymentConverter getCisPaymentConverter()
	{
		return cisPaymentConverter;
	}

	protected CisShipmentConverter getCisShipmentConverter()
	{
		return cisShipmentConverter;
	}

	protected CisReturnConverter getCisReturnConverter()
	{
		return cisReturnConverter;
	}
}
