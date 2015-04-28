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
package com.hybris.oms.rest.integration.rule;

import com.hybris.oms.api.rule.RuleFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.domain.rule.domain.Rule;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * End-to-end integration tests.
 * This class should contain only methods that manipulate data (POST, PUT, DELETE).
 */
public class RulePostIntegrationTest extends RestClientIntegrationTest
{
	@Autowired
	private RuleFacade ruleFacade;

	@Test
	public void testCreateRule()
	{
		final RuleShort ruleShort = this.buildRuleShort();
		final Rule createdRule = this.ruleFacade.createRule(ruleShort);

		Assert.assertTrue(createdRule != null);
		
		ruleFacade.deleteRule(createdRule.getRuleId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testGetRuleByRuleId()
	{
		this.ruleFacade.getRuleByRuleId("Unknown");
	}
}
