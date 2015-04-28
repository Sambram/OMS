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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.sourcing.SourcingService;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


/**
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(OrderDataStaticUtils.class)
@PowerMockIgnore("org.slf4j.*")
public class FulfillmentWorkItemWorkerTest
{
	private static final String ORDER_ID = "ORDER_ID";

	@InjectMocks
	private final FulfillmentWorkItemWorker worker = new FulfillmentWorkItemWorker();

	@Mock
	private ShipmentService mockShipmentService;
	@Mock
	private SourcingService mockSourcingService;

	@Mock
	private ShipmentData mockShipmentEarlier;
	@Mock
	private ShipmentData mockShipmentLater;
	@Mock
	private OrderData mockOrder;
	@Mock
	private OrderLineQuantityData mockOlq;

	private Map<String, Object> parameters;

	@Before
	public void setUp()
	{
		when(mockSourcingService.sourceOrder(ORDER_ID)).thenReturn(mockOrder);
		when(mockShipmentEarlier.getCreationTime()).thenReturn(new Date(500));
		when(mockShipmentLater.getCreationTime()).thenReturn(new Date(1000));

		parameters = new HashMap<>();
		parameters.put(KEY_ORDER_ID, ORDER_ID);
	}

	@Test
	public void shouldFailSourcing_Incomplete()
	{
		mockStatic(OrderDataStaticUtils.class);
		when(OrderDataStaticUtils.isCompletelySourced(mockOrder)).thenReturn(false);

		worker.executeInternal(parameters);
		assertFalse((boolean) parameters.get(CoreWorkflowConstants.KEY_OUTCOME_NAME));
		verify(mockShipmentService).createShipmentsForOrder(mockOrder);
	}

	@Test
	public void shouldSource_Complete()
	{
		mockStatic(OrderDataStaticUtils.class);
		when(OrderDataStaticUtils.isCompletelySourced(mockOrder)).thenReturn(true);

		worker.executeInternal(parameters);
		assertTrue((boolean) parameters.get(CoreWorkflowConstants.KEY_OUTCOME_NAME));
		verify(mockShipmentService).createShipmentsForOrder(mockOrder);
	}

}
