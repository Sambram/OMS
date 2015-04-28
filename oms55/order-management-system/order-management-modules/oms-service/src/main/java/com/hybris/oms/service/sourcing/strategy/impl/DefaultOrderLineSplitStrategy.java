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
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;
import com.hybris.oms.service.sourcing.context.PropertiesBuilderSupport;
import com.hybris.oms.service.sourcing.strategy.OrderLineSplitStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Default implementation of {@link OrderLineSplitStrategy}. Will create a single {@link SourcingLine} for every
 * orderLine. The orderLineId is used as lineId.
 */
public class DefaultOrderLineSplitStrategy implements OrderLineSplitStrategy
{

	private List<PropertiesBuilder<OrderLineData>> propertiesBuilders;

	@Override
	public List<SourcingLine> getSourcingLines(final OrderLineData orderLine)
	{
		String country = null;
		final OrderData order = orderLine.getMyOrder();
		if (order.getShippingAddress() != null)
		{
			country = order.getShippingAddress().getCountryIso3166Alpha2Code();
		}

		final Map<String, Object> properties = PropertiesBuilderSupport.buildProperties(orderLine, propertiesBuilders);

		return Collections.singletonList(new SourcingLine(orderLine.getSkuId(), orderLine.getQuantityUnassignedValue(), orderLine
				.getOrderLineId(), orderLine.getPickupStoreId(), orderLine.getLocationRoles(), country, orderLine.getOrderLineId(),
				properties));
	}

	public void setPropertiesBuilders(final List<PropertiesBuilder<OrderLineData>> propertiesBuilders)
	{
		this.propertiesBuilders = propertiesBuilders;
	}

}
