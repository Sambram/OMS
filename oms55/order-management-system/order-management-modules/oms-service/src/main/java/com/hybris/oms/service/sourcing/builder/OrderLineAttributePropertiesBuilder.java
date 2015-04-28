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

import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;


/**
 * Strategy for populating properties from {@link OrderLineAttributeData}.
 */
public class OrderLineAttributePropertiesBuilder implements PropertiesBuilder<OrderLineData>
{

	@Override
	public Map<String, Object> addProperties(final OrderLineData source, final Map<String, Object> properties)
	{
		Map<String, Object> result = properties;
		if (source != null && CollectionUtils.isNotEmpty(source.getOrderLineAttributes()))
		{
			if (result == null)
			{
				result = new HashMap<>();
			}
			for (final OrderLineAttributeData attr : source.getOrderLineAttributes())
			{
				result.put(attr.getAttributeId(), attr.getDescription());
			}
		}
		return result;
	}

}
