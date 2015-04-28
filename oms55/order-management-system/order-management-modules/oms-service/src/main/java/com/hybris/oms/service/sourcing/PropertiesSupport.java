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
package com.hybris.oms.service.sourcing;

import java.util.Collections;
import java.util.Map;


/**
 * Support for additional properties used for sourcing.
 */
public class PropertiesSupport
{
	private final Map<String, Object> properties;

	public PropertiesSupport(final Map<String, Object> properties)
	{
		this.properties = properties == null ? Collections.<String, Object>emptyMap() : Collections
				.<String, Object>unmodifiableMap(properties);
	}

	/**
	 * Returns an immutable {@link Map} of properties. Never <tt>null</tt>
	 * 
	 * @return an immutable {@link Map} of properties
	 */
	public Map<String, Object> getProperties()
	{
		return properties;
	}

}
