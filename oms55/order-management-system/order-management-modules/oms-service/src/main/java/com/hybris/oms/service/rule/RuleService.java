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
package com.hybris.oms.service.rule;

import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.rule.RuleData;

import java.util.List;


/**
 * The Interface RulesService.
 */
public interface RuleService
{

	/**
	 * Create and save rule from input parameters.
	 * 
	 * @deprecated Use {@link RuleService#createRule(RuleShort)} instead.
	 * 
	 * @param olqFromStatus
	 * @param olqToStatus
	 * @param inventoryStatus
	 * @param change
	 * @return the new rule
	 */
	@Deprecated
	RuleData createAndSaveRule(final String olqFromStatus, final String olqToStatus, final String inventoryStatus,
			final String change);

	/**
	 * Create a new rule from a {@link RuleShort}
	 * 
	 * @param rule - the DTO representation of the new rule to be created
	 * @return the new rule
	 */
	RuleData createRule(final RuleShort rule);

	/**
	 * Delete a rule by rule id.
	 * 
	 * @param ruleId the rule id
	 */
	void deleteRuleByRuleId(String ruleId);

	/**
	 * Finds existing rules that fulfills input parameters and executes proper operations.
	 * 
	 * @param previousStatus
	 *           previous olq status
	 * @param currentStatus
	 *           current olq status
	 * @param olq - the {@link OrderLineQuantityData} that is changing status
	 * 
	 */
	void executeOLQStatusChange(final String previousStatus, final String currentStatus, final OrderLineQuantityData olq);

	/**
	 * Find all rules.
	 * 
	 * @return list of rules (managed objects)
	 */
	List<RuleData> findAllRules();

	/**
	 * Get the rule by rule id.
	 * 
	 * @param ruleId the rule id
	 * @return rule matching criteria
	 */
	RuleData getRuleByRuleId(final String ruleId);

}
