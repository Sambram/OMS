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
package com.hybris.oms.service.shipment.strategy.impl;

import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.shipment.strategy.OlqGroupingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Default implementation of {@link OlqGroupingStrategy}. This will group by
 * locationId, status and optionally pickupStoreId if usesPickupStoreId is set.
 */
public class DefaultOlqGroupingStrategy implements OlqGroupingStrategy
{

	@Override
	public List<List<OrderLineQuantityData>> groupOlqs(final List<OrderLineQuantityData> olqs)
	{
		final List<List<OrderLineQuantityData>> groupedOlqs = new ArrayList<>();
		final HashMap<String, Integer> groups = new HashMap<>();
		int currentGroupIndex = 0;

		for (final OrderLineQuantityData olq : olqs)
		{
			final String groupKey = makeKey(olq);
			Integer groupIndex = groups.get(groupKey);
			if (groupIndex == null)
			{
				groupIndex = Integer.valueOf(currentGroupIndex++);
				groups.put(groupKey, groupIndex);
				groupedOlqs.add(new ArrayList<OrderLineQuantityData>());
			}
			groupedOlqs.get(groupIndex).add(olq);
		}
		return groupedOlqs;
	}

	protected String makeKey(final OrderLineQuantityData olq)
	{
		final String pickupStoreId = olq.getOrderLine().getPickupStoreId();
		final String locationId = olq.getStockroomLocationId();
		final String statusCode = olq.getStatus().getStatusCode();
		
		if (pickupStoreId != null)
		{
			return String.format("%s|%s|%s", locationId, statusCode, pickupStoreId);
		}
		return String.format("%s|%s", locationId, statusCode);
	}

}
