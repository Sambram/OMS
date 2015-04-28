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

/**
 * Utility methods to calculate the distance between 2 coordinates.
 */
public final class DistanceUtils
{
	/**
	 * Radius of the Earth in kilometers.
	 */
	private static final double RADIUS = 6372.8d;

	private DistanceUtils()
	{
		// do not instantiate
	}

	/**
	 * Implementation of the Haversine formula to calculate the distance between two coordinates on the Earth's surface.
	 * 
	 * @param coordinates1 first coordinates, may not be <tt>null</tt>
	 * @param coordinates2 second coordinates, may not be <tt>null</tt>
	 * @return distance between two coordinates on Earth
	 * @throws IllegalArgumentException if any of the parameters cannot be parsed as double
	 */
	public static double haversineFormula(final RadianCoordinates coordinates1, final RadianCoordinates coordinates2)
	{
		final double dLatitude = coordinates2.getLatitude() - coordinates1.getLatitude();
		final double dLongitude = coordinates2.getLongitude() - coordinates1.getLongitude();
		final double sinLatitude = Math.sin(dLatitude / 2);
		final double sinLongitude = Math.sin(dLongitude / 2);

		final double valueA = sinLatitude * sinLatitude + sinLongitude * sinLongitude * Math.cos(coordinates1.getLatitude())
				* Math.cos(coordinates2.getLatitude());
		final double valueC = 2 * Math.asin(Math.sqrt(valueA));
		return RADIUS * valueC;
	}
}
