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
package com.hybris.oms.service.sourcing.strategy.impl;

import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.builder.SourcingOLQBuilder;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.context.SourcingConfiguration;
import com.hybris.oms.service.sourcing.context.SourcingContext;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.context.SourcingMatrix;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fest.assertions.Assertions;
import org.fest.assertions.MapAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;


/**
 * Unit test for {@link PickupInStoreStrategy}.
 */
public class PickupInStoreStrategyIntegrationTest
{
	private static final String LOC_ID = "loc";
	private static final String SKU = "sku";
	private static final String OL_ID = "ol";

	private static final String LOC_ID_2 = "loc2";
	private static final String SKU_2 = "sku2";
	private static final String OL_ID_2 = "ol2";

	private static final String ATS_ID = "ON_HAND";

	private final LocationInfo info1 = new LocationInfo(LOC_ID);
	private final LocationInfo info2 = new LocationInfo(LOC_ID_2);

	private PickupInStoreStrategy strategy;

	@Before
	public void setUp()
	{
		strategy = new PickupInStoreStrategy();
		strategy.setOlqBuilder(new SourcingOLQBuilder());
	}

	@Test
	public void shouldAssignOrderLines()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(info1, 1)));
		matrix.put(OL_ID_2, Lists.newArrayList(new SourcingLocation(info2, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, LOC_ID, null, null), new SourcingLine(
				SKU_2, 1, OL_ID_2, LOC_ID_2, null, null));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()),
				MapAssert.entry(lines.get(1), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID),
				new SourcingOLQ(SKU_2, 1, LOC_ID_2, OL_ID_2));
	}

	@Test
	public void shouldAssignAsManyAsPossible()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(info1, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1000, OL_ID, LOC_ID, null, null));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID));
	}

	@Test
	public void shouldHandleDuplicateSkus()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(info1, 2)));
		matrix.put(OL_ID_2, Lists.newArrayList(new SourcingLocation(info1, 2)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, LOC_ID, null, null), new SourcingLine(
				SKU, 1, OL_ID_2, LOC_ID, null, null));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()),
				MapAssert.entry(lines.get(1), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID),
				new SourcingOLQ(SKU, 1, LOC_ID, OL_ID_2));
	}

	@Test
	public void shouldExcludeProcessedLines()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(info1, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Collections.singletonList(new SourcingLine(SKU, 1, OL_ID, LOC_ID, null, null));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		final ProcessStatus result = context.getProcessStatus();
		result.markLineAsProcessed(lines.get(0), strategy.getClass());
		strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).isEmpty();
		Assert.assertTrue(status.isEmpty());
	}

	@Test
	public void shouldOnlyProcessPickupLines()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(info1, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Collections.singletonList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assertions.assertThat(status.getProcessedLines()).isEmpty();
		Assertions.assertThat(status.getSourcingOlqs()).isEmpty();
		Assert.assertTrue(status.isEmpty());
	}

}
