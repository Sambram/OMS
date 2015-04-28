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
package com.hybris.oms.ui.rest.web.order;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.ui.api.order.UIOrderDetails;
import com.hybris.oms.ui.api.order.UiOrderFacade;

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


/**
 * WebResource exposing {@link UiOrderFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/uiorderdetails/{orderId}.
 */
@Component
@Path("/uiorderdetails/{orderId}")
@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
		RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class UIOrderDetailsResource
{

	@Autowired
	private UiOrderFacade uiOrderFacade;

	/**
	 * Get one {@link UIOrderDetails} by unique order id.
	 * 
	 * @param orderId the order id which is used to fetch the order object
	 * @return A response with an UIOrderDetails object embedded in it.
	 */
	@GET
	@TypeHint(UIOrderDetails.class)
	public Response getUIOrderDetailsByOrderId(@PathParam("orderId") final String orderId)
	{
		final Response.ResponseBuilder response = Response.ok();
		response.entity(this.uiOrderFacade.getUIOrderDetailsByOrderId(orderId));
		return response.build();
	}

}
