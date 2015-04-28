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
package com.hybris.oms.facade.conversion.impl.rule;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.rule.domain.Rule;
import com.hybris.oms.domain.rule.domain.RuleAction;
import com.hybris.oms.domain.rule.domain.RuleCondition;
import com.hybris.oms.domain.rule.enums.Operator;
import com.hybris.oms.service.managedobjects.rule.RuleData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class RuleConverterTest
{
	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private Converter<RuleData, Rule> ruleConverter;

	private RuleTestHelpers testUtils;

	@Before
	public void setUp()
	{
		this.testUtils = new RuleTestHelpers();
		this.testUtils.createObjects(this.persistenceManager);
	}

	@Test
	@Transactional
	public void testConvertingRule()
	{
		// given
		final RuleData source = this.testUtils.getRuleData();

		// when
		final Rule target = this.ruleConverter.convert(source);

		// then
		source.getModifiedTime();
		assertValidRule(target);
	}

	@Test
	@Transactional
	public void testConvertingRuleWithFlush()
	{
		// given
		final RuleData source = this.testUtils.getRuleData();
		this.persistenceManager.flush();

		// when
		final Rule rule = this.ruleConverter.convert(source);

		// then
		assertValidRule(rule);
	}

	private void assertValidRule(final Rule rule)
	{
		Assert.assertEquals(null, rule.getAccountIds());
		Assert.assertEquals(Operator.OR, rule.getConditionOperator());
		Assert.assertEquals(RuleTestHelpers.RULE_DESCRIPTION, rule.getDescription());
		Assert.assertEquals(Operator.AND, rule.getEligibilityOperator());
		Assert.assertEquals(RuleTestHelpers.RULE_END_DATE, rule.getEndDate());
		Assert.assertEquals(null, rule.getLastModifiedDate());
		Assert.assertEquals(RuleTestHelpers.RULE_NAME, rule.getName());

		Assert.assertEquals(2, rule.getRuleActions().size());

		final List<RuleAction> actions = rule.getRuleActions();
		Collections.sort(actions, new Comparator<RuleAction>()
		{
			@Override
			public int compare(final RuleAction o1, final RuleAction o2)
			{
				return Integer.valueOf(o1.getSalience()).compareTo(Integer.valueOf(o2.getSalience()));
			}
		});
		Assert.assertEquals(RuleTestHelpers.RA1_SEQUENCE_NR, actions.get(0).getSequenceNr());
		Assert.assertEquals(RuleTestHelpers.RA2_SEQUENCE_NR, actions.get(1).getSequenceNr());

		Assert.assertEquals(2, rule.getRuleConditions().size());
		final List<RuleCondition> conditions = rule.getRuleConditions();
		Collections.sort(conditions, new Comparator<RuleCondition>()
		{
			@Override
			public int compare(final RuleCondition o1, final RuleCondition o2)
			{
				return Integer.valueOf(o1.getSequenceNr()).compareTo(Integer.valueOf(o2.getSequenceNr()));
			}
		});
		Assert.assertEquals(RuleTestHelpers.RC1_SEQUENCE_NR, conditions.get(0).getSequenceNr());
		Assert.assertEquals(RuleTestHelpers.RC2_SEQUENCE_NR, conditions.get(1).getSequenceNr());

		Assert.assertEquals(RuleTestHelpers.RULE_ID, rule.getRuleId());
		Assert.assertEquals(RuleTestHelpers.RULE_START_DATE, rule.getStartDate());
	}
}
