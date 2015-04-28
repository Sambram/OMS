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
package com.hybris.oms.service.order.procs;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.order.impl.OlqDataStaticUtils;

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
public class OrderLineQuantityDataProcsTest
{
	private static final String OLQ_HYBRIS_ID_1 = "single|OrderLineQuantityData|6";
	private static final String OLQ_HYBRIS_ID_2 = "single|OrderLineQuantityData|7";
	private static final String OLQ_HYBRIS_ID_3 = "single|OrderLineQuantityData|8";
	private static final String OLQ_HYBRIS_ID_4 = "single|OrderLineQuantityData|15";

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	@Autowired
	private ImportService importService;
	private OrderLineQuantityData olq1;
	private OrderLineQuantityData olq2;
	private OrderLineQuantityData olq3;

	private OrderLineQuantityData olq4;

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.olq1 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_1));
		this.olq2 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_2));
		this.olq3 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_3));
		this.olq4 = this.persistenceManager.get(HybrisId.valueOf(OLQ_HYBRIS_ID_4));
	}

	@Test
	@Transactional
	public void testCalculateOrderLineQuantitySubtotalAmount()
	{
		final AmountVT amount = OlqDataStaticUtils.calculateOrderLineQuantitySubtotalAmount(this.olq1);
		Assert.assertEquals(50d, amount.getValue(), 0.001d);
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void testCalculateOrderLineQuantitySubtotalAmountThrowsWhenNull()
	{
		OlqDataStaticUtils.calculateOrderLineQuantitySubtotalAmount(null);
	}

	@Test
	@Transactional
	public void testCalculateOrderLineQuantityTaxAmount()
	{
		final AmountVT amount = OlqDataStaticUtils.calculateOrderLineQuantityTaxAmount(this.olq1);
		Assert.assertEquals(5d, amount.getValue(), 0.001d);
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void testCalculateOrderLineQuantityTaxAmountThrowsWhenNull()
	{
		OlqDataStaticUtils.calculateOrderLineQuantityTaxAmount(null);
	}

}
