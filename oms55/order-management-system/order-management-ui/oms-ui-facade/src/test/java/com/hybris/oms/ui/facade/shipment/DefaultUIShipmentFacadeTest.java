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
package com.hybris.oms.ui.facade.shipment;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.shipment.ShipmentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;


/**
 * Unit test with mocks for {@link DefaultUiShipmentFacade}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OrderDataStaticUtils.class})
@PowerMockIgnore({"javax.management.*", "javax.xml.parsers.*", "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
		"org.slf4j.*"})
public class DefaultUIShipmentFacadeTest
{

	private static final Long SHIPMENT_ID_1 = 1L;
	private static final Long OLQ_ID_1 = 1L;
	private static final Long SHIPMENT_ID_99 = 99L;
	private static final Long OLQ_ID_99 = 99L;
	private static final String LOCATION_ID_1 = "1";

	@InjectMocks
	private final DefaultUiShipmentFacade uiShipmentFacade = new DefaultUiShipmentFacade();

	@Mock
	private ShipmentService mockShipmentService;
	@Mock
	private OrderService mockOrderService;
	@Mock
	private InventoryService mockInventoryService;

	@Mock
	private PlatformTransactionManager transactionManager;

	@Mock
	private OrderData mockOrder_1;
	@Mock
	private ShipmentData mockShipment_1;
	@Mock
	private ShipmentData mockShipment_2;
	@Mock
	private OrderLineQuantityData mockOLQ_1;
	@Mock
	private OrderLineQuantityData mockOLQ_2;
	@Mock
	private OrderLineQuantityData mockOLQ_3;
	@Mock
	private StockroomLocationData mockLocation_1;
	@Mock
	private StockroomLocationData mockLocation_2;
	@Mock
	private Converters mockConverter;
	@Mock
	private Converter<ShipmentData, Shipment> mockShipmentConverter;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		Mockito.when(mockInventoryService.getLocationByLocationId(LOCATION_ID_1)).thenReturn(mockLocation_1);
		Mockito.when(mockShipmentService.getShipmentById(SHIPMENT_ID_1)).thenReturn(mockShipment_1);
		Mockito.when(mockOrderService.getOrderLineQuantityByOlqId(OLQ_ID_1)).thenReturn(mockOLQ_1);
		Mockito.when(mockShipment_1.getOrderFk()).thenReturn(mockOrder_1);
		Mockito.when(mockOLQ_1.getShipment()).thenReturn(mockShipment_1);
		Mockito.when(mockOLQ_2.getShipment()).thenReturn(mockShipment_2);

		PowerMockito.mockStatic(OrderDataStaticUtils.class);

		Mockito.when(mockShipmentService.splitShipmentByOlqs(mockShipment_1, Collections.singletonList(mockOLQ_1))).thenReturn(
				mockShipment_2);

	}

	@Test
	@Transactional
	public void shouldOnlyDeclineShipmentWhenRemoveOlqFromShipmentWithOneOlq()
	{
		final List<OrderLineQuantityData> orderLineQuantityDataList = new ArrayList<>();
		orderLineQuantityDataList.add(mockOLQ_1);

		Mockito.when(OrderDataStaticUtils.getOrderLineQuantities(mockOrder_1)).thenReturn(orderLineQuantityDataList);

		uiShipmentFacade.removeOrderLineQuantityFromShipment(SHIPMENT_ID_1.toString(), OLQ_ID_1.toString());

		Mockito.verify(mockShipmentService).declineShipment(mockShipment_1);
		Mockito.verify(mockShipmentService, Mockito.times(0)).splitShipmentByOlqs(Mockito.any(ShipmentData.class),
				Mockito.anyList());
	}

	@Test
	@Transactional
	public void shouldSplitAndDeclineShipmentWhenRemoveOlqFromShipmentWithMoreThanOneOlq()
	{
		final List<OrderLineQuantityData> orderLineQuantityDataList = new ArrayList<>();
		orderLineQuantityDataList.add(mockOLQ_1);
		orderLineQuantityDataList.add(mockOLQ_2);

		Mockito.when(OrderDataStaticUtils.getOrderLineQuantities(mockOrder_1)).thenReturn(orderLineQuantityDataList);

		uiShipmentFacade.removeOrderLineQuantityFromShipment(SHIPMENT_ID_1.toString(), OLQ_ID_1.toString());

		Mockito.verify(mockShipmentService).declineShipment(mockShipment_2);
		Mockito.verify(mockShipmentService).splitShipmentByOlqs(Mockito.any(ShipmentData.class), Mockito.anyList());
	}

	@Transactional
	@Test(expected = EntityNotFoundException.class)
	public void shouldFailRemoveOrderLineQuantity_ShipmentDoesNotExist()
	{
		Mockito.when(mockShipmentService.getShipmentById(SHIPMENT_ID_99)).thenThrow(new EntityNotFoundException(""));
		uiShipmentFacade.removeOrderLineQuantityFromShipment(SHIPMENT_ID_99.toString(), OLQ_ID_1.toString());

		Mockito.verify(mockShipmentService).getShipmentById(SHIPMENT_ID_99);
	}

	@Transactional
	@Test(expected = EntityNotFoundException.class)
	public void shouldFailRemoveOrderLineQuantity_OlqDoesNotExist()
	{
		final List<OrderLineQuantityData> orderLineQuantityDataList = new ArrayList<>();
		orderLineQuantityDataList.add(mockOLQ_1);
		orderLineQuantityDataList.add(mockOLQ_2);

		Mockito.when(OrderDataStaticUtils.getOrderLineQuantities(mockOrder_1)).thenReturn(orderLineQuantityDataList);

		Mockito.when(mockOrderService.getOrderLineQuantityByOlqId(OLQ_ID_99)).thenThrow(new EntityNotFoundException(""));
		uiShipmentFacade.removeOrderLineQuantityFromShipment(SHIPMENT_ID_1.toString(), OLQ_ID_99.toString());

		Mockito.verify(mockOrderService).getOrderLineQuantityByOlqId(SHIPMENT_ID_99);
	}

	@Test
	@Transactional
	public void shouldRemoveAllOlqFromShipment()
	{
		uiShipmentFacade.removeAllOrderLineQuantitiesFromShipment(SHIPMENT_ID_1.toString());
		Mockito.verify(mockShipmentService).declineShipment(mockShipment_1);
	}

	@Transactional
	@Test(expected = EntityNotFoundException.class)
	public void shouldFailInRemoveAllOrderLineQuantityWhenShipmentDoesNotExist()
	{
		Mockito.when(mockShipmentService.getShipmentById(SHIPMENT_ID_99)).thenThrow(new EntityNotFoundException(""));
		uiShipmentFacade.removeAllOrderLineQuantitiesFromShipment(SHIPMENT_ID_99.toString());

		Mockito.verify(mockShipmentService).getShipmentById(SHIPMENT_ID_99);
	}

}
