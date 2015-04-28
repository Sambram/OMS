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
import com.hybris.oms.api.inventory.OmsInventory;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse;


/**
 * WebResource exposing {@link InventoryFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/inventory/createUpdate.
 */

@Component
@Path("/inventory/createUpdate")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class InventoryCreateUpdateResource
{
	@Autowired
	private InventoryFacade inventoryServiceApi;

	/**
	 * create or update one single inventory item.
	 * 
	 * @param inventory An OmsInventory object to be updated if it already exist or to be created if it doesn't
	 * 
	 * @return A Response object containing the updated/created OmsInventory object embedded in it.
	 */
	@PUT
	@TypeHint(OmsInventory.class)
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response createUpdateInventory(final OmsInventory inventory)
	{
		final OmsInventory createdDto = this.inventoryServiceApi.createUpdateInventory(inventory);
		return Response.status(ClientResponse.Status.OK).entity(createdDto).build();
	}

}
