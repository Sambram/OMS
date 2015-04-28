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
package com.hybris.oms.service.sourcing.strategy.impl;


import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.impl.DefaultSourcingService;
import com.hybris.oms.service.sourcing.strategy.OrderLineSplitStrategy;

import java.util.List;

import org.fest.assertions.Assertions;
import org.fest.assertions.MapAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


/**
 * Integration test for {@link DefaultSourcingService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultOrderLineSplitStrategyIntegrationTest
{

	private static final String ORDER_ID = "100";
	private static final String OL_ID = "18";

	private static final String TEST_PROP_KEY = "test";
	private static final Integer TEST_PROP_VALUE = 51;
	private static final String OL_ATT_ID = "category";
	private static final String OL_ATT_DESC = "digital";

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private OrderLineSplitStrategy strategy;

	@Autowired
	private ImportService importService;

	private OrderLineData orderLine;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(new ClasspathScanningResourceFetcher()
				.fetchResources("/order/test-order-data-import.mcsv")[0]);
		final OrderData order = persistenceManager.getByIndex(OrderData.UX_ORDERS_ORDERID, ORDER_ID);
		final OrderLineData ol1 = persistenceManager.getByIndex(OrderLineData.UQ_ORDER_ORDERLINEID, order, OL_ID);
		ol1.setProperty(TEST_PROP_KEY, TEST_PROP_VALUE);
		final OrderLineAttributeData attribute = persistenceManager.create(OrderLineAttributeData.class);
		attribute.setAttributeId(OL_ATT_ID);
		attribute.setDescription(OL_ATT_DESC);
		attribute.setOrderLine(ol1);
		ol1.getOrderLineAttributes().add(attribute);
		orderLine = ol1;
		persistenceManager.flush();
	}

	@Test
	@Transactional
	public void shouldPopulatePropertiesForLines()
	{
		final List<SourcingLine> result = strategy.getSourcingLines(orderLine);
		Assert.assertEquals(1, result.size());
		final SourcingLine line = result.get(0);
		Assertions.assertThat(line.getProperties()).includes(MapAssert.entry(TEST_PROP_KEY, TEST_PROP_VALUE),
				MapAssert.entry(OL_ATT_ID, OL_ATT_DESC));
		Assert.assertEquals(orderLine.getSkuId(), line.getSku());
		Assert.assertEquals(orderLine.getQuantityUnassignedValue(), line.getQuantity());
		Assert.assertEquals(orderLine.getOrderLineId(), line.getLineId());
		Assert.assertEquals(orderLine.getOrderLineId(), line.getOrderLineId());
		Assert.assertNull(line.getPickupLocationId());
		Assertions.assertThat(line.getLocationRoles()).containsOnly("SHIPPING");
		Assert.assertEquals(orderLine.getMyOrder().getShippingAddress().getCountryIso3166Alpha2Code(), line.getCountry());
	}
}
