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

/**
 * Represents a percentage or absolute threshold.
 */
public class Threshold
{
	private final int value;

	private final boolean percentage;

	public Threshold(final int value, final boolean percentage)
	{
		this.value = value;
		this.percentage = percentage;
	}

	public int getValue()
	{
		return this.value;
	}

	public boolean isPercentage()
	{
		return this.percentage;
	}

}
