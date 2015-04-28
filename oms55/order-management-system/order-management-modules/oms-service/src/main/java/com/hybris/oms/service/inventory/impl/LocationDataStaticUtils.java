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
package com.hybris.oms.service.inventory.impl;

import com.hybris.oms.service.common.AddressStaticUtils;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;


public final class LocationDataStaticUtils
{
	private LocationDataStaticUtils()
	{
		// do not instantiate
	}

	/**
	 * Check if the location's address has valid geocodes.
	 * 
	 * @param location
	 * @return true of location has address with valid coordinates, false otherwise
	 */
	public static boolean hasValidAddressGeocodes(final StockroomLocationData location)
	{
		return ((location.getAddress() != null) && (AddressStaticUtils.hasValidCoordinates(location.getAddress())));
	}

	/**
	 * Checks for valid coordinates.
	 * 
	 * @return true, if successful
	 */
	public static boolean hasValidCoordinates(final StockroomLocationData location)
	{
		if (location.getAddress() == null)
		{
			return false;
		}
		return AddressStaticUtils.hasValidCoordinates(location.getAddress());
	}
}
