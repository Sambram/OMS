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
package com.hybris.oms.ui.rest.web.returns;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.ui.api.returns.ReturnDetail;
import com.hybris.oms.ui.api.returns.ReturnDetailFacade;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link ReturnDetailFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/buildreturndetail/{orderId}.
 */
@Component
@Path("/orders/{orderId}/returnDetails/build")
@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR})
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class BuildReturnDetailResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildReturnDetailResource.class);

	@Autowired
	private ReturnDetailFacade uiReturnFacade;

	/**
	 * Get a new {@link ReturnDetail} by linked order id.
	 * 
	 * @param orderId the order id which is linked to the return object
	 * @return A response with an ReturnDetail object embedded in it.
	 */
	@GET
	@TypeHint(ReturnDetail.class)
	public Response buildReturnDetailByOrderId(@PathParam("orderId") final String orderId)
	{
		LOGGER.trace("buildReturnDetailByOrderId");
		final Response.ResponseBuilder response = Response.ok();
		response.entity(this.uiReturnFacade.buildReturnDetailByOrderId(orderId));
		return response.build();
	}

}
