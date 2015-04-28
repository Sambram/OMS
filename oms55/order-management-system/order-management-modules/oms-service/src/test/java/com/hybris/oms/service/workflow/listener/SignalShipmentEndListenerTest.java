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
package com.hybris.oms.service.workflow.listener;

import static com.hybris.oms.service.workflow.CoreWorkflowConstants.KEY_TENANT_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.commons.context.Context;
import com.hybris.commons.context.impl.DefaultContext;
import com.hybris.commons.tenant.MultiTenantContextService;
import com.hybris.commons.tenant.TenantContextService;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.workflow.UserTaskForm;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SignalShipmentEndListenerTest
{
	private static final String ORDER_ID = "9";

	@InjectMocks
	private final SignalShipmentEndListener listener = new SignalShipmentEndListener();

	@Mock
	private WorkflowExecutor<OrderData> orderWorkflowExecutor;
	@Mock
	private OrderService orderService;

	@Mock
	private OrderData order;
	@Mock
	private DelegateExecution execution;

	@Spy
	private final Context context = new DefaultContext();
	@Spy
	private final TenantContextService tenantContextService = new MultiTenantContextService(context);

	@Before
	public void setUp()
	{
		when(execution.getVariable(KEY_ORDER_ID)).thenReturn(ORDER_ID);
		when(execution.getVariable(KEY_TENANT_ID)).thenReturn("single");
		when(orderService.getOrderByOrderId(ORDER_ID)).thenReturn(order);
	}

	@Test
	public void shouldNotify()
	{
		listener.notify(execution);
		verify(orderService, times(1)).getOrderByOrderId(ORDER_ID);
		verify(orderWorkflowExecutor, times(1)).completeUserTask(any(OrderData.class), any(UserTaskForm.class));
	}
}
