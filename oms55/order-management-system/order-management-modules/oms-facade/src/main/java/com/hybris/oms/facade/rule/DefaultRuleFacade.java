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
import com.hybris.oms.api.rule.RuleFacade;
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.domain.rule.domain.Rule;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.managedobjects.rule.RuleData;
import com.hybris.oms.service.rule.RuleService;
import com.hybris.oms.service.rule.strategy.InventoryUpdateStrategyFactory;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;


/**
 * Rules facade implementation. This is where the POJO domain objects are
 * converted to managed objects and the lower level services are called to
 * perform the CRUD operations on managed objects.
 * 
 */
public class DefaultRuleFacade implements RuleFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultRuleFacade.class);

	private RuleService ruleService;
	private InventoryUpdateStrategyFactory inventoryUpdateStrategyFactory;

	private Converter<RuleData, Rule> ruleConverter;
	private Converters converters;
	private Validator<RuleShort> ruleValidator;
	private FailureHandler entityValidationHandler;

	@Override
	@Transactional(readOnly = true)
	public List<Rule> findAllRules()
	{
		LOG.trace("findAllRules");
		final List<RuleData> rulesData = this.ruleService.findAllRules();
		return this.converters.convertAll(rulesData, this.ruleConverter);
	}

	@Override
	public Set<String> findAllInventoryUpdateStrategies()
	{
		LOG.trace("findAllInventoryUpdateStrategies");
		return inventoryUpdateStrategyFactory.getStrategyKeys();
	}

	@Override
	@Transactional
	public Rule createRule(final RuleShort rule)
	{
		LOG.trace("createRule");
		ruleValidator.validate("Rule", rule, entityValidationHandler);
		final RuleData ruleData = this.ruleService.createRule(rule);
		return this.ruleConverter.convert(ruleData);
	}

	@Override
	@Transactional
	public void deleteRule(final String ruleId)
	{
		LOG.trace("deleteRuleByRuleId");
		this.ruleService.deleteRuleByRuleId(ruleId);
	}

	@Override
	@Transactional(readOnly = true)
	public Rule getRuleByRuleId(final String ruleId)
	{
		final RuleData ruleData = this.ruleService.getRuleByRuleId(ruleId);
		return this.ruleConverter.convert(ruleData);
	}

	@Required
	public void setRuleService(final RuleService ruleService)
	{
		this.ruleService = ruleService;
	}

	@Required
	public void setRuleConverter(final Converter<RuleData, Rule> ruleConverter)
	{
		this.ruleConverter = ruleConverter;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setRuleValidator(final Validator<RuleShort> ruleValidator)
	{
		this.ruleValidator = ruleValidator;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	@Required
	public void setInventoryUpdateStrategyFactory(final InventoryUpdateStrategyFactory inventoryUpdateStrategyFactory)
	{
		this.inventoryUpdateStrategyFactory = inventoryUpdateStrategyFactory;
	}

	protected RuleService getRuleService()
	{
		return ruleService;
	}

	protected Converter<RuleData, Rule> getRuleConverter()
	{
		return ruleConverter;
	}

	protected Converters getConverters()
	{
		return converters;
	}

	protected Validator<RuleShort> getRuleValidator()
	{
		return ruleValidator;
	}

	protected FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	protected InventoryUpdateStrategyFactory getInventoryUpdateStrategyFactory()
	{
		return inventoryUpdateStrategyFactory;
	}
}
