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

import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.remote.exception.InvalidGeolocationResponseException;
import com.hybris.oms.domain.remote.exception.InvalidShipmentLabelResponseException;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;


public class DefaultCisService implements CisService
{
	private GeolocationService geoLocationService;

	private PaymentService paymentService;

	private ShippingService shippingService;

	private TaxService taxService;

	@Override
	public AddressVT geocodeAddress(final AddressVT address) throws RemoteRequestException, InvalidGeolocationResponseException
	{
		return this.geoLocationService.geocodeAddress(address);
	}

	@Override
	public void capturePayment(final ShipmentData shipment) throws RemoteRequestException
	{
		this.paymentService.capturePayment(shipment);
	}

	@Override
	public String createShipmentLabel(final ShipmentData shipment) throws RemoteRequestException,
			InvalidShipmentLabelResponseException
	{
		return this.shippingService.createShipmentLabel(shipment);
	}

	@Override
	public byte[] getShipmentLabel(final ShipmentData shipment) throws RemoteRequestException,
			InvalidShipmentLabelResponseException
	{
		return this.shippingService.getShipmentLabel(shipment);
	}

	@Override
	public void refundPayment(ReturnData aReturn) throws RemoteRequestException
	{
		this.paymentService.refundPayment(aReturn);
	}

	@Override
	public void reverseTax(ReturnData aReturn) throws RemoteRequestException
	{
		this.taxService.revertTax(aReturn);
	}

	@Override
	public void invoiceTaxes(final ShipmentData shipment) throws RemoteRequestException
	{
		this.taxService.invoiceTaxes(shipment);
	}

	protected GeolocationService getGeoLocationService()
	{
		return geoLocationService;
	}

	public PaymentService getPaymentService()
	{
		return paymentService;
	}

	protected ShippingService getShippingService()
	{
		return shippingService;
	}

	public TaxService getTaxService()
	{
		return taxService;
	}

	@Required
	public void setGeoLocationService(final GeolocationService geoLocationService)
	{
		this.geoLocationService = geoLocationService;
	}

	@Required
	public void setPaymentService(final PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	@Required
	public void setShippingService(final ShippingService shippingService)
	{
		this.shippingService = shippingService;
	}

	@Required
	public void setTaxService(final TaxService taxService)
	{
		this.taxService = taxService;
	}

}
