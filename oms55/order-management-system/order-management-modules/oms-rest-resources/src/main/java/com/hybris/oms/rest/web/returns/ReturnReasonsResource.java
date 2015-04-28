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
package com.hybris.oms.rest.web.returns;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.returns.ReturnFacade;
import com.hybris.oms.domain.JaxbBaseList;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;


@Service
@Path("/returnReasons")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ReturnReasonsResource
{

	@Autowired
	private ReturnFacade returnFacade;


	/**
	 * Get the supported Return Reason Codes.
	 * 
	 * @return response with a @List of the reason codes as @String
	 */
	@GET
	@Path("/codes")
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response getReturnReasonCodes()
	{
		final List<String> codes = returnFacade.returnReasonCodes();

		final Response.ResponseBuilder response = Response.ok();
		response.entity(new JaxbBaseList<String>(codes));
		return response.build();

	}
}
