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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
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
 * http://localhost:8080/oms-rest-webapp/webresources/statuses/items/{statusCode}.
 */
@Component
@Path("/statuses/items/{statusCode}")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class InventoryStatusResource
{

	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryStatusResource.class);
	@Autowired
	private InventoryFacade inventoryServiceApi;

	/**
	 * Get one item status by status code.
	 * 
	 * @param statusCode the status code which the retrieved itemStatus will be filtered on ( String Based on the
	 *           ItemStatus object )
	 * @return a Response object containing a ItemStatus object embedded in it
	 */
	@GET
	@TypeHint(ItemStatus.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response getItemStatusByStatusCode(@PathParam("statusCode") final String statusCode)
	{
		LOGGER.trace("getItemStatusByStatusCode");

		final ItemStatus itemStatus = this.inventoryServiceApi.getItemStatusByStatusCode(statusCode);
		final GenericEntity<ItemStatus> entity = new ItemStatusEntity(itemStatus);
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
}
