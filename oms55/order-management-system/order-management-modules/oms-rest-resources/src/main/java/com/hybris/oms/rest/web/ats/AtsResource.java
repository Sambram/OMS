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
package com.hybris.oms.rest.web.ats;

import com.hybris.commons.web.authorization.RoleConstants;
import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.domain.ats.AtsQuantities;
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link AtsFacade} http://localhost:8080/oms-rest-webapp/webresources/ats.
 */
@Component
@Path("/ats")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class AtsResource
{

	private static final Logger LOG = LoggerFactory.getLogger(AtsResource.class);

	private static final String PARAM_ATS_ID = "atsId";

	private static final String PARAM_SKU = "sku";

	@Autowired
	private AtsFacade facade;

	/**
	 * Gets the global ats.
	 * 
	 * @param skus the item ids that the global ats quantities will be calculated for
	 * @param atsIds the ats formula ids that will be used to calculate the ats quantities
	 * @throws EntityNotFoundException thrown when either the passed sku(s) or the ats formula id(s) are not found
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
	public AtsQuantities getGlobalAts(@QueryParam(PARAM_SKU) final Set<String> skus,
			@QueryParam(PARAM_ATS_ID) final Set<String> atsIds) throws EntityNotFoundException
	{
		LOG.debug("GET /ats");
		return this.facade.findGlobalAts(skus, atsIds);
	}
}
