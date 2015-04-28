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
package com.hybris.oms.domain.rule.domain;

import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
import com.hybris.oms.domain.rule.custom.ChangeOrderStatusRule;
import com.hybris.oms.domain.rule.enums.Operator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * The Class Rule.
 */
@XmlSeeAlso(ChangeOrderStatusRule.class)
@XmlRootElement(name = "rule")
public abstract class Rule extends PropertyAwareEntityDto
{
	private static final long serialVersionUID = 8413364805909243038L;

	private String accountIds;
	private Operator conditionOperator = Operator.OR;
	private String description;
	private Operator eligibilityOperator = Operator.OR;
	private boolean enabled;
	private Date endDate;
	private Date lastModifiedDate;
	private String name;
	private List<RuleAction> ruleActions = new ArrayList<>();
	private List<RuleCondition> ruleConditions = new ArrayList<>();
	private String ruleId;
	private Date startDate;

	/**
	 * Add an action to the rule.
	 * 
	 * @param ruleAction the rule action
	 */
	public void addAction(final RuleAction ruleAction)
	{
		this.ruleActions.add(ruleAction);
	}

	/**
	 * Adds a condition to the set of conditions.
	 * 
	 * @param condition the condition
	 */
	public void addCondition(final RuleCondition condition)
	{
		this.ruleConditions.add(condition);
	}

	/**
	 * Gets the account ids.
	 * 
	 * @return the account ids
	 */
	public String getAccountIds()
	{
		return this.accountIds;
	}

	/**
	 * Gets the operator (AND/OR) if there are multiple conditions.
	 * 
	 * @return the condition operator
	 */
	public Operator getConditionOperator()
	{
		return this.conditionOperator;
	}

	/**
	 * Get the description of this rule.
	 * 
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Gets the operator (AND/OR) if there are multiple eligibility conditions.
	 * 
	 * @return the eligibility operator
	 */
	public Operator getEligibilityOperator()
	{
		return this.eligibilityOperator;
	}

	/**
	 * Get the end date. After the end date, the rule will no longer be applied.
	 * 
	 * @return the end date
	 */
	public Date getEndDate()
	{
		if (this.endDate == null)
		{
			return null;
		}
		return new Date(this.endDate.getTime());
	}

	/**
	 * Gets the last modified date.
	 * 
	 * @return the last modified date
	 */
	public Date getLastModifiedDate()
	{
		if (this.lastModifiedDate == null)
		{
			return null;
		}
		return new Date(this.lastModifiedDate.getTime());
	}

	/**
	 * Get the name of this rule.
	 * 
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Get the actions associated with this rule.
	 * 
	 * @return the rule actions
	 */
	public List<RuleAction> getRuleActions()
	{
		return this.ruleActions;
	}

	/**
	 * Get the conditions associated with this rule.
	 * 
	 * @return the rule conditions
	 */
	public List<RuleCondition> getRuleConditions()
	{
		return this.ruleConditions;
	}

	/**
	 * Gets the rule id.
	 * 
	 * @return the rule id
	 */
	public String getRuleId()
	{
		return this.ruleId;
	}

	/**
	 * Get the starting date that this rule can be applied.
	 * 
	 * @return the start date
	 */
	public Date getStartDate()
	{
		if (this.startDate == null)
		{
			return null;
		}
		return new Date(this.startDate.getTime());
	}

	/**
	 * Checks if is enabled.
	 * 
	 * @return true, if is enabled
	 */
	public boolean isEnabled()
	{
		return this.enabled;
	}

	/**
	 * Sets the account ids.
	 * 
	 * @param accountIds the new account ids
	 */
	public void setAccountIds(final String accountIds)
	{
		this.accountIds = accountIds;
	}

	/**
	 * Sets the operator (AND/OR) if there are multiple eligibility conditions.
	 * 
	 * @param conditionOperator the new condition operator
	 */
	public void setConditionOperator(final Operator conditionOperator)
	{
		this.conditionOperator = conditionOperator;
	}

	/**
	 * Set the description of the rule.
	 * 
	 * @param description the new description
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * Sets the operator (AND/OR) if there are multiple eligibility conditions.
	 * 
	 * @param eligibilityOperator the new eligibility operator
	 */
	public void setEligibilityOperator(final Operator eligibilityOperator)
	{
		this.eligibilityOperator = eligibilityOperator;
	}

	/**
	 * Sets the enabled.
	 * 
	 * @param enabled the new enabled
	 */
	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * Set the end date.
	 * 
	 * @param endDate the new end date
	 */
	public void setEndDate(final Date endDate)
	{
		if (endDate == null)
		{
			this.endDate = null;
		}
		else
		{
			this.endDate = new Date(endDate.getTime());
		}
	}

	/**
	 * Sets the last modified date.
	 * 
	 * @param lastModifiedDate the new last modified date
	 */
	public void setLastModifiedDate(final Date lastModifiedDate)
	{
		if (lastModifiedDate == null)
		{
			this.lastModifiedDate = null;
		}
		else
		{

			this.lastModifiedDate = new Date(lastModifiedDate.getTime());
		}
	}

	/**
	 * Set the name of the rule.
	 * 
	 * @param name the new name
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Set the actions of this rule.
	 * 
	 * @param actions the new rule actions
	 */
	public void setRuleActions(final List<RuleAction> actions)
	{
		this.ruleActions = actions;
	}

	/**
	 * Set the conditions of this rule.
	 * 
	 * @param conditions the new rule conditions
	 */
	public void setRuleConditions(final List<RuleCondition> conditions)
	{
		this.ruleConditions = conditions;
	}

	/**
	 * Sets the rule id.
	 * 
	 * @param ruleId the new rule id
	 */
	public void setRuleId(final String ruleId)
	{
		this.ruleId = ruleId;
	}

	/**
	 * Set the starting date that this rule can be applied.
	 * 
	 * @param startDate the new start date
	 */
	public void setStartDate(final Date startDate)
	{
		if (startDate == null)
		{
			this.startDate = null;
		}
		else
		{
			this.startDate = new Date(startDate.getTime());
		}
	}

	@Override
	public String getId()
	{
		return ruleId;
	}

}
