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
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.strategy.LineActionsStrategy;

import java.util.Map.Entry;


/**
 * Default implementation of {@link LineActionsStrategy}.
 */
public class DefaultLineActionsStrategy implements LineActionsStrategy
{

	@Override
	public void processLineActions(final OrderData order, final SourcingResult sourcingResult)
	{
		for (final Entry<String, String> entry : sourcingResult.getLineActions().entrySet())
		{
			for (final OrderLineData orderLine : order.getOrderLines())
			{
				if (orderLine.getOrderLineId().equals(entry.getKey()))
				{
					orderLine.setOrderLineStatus(entry.getValue());
				}
			}
		}
	}

}
