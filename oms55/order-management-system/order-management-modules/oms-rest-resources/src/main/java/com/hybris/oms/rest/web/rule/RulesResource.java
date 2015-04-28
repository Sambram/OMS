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
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.domain.rule.domain.Rule;
import com.hybris.oms.domain.rule.domain.RuleList;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
 * WebResource exposing {@link RuleFacade} Local Path: http://localhost:8080/oms-rest-webapp/webresources/rules.
 */
@Path("/rules")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Component
public class RulesResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RulesResource.class);

	@Autowired
	private RuleFacade rulesServices;

	/**
	 * Get list of all rules.
	 * 
	 * @return rest response with list of rules
	 */
	@GET
	@Secured(RoleConstants.ADMIN)
	public Response findAllRules()
	{
		LOGGER.trace("findAllRules()");
		final RuleList result = new RuleList();
		final List<Rule> rules = this.rulesServices.findAllRules();
		if (rules != null)
		{
			result.initialize(rules);
		}
		return Response.ok().entity(result).build();
	}

	/**
	 * Create new rule via REST interface.
	 * 
	 * @param rule an object of type {@link RuleShort} to be created
	 * @return response object with the newly created {@link Rule} object embedded in it
	 */
	@POST
	@Secured(RoleConstants.ADMIN)
	public Response postRule(final RuleShort rule)
	{
		LOGGER.trace("postRule()");
		final Rule ruleCreated = this.rulesServices.createRule(rule);
		return Response.status(Response.Status.CREATED).entity(ruleCreated).build();
	}


}
