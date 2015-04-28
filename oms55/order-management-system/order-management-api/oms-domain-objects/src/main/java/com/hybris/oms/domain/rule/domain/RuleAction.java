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

import com.hybris.oms.domain.rule.custom.ChangeOrderStatusRuleAction;

import javax.xml.bind.annotation.XmlSeeAlso;


/** Abstract base class for Rule Actions. */
@XmlSeeAlso(ChangeOrderStatusRuleAction.class)
public abstract class RuleAction extends RuleElement
{
	private static final long serialVersionUID = 532219564838819896L;

	private int salience;

	@Override
	public String getKind()
	{
		return "Action";
	}

	/**
	 * Get the salience value for this rule. The higher the salience, the
	 * earlier the actions of the rule will be executed relative to other rules.
	 * 
	 * @return the salience
	 */
	public int getSalience()
	{
		return this.salience;
	}

	/**
	 * Set the salience value.
	 * 
	 * @param salience the new salience
	 */
	public void setSalience(final int salience)
	{
		this.salience = salience;
	}

}
