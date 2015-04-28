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

import com.hybris.cis.api.shipping.model.CisShipment;
import com.hybris.cis.client.rest.shipping.ShippingClient;
import com.hybris.commons.client.RestResponse;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.remote.exception.InvalidShipmentLabelResponseException;
import com.hybris.oms.service.cis.CisConverter;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


public class ShippingService
{
	private ShippingClient shippingClient;

	private CisConverter cisConverter;

	/**
	 * Calls cis services to create shipping label.
	 * 
	 * @param shipment
	 * @return the label id
	 * @throws RemoteRequestException
	 */
	public String createShipmentLabel(final ShipmentData shipment)
	{
		final OrderData order = shipment.getOrderFk();
		final CisShipment cisShipment = this.cisConverter.convertOmsShipmentToCisShipment(order, shipment);

		RestResponse<CisShipment> response = null;
		try
		{
			response = this.shippingClient.createShipment(Long.toString(shipment.getShipmentId()), cisShipment);
		}
		catch (final RuntimeException e)
		{
			throw new RemoteRequestException(CisService.CIS_COMMUNICATION_ERROR + e.getMessage(), e);
		}

		// Throw exception if no label Urls are returned or if returned label Url is equal to null
		if (response == null || response.getResult() == null || CollectionUtils.isEmpty(response.getResult().getLabels())
				|| response.getResult().getLabels().get(0).getHref() == null)
		{
			throw new InvalidShipmentLabelResponseException("CIS did not return any shipping labels");
		}

		return response.getResult().getLabels().get(0).getHref();
	}

	/**
	 * Calls cis services to get the byte array representation of a label.
	 * 
	 * @param shipment
	 * @return
	 * @throws RemoteRequestException
	 */
	public byte[] getShipmentLabel(final ShipmentData shipment)
	{
		final String labelHref = shipment.getDelivery().getLabelUrl();
		RestResponse<byte[]> response = null;

		final URI labelHrefURI = UriBuilder.fromPath(labelHref).build();
		try
		{
			response = this.shippingClient.getLabel(Long.toString(shipment.getShipmentId()), labelHrefURI);
		}
		catch (final RuntimeException e)
		{
			throw new RemoteRequestException("An error occurred during communication with CIS :" + e.getMessage(), e);
		}

		// Throw exception if no labels are returned or if returned label is equal to null
		if (response.getResult() == null)
		{
			throw new InvalidShipmentLabelResponseException("CIS did not return any shipping labels");
		}

		return response.getResult();
	}

	protected CisConverter getCisConverter()
	{
		return cisConverter;
	}

	protected ShippingClient getShippingClient()
	{
		return shippingClient;
	}

	@Required
	public void setCisConverter(final CisConverter cisConverter)
	{
		this.cisConverter = cisConverter;
	}

	@Required
	public void setShippingClient(final ShippingClient shippingClient)
	{
		this.shippingClient = shippingClient;
	}

}
