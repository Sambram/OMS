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
package com.hybris.oms.service.sourcing.context;


import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Utility class to include {@link PropertiesBuilder}.
 */
public final class PropertiesBuilderSupport
{
	private PropertiesBuilderSupport()
	{
		// static utility
	}

	public static <S> Map<String, Object> buildProperties(final S source, final PropertiesBuilder<S> builder)
	{
		Map<String, Object> result = null;
		if (builder != null)
		{
			result = buildProperties(source, Collections.singletonList(builder));
		}
		return result;
	}

	public static <S> Map<String, Object> buildProperties(final S source, final List<PropertiesBuilder<S>> builders)
	{
		Map<String, Object> result = null;
		if (builders != null)
		{
			for (final PropertiesBuilder<S> builder : builders)
			{
				result = builder.addProperties(source, result);
			}
		}
		return result;
	}

}
