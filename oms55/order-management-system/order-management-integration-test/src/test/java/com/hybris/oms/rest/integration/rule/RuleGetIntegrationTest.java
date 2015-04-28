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
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.domain.rule.domain.Rule;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * End-to-end integration tests.
 * This class should contain only methods to retrieve data (GET).
 */
public class RuleGetIntegrationTest extends RestClientIntegrationTest
{

	@Autowired
	private RuleFacade ruleFacade;

	private Rule createdRule;

	@Before
	public void setUp()
	{
		final RuleShort ruleShort = this.buildRuleShort();
		this.createdRule = this.ruleFacade.createRule(ruleShort);
	}

	@After
	public void tearDown()
	{
		this.ruleFacade.deleteRule(this.createdRule.getRuleId());
	}

	@Test
	public void testFindAllRules()
	{
		final List<Rule> rules = this.ruleFacade.findAllRules();
		Assert.assertTrue(rules != null);
		Assert.assertFalse(rules.isEmpty());
	}

	@Test
	public void testGetRuleByRuleId()
	{
		// Retrieve a single Rule for a given RuleSet name and Rule id
		final Rule rule = this.ruleFacade.getRuleByRuleId(this.createdRule.getRuleId());
		Assert.assertTrue(rule != null);
	}
}
