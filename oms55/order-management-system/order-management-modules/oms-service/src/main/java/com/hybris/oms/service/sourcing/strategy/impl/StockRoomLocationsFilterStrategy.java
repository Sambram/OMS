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

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.sourcing.strategy.LocationsFilterStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Filter which takes locationIds from {@link OrderData#getStockroomLocationIds()}.
 */
public class StockRoomLocationsFilterStrategy implements LocationsFilterStrategy<OrderData, Set<String>>
{

	@Override
	public Set<String> filter(final OrderData source, final Set<String> filterLocationIds)
	{
		Set<String> result = null;
		final List<String> stockRoomLocationIds = source.getStockroomLocationIds();
		if (CollectionUtils.isNotEmpty(stockRoomLocationIds))
		{
			final Set<String> locationIds = new HashSet<>();
			for (final String locationId : stockRoomLocationIds)
			{
				if (CollectionUtils.isEmpty(filterLocationIds) || filterLocationIds.contains(locationId))
				{
					locationIds.add(locationId);
					result = locationIds;
				}
			}
		}
		return result;
	}

}
