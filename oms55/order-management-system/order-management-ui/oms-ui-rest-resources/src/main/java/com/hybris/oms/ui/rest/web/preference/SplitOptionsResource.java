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
package com.hybris.oms.ui.rest.web.preference;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.domain.JaxbBaseSet;
import com.hybris.oms.ui.api.preference.UiPreferenceFacade;

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
 * WebResource exposing {@link PreferenceFacade}
 * http://localhost:8080/oms-rest-webapp/webresources/tenantPreferences/splitoptions.
 * This resource is specific to split options.
 */
@Component
@Path("/tenantPreferences/splitoptions")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class SplitOptionsResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SplitOptionsResource.class);

	@Autowired
	private UiPreferenceFacade uiPreferenceFacade;

	/**
	 * Get a set of all the sourcing split options.
	 * 
	 * @return A Response object containing a set of {@link String}s representing the various split options
	 */
	@GET
	@Secured({RoleConstants.ADMIN})
	public Response findAllSourcingSplitOptions()
	{
		LOGGER.trace("findAllSourcingSplitOptions");
		final Set<String> options = this.uiPreferenceFacade.findAllSourcingSplitOptions();
		return Response.ok().entity(new JaxbBaseSet<>(options)).build();
	}

}
