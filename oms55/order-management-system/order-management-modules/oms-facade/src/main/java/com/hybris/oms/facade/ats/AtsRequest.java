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
package com.hybris.oms.facade.ats;

import java.util.Set;


/**
 * DTO to hold all possible ATS request parameters.
 */
public class AtsRequest
{
	private Set<String> atsIds;
	private Set<String> skus;
	private Set<String> locations;

	public Set<String> getAtsIds()
	{
		return this.atsIds;
	}

	public void setAtsIds(final Set<String> atsIds)
	{
		this.atsIds = atsIds;
	}

	public Set<String> getSkus()
	{
		return this.skus;
	}

	public void setSkus(final Set<String> skus)
	{
		this.skus = skus;
	}

	public Set<String> getLocations()
	{
		return this.locations;
	}

	public void setLocations(final Set<String> locations)
	{
		this.locations = locations;
	}
}
