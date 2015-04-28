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
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.domain.exception.EntityNotFoundException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link AtsFacade} http://localhost:8080/oms-rest-webapp/webresources/ats/formula/atsId.
 * 
 */
@Component
@Path("/ats/formula/{" + AtsFormulaResource.PARAM_ATS_ID + '}')
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class AtsFormulaResource
{


	public static final String PARAM_ATS_ID = "atsId";


	private static final Logger LOG = LoggerFactory.getLogger(AtsFormulaResource.class);


	@Autowired
	private AtsFacade facade;

	/**
	 * Delete formula.
	 * 
	 * @param atsId The id of the ats formula to be deleted.
	 * @throws EntityNotFoundException thrown when the formula to be deleted is not found
	 */
	@DELETE
	@Secured(RoleConstants.ADMIN)
	public void deleteFormula(@PathParam(PARAM_ATS_ID) final String atsId) throws EntityNotFoundException
	{
		LOG.debug("DELETE /ats/formula/{}", atsId);
		this.facade.deleteFormula(atsId);
	}

	/**
	 * Gets the formula by id.
	 * 
	 * @param atsId The id of the ats formula to be retrieved.
	 * @return the AtsFormula object corresponding the passed ats id
	 * @throws EntityNotFoundException thrown when the formula to be retrieved is not found
	 */
	@GET
	@Secured(RoleConstants.ADMIN)
	public AtsFormula getFormulaById(@PathParam(PARAM_ATS_ID) final String atsId) throws EntityNotFoundException
	{
		LOG.debug("GET /ats/formula/{}", atsId);
		return this.facade.getFormulaById(atsId);
	}

	/**
	 * Update formula by id.
	 * 
	 * @param atsId The id of the ats formula to be updated.
	 * @param formula of type AtsFormula to update
	 * @return the updated AtsFormula object
	 * @throws EntityNotFoundException thrown when the formula to be updated is not found
	 */
	@PUT
	@Secured(RoleConstants.ADMIN)
	public AtsFormula updateFormula(@PathParam(PARAM_ATS_ID) final String atsId, final AtsFormula formula)
			throws EntityNotFoundException
	{
		LOG.debug("PUT /ats/formula/{}", atsId);
		this.facade.updateFormula(atsId, formula);
		return formula;
	}


}
