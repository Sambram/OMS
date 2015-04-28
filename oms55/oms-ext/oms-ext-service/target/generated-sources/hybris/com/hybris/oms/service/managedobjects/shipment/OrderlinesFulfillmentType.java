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
 
package com.hybris.oms.service.managedobjects.shipment;

import com.hybris.kernel.api.HybrisEnumValue;

/**
 * Generated enumeration OrderlinesFulfillmentType declared at extension shipment.
 * <p/>
 * .
 */
public enum OrderlinesFulfillmentType implements HybrisEnumValue
{
	/**
	 * Generated enumeration value for REGULAR declared at extension shipment.
	 * <p/>
	 	 * .
	 	 */
	REGULAR ("REGULAR")
		,
		/**
	 * Generated enumeration value for BACK_ORDERLINE declared at extension shipment.
	 * <p/>
	 	 * .
	 	 */
	BACK_ORDERLINE ("BACK_ORDERLINE")
		,
		/**
	 * Generated enumeration value for PRE_ORDERLINE declared at extension shipment.
	 * <p/>
	 	 * .
	 	 */
	PRE_ORDERLINE ("PRE_ORDERLINE")
		;
		 
	/**<i>Generated type code constant.</i>*/
	public static final String _TYPECODE = "OrderlinesFulfillmentType";
	
	
	/** The code of this enumeration.*/
	private final String code;
	
	/**
	 * Creates a new enumeration value for this enumeration type.
	 *  
	 * @param code the enumeration value code
	 */
	private OrderlinesFulfillmentType (final String code)
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
