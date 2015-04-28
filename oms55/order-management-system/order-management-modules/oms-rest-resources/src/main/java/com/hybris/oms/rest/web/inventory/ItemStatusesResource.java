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
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.ItemStatusList;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;



/**
 * WebResource exposing {@link InventoryFacade} http://localhost:8080/oms-rest-webapp/webresources/statuses/items.
 */
@Component
@Path("/statuses/items")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class ItemStatusesResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemStatusesResource.class);

	@Autowired
	private InventoryFacade inventoryServiceApi;

	/**
	 * Create one item status.
	 * 
	 * @param itemStatus the itemStatus object to be created
	 * @return a Response object containing the ItemStatus object embedded in it
	 */
	@POST
	@Secured(RoleConstants.ADMIN)
	public Response createItemStatus(final ItemStatus itemStatus)
	{
		LOGGER.trace("createItemStatus");
		final ItemStatus itemStatusSaved = this.inventoryServiceApi.createItemStatus(itemStatus);
		final GenericEntity<ItemStatus> entity = new ItemStatusEntity(itemStatusSaved);
		return Response.ok().entity(entity).build();
	}

	/**
	 * Get a list of all the item statuses in the system.
	 * 
	 * @return a Response object containing the list of ItemStatus object embedded in it.
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
	public Response findAllItemStatuses()
	{
		LOGGER.trace("findAllItemStatuses ");

		final ItemStatusList result = new ItemStatusList();
		final List<ItemStatus> itemStatuses = this.inventoryServiceApi.findAllItemStatuses();
		if (itemStatuses != null)
		{
			result.initializeItemStatuses(itemStatuses);
		}
		final GenericEntity<ItemStatusList> entity = new ItemStatusListEntity(result);
		return Response.ok().entity(entity).build();
	}

	/**
	 * The Class ItemStatusEntity.
	 */
	private static final class ItemStatusEntity extends GenericEntity<ItemStatus>
	{

		/**
		 * Instantiates a new item status entity.
		 * 
		 * @param entity the entity
		 */
		private ItemStatusEntity(final ItemStatus entity)
		{
			super(entity);
		}
	}

	/**
	 * The Class ItemStatusListEntity.
	 */
	private static final class ItemStatusListEntity extends GenericEntity<ItemStatusList>
	{

		/**
		 * Instantiates a new item status list entity.
		 * 
		 * @param entity of type ItemStatuList
		 */
		private ItemStatusListEntity(final ItemStatusList entity)
		{
			super(entity);
		}
	}
}
