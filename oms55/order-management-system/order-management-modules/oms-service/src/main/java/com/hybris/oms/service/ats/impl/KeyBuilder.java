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
package com.hybris.oms.service.ats.impl;

import com.hybris.oms.service.ats.StatusRealm;



/**
 * Builder for keys used internally within the ats component.
 */
public class KeyBuilder
{
	private static final String SEPERATOR = "|";
	private static final String REALM_SEPERATOR = ":";

	/**
	 * Retrieves an internal key for an ATS term.
	 */
	public String getKey(final String location, final String sku, final StatusRealm realm, final String status)
	{
		final StringBuilder builder = new StringBuilder();
		final String locKey = getLocationKey(location);
		if (locKey != null)
		{
			builder.append(locKey).append(SEPERATOR);
		}
		builder.append(sku).append(SEPERATOR);
		builder.append(realm.getPrefix()).append(REALM_SEPERATOR).append(status);
		return builder.toString();
	}

	/**
	 * Retrieves an internal key for a location.
	 */
	public String getLocationKey(final String locationId)
	{
		return locationId == null || locationId.equals(AtsConstants.GLOBAL_LOC) ? null : locationId;
	}

}
