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
import com.hybris.oms.domain.rule.domain.Rule;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;



/**
 * JAX-RS resource to expose required rules facade methods via web services.
 * WebResource exposing {@link RuleFacade} Local Path: http://localhost:8080/oms-rest-webapp/webresources/rules/{ruleId}
 * 
 * 
 */
@Path("/rules/{ruleId}")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
@Component
public class RuleResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleResource.class);

	@Autowired
	private RuleFacade rulesServices;

	/**
	 * Delete a rule from a given rule set.
	 * 
	 * @param ruleId the rule id to be deleted
	 * @return rest response with no content
	 */
	@DELETE
	@Secured(RoleConstants.ADMIN)
	public Response deleteRuleByRuleId(@PathParam("ruleId") final String ruleId)
	{
		LOGGER.trace("deleteRuleByRuleId({}, {})", ruleId);
		this.rulesServices.deleteRule(ruleId);
		return Response.noContent().build();
	}

	/**
	 * Get rule by id.
	 * 
	 * @param ruleId the rule id to be fetched
	 * @return rest response with list of rules
	 */
	@GET
	@Secured(RoleConstants.ADMIN)
	public Response getRuleByRuleId(@PathParam("ruleId") final String ruleId)
	{
		LOGGER.trace("getRuleByRuleId({})", ruleId);
		final Rule rule = this.rulesServices.getRuleByRuleId(ruleId);
		return Response.ok().entity(rule).build();
	}


}
