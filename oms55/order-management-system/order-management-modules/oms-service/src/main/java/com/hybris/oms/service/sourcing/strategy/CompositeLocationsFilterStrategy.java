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
import java.util.List;
import java.util.Set;


/**
 * Composite filter strategy processing a chain of {@link LocationsFilterStrategy} instances. Returns a
 * {@link Collection} of
 * locations to be used for sourcing or <tt>null</tt>, if no locations are available
 */
public class CompositeLocationsFilterStrategy<S, T extends Collection<?>> implements LocationsFilterStrategy<S, T>
{
	private List<LocationsFilterStrategy<S, T>> locationFilters;

	@Override
	public T filter(final S source, final Set<String> filterLocationIds)
	{
		T result = null;
		if (locationFilters != null)
		{
			for (final LocationsFilterStrategy<S, T> filter : locationFilters)
			{
				result = filter.filter(source, filterLocationIds);
				if (result != null)
				{
					break;
				}
			}
		}
		return result;
	}

	protected List<LocationsFilterStrategy<S, T>> getLocationFilters()
	{
		return locationFilters;
	}

	public void setLocationFilters(final List<LocationsFilterStrategy<S, T>> locationFilters)
	{
		this.locationFilters = locationFilters;
	}

}
