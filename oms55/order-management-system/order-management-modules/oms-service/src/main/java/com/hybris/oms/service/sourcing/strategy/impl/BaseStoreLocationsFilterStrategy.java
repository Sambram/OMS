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
package com.hybris.oms.service.sourcing.strategy.impl;

import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.sourcing.strategy.LocationsFilterStrategy;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;



/**
 * Filter which takes locationIds from {@link OrderData#getBaseStore()}.
 */
public class BaseStoreLocationsFilterStrategy implements LocationsFilterStrategy<OrderData, Set<String>>
{

	@Override
	public Set<String> filter(final OrderData source, final Set<String> filterLocationIds)
	{
		Set<String> result = null;
		final BaseStoreData baseStore = source.getBaseStore();
		if (baseStore != null && CollectionUtils.isNotEmpty(baseStore.getStockroomLocations()))
		{
			final Set<String> locationIds = new HashSet<>();
			for (final StockroomLocationData location : baseStore.getStockroomLocations())
			{
				if (CollectionUtils.isEmpty(filterLocationIds) || filterLocationIds.contains(location.getLocationId()))
				{
					locationIds.add(location.getLocationId());
					result = locationIds;
				}
			}
		}
		return result;
	}

}
