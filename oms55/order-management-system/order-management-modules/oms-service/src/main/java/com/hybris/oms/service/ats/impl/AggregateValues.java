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

import com.hybris.oms.service.ats.AtsQuantityAggregate;
import com.hybris.oms.service.ats.AtsUnassignedQuantity;

import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * Represents the aggregate values used for calculating ATS. This includes quantities retrieved from
 * {@link AtsQuantityAggregate} and unassigned quantity retrieved from {@link AtsUnassignedQuantity} for global ATS
 * calculations. For all local aggregates, the locationIds are provided.
 */
public class AggregateValues
{
	private final Set<String> skus;
	private final Set<String> locationIds;
	private final Map<String, Integer> aggValues;
	private final Map<String, Integer> quantityUnassigned;

	public AggregateValues(final Set<String> skus, final Set<String> locationIds, final Map<String, Integer> aggValues,
			final Map<String, Integer> quantityUnassigned)
	{
		this.locationIds = locationIds;
		this.skus = skus;
		this.aggValues = aggValues;
		this.quantityUnassigned = quantityUnassigned == null ? Collections.<String, Integer>emptyMap() : quantityUnassigned;
	}

	/**
	 * Returns the {@link Set} of locationIds of all aggregates.
	 */
	public Set<String> getLocationIds()
	{
		return Collections.unmodifiableSet(locationIds);
	}

	/**
	 * Returns the {@link Set} of skus of all aggregates.
	 */
	public Set<String> getSkus()
	{
		return Collections.unmodifiableSet(skus);
	}

	/**
	 * Returns the value of an aggregate for a specific key.
	 */
	public Integer get(final String key)
	{
		return aggValues.get(key);
	}

	/**
	 * Returns the unassigned quantity for a specific sku.
	 */
	public Integer getUnassignedQuantity(final String sku)
	{
		return quantityUnassigned.get(sku);
	}

}
