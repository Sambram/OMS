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
package com.hybris.oms.service.health.impl;

import com.hybris.commons.monitoring.collector.MetricCollector;
import com.hybris.commons.tenant.TenantContextService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.persistence.initialization.InitializationService;
import com.hybris.kernel.regioncache.CacheController;
import com.hybris.kernel.services.aggregation.AggregationGroupBean;
import com.hybris.oms.service.health.HealthService;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * Integration tests for {@link DefaultHealthService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-health-spring-test.xml"})
public class DefaultHealthServiceTest
{

	@Autowired
	private HealthService serviceHealthService;

	@Autowired
	private InitializationService initializationService;

	@Autowired
	private HealthServiceTestDataBuilder testDataBuilder;

	@Autowired
	private MetricCollector metricCollector;

	@Autowired
	private TenantContextService tcs;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	@Qualifier("serviceHealthAggGroupBean")
	private AggregationGroupBean healthServiceAggregates;

	@Autowired
	private CacheController cacheController;

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setUp()
	{
		// the setup is run only the first time

		final Set<String> tenants = initializationService.getInitializedTenants();
		if (!tenants.contains("TenantA"))
		{
			// ------------ initialize tenants --------------

			initializationService.removeTenants(tenants);
			initializationService.createTenants(new HashSet<String>(Arrays.asList(new String[]{"TenantA", "TenantB", "Tenant0"})));

			// ------------ create inventory --------------

			tcs.executeWithTenant(new TransactionalTenantOperation("TenantA", transactionManager)
			{
				@Override
				protected void executeInTransaction()
				{
					testDataBuilder.createInventoryData(); // creates 12 entities
				}
			});

			tcs.executeWithTenant(new TransactionalTenantOperation("TenantB", transactionManager)
			{
				@Override
				public void executeInTransaction()
				{
					testDataBuilder.createInventoryData(); // creates 12 entities

					testDataBuilder.createMoreInventoryData(); // creates 4 entities
				}
			});

			// ------------ update inventory --------------

			tcs.executeWithTenant(new TransactionalTenantOperation("TenantA", transactionManager)
			{
				@Override
				public void executeInTransaction()
				{
					testDataBuilder.updateInventory();
				}

			});

			// ------------ create orders --------------

			tcs.executeWithTenant(new TransactionalTenantOperation("TenantA", transactionManager)
			{
				@Override
				public void executeInTransaction()
				{
					testDataBuilder.createOrder("order1", "line1", 1, "SHIPPED", "loc1");
					testDataBuilder.createOrder("order2", "line5", 2, "SHIPPED", "loc2");
					testDataBuilder.createOrder("order3", "line2", 3, "OPEN", "loc1");
					testDataBuilder.createOrder("order4", "line2", 4, "CANCELLED", "loc2");
				}
			});

			tcs.executeWithTenant(new TransactionalTenantOperation("TenantB", transactionManager)
			{
				@Override
				public void executeInTransaction()
				{
					testDataBuilder.createOrder("order1", "line1", 5, "OPEN", "loc1");
					testDataBuilder.createOrder("order2", "line2", 6, "CANCELLED", "loc2");
				}
			});
		}
		OmsTestUtils.delay(200);
	}

	@After
	public void tearDown()
	{
		OmsTestUtils.clearCaches(cacheController);
	}

	@Test
	public void testCreateAndRemoveTenantEvent() throws InterruptedException
	{
		Map<String, Long> locationsPerTenant = serviceHealthService.getNumberOfStockRoomLocationsPerTenant();
		Assert.assertNull(locationsPerTenant.get("icke"));

		initializationService.createTenants(Collections.singleton("icke"));
		locationsPerTenant = serviceHealthService.getNumberOfStockRoomLocationsPerTenant();
		Assert.assertEquals(0, locationsPerTenant.get("icke").longValue());

		initializationService.removeTenants(Collections.singleton("icke"));
		locationsPerTenant = serviceHealthService.getNumberOfStockRoomLocationsPerTenant();
		Assert.assertNull(locationsPerTenant.get("icke"));
	}


	@Test
	public void testTenantRemovedOnReinitializeEvent() throws InterruptedException
	{
		Map<String, Long> locationsPerTenant = serviceHealthService.getNumberOfStockRoomLocationsPerTenant();
		Assert.assertNotNull(locationsPerTenant.get("TenantA"));
		Assert.assertNull(locationsPerTenant.get("yMetaData")); // was removed in setUp

		try
		{
			initializationService.initializeSystem();
			locationsPerTenant = serviceHealthService.getNumberOfStockRoomLocationsPerTenant();
			Assert.assertNull(locationsPerTenant.get("TenantA"));
			Assert.assertNotNull(locationsPerTenant.get("yMetaData"));
		}
		finally
		{
			metricCollector.removeAllMetrics();
		}
	}

	@Test
	public void testNumberOfStockRoomLocations()
	{
		Assert.assertEquals(5, serviceHealthService.getNumberOfAllStockRoomLocations());
		final Map<String, Long> binLocationsPerTenant = serviceHealthService.getNumberOfStockRoomLocationsPerTenant();
		Assert.assertEquals(3, binLocationsPerTenant.size());
		Assert.assertEquals(2, binLocationsPerTenant.get("TenantA").longValue());
		Assert.assertEquals(3, binLocationsPerTenant.get("TenantB").longValue());
		Assert.assertEquals(0, binLocationsPerTenant.get("Tenant0").longValue());

		Assert.assertEquals(2, serviceHealthService.getNumberOfStockRoomLocationsForTenant("TenantA"));
		Assert.assertEquals(3, serviceHealthService.getNumberOfStockRoomLocationsForTenant("TenantB"));
		Assert.assertEquals(0, serviceHealthService.getNumberOfStockRoomLocationsForTenant("Tenant0"));
	}

	@Test
	public void testNumbersOfInventories() throws InterruptedException
	{
		final Map<String, Map<String, Long>> inventoriesPerTenant = serviceHealthService.getNumbersOfInventoriesPerTenant();
		Assert.assertEquals(3, inventoriesPerTenant.size());
		Assert.assertEquals(2, inventoriesPerTenant.get("TenantA").size());
		Assert.assertEquals(2, inventoriesPerTenant.get("TenantB").size());
		Assert.assertEquals(0, inventoriesPerTenant.get("Tenant0").size());

		Map<String, Long> inventoryPerStatus = inventoriesPerTenant.get("TenantA");
		Assert.assertEquals(2, inventoryPerStatus.get("status1").longValue());
		Assert.assertEquals(1, inventoryPerStatus.get("status2").longValue());

		inventoryPerStatus = inventoriesPerTenant.get("TenantB");
		Assert.assertEquals(3, inventoryPerStatus.get("status1").longValue());
		Assert.assertEquals(1, inventoryPerStatus.get("status2").longValue());

		inventoryPerStatus = inventoriesPerTenant.get("Tenant0");
		Assert.assertNull(inventoryPerStatus.get("status1"));
		Assert.assertNull(inventoryPerStatus.get("status2"));

		final Map<String, Long> inventoriesPerTenantA = serviceHealthService.getNumbersOfInventoriesForTenant("TenantA");
		Assert.assertEquals(2, inventoriesPerTenantA.size());
		Assert.assertEquals(2, inventoriesPerTenantA.get("status1").longValue());
		Assert.assertEquals(1, inventoriesPerTenantA.get("status2").longValue());

		final Map<String, Long> inventoriesPerTenantB = serviceHealthService.getNumbersOfInventoriesForTenant("TenantB");
		Assert.assertEquals(2, inventoriesPerTenantB.size());
		Assert.assertEquals(3, inventoriesPerTenantB.get("status1").longValue());
		Assert.assertEquals(1, inventoriesPerTenantB.get("status2").longValue());

		final Map<String, Long> inventoriesPerTenant0 = serviceHealthService.getNumbersOfInventoriesForTenant("Tenant0");
		Assert.assertEquals(0, inventoriesPerTenant0.size());
		Assert.assertNull(inventoriesPerTenant0.get("status1"));
		Assert.assertNull(inventoriesPerTenant0.get("status2"));
	}

	@Test
	public void testNumbersOfInventoriesAddedInLastHour() throws InterruptedException
	{
		final long inventories = serviceHealthService.getNumberOfAllInventoriesAddedInLastHour();
		Assert.assertEquals(7L, inventories);

		final Map<String, Long> inventoriesPerTenant = serviceHealthService.getNumberOfInventoriesAddedInLastHourPerTenant();
		Assert.assertEquals(3L, inventoriesPerTenant.get("TenantA").longValue());
		Assert.assertEquals(4L, inventoriesPerTenant.get("TenantB").longValue());
		Assert.assertEquals(0L, inventoriesPerTenant.get("Tenant0").longValue());

		Assert.assertEquals(3L, serviceHealthService.getNumberOfInventoriesAddedInLastHourForTenant("TenantA"));
		Assert.assertEquals(4L, serviceHealthService.getNumberOfInventoriesAddedInLastHourForTenant("TenantB"));
		Assert.assertEquals(0L, serviceHealthService.getNumberOfInventoriesAddedInLastHourForTenant("Tenant0"));
	}

	@Test
	public void testNumbersOfInventoriesAddedInLastDay() throws InterruptedException
	{
		final long inventories = serviceHealthService.getNumberOfAllInventoriesAddedInLastDay();
		Assert.assertEquals(7L, inventories);

		final Map<String, Long> inventoriesPerTenant = serviceHealthService.getNumberOfInventoriesAddedInLastDayPerTenant();
		Assert.assertEquals(3L, inventoriesPerTenant.get("TenantA").longValue());
		Assert.assertEquals(4L, inventoriesPerTenant.get("TenantB").longValue());
		Assert.assertEquals(0L, inventoriesPerTenant.get("Tenant0").longValue());

		Assert.assertEquals(3L, serviceHealthService.getNumberOfInventoriesAddedInLastDayForTenant("TenantA"));
		Assert.assertEquals(4L, serviceHealthService.getNumberOfInventoriesAddedInLastDayForTenant("TenantB"));
		Assert.assertEquals(0L, serviceHealthService.getNumberOfInventoriesAddedInLastDayForTenant("Tenant0"));
	}

	@Test
	public void testNumberOfATSValuesCalculatedInLastHour() throws InterruptedException
	{
		final long atsValues = serviceHealthService.getNumberOfAllATSValuesCalculatedInLastHour();
		Assert.assertEquals(8L, atsValues);

		final Map<String, Long> atsValuesPerTenant = serviceHealthService.getNumberOfATSValuesCalculatedInLastHourPerTenant();
		Assert.assertEquals(3, atsValuesPerTenant.size());
		Assert.assertEquals(4L, atsValuesPerTenant.get("TenantA").longValue());
		Assert.assertEquals(4L, atsValuesPerTenant.get("TenantB").longValue());
		Assert.assertEquals(0L, atsValuesPerTenant.get("Tenant0").longValue());

		Assert.assertEquals(4L, serviceHealthService.getNumberOfATSValuesCalculatedInLastHourForTenant("TenantA"));
		Assert.assertEquals(4L, serviceHealthService.getNumberOfATSValuesCalculatedInLastHourForTenant("TenantB"));
		Assert.assertEquals(0L, serviceHealthService.getNumberOfATSValuesCalculatedInLastHourForTenant("Tenant0"));
	}

	@Test
	public void testNumberOfATSValuesCalculatedInLastDay() throws InterruptedException
	{
		final long atsValues = serviceHealthService.getNumberOfAllATSValuesCalculatedInLastDay();
		Assert.assertEquals(8L, atsValues);

		final Map<String, Long> atsValuesPerTenant = serviceHealthService.getNumberOfATSValuesCalculatedInLastDayPerTenant();
		Assert.assertEquals(3, atsValuesPerTenant.size());
		Assert.assertEquals(4L, atsValuesPerTenant.get("TenantA").longValue());
		Assert.assertEquals(4L, atsValuesPerTenant.get("TenantB").longValue());
		Assert.assertEquals(0L, atsValuesPerTenant.get("Tenant0").longValue());

		Assert.assertEquals(4L, serviceHealthService.getNumberOfATSValuesCalculatedInLastDayForTenant("TenantA"));
		Assert.assertEquals(4L, serviceHealthService.getNumberOfATSValuesCalculatedInLastDayForTenant("TenantB"));
		Assert.assertEquals(0L, serviceHealthService.getNumberOfATSValuesCalculatedInLastDayForTenant("Tenant0"));
	}


	@Test
	public void testNumberOfOrdersPlacedInLastHour()
	{
		Assert.assertEquals(6, serviceHealthService.getNumberOfAllOrdersPlacedInLastHour());

		final Map<String, Long> numberOfOrdersPerTenant = serviceHealthService.getNumberOfOrdersPlacedInLastHourPerTenant();
		Assert.assertEquals(3, numberOfOrdersPerTenant.size());
		Assert.assertEquals(4, numberOfOrdersPerTenant.get("TenantA").longValue());
		Assert.assertEquals(2, numberOfOrdersPerTenant.get("TenantB").longValue());
		Assert.assertEquals(0, numberOfOrdersPerTenant.get("Tenant0").longValue());

		Assert.assertEquals(4, serviceHealthService.getNumberOfOrdersPlacedInLastHourForTenant("TenantA"));
		Assert.assertEquals(2, serviceHealthService.getNumberOfOrdersPlacedInLastHourForTenant("TenantB"));
		Assert.assertEquals(0, serviceHealthService.getNumberOfOrdersPlacedInLastHourForTenant("Tenant0"));
	}

	@Test
	public void testNumberOfOrdersPlacedInLastDay()
	{
		Assert.assertEquals(6, serviceHealthService.getNumberOfAllOrdersPlacedInLastDay());

		final Map<String, Long> numberOfOrdersPerTenant = serviceHealthService.getNumberOfOrdersPlacedInLastDayPerTenant();
		Assert.assertEquals(3, numberOfOrdersPerTenant.size());
		Assert.assertEquals(4, numberOfOrdersPerTenant.get("TenantA").longValue());
		Assert.assertEquals(2, numberOfOrdersPerTenant.get("TenantB").longValue());
		Assert.assertEquals(0, numberOfOrdersPerTenant.get("Tenant0").longValue());

		Assert.assertEquals(4, serviceHealthService.getNumberOfOrdersPlacedInLastDayForTenant("TenantA"));
		Assert.assertEquals(2, serviceHealthService.getNumberOfOrdersPlacedInLastDayForTenant("TenantB"));
		Assert.assertEquals(0, serviceHealthService.getNumberOfOrdersPlacedInLastDayForTenant("Tenant0"));
	}

	@Test
	public void testStatusesOfAllOQLs() throws InterruptedException
	{
		final Map<String, Map<String, Long>> statusesForOQLs = serviceHealthService.getStatusesOfOQLsPerTenant();
		Assert.assertEquals(3, statusesForOQLs.size());

		Assert.assertEquals(3, statusesForOQLs.get("TenantA").size());
		Assert.assertEquals(2, statusesForOQLs.get("TenantB").size()); // there was no orders imported for TenantB
		Assert.assertEquals(0, statusesForOQLs.get("Tenant0").size());

		Map<String, Long> statusForOQLs = statusesForOQLs.get("TenantA");
		Assert.assertEquals(2, statusForOQLs.get("SHIPPED").longValue());
		Assert.assertEquals(1, statusForOQLs.get("OPEN").longValue());
		Assert.assertEquals(1, statusForOQLs.get("CANCELLED").longValue());

		statusForOQLs = statusesForOQLs.get("TenantB");
		Assert.assertEquals(1, statusForOQLs.get("OPEN").longValue());
		Assert.assertEquals(1, statusForOQLs.get("CANCELLED").longValue());

		final Map<String, Long> statusForOQLsA = serviceHealthService.getStatusesOfOQLsForTenant("TenantA");
		Assert.assertEquals(2, statusForOQLsA.get("SHIPPED").longValue());
		Assert.assertEquals(1, statusForOQLsA.get("OPEN").longValue());
		Assert.assertEquals(1, statusForOQLsA.get("CANCELLED").longValue());

		final Map<String, Long> statusForOQLsB = serviceHealthService.getStatusesOfOQLsForTenant("TenantB");
		Assert.assertEquals(1, statusForOQLsB.get("OPEN").longValue());
		Assert.assertEquals(1, statusForOQLsB.get("CANCELLED").longValue());
	}

	@Test
	public void testAddTenant()
	{
		initializationService.createTenants(new HashSet<String>(Arrays.asList(new String[]{"Tplus1"})));
		checkNumberOfTenants(4);
		initializationService.removeTenants(new HashSet<String>(Arrays.asList(new String[]{"Tplus1"})));
	}

	@Test
	public void testRemoveTenant()
	{
		initializationService.removeTenants(new HashSet<String>(Arrays.asList(new String[]{"Tenant0"})));
		checkNumberOfTenants(2);
		initializationService.createTenants(new HashSet<String>(Arrays.asList(new String[]{"Tenant0"})));
	}

	private void checkNumberOfTenants(final long numberOfTenants)
	{
		Assert.assertEquals(numberOfTenants, serviceHealthService.getNumberOfStockRoomLocationsPerTenant().size());
		Assert.assertEquals(numberOfTenants, serviceHealthService.getNumbersOfInventoriesPerTenant().size());
		Assert.assertEquals(numberOfTenants, serviceHealthService.getNumberOfInventoriesAddedInLastHourPerTenant().size());
		Assert.assertEquals(numberOfTenants, serviceHealthService.getNumberOfATSValuesCalculatedInLastHourPerTenant().size());
		Assert.assertEquals(numberOfTenants, serviceHealthService.getNumberOfATSValuesCalculatedInLastDayPerTenant().size());
		Assert.assertEquals(numberOfTenants, serviceHealthService.getNumberOfOrdersPlacedInLastHourPerTenant().size());
		Assert.assertEquals(numberOfTenants, serviceHealthService.getNumberOfOrdersPlacedInLastDayPerTenant().size());
		Assert.assertEquals(numberOfTenants, serviceHealthService.getStatusesOfOQLsPerTenant().size());
	}

}
