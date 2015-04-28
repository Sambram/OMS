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
package com.hybris.oms.facade.order;


import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.workflow.executor.order.OrderWorkflowExecutor;


@RunWith(PowerMockRunner.class)
@PrepareForTest(OrderDataStaticUtils.class)
@PowerMockIgnore({"javax.management.*", "javax.xml.parsers.*", "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
		"org.slf4j.*"})
public class DefaultOrderFacadeTest
{

	private final OrderLineQuantityStatus olqs = new OrderLineQuantityStatus();
	private final Order order = new Order();

	@InjectMocks
	private final DefaultOrderFacade orderFacadeUnderTest = new DefaultOrderFacade();
	@Mock
	private OrderLineQuantityStatusData mockedOlqs;
	@Mock
	private OrderService mockedOrderService;
	@Mock
	private Converter<OrderLineQuantityStatusData, OrderLineQuantityStatus> orderLineQuantityStatusConverter;
	@Mock
	private Converter<OrderLineQuantityStatus, OrderLineQuantityStatusData> orderLineQuantityStatusReverseConverter;
	@Mock
	private Populator<OrderLineQuantityStatus, OrderLineQuantityStatusData> orderLineQuantityStatusReversePopulator;
	@Mock
	private Converters converters;
	@Mock
	private Converter<OrderData, Order> orderConverter;
	@Mock
	private Converter<Order, OrderData> orderReverseConverter;

	/**
	 * There seems to be a bug in PowerMock. The order validator mock will evaluate to null unless we add another
	 * validator mock.
	 */
	@SuppressWarnings("PMD.UnusedPrivateField")
	@Mock
	private Validator<OrderLine> orderLineValidator;
	@SuppressWarnings("PMD.UnusedPrivateField")
	@Mock
	private Validator<Order> orderValidator;
	@Mock
	private OrderWorkflowExecutor orderWorkflowExecutor;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(OrderDataStaticUtils.class);
		this.populateOrder();
	}

	@Test
	public final void testCreateOrderLineQuantityStatus()
	{
		Mockito.when(this.orderLineQuantityStatusReverseConverter.convert(null)).thenReturn(this.mockedOlqs);
		this.orderFacadeUnderTest.createOrderLineQuantityStatus(null);
		Mockito.verify(this.orderLineQuantityStatusReverseConverter).convert(null);
		Mockito.verify(this.orderLineQuantityStatusConverter).convert(this.mockedOlqs);
	}

	@Test
	@PowerMockIgnore({"ch.qos.logback.*", "org.slf4j.*", "com.sun.org.*"})
	public final void testCreateValidOrder()
	{
		Mockito.when(OrderDataStaticUtils.hasValidShippingAddressGeocodes(Mockito.any(OrderData.class))).thenReturn(true);
		final Order myMockedOrder = Mockito.mock(Order.class);
		final OrderData myMockedOrderData = Mockito.mock(OrderData.class);

		Mockito.when(orderReverseConverter.convert(myMockedOrder)).thenReturn(myMockedOrderData);

		this.orderFacadeUnderTest.createOrder(myMockedOrder);
		Mockito.verify(this.orderReverseConverter).convert(myMockedOrder);
		Mockito.verify(orderWorkflowExecutor).execute(Mockito.any(OrderData.class));
	}

	@Test
	public final void testFindAllOrderLineQuantityStatuses()
	{
		Mockito.when(this.converters.convertAll(null, this.orderLineQuantityStatusConverter)).thenReturn(Arrays.asList(this.olqs));
		this.orderFacadeUnderTest.findAllOrderLineQuantityStatuses();
		Mockito.verify(this.mockedOrderService).getAllOrderLineQuantityStatuses();
	}

	@Test
	public final void testGetOrderWithOrderId()
	{
		Mockito.when(this.orderConverter.convert(null)).thenReturn(this.order);
		this.orderFacadeUnderTest.getOrderByOrderId("testGetOrderWithCode");
		Mockito.verify(this.mockedOrderService).getOrderByOrderId("testGetOrderWithCode");
	}

	@Test
	public final void testUpdateOrderLineQuantityStatus()
	{
		Mockito.when(this.mockedOrderService.getOrderLineQuantityStatusByStatusCode("status")).thenReturn(this.mockedOlqs);
		final OrderLineQuantityStatus orderLineQuantityStatus = new OrderLineQuantityStatus();
		orderLineQuantityStatus.setStatusCode("status");

		this.orderFacadeUnderTest.updateOrderLineQuantityStatus(orderLineQuantityStatus);
		Mockito.verify(this.orderLineQuantityStatusReversePopulator).populate(orderLineQuantityStatus, this.mockedOlqs);
		Mockito.verify(this.orderLineQuantityStatusConverter).convert(this.mockedOlqs);
	}

	private void populateOrder()
	{
		this.order.setShippingAddress(new Address());
	}

}
