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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
 * The Class BinResource
 * Provide REST API for operations that require both locationId and binCode in the URI
 * 
 */

/**
 * WebResource exposing {@link InventoryFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/stockrooms/{locationId}/bins/{binCode}.
 */
@Component
@Path("/stockrooms/{locationId}/bins/{binCode}")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class BinResource
{

	private static final Logger LOGGER = LoggerFactory.getLogger(BinResource.class);

	@Autowired
	private InventoryFacade inventoryServiceApi;

	/**
	 * Delete one bin.
	 * 
	 * @param binCode the bin code that defines the bin object to be deleted
	 * @param locationId the location id that's linked to the bin object to be deleted
	 * @return a Response object with NO_CONTENT.
	 */
	@DELETE
	@Secured({RoleConstants.ADMIN, RoleConstants.INVENTORY_MANAGER})
	public Response deleteBin(@PathParam("binCode") final String binCode, @PathParam("locationId") final String locationId)
	{
		LOGGER.trace("deleteBin");
		this.inventoryServiceApi.deleteBinByBinCodeLocationId(binCode, locationId);
		return Response.noContent().build();
	}

	/**
	 * Get one bin defined by bin code and location id.
	 * 
	 * @param binCode the bin code that defines the bin object to be retrieved
	 * @param locationId the location id that's linked to the bin object to be retrieved
	 * @return a Response object containing the bin dto.
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.INVENTORY_MANAGER})
	public Response getBin(@PathParam("binCode") final String binCode, @PathParam("locationId") final String locationId)
	{
		LOGGER.trace("getBin");
		final Bin bin = this.inventoryServiceApi.getBinByBinCodeLocationId(binCode, locationId);
		return Response.ok().entity(bin).build();
	}

	/**
	 * Update one bin.
	 * 
	 * @param bin the Bin object to be updated
	 * @param binCode the bin code that defines the bin object to be updated
	 * @param locationId the location id that is linked to the bin object to be updated
	 * @return a Response object containing the updated Bin object embedded in it.
	 */
	@PUT
	@TypeHint(Bin.class)
	@Secured(RoleConstants.ADMIN)
	public Response updateBin(@PathParam("binCode") final String binCode, @PathParam("locationId") final String locationId,
			final Bin bin)
	{
		LOGGER.trace("updateBin");

		bin.setBinCode(binCode);
		bin.setLocationId(locationId);

		final Bin entity = this.inventoryServiceApi.updateBin(bin);
		return Response.ok().entity(entity).build();
	}

}
