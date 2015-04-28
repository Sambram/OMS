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

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.hybris.oms.domain.returns.ReturnReview;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.returns.ReturnFacade;


@Component
@Path("/returns/{returnId}/reviews")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class ReturnReviewsResource
{
	@Autowired
	private ReturnFacade returnFacade;

	@POST
	@TypeHint(ReturnReview.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.INVENTORY_MANAGER})
	public Response createReturnReview(@PathParam("returnId") final String returnId, final ReturnReview returnReview)
	{
		returnReview.setReturnId(returnId);
		returnFacade.createReturnReview(returnReview);
		return Response.ok().entity(returnReview).build();
	}
}
