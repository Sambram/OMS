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
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.service.ats.DuplicateFormulaException;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.jaxrs.TypeHint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


/**
 * WebResource exposing {@link AtsFacade} http://localhost:8080/oms-rest-webapp/webresources/ats/formula.
 */
@Component
@Path("/ats/formula")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class AtsFormulasResource
{

	private static final Logger LOG = LoggerFactory.getLogger(AtsFormulasResource.class);

	@Autowired
	private AtsFacade facade;

	/**
	 * Creates the formula.
	 * 
	 * @param formula the object of type {@link AtsFormula} to be created
	 * @return the response with the object of type {@link AtsFormula} embedded in it
	 * @throws DuplicateFormulaException thrown when the formula to be created already exist
	 */
	@POST
	@Secured(RoleConstants.ADMIN)
	@TypeHint(AtsFormula.class)
	public Response createFormula(final AtsFormula formula) throws DuplicateEntityException
	{
		this.facade.createFormula(formula);
		return Response.status(Response.Status.CREATED).entity(formula).build();
	}

	/**
	 * Find all formulas.
	 * 
	 * @return collection of type AtsFormula representing all existing formulas
	 */
	@GET
	@Secured(RoleConstants.ADMIN)
	public Collection<AtsFormula> findAllFormulas()
	{
		LOG.debug("GET /ats/formula");
		return this.facade.findAllFormulas();
	}

}
