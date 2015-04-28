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
import javax.ws.rs.PUT;
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
 * http://localhost:8080/oms-rest-webapp/webresources/shipments/{shipmentId}/status.
 */
@Component
@Path("/shipments/{shipmentId}/status/{statusCode}")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class ShipmentStatusResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentStatusResource.class);

	@Autowired
	private ShipmentFacade shipmentServices;

	/**
	 * Update shipment status.
	 * 
	 * @param shipmentId the shipment id to be updated with the new olqStatus
	 * @param olqStatus the olq status code to update the shipment with
	 * @return the response that contain the updated shipment object embedded in it
	 */
	@PUT
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response updateShipmentStatus(@PathParam("shipmentId") final String shipmentId,
			@PathParam("statusCode") final String olqStatus)
	{
		LOGGER.trace("updateShipmentStatus");
		final Shipment shipment = this.shipmentServices.updateShipmentStatus(shipmentId, olqStatus);
		return Response.ok().entity(shipment).build();
	}
}
