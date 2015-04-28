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

import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_LOCATION_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_SHIPMENT_ID;

import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.shipment.ShipmentService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
public class ReallocateShipmentWorkItemWorkerTest
{
	private static final String LOCATION_ID = "LOCATION_ID";
	private static final long SHIPMENT_ID = 1L;

	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private ShipmentService mockShipmentService;
	@Mock
	private ShipmentData mockShipment;
	@Mock
	private StockroomLocationData mockLocation;

	@InjectMocks
	private final ReallocateShipmentWorkItemWorker worker = new ReallocateShipmentWorkItemWorker();

	private Map<String, Object> parameters;

	@Before
	public void setUp()
	{
		Mockito.when(mockShipmentService.getShipmentById(SHIPMENT_ID)).thenReturn(mockShipment);
		Mockito.when(mockInventoryService.getLocationByLocationId(LOCATION_ID)).thenReturn(mockLocation);

		parameters = new HashMap<>();
		parameters.put(KEY_LOCATION_ID, LOCATION_ID);
		parameters.put(KEY_SHIPMENT_ID, SHIPMENT_ID);
	}

	@Test
	public void shouldReallocateShipment()
	{
		worker.executeInternal(parameters);
		Mockito.verify(mockShipmentService).getShipmentById(SHIPMENT_ID);
		Mockito.verify(mockInventoryService).getLocationByLocationId(LOCATION_ID);
		Mockito.verify(mockShipmentService).reallocateShipment(mockShipment, mockLocation);
	}
}
