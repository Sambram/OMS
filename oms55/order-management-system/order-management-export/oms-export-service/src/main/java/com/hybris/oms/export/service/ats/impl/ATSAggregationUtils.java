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
package com.hybris.oms.export.service.ats.impl;

import com.hybris.kernel.api.KernelEvent;

import java.util.Map;

import org.apache.commons.collections.MapUtils;


/**
 * Convenience methods for determining the types of aggregation as well as
 * extracting aggregate attributes based on the group String[] array.
 */
public final class ATSAggregationUtils
{
	private ATSAggregationUtils()
	{
		// avoid instantiation
	}

	/**
	 * Get a valid map of values from the event. If both previous and current values are valid, then this will return the
	 * current values.
	 *
	 * @param event
	 * @return values map
	 */
	public static Map<String, Object> getDefaultValues(final KernelEvent event)
	{
		Map<String, Object> values = null;
		final Map<String, Object> previousValues = event.getPreviousValues();
		if (MapUtils.isNotEmpty(previousValues))
		{
			values = previousValues;
		}
		final Map<String, Object> currentValues = event.getCurrentValues();
		if (MapUtils.isNotEmpty(currentValues))
		{
			values = currentValues;
		}
		return values;
	}
}
