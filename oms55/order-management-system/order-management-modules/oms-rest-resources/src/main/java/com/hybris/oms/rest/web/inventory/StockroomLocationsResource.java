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
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.domain.inventory.StockRoomLocationList;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;



/**
 * WebResource exposing {@link InventoryFacade} http://localhost:8080/oms-rest-webapp/webresources/stockrooms.
 */
@Component
@Path("/stockrooms")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class StockroomLocationsResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(StockroomLocationsResource.class);

	@Autowired
	private InventoryFacade inventoryServiceApi;

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	/**
	 * Create one Stock Room Location.
	 * 
	 * @param location
	 *           {@link Location} object to be created
	 * @return A Response object containing the created {@link Location} object embedded in it.
	 */
	@POST
	@TypeHint(Location.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
	public Response createStockRoomLocation(final Location location)
	{
		LOGGER.trace("createStockRoomLocation");

		final Location locationSaved = this.inventoryServiceApi.createStockRoomLocation(location);
		return Response.ok().entity(locationSaved).build();
	}

	/**
	 * Create or Update one Stock Room Location.
	 * 
	 * @param location
	 *           {@link Location} object to be created/updated
	 * @return A Response object containing the created/updated Location object embedded in it.
	 */
	@PUT
	@TypeHint(Location.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response createUpdateStockRoomLocation(final Location location)
	{
		LOGGER.trace("createUpdateStockRoomLocation");

		final Location locationSaved = this.inventoryServiceApi.createUpdateStockRoomLocation(location);
		return Response.ok().entity(locationSaved).build();
	}

	/**
	 * Get a list of all the Stock Room Location.
	 * 
	 * @param uriInfo
	 *           URI info.
	 * 
	 * @return a Response object containing the list of all Location objects.
	 *         {@link com.hybris.oms.domain.inventory.LocationQuerySortSupport}
	 *         {@link com.hybris.oms.domain.SortDirection}
	 */
	@GET
	@SuppressWarnings("PMD.ExcessiveParameterList")
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response findAllStockRoomLocations(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findStockRoomLocationsByQuery");
		final StockRoomLocationList result = new StockRoomLocationList();

		final LocationQueryObject queryObject = new LocationQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<Location> pagedLocations = this.inventoryServiceApi.findStockRoomLocationsByQuery(queryObject);
		if (pagedLocations.getResults() != null)
		{
			result.initializeLocations(pagedLocations.getResults());
		}

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedLocations.getNextPage(),
				pagedLocations.getPreviousPage(), pagedLocations.getTotalPages(), pagedLocations.getTotalRecords());

		final GenericEntity<StockRoomLocationList> entity = new GenericEntity<StockRoomLocationList>(result)
		{};

		return responseBuilder.entity(entity).build();
	}

}
