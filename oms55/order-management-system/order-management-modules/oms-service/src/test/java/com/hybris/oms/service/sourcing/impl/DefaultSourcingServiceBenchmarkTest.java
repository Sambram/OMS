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

import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.JobSchedulerService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.kernel.test.utils.DeadlockDetector;
import com.hybris.kernel.test.utils.RunnerCreator;
import com.hybris.kernel.test.utils.TestThreadsHolder;
import com.hybris.kernel.test.utils.ThreadDump;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.inventory.impl.dataaccess.AggItemQuantityByItemIdLocationStatus;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.SourcingService;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableSet;


/**
 * Benchmark test for {@link DefaultSourcingService}. Not part of the build test suite.
 * Should be only executed on a production level db (e.g. MySQL).
 * Please configure this accordingly in order-management-configuration/src/test/resources/local-test.properties
 * Please set the general LOG level to warn to retrieve accurate results.
 * All measurements should be repeated to avoid singularities.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
@Ignore("Load Test, run manually for now")
public class DefaultSourcingServiceBenchmarkTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSourcingServiceBenchmarkTest.class);

	/**
	 * Number of threads to use.
	 */
	private static final int NUM_THREADS = 1;

	/**
	 * Number of products per location (small: 10T, medium: 200T, enterprise: 1M).
	 */
	private static final int NUM_SKU = 1000;

	/**
	 * Number of locations (small: 10, medium: 100, enterprise: 250).
	 */
	private static final int NUM_LOCATIONS = 10;

	/**
	 * Initialize the data. Setting this to false saves time with a large
	 * number of SKUs/Locations together with kernel.autoInitMode=none in local-test.properties.
	 */
	private static final boolean INIT_DATA = true;

	/**
	 * Call sourcing simulation with ATS.
	 */
	private static final boolean SIMULATE_SOURCING = false;

	/**
	 * Number of iterations per thread.
	 */
	private static final int ITERATIONS = 1000;

	private static final String LOC = "loc";
	private static final String WAREHOUSE = "loc0";
	private static final String SKU = "sku";
	private static final String ON_HAND = "ON_HAND";
	private static final String ATS_ID = "WEB";
	private static final int MAX_DURATION_SECONDS = 1800;

	private static final int UNITS = 1000000;

	@Autowired
	private PersistenceManager pmgr;

	@Autowired
	private AggregationService aggregationService;

	@Autowired
	private SourcingService sourcingService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private JobSchedulerService scheduler;

	@Resource
	private JdbcPersistenceEngine persistenceEngine;

	@Autowired
	private ImportService importService;

	private final Random random = new Random();

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Before
	public void setUp()
	{
		OmsTestUtils.unscheduleJobs(scheduler);
		if (INIT_DATA)
		{
			OmsTestUtils.cleanUp(persistenceEngine);
			init();
		}
	}

	public void iterate()
	{
		for (int i = 0; i < ITERATIONS; i++)
		{
			final String sku = this.getRandomSku();
			final List<SourcingLine> lines = Collections.singletonList(new SourcingLine(sku, 1, "1"));
			if (SIMULATE_SOURCING)
			{
				simulateSourcing(lines);
			}
			else
			{
				sourceInput(sku, lines);
			}
		}
	}

	private void sourceInput(final String sku, final List<SourcingLine> lines)
	{
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(sku, 1, WAREHOUSE, "1"));
				return null;
			}
				});
	}

	private void simulateSourcing(final List<SourcingLine> lines)
	{
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final StockroomLocationData result = sourcingService.simulateSourcing(lines, ATS_ID, null, null);
				Assert.assertEquals(WAREHOUSE, result.getLocationId());
				return null;
			}
				});
	}

	private String getRandomSku()
	{
		return SKU + random.nextInt(NUM_SKU);
	}

	@Test
	public void benchmark()
	{
		LOG.info("Starting benchmark test");
		final long time1 = System.currentTimeMillis();
		final TestThreadsHolder<Runnable> threadRunner = new TestThreadsHolder<>(NUM_THREADS, new RunnerCreator<Runnable>()
				{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new Runnable()
				{
					@Override
					public void run()
					{
						iterate();
					}
				};
			}
				});
		threadRunner.startAll();
		final boolean stopped = threadRunner.waitForAll(MAX_DURATION_SECONDS, TimeUnit.SECONDS);

		if (!stopped)
		{
			LOG.error("Still running threads: " + threadRunner.getAlive());
			ThreadDump.dumpThreads(System.err);
			DeadlockDetector.printDeadlocks(System.err);
			threadRunner.stopAll();
		}
		Assert.assertTrue(stopped);
		final long delta = System.currentTimeMillis() - time1;
		Assert.assertFalse("Errors have occured", threadRunner.hasErrors());
		LOG.info(
				"{} thread(s) finished with {} iterations, {} locations, {} SKUs in {}ms",
				new Object[]{Integer.valueOf(NUM_THREADS), Integer.valueOf(ITERATIONS), Integer.valueOf(NUM_LOCATIONS),
						Integer.valueOf(NUM_SKU), Long.valueOf(delta)});
	}

	private void init()
	{
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				importService.loadMcsvResource(fetcher.fetchResources("/ats/test-ats-data-import.mcsv")[0]);
				importService.loadMcsvResource(fetcher.fetchResources("/order/test-order-status-data-import.mcsv")[0]);
				importService.loadMcsvResource(fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
				return null;
			}
				});
		final long start = System.currentTimeMillis();
		for (int j = 0; j < NUM_LOCATIONS; j++)
		{
			this.importDataForLocation(j);
		}

		final long delta = System.currentTimeMillis() - start;
		LOG.info("Inventory created in {}ms - {}rows/s", Long.valueOf(delta), NUM_SKU * NUM_LOCATIONS * 1000 / delta);
		this.initAgg();
	}

	private void importDataForLocation(final int locationIdx)
	{
		final long start = System.currentTimeMillis();
		final String locationId = LOC + locationIdx;
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final StockroomLocationData loc = pmgr.create(StockroomLocationData.class);
				loc.setLocationId(locationId);
				loc.setPriority(locationIdx);
				loc.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
				final BinData bin = pmgr.create(BinData.class);
				bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
				bin.setStockroomLocation(loc);
				for (int i = 0; i < NUM_SKU; i++)
				{
					final ItemLocationData il = pmgr.create(ItemLocationData.class);
					il.setItemId(SKU + i);
					il.setStockroomLocation(loc);
					il.setFuture(false);
					il.setBin(bin);
					final CurrentItemQuantityData iq = pmgr.create(CurrentItemQuantityData.class);
					iq.setOwner(il);
					iq.setQuantityValue(UNITS);
					iq.setStatusCode(ON_HAND);
				}
				return null;
			}
				});
		final long delta = System.currentTimeMillis() - start;
		LOG.info("Created {} products for {} in {}ms", new Object[]{NUM_SKU, locationId, Long.valueOf(delta)});
	}

	private void initAgg()
	{
		final long time1 = System.currentTimeMillis();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				aggregationService.clearAggregates(AggItemQuantityByItemIdLocationStatus.class);
				aggregationService.recalculateAggregation(AggItemQuantityByItemIdLocationStatus.class);
				return null;
			}
				});
		final long delta = System.currentTimeMillis() - time1;
		LOG.info("Aggregation recalculated in {}ms - {} rows/s", Long.valueOf(delta), NUM_SKU * NUM_LOCATIONS * 1000 / delta);
	}

}
