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

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


/**
 * Represents radian coordinates in the service layer.
 */
public class RadianCoordinates implements Serializable
{
	private static final long serialVersionUID = 3594831075895043551L;

	private static final Logger LOG = LoggerFactory.getLogger(RadianCoordinates.class);

	private static final int LATITUDE_MIN = 90;
	private static final int LATITUDE_MAX = -90;
	private static final int LONGITUDE_MIN = -180;
	private static final int LONGITUDE_MAX = 180;

	private final double latitude;

	private final double longitude;

	public RadianCoordinates(final double latitude, final double longitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude()
	{
		return this.latitude;
	}

	public double getLongitude()
	{
		return this.longitude;
	}

	/**
	 * Creates a new {@link RadianCoordinates} instance from the given parameters.
	 * 
	 * @param latitude has to be in between -90 and 90.
	 * @param longitude has to be betwenn -180 and 180.
	 * @return {RadianCoordinates}
	 * @throws IllegalArgumentException latitude is not between -90 and 90 or longitude is not between -180 and 180
	 */
	public static RadianCoordinates fromDegrees(final double latitude, final double longitude)
	{
		Preconditions.checkArgument(latitude >= LATITUDE_MAX && latitude <= LATITUDE_MIN);
		Preconditions.checkArgument(longitude >= LONGITUDE_MIN && longitude <= LONGITUDE_MAX);
		return new RadianCoordinates(Math.toRadians(latitude), Math.toRadians(longitude));
	}

	/**
	 * Creates a new {@link RadianCoordinates} instance from the given parameters. If any of them is <tt>null</tt>,
	 * returns <tt>null</tt>.
	 * 
	 * @param latitude has to be in between -90 and 90 or <tt>null</tt>.
	 * @param longitude has to be betwenn -180 and 180 or <tt>null</tt>.
	 * @return {RadianCoordinates}
	 * @throws IllegalArgumentException latitude is not between -90 and 90 or longitude is not between -180 and 180
	 */
	public static RadianCoordinates fromOptionalDegrees(final Double latitude, final Double longitude)
	{
		RadianCoordinates result = null;
		if (latitude != null && longitude != null)
		{
			try
			{
				result = fromDegrees(latitude.doubleValue(), longitude.doubleValue());
			}
			catch (final IllegalArgumentException e)
			{
				LOG.warn("Invalid location coordinates {}/{} due to {}", new Object[]{latitude, longitude, e.toString()});
			}
		}
		return result;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("latitude", this.latitude).append("longitude", this.longitude).toString();
	}

}
