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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.rule.Operator;
import com.hybris.oms.service.managedobjects.rule.RuleActionData;
import com.hybris.oms.service.managedobjects.rule.RuleConditionsData;
import com.hybris.oms.service.managedobjects.rule.RuleData;
import com.hybris.oms.service.managedobjects.rule.RuleElementData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterKey;
import com.hybris.oms.service.rule.RuleService;
import com.hybris.oms.service.rule.strategy.InventoryUpdateDto;
import com.hybris.oms.service.rule.strategy.InventoryUpdateStrategy;
import com.hybris.oms.service.rule.strategy.InventoryUpdateStrategyFactory;
import com.hybris.oms.service.service.AbstractHybrisService;


/**
 * The Class DefaultRulesService.
 */
public class DefaultRuleService extends AbstractHybrisService implements RuleService
{
	private InventoryService inventoryService;
	private InventoryUpdateStrategyFactory inventoryUpdateStrategyFactory;

	@Override
	public RuleData createRule(final RuleShort rule)
	{
		final String ruleId = rule.getOlqFromStatus() + "_" + "olqToStatus" + "_" + rule.getInventoryStatus();
		final RuleData newRule = this.createRule(ruleId, ruleId, Operator.OR, Operator.OR, true);
		final RuleConditionsData ruleConditions = this.createCondition(1, newRule);
		final RuleActionData ruleAction = this.createAction(1, newRule, 1);

		this.createRuleParameter(RuleParameterKey.CONDITION_ORDERLINE_PREVIOUS_STATUS, rule.getOlqFromStatus(), ruleConditions);
		this.createRuleParameter(RuleParameterKey.CONDITION_ORDERLINE_CURRENT_STATUS, rule.getOlqToStatus(), ruleConditions);
		this.createRuleParameter(RuleParameterKey.ACTION_INVENTORY_STATUS, rule.getInventoryStatus(), ruleAction);
		this.createRuleParameter(RuleParameterKey.ACTION_INVENTORY_STRATEGY, rule.getUpdateStrategy(), ruleAction);
		this.createRuleParameter(RuleParameterKey.ACTION_INVENTORY_POSITIVE, Boolean.toString(rule.isPositive()), ruleAction);

		try
		{
			this.getPersistenceManager().flush();
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateEntityException("Rule already exists.", e);
		}

		return newRule;
	}

	@Override
	public RuleData createAndSaveRule(final String olqFromStatus, final String olqToStatus, final String inventoryStatus,
			final String change)
	{
		final RuleShort rule = new RuleShort();
		rule.setOlqFromStatus(olqFromStatus);
		rule.setOlqToStatus(olqToStatus);
		rule.setInventoryStatus(inventoryStatus);
		rule.setChange(change);
		rule.setUpdateStrategy("OLQ_QUANTITY");
		rule.setPositive(Integer.parseInt(change) >= 0 ? Boolean.TRUE : Boolean.FALSE);
		return createRule(rule);
	}

	@Override
	public void deleteRuleByRuleId(final String ruleId)
	{
		final RuleData rule = this.getRuleByRuleId(ruleId);
		this.getPersistenceManager().remove(rule);
	}

	@Override
	public void executeOLQStatusChange(final String previousStatus, final String currentStatus, final OrderLineQuantityData olq)
	{
		final List<RuleData> rules = this.findAllRules();
		for (final RuleData rule : rules)
		{
			final boolean isConditionFulfilled = matchRuleConditions(rule.getRuleConditions(), rule.getConditionOperator(),
					previousStatus, currentStatus);

			if (isConditionFulfilled)
			{
				for (final RuleActionData ruleAction : rule.getRuleActions())
				{
					String strategyKey = null;
					String inventoryStatus = null;
					boolean positive = Boolean.TRUE;
					for (final RuleParameterData ruleParameter : ruleAction.getParameters())
					{
						if (RuleParameterKey.ACTION_INVENTORY_STATUS.equals(ruleParameter.getKey()))
						{
							inventoryStatus = ruleParameter.getValue();
						}
						if (RuleParameterKey.ACTION_INVENTORY_POSITIVE.equals(ruleParameter.getKey()))
						{
							positive = Boolean.valueOf(ruleParameter.getValue());
						}
						if (RuleParameterKey.ACTION_INVENTORY_STRATEGY.equals(ruleParameter.getKey()))
						{
							strategyKey = ruleParameter.getValue();
						}
					}

					// Get params
					final String locationId = olq.getStockroomLocationId();
					final String sku = olq.getOrderLine().getSkuId();

					// Calculate quantity by which to update the inventory
					final InventoryUpdateStrategy strategy = inventoryUpdateStrategyFactory.getStrategy(strategyKey);
					final int updateQuantity = strategy.calculateInventoryUpdateQuantity(new InventoryUpdateDto(locationId, sku, olq
							.getQuantityValue(), positive));

					// Updating inventory at the stockroom location level (DEFAULT_BIN)
					try
					{
						this.inventoryService.updateCurrentItemQuantityIncremental(locationId, InventoryServiceConstants.DEFAULT_BIN,
								sku, inventoryStatus, updateQuantity);
					}
					catch (final EntityNotFoundException e)
					{
						this.inventoryService.createUpdateItemQuantity(sku, locationId, InventoryServiceConstants.DEFAULT_BIN,
								inventoryStatus, olq.getQuantityUnitCode(), updateQuantity, null);
					}
				}
			}
		}
	}

	@Override
	public RuleData getRuleByRuleId(final String ruleId)
	{
		try
		{
			return this.getPersistenceManager().getByIndex(RuleData.UX_RULES_RULEID, ruleId);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Rule not found. Rule Id: " + ruleId + ".", e);
		}
	}

	@Override
	public List<RuleData> findAllRules()
	{
		return this.getPersistenceManager().createCriteriaQuery(RuleData.class).resultList();
	}

	protected RuleActionData createAction(final int sequenceNumber, final RuleData rule, final int salience)
	{
		final RuleActionData ruleAction = this.getPersistenceManager().create(RuleActionData.class);
		ruleAction.setSequenceNr(sequenceNumber);
		ruleAction.setRule(rule);
		ruleAction.setSalience(salience);

		return ruleAction;
	}

	protected RuleConditionsData createCondition(final int sequenceNumber, final RuleData rule)
	{
		final RuleConditionsData ruleConditions = this.getPersistenceManager().create(RuleConditionsData.class);
		ruleConditions.setSequenceNr(sequenceNumber);
		ruleConditions.setRule(rule);

		return ruleConditions;
	}

	protected RuleData createRule(final String id, final String name, final Operator conditionOperator,
			final Operator eligibilityOperator, final boolean enabled)
	{
		final RuleData rule = this.getPersistenceManager().create(RuleData.class);
		rule.setRuleId(id);
		rule.setName(name);
		rule.setConditionOperator(conditionOperator);
		rule.setEligibilityOperator(eligibilityOperator);
		rule.setEnabled(enabled);

		return rule;
	}

	protected RuleParameterData createRuleParameter(final RuleParameterKey key, final String value,
			final RuleElementData ruleElement)
	{
		final RuleParameterData ruleParameter = this.getPersistenceManager().create(RuleParameterData.class);
		ruleParameter.setKey(key);
		ruleParameter.setValue(value);
		ruleParameter.setRuleElement(ruleElement);

		return ruleParameter;
	}

	protected boolean matchRuleConditions(final List<RuleConditionsData> ruleConditions, final Operator conditionOperator,
			final String previousStatus, final String currentStatus)
	{
		final List<Boolean> conditionResults = new ArrayList<>();
		for (final RuleConditionsData ruleCondition : ruleConditions)
		{
			boolean previousMet = false;
			boolean currentMet = false;
			for (final RuleParameterData ruleParameter : ruleCondition.getParameters())
			{
				if (RuleParameterKey.CONDITION_ORDERLINE_PREVIOUS_STATUS.equals(ruleParameter.getKey()))
				{
					previousMet = previousStatus.equals(ruleParameter.getValue());
				}
				if (RuleParameterKey.CONDITION_ORDERLINE_CURRENT_STATUS.equals(ruleParameter.getKey()))
				{
					currentMet = currentStatus.equals(ruleParameter.getValue());
				}
			}
			conditionResults.add(previousMet && currentMet);
		}

		if (conditionResults.isEmpty())
		{
			return true;
		}

		if (Operator.OR.equals(conditionOperator))
		{
			return conditionResults.contains(Boolean.TRUE);
		}
		else
		{
			return !conditionResults.contains(Boolean.FALSE);
		}
	}

	public InventoryService getInventoryService()
	{
		return inventoryService;
	}

	protected InventoryUpdateStrategyFactory getInventoryUpdateStrategyFactory()
	{
		return inventoryUpdateStrategyFactory;
	}

	@Required
	public void setInventoryService(final InventoryService inventryService)
	{
		this.inventoryService = inventryService;
	}

	@Required
	public void setInventoryUpdateStrategyFactory(final InventoryUpdateStrategyFactory inventoryUpdateStrategyFactory)
	{
		this.inventoryUpdateStrategyFactory = inventoryUpdateStrategyFactory;
	}

}
