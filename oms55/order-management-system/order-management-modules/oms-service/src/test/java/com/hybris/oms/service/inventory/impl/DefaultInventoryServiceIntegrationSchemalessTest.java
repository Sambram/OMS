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
package com.hybris.oms.service.inventory.impl;

import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.util.OmsTestUtils;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/oms-service-spring-schemaless-test.xml")
public class DefaultInventoryServiceIntegrationSchemalessTest
{
	private static final String LOCATION_ID_VALUE = "1";
	private static final String BIN_CODE_VALUE_1 = "1";
	private static final String BIN_SIZE_VALUE_10 = "10";
	private static final int BIN_PRIORITY_VALUE = 6;
	private static final String BIN_COLOR_VALUE = "Blue";
	private static final String BIN_DESCRIPTION_VALUE = "Bin Description";
	private static final String BIN_SHAPE_VALUE = "Box";
	private static final String BIN_WEIGHT_VALUE = "10.5";
	private static final String BIN_IS_FRAGILE_VALUE = "True";
	private static final String BIN_IS_WEAVED_VALUE = "False";
	private static final String BIN_IS_COVERED_VALUE = "False";
	private static final String BIN_IS_PRETTY_VALUE = "False";
	private static final String BIN_WIDTH_VALUE = "55";
	private static final String BIN_HEIGHT_VALUE = "75";

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private SchemalessInventoryQueryFactory factory;

	@Resource
	private JdbcPersistenceEngine persistenceEngine;

	private InventoryScenarioTestDataHelper inventoryTestHelper;

	@Before
	public void setUp()
	{
		inventoryTestHelper = new InventoryScenarioTestDataHelper(persistenceManager);
	}

	@Test
	@Transactional
	public void testSearchBinBySchemalessAttribute()
	{
		factory.setAllowNull(false);
		final StockroomLocationData location = inventoryTestHelper.createStockRoomLocation(LOCATION_ID_VALUE);
		final BinData bin = inventoryTestHelper.createBin(location, BIN_CODE_VALUE_1, BIN_PRIORITY_VALUE, null);

		bin.setProperty(SchemalessInventoryQueryFactory.BIN_SIZE, BIN_SIZE_VALUE_10);

		this.persistenceManager.flush();

		final BinQueryObject queryObject = new BinQueryObject();
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_SIZE, BIN_SIZE_VALUE_10);

		final Page<BinData> bins = inventoryService.findPagedBinsByQuery(queryObject);

		Assert.assertNotNull(bins);
		Assert.assertNotNull(bins.getContent());
		Assert.assertEquals(1, bins.getContent().size());

		Assert.assertEquals(LOCATION_ID_VALUE, bins.getContent().get(0).getStockroomLocation().getLocationId());
		Assert.assertEquals(BIN_CODE_VALUE_1, bins.getContent().get(0).getBinCode());
		Assert.assertEquals(BIN_PRIORITY_VALUE, bins.getContent().get(0).getPriority());
		Assert.assertNull(bins.getContent().get(0).getDescription());
		Assert.assertEquals(BIN_SIZE_VALUE_10, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_SIZE));
	}

	@Test
	@Transactional
	public void testSearchBinBySchemalessAndStaticAttributes()
	{
		factory.setAllowNull(false);
		final StockroomLocationData location = inventoryTestHelper.createStockRoomLocation(LOCATION_ID_VALUE);
		final BinData bin = inventoryTestHelper.createBin(location, BIN_CODE_VALUE_1, BIN_PRIORITY_VALUE, BIN_DESCRIPTION_VALUE);

		bin.setProperty(SchemalessInventoryQueryFactory.BIN_SIZE, BIN_SIZE_VALUE_10);

		this.persistenceManager.flush();

		final BinQueryObject queryObject = new BinQueryObject();
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_SIZE, BIN_SIZE_VALUE_10);

		final Page<BinData> bins = inventoryService.findPagedBinsByQuery(queryObject);

		Assert.assertNotNull(bins);
		Assert.assertNotNull(bins.getContent());
		Assert.assertEquals(1, bins.getContent().size());

		Assert.assertEquals(LOCATION_ID_VALUE, bins.getContent().get(0).getStockroomLocation().getLocationId());
		Assert.assertEquals(BIN_CODE_VALUE_1, bins.getContent().get(0).getBinCode());
		Assert.assertEquals(BIN_PRIORITY_VALUE, bins.getContent().get(0).getPriority());
		Assert.assertEquals(BIN_DESCRIPTION_VALUE, bins.getContent().get(0).getDescription());
		Assert.assertEquals(BIN_SIZE_VALUE_10, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_SIZE));
	}

	@Test
	@Transactional
	public void testSearchBinBySchemalessAttributeWithNull()
	{
		factory.setAllowNull(true);
		final StockroomLocationData location = inventoryTestHelper.createStockRoomLocation(LOCATION_ID_VALUE);
		inventoryTestHelper.createBin(location, BIN_CODE_VALUE_1, BIN_PRIORITY_VALUE, null);

		this.persistenceManager.flush();

		final BinQueryObject queryObject = new BinQueryObject();

		final Page<BinData> bins = inventoryService.findPagedBinsByQuery(queryObject);

		Assert.assertNotNull(bins);
		Assert.assertNotNull(bins.getContent());
		Assert.assertEquals(1, bins.getContent().size());

		Assert.assertEquals(LOCATION_ID_VALUE, bins.getContent().get(0).getStockroomLocation().getLocationId());
		Assert.assertEquals(BIN_CODE_VALUE_1, bins.getContent().get(0).getBinCode());
		Assert.assertEquals(BIN_PRIORITY_VALUE, bins.getContent().get(0).getPriority());
		Assert.assertEquals(null, bins.getContent().get(0).getDescription());
		Assert.assertEquals(null, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_SIZE));
	}

	@Test
	@Transactional
	public void testSearchBinBySchemalessAttributeWithNullString()
	{
		factory.setAllowNull(true);
		final StockroomLocationData location = inventoryTestHelper.createStockRoomLocation(LOCATION_ID_VALUE);
		final BinData bin = inventoryTestHelper.createBin(location, BIN_CODE_VALUE_1, BIN_PRIORITY_VALUE, BIN_DESCRIPTION_VALUE);

		bin.setProperty(SchemalessInventoryQueryFactory.BIN_SIZE, "");

		this.persistenceManager.flush();

		final BinQueryObject queryObject = new BinQueryObject();
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_SIZE, "");
		queryObject.setDescription(BIN_DESCRIPTION_VALUE);

		final Page<BinData> bins = inventoryService.findPagedBinsByQuery(queryObject);

		Assert.assertNotNull(bins);
		Assert.assertNotNull(bins.getContent());
		Assert.assertEquals(1, bins.getContent().size());

		Assert.assertEquals(LOCATION_ID_VALUE, bins.getContent().get(0).getStockroomLocation().getLocationId());
		Assert.assertEquals(BIN_CODE_VALUE_1, bins.getContent().get(0).getBinCode());
		Assert.assertEquals(BIN_PRIORITY_VALUE, bins.getContent().get(0).getPriority());
		Assert.assertEquals(BIN_DESCRIPTION_VALUE, bins.getContent().get(0).getDescription());
		Assert.assertEquals("", bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_SIZE));
	}

	@Test
	@Transactional
	public void testSearchByMultipleSchemalessAtts()
	{
		factory.setAllowNull(false);
		final StockroomLocationData location = inventoryTestHelper.createStockRoomLocation(LOCATION_ID_VALUE);
		final BinData bin = inventoryTestHelper.createBin(location, BIN_CODE_VALUE_1);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_SIZE, BIN_SIZE_VALUE_10);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_COLOR, BIN_COLOR_VALUE);

		this.persistenceManager.flush();

		final BinQueryObject queryObject = new BinQueryObject();
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_SIZE, BIN_SIZE_VALUE_10);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_COLOR, BIN_COLOR_VALUE);

		final Page<BinData> bins = inventoryService.findPagedBinsByQuery(queryObject);

		Assert.assertEquals(LOCATION_ID_VALUE, bins.getContent().get(0).getStockroomLocation().getLocationId());
		Assert.assertEquals(BIN_CODE_VALUE_1, bins.getContent().get(0).getBinCode());
		Assert.assertEquals(BIN_SIZE_VALUE_10, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_SIZE));
		Assert.assertEquals(BIN_COLOR_VALUE, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_COLOR));
	}

	@Test
	@Transactional
	public void testSearchBinByMultipleSchemalessAndStaticAttributes()
	{
		factory.setAllowNull(false);
		final StockroomLocationData location = inventoryTestHelper.createStockRoomLocation(LOCATION_ID_VALUE);
		final BinData bin = inventoryTestHelper.createBin(location, BIN_CODE_VALUE_1, BIN_PRIORITY_VALUE, BIN_DESCRIPTION_VALUE);

		bin.setProperty(SchemalessInventoryQueryFactory.BIN_SIZE, BIN_SIZE_VALUE_10);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_COLOR, BIN_COLOR_VALUE);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_SHAPE, BIN_SHAPE_VALUE);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_WEIGHT, BIN_WEIGHT_VALUE);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_IS_FRAGILE, BIN_IS_FRAGILE_VALUE);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_IS_WEAVED, BIN_IS_WEAVED_VALUE);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_IS_COVERED, BIN_IS_COVERED_VALUE);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_IS_PRETTY, BIN_IS_PRETTY_VALUE);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_WIDTH, BIN_WIDTH_VALUE);
		bin.setProperty(SchemalessInventoryQueryFactory.BIN_HEIGHT, BIN_HEIGHT_VALUE);

		this.persistenceManager.flush();

		final BinQueryObject queryObject = new BinQueryObject();
		queryObject.setDescription(BIN_DESCRIPTION_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_SIZE, BIN_SIZE_VALUE_10);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_COLOR, BIN_COLOR_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_SHAPE, BIN_SHAPE_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_WEIGHT, BIN_WEIGHT_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_IS_FRAGILE, BIN_IS_FRAGILE_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_IS_WEAVED, BIN_IS_WEAVED_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_IS_COVERED, BIN_IS_COVERED_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_IS_PRETTY, BIN_IS_PRETTY_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_WIDTH, BIN_WIDTH_VALUE);
		queryObject.setValue(SchemalessInventoryQueryFactory.BIN_HEIGHT, BIN_HEIGHT_VALUE);

		final Page<BinData> bins = inventoryService.findPagedBinsByQuery(queryObject);

		Assert.assertNotNull(bins);
		Assert.assertNotNull(bins.getContent());
		Assert.assertEquals(1, bins.getContent().size());
		// static
		Assert.assertEquals(LOCATION_ID_VALUE, bins.getContent().get(0).getStockroomLocation().getLocationId());
		Assert.assertEquals(BIN_CODE_VALUE_1, bins.getContent().get(0).getBinCode());
		Assert.assertEquals(BIN_PRIORITY_VALUE, bins.getContent().get(0).getPriority());
		Assert.assertEquals(BIN_DESCRIPTION_VALUE, bins.getContent().get(0).getDescription());
		// schemaless
		Assert.assertEquals(BIN_SIZE_VALUE_10, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_SIZE));
		Assert.assertEquals(BIN_COLOR_VALUE, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_COLOR));
		Assert.assertEquals(BIN_SHAPE_VALUE, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_SHAPE));
		Assert.assertEquals(BIN_WEIGHT_VALUE, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_WEIGHT));
		Assert.assertEquals(BIN_IS_FRAGILE_VALUE,
				bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_IS_FRAGILE));
		Assert.assertEquals(BIN_IS_WEAVED_VALUE, bins.getContent().get(0)
				.getProperty(SchemalessInventoryQueryFactory.BIN_IS_WEAVED));
		Assert.assertEquals(BIN_IS_COVERED_VALUE,
				bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_IS_COVERED));
		Assert.assertEquals(BIN_IS_PRETTY_VALUE, bins.getContent().get(0)
				.getProperty(SchemalessInventoryQueryFactory.BIN_IS_PRETTY));
		Assert.assertEquals(BIN_WIDTH_VALUE, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_WIDTH));
		Assert.assertEquals(BIN_HEIGHT_VALUE, bins.getContent().get(0).getProperty(SchemalessInventoryQueryFactory.BIN_HEIGHT));

	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}
}
