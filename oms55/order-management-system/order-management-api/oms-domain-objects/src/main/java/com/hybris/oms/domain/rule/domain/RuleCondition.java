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

import com.hybris.oms.domain.rule.custom.ChangeOrderStatusRuleCondition;

import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * The Class RuleCondition.
 */
@XmlSeeAlso(ChangeOrderStatusRuleCondition.class)
public abstract class RuleCondition extends RuleElement
{
	private static final long serialVersionUID = 4217033661209326928L;

	@Override
	public String getKind()
	{
		return "Condition";
	}

}
