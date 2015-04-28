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
package com.hybris.oms.service.orderline;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.order.business.builders.OrderLineBuilder;
import com.hybris.oms.service.order.impl.DefaultOrderService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-service-spring-test.xml")
public class OrderLineUniqueIdTest
{

	private static final String CONSTANT_2 = "2";
	private static final String CONSTANT_1 = "1";
	@Autowired
	private PersistenceManager persistenceManager;
	@Autowired
	private final DefaultOrderService serviceUnderTest = new DefaultOrderService();

	@Test
	@Transactional
	public void testShouldAllowSameOrderLineIdForDifferentOrders()
	{
		// creating 2 orders with the same order line id
		this.buildOrder(CONSTANT_1, CONSTANT_1);
		this.buildOrder(CONSTANT_2, CONSTANT_1);

		this.serviceUnderTest.flush();
		final List<OrderData> orders = this.persistenceManager.search(this.persistenceManager.createCriteriaQuery(OrderData.class));
		Assert.assertEquals(2, orders.size());
	}

	@Test(expected = PrimaryKeyViolationException.class)
	@Transactional
	public void testShouldNOTAllowSameOrderLineIdAndOrderIdForDifferentOrders()
	{
		// creating 2 orders with the same order/ order line ids
		this.buildOrder(CONSTANT_1, CONSTANT_1);
		this.buildOrder(CONSTANT_1, CONSTANT_1);

		this.serviceUnderTest.flush();
		Assert.fail();

	}

	private OrderData buildOrder(final String orderId, final String orderLineId)
	{
		final OrderData order = this.persistenceManager.create(OrderData.class);
		order.setOrderId(orderId);

		order.setUsername("someUser");
		order.setFirstName("firstName");
		order.setLastName("lastName");
		order.setEmailid("test@test.de");

		order.setIssueDate(Calendar.getInstance().getTime());
		order.setShippingAddress(new AddressVT("test", "test", "test", "test", "test", 0d, 0d, "test", "test", null, null));
		order.setShippingMethod("test");

		final OrderLineData orderLine = buildOrderLine(orderLineId);
		orderLine.setMyOrder(order);
		order.setOrderLines(Lists.newArrayList(orderLine));

		final ShippingAndHandlingData shippingAndHandlingData = this.persistenceManager.create(ShippingAndHandlingData.class);
		shippingAndHandlingData.setOrderId(order.getOrderId());
		shippingAndHandlingData.setShippingPrice(new PriceVT("test", 0d, "test", 0d, "test", 0d));
		order.setShippingAndHandling(shippingAndHandlingData);


		final PaymentInfoData paymentInfo = this.persistenceManager.create(PaymentInfoData.class);
		paymentInfo.setBillingAddress(new AddressVT("test", "test", "test", "test", "test", 0d, 0d, "test", "test", null, null));
		paymentInfo.setMyOrder(order);
		paymentInfo.setPaymentInfoType("test");
		paymentInfo.setAuthUrl("test");
		order.setPaymentInfos(Lists.newArrayList(paymentInfo));
		return order;
	}

	private OrderLineData buildOrderLine(final String orderLineId)

	{
		final OrderLineData orderLine = this.persistenceManager.create(OrderLineData.class);

		orderLine.setOrderLineId(orderLineId);
		orderLine.setSkuId("test");
		OrderLineBuilder.setQuantitiesAndUnits(orderLine, "test", 0);
		orderLine.setTaxCategory("test");
		orderLine.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		return orderLine;
	}
}
