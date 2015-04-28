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
package com.hybris.oms.ui.rest.web.shipment;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
 * http://localhost:8080/oms-rest-webapp/webresources/shipments/{shipmentId}/allOlq.
 */
@Component
@Path("/shipments/{shipmentId}/allOLQ")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class ShipmentAllOlqResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentAllOlqResource.class);

	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	/**
	 * Remove all OLQs assigned to Shipment.
	 * 
	 * @param shipmentId
	 *           the shipment id that identifies the @ Shipment} object where we will remove the list of olqs from.
	 * @return rest response with 200 status code
	 */
	@DELETE
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response removeAllOLQsFromShipment(@PathParam("shipmentId") final String shipmentId)
	{
		LOGGER.trace("removeAllOLQsFromShipment");
		this.uiShipmentFacade.removeAllOrderLineQuantitiesFromShipment(shipmentId);
		return Response.ok().build();
	}
}
