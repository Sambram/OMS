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
package com.hybris.oms.service.cis;

import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.remote.exception.InvalidGeolocationResponseException;
import com.hybris.oms.domain.remote.exception.InvalidShipmentLabelResponseException;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;


public interface CisService
{
	String CIS_COMMUNICATION_ERROR = "An error occurred during communication with CIS: ";

	/**
	 * Id of the line item used to represent the shipping taxes during Invoice Taxes.
	 */
	int INVOICE_TAX_SHIPPING_LINE_ITEM_ID = -1;

	/**
	 * Code of the line item used to represent the shipping taxes during Invoice Taxes.
	 */
	String INVOICE_TAX_SHIPPING_LINE_ITEM_CODE = "Shipping Code";

	/**
	 * Capture Payment for a Shipment (based on a previous authorization).
	 * Sets the amount captured in the shipment.
	 * 
	 * @param shipment
	 * @throws RemoteRequestException
	 */
	void capturePayment(final ShipmentData shipment) throws RemoteRequestException;

	/**
	 * Invoice taxes for a given shipment.
	 * Sets the taxes committed in the shipment.
	 * 
	 * @param shipment
	 * @throws RemoteRequestException
	 */
	void invoiceTaxes(final ShipmentData shipment) throws RemoteRequestException;

	/**
	 * Geocode the address. (Convert address to latitude/longitude).
	 * Sets the geo-codes into the address.
	 * 
	 * @param address
	 * @return new address with geocodes
	 * @throws RemoteRequestException
	 * @throws InvalidGeolocationResponseException
	 */
	AddressVT geocodeAddress(final AddressVT address) throws RemoteRequestException, InvalidGeolocationResponseException;

	/**
	 * Call cis services to create shipping label.
	 * 
	 * @param shipment
	 * @return the label id
	 * @throws RemoteRequestException
	 */
	String createShipmentLabel(final ShipmentData shipment) throws RemoteRequestException, InvalidShipmentLabelResponseException;

	/**
	 * Call cis services to get the byte array representation of a label.
	 * 
	 * @param shipment
	 * @return
	 * @throws RemoteRequestException
	 */
	byte[] getShipmentLabel(final ShipmentData shipment) throws RemoteRequestException, InvalidShipmentLabelResponseException;

	/**
	 * Refund Payment for a Return. Sets the amount captured in the Return.ReturnPaymentInfo.
	 * 
	 * @param aReturn
	 * @throws RemoteRequestException
	 */
	void refundPayment(final ReturnData aReturn) throws RemoteRequestException;


	/**
	 * Reveres Tax committed.
	 * 
	 * @param aReturn
	 * @throws RemoteRequestException
	 */
	void reverseTax(final ReturnData aReturn) throws RemoteRequestException;
}
