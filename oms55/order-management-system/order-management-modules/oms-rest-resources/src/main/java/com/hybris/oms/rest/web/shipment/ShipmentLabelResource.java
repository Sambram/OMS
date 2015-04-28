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
import javax.ws.rs.GET;
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
 * http://localhost:8080/oms-rest-webapp/webresources/shipments/{shipmentId}/label.
 */
@Component
@Path("/shipments/{shipmentId}/label")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class ShipmentLabelResource
{

	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentLabelResource.class);

	@Autowired
	private ShipmentFacade shipmentServices;

	/**
	 * Get Shipping label.
	 * 
	 * @param shipmentId the shipment id of the {@link Shipment} to be fetch the shipping label for.
	 * @return byte array containing the label
	 */

	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response getShipmentsLabel(@PathParam("shipmentId") final String shipmentId)
	{
		LOGGER.trace("getShipmentsLabel");
		final byte[] label = this.shipmentServices.retrieveShippingLabelsByShipmentId(shipmentId);
		return Response.ok().entity(label).build();
	}
}
