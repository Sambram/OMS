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
import com.hybris.oms.domain.JaxbBaseMap;
import com.hybris.oms.domain.JaxbBaseSet;
import com.hybris.oms.domain.shipping.ShipmentSplitResult;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link ShipmentFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/shipments/{shipmentId}/olqs/split.
 */
@Component
@Path("/shipments/{shipmentId}/olqs")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ShipmentOlqsResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentOlqsResource.class);

	@Autowired
	private ShipmentFacade shipmentFacade;

	@Path("/split")
	@POST
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public ShipmentSplitResult splitShipmentByOlqs(@PathParam("shipmentId") final String shipmentId,
			final JaxbBaseSet<String> olqIds)
	{
		LOGGER.trace("Split Shipment by olqs");
		return this.shipmentFacade.splitShipmentByOlqs(shipmentId, olqIds.getSet());
	}

	@Path("/splitquantities")
	@POST
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public ShipmentSplitResult splitShipmentByOlqQuantities(@PathParam("shipmentId") final String shipmentId,
			final JaxbBaseMap<String, Integer> olqQuantityMap)
	{
		LOGGER.trace("Split a shipment by orderline quantities and their quantity value.");
		return this.shipmentFacade.splitShipmentByOlqQuantities(shipmentId, olqQuantityMap.getMap());
	}

}
