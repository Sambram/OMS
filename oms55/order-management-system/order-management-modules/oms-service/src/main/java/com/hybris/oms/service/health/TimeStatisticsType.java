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
package com.hybris.oms.service.health;

/**
 * Stores possible numbers of hours for time statistics, together with their identifier names.
 */
public enum TimeStatisticsType
{
	/**
	 * Last hour.
	 */
	HOUR("1"), //
	/**
	 * Last day (=24 hours).
	 */
	DAY("24");

	private String timeCode;

	private TimeStatisticsType(final String timeCode)
	{
		this.timeCode = timeCode;
	}

	public String getTimeCode()
	{
		return timeCode;
	}
}
