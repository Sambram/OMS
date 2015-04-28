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
package com.hybris.oms.export.facade.ats;

import static com.hybris.oms.service.util.OmsTestUtils.delay;
import static org.junit.Assert.assertEquals;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.export.api.ats.ATSExportFacade;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.util.OmsTestUtils;
import com.hybris.oms.service.util.ShipmentTestUtils;

import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.Iterables;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-export-facade-spring-test.xml"})
public class ATSExportFacadeTest
{
	private static final String ATS_ID1 = "atsId1";

	private final Date today = new Date();
	private final long pollingDate = today.getTime();

	@Resource
	private ATSExportFacade facade;

	@Resource
	private PersistenceManager persistenceManager;

	@Autowired
	private AtsService atsService;

	@Autowired
	private InventoryService inventoryService;


	@Resource
	private PlatformTransactionManager transactionManager;

	@Resource
	private JdbcPersistenceEngine persistenceEngine;

	@After
	public void cleanData()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
		persistenceEngine.removeAll(ExportSkus._TYPECODE);
	}

	@Test
	public void shouldTriggerNoFullExport_NothingToExport()
	{
		final int result = this.facade.triggerFullExport();
		assertEquals(0, result);

		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final Iterable<ExportSkus> skus = persistenceManager.createQuery(ExportSkus.class).resultList();
				assertEquals(0, Iterables.size(skus));
			}
		});
	}

	@Test
	public void shouldPollNoChanges_NothingExported()
	{
		final AvailabilityToSellDTO result = this.facade.pollChanges(-1, pollingDate, ATS_ID1);
		assertEquals(result.getLatestChange(), null);
		assertEquals(0, result.getQuantities().size());
	}

	@Test
	public void shouldExportFull()
	{
		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsData();
				createAtsFormulas();
			}
		});

		delay(150);

		// Clear exported skus and make sure it is empty
		persistenceEngine.removeAll(ExportSkus._TYPECODE);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final Iterable<ExportSkus> skus = persistenceManager.createQuery(ExportSkus.class).resultList();
				assertEquals(0, Iterables.size(skus));
			}
		});

		// Export sku/location pair
		facade.triggerFullExport();

		// Make sure all sku-location pairs were exported
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final Iterable<ExportSkus> skus = persistenceManager.createQuery(ExportSkus.class).resultList();
				assertEquals(3, Iterables.size(skus));
			}
		});
	}

	@Test
	public void shouldExportSkuLocationPair_NoInventoryExists()
	{
		this.facade.triggerExport("sku1", "loc1");

		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final Iterable<ExportSkus> skus = persistenceManager.createQuery(ExportSkus.class).resultList();
				assertEquals(1, Iterables.size(skus));
			}
		});
	}

	@Test
	public void shouldExportSkuLocationPair_InventoryExists()
	{
		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsData();
				createAtsFormulas();
			}
		});

		delay(150);

		// Clear exported skus and make sure it is empty
		persistenceEngine.removeAll(ExportSkus._TYPECODE);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final Iterable<ExportSkus> skus = persistenceManager.createQuery(ExportSkus.class).resultList();
				assertEquals(0, Iterables.size(skus));
			}
		});

		// Export sku/location pair
		facade.triggerExport("sku1", "loc1");

		// Make sure exported sku-location pair was exported
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final Iterable<ExportSkus> skus = persistenceManager.createQuery(ExportSkus.class).resultList();
				assertEquals(1, Iterables.size(skus));
			}
		});
	}

	@Test
	public void shouldPollChanges_SkusExportedAutomatically()
	{
		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsData();
				createAtsFormulas();
			}
		});

		delay(150);

		// Clear exported skus and make sure it is empty
		persistenceEngine.removeAll(ExportSkus._TYPECODE);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				final Iterable<ExportSkus> skus = persistenceManager.createQuery(ExportSkus.class).resultList();
				assertEquals(0, Iterables.size(skus));
			}
		});

		// Update inventory to trigger an export
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				updateInventory();
			}
		});

		delay(150);

		// Verify that inventory update export is polled properly
		final AvailabilityToSellDTO pollResult = facade.pollChanges(-1, pollingDate, ATS_ID1);
		assertEquals(1, pollResult.getQuantities().size());
	}

	@Test
	public void shouldUnmarkSku()
	{
		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsData();
				createAtsFormulas();
			}
		});

		delay(150);

		AvailabilityToSellDTO pollResult = facade.pollChanges(-1, pollingDate, ATS_ID1);
		assertEquals(2, pollResult.getQuantities().size());

		// Clear the consumed changes (will clear 1 of 2 changes only))
		facade.unmarkSkuForExport(pollResult.getLatestChange());

		pollResult = facade.pollChanges(-1, pollingDate, ATS_ID1);
		assertEquals(1, pollResult.getQuantities().size());
	}

	@Test
	public void shouldNotUnmarkSku_SameDate()
	{
		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsData();
				createAtsFormulas();
			}
		});

		delay(150);

		AvailabilityToSellDTO pollResult = facade.pollChanges(1, pollingDate, ATS_ID1);
		assertEquals(1, pollResult.getQuantities().size());

		// Clear the consumed changes (will not actually clear the 1 change since the clear queries for all changes LESS
		// THAN the latest change)
		facade.unmarkSkuForExport(pollResult.getLatestChange());

		pollResult = facade.pollChanges(-1, pollingDate, ATS_ID1);
		assertEquals(2, pollResult.getQuantities().size());
	}

	@Test
	public void shouldNotPollChanges_CreatingOrder()
	{
		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);

		template.execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createAtsFormulas();
				ShipmentTestUtils.createOrder(persistenceManager);
			}
		});

		delay(150);

		final AvailabilityToSellDTO result = facade.pollChanges(-1, pollingDate, ATS_ID1);
		assertEquals(0, result.getQuantities().size());
	}

	private void updateInventory()
	{
		// as we are in a transaction, at commit only one change gets recognized and so only one message gets exported for
		this.inventoryService.updateCurrentItemQuantity("loc1", InventoryServiceConstants.DEFAULT_BIN, "sku1", "status1", 300);
		this.persistenceManager.flush(); // it should also work with flush
		this.inventoryService.updateCurrentItemQuantity("loc1", InventoryServiceConstants.DEFAULT_BIN, "sku1", "status1", 301);
		this.persistenceManager.flush();
	}

	private void createAtsFormulas()
	{
		this.atsService.createFormula("atsId1", "I[status1]", "ats1", "ats1");
		this.atsService.createFormula("atsId2", "I[status2]", "ats2", "ats2");
		this.persistenceManager.flush();
	}

	private void createAtsData()
	{
		final StockroomLocationData location1 = this.persistenceManager.create(StockroomLocationData.class);
		location1.setLocationId("loc1");
		location1.setLocationRoles(Collections.singleton(LocationRole.PICKUP.getCode()));

		final StockroomLocationData location2 = this.persistenceManager.create(StockroomLocationData.class);
		location2.setLocationId("loc2");
		location2.setLocationRoles(Collections.singleton(LocationRole.PICKUP.getCode()));

		final ItemStatusData status1 = this.persistenceManager.create(ItemStatusData.class);
		status1.setStatusCode("status1");
		status1.setDescription("status1");

		final ItemStatusData status2 = this.persistenceManager.create(ItemStatusData.class);
		status2.setStatusCode("status2");
		status2.setDescription("status2");

		final BinData bin1 = this.persistenceManager.create(BinData.class);
		bin1.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
		bin1.setStockroomLocation(location1);
		bin1.setPriority(1);

		final BinData bin2 = this.persistenceManager.create(BinData.class);
		bin2.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
		bin2.setStockroomLocation(location2);
		bin2.setPriority(1);

		final ItemLocationData inventory1 = this.persistenceManager.create(ItemLocationData.class);
		inventory1.setItemId("sku1");
		inventory1.setStockroomLocation(location1);
		inventory1.setFuture(false);
		inventory1.setBin(bin1);

		final ItemLocationData inventory2 = this.persistenceManager.create(ItemLocationData.class);
		inventory2.setItemId("sku1");
		inventory2.setStockroomLocation(location2);
		inventory2.setFuture(false);
		inventory2.setBin(bin2);

		final ItemLocationData inventory3 = this.persistenceManager.create(ItemLocationData.class);
		inventory3.setItemId("sku2");
		inventory3.setStockroomLocation(location1);
		inventory3.setFuture(false);
		inventory3.setBin(bin1);

		final CurrentItemQuantityData quantity1 = this.persistenceManager.create(CurrentItemQuantityData.class);
		quantity1.setQuantityValue(13);
		quantity1.setOwner(inventory1);
		quantity1.setStatusCode("status1");

		final CurrentItemQuantityData quantity2 = this.persistenceManager.create(CurrentItemQuantityData.class);
		quantity2.setQuantityValue(15);
		quantity2.setOwner(inventory1);
		quantity2.setStatusCode("status2");

		final CurrentItemQuantityData quantity3 = this.persistenceManager.create(CurrentItemQuantityData.class);
		quantity3.setQuantityValue(12);
		quantity3.setOwner(inventory3);
		quantity3.setStatusCode("status1");

		this.persistenceManager.flush();
	}

}
