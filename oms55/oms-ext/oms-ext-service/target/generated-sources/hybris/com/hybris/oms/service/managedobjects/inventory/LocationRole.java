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
 
package com.hybris.oms.service.managedobjects.inventory;

import com.hybris.kernel.api.HybrisEnumValue;

/**
 * Generated enumeration LocationRole declared at extension inventory.
 * <p/>
 * .
 */
public enum LocationRole implements HybrisEnumValue
{
	/**
	 * Generated enumeration value for PICKUP declared at extension inventory.
	 * <p/>
	 	 * .
	 	 */
	PICKUP ("PICKUP")
		,
		/**
	 * Generated enumeration value for SHIPPING declared at extension inventory.
	 * <p/>
	 	 * .
	 	 */
	SHIPPING ("SHIPPING")
		;
		 
	/**<i>Generated type code constant.</i>*/
	public static final String _TYPECODE = "LocationRole";
	
	
	/** The code of this enumeration.*/
	private final String code;
	
	/**
	 * Creates a new enumeration value for this enumeration type.
	 *  
	 * @param code the enumeration value code
	 */
	private LocationRole (final String code)
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
