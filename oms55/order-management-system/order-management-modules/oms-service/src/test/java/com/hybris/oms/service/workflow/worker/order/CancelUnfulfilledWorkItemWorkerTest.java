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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class CancelUnfulfilledWorkItemWorkerTest
{
	private static final String ORDER_ID = "ORDER_ID";

	@InjectMocks
	private final CancelUnfulfilledWorkItemWorker worker = new CancelUnfulfilledWorkItemWorker();

	@Mock
	private OrderService mockOrderService;

	@Mock
	private OrderData mockOrder;

	private Map<String, Object> parameters;

	@Before
	public void setUp()
	{
		when(mockOrderService.getOrderByOrderId(ORDER_ID)).thenReturn(mockOrder);
		doNothing().when(mockOrderService).removeUnassignedQuantities(mockOrder);

		parameters = new HashMap<>();
		parameters.put(KEY_ORDER_ID, ORDER_ID);
	}

	@Test
	public void shouldRemoveUnassignedQuantities()
	{
		worker.executeInternal(parameters);
		verify(mockOrderService).removeUnassignedQuantities(mockOrder);
	}

}
