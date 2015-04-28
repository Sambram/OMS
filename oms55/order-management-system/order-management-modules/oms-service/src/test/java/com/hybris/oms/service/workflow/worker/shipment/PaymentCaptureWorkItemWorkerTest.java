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
package com.hybris.oms.service.workflow.worker.shipment;

import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.shipment.ShipmentService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

import static com.hybris.oms.service.workflow.CoreWorkflowConstants.KEY_OUTCOME_NAME;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_SHIPMENT_ID;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PaymentCaptureWorkItemWorkerTest
{
	@InjectMocks
	PaymentWorkItemWorker paymentWorker = new PaymentWorkItemWorker();
	@Mock
	ShipmentService mockShipmentService;
	@Mock
	CisService mockCisService;

	Map<String, Object> workflowParameters = new HashMap<>();

	ShipmentData shipmentData;
	OrderData orderData;

	final Long shipmentId = 10L;
	final Long orderId = 1L;

	public static final boolean SUCCESS = true;

	@Before
	public void setup()
	{
		shipmentData = mock(ShipmentData.class);
		orderData = mock(OrderData.class);

		when(mockShipmentService.findShipmentsByOrder(orderData)).thenReturn(ImmutableList.of(shipmentData));
		when(mockShipmentService.getShipmentById(shipmentId)).thenReturn(shipmentData);

		workflowParameters.put(KEY_SHIPMENT_ID, shipmentId);
		workflowParameters.put(KEY_ORDER_ID, orderId);
	}

	boolean getWorkflowResult()
	{
		return (boolean) workflowParameters.get(KEY_OUTCOME_NAME);
	}

	@Test
	public void capturePaymentSuccess()
	{
		// WHEN:
		paymentWorker.executeInternal(workflowParameters);

		// THEN:
		verify(mockShipmentService, atLeast(1)).capturePayment(shipmentData);
		assertThat(getWorkflowResult()).isEqualTo(SUCCESS);
	}
}
