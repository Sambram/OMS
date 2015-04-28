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

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;


/**
 * Represents the configured {@link Threshold} per location.
 */
public class LocationThreshold
{
	private static final int HUNDRED_PERCENT = 100;

	private final Map<String, Threshold> values = new HashMap<>();

	public void addThreshold(final String locationId, final Threshold threshold)
	{
		this.values.put(locationId, threshold);
	}

	/**
	 * Calculates ats with threshold for a given ats value and locationId.
	 */
	public int calculateAtsWithThreshold(final int ats, final String locationId)
	{
		int result = ats;
		final Threshold threshold = values.get(locationId);
		Preconditions.checkState(threshold != null, "Missing threshold for locationId: {} ", locationId);
		if (threshold.getValue() != 0)
		{
			if (threshold.isPercentage())
			{
				result = ats * (HUNDRED_PERCENT - threshold.getValue()) / HUNDRED_PERCENT;
			}
			else
			{
				result = ats - threshold.getValue();
			}
		}
		return result;
	}

}
