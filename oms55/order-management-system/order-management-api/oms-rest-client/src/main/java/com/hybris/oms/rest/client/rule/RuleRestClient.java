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
package com.hybris.oms.rest.client.rule;

import com.hybris.commons.client.RestResponseException;
import com.hybris.oms.api.rule.RuleFacade;
import com.hybris.oms.domain.JaxbBaseSet;
import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.domain.rule.domain.Rule;
import com.hybris.oms.domain.rule.domain.RuleList;
import com.hybris.oms.rest.client.web.DefaultRestClient;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RuleRestClient extends DefaultRestClient implements RuleFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleRestClient.class);

	@Override
	public List<Rule> findAllRules()
	{
		try
		{
			return getClient().call("rules").get(RuleList.class).getResult().getList();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public void deleteRule(final String ruleId)
	{
		LOGGER.trace("deleteRuleByRuleId: ruleId={}", ruleId);
		try
		{
			getClient().call("rules/%s", ruleId).delete().result();
		}
		catch (final RestResponseException e)
		{
			e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Rule createRule(final RuleShort rule)
	{
		LOGGER.trace("createRule");
		try
		{
			return getClient().call("rules").post(Rule.class, rule).result();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Rule getRuleByRuleId(final String ruleId)
	{
		LOGGER.trace("getRuleByRuleId: ruleId={}", ruleId);
		try
		{
			return getClient().call("rules/%s", ruleId).get(Rule.class).getResult();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}

	@Override
	public Set<String> findAllInventoryUpdateStrategies()
	{
		LOGGER.trace("findAllInventoryUpdateStrategies");
		try
		{
			@SuppressWarnings("unchecked")
			final JaxbBaseSet<String> optionsSet = getClient().call("inventoryupdatestrategies").get(JaxbBaseSet.class).result();
			return optionsSet.getSet();
		}
		catch (final RestResponseException e)
		{
			return e.unwrap(HybrisSystemException.class);
		}
	}
}
