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
import com.hybris.oms.service.sourcing.context.SourcingSplitOption;

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
 * Unit test for {@link SplitOrderLineStrategy}.
 */
public class SplitOrderLineStrategyTest
{
	private static final String LOC_ID = "loc";
	private static final String SKU = "sku";
	private static final String OL_ID = "ol";
	private static final String LOC_ID_2 = "loc2";
	private static final String SKU_2 = "sku2";
	private static final String OL_ID_2 = "ol2";
	private static final String ATS_ID = "atsId";

	private final LocationInfo info1 = new LocationInfo(LOC_ID);
	private final LocationInfo info2 = new LocationInfo(LOC_ID_2);

	private SplitOrderLineStrategy strategy;

	@Before
	public void setUp()
	{
		strategy = new SplitOrderLineStrategy();
		strategy.setOlqBuilder(new SourcingOLQBuilder());
	}

	@Test
	public void shouldRankByLocationId()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID));
	}

	@Test
	public void shouldSplitOrder()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info2, 1)));
		matrix.put(OL_ID_2, Lists.newArrayList(new SourcingLocation(this.info1, 2)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU_2, 2, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()),
				MapAssert.entry(lines.get(1), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID),
				new SourcingOLQ(SKU_2, 2, LOC_ID, OL_ID_2));
	}

	@Test
	public void shouldAssignAndMarkLineAsProcessed()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID));
	}

	@Test
	public void shouldSplitOrderLine()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 2), new SourcingLocation(this.info2, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 3, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 2, LOC_ID, OL_ID),
				new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID));
	}

	@Test
	public void shouldHandleDuplicateSkus()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 2)));
		matrix.put(OL_ID_2, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 2)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU, 1, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()),
				MapAssert.entry(lines.get(1), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).isNotEmpty();
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID),
				new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID_2));
	}

	@Test
	public void shouldExcludeFinishedLines()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Collections.singletonList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		final ProcessStatus result = context.getProcessStatus();
		result.markLineAsProcessed(lines.get(0), strategy.getClass());
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).isEmpty();
		Assert.assertTrue(status.isEmpty());
	}

	@Test
	public void shouldFailWithoutOrderLineSplitting()
	{
		final List<SourcingLine> lines = Collections.singletonList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false, SourcingSplitOption.ON_HOLD,
				SourcingSplitOption.CANCELLED);
		final SourcingContext context = new SourcingContext(lines, new SourcingMatrix(
				Collections.<String, List<SourcingLocation>>emptyMap()), config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).isEmpty();
		Assert.assertTrue(status.isEmpty());
		Assertions.assertThat(status.getLineActions()).includes(
				MapAssert.entry(lines.get(0), SourcingSplitOption.CANCELLED.getAction()));
	}
}
