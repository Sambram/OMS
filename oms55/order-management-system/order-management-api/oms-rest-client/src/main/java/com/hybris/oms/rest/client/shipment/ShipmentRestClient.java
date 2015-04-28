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
package com.hybris.oms.rest.client.shipment;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.commons.client.RestCallBuilder;
import com.hybris.commons.client.RestResponse;
import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.BatchResult;
import com.hybris.oms.domain.JaxbBaseMap;
import com.hybris.oms.domain.JaxbBaseSet;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentDetails;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShipmentSplitResult;
import com.hybris.oms.rest.client.util.RestCallPopulator;
import com.hybris.oms.rest.client.util.RestUtil;
import com.hybris.oms.rest.client.web.DefaultRestClient;
import com.sun.jersey.api.client.GenericType;


/**
 * The Class ShipmentRestClient.
 */
@SuppressWarnings("PMD.TooManyPublicMethods")
public class ShipmentRestClient extends DefaultRestClient implements ShipmentFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentRestClient.class);
	private static final String OLQ_ID = "olqId";
	private static final GenericType<Shipment> SHIPMENT_TYPE = new GenericType<Shipment>()
	{ // nothing to declare
	};
	private static final GenericType<ShipmentSplitResult> SHIPMENT_SPLIT_TYPE = new GenericType<ShipmentSplitResult>()
	{ // nothing to declare
	};
	private static final GenericType<Collection<Shipment>> SHIPMENTS = new GenericType<Collection<Shipment>>()
	{};

	private RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator;

	@Override
	public Shipment confirmShipment(final String shipmentId) throws EntityNotFoundException
	{
		LOGGER.debug("confirmShipment: shipmentId={}", shipmentId);
		try
		{
			return getClient().call("shipments/%s/confirm", shipmentId).post(SHIPMENT_TYPE).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Pageable<Shipment> findShipmentsByQuery(final ShipmentQueryObject shipmentQueryObject)
	{
		try
		{
			final RestCallBuilder call = getClient().call("shipments");

			this.queryObjectRestCallPopulator.populate(shipmentQueryObject, call);

			final RestResponse<List<Shipment>> response = call.get(new GenericType<List<Shipment>>()
			{ /* NOPMD */});

			final List<Shipment> shipments = response.result();

			final PageInfo pageInfo = RestUtil.getHeaderPageInfo(response);

			return new PagedResults<Shipment>(shipments, pageInfo);
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Shipment getShipmentById(final String shipmentId)
	{
		try
		{
			return getClient().call("shipments/%s", shipmentId).get(Shipment.class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Collection<Shipment> getShipmentsByOrderId(final String orderId)
	{
		LOGGER.debug("getShipmentsByOrderId: orderId={}", orderId);
		try
		{
			return getClient().call("orders/%s/shipments", orderId).get(SHIPMENTS).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Shipment packShipment(final String shipmentId) throws EntityNotFoundException
	{
		LOGGER.debug("packShipment: shipmentId={}", shipmentId);
		try
		{
			return getClient().call("shipments/%s/pack", shipmentId).post(SHIPMENT_TYPE).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Shipment pickShipment(final String shipmentId) throws EntityNotFoundException
	{
		LOGGER.debug("pickShipment: shipmentId={}", shipmentId);
		try
		{
			return getClient().call("shipments/%s/pick", shipmentId).post(SHIPMENT_TYPE).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public byte[] retrieveShippingLabelsByShipmentId(final String shipmentId)
	{
		try
		{
			return getClient().call("shipments/%s/label", shipmentId).get(byte[].class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void updateShipmentDetails(final String shipmentId, final ShipmentDetails shipmentDetails)
	{
		LOGGER.debug("updateShipmentDetails: shipmentId={} shipmentDetails={}", shipmentId, shipmentDetails);
		try
		{
			getClient().call("shipments/%s/details", shipmentId).put(ShipmentDetails.class, shipmentDetails).result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Shipment updateShipmentStatus(final String shipmentId, final String olqStatus)
	{
		LOGGER.debug("updateShipmentStatus: shipmentId={} olqStatus={}", shipmentId, olqStatus);
		try
		{
			return getClient().call("shipments/%s/status/%s", shipmentId, olqStatus).put(Shipment.class).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Shipment cancelShipment(final String shipmentId) throws EntityNotFoundException
	{
		LOGGER.debug("cancelShipment: shipmentId={}", shipmentId);
		try
		{
			return getClient().call("shipments/%s/cancel", shipmentId).post(SHIPMENT_TYPE).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void declineShipment(final String shipmentId) throws EntityNotFoundException, InvalidOperationException
	{
		LOGGER.debug("Decline Shipment: shipmentId={}", shipmentId);
		try
		{
			getClient().call("shipments/%s/decline", shipmentId).post().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Shipment reallocateShipment(final String shipmentId, final String locationId) throws EntityNotFoundException,
			InvalidOperationException
	{
		LOGGER.debug("Re-Allocate Shipment: shipmentId={}, locationId={}", shipmentId, locationId);
		try
		{
			return getClient().call("shipments/%s/reallocate/%s", shipmentId, locationId).post(SHIPMENT_TYPE).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public ShipmentSplitResult splitShipmentByOlqs(final String shipmentId, final Set<String> olqIds)
			throws EntityNotFoundException, EntityValidationException, InvalidOperationException
	{
		LOGGER.debug("Split Shipment: shipmentId={}, olqIds={}", shipmentId, olqIds);
		try
		{
			final RestCallBuilder builder = getClient().call("shipments/%s/olqs/split", shipmentId);
			addCollectionParam(OLQ_ID, olqIds, builder);
			return builder.post(SHIPMENT_SPLIT_TYPE, new JaxbBaseSet<String>(olqIds)).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public ShipmentSplitResult splitShipmentByOlqQuantities(final String shipmentId, final Map<String, Integer> olqIdQuantityValue)
	{
		LOGGER.debug("Split shipments by quantities of olqs for shipmentId={} ", shipmentId);
		try
		{
			final RestResponse<ShipmentSplitResult> response = this.getClient()
					.call("shipments/%s/olqs/splitquantities", shipmentId)
					.post(SHIPMENT_SPLIT_TYPE, new JaxbBaseMap<>(olqIdQuantityValue));
			return response.getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public BatchResult declineShipments(final Set<String> shipmentIds)
	{
		LOGGER.debug("Decline Shipments");
		try
		{
			final RestResponse<BatchResult> response = this.getClient().call("shipments/decline")
					.post(new GenericType<BatchResult>()
					{}, new JaxbBaseSet<>(shipmentIds));
			return response.getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public BatchResult cancelShipments(final Set<String> shipmentIds) throws EntityNotFoundException
	{
		LOGGER.debug("Cancel Shipments");
		try
		{
			final RestResponse<BatchResult> response = this.getClient().call("shipments/cancel").post(new GenericType<BatchResult>()
			{}, new JaxbBaseSet<>(shipmentIds));
			return response.getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public BatchResult confirmShipments(final Set<String> shipmentIds) throws EntityNotFoundException
	{
		LOGGER.debug("Confirm Shipments");
		try
		{
			final RestResponse<BatchResult> response = this.getClient().call("shipments/confirm")
					.post(new GenericType<BatchResult>()
					{}, new JaxbBaseSet<>(shipmentIds));
			return response.getResult();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
			return null;
		}
	}

	@Override
	public Shipment manualCapture(final String shipmentId) throws EntityNotFoundException, InvalidOperationException
	{
		LOGGER.debug("manualCapture shipment with Id {}", shipmentId);
		try
		{
			return getClient().call("shipments/%s/manual", shipmentId).put(Shipment.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Required
	public void setQueryObjectRestCallPopulator(final RestCallPopulator<QueryObject<?>> queryObjectRestCallPopulator)
	{
		this.queryObjectRestCallPopulator = queryObjectRestCallPopulator;
	}

}
