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
package com.hybris.oms.facade.conversion.impl.order;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.service.managedobjects.order.OrderLineData;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OrderLineConverterTest
{

	private static final double NUMBER_DOUBLE = 1d;
	private static final String TAX_CATEGORY = "taxCategory";
	private static final int NUMBER_INT = 1;
	private static final String CODE = "code";
	private static final String STATUS = "status";
	private static final String STRING_NUMBER = "1";
	private static final String NOTE = "note";
	private static final Set<LocationRole> LOCATION_ROLES = ImmutableSet.of(LocationRole.SHIPPING);

	@Autowired
	private Converter<OrderLineData, OrderLine> orderLineConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private OrderTestUtils orderTestUtils;
	private OrderLineData orderLineData;

	@Before
	public void setUp()
	{
		// given
		this.orderTestUtils = new OrderTestUtils();
		this.orderTestUtils.createObjects(this.persistenceManager);
		this.orderLineData = this.prepareData();
	}

	@Transactional
	@Test
	public void convertingNotFlushedOrderLineData()
	{
		// when
		final OrderLine orderLine = this.orderLineConverter.convert(this.orderLineData);

		// then
		assertValid(orderLine);
	}

	@Transactional
	@Test
	public void convertingFlushedOrderLineData()
	{
		// when
		final OrderLine orderLine = this.orderLineConverter.convert(this.orderLineData);

		// then
		assertValid(orderLine);
	}

	private OrderLineData prepareData()
	{
		final OrderLineData resultData = this.persistenceManager.create(OrderLineData.class);
		resultData.setNote(NOTE);
		resultData.setOrderLineId(STRING_NUMBER);
		resultData.setOrderLineStatus(STATUS);
		resultData.setQuantityUnassignedUnitCode(CODE);
		resultData.setQuantityUnassignedValue(NUMBER_INT);
		resultData.setQuantityUnitCode(CODE);
		resultData.setQuantityValue(NUMBER_INT);
		resultData.setSkuId(STRING_NUMBER);
		resultData.setTaxCategory(TAX_CATEGORY);
		resultData.setUnitPriceCurrencyCode(CODE);
		resultData.setUnitPriceValue(NUMBER_DOUBLE);
		resultData.setUnitTaxCurrencyCode(CODE);
		resultData.setUnitTaxValue(NUMBER_DOUBLE);
		resultData.setPickupStoreId(STRING_NUMBER);
		resultData.setMyOrder(this.orderTestUtils.getOrderData());
		resultData.setOrderLineAttributes(ImmutableList.of(this.orderTestUtils.getOrderLineAttributeData()));
		resultData.setOrderLineQuantities(ImmutableList.of(this.orderTestUtils.getOrderLineQuantityData()));
		resultData
				.setLocationRoles(ImmutableSet.of(com.hybris.oms.service.managedobjects.inventory.LocationRole.SHIPPING.getCode()));
		this.orderTestUtils.getOrderLineQuantityData().setOrderLine(resultData);

		return resultData;
	}

	private void assertValid(final OrderLine orderLine)
	{
		Assert.assertEquals(NOTE, orderLine.getNote());
		Assert.assertEquals(STRING_NUMBER, orderLine.getOrderLineId());
		Assert.assertEquals(STATUS, orderLine.getOrderLineStatus());
		Assert.assertEquals(CODE, orderLine.getQuantityUnassigned().getUnitCode());
		Assert.assertEquals(NUMBER_INT, orderLine.getQuantityUnassigned().getValue());
		Assert.assertEquals(CODE, orderLine.getQuantity().getUnitCode());
		Assert.assertEquals(NUMBER_INT, orderLine.getQuantity().getValue());
		Assert.assertEquals(TAX_CATEGORY, orderLine.getTaxCategory());
		Assert.assertEquals(CODE, orderLine.getUnitPrice().getCurrencyCode());
		Assert.assertEquals(NUMBER_DOUBLE, orderLine.getUnitPrice().getValue(), 0.001d);
		Assert.assertEquals(CODE, orderLine.getUnitTax().getCurrencyCode());
		Assert.assertEquals(NUMBER_DOUBLE, orderLine.getUnitTax().getValue(), 0.001d);
		Assert.assertEquals(STRING_NUMBER, orderLine.getPickupStoreId());
		Assert.assertEquals(STRING_NUMBER, orderLine.getSkuId());

		Assert.assertNotNull(orderLine.getOrderLineQuantities());
		Assert.assertEquals(1, orderLine.getOrderLineQuantities().size());
		Assert.assertEquals(this.orderLineData.getOrderLineQuantities().size(), orderLine.getOrderLineQuantities().size());
		Assert.assertNotNull(orderLine.getOrderLineAttributes());
		Assert.assertEquals(1, orderLine.getOrderLineAttributes().size());
		Assert.assertEquals(this.orderLineData.getOrderLineAttributes().size(), orderLine.getOrderLineAttributes().size());
		Assert.assertEquals(LOCATION_ROLES, orderLine.getLocationRoles());
	}

}
