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
package com.hybris.oms.service.workflow.worker.order;

import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OrderDataStaticUtils.class})
@PowerMockIgnore({"org.slf4j.*", "com.sun.org.apache.xerces.*"})
public class FulfillmentCompletionWorkItemWorkerTest
{
	private static final String ORDER_ID = "ORDER_ID";

	@InjectMocks
	private final FulfillmentCompletionWorkItemWorker worker = new FulfillmentCompletionWorkItemWorker();

	@Mock
	private OrderService mockOrderService;
	@Mock
	private OrderData mockOrder;

	private Map<String, Object> parameters;

	@Before
	public void setUp()
	{
		PowerMockito.mockStatic(OrderDataStaticUtils.class);

		Mockito.when(mockOrderService.getOrderByOrderId(ORDER_ID)).thenReturn(mockOrder);

		parameters = new HashMap<>();
		parameters.put(KEY_ORDER_ID, ORDER_ID);
	}

	@Test
	public void shouldBeSourcingComplete()
	{
		Mockito.when(OrderDataStaticUtils.isCompletelySourced(mockOrder)).thenReturn(Boolean.TRUE);

		worker.executeInternal(parameters);
		Assert.assertFalse((boolean) parameters.get(CoreWorkflowConstants.KEY_OUTCOME_NAME));
	}

	@Test
	public void shouldBeSourcingIncomplete()
	{
		Mockito.when(OrderDataStaticUtils.isCompletelySourced(mockOrder)).thenReturn(Boolean.FALSE);

		worker.executeInternal(parameters);
		Assert.assertTrue((boolean) parameters.get(CoreWorkflowConstants.KEY_OUTCOME_NAME));
	}
}
