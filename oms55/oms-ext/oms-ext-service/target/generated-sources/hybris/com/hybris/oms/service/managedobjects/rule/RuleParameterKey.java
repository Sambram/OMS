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
 
package com.hybris.oms.service.managedobjects.rule;

import com.hybris.kernel.api.HybrisEnumValue;

/**
 * Generated enumeration RuleParameterKey declared at extension rule.
 * <p/>
 * .
 */
public enum RuleParameterKey implements HybrisEnumValue
{
	/**
	 * Generated enumeration value for ACTION_INVENTORY_STATUS declared at extension rule.
	 * <p/>
	 	 * .
	 	 */
	ACTION_INVENTORY_STATUS ("ACTION_INVENTORY_STATUS")
		,
		/**
	 * Generated enumeration value for CONDITION_ORDERLINE_PREVIOUS_STATUS declared at extension rule.
	 * <p/>
	 	 * .
	 	 */
	CONDITION_ORDERLINE_PREVIOUS_STATUS ("CONDITION_ORDERLINE_PREVIOUS_STATUS")
		,
		/**
	 * Generated enumeration value for ACTION_INVENTORY_STRATEGY declared at extension rule.
	 * <p/>
	 	 * .
	 	 */
	ACTION_INVENTORY_STRATEGY ("ACTION_INVENTORY_STRATEGY")
		,
		/**
	 * Generated enumeration value for CONDITION_ORDERLINE_CURRENT_STATUS declared at extension rule.
	 * <p/>
	 	 * .
	 	 */
	CONDITION_ORDERLINE_CURRENT_STATUS ("CONDITION_ORDERLINE_CURRENT_STATUS")
		,
		/**
	 * Generated enumeration value for ACTION_INVENTORY_POSITIVE declared at extension rule.
	 * <p/>
	 	 * .
	 	 */
	ACTION_INVENTORY_POSITIVE ("ACTION_INVENTORY_POSITIVE")
		;
		 
	/**<i>Generated type code constant.</i>*/
	public static final String _TYPECODE = "RuleParameterKey";
	
	
	/** The code of this enumeration.*/
	private final String code;
	
	/**
	 * Creates a new enumeration value for this enumeration type.
	 *  
	 * @param code the enumeration value code
	 */
	private RuleParameterKey (final String code)
	{
		this.code = code.intern();
	}
	
	/**
	 * Gets the code of this enumeration value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enumeration value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return getClass().getSimpleName();
	}
}
