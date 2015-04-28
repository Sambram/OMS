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
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationList;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.rest.web.util.QueryObjectPopulator;
import com.hybris.oms.rest.web.util.RestUtil;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse.Status;


/**
 * WebResource exposing {@link InventoryFacade} http://localhost:8080/oms-rest-webapp/webresources/inventory.
 */
@Component
@Path("/inventory")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class InventoryResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResource.class);

	@Autowired
	@Qualifier("uriQueryObjectPopulator")
	private QueryObjectPopulator<UriInfo> queryObjectPopulator;

	@Autowired
	private InventoryFacade inventoryServiceApi;

	/**
	 * Create one single inventory item.
	 * 
	 * @param inventory
	 *           An OmsInventory object to be created
	 * 
	 * @return A Response object containing the created OmsInventory object embedded in it.
	 */
	@POST
	@Secured(RoleConstants.ADMIN)
	public Response createInventory(final OmsInventory inventory)
	{
		final OmsInventory createdDto = this.inventoryServiceApi.createInventory(inventory);
		return Response.status(Status.CREATED).entity(createdDto).build();
	}

	/**
	 * 
	 * Delete one single inventory item.
	 * 
	 * @param inventory
	 *           An OmsInventory object to be deleted
	 */
	@DELETE
	@Secured(RoleConstants.ADMIN)
	public void deleteInventory(final OmsInventory inventory)
	{
		this.inventoryServiceApi.deleteInventory(inventory);
	}

	/**
	 * Get a list of ItemLocation according to given query parameters. <br>
	 * <i>Notice that HTTP implementations usually restrict the size of a get call. To grant the call to this resource
	 * will
	 * work, it is recommended that the whole URL does not exceed 1024 characters.</i>
	 * 
	 * @param uriInfo
	 *           URI info.
	 * 
	 * @return A Response object containing a list of ItemLocation embedded in it.
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response findItemLocationsByQuery(@Context final UriInfo uriInfo)
	{
		LOGGER.trace("findItemLocationsByQuery");
		final ItemLocationList result = new ItemLocationList();

		final ItemLocationsQueryObject queryObject = new ItemLocationsQueryObject();
		this.queryObjectPopulator.populate(uriInfo, queryObject);

		final Pageable<ItemLocation> pagedItemLocations = this.inventoryServiceApi.findItemLocationsByQuery(queryObject);
		if (pagedItemLocations.getResults() != null)
		{
			result.initializeItemLocations(pagedItemLocations.getResults());
		}

		final Response.ResponseBuilder responseBuilder = RestUtil.createResponsePagedHeaders(pagedItemLocations.getNextPage(),
				pagedItemLocations.getPreviousPage(), pagedItemLocations.getTotalPages(), pagedItemLocations.getTotalRecords());

		final GenericEntity<ItemLocationList> entity = new ItemLocationListEntity(result);
		return responseBuilder.entity(entity).build();
	}

	/**
	 * incrementally update one single inventory item.
	 * 
	 * @param inventory
	 *           An OmsInventory object to be updated
	 * @param isIncremental
	 *           a boolean Indicator if the update is incremental
	 * @return response with the DTO object representing the updated inventory
	 */
	@PUT
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response updateInventory(final OmsInventory inventory, @QueryParam("incremental") final Boolean isIncremental)
	{
		OmsInventory updatedDto = null;
		if (isIncremental)
		{
			updatedDto = this.inventoryServiceApi.updateIncrementalInventory(inventory);
		}
		else
		{
			updatedDto = this.inventoryServiceApi.updateInventory(inventory);
		}
		return Response.status(Status.OK).entity(updatedDto).build();
	}

	/**
	 * The Class ItemLocationListEntity.
	 */
	private static final class ItemLocationListEntity extends GenericEntity<ItemLocationList>
	{

		/**
		 * Instantiates a new item location list entity.
		 * 
		 * @param entity
		 *           the entity
		 */
		private ItemLocationListEntity(final ItemLocationList entity)
		{
			super(entity);
		}
	}
}
