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
package com.hybris.oms.ui.rest.client.shipment;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;
import com.hybris.oms.ui.api.shipment.OrderShipmentDetail;
import com.hybris.oms.ui.api.shipment.OrderShipmentList;
import com.hybris.oms.ui.api.shipment.PickSlipBinInfo;
import com.hybris.oms.ui.api.shipment.ShipmentDetail;
import com.hybris.oms.ui.api.shipment.UIShipment;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.sun.jersey.api.client.GenericType;


/**
 * The Class UiShipmentRestClient.
 */
public class UiShipmentRestClient extends DefaultRestClient implements UiShipmentFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(UiShipmentRestClient.class);

	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Override
	public List<OrderShipmentDetail> findOrderShipmentDetailsByOrderId(final String orderId, final boolean allLocationDisplay)
			throws EntityNotFoundException, RemoteRequestException
	{
		try
		{
			return getClient().call("/uiordershipments/order/%s", orderId)
					.queryParam("allLocationDisplay", Boolean.toString(allLocationDisplay)).get(OrderShipmentList.class).result()
					.getList();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public List<OrderShipmentDetail> findOrderShipmentDetailsByShipmentId(final String shipmentId, final boolean allLocationDisplay)
	{
		try
		{
			return getClient().call("/uiordershipments/shipment/%s", shipmentId)
					.queryParam("allLocationDisplay", Boolean.toString(allLocationDisplay)).get(OrderShipmentList.class).result()
					.getList();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public Pageable<UIShipment> findUIShipmentsByQuery(final ShipmentQueryObject shipmentQueryObject)
	{
		try
		{
			final RestCallBuilder call = getClient().call("uishipments");
			this.queryObjectRestCallPopulator.populate(shipmentQueryObject, call);
			final RestResponse<List<UIShipment>> response = call.get(new GenericType<List<UIShipment>>()
			{ /* NOPMD */
			});

			final List<UIShipment> shipments = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<UIShipment>(shipments, pageInfo);
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public UIShipment getUIShipmentById(final String shipmentId) throws EntityNotFoundException
	{
		try
		{
			return getClient().call("uishipments/%s", shipmentId).get(UIShipment.class).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public Pageable<ShipmentDetail> findShipmentDetailsByQuery(final ShipmentQueryObject shipmentQueryObject)
			throws EntityNotFoundException, RemoteRequestException
	{
		try
		{
			final RestCallBuilder call = getClient().call("uishipmentdetails");
			this.queryObjectRestCallPopulator.populate(shipmentQueryObject, call);
			final RestResponse<List<ShipmentDetail>> response = call.get(new GenericType<List<ShipmentDetail>>()
			{ /* NOPMD */});

			final List<ShipmentDetail> shipmentDetails = response.result();
			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);
			return new PagedResults<ShipmentDetail>(shipmentDetails, pageInfo);
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public ShipmentDetail getShipmentDetailById(final String shipmentId) throws EntityNotFoundException
	{
		try
		{
			final RestCallBuilder call = getClient().call("uishipmentdetails/%s", shipmentId);
			final RestResponse<ShipmentDetail> response = call.get(ShipmentDetail.class);
			return response.result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public void removeAllOrderLineQuantitiesFromShipment(final String shipmentId)
	{

		LOGGER.debug("removeAllOLQsFromShipment: shipmentId={}", shipmentId);
		try
		{
			getClient().call("shipments/%s/allOLQ", shipmentId).delete();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void removeOrderLineQuantityFromShipment(final String shipmentId, final String olqId)
	{

		LOGGER.debug("updateShipmentStatus: shipmentId={} olqId={}", shipmentId, olqId);
		try
		{
			getClient().call("shipments/%s/olq/%s", shipmentId, olqId).delete();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public PickSlipBinInfo getBinInfoForPickSlipByShipmentId(final String shipmentId)
	{

		LOGGER.debug("getBinInfoForPickSlipByShipmentId: shipmentId={} ", shipmentId);
		try
		{
			return getClient().call("shipments/%s/uiPickShipmentWithBins", shipmentId).get(PickSlipBinInfo.class).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}

}
