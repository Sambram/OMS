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

import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.strategy.SourcingStrategy;

import java.util.Collections;

import org.fest.assertions.Assertions;
import org.fest.assertions.MapAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for {@link ProcessStatus}.
 */
public class ProcessStatusTest
{
	private static final String OL_ID = "41";
	private static final String OL_ID_2 = "42";
	private static final String LOC_ID = "locId";
	private static final String LOC_ID_2 = "locId2";
	private static final String SKU = "SKU";
	private static final String TEST_PROP_KEY = "test";
	private static final Integer TEST_PROP_VALUE = 51;

	private ProcessStatus status;
	private SourcingLine line;

	@Before
	public void setUp()
	{
		this.status = new ProcessStatus();
		this.line = new SourcingLine(SKU, 10, OL_ID, null, null, null);
	}

	@Test
	public void shouldBeEmptyInitially()
	{
		Assertions.assertThat(this.status.getProcessedLines()).isEmpty();
		Assertions.assertThat(this.status.getSourcingOlqs()).isEmpty();
		assertNotFinished();
		Assert.assertEquals(0, this.status.getReservedForLocation(SKU, LOC_ID));
		Assert.assertEquals(0, this.status.getReservedForLine(this.line));
	}

	@Test
	public void shouldReserveForLine()
	{
		this.status.addOLQ(new SourcingOLQ(SKU, 5, LOC_ID, OL_ID));
		this.status.addOLQ(new SourcingOLQ(SKU, 5, LOC_ID_2, OL_ID_2));
		Assert.assertEquals(5, this.status.getReservedForLine(this.line));
		Assert.assertEquals(5, this.status.getReservedForLocation(SKU, LOC_ID_2));
		assertNotFinished();
	}

	@Test
	public void shouldReserveForLocation()
	{
		this.status.addOLQ(new SourcingOLQ(SKU, 5, LOC_ID, OL_ID));
		this.status.addOLQ(new SourcingOLQ(SKU, 5, LOC_ID, OL_ID_2));
		Assert.assertEquals(5, this.status.getReservedForLine(this.line));
		Assert.assertEquals(10, this.status.getReservedForLocation(SKU, LOC_ID));
		assertNotFinished();
	}

	@Test
	public void shouldMaintainFailedLine()
	{
		this.status.markLineAsProcessed(this.line, SourcingStrategy.class);
		Assertions.assertThat(this.status.getProcessedLines()).includes(
				MapAssert.entry(this.line, SourcingStrategy.class.getSimpleName()));
		Assert.assertFalse(this.status.isUnprocessedLine(this.line));
		Assert.assertTrue(this.status.allLinesProcessed(Collections.singletonList(this.line)));
	}

	@Test
	public void shouldMaintainSuccessfulLine()
	{
		this.status.markLineAsProcessed(this.line, SourcingStrategy.class);
		Assertions.assertThat(this.status.getProcessedLines()).includes(
				MapAssert.entry(this.line, SourcingStrategy.class.getSimpleName()));
		Assert.assertFalse(this.status.isUnprocessedLine(this.line));
		Assert.assertTrue(this.status.allLinesProcessed(Collections.singletonList(this.line)));
	}

	@Test
	public void shouldMaintainProperties()
	{
		status.getProperties().put(TEST_PROP_KEY, TEST_PROP_VALUE);
		Assert.assertEquals(TEST_PROP_VALUE, status.getProperties().get(TEST_PROP_KEY));
		status.setProperties(null);
		Assert.assertNull(status.getProperties().get(TEST_PROP_KEY));
	}

	private void assertNotFinished()
	{
		Assert.assertFalse(this.status.isProcessFinished());
		Assert.assertTrue(this.status.isUnprocessedLine(this.line));
		Assert.assertFalse(this.status.allLinesProcessed(Collections.singletonList(this.line)));
	}

}
