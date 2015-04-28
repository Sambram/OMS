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

import com.hybris.oms.service.sourcing.PropertiesSupport;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Preconditions;


/**
 * Provides the ATS and distance to a specific {@link LocationInfo}.
 */
public class SourcingLocation extends PropertiesSupport
{
	private final int ats;

	private final LocationInfo info;

	/**
	 * Creates a new {@link SourcingLocation} instance.
	 * 
	 * @param info must not be null
	 * @param ats the amount to sell
	 * @throws IllegalArgumentException if info is null
	 */
	public SourcingLocation(final LocationInfo info, final int ats)
	{
		this(info, ats, null);
	}

	/**
	 * Creates a new {@link SourcingLocation} instance.
	 * 
	 * @param info must not be null
	 * @param ats the amount to sell
	 * @param properties additional properties
	 * @throws IllegalArgumentException if info is null
	 */
	public SourcingLocation(final LocationInfo info, final int ats, final Map<String, Object> properties)
	{
		super(properties);
		Preconditions.checkArgument(info != null, "The LocationInfo cannot be null");
		this.info = info;
		this.ats = ats;
	}

	public int getAts()
	{
		return this.ats;
	}

	public LocationInfo getInfo()
	{
		return this.info;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("info", this.info).append("ats", this.ats)
				.append("properties", getProperties()).toString();
	}

	/**
	 * {@link Comparator} to sort {@SourcingLocation}s based on the ATS quantity. Higher
	 * ATS is ranked higher.
	 */
	public static final class AtsComparator implements Comparator<SourcingLocation>, Serializable
	{
		private static final long serialVersionUID = 5107414198271174482L;

		@Override
		public int compare(final SourcingLocation firstLocation, final SourcingLocation secondLocation)
		{
			return Integer.valueOf(secondLocation.getAts()).compareTo(Integer.valueOf(firstLocation.getAts()));
		}
	}

	/**
	 * Composite {@link Comparator} composed of a {@link List} of {@link Comparator}s.
	 */
	public static final class CompositeComparator<T> implements Comparator<T>, Serializable
	{
		private static final long serialVersionUID = 3876540162970258764L;

		private final List<Comparator<T>> comparators;

		private final String descriptor;

		/**
		 * Creates a new composite comparator from the list of comparators.
		 * 
		 * @param comparators list of comparators, must not be empty
		 * @param descriptor optional descriptor
		 * @throws IllegalArgumentException if the list of comparators is empty.
		 */
		public CompositeComparator(final List<Comparator<T>> comparators, final String descriptor)
		{
			Preconditions.checkArgument(CollectionUtils.isNotEmpty(comparators), "comparators cannot be empty");
			this.comparators = comparators;
			this.descriptor = descriptor;
		}

		@Override
		public int compare(final T loc1, final T loc2)
		{
			int result = 0;
			for (final Comparator<T> comp : this.comparators)
			{
				result = comp.compare(loc1, loc2);
				if (result != 0)
				{
					break;
				}
			}
			return result;
		}

		public List<Comparator<T>> getComparators()
		{
			return Collections.unmodifiableList(this.comparators);
		}

		@Override
		public String toString()
		{
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("descriptor", this.descriptor).toString();
		}
	}

	/**
	 * {@link Comparator} to sort {@SourcingLocation}s based on the distance from the shipping
	 * location. Lower distances are ranked higher.
	 */
	public static final class DistanceComparator implements Comparator<SourcingLocation>, Serializable
	{
		private static final long serialVersionUID = -1211008572252746992L;

		@Override
		public int compare(final SourcingLocation firstLocation, final SourcingLocation secondLocation)
		{
			return Double.valueOf(firstLocation.getInfo().getDistance()).compareTo(
					Double.valueOf(secondLocation.getInfo().getDistance()));
		}
	}

	/**
	 * Default {@link Comparator} to sort {@SourcingLocation}s based on the locationId. Lower
	 * locationIds are ranked higher.
	 */
	public static final class LocationIdComparator implements Comparator<SourcingLocation>, Serializable
	{
		private static final long serialVersionUID = -1183654424996651860L;

		@Override
		public int compare(final SourcingLocation firstLocation, final SourcingLocation secondLocation)
		{
			return firstLocation.getInfo().getLocationId().compareTo(secondLocation.getInfo().getLocationId());
		}
	}

	/**
	 * {@link Comparator} to sort {@SourcingLocation}s based on stockroom location priority. Lower
	 * priorities are ranked higher.
	 */
	public static final class PriorityComparator implements Comparator<SourcingLocation>, Serializable
	{
		private static final long serialVersionUID = -4992987346575009455L;

		@Override
		public int compare(final SourcingLocation firstLocation, final SourcingLocation secondLocation)
		{
			return Integer.valueOf(firstLocation.getInfo().getPriority()).compareTo(
					Integer.valueOf(secondLocation.getInfo().getPriority()));
		}
	}
}
