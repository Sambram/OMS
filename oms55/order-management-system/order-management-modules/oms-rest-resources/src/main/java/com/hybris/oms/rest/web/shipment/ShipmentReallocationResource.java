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
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.shipping.Shipment;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link ShipmentFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/shipments/{shipmentId}/reallocate/{locationId}.
 */
@Component
@Path("/shipments/{shipmentId}/reallocate/{locationId}")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class ShipmentReallocationResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentReallocationResource.class);

	@Autowired
	private ShipmentFacade shipmentServices;

	/**
	 * Reallocate shipment.
	 * 
	 * @param shipmentId - the shipment id of the {@link Shipment} to be reallocated.
	 * @param locationId - the location id of the {@link Location} to reallocate the shipment to.
	 * @return the response with the reallocated {@link Shipment} object embedded in it
	 */
	@POST
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response reallocateShipment(@PathParam("shipmentId") final String shipmentId,
			@PathParam("locationId") final String locationId)
	{
		LOGGER.trace("Re-Allocate Shipment");
		final Shipment shipment = this.shipmentServices.reallocateShipment(shipmentId, locationId);
		return Response.ok().entity(shipment).build();
	}
}
