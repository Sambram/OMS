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
package com.hybris.oms.service.sourcing.impl;

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.sourcing.builder.SourcingLocationBuilder;
import com.hybris.oms.service.sourcing.builder.SourcingMatrixTotalsRowBuilder;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;
import com.hybris.oms.service.sourcing.context.SourcingLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgroups.util.Tuple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


/**
 * Unit test for {@link SourcingMatrixTotalsRowBuilder}.
 */
public class SourcingMatrixTotalsRowBuilderTest
{
	private static final String ATS_ID = "ats";
	private static final String LOC_ID = "loc";
	private static final String LOC_ID_2 = "loc2";
	private static final String SKU = "SKU";
	private static final String OL_ID = "ol";
	private static final String OL_ID_2 = "ol2";
	private static final String DISTANCE_KEY = "distance";
	private static final Long MILLISTIME = null;

	private final SourcingMatrixTotalsRowBuilder builder = new SourcingMatrixTotalsRowBuilder();

	private final LocationInfo info1 = new LocationInfo(LOC_ID);
	private final LocationInfo info2 = new LocationInfo(LOC_ID_2);
	private final Map<String, LocationInfo> locationInfos = new HashMap<>();

	@Before
	public void setUp()
	{
		builder.setSourcingLocationBuilder(new SourcingLocationBuilder());
		locationInfos.put(LOC_ID, info1);
		locationInfos.put(LOC_ID_2, info2);
	}

	@Test
	public void shouldProvideAts()
	{
		final AtsResult atsResult = new AtsResult();
		atsResult.addResult(LOC_ID, SKU, ATS_ID, 1, MILLISTIME);
		atsResult.addResult(LOC_ID_2, SKU, ATS_ID, 1, MILLISTIME);
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		matrix.put(OL_ID_2, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		final List<SourcingLocation> totals = builder.calculateTotals(null, matrix, atsResult, locationInfos);
		Assert.assertEquals(2, totals.size());
		Assert.assertEquals(1, totals.get(0).getAts());
		Assert.assertEquals(1, totals.get(1).getAts());
	}

	@Test
	public void shouldProvideMinimumDistance()
	{
		final AtsResult atsResult = new AtsResult();
		atsResult.addResult(LOC_ID, SKU, ATS_ID, 1, MILLISTIME);
		atsResult.addResult(LOC_ID_2, SKU, ATS_ID, 1, MILLISTIME);
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(
				new SourcingLocation(this.info2, 1, Collections.<String, Object>singletonMap(DISTANCE_KEY, 1d)),
				new SourcingLocation(this.info1, 1, Collections.<String, Object>singletonMap(DISTANCE_KEY, 2d))));
		matrix.put(OL_ID_2, Lists.newArrayList(
				new SourcingLocation(this.info2, 1, Collections.<String, Object>singletonMap(DISTANCE_KEY, 3d)),
				new SourcingLocation(this.info1, 1, Collections.<String, Object>singletonMap(DISTANCE_KEY, 4d))));
		final PropertiesBuilder<Tuple<LocationInfo, Map<String, List<SourcingLocation>>>> totalsPropertiesBuilder = new PropertiesBuilder<Tuple<LocationInfo, Map<String, List<SourcingLocation>>>>()
		{
			@Override
			public Map<String, Object> addProperties(final Tuple<LocationInfo, Map<String, List<SourcingLocation>>> source,
					final Map<String, Object> properties)
			{
				double minDistance = Double.MAX_VALUE;
				for (final List<SourcingLocation> row : source.getVal2().values())
				{
					for (final SourcingLocation location : row)
					{
						if (location.getInfo().equals(source.getVal1()))
						{
							final Double distance = (Double) location.getProperties().get(DISTANCE_KEY);
							if (distance.compareTo(minDistance) < 0)
							{
								minDistance = distance;
							}
							break;
						}
					}
				}
				return Collections.<String, Object>singletonMap(DISTANCE_KEY, minDistance);
			}
		};
		builder.setPropertiesBuilders(Collections.singletonList(totalsPropertiesBuilder));
		final List<SourcingLocation> totals = builder.calculateTotals(null, matrix, atsResult, locationInfos);
		Assert.assertEquals(2, totals.size());
		for (final SourcingLocation total : totals)
		{
			if (total.getInfo().equals(info1))
			{
				Assert.assertEquals(2d, total.getProperties().get(DISTANCE_KEY));
			}
			else
			{
				Assert.assertEquals(1d, total.getProperties().get(DISTANCE_KEY));
			}
		}
	}
}
