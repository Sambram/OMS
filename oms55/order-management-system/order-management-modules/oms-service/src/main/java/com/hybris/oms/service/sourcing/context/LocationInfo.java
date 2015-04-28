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
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Preconditions;
import com.hybris.oms.service.sourcing.PropertiesSupport;
import com.hybris.oms.service.sourcing.builder.SourcingMatrixBuilder;


/**
 * Provides data retrieved from {@link com.hybris.oms.service.managedobjects.inventory.StockroomLocationData} for the
 * {@link SourcingMatrixBuilder}. This can then be sorted using a specific {@link java.util.Comparator}. The distance
 * will provide the distance from the store location to the location provided with the order. If no geo location data is
 * available either for the location or for the order, the distance is set to Double.MAX_VALUE. The priority is
 * configured in {@link com.hybris.oms.service.managedobjects.inventory.StockroomLocationData} as well as the
 * locationId.
 */
public class LocationInfo extends PropertiesSupport
{
	private final double distance;
	private final int priority;
	private final String locationId;
	private final Set<String> locationRoles;

	public LocationInfo(final String locationId)
	{
		this(0, locationId, Double.MAX_VALUE, null);
	}

	/**
	 * Creates a new {@link LocationInfo} instance.
	 * 
	 * @param priority priority of the location, lower means higher priority
	 * @param locationId id of the location, cannot be blank
	 * @throws IllegalArgumentException if distance <= 0 or locationId is blank
	 */
	public LocationInfo(final int priority, final String locationId)
	{
		this(priority, locationId, Double.MAX_VALUE, null);
	}

	/**
	 * Creates a new {@link LocationInfo} instance.
	 * 
	 * @param priority priority of the location, lower means higher priority
	 * @param locationId id of the location, cannot be blank
	 * @param distance distance in kilometers, has to be >= 0
	 * @throws IllegalArgumentException if distance <= 0 or locationId is blank
	 */
	public LocationInfo(final int priority, final String locationId, final double distance)
	{
		this(priority, locationId, distance, null);
	}

	public LocationInfo(final int priority, final String locationId, final double distance, final Set<String> locationRoles)
	{
		this(priority, locationId, distance, locationRoles, null);
	}

	/**
	 * Creates a new {@link LocationInfo} instance.
	 * 
	 * @param priority
	 *           priority of the location, lower means higher priority
	 * @param locationId
	 *           id of the location, cannot be blank
	 * @param distance
	 *           distance in kilometers, has to be >= 0
	 * @param locationRoles
	 *           location set of roles(pickup , shipping etc)
	 * @param properties
	 *           additional properties
	 * @throws IllegalArgumentException
	 *            if distance <= 0 or locationId is blank
	 */
	public LocationInfo(final int priority, final String locationId, final double distance, final Set<String> locationRoles,
			final Map<String, Object> properties)			throws IllegalArgumentException
	{
		super(properties);
		Preconditions.checkArgument(distance >= 0d, "The distance cannot be negative for locationId %s", locationId);
		Preconditions.checkArgument(StringUtils.isNotBlank(locationId), "The locationId cannot be blank");
		this.distance = distance;
		this.priority = priority;
		this.locationId = locationId;
		this.locationRoles = locationRoles;
	}

	public double getDistance()
	{
		return this.distance;
	}

	public int getPriority()
	{
		return this.priority;
	}

	public String getLocationId()
	{
		return this.locationId;
	}

	public Set<String> getLocationRoles()
	{
		return locationRoles;
	}
	@Override
	public int hashCode()
	{
		return Objects.hash(locationId);
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other == null || this.getClass() != other.getClass())
		{
			return false;
		}
		return Objects.equals(locationId, ((LocationInfo) other).getLocationId());
	}
	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("locId", this.locationId).append("prio", this.priority);
		if (this.distance < Double.MAX_VALUE)
		{
			builder.append("dist", this.distance);
		}
		builder.append("properties", getProperties());
		builder.append("locationRoles", getLocationRoles().toString());
		return builder.toString();
	}
}
