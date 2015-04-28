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
package com.hybris.oms.service.ats.impl;

import com.hybris.oms.service.ats.StatusRealm;


/**
 * Represents an operand within an ATS formula.
 */
public class Operand
{
	private final boolean negativeSign;
	private final StatusRealm realm;
	private final String statusCode;

	public Operand(final boolean negativeSign, final StatusRealm realm, final String statusCode)
	{
		this.negativeSign = negativeSign;
		this.realm = realm;
		this.statusCode = statusCode;
	}

	public boolean isNegativeSign()
	{
		return this.negativeSign;
	}

	public StatusRealm getStatusRealm()
	{
		return this.realm;
	}

	public String getStatusCode()
	{
		return this.statusCode;
	}

	/**
	 * Calculates the value of a single operand.
	 * 
	 * @return calculated value or <tt>null</tt> if no aggregated values are available and the previous value was
	 *         <tt>null</tt>.
	 */
	public Integer calculate(final String sku, final String locationId, final KeyBuilder keyBuilder,
			final CalculationContext context)
	{
		Integer result = null;
		final String key = keyBuilder.getKey(locationId, sku, realm, statusCode);
		final Integer aggValue = context.getAggregateValues().get(key);
		if (aggValue != null)
		{
			int opValue = aggValue.intValue();
			if (negativeSign)
			{
				opValue = -opValue;
			}
			result = Integer.valueOf(opValue);
		}
		return result;
	}



}
