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
package com.hybris.oms.facade.inventory;


import java.util.Collections;
import java.util.concurrent.TimeUnit;

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

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.JobDetails;
import com.hybris.kernel.api.JobSchedulerService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.test.utils.DeadlockDetector;
import com.hybris.kernel.test.utils.RunnerCreator;
import com.hybris.kernel.test.utils.TestThreadsHolder;
import com.hybris.kernel.test.utils.ThreadDump;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;


/**
 * Benchmark test for {@link DefaultInventoryFacade}. Not part of the build test suite. Should be only executed on a
 * production level db (e.g. MySQL). Please configure this accordingly in
 * order-management-configuration/src/test/resources/local-test.properties Please set the general LOG level to warn to
 * retrieve accurate results. All measurements should be repeated to avoid singularities.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
{ "/oms-facade-spring-test.xml" })
@Ignore("Load Test, run manually for now")
public class DefaultInventoryFacadeBenchmarkTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultInventoryFacadeBenchmarkTest.class);

	/**
	 * Number of products per location.
	 */
	private static final int NUM_SKU_PER_LOC = 4000;

	/**
	 * Number of threads to use.
	 */
	private static final int NUM_THREADS = 4;

	private static final String LOC = "loc";
	private static final String SKU = "sku";
	private static final String ON_HAND = "ON_HAND";
	private static final int MAX_DURATION_SECONDS = 1800;

	@Autowired
	private PersistenceManager pmgr;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private JobSchedulerService scheduler;

	@Autowired
	private ImportService importService;

	@Autowired
	private InventoryFacade inventoryFacade;

	@Before
	public void setUp()
	{
		this.unscheduleJobs();
		this.init();
	}

	@Test
	public void benchmark()
	{
		benchmark(true);
		benchmark(false);
	}

	private void benchmark(final boolean create)
	{
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
						iterate(create, threadNumber);
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
				"{} skus {} for {} locations with {} threads in {}ms",
				new Object[]
				{ Integer.valueOf(NUM_SKU_PER_LOC), (create ? "inserted" : "updated"), Integer.valueOf(NUM_THREADS),
						Integer.valueOf(NUM_THREADS), Long.valueOf(delta) });
	}

	public void iterate(final boolean create, final int threadNumber)
	{
		for (int i = 0; i < NUM_SKU_PER_LOC; i++)
		{
			final String sku = SKU + i;
			final String locationId = LOC + threadNumber;
			createUpdateInventory(create, sku, locationId);
		}
	}

	private void createUpdateInventory(final boolean create, final String sku, final String locationId)
	{
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OmsInventory inv = createOmsInventory(create, sku, locationId);
				if (create)
				{
					inventoryFacade.createInventory(inv);
				}
				else
				{
					inventoryFacade.updateInventory(inv);
				}
				return null;
			}

		});
	}

	private OmsInventory createOmsInventory(final boolean create, final String sku, final String locationId)
	{
		final OmsInventory inv = new OmsInventory();
		inv.setLocationId(locationId);
		inv.setSkuId(sku);
		inv.setStatus(ON_HAND);
		inv.setQuantity(create ? 10 : 20);
		return inv;
	}

	private void init()
	{
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				importService.loadEssentialData();
				for (int i = 0; i < NUM_THREADS; i++)
				{
					final StockroomLocationData loc = pmgr.create(StockroomLocationData.class);
					loc.setLocationId(LOC + i);
					loc.setLocationRoles(Collections.singleton("SHIPPING"));
				}
				return null;
			}
		});
	}

	private void unscheduleJobs()
	{
		for (final JobDetails job : this.scheduler.getAll("single"))
		{
			this.scheduler.delete(job);
		}
	}

}
