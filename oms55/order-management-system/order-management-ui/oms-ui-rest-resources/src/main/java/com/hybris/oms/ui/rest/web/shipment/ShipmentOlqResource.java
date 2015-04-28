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
 * http://localhost:8080/oms-rest-webapp/webresources/shipments/{shipmentId}/olq/{olqId}.
 */
@Component
@Path("/shipments/{shipmentId}/olq/{olqId}")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class ShipmentOlqResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentOlqResource.class);

	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	/**
	 * Request removal of an OLQ from a shipment.
	 * 
	 * @param shipmentId
	 *           the shipment id that identifies the @ Shipment} object where we will remove the passed olqs from.
	 * @param olqId
	 *           The order line quantity id that is requested to be removed from the shipment
	 * @return rest response with 200 status code
	 */
	@DELETE
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response removeOLQFromShipment(@PathParam("shipmentId") final String shipmentId, @PathParam("olqId") final String olqId)
	{
		LOGGER.trace("removeOLQFromShipment");
		this.uiShipmentFacade.removeOrderLineQuantityFromShipment(shipmentId, olqId);
		return Response.ok().build();
	}
}
