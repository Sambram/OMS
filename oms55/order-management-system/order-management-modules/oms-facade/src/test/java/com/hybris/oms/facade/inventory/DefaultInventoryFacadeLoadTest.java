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

import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.kernel.persistence.StaleStateException;
import com.hybris.oms.api.basestore.BaseStoreFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/oms-facade-spring-test.xml"})
public class DefaultInventoryFacadeLoadTest
{

	private static int NUMBER_OF_THREADS = 5;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private BaseStoreFacade baseStoreFacade;

	@Resource
	private JdbcPersistenceEngine persistenceEngine;

	@Before
	public void setUp()
	{
		new TransactionTemplate(transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final BaseStore baseStore = new BaseStore();
				baseStore.setName("baseStore1");
				baseStoreFacade.createBaseStore(baseStore);
				return null;
			}
		});
	}

	@After
	public void tearDown()
	{
		persistenceEngine.removeAll(BaseStoreData._TYPECODE);
		persistenceEngine.removeAll(StockroomLocationData._TYPECODE);
	}

	@Test
	public void shouldCreateLocationWithBaseStore()
	{
		new TransactionTemplate(transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final Address address = new Address();
				address.setAddressLine1("addressLine1");

				final Location location = new Location();
				location.setLocationId("location1");
				location.setBaseStores(ImmutableSet.of("baseStore1"));
				location.setAddress(address);

				inventoryFacade.createStockRoomLocation(location);
				return null;
			}
		});
	}

	// @Ignore("OMSWSC-103")
	@Test
	public void shouldNotThrownStaleStateExceptionWhenCreateLocationsWithBaseStoreParallel() throws InterruptedException
	{
		final CountDownLatch startSignal = new CountDownLatch(1);
		final CountDownLatch doneSignal = new CountDownLatch(NUMBER_OF_THREADS);

		final List<CreateLocationTask> tasks = new ArrayList<>();
		final ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < NUMBER_OF_THREADS; i++)
		{
			final CreateLocationTask task = new CreateLocationTask(startSignal, doneSignal);
			tasks.add(task);
			executor.execute(task);
		}

		startSignal.countDown();
		doneSignal.await();

		for (final CreateLocationTask createLocationTask : tasks)
		{
			Assert.assertFalse("StaleStateException was thrown!", createLocationTask.isStaleStateThrown());
		}
	}

	private class CreateLocationTask implements Runnable
	{
		private final CountDownLatch startSignal;
		private final CountDownLatch doneSignal;

		private boolean staleStateThrown;

		public CreateLocationTask(final CountDownLatch startSignal, final CountDownLatch doneSignal)
		{
			this.staleStateThrown = false;
			this.startSignal = startSignal;
			this.doneSignal = doneSignal;
		}

		@Override
		public void run()
		{
			new TransactionTemplate(transactionManager).execute(new TransactionCallback<Integer>()
			{
				@Override
				public Integer doInTransaction(final TransactionStatus status)
				{
					try
					{
						startSignal.await();
						final Location location = new Location();
						location.setLocationId(UUID.randomUUID().toString());
						location.setBaseStores(ImmutableSet.of("baseStore1"));
						inventoryFacade.createStockRoomLocation(location);
					}
					catch (final StaleStateException e)
					{
						staleStateThrown = true;
					}
					catch (final Exception e)
					{
						//
					}
					finally
					{
						doneSignal.countDown();
					}

					return null;
				}
			});

		}

		public boolean isStaleStateThrown()
		{
			return staleStateThrown;
		}

	}

}
