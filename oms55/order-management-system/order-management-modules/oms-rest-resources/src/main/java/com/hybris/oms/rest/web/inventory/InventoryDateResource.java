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
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.ClientResponse;


/**
 * 
 * WebResource exposing {@link InventoryFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/inventory/date/newExpectedDeliveryDate.
 */
@Component
@Path("/inventory/date/{newExpectedDeliveryDate: [0-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]}")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class InventoryDateResource
{
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	@Autowired
	private InventoryFacade inventoryServiceApi;

	/**
	 * Updates the future inventory for changed expectedDeliveryDate and quantity.
	 * 
	 * @param omsInventories a list of objects from type OmsInventory to be updated
	 * @param newExpectedDeliveryDate the new expected delivery date to update
	 * @throws EntityNotFoundException thrown when one of OmsInventory objects to be updated is not found
	 * @throws java.text.ParseException thrown when parsing of the expected delivery date have an error
	 */
	@PUT
	@Secured({RoleConstants.ADMIN, RoleConstants.FULFILLMENT_MANAGER, RoleConstants.INVENTORY_MANAGER})
	public Response updateFutureInventoryDateQuantity(final List<OmsInventory> omsInventories,
			@PathParam("newExpectedDeliveryDate") final String newExpectedDeliveryDate) throws EntityNotFoundException,
			ParseException
	{
		List<OmsInventory> updInventories = null;

		if (omsInventories != null)
		{
			if (omsInventories.get(0).getDeliveryDate() == null || newExpectedDeliveryDate == null)
			{
				for (final OmsInventory omsInventory : omsInventories)
				{
					this.inventoryServiceApi.updateIncrementalInventory(omsInventory);
				}
			}
			else
			{
				Date deliveryDate = null;
				final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				deliveryDate = dateFormat.parse(newExpectedDeliveryDate);
				updInventories = this.inventoryServiceApi.updateFutureInventoryDate(omsInventories, deliveryDate);
			}
		}
		final GenericEntity<List<OmsInventory>> genericEntity = new GenericEntity<List<OmsInventory>>(updInventories)
		{
			// empty block
		};
		return Response.status(ClientResponse.Status.OK).entity(genericEntity).build();
	}

}
