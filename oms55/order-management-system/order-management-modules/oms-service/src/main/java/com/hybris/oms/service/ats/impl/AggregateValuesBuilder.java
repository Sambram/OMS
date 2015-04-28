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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.service.ats.AtsLocalQuantityAggregate;
import com.hybris.oms.service.ats.AtsQuantityAggregate;
import com.hybris.oms.service.ats.AtsUnassignedQuantity;
import com.hybris.oms.service.ats.StatusRealm;


/**
 * Builder for {@link AggregateValues}.
 */
public class AggregateValuesBuilder
{
	private AggregateHelper aggregateHelper;
	private KeyBuilder keyBuilder;

	/**
	 * Constructs a new {@link AggregateValues} instance. For this it retrieves aggregates using {@link AggregateHelper}
	 * which are filtered using {@link AtsFilter}. If globalAts is requested, additionally the quantity unassigned is
	 * retrieved from {@link AggregateHelper}. Keys for {@link AggregateValues} are constructed using {@link KeyBuilder}.
	 * For global ATS the key AtsConstants.GLOBAL_LOC is used.
	 * 
	 * @return a new instance of {@link AggregateValues}
	 */
	public AggregateValues build(final AtsFilter atsFilter, final boolean globalAts, Set<StatusRealm> statusRealms)
	{
		final Set<String> locationIds = new HashSet<>();
		final Set<String> skus = new HashSet<>();
		final Map<String, Integer> aggValues = new HashMap<>();
		for (final StatusRealm realm : statusRealms)
		{
			for (final AtsQuantityAggregate agg : aggregateHelper.retrieveAggregates(atsFilter, realm, globalAts))
			{
				String aggLocationId = null;
				if (agg instanceof AtsLocalQuantityAggregate)
				{
					aggLocationId = ((AtsLocalQuantityAggregate) agg).getLocationId();
				}
				if (atsFilter.filterSku(agg.getSku()) && (aggLocationId == null || atsFilter.filterLocationId(aggLocationId)))
				{
					if (aggLocationId != null)
					{
						locationIds.add(aggLocationId);
					}
					final String key = keyBuilder.getKey(aggLocationId, agg.getSku(), realm, agg.getStatusCode());

                    int previousValue = 0;
                    if(aggValues.get(key) != null){
                        previousValue = aggValues.get(key);
                    }

					aggValues.put(key, previousValue + Integer.valueOf(agg.getQuantity()));
					skus.add(agg.getSku());
				}
			}
		}
		Map<String, Integer> quantityUnassigned = null;
		if (globalAts)
		{
			locationIds.add(AtsConstants.GLOBAL_LOC);
			quantityUnassigned = retrieveQuantityUnassigned(atsFilter);
		}
		return buildValues(locationIds, skus, aggValues, quantityUnassigned);
	}

	/**
	 * Instantiates the immutable {@link AggregateValues} from the given parameters.
	 */
	protected AggregateValues buildValues(final Set<String> locationIds, final Set<String> skus,
			final Map<String, Integer> aggValues, final Map<String, Integer> quantityUnassigned)
	{
		return new AggregateValues(skus, locationIds, aggValues, quantityUnassigned);
	}

	/**
	 * Retrieves a {@link Map} of unassigned quantity per SKU.
	 * 
	 * @param atsFilter
	 * @return a map of unassigned quantity per SKU, never <tt>null</tt>
	 */
	protected Map<String, Integer> retrieveQuantityUnassigned(final AtsFilter atsFilter)
	{
		final Map<String, Integer> result = new HashMap<>();
		for (final AtsUnassignedQuantity quantity : this.aggregateHelper.retrieveQuantityUnassigned(atsFilter.getSkus()))
		{
			if (atsFilter.filterSku(quantity.getSku()) && quantity.getQuantityUnassigned() != 0)
			{
				result.put(quantity.getSku(), Integer.valueOf(quantity.getQuantityUnassigned()));
			}
		}
		return result;
	}

	@Required
	protected AggregateHelper getAggregateHelper()
	{
		return aggregateHelper;
	}

	public void setAggregateHelper(final AggregateHelper aggregateHelper)
	{
		this.aggregateHelper = aggregateHelper;
	}

	@Required
	protected KeyBuilder getKeyBuilder()
	{
		return keyBuilder;
	}

	public void setKeyBuilder(final KeyBuilder keyBuilder)
	{
		this.keyBuilder = keyBuilder;
	}
}
