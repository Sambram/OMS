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

import com.hybris.kernel.api.AggregationService;
import com.hybris.oms.service.ats.AtsQuantityAggregate;
import com.hybris.oms.service.ats.AtsUnassignedQuantity;
import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.ats.StatusRealmRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * Helper class to retrieve aggregates for ATS from {@link AggregationService}.
 */
public class AggregateHelper
{
	private AggregationService aggregationService;

	private StatusRealmRegistry realmRegistry;

	/**
	 * Retrieves all aggregates of the given class using filters on SKUs and locations.
	 * 
	 * @return {@link Collection} of aggregates
	 */
	public Collection<? extends AtsQuantityAggregate> retrieveAggregates(final AtsFilter atsFilter, final StatusRealm realm,
			final boolean globalAts)
	{
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(atsFilter.getSkus()),
				"retrieving aggregates without filterSkus not allowed");
		final Collection<AtsQuantityAggregate> result = new ArrayList<>();
		final Class<? extends AtsQuantityAggregate> aggClazz = realmRegistry.getAggregateClassForRealm(realm, globalAts);
		final String singleLocation = getSingleValue(atsFilter.getLocationIds());
		for (final String sku : atsFilter.getSkus())
		{
			this.retrieveAggregates(aggClazz, result, sku, singleLocation);
		}
		return result;
	}

	/**
	 * Retrieves all aggregates of type {@link AtsUnassignedQuantity} using a filter on SKUs.
	 * 
	 * @return {@link Collection} of aggregates
	 */
	public Collection<AtsUnassignedQuantity> retrieveQuantityUnassigned(final Set<String> filterSkus)
	{
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(filterSkus), "retrieving aggregates without filterSkus not allowed");
		final Collection<AtsUnassignedQuantity> quantities = new ArrayList<>();
		final Class<? extends AtsUnassignedQuantity> aggClass = realmRegistry.getAggClassForUnassignedQuantity();
		for (final String sku : filterSkus)
		{
			quantities.addAll(this.aggregationService.getAggregates(aggClass, sku));
		}
		return quantities;
	}

	protected void retrieveAggregates(final Class<? extends AtsQuantityAggregate> clazz, final Collection<AtsQuantityAggregate> result,
			final String sku, final String location)
	{
		if (location == null)
		{
			result.addAll(this.aggregationService.getAggregates(clazz, sku));
		}
		else
		{
			result.addAll(this.aggregationService.getAggregates(clazz, sku, location));
		}
	}

	protected String getSingleValue(final Set<String> values)
	{
		return values != null && values.size() == 1 ? values.iterator().next() : null;
	}

	@Required
	protected AggregationService getAggregationService()
	{
		return aggregationService;
	}

	public void setAggregationService(final AggregationService aggregationService)
	{
		this.aggregationService = aggregationService;
	}

	@Required
	protected StatusRealmRegistry getRealmRegistry()
	{
		return realmRegistry;
	}

	public void setRealmRegistry(final StatusRealmRegistry realmRegistry)
	{
		this.realmRegistry = realmRegistry;
	}

}
