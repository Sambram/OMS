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
package com.hybris.oms.rest.web.inventory;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.inventory.Bin;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * The Class BinLocationResource
 * Provide REST API for operations that require locationId in the URI
 */

/**
 * WebResource exposing {@link InventoryFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/stockrooms/{locationId}/bins.
 */
@Component
@Path("/stockrooms/{locationId}/bins")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class BinLocationResource
{

	private static final Logger LOGGER = LoggerFactory.getLogger(BinLocationResource.class);

	@Autowired
	private InventoryFacade inventoryServiceApi;

	/**
	 * Create one single bin item.
	 * 
	 * @param bin
	 *           A Bin object to be created
	 * @param locationId
	 *           location unique identifier to be linked to the bin
	 * 
	 * @return A Response object containing a Bin object embedded in it.
	 */
	@POST
	@TypeHint(Bin.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.INVENTORY_MANAGER})
	public Response createBin(@PathParam("locationId") final String locationId, final Bin bin)
	{
		LOGGER.trace("createBin");
		bin.setLocationId(locationId);
		final Bin createdDto = this.inventoryServiceApi.createBin(bin);
		return Response.status(Status.CREATED).entity(createdDto).build();
	}

}
