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
package com.hybris.oms.rest.integration.shipment;


import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.BatchResult;
import com.hybris.oms.domain.ProcessedItem;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentSplitResult;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;


public class ShipmentPostIntegrationTest extends RestClientIntegrationTest
{
	private OmsInventory inventory1;
	private OmsInventory inventory2;
	private OmsInventory binInventory;
	private OmsInventory binInventory2;
	private Location location;

	@Autowired
	private ShipmentFacade shipmentFacade;
	@Autowired
	private OrderFacade orderFacade;
	@Autowired
	private InventoryFacade inventoryFacade;

	@Before
	public void setUp()
	{
		String locationId = generateLocationId();
		location = buildLocation(locationId);
		inventoryFacade.createStockRoomLocation(location);
		inventory1 = createInventory();
		inventory2 = createInventory();
	}

	@After
	public void tearDown()
	{
		if (binInventory != null)
		{
			inventoryFacade.deleteInventory(binInventory);
		}
		if (binInventory2 != null)
		{
			inventoryFacade.deleteInventory(binInventory2);
		}
		inventoryFacade.deleteInventory(inventory1);
		inventoryFacade.deleteInventory(inventory2);
	}

	@Test
	public void shouldHaveAvailableActions()
	{
		List<Shipment> shipments = buildShipments(inventory1.getSkuId());

		assertTrue(shipments.get(0).getActions().contains("CANCEL"));
		assertTrue(shipments.get(0).getActions().contains("DECLINE"));
		assertTrue(shipments.get(0).getActions().contains("CONFIRM"));
		assertTrue(shipments.get(0).getActions().contains("REALLOCATE"));
	}

	@Test
	public void shouldDeclineOneShipmentAndFailOnAnother()
	{
		List<Shipment> shipments = buildShipments(inventory1.getSkuId());
		Set<String> shipmentIds = new HashSet<>();
		String shipmentId1 = shipments.get(0).getShipmentId();
		shipmentIds.add(shipmentId1);

		shipments = buildShipments(inventory1.getSkuId());
		String shipmentId2 = shipments.get(0).getShipmentId();
		shipmentIds.add(shipmentId2);

		shipmentFacade.declineShipment(shipmentId2);
		delay(1000);

		BatchResult result = shipmentFacade.declineShipments(shipmentIds);
		delay(1000);

		List<ProcessedItem> processedItemList = result.getProcessedItems();

		assertEquals(processedItemList.size(), 2);

		for (ProcessedItem processedItem : processedItemList)
		{
			if (processedItem.getBusinessId().equals(shipmentId1))
			{
				assertEquals(processedItem.getItemStatus(), ProcessedItem.STATUS.SUCCEEDED);

			}
			else if (processedItem.getBusinessId().equals(shipmentId2))
			{
				assertEquals(processedItem.getItemStatus(), ProcessedItem.STATUS.FAILED);
			}
		}
	}

	@Test
	public void shouldDeclineShipments()
	{
		List<Shipment> shipments = buildShipments(inventory1.getSkuId());
		Set<String> shipmentIds = new HashSet<>();
		shipmentIds.add(shipments.get(0).getShipmentId());

		shipments = buildShipments(inventory2.getSkuId());
		shipmentIds.add(shipments.get(0).getShipmentId());

		BatchResult result = shipmentFacade.declineShipments(shipmentIds);

		assertEquals(result.getProcessedItems().size(), 2);

		delay(1000);

		for (final ProcessedItem processedItem : result.getProcessedItems())
		{
			try
			{
				shipmentFacade.getShipmentById(processedItem.getBusinessId());
				fail();
			}
			catch (EntityNotFoundException e)
			{
				// Do Nothing
			}
		}
	}

	@Test
	public void shouldConfirmShipments()
	{
		List<Shipment> shipments = buildShipments(inventory1.getSkuId());
		Set<String> shipmentIds = new HashSet<>();
		shipmentIds.add(shipments.get(0).getShipmentId());

		shipments = buildShipments(inventory2.getSkuId());
		shipmentIds.add(shipments.get(0).getShipmentId());

		BatchResult result = shipmentFacade.confirmShipments(shipmentIds);
		assertEquals(result.getProcessedItems().size(), 2);

		delay();

		Shipment shipment1 = shipmentFacade.getShipmentById(result.getProcessedItems().get(0).getBusinessId());
		assertEquals(STATUS_SHIPPED, shipment1.getOlqsStatus());

		Shipment shipment2 = shipmentFacade.getShipmentById(result.getProcessedItems().get(1).getBusinessId());
		assertEquals(STATUS_SHIPPED, shipment2.getOlqsStatus());
	}

	@Test
	public void shouldCancelShipments()
	{
		List<Shipment> shipments = buildShipments(inventory1.getSkuId());
		Set<String> shipmentIds = new HashSet<>();
		shipmentIds.add(shipments.get(0).getShipmentId());

		shipments = buildShipments(inventory2.getSkuId());
		shipmentIds.add(shipments.get(0).getShipmentId());

		BatchResult result = shipmentFacade.cancelShipments(shipmentIds);
		assertEquals(result.getProcessedItems().size(), 2);

		delay();

		Shipment shipment1 = shipmentFacade.getShipmentById(result.getProcessedItems().get(0).getBusinessId());
		assertEquals(STATUS_CANCELLED, shipment1.getOlqsStatus());

		Shipment shipment2 = shipmentFacade.getShipmentById(result.getProcessedItems().get(1).getBusinessId());
		assertEquals(STATUS_CANCELLED, shipment2.getOlqsStatus());
	}

	@Test
	public void shouldSplitShipmentsByOlqQuantities()
	{
		List<Shipment> shipments = buildShipments(inventory1.getSkuId());
		Shipment shipment = shipments.get(0);

		List<String> olqIds = shipment.getOlqIds();

		Map<String, Integer> olqIdQuantityValueMap = new HashMap<>();
		olqIdQuantityValueMap.put(olqIds.get(0), 1);

		ShipmentSplitResult splitResult = shipmentFacade.splitShipmentByOlqQuantities(
                shipment.getShipmentId(), olqIdQuantityValueMap);

		assertNotEquals(splitResult.getNewShipment().getShipmentId(), shipment.getShipmentId());
		assertEquals(splitResult.getNewShipment().getOlqIds().size(), 1);
		assertNotEquals(splitResult.getNewShipment().getOlqIds().get(0), shipment.getOlqIds().get(0));
	}

	@Test
	public void sourcingShouldAvoidBannedLocationsForSkus()
	{

		List<Shipment> shipmentsSuccess = buildShipments(inventory1.getSkuId());
		Set<String> shipmentIds = new HashSet<>();

		shipmentIds.add(shipmentsSuccess.get(0).getShipmentId());

		shipmentFacade.declineShipments(shipmentIds);
		delay(1000);
		List<Shipment> shipmentsFail = buildShipments(inventory1.getSkuId());
		assertTrue(shipmentsFail.isEmpty());

		inventoryFacade.createUpdateInventory(inventory1);
		delay(1000);
		List<Shipment> shipments = buildShipments(inventory1.getSkuId());
		assertEquals(shipments.size(), 1);

	}

}
