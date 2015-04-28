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
package com.hybris.oms.service.sourcing.builder;

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsResult.AtsRow;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;
import com.hybris.oms.service.sourcing.context.PropertiesBuilderSupport;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.strategy.impl.ItemGroupingStrategy;
import com.hybris.oms.service.sourcing.strategy.impl.WholeOrderStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.jgroups.util.Tuple;
import org.springframework.beans.factory.annotation.Required;


/**
 * Builder responsible for building the totals row in the sourcing matrix.
 */
public class SourcingMatrixTotalsRowBuilder
{
	private SourcingLocationBuilder sourcingLocationBuilder;

	private List<PropertiesBuilder<Tuple<LocationInfo, Map<String, List<SourcingLocation>>>>> propertiesBuilders;

	/**
	 * Calculates the row with totals used for ranking locations by the {@link WholeOrderStrategy} and
	 * {@link ItemGroupingStrategy}.
	 */
	public List<SourcingLocation> calculateTotals(@SuppressWarnings("unused") final List<SourcingLine> sourcingLines,
			final Map<String, List<SourcingLocation>> matrix, final AtsResult atsResult,
			final Map<String, LocationInfo> locationInfos)
	{
		final List<SourcingLocation> totalsRow = new ArrayList<>();
		final Map<String, Integer> atsTotals = calculateAtsTotals(atsResult);
		for (final Entry<String, Integer> entry : atsTotals.entrySet())
		{
			final LocationInfo info = locationInfos.get(entry.getKey());
			totalsRow.add(sourcingLocationBuilder.build(info, entry.getValue(),
					PropertiesBuilderSupport.buildProperties(new Tuple<>(info, matrix), propertiesBuilders)));
		}
		return totalsRow;
	}

	/**
	 * Calculates ATS totals by inspecting the atsResult.
	 */
	protected Map<String, Integer> calculateAtsTotals(final AtsResult atsResult)
	{
		final Map<String, Integer> atsTotals = new HashMap<>();
		for (final AtsRow row : atsResult.getResults())
		{
			Integer total = atsTotals.get(row.getKey().getLocationId());
			if (total == null)
			{
				total = NumberUtils.INTEGER_ZERO;
			}
			atsTotals.put(row.getKey().getLocationId(), total.intValue() + row.getValue().intValue());
		}
		return atsTotals;
	}

	@Required
	protected SourcingLocationBuilder getSourcingLocationBuilder()
	{
		return sourcingLocationBuilder;
	}

	public void setSourcingLocationBuilder(final SourcingLocationBuilder sourcingLocationBuilder)
	{
		this.sourcingLocationBuilder = sourcingLocationBuilder;
	}

	protected List<PropertiesBuilder<Tuple<LocationInfo, Map<String, List<SourcingLocation>>>>> getPropertiesBuilders()
	{
		return propertiesBuilders;
	}

	public void setPropertiesBuilders(
			final List<PropertiesBuilder<Tuple<LocationInfo, Map<String, List<SourcingLocation>>>>> propertiesBuilders)
	{
		this.propertiesBuilders = propertiesBuilders;
	}

}
