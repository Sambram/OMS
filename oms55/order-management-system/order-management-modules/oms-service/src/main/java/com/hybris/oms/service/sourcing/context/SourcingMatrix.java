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

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.sourcing.SourcingLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Preconditions;


/**
 * Represents the matrix of sourcing lineIds by {@link SourcingLocation}. Totals per location across all skus can be
 * retrieved by calling getTotalSourcingLocations.
 */
public class SourcingMatrix
{
	private static final String SUM = "_SUM_";

	private final Map<String, List<SourcingLocation>> matrix;

	private final AtsResult atsResult;

	private final String atsId;

	/**
	 * Constructor to be used for tests only. It will calculate totals across locations assuming there is only one order
	 * line per SKU.
	 */
	public SourcingMatrix(final Map<String, List<SourcingLocation>> matrix)
	{
		this.matrix = matrix;
		this.atsId = null;
		this.atsResult = null;
		if (MapUtils.isNotEmpty(matrix))
		{
			final Map<LocationInfo, Integer> totals = new HashMap<>();
			for (final Entry<String, List<SourcingLocation>> entry : matrix.entrySet())
			{
				for (final SourcingLocation location : entry.getValue())
				{
					final LocationInfo info = location.getInfo();
					final Integer total = totals.get(info);
					final int newTotal = (total == null ? 0 : total.intValue()) + location.getAts();
					totals.put(info, Integer.valueOf(newTotal));
				}
			}
			final List<SourcingLocation> totalLocations = new ArrayList<>();
			for (final Entry<LocationInfo, Integer> entry : totals.entrySet())
			{
				totalLocations.add(new SourcingLocation(entry.getKey(), entry.getValue().intValue()));
			}
			matrix.put(SUM, totalLocations);
		}
	}

	/**
	 * Creates a new instance from the given parameters.
	 */
	public SourcingMatrix(final Map<String, List<SourcingLocation>> matrix, final List<SourcingLocation> totalsRow,
			final AtsResult atsResult, final String atsId)
	{
		this.atsResult = atsResult;
		this.atsId = atsId;
		if (MapUtils.isEmpty(matrix))
		{
			this.matrix = Collections.emptyMap();
		}
		else
		{
			Preconditions
					.checkArgument(CollectionUtils.isNotEmpty(totalsRow), "totalsRow cannot be null if the matrix is not empty");
			this.matrix = new HashMap<>(matrix);
			this.matrix.put(SUM, totalsRow);
		}
	}

	/**
	 * Returns a copy of the list of the total {@link SourcingLocation}s or an empty list.
	 *
	 * @param comparator optional comparator for sorting. If <tt>null</tt> the order is undefined.
	 * @return sorted copy of the list of the total {@link SourcingLocation}s or an empty list. Never <tt>null</tt>.
	 */
	public List<SourcingLocation> getTotalSourcingLocations(final Comparator<SourcingLocation> comparator)
	{
		return getSortedResult(this.matrix.get(SUM), comparator);
	}

	/**
	 * Returns a copy of the list of the {@link SourcingLocation}s for this sku or an empty list.
	 *
	 * @param lineId lineId of the matrix row
	 * @param comparator optional comparator for sorting. If <tt>null</tt> the order is undefined.
	 * @return sorted copy of the list of {@link SourcingLocation}s for this sku or an empty list. Never <tt>null</tt>.
	 * @throws IllegalArgumentException if the sku is blank
	 */
	public List<SourcingLocation> getSourcingLocations(final String lineId, final Comparator<SourcingLocation> comparator)
	{
		return getSortedResult(this.matrix.get(lineId), comparator);
	}

	/**
	 * Returns the ats for a locationId. Used for strategies calling getTotalSourcingLocations since
	 * {@link SourcingLocation} contains the aggregated ats value in this case. If the value is unknown, returns 0.
	 */
	public int getAtsForLocationId(final SourcingLine line, final String locationId)
	{
		int result = 0;
		if (this.atsResult != null)
		{
			result = this.atsResult.getResult(line.getSku(), this.atsId, locationId, (new Date()).getTime()).intValue();
		}
		else
		{
			final List<SourcingLocation> srcLocations = this.matrix.get(line.getLineId());
			if (srcLocations != null)
			{
				for (final SourcingLocation srcLocation : srcLocations)
				{
					if (srcLocation.getInfo().getLocationId().equals(locationId))
					{
						result = srcLocation.getAts();
					}
				}
			}
		}
		return result;
	}

	/**
	 * Returns <tt>true</tt> if the matrix is empty.
	 *
	 * @return <tt>true</tt> if the matrix is empty
	 */
	public boolean isEmpty()
	{
		return this.matrix.isEmpty();
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("matrix", this.matrix).toString();
	}

	protected List<SourcingLocation> getSortedResult(final List<SourcingLocation> sourcingLocations,
			final Comparator<SourcingLocation> comparator)
	{
		final List<SourcingLocation> result = sourcingLocations == null ? Collections.<SourcingLocation>emptyList()
				: new ArrayList<>(sourcingLocations);
		if (comparator != null)
		{
			Collections.sort(result, comparator);
		}
		return result;
	}

}
