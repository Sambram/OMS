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

import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


/**
 * Integration test for {@link ItemGroupingStrategy}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class ItemGroupingStrategyIntegrationTest
{
	private static final String LOC_ID = "loc";
	private static final String SKU = "sku";
	private static final String OL_ID = "ol";
	private static final String LOC_ID_2 = "loc2";
	private static final String SKU_2 = "sku2";
	private static final String OL_ID_2 = "ol2";
	private static final String ATS_ID = "atsId";

	private final LocationInfo info1 = new LocationInfo(0, LOC_ID, Double.MAX_VALUE, ImmutableSet.of(LocationRole.SHIPPING.name()));
	private final LocationInfo info2 = new LocationInfo(0, LOC_ID_2, Double.MAX_VALUE, ImmutableSet.of(LocationRole.SHIPPING
			.name()));

	@Autowired
	private ItemGroupingStrategy strategy;

	@Test
	public void shouldRankByTotalAts()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		matrix.put(OL_ID_2, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 2)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID), new SourcingLine(SKU_2, 1, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assert.assertEquals(1, status.getReservedForLocation(SKU, LOC_ID));
		Assert.assertEquals(1, status.getReservedForLocation(SKU, LOC_ID_2));
		Assert.assertEquals(1, status.getReservedForLocation(SKU_2, LOC_ID_2));
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()),
				MapAssert.entry(lines.get(1), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID),
				new SourcingOLQ(SKU, 1, LOC_ID, OL_ID), new SourcingOLQ(SKU_2, 1, LOC_ID_2, OL_ID_2));
	}

	@Test
	public void shouldHandleDuplicateSkus()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		matrix.put(OL_ID_2, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU, 1, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assert.assertEquals(1, status.getReservedForLocation(SKU, LOC_ID));
		Assert.assertEquals(1, status.getReservedForLocation(SKU, LOC_ID_2));
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()),
				MapAssert.entry(lines.get(1), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID),
				new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID_2));
	}

	@Test
	public void shouldExcludeProcessedLines()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
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
	public void shouldExecuteOnlyIfConfigured()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Collections.singletonList(new SourcingLocation(this.info1, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Collections.singletonList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assertions.assertThat(status.getProcessedLines()).isEmpty();
		Assertions.assertThat(status.getSourcingOlqs()).isEmpty();
		Assert.assertTrue(status.isEmpty());
	}

	@Test
	public void shouldAssignAsManyAsPossible()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Collections.singletonList(new SourcingLine(SKU, 3, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertFalse(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID),
				new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID));
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
	}

	@Test
	public void shouldCancelWithoutOrderLineSplitting()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(OL_ID, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		final SourcingMatrix srcMatrix = new SourcingMatrix(matrix);
		final List<SourcingLine> lines = Collections.singletonList(new SourcingLine(SKU, 3, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true, SourcingSplitOption.CANCELLED,
				SourcingSplitOption.ON_HOLD);
		final SourcingContext context = new SourcingContext(lines, srcMatrix, config);
		this.strategy.source(context);
		final ProcessStatus status = context.getProcessStatus();
		Assert.assertTrue(status.isEmpty());
		Assert.assertTrue(status.isProcessFinished());
		Assertions.assertThat(status.getProcessedLines()).includes(
				MapAssert.entry(lines.get(0), strategy.getClass().getSimpleName()));
		Assertions.assertThat(status.getSourcingOlqs()).isEmpty();
		Assertions.assertThat(status.getLineActions()).includes(
				MapAssert.entry(lines.get(0), SourcingSplitOption.ON_HOLD.getAction()));
	}

}
