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
import com.hybris.oms.domain.inventory.Location;

import javax.ws.rs.Consumes;
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
 * WebResource exposing {@link InventoryFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/stockrooms/{locationId}.
 */
@Component
@Path("/stockrooms/{locationId}")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class StockroomLocationResource
{
	private static final String LOCATION_ID = "locationId";

	private static final Logger LOGGER = LoggerFactory.getLogger(StockroomLocationsResource.class);
	@Autowired
	private transient InventoryFacade inventoryServiceApi;

	/**
	 * Get one Stock Room Location by stock room location id.
	 * 
	 * @param locationId The unique location id which is used to retrieve a {@link Location} object
	 * @return A Response object containing the Location object embedded in it.
	 */
	@GET
	@TypeHint(Location.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response findStockRoomLocationByLocationId(@PathParam(LOCATION_ID) final String locationId)
	{
		LOGGER.trace("findStockRoomLocationByLocationId");
		final Location stockRoomLocation = this.inventoryServiceApi.getStockRoomLocationByLocationId(locationId);
		return Response.ok().entity(stockRoomLocation).build();
	}

	/**
	 * Update one Stock Room Location.
	 * 
	 * @param locationId the location id of the {@link Location} object to be updated
	 * @param location {@link Location} object to be updated
	 * @return a Response object containing the updated Location object embedded in it.
	 */
	@PUT
	@TypeHint(Location.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response updateStockRoomLocation(@PathParam(LOCATION_ID) final String locationId, final Location location)
	{
		LOGGER.trace("updateStockRoomLocation");

		// Use the location id from the path parameter.
		location.setLocationId(locationId);

		final Location entity = this.inventoryServiceApi.updateStockRoomLocation(location);
		return Response.ok().entity(entity).build();
	}

}
