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
package com.hybris.oms.rest.web.order;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.adapter.DateAdaptor;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link OrderFacade} http://localhost:8080/oms-rest-webapp/webresources/ordersIds.
 */

@Component
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/ordersIds")
public class OrdersIdResource
{
	@Autowired
	private OrderFacade orderFacade;

	/**
	 * Generates a list of order IDs from a given query.
	 * 
	 * @param uriInfo {@link UriInfo} object in which query parameters will be used to filter the fetched order ids
	 * @return response with no content if "updatedSince" query parameter was null. and it was not null it will return
	 *         the list of order ids updated since.
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.ACCELERATOR, RoleConstants.SHIPPING_USER,
			RoleConstants.INSTOREPICKUP_USER, RoleConstants.INSTOREPICKUPANDSHIPPING_USER})
	public Response getOrderIds(@Context final UriInfo uriInfo)
	{
		// There is currently only one option here
		final String updatedSince = uriInfo.getQueryParameters(true).getFirst("updatedSince");
		if (updatedSince != null)
		{
			final Date cutOff = new DateAdaptor().unmarshal(updatedSince);
			return Response.ok().entity(this.orderFacade.getOrderIdsUpdatedAfter(cutOff)).build();
		}

		return Response.noContent().build();
	}
}
