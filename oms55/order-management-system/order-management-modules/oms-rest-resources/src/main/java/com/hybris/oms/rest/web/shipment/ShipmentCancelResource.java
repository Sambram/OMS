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
 * http://localhost:8080/oms-rest-webapp/webresources/shipments/{shipmentId}/cancel.
 */
@Component
@Path("/shipments/{shipmentId}/cancel")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class ShipmentCancelResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentCancelResource.class);

	@Autowired
	private ShipmentFacade shipmentServices;

	/**
	 * Cancel shipment.
	 * 
	 * @param shipmentId the shipment id of the {@link Shipment} to be cancelled.
	 * @return the response with the cancelled {@link Shipment} object embedded in it
	 */
	@POST
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response cancelShipment(@PathParam("shipmentId") final String shipmentId)
	{
		LOGGER.trace("cancelShipment");
		final Shipment shipment = this.shipmentServices.cancelShipment(shipmentId);
		return Response.ok().entity(shipment).build();
	}
}
