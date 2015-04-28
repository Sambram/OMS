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
package com.hybris.oms.service.shipment.strategy.impl;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.shipment.strategy.OlqGroupingStrategy;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultOlqGroupingStrategyTest
{
	private static final String OLQ_HYBRIS_ID_1 = "single|OrderLineQuantityData|2";
	private static final String OLQ_HYBRIS_ID_2 = "single|OrderLineQuantityData|3";
	private static final String OLQ_HYBRIS_ID_3 = "single|OrderLineQuantityData|12";
	private static final String OLQ_HYBRIS_ID_4 = "single|OrderLineQuantityData|13";
	private static final String OLQ_HYBRIS_ID_5 = "single|OrderLineQuantityData|6";
	private static final String OLQ_HYBRIS_ID_6 = "single|OrderLineQuantityData|7";
	private static final String OLQ_HYBRIS_ID_7 = "single|OrderLineQuantityData|8";
	private static final String OLQ_HYBRIS_ID_8 = "single|OrderLineQuantityData|15";

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	private OrderLineQuantityData olq1;
	private OrderLineQuantityData olq2;
	private OrderLineQuantityData olq3;
	private OrderLineQuantityData olq4;
	private OrderLineQuantityData olq5;
	private OrderLineQuantityData olq6;
	private OrderLineQuantityData olq7;
	private OrderLineQuantityData olq8;

	@Autowired
	private ImportService importService;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private OlqGroupingStrategy olqGroupingStrategy;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.olq1 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_1));
		this.olq2 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_2));
		this.olq3 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_3));
		this.olq4 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_4));
		this.olq5 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_5));
		this.olq6 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_6));
		this.olq7 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_7));
		this.olq8 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_8));
	}

	@Test
	@Transactional
	public void testGroupOlqsByLocationAndStatus()
	{
		final List<OrderLineQuantityData> olqs = Lists.newArrayList(this.olq1, this.olq2, this.olq3, this.olq4);
		final List<List<OrderLineQuantityData>> groups = olqGroupingStrategy.groupOlqs(olqs);

		Assert.assertEquals(2, groups.size());
		Assert.assertEquals(2, groups.get(0).size());
		
		Assert.assertTrue(groups.get(0).contains(this.olq1));
		Assert.assertTrue(groups.get(0).contains(this.olq2));
		
		Assert.assertTrue(groups.get(1).contains(this.olq3));
		Assert.assertTrue(groups.get(1).contains(this.olq4));
	}

	@Test
	@Transactional
	public void testGroupOlqsPickupStoreIdsByLocationAndStatus()
	{
		final List<OrderLineQuantityData> olqs = Lists.newArrayList(this.olq5, this.olq6, this.olq7, this.olq8);
		final List<List<OrderLineQuantityData>> groups = olqGroupingStrategy.groupOlqs(olqs);

		Assert.assertEquals(4, groups.size());
		Assert.assertEquals(1, groups.get(0).size());
		
		Assert.assertTrue(groups.get(0).contains(this.olq5));
		Assert.assertTrue(groups.get(1).contains(this.olq6));
		Assert.assertTrue(groups.get(2).contains(this.olq7));
		Assert.assertTrue(groups.get(3).contains(this.olq8));
	}

}
