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
import com.hybris.oms.ui.api.shipment.PickSlipBinInfo;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

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
 * http://localhost:8080/oms-rest-webapp/webresources/shipments/{shipmentId}/pick.
 */
@Component
@Path("/shipments/{shipmentId}/uiPickShipmentWithBins")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class ShipmentPickSlipBinInfoResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentPickSlipBinInfoResource.class);

	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	/**
	 * Gets the pick slip info.
	 * 
	 * @param shipmentId the shipment id of the {@link PickSlipBinInfo} to be fetched
	 * @return the pick slip info {@link PickSlipBinInfo}
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response getPickSlipInfo(@PathParam("shipmentId") final String shipmentId)
	{
		LOGGER.trace("pickShipmentWithBins");
		final PickSlipBinInfo pickslipInfo = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(shipmentId);
		return Response.ok().entity(pickslipInfo).build();
	}
}
