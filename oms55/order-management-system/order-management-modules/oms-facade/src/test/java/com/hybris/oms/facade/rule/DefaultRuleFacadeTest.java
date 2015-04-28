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
package com.hybris.oms.facade.rule;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.domain.rule.custom.ChangeOrderStatusRule;
import com.hybris.oms.domain.rule.domain.Rule;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.managedobjects.rule.RuleData;
import com.hybris.oms.service.rule.RuleService;
import com.hybris.oms.service.rule.strategy.InventoryUpdateStrategyFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@RunWith(MockitoJUnitRunner.class)
public class DefaultRuleFacadeTest
{
	private static final String RULE_ID = "1";
	private static final String STRATEGY_KEY_1 = "STRATEGY_KEY_1";
	private static final String STRATEGY_KEY_2 = "STRATEGY_KEY_2";

	@InjectMocks
	private final DefaultRuleFacade rulesFacade = new DefaultRuleFacade();

	@Mock
	private RuleService mockRuleService;
	@Mock
	private InventoryUpdateStrategyFactory inventoryUpdateStrategyFactory;
	@Mock
	private Converters converters;
	@Mock
	private Converter<RuleData, Rule> ruleConverter;
	@Mock
	private Validator<RuleShort> ruleShortValidator;
	@Mock
	private FailureHandler entityValidationHandler;

	@Mock
	private RuleData ruleData;

	private final RuleShort ruleShort = new RuleShort();
	private final Rule rule = new ChangeOrderStatusRule();
	private final List<RuleData> ruleDataList = new ArrayList<>();

	@Before
	public void setUp()
	{

		this.ruleDataList.add(this.ruleData);

		Mockito.when(mockRuleService.findAllRules()).thenReturn(this.ruleDataList);
		Mockito.when(mockRuleService.createRule(ruleShort)).thenReturn(ruleData);
		Mockito.when(mockRuleService.getRuleByRuleId(RULE_ID)).thenReturn(ruleData);
		Mockito.doNothing().when(mockRuleService).deleteRuleByRuleId(Mockito.anyString());

		Mockito.when(inventoryUpdateStrategyFactory.getStrategyKeys()).thenReturn(Sets.newHashSet(STRATEGY_KEY_1, STRATEGY_KEY_2));

		Mockito.when(converters.convertAll(ruleDataList, ruleConverter)).thenReturn(Arrays.asList(this.rule));
		Mockito.when(ruleConverter.convert(ruleData)).thenReturn(rule);
		Mockito.doNothing().when(ruleShortValidator)
				.validate(Mockito.anyString(), Mockito.any(RuleShort.class), Mockito.any(FailureHandler.class));
		Mockito.doNothing().when(entityValidationHandler).execute(Mockito.any(ValidationContext.class));
	}

	@Test
	public void shouldFindAllRules()
	{
		final List<Rule> rulesTest = this.rulesFacade.findAllRules();

		// Asserts
		Assert.assertNotNull(rulesTest);
		Assert.assertEquals(1, rulesTest.size());
		Assert.assertEquals(this.rule, rulesTest.get(0));

		// Mock verifications
		Mockito.verify(this.mockRuleService, Mockito.times(1)).findAllRules();
		Mockito.verify(this.converters, Mockito.times(1)).convertAll(this.ruleDataList, this.ruleConverter);
	}

	@Test
	public void shouldCreateRule()
	{
		final Rule ruleTest = this.rulesFacade.createRule(ruleShort);

		Assert.assertNotNull(ruleTest);
		Assert.assertEquals(rule, ruleTest);

		Mockito.verify(this.ruleConverter, Mockito.times(1)).convert(this.ruleData);
		Mockito.verify(this.mockRuleService, Mockito.times(1)).createRule(ruleShort);
	}

	@Test
	public void shouldDeleteRuleByRuleId()
	{
		this.rulesFacade.deleteRule(RULE_ID);
		Mockito.verify(this.mockRuleService, Mockito.times(1)).deleteRuleByRuleId(RULE_ID);
	}

	@Test
	public void shouldGetRuleByRuleId()
	{
		final Rule ruleTest = this.rulesFacade.getRuleByRuleId(RULE_ID);
		Assert.assertEquals(rule, ruleTest);
	}

	@Test
	public void shouldFindAllInventoryUpdateStrategies()
	{
		final Set<String> strategies = rulesFacade.findAllInventoryUpdateStrategies();
		Assert.assertEquals(2, strategies.size());
	}
}
