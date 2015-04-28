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

import java.util.Map;


/**
 * Interface to add properties from a source in a chain.
 */
public interface PropertiesBuilder<S>
{

	/**
	 * Extension point to add properties from a given source.
	 * 
	 * @param source source object to retrieve properties from
	 * @param properties properties to be enriched. Can be <code>null</code>
	 * @return enriched properties, can be <tt>null</tt> if no properties were added and the parameter was <tt>null</tt>
	 */
	Map<String, Object> addProperties(S source, Map<String, Object> properties);

}
