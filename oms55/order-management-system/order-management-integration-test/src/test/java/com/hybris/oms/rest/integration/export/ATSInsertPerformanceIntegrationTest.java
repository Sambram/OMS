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
package com.hybris.oms.rest.integration.export;

import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.ats.AtsFormula;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.export.api.ExportFacade;
import com.hybris.oms.export.api.ats.ATSExportFacade;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;


/**
 * Test ignored, because performance test should not be mixed with integration, especially when unstable. <br/>
 * @see <a href="https://jira.hybris.com/browse/OA-278">JIRA OA-278</a>
 */
@Ignore
public class ATSInsertPerformanceIntegrationTest extends AbstractExportIntegrationTestSupport<AvailabilityToSellDTO>
{
	private static final Logger LOG = LoggerFactory.getLogger(ATSInsertPerformanceIntegrationTest.class);

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private AtsFacade atsFacade;

	@Autowired
	private ATSExportFacade exportFacade;

	private OmsInventory inventory;

	@Before
	public void setUp()
	{
		this.clearQueue();
	}

	@Override
	public ExportFacade<AvailabilityToSellDTO> getClient()
	{
		return exportFacade;
	}

	@Test
	public void testPerformanceOfCreatingLocation()
	{
		final long locationCreationThreshold = 100;
		executeWithStopWatch("Create Location", locationCreationThreshold, new Runnable()
		{
			@Override
			public void run()
			{
				final Location location = buildLocation(generateLocationId());
				inventoryFacade.createStockRoomLocation(location);
			}
		});
	}

	@Test
	public void testPerformanceOfCreatingFormula()
	{
		final long formulaCreationThreshold = 100;

		try
		{
			executeWithStopWatch("Create formula", formulaCreationThreshold, new Runnable()
			{
				@Override
				public void run()
				{
					final AtsFormula formula = new AtsFormula("atsId", "I[ON_HAND]-O[ON_HOLD]", "Complex Formula", "very good");
					atsFacade.createFormula(formula);
				}
			});
		}
		finally
		{
			atsFacade.deleteFormula("atsId");
		}
	}

	@Test
	public void testPerformanceOfCreatingInventory()
	{
		final long inventoryCreationThreshold = 300;

		final String locationId = generateLocationId();
		try
		{
			final String sku = generateSku();
			inventory = this.buildInventory(sku, locationId, ON_HAND, null, 5);
			final Location location = this.buildLocation(locationId);
			this.inventoryFacade.createStockRoomLocation(location);

			executeWithStopWatch("create Inventory", inventoryCreationThreshold, new Runnable()
			{
				@Override
				public void run()
				{
					inventoryFacade.createInventory(inventory);
				}
			});
		}
		finally
		{
			inventoryFacade.deleteInventory(inventory);

		}
	}

	private void executeWithStopWatch(final String taskName, final long threshold, final Runnable runnable)
	{
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		runnable.run();

		stopWatch.stop();
		final long executionTime = stopWatch.getLastTaskInfo().getTimeMillis();

		LOG.info("Execution time of " + taskName + " is :" + executionTime + " milliseconds");

		Assert.assertTrue(threshold > executionTime);
	}
}
