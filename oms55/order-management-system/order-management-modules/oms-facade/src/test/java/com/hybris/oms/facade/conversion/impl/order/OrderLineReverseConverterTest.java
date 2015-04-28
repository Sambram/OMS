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
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.order.OrderLineData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OrderLineReverseConverterTest
{

	private static final double NUMBER_DOUBLE = 1d;
	private static final String TAX_CATEGORY = "taxCategory";
	private static final int NUMBER_INT = 1;
	private static final String CODE = "code";
	private static final String STATUS = "status";
	private static final String STRING_NUMBER = "1";
	private static final String NOTE = "note";
	private static final Set<String> LOCATION_ROLES = ImmutableSet.of(LocationRole.SHIPPING.name());

	@Autowired
	private Converter<OrderLine, OrderLineData> orderLineReverseConverter;

	@Transactional
	@Test
	public void reverseConvertingNotFlushedOrderLineData()
	{
		// given
		final OrderLine orderLine = new OrderLine();
		orderLine.setNote(NOTE);
		orderLine.setOrderLineId(STRING_NUMBER);
		orderLine.setOrderLineStatus(STATUS);
		orderLine.setQuantity(new Quantity(CODE, NUMBER_INT));
		orderLine.setQuantityUnassigned(new Quantity(CODE, NUMBER_INT));
		orderLine.setSkuId(STRING_NUMBER);
		orderLine.setTaxCategory(TAX_CATEGORY);
		orderLine.setUnitPrice(new Amount(CODE, NUMBER_DOUBLE));
		orderLine.setUnitTax(new Amount(CODE, NUMBER_DOUBLE));
		orderLine.setPickupStoreId(STRING_NUMBER);
		orderLine.setLocationRoles(ImmutableSet.of(com.hybris.oms.domain.locationrole.LocationRole.SHIPPING));

		final List<OrderLineAttribute> orderLineAttributes = new ArrayList<OrderLineAttribute>();
		orderLineAttributes.add(new OrderLineAttribute("", ""));
		orderLine.setOrderLineAttributes(new ArrayList<OrderLineAttribute>());

		// when
		final OrderLineData orderLineData = this.orderLineReverseConverter.convert(orderLine);

		// then
		Assert.assertTrue(orderLineData.getId() == null);
		Assert.assertEquals(NOTE, orderLineData.getNote());
		Assert.assertEquals(STRING_NUMBER, orderLineData.getOrderLineId());
		Assert.assertEquals(STATUS, orderLineData.getOrderLineStatus());
		Assert.assertEquals(CODE, orderLineData.getQuantityUnassignedUnitCode());
		Assert.assertEquals(NUMBER_INT, orderLineData.getQuantityUnassignedValue());
		Assert.assertEquals(CODE, orderLineData.getQuantityUnitCode());
		Assert.assertEquals(NUMBER_INT, orderLineData.getQuantityValue());
		Assert.assertEquals(TAX_CATEGORY, orderLineData.getTaxCategory());
		Assert.assertEquals(CODE, orderLineData.getUnitPriceCurrencyCode());
		Assert.assertEquals(NUMBER_DOUBLE, orderLineData.getUnitPriceValue(), 0.001d);
		Assert.assertEquals(CODE, orderLineData.getUnitTaxCurrencyCode());
		Assert.assertEquals(NUMBER_DOUBLE, orderLineData.getUnitTaxValue(), 0.001d);
		Assert.assertEquals(STRING_NUMBER, orderLineData.getPickupStoreId());
		Assert.assertEquals(STRING_NUMBER, orderLineData.getSkuId());
		Assert.assertEquals(LOCATION_ROLES, orderLineData.getLocationRoles());
		Assert.assertNotNull(orderLineData.getOrderLineAttributes());
	}
}
