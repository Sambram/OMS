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
package com.hybris.oms.rest.web.rule;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.rule.RuleFacade;
import com.hybris.oms.domain.JaxbBaseSet;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link RuleFacade} Local Path:
 * http://localhost:8080/oms-rest-webapp/webresources/inventoryupdatestrategies.
 */

@Path("/inventoryupdatestrategies")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Component
public class RulesInventoryStrategiesResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RulesInventoryStrategiesResource.class);

	@Autowired
	private RuleFacade rulesServices;

	/**
	 * Get all of the available inventory update strategies.
	 * 
	 * @return rest response with set of keys for available strategies.
	 */
	@GET
	@Secured({RoleConstants.ADMIN})
	public Response findAllInventoryUpdateStrategies()
	{
		LOGGER.trace("findAllInventoryUpdateStrategies");
		final Set<String> options = this.rulesServices.findAllInventoryUpdateStrategies();
		return Response.ok().entity(new JaxbBaseSet<>(options)).build();
	}
}
