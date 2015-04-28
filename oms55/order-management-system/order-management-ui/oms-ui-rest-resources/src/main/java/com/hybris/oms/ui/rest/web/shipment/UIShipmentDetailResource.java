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
import com.hybris.oms.ui.api.shipment.ShipmentDetail;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


@Component
@Path("/uishipmentdetails/{shipmentId}")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class UIShipmentDetailResource
{
	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	@GET
	@TypeHint(ShipmentDetail.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.SHIPPING_USER, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response getShipmentDetailById(@PathParam("shipmentId") final String shipmentId)
	{
		final ShipmentDetail shipmentDetail = this.uiShipmentFacade.getShipmentDetailById(shipmentId);
		final Response.ResponseBuilder response = Response.ok();
		response.entity(shipmentDetail);
		return response.build();
	}
}
