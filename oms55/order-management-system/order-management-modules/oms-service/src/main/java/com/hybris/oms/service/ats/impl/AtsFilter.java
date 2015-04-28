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

import java.util.Collections;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Provides filtering functionality on locationIds, skus and atsIds.
 */
public class AtsFilter
{
	private final Set<String> skus;
	private final Set<String> locationIds;
	private final Set<String> atsIds;

	public AtsFilter(final Set<String> skus, final Set<String> locationIds, final Set<String> atsIds)
	{
		this.skus = skus;
		this.locationIds = locationIds;
		this.atsIds = atsIds;
	}

	/**
	 * Filter atsId.
	 * 
	 * @return <tt>true</tt> if this atsId is contained in the filter conditions
	 */
	public boolean filterAtsId(final String atsId)
	{
		return filter(atsId, atsIds);
	}

	/**
	 * Filter sku.
	 * 
	 * @return <tt>true</tt> if this sku is contained in the filter conditions
	 */
	public boolean filterSku(final String sku)
	{
		return filter(sku, skus);
	}

	/**
	 * Filter location.
	 * 
	 * @return <tt>true</tt> if this location is contained in the filter conditions
	 */
	public boolean filterLocationId(final String locationId)
	{
		return filter(locationId, locationIds);
	}

	/**
	 * Retrieves a {@link Set} of skus to be filtered, <tt>never</tt> null.
	 */
	public Set<String> getSkus()
	{
		return skus == null ? Collections.<String>emptySet() : Collections.unmodifiableSet(skus);
	}

	/**
	 * Retrieves a {@link Set} of locationIds to be filtered, <tt>never</tt> null.
	 */
	public Set<String> getLocationIds()
	{
		return locationIds == null ? Collections.<String>emptySet() : Collections.unmodifiableSet(locationIds);
	}

	/**
	 * Retrieves a {@link Set} of atsIds to be filtered, <tt>never</tt> null.
	 */
	public Set<String> getAtsIds()
	{
		return atsIds == null ? Collections.<String>emptySet() : Collections.unmodifiableSet(atsIds);
	}

	protected boolean filter(final String value, final Set<String> filter)
	{
		return CollectionUtils.isEmpty(filter) || filter.contains(value);
	}

}
