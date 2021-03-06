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
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.impl.DefaultSourcingResult;

import java.util.Collections;
import java.util.Date;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


/**
 * Integration test for {@link DefaultSourcingResultPersistenceStrategy}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultSourcingResultPersistenceStrategyIntegrationTest
{

	private static final String LOC_ID = "loc1";
	private static final String SKU = "1";
	private static final String ORDER_ID = "100";
	private static final String OL_ID = "18";

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private DefaultSourcingResultPersistenceStrategy strategy;

	@Autowired
	private ImportService importService;

	private OrderData order1;

	@Before
	public void setUp()
	{
		final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
		this.importService.loadMcsvResource(fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(fetcher.fetchResources("/order/test-order-status-data-import.mcsv")[0]);
		order1 = createOrder();
		persistenceManager.flush();
	}

	@Test
	@Transactional
	public void shouldIgnoreEmptyResult()
	{
		strategy.persistSourcingResult(order1, new DefaultSourcingResult());
		final OrderLineData orderLine = persistenceManager.getByIndex(OrderLineData.UQ_ORDER_ORDERLINEID, order1, OL_ID);
		Assert.assertTrue(orderLine.getOrderLineQuantities().isEmpty());
		Assert.assertNull(orderLine.getOrderLineStatus());
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void shouldRejectNullParameter()
	{
		strategy.persistSourcingResult(null, new DefaultSourcingResult());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void shouldRejectInvalidOrderLineId()
	{
		final DefaultSourcingResult result = new DefaultSourcingResult();
		result.setSourcingOlqs(Collections.singletonList(new SourcingOLQ(SKU, 1, LOC_ID, "invalid")));
		strategy.persistSourcingResult(order1, result);
	}

	@Test(expected = IllegalStateException.class)
	@Transactional
	public void shouldRejectInvalidQuantity()
	{
		final DefaultSourcingResult result = new DefaultSourcingResult();
		result.setSourcingOlqs(Collections.singletonList(new SourcingOLQ(SKU, 11, LOC_ID, OL_ID)));
		strategy.persistSourcingResult(order1, result);
	}

	@Test
	@Transactional
	public void shouldPersistSourcingResult()
	{
		final DefaultSourcingResult result = new DefaultSourcingResult();
		result.setSourcingOlqs(Collections.singletonList(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID)));
		strategy.persistSourcingResult(order1, result);
		final OrderLineData orderLine = order1.getOrderLines().get(0);
		Assertions.assertThat(orderLine.getOrderLineQuantities()).isNotEmpty();
		final OrderLineQuantityData olq = orderLine.getOrderLineQuantities().get(0);
		Assert.assertEquals(SKU, orderLine.getSkuId());
		Assert.assertEquals(LOC_ID, olq.getStockroomLocationId());
		Assert.assertEquals(1, olq.getQuantityValue());
		Assert.assertEquals(9, orderLine.getQuantityUnassignedValue());
	}

	private OrderData createOrder()
	{
		final OrderData order = this.createOrder(ORDER_ID);
		this.createShippingAndHandling(order);
		this.createOrderLine(OL_ID, order, 10, 10, SKU);
		this.createPaymentInfo(order);
		return order;
	}

	private OrderData createOrder(final String id)
	{
		final OrderData result = persistenceManager.create(OrderData.class);
		result.setOrderId(id);
		result.setEmailid("user");
		result.setFirstName("first");
		result.setLastName("last");
		result.setIssueDate(new Date());
		result.setShippingAddress(AddressBuilder.anAddress().buildAddressVT());
		result.setShippingMethod("NA");
		return result;
	}

	private OrderLineData createOrderLine(final String orderLineId, final OrderData order, final int quantity,
			final int qtyUnassigned, final String sku)
	{
		final OrderLineData result = persistenceManager.create(OrderLineData.class);
		result.setMyOrder(order);
		result.setOrderLineId(orderLineId);
		result.setSkuId(sku);
		result.setUnitPriceCurrencyCode("EUR");
		result.setUnitPriceValue(0);
		result.setUnitTaxValue(0);
		result.setUnitTaxCurrencyCode("EUR");
		result.setQuantityValue(quantity);
		result.setQuantityUnassignedValue(qtyUnassigned);
		result.setTaxCategory("NA");
		result.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		order.getOrderLines().add(result);
		return result;
	}

	private PaymentInfoData createPaymentInfo(final OrderData order)
	{
		final PaymentInfoData paymentInfo = persistenceManager.create(PaymentInfoData.class);
		paymentInfo.setAuthUrl("test");
		paymentInfo.setPaymentInfoType("test");
		paymentInfo.setBillingAddress(AddressBuilder.anAddress().buildAddressVT());
		paymentInfo.setMyOrder(order);
		order.getPaymentInfos().add(paymentInfo);
		return paymentInfo;
	}

	private ShippingAndHandlingData createShippingAndHandling(final OrderData order)
	{
		final PriceVT shippingPrice = new PriceVT("EUR", 10d, null, 10d, null, 10d);
		final ShippingAndHandlingData shd = persistenceManager.create(ShippingAndHandlingData.class);
		shd.setOrderId(order.getOrderId());
		shd.setShippingPrice(shippingPrice);
		order.setShippingAndHandling(shd);
		return shd;
	}



}
