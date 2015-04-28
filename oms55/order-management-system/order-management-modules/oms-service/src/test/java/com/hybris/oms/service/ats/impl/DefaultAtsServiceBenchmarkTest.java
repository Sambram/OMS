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
package com.hybris.oms.service.ats.impl;

import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.JobSchedulerService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.kernel.test.utils.DeadlockDetector;
import com.hybris.kernel.test.utils.RunnerCreator;
import com.hybris.kernel.test.utils.TestThreadsHolder;
import com.hybris.kernel.test.utils.ThreadDump;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.inventory.impl.dataaccess.AggItemQuantityByItemIdLocationStatus;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

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


/**
 * Load test for {@link DefaultAtsService}. Not part of the build test suite.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
@Ignore("manual benchmark test")
public class DefaultAtsServiceBenchmarkTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultAtsServiceBenchmarkTest.class);

	private static final String ENV_SKUS = "skus";
	private static final String ENV_LOCATIONS = "locations";
	private static final String ENV_SKIP_INIT = "skipInit";
	private static final String ENV_ITERATIONS = "iterations";
	private static final String ENV_FILTER_SKUS = "filterSkus";
	private static final String ENV_FILTER_LOCATIONS = "filterLocations";
	private static final String ENV_THREADS = "threads";

	private static final String DEFAULT_SKUS = "1000";
	private static final String DEFAULT_FILTER_SKUS = "1";
	private static final String DEFAULT_LOCATIONS = "10";
	private static final String DEFAULT_ITERATIONS = "1000";
	private static final String DEFAULT_SKIP_INIT = "false";
	private static final String DEFAULT_THREADS = "1";

	private static final String SKU = "SKU";
	private static final String ON_HAND = "ON_HAND";
	private static final String LOC = "loc";
	private static final String ATS_ID = "loadTest";

	private static final int RETRIES = 120;
	private static final int DELAY_MILLIS = 1000;
	private static final int MAX_DURATION_SECONDS = 1800;

	private static final Long MILLISTIME = null;


	private final Random random = new Random();

	@Autowired
	private PersistenceManager pmgr;

	@Autowired
	private AtsService atsService;

	@Autowired
	private AggregationService aggregationService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private JobSchedulerService scheduler;

	@Resource
	private JdbcPersistenceEngine persistenceEngine;

	@Before
	public void setUp()
	{
		OmsTestUtils.unscheduleJobs(scheduler);
		if (!isSkipInit())
		{
			this.cleanUp();
			this.init();
		}
	}

	@Test
	public void loadTest()
	{
		final long time1 = System.currentTimeMillis();
		final TestThreadsHolder<Runnable> threadRunner = new TestThreadsHolder<>(getThreads(), new RunnerCreator<Runnable>()
		{
			@Override
			public Runnable newRunner(final int threadNumber)
			{
				return new Runnable()
				{
					@Override
					public void run()
					{
						measureAts();
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
		for (final Throwable error : threadRunner.getErrors().values())
		{
			LOG.error("Unexpected error", error);
		}
		Assert.assertFalse("Errors have occured", threadRunner.hasErrors());
		final long delta = System.currentTimeMillis() - time1;
		LOG.info(
				"{} threads, {} iterations, {} skus, {} locations, {} filterSkus, {} filterLocations, total time {}ms, throughput {}",
				new Object[]{Integer.valueOf(getThreads()), Integer.valueOf(getIterations()), Integer.valueOf(getSkus()),
						Integer.valueOf(getLocations()), Integer.valueOf(getFilterSkus()), Integer.valueOf(getFilterLocations()),
						Long.valueOf(delta), Double.valueOf((double) getThreads() * getIterations() * 1000 / delta)});
	}

	private void measureAts()
	{
		final Set<String> filterSkus = new HashSet<>();
		for (int i = 0; i < getFilterSkus(); i++)
		{
			filterSkus.add(SKU + random.nextInt(getSkus()));
		}
		final Set<String> filterLocations = new HashSet<>();
		for (int i = 0; i < getFilterLocations(); i++)
		{
			filterLocations.add(LOC + random.nextInt(getLocations()));
		}
		for (int i = 0; i < getIterations(); i++)
		{
			final String sku = filterSkus.iterator().next();
			final String loc = filterLocations.iterator().next();
			final AtsResult result = new TransactionTemplate(transactionManager).execute(new TransactionCallback<AtsResult>()
			{
				@Override
				public AtsResult doInTransaction(final TransactionStatus status)
				{
					return atsService.getLocalAts(filterSkus, filterLocations, null);
				}
			});
			Assert.assertEquals("Invalid result for sku=" + sku + ", loc=" + loc, Integer.valueOf(100),
					result.getResult(sku, ATS_ID, loc, MILLISTIME));
		}
	}

	private void init()
	{
		this.aggregationService.clearAggregates(AggItemQuantityByItemIdLocationStatus.class);
		final long time1 = System.currentTimeMillis();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final ItemStatusData ist = DefaultAtsServiceBenchmarkTest.this.pmgr.create(ItemStatusData.class);
				ist.setStatusCode(ON_HAND);
				ist.setDescription("On Hand");
				DefaultAtsServiceBenchmarkTest.this.pmgr.flush();
				DefaultAtsServiceBenchmarkTest.this.atsService.createFormula(ATS_ID, "I[" + ON_HAND + "]", "On Hand", null);
				final List<StockroomLocationData> locations = new ArrayList<>();
				final List<BinData> bins = new ArrayList<>();
				for (int i = 0; i < getLocations(); i++)
				{
					final StockroomLocationData loc = DefaultAtsServiceBenchmarkTest.this.pmgr.create(StockroomLocationData.class);
					loc.setLocationId(LOC + i);
					loc.setLocationRoles(Collections.singleton("SHIPPING"));
					final BinData bin = pmgr.create(BinData.class);
					bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
					bin.setStockroomLocation(loc);
					locations.add(loc);
					bins.add(bin);
				}
				for (int i = 0; i < getSkus(); i++)
				{
					for (int j = 0; j < getLocations(); j++)
					{
						final ItemLocationData il = DefaultAtsServiceBenchmarkTest.this.pmgr.create(ItemLocationData.class);
						il.setItemId(SKU + i);
						il.setStockroomLocation(locations.get(j));
						il.setFuture(false);
						il.setBin(bins.get(j));
						final CurrentItemQuantityData iq = DefaultAtsServiceBenchmarkTest.this.pmgr
								.create(CurrentItemQuantityData.class);
						iq.setOwner(il);
						iq.setQuantityValue(100);
						iq.setStatusCode(ON_HAND);
					}
					if (i % 100 == 0)
					{
						pmgr.flush();
					}
				}
				return null;
			}
		});
		final long dt = System.currentTimeMillis() - time1;
		LOG.info("Inventory created in {}ms - {}rows/s", Long.valueOf(dt), getSkus() * 1000 / dt);
		this.waitForAggregation();
	}

	private void waitForAggregation()
	{
		final long time1 = System.currentTimeMillis();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final String sku = SKU + (getSkus() - 1);
				for (int i = 0; i < RETRIES; i++)
				{
					final AtsResult result = atsService.getLocalAts(Collections.singleton(sku), null, null);
					if (result.getResults().size() == getLocations())
					{
						break;
					}
					OmsTestUtils.delay(DELAY_MILLIS);
				}
				final AtsResult result = atsService.getLocalAts(Collections.singleton(sku), null, null);
				Assert.assertEquals(Integer.valueOf(100), result.getResult(sku, ATS_ID, LOC + "0", MILLISTIME));
				return null;
			}
		});
		LOG.info("Aggregated in {}ms", Long.valueOf(System.currentTimeMillis() - time1));
	}

	private boolean isSkipInit()
	{
		return getBooleanProperty(ENV_SKIP_INIT, DEFAULT_SKIP_INIT);
	}

	private int getSkus()
	{
		return getIntProperty(ENV_SKUS, DEFAULT_SKUS);
	}

	private int getLocations()
	{
		return getIntProperty(ENV_LOCATIONS, DEFAULT_LOCATIONS);
	}

	private int getIterations()
	{
		return getIntProperty(ENV_ITERATIONS, DEFAULT_ITERATIONS);
	}

	private int getFilterSkus()
	{
		return getIntProperty(ENV_FILTER_SKUS, DEFAULT_FILTER_SKUS);
	}

	private int getFilterLocations()
	{
		return getIntProperty(ENV_FILTER_LOCATIONS, Integer.toString(getLocations()));
	}

	private int getThreads()
	{
		return getIntProperty(ENV_THREADS, DEFAULT_THREADS);
	}

	private int getIntProperty(final String key, final String defaultValue)
	{
		return Integer.parseInt(System.getProperty(key, defaultValue));
	}

	private boolean getBooleanProperty(final String key, final String defaultValue)
	{
		return Boolean.parseBoolean(System.getProperty(key, defaultValue));
	}

	public void cleanUp()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

}
