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
package com.hybris.oms.api.rule;


import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.domain.rule.domain.Rule;

import java.util.List;
import java.util.Set;


/**
 * The Interface RulesServices Api.
 */
public interface RuleFacade
{

	/**
	 * Get all rules that belong to a given rule set.
	 * 
	 * @category OMS-UI
	 * 
	 * @return list of rules
	 */
	List<Rule> findAllRules();

	/**
	 * Get all of the available inventory update strategies.
	 * 
	 * @return the keys representing the available strategies
	 */
	Set<String> findAllInventoryUpdateStrategies();

	/**
	 * Get rule by id and ruleSet name.
	 * 
	 * @param ruleId the id to fetch
	 * @return rule
	 * @throws EntityNotFoundException if rule with the provided rule id is not found
	 */
	Rule getRuleByRuleId(final String ruleId) throws EntityNotFoundException;

	/**
	 * Create a new rule and associated it with the given rule set.
	 * 
	 * @category OMS-UI
	 * 
	 * @param rule the rules to create.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           rule.change must not be blank.
	 *           <dd>
	 *           rule.change must be number.
	 *           <dd>
	 *           rule.olqFromStatus must be not be blank.
	 *           <dd>
	 *           rule.olqToStatus must exists in OMS.
	 *           <dd>
	 *           rule.olqFromStatus must be not be blank.
	 *           <dd>
	 *           rule.olqToStatus must exists in OMS.
	 *           <dd>
	 *           rule.inventoryStatus must be not be blank.
	 *           <dd>
	 *           rule.inventoryStatus must exists in OMS.
	 * @return the newly created rule
	 * @throws EntityValidationException - if preconditions are not met.
	 * @throws DuplicateEntityException - if the rule already exists
	 */
	Rule createRule(RuleShort rule) throws EntityValidationException, DuplicateEntityException;

	/**
	 * Delete a rule by rule id.
	 * 
	 * @category OMS-UI
	 * 
	 * @param ruleId the rule id to delete
	 * @throws EntityNotFoundException if rule with the provided ruleId is not found
	 */
	void deleteRule(final String ruleId) throws EntityNotFoundException;

}
