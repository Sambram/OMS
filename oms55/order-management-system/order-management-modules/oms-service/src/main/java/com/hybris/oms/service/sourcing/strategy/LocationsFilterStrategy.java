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
package com.hybris.oms.service.sourcing.strategy;

import java.util.Collection;
import java.util.Set;


/**
 * Interface to modify a {@link Set} of locationIds in a chain. Used to filter locations for sourcing.
 */
public interface LocationsFilterStrategy<S, T extends Collection<?>>
{

	/**
	 * Extension point to modify a {@link Set} of locationIds in a chain.
	 * 
	 * @param source source object
	 * @param filterLocationIds locationIds that will be used for filtering ATS and location queries.
	 * @return <tt>null</tt> if other filters should be applied
	 */
	T filter(S source, Set<String> filterLocationIds);

}
