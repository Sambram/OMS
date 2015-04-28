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
package com.hybris.oms.facade.shipment;

import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_SPLIT_SHIPMENT_ID;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentDetails;
import com.hybris.oms.facade.conversion.impl.shipment.ShipmentDetailsReversePopulator;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.impl.shipment.ShipmentOlqDto;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OlqDataStaticUtils;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.UserTaskForm;
import com.hybris.oms.service.workflow.executor.UserTaskData;
import com.hybris.oms.service.workflow.executor.shipment.ShipmentWorkflowExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({OlqDataStaticUtils.class, OrderDataStaticUtils.class})
@PowerMockIgnore({"ch.qos.logback.*", "org.slf4j.*", "com.sun.org.apache.xerces.*"})
public class DefaultShipmentFacadeTest
{
	private static final String SHIPMENT_ID_1 = "1";
	private static final String SHIPMENT_ID_2 = "2";
	private static final String OLQ_ID_1 = "1";
	private static final String OLQ_ID_2 = "2";

	@Mock
	private Converter<ShipmentData, Shipment> shipmentConverter;
	@Mock
	private Converters converters;
	@Mock
	private ShipmentDetailsReversePopulator shipmentDetailsReverseConverter;
	@Mock
	private ShipmentService shipmentService;
	@Mock
	private OrderService orderService;
	@Mock
	private ShipmentWorkflowExecutor shipmentWorkflowExecutor;
	@Mock
	private Validator<ShipmentOlqDto> shipmentOlqValidator;
	@Mock
	private UserTaskForm userTaskForm;

	@Mock
	private ShipmentData shipmentData1, shipmentData2;
	@Mock
	private OrderData orderData;
	@Mock
	private OrderLineQuantityData olq1, olq2;
	@Mock
	private OrderLineQuantityStatusData olqStatus;

	@InjectMocks
	private final DefaultShipmentFacade shipmentServiceApi = new DefaultShipmentFacade();

	private Shipment shipment1, shipment2;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		shipmentServiceApi.setShipmentOlqValidator(shipmentOlqValidator);

		final List<OrderLineQuantityData> olqs_1_2 = new ArrayList<>();
		olqs_1_2.add(olq1);
		olqs_1_2.add(olq2);

		shipmentServiceApi.setMaximumSynchronousOperation(15);
		// Static mocks
		PowerMockito.mockStatic(OlqDataStaticUtils.class);
		PowerMockito.mockStatic(OrderDataStaticUtils.class);
		Mockito.when(OrderDataStaticUtils.getOrderLineQuantities(orderData)).thenReturn(olqs_1_2);

		// Managed Object mocks
		Mockito.when(shipmentData1.getOrderFk()).thenReturn(orderData);

		// Service mocks
		Mockito.when(this.shipmentService.getShipmentById(Long.valueOf(SHIPMENT_ID_1))).thenReturn(this.shipmentData1);
		Mockito.when(this.shipmentService.getShipmentById(Long.valueOf(SHIPMENT_ID_2))).thenReturn(this.shipmentData2);
	}

	@Test
	public void testUpdateShipmentDetails()
	{
		final ShipmentDetails shipmentDetails = new ShipmentDetails();
		this.shipmentServiceApi.updateShipmentDetails(SHIPMENT_ID_1, shipmentDetails);

		Mockito.verify(this.shipmentService, new Times(1)).getShipmentById(Long.valueOf(SHIPMENT_ID_1));
		Mockito.verify(this.shipmentDetailsReverseConverter, new Times(1)).populate(shipmentDetails, this.shipmentData1);
	}

	@Test
	public void shouldDeclineShipments()
	{
		Mockito.when(this.shipmentServiceApi.getShipmentById(SHIPMENT_ID_1)).thenReturn(shipment1);
		Mockito.when(this.shipmentServiceApi.getShipmentById(SHIPMENT_ID_2)).thenReturn(shipment2);
		Mockito.when(
				orderService
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_MANUAL_DECLINED))
				.thenReturn(olqStatus);

		final Set<String> shipmentIds = new HashSet<>();
		shipmentIds.add(SHIPMENT_ID_1);
		shipmentIds.add(SHIPMENT_ID_2);

		this.shipmentServiceApi.declineShipments(shipmentIds);

		Mockito.verify(this.shipmentWorkflowExecutor, new Times(2)).completeUserTask(Mockito.any(ShipmentData.class),
				Mockito.any(UserTaskForm.class));
	}

	@Test
	public void shouldCancelShipments()
	{
		Mockito.when(this.shipmentServiceApi.getShipmentById(SHIPMENT_ID_1)).thenReturn(shipment1);
		Mockito.when(this.shipmentServiceApi.getShipmentById(SHIPMENT_ID_2)).thenReturn(shipment2);
		Mockito.when(
				orderService.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED))
				.thenReturn(olqStatus);

		final Set<String> shipmentIds = new HashSet<>();
		shipmentIds.add(SHIPMENT_ID_1);
		shipmentIds.add(SHIPMENT_ID_2);

		this.shipmentServiceApi.cancelShipments(shipmentIds);

		Mockito.verify(this.shipmentWorkflowExecutor, new Times(2)).completeUserTask(Mockito.any(ShipmentData.class),
				Mockito.any(UserTaskForm.class));
	}

	@Test
	public void shouldConfirmShipments()
	{
		Mockito.when(this.shipmentServiceApi.getShipmentById(SHIPMENT_ID_1)).thenReturn(shipment1);
		Mockito.when(this.shipmentServiceApi.getShipmentById(SHIPMENT_ID_2)).thenReturn(shipment2);
		Mockito.when(
				orderService.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED))
				.thenReturn(olqStatus);

		final Set<String> shipmentIds = new HashSet<>();
		shipmentIds.add(SHIPMENT_ID_1);
		shipmentIds.add(SHIPMENT_ID_2);

		this.shipmentServiceApi.confirmShipments(shipmentIds);

		Mockito.verify(this.shipmentWorkflowExecutor, new Times(2)).completeUserTask(Mockito.any(ShipmentData.class),
				Mockito.any(UserTaskForm.class));
	}

	@Test
	public void shouldThrowAnExceptionWhenPerformingBatchOperationWhenMaximumReached()
	{
		final Set<String> shipmentIds = new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
				"12", "13", "14", "15", "16"));

		try
		{
			this.shipmentServiceApi.confirmShipments(shipmentIds);
			Assert.fail();
		}
		catch (final InvalidOperationException e)
		{
			// Do Nothing
		}
	}

	@Test
	public void shouldSplitShipmentByOlqQuantities()
	{
		final OrderLineQuantityData orderLineQuantityData1 = Mockito.mock(OrderLineQuantityData.class);
		final OrderLineQuantityData orderLineQuantityData2 = Mockito.mock(OrderLineQuantityData.class);

		final Map<String, Integer> olqQuantityMap = new HashMap<>();
		olqQuantityMap.put(OLQ_ID_1, 1);
		olqQuantityMap.put(OLQ_ID_2, 2);

		Mockito.when(this.shipmentServiceApi.getShipmentById(SHIPMENT_ID_1)).thenReturn(shipment1);
		Mockito.when(this.shipmentServiceApi.getShipmentById(SHIPMENT_ID_2)).thenReturn(shipment2);
		Mockito.when(this.orderService.getOrderLineQuantityByOlqId(Long.valueOf(OLQ_ID_1))).thenReturn(orderLineQuantityData1);
		Mockito.when(this.orderService.getOrderLineQuantityByOlqId(Long.valueOf(OLQ_ID_2))).thenReturn(orderLineQuantityData2);
		Mockito.when(shipmentWorkflowExecutor.getParameter(shipmentData1, KEY_SPLIT_SHIPMENT_ID)).thenReturn(SHIPMENT_ID_2);

		this.shipmentServiceApi.splitShipmentByOlqQuantities(SHIPMENT_ID_1, olqQuantityMap);
		Mockito.verify(shipmentWorkflowExecutor).completeUserTask(Mockito.any(ShipmentData.class), Mockito.any(UserTaskForm.class),
				Mockito.any(UserTaskData.class));
	}

}
