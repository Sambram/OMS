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
package com.hybris.oms.rest.web.shipment;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.BatchResult;
import com.hybris.oms.domain.JaxbBaseSet;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * * WebResource exposing {@link ShipmentFacade} http://localhost:8080/oms-rest-webapp/webresources/shipments.
 */
@Component
@Path("/shipments")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class ShipmentsResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentsResource.class);
	private static final String SHIPMENT_ID = "shipmentId";
	@Autowired
	private ShipmentFacade shipmentServices;
	@Autowired
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	/**
	 * Get a list of shipments regarding the query parameters informed.
	 * 
	 * @param uriInfo
	 *           URI info
	 * 
	 * @return a Response object containing the list of orders
	 *         {@link com.hybris.oms.domain.shipping.ShipmentQuerySupport} {@link com.hybris.oms.domain.SortDirection}
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response findShipmentsByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findShipmentByQuery");

		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<Shipment> pagedShipment = this.shipmentServices.findShipmentsByQuery(queryObject);

		final GenericEntity<List<Shipment>> entity = new GenericEntity<List<Shipment>>(pagedShipment.getResults())
		{// empty as always
		};

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedShipment.getNextPage(),
				pagedShipment.getPreviousPage(), pagedShipment.getTotalPages(), pagedShipment.getTotalRecords());
		return responseBuilder.entity(entity).build();
	}

	/**
	 * Request a {@link Shipment} by shipment id.
	 * 
	 * @param shipmentId
	 *           the shipment id to be fetched
	 * @return rest response with {@link Shipment} object embedded in it
	 */
	@Path("/{shipmentId}")
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response getShipmentByShipmentId(@PathParam(SHIPMENT_ID) final String shipmentId)
	{
		LOGGER.trace("getShipmentByShipmentId");
		final Shipment shipment = this.getShipmentById(shipmentId);
		return Response.ok().entity(shipment).build();
	}

	/**
	 * Update shipment details.
	 * 
	 * @param shipmentId
	 *           the shipment id that will be updated
	 * @param shipmentDetails
	 *           the {@link com.hybris.oms.domain.shipping.ShipmentDetails} object to update the shipment with
	 * @return the response with {@link com.hybris.oms.domain.shipping.ShipmentDetails} object embedded in it
	 */
	@Path("/{shipmentId}/details")
	@PUT
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public com.hybris.oms.domain.shipping.ShipmentDetails updateShipmentDetails(@PathParam(SHIPMENT_ID) final String shipmentId,
			final com.hybris.oms.domain.shipping.ShipmentDetails shipmentDetails)
	{
		LOGGER.trace("updateShipmentDetails");
		this.getShipmentServices().updateShipmentDetails(shipmentId, shipmentDetails);
		return shipmentDetails;
	}

	/**
	 * Decline shipments.
	 * 
	 * @param shipmentIds - the set of shipment ids of the {@link Shipment} to be declined.
	 * @return the response with the declined {@link Shipment} object embedded in it.
	 */
	@Path("/decline")
	@POST
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response declineShipments(final JaxbBaseSet<String> shipmentIds)
	{
		LOGGER.trace("Decline shipments");
		final BatchResult result = this.shipmentServices.declineShipments(shipmentIds.getSet());

		final GenericEntity<BatchResult> entity = new GenericEntity<BatchResult>(result)
		{// empty as always
		};


		return Response.ok().entity(entity).build();
	}

	/**
	 * Cancel shipments.
	 * 
	 * @param shipmentIds - the set of shipment ids of the {@link Shipment} to be cancelled.
	 * @return the response with the Cancelled {@link Shipment} object embedded in it.
	 */
	@Path("/cancel")
	@POST
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response cancelShipments(final JaxbBaseSet<String> shipmentIds)
	{
		LOGGER.trace("Cancel shipments");
		final BatchResult result = this.shipmentServices.cancelShipments(shipmentIds.getSet());

		final GenericEntity<BatchResult> entity = new GenericEntity<BatchResult>(result)
		{// empty as always
		};


		return Response.ok().entity(entity).build();
	}

	/**
	 * Confirm shipments.
	 * 
	 * @param shipmentIds - the set of shipment ids of the {@link Shipment} to be confirmed.
	 * @return the response with the confirmed {@link Shipment} object embedded in it.
	 */
	@Path("/confirm")
	@POST
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response confirmShipments(final JaxbBaseSet<String> shipmentIds)
	{
		LOGGER.trace("Confirm shipments");
		final BatchResult result = this.shipmentServices.confirmShipments(shipmentIds.getSet());

		final GenericEntity<BatchResult> entity = new GenericEntity<BatchResult>(result)
		{// empty as always
		};


		return Response.ok().entity(entity).build();
	}

	/**
	 * Get shipment by id.
	 * 
	 * @param shipmentId
	 *           the shipment id to fetch
	 * @return the {@link Shipment} object
	 */
	private Shipment getShipmentById(final String shipmentId)
	{
		return this.getShipmentServices().getShipmentById(shipmentId);
	}

	private ShipmentFacade getShipmentServices()
	{
		return this.shipmentServices;
	}

	public void setShipmentServices(final ShipmentFacade shipmentServices)
	{
		this.shipmentServices = shipmentServices;
	}
}
