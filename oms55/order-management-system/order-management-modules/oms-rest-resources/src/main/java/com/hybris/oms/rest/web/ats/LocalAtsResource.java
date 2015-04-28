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
import com.hybris.oms.domain.ats.AtsLocalQuantities;
import com.hybris.oms.domain.exception.EntityNotFoundException;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link AtsFacade} http://localhost:8080/oms-rest-webapp/webresources/ats/local.
 */
@Component
@Path("/ats/local")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class LocalAtsResource
{

	private static final String PARAM_ATS_ID = "atsId";

	private static final String PARAM_LOC_ID = "locId";

	private static final String PARAM_SKU = "sku";

	@Autowired
	private AtsFacade facade;

	/**
	 * Gets the local ats.
	 * 
	 * @param skus items ids which the local ats quantities will be calculated
	 * @param locations stock room location Ids where the local ats quantities will be calculated
	 * @param atsIds the ats formula ids that it will be used to calculate the ats quantities
	 * @return list of type AtsLocalQuantities
	 * @throws EntityNotFoundException thrown when one of the passed skus, atsIds or locations is not found
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
	public List<AtsLocalQuantities> getLocalAts(@QueryParam(PARAM_SKU) final Set<String> skus,
			@QueryParam(PARAM_LOC_ID) final Set<String> locations, @QueryParam(PARAM_ATS_ID) final Set<String> atsIds)
			throws EntityNotFoundException
	{
		return this.facade.findLocalAts(skus, locations, atsIds);
	}
}
