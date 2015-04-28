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
package com.hybris.oms.service.sourcing.builder;

import com.hybris.oms.service.sourcing.context.SourcingLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Preconditions;


/**
 * Allows to create a {@link Comparator} chain from a comparator descriptor.
 */
public class SourcingComparatorFactory implements InitializingBean
{
	private static final String DEFAULT_COMPARATOR_ID = "_default_";

	private static final String DESCRIPTOR_SEPARATOR = ",";

	private Map<String, Comparator<SourcingLocation>> comparatorMap;

	private Comparator<SourcingLocation> defaultComparator;

	@Override
	public void afterPropertiesSet()
	{
		if (this.comparatorMap == null || this.comparatorMap.isEmpty())
		{
			throw new IllegalArgumentException("comparatorMap is mandatory but it is not set");
		}
		final Comparator<SourcingLocation> comparator = this.comparatorMap.get(DEFAULT_COMPARATOR_ID);
		Preconditions.checkArgument(comparator != null, "default Comparator not configured in comparatorMap");
		this.defaultComparator = comparator;
	}

	/**
	 * Create a composite {@link Comparator} from the comparator descriptor containing comparatorIds separated by comma.
	 * The configured default comparator will always be appended as last {@link Comparator}
	 * 
	 * @param sequenceDescriptor a descriptor the sequence to rank locations. Consists of comparator ids separated by
	 *           comma, e.g. "DISTANCE,ATS". Must not be blank and contain only known comparator ids.
	 * @return a composite {@link Comparator}
	 * @throws IllegalArgumentException if the sequenceDescriptor is blank or the comparator id is unknown.
	 */
	public Comparator<SourcingLocation> createComparator(final String sequenceDescriptor)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(sequenceDescriptor), "sourcingDescriptor cannot be blank");
		final String[] comparatorIds = sequenceDescriptor.split(DESCRIPTOR_SEPARATOR);
		final List<Comparator<SourcingLocation>> comparators = new ArrayList<>();
		for (final String comparatorId : comparatorIds)
		{
			final Comparator<SourcingLocation> comparator = this.comparatorMap.get(comparatorId);
			Preconditions.checkArgument(comparator != null, "Unknown comparatorId %s found in %s", comparatorId, sequenceDescriptor);
			comparators.add(comparator);
		}
		comparators.add(this.defaultComparator);
		return new SourcingLocation.CompositeComparator<>(comparators, sequenceDescriptor);
	}

	public void setComparatorMap(final Map<String, Comparator<SourcingLocation>> comparatorMap)
	{
		this.comparatorMap = comparatorMap;
	}
	
	/**
	 * Get the comparator map.
	 * 
	 * @return unmodifiable version of the comparator map
	 */
	public Map<String, Comparator<SourcingLocation>> getComparatorMap() 
	{
		return Collections.unmodifiableMap(this.comparatorMap);
	}
	
	public static String getDefaultComparatorId() 
	{
		return DEFAULT_COMPARATOR_ID;
	}
}
