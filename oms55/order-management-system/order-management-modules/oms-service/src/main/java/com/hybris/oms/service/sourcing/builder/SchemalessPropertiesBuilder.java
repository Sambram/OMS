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

import com.hybris.kernel.api.PropertyAware;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;


/**
 * Strategy for populating properties from an instance of {@link PropertyAware}.
 */
public class SchemalessPropertiesBuilder implements PropertiesBuilder<PropertyAware>
{

	@Override
	public Map<String, Object> addProperties(final PropertyAware source, final Map<String, Object> properties)
	{
		Map<String, Object> result = properties;
		if (source != null && MapUtils.isNotEmpty(source.getAllProperties()))
		{
			if (result == null)
			{
				result = new HashMap<>();
			}
			result.putAll(source.getAllProperties());
		}
		return result;
	}

}
