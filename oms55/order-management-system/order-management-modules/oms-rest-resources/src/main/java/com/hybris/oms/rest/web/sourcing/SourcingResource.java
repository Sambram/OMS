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
package com.hybris.oms.rest.web.sourcing;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.fulfillment.SourceSimulationFacade;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.order.jaxb.SourceSimulationParameter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link SourceSimulationFacade} http://localhost:8080/oms-rest-webapp/webresources/source.
 */

@Component
@Path("/source")
@Consumes({MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_XML})
public class SourcingResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SourcingResource.class);

	@Autowired
	@Qualifier("defaultSourcingSimulationFacade")
	private SourceSimulationFacade sourcingSimulationFacade;

	/**
	 * Get the best location for a given <sku, quantity> using an adress and/or a ats.
	 * 
	 * @param sourceSimulationParameter {@link SourceSimulationParameter} a map of sku and quantity
	 *           map containing sku and quantity
	 * 
	 * @return a Response object containing the best location {@link Location}
	 */
	@POST
	@Secured(RoleConstants.ACCELERATOR)
	public Response sourceBestLocation(final SourceSimulationParameter sourceSimulationParameter)
	{
		LOGGER.trace("sourceBestLocation");

		final Location location = this.sourcingSimulationFacade.findBestSourcingLocation(sourceSimulationParameter);
		final GenericEntity<Location> entity = new GenericEntity<Location>(location)
		{ /* NOPMD */};

		return Response.ok().entity(entity).build();
	}
}
