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
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;

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

import com.google.common.collect.Lists;


/**
 *
 */
@PrepareForTest(OrderDataStaticUtils.class)
@PowerMockIgnore("org.slf4j.*")
@RunWith(PowerMockRunner.class)
public class ShipmentsCompletionWorkItemWorkerTest
{
	private static final String ORDER_ID = "ORDER_ID";
	private static final long SHIPMENT_ID_1 = 1L;
	private static final long SHIPMENT_ID_2 = 2L;

	@InjectMocks
	private final ShipmentsCompletionWorkItemWorker worker = new ShipmentsCompletionWorkItemWorker();

	@Mock
	private OrderService mockOrderService;
	@Mock
	private ShipmentService mockShipmentService;
	@Mock
	private WorkflowExecutor<ShipmentData> mockWorkflowExecutor;

	@Mock
	private ShipmentData mockShipment_1;
	@Mock
	private ShipmentData mockShipment_2;
	@Mock
	private OrderData mockOrder;

	private Map<String, Object> parameters;

	@Before
	public void setUp()
	{
		PowerMockito.mockStatic(OrderDataStaticUtils.class);

		Mockito.when(mockOrderService.getOrderByOrderId(ORDER_ID)).thenReturn(mockOrder);
		Mockito.when(mockShipmentService.findShipmentsByOrder(mockOrder)).thenReturn(
				Lists.newArrayList(mockShipment_1, mockShipment_2));

		Mockito.when(mockShipment_1.getShipmentId()).thenReturn(SHIPMENT_ID_1);
		Mockito.when(mockShipment_2.getShipmentId()).thenReturn(SHIPMENT_ID_2);

		parameters = new HashMap<>();
		parameters.put(KEY_ORDER_ID, ORDER_ID);
	}

	@Test
	public void shouldBeAllShipmentWorkflowsComplete()
	{
		Mockito.when(OrderDataStaticUtils.isCompletelySourced(mockOrder)).thenReturn(Boolean.TRUE);
		Mockito.when(mockWorkflowExecutor.isWorkflowInProgress(Mockito.any(ShipmentData.class))).thenReturn(Boolean.FALSE);
		worker.executeInternal(parameters);
		Assert.assertTrue((boolean) parameters.get(CoreWorkflowConstants.KEY_OUTCOME_NAME));
	}

	@Test
	public void shouldBeWorkflowIncomplete_Workflow1()
	{
		Mockito.when(OrderDataStaticUtils.isCompletelySourced(mockOrder)).thenReturn(Boolean.TRUE);
		Mockito.when(mockWorkflowExecutor.isWorkflowInProgress(mockShipment_1)).thenReturn(Boolean.TRUE);
		Mockito.when(mockWorkflowExecutor.isWorkflowInProgress(mockShipment_2)).thenReturn(Boolean.FALSE);

		worker.executeInternal(parameters);
		Assert.assertFalse((boolean) parameters.get(CoreWorkflowConstants.KEY_OUTCOME_NAME));
	}

	@Test
	public void shouldBeWorkflowIncomplete_Workflow2()
	{
		Mockito.when(OrderDataStaticUtils.isCompletelySourced(mockOrder)).thenReturn(Boolean.TRUE);
		Mockito.when(mockWorkflowExecutor.isWorkflowInProgress(mockShipment_1)).thenReturn(Boolean.FALSE);
		Mockito.when(mockWorkflowExecutor.isWorkflowInProgress(mockShipment_2)).thenReturn(Boolean.TRUE);

		worker.executeInternal(parameters);
		Assert.assertFalse((boolean) parameters.get(CoreWorkflowConstants.KEY_OUTCOME_NAME));
	}

}
