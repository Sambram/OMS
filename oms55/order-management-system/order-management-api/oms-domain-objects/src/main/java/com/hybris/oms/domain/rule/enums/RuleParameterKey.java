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
package com.hybris.oms.domain.rule.enums;

/**
 * The Enum RuleParameterKey.
 */
public enum RuleParameterKey
{

	/**
	 * Condition / keys.
	 */
	CONDITION_ORDERLINE_PREVIOUS_STATUS,

	/** The condition orderline current status. */
	CONDITION_ORDERLINE_CURRENT_STATUS,

	/**
	 * Action / keys.
	 */
	ACTION_INVENTORY_STATUS,

	/**
	 * The action inventory increment value.
	 * 
	 * @deprecated Should use ACTION_INVENTORY_POSITIVE instead.
	 */
	ACTION_INVENTORY_INCREMENT_VALUE,

	/** Whether the change is positive or not. */
	ACTION_INVENTORY_POSITIVE,

	/** Strategy key */
	ACTION_INVENTORY_STRATEGY;

}
