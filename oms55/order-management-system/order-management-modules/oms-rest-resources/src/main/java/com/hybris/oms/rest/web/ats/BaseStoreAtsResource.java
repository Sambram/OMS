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
 * WebResource exposing {@link AtsFacade} http://localhost:8080/oms-rest-webapp/webresources/ats/baseStore.
 */

@Component
@Path("/ats/baseStore")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class BaseStoreAtsResource
{

	private static final String PARAM_ATS_ID = "atsId";

	private static final String PARAM_SKU = "sku";

	private static final String LOCATION_ROLES = "locationRoles";

	private static final String BASESTORE_NAME = "baseStore";

	private static final String COUNTRY_CODES = "shipToCountriesCodes";

	@Autowired
	private AtsFacade facade;

	/**
	 * Find ats by base store.
	 * 
	 * @param skus the item ids that the ats will be calculated for
	 * @param atsIds the ats formula ids that it will be used to calculate the ats quantities
	 * @param baseStore the base store name (id) which the ats quantities will be calculated for
	 * @param countryCodes the country codes which the ats quantities will be calculated for
	 * @param locationRoles the location roles which the ats quantities will be calculated for
	 * @return the list of type AtsLocalQuantities filtered by passed: skus, atsIds, baseStore, countryCodes and
	 *         locationRoles
	 * @throws EntityNotFoundException thrown when one of the passed skus, locations, atsIds, locationRoles or
	 *            countryCodes is not found
	 */
	@GET
	@Secured({RoleConstants.ADMIN, RoleConstants.ACCELERATOR})
	public List<AtsLocalQuantities> findAtsByBaseStore(@QueryParam(PARAM_SKU) final Set<String> skus,
			@QueryParam(PARAM_ATS_ID) final Set<String> atsIds, @QueryParam(BASESTORE_NAME) final String baseStore,
			@QueryParam(COUNTRY_CODES) final Set<String> countryCodes, @QueryParam(LOCATION_ROLES) final Set<String> locationRoles)
			throws EntityNotFoundException
	{
		return this.facade.findAtsByBaseStore(skus, atsIds, baseStore, countryCodes, locationRoles);
	}

}
