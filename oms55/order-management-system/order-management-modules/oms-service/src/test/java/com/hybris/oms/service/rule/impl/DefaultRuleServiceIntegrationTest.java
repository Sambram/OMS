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
package com.hybris.oms.service.rule.impl;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.service.managedobjects.rule.Operator;
import com.hybris.oms.service.managedobjects.rule.RuleActionData;
import com.hybris.oms.service.managedobjects.rule.RuleConditionsData;
import com.hybris.oms.service.managedobjects.rule.RuleData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterKey;
import com.hybris.oms.service.rule.RuleService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultRuleServiceIntegrationTest
{
	private static final Boolean POSITIVE = Boolean.TRUE;
	private static final String CHANGE = "1";
	private static final String NEW_INVENTORY_STATUS = "NEW_INVENTORY_STATUS";
	private static final String RULE_ID = "1";
	private static final String STATUS_FROM = "STATUS_FROM";
	private static final String STATUS_TO = "STATUS_TO";
	private static final String STRATEGY = "OLQ_QUANTITY";

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Autowired
	private ImportService importService;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private RuleService ruleService;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/rule/test-rule-data-import.mcsv")[0]);
	}

	@Transactional
	@Test
	public void shouldCreateRule()
	{
		final RuleShort ruleShort = new RuleShort();
		ruleShort.setInventoryStatus(NEW_INVENTORY_STATUS);
		ruleShort.setOlqFromStatus(STATUS_FROM);
		ruleShort.setOlqToStatus(STATUS_TO);
		ruleShort.setChange(CHANGE);
		ruleShort.setPositive(Boolean.TRUE);
		ruleShort.setUpdateStrategy(STRATEGY);

		final RuleData rule = this.ruleService.createRule(ruleShort);

		Assert.assertNotNull(rule.getId());
		ruleAssertions(rule);

		final RuleConditionsData condition = conditionAssertions(rule);
		conditionParamsAssertions(condition);

		final RuleActionData action = actionAssertions(rule);
		actionParamsAssertions(action);
	}

	@Transactional
	@Test
	public void testCreateAndSaveRule()
	{
		final RuleData rule = this.ruleService.createAndSaveRule(STATUS_FROM, STATUS_TO, NEW_INVENTORY_STATUS, CHANGE);

		Assert.assertNotNull(rule.getId());
		ruleAssertions(rule);

		final RuleConditionsData condition = conditionAssertions(rule);
		conditionParamsAssertions(condition);

		final RuleActionData action = actionAssertions(rule);
		actionParamsAssertions(action);
	}

	@Transactional
	@Test(expected = DuplicateEntityException.class)
	public void testCreateDuplicateRule()
	{
		this.ruleService.createAndSaveRule(STATUS_FROM, STATUS_TO, NEW_INVENTORY_STATUS, CHANGE);
		this.ruleService.createAndSaveRule(STATUS_FROM, STATUS_TO, NEW_INVENTORY_STATUS, CHANGE);
	}

	@Transactional
	@Test
	public void testDeleteRuleByRuleId()
	{
		this.ruleService.deleteRuleByRuleId(RULE_ID);
		Assert.assertNotNull(this.ruleService.getRuleByRuleId(RULE_ID));
	}

	@Transactional
	@Test
	public void testGetRuleByRuleId()
	{
		final RuleData ruleData = this.ruleService.getRuleByRuleId(RULE_ID);
		Assert.assertNotNull(ruleData);
	}

	@Transactional
	@Test
	public void testFindAllRules()
	{
		final List<RuleData> rulesData = this.ruleService.findAllRules();
		Assert.assertNotNull(rulesData);
		Assert.assertEquals(3, rulesData.size());
		Assert.assertEquals(1, rulesData.get(0).getRuleActions().size());
		Assert.assertEquals(1, rulesData.get(0).getRuleConditions().size());
		Assert.assertEquals(3, rulesData.get(0).getRuleActions().iterator().next().getParameters().size());
		Assert.assertEquals(2, rulesData.get(0).getRuleConditions().iterator().next().getParameters().size());
	}

	private void actionParamsAssertions(final RuleActionData action)
	{
		final List<RuleParameterData> actionParams = action.getParameters();
		Assert.assertEquals(3, actionParams.size());

		Assert.assertEquals(getActionParamByKey(actionParams, RuleParameterKey.ACTION_INVENTORY_STATUS), NEW_INVENTORY_STATUS);
		Assert.assertEquals(getActionParamByKey(actionParams, RuleParameterKey.ACTION_INVENTORY_STRATEGY), STRATEGY);
		Assert.assertEquals(Boolean.parseBoolean(getActionParamByKey(actionParams, RuleParameterKey.ACTION_INVENTORY_POSITIVE)),
				POSITIVE);
	}

	private String getActionParamByKey(final List<RuleParameterData> params, final RuleParameterKey key)
	{
		String value = null;
		for (final RuleParameterData param : params)
		{
			if (param.getKey().equals(key))
			{
				value = param.getValue();
				break;
			}
		}
		return value;
	}

	private RuleActionData actionAssertions(final RuleData rule)
	{
		final List<RuleActionData> actions = rule.getRuleActions();
		Assert.assertEquals(1, actions.size());
		final RuleActionData action = actions.get(0);
		Assert.assertEquals(1, action.getSequenceNr());
		Assert.assertEquals(rule, action.getRule());
		Assert.assertEquals(1, action.getSalience());
		return action;
	}

	private void conditionParamsAssertions(final RuleConditionsData condition)
	{
		final List<RuleParameterData> conditionParams = condition.getParameters();
		Assert.assertEquals(2, conditionParams.size());
		int previousIndex = 1;
		int currentIndex = 0;
		if (RuleParameterKey.CONDITION_ORDERLINE_PREVIOUS_STATUS.equals(conditionParams.get(0).getKey()))
		{
			previousIndex = 0;
			currentIndex = 1;
		}
		Assert.assertEquals(conditionParams.get(previousIndex).getKey(), RuleParameterKey.CONDITION_ORDERLINE_PREVIOUS_STATUS);
		Assert.assertEquals(conditionParams.get(previousIndex).getValue(), STATUS_FROM);
		Assert.assertEquals(conditionParams.get(currentIndex).getKey(), RuleParameterKey.CONDITION_ORDERLINE_CURRENT_STATUS);
		Assert.assertEquals(conditionParams.get(currentIndex).getValue(), STATUS_TO);
	}

	private RuleConditionsData conditionAssertions(final RuleData rule)
	{
		final List<RuleConditionsData> conditions = rule.getRuleConditions();
		Assert.assertEquals(1, conditions.size());
		final RuleConditionsData condition = conditions.get(0);
		Assert.assertEquals(1, condition.getSequenceNr());
		Assert.assertEquals(rule, condition.getRule());
		return condition;
	}

	private void ruleAssertions(final RuleData rule)
	{
		Assert.assertNotNull(rule.getRuleId());
		Assert.assertEquals(rule.getRuleId(), rule.getName());
		Assert.assertEquals(Operator.OR, rule.getConditionOperator());
		Assert.assertEquals(Operator.OR, rule.getEligibilityOperator());
		Assert.assertTrue(rule.isEnabled());
	}

}
