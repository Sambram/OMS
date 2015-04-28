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

import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShipmentSplitResult;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * End-to-end integration tests.
 * This class should contain methods that find shipments and update shipment status.
 */
public class ShipmentIntegrationTest extends RestClientIntegrationTest
{
	private static final String LOCATION_ID_INVALID = "INVALID";
	private static final String SHIPMENT_ID_INVALID = "99999999";
	private OmsInventory inventory;
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
		final String locationId = this.generateLocationId();
		this.location = this.buildLocation(locationId);
		this.inventoryFacade.createStockRoomLocation(this.location);
		this.inventory = createInventory();
	}

	@After
	public void tearDown()
	{
		if (binInventory != null)
		{
			this.inventoryFacade.deleteInventory(this.binInventory);
		}
		if (binInventory2 != null)
		{
			this.inventoryFacade.deleteInventory(this.binInventory2);
		}
		this.inventoryFacade.deleteInventory(this.inventory);
	}

	@Test
	public void testGetShipmentById()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());
		final String shipmentId = shipments.get(0).getShipmentId();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		Assert.assertEquals(shipmentId, shipment.getShipmentId());
		Assert.assertEquals(shipmentId, shipment.getId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testGetShipmentByIdNotFound()
	{
		this.shipmentFacade.getShipmentById(SHIPMENT_ID_INVALID);
	}

	@Test
	public void shouldSuccessfullyGetShipmentsByOrderId()
	{
		Order order = this.buildOrder("sku");
		order = this.orderFacade.createOrder(order);
		final Collection<Shipment> shipments = this.shipmentFacade.getShipmentsByOrderId(order.getOrderId());
		for (final Shipment shipment : shipments)
		{
			Assert.assertEquals(order.getOrderId(), shipment.getOrderId());
		}
	}

	@Test
	public void testFindAllShipmentsByOrdersLocationIdStatus()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());

		final ShipmentQueryObject shipmentQueryObject = new ShipmentQueryObject();
		shipmentQueryObject.setPageNumber(0);
		shipmentQueryObject.setPageSize(2);
		shipmentQueryObject.setOrderIds(Arrays.asList(shipments.get(0).getOrderId()));
		shipmentQueryObject.setLocationIds(Arrays.asList(inventory.getLocationId()));
		shipmentQueryObject.setShipmentStatusIds(Arrays.asList(STATUS_ALLOCATED));

		final Pageable<Shipment> shipment1 = this.shipmentFacade.findShipmentsByQuery(shipmentQueryObject);
		Assert.assertEquals(1, shipment1.getResults().size());
	}

	@Test
	public void testFindAllShipmentsByLocationId()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());

		final ShipmentQueryObject shipmentQueryObject = new ShipmentQueryObject();
		shipmentQueryObject.setPageNumber(0);
		shipmentQueryObject.setPageSize(2);
		shipmentQueryObject.setOrderIds(Arrays.asList(shipments.get(0).getOrderId()));
		shipmentQueryObject.setLocationIds(Arrays.asList(inventory.getLocationId()));

		final Pageable<Shipment> shipment1 = this.shipmentFacade.findShipmentsByQuery(shipmentQueryObject);
		Assert.assertEquals(1, shipment1.getResults().size());
	}

	@Test
	public void testFindAllShipmentsByStatus()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());

		final ShipmentQueryObject shipmentQueryObject = new ShipmentQueryObject();
		shipmentQueryObject.setPageNumber(0);
		shipmentQueryObject.setPageSize(2);
		shipmentQueryObject.setOrderIds(Arrays.asList(shipments.get(0).getOrderId()));
		shipmentQueryObject.setShipmentStatusIds(Arrays.asList(STATUS_ALLOCATED));

		final Pageable<Shipment> shipment1 = this.shipmentFacade.findShipmentsByQuery(shipmentQueryObject);
		Assert.assertEquals(1, shipment1.getResults().size());
	}

	@Test
	public void testPagingInFindByQuery()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());

		// Given
		final ShipmentQueryObject shipmentQueryObject = new ShipmentQueryObject();
		shipmentQueryObject.setPageNumber(0);
		shipmentQueryObject.setPageSize(1);
		shipmentQueryObject.setOrderIds(Arrays.asList(shipments.get(0).getOrderId()));

		// When
		final Pageable<Shipment> pagedShipmentDetails = this.shipmentFacade.findShipmentsByQuery(shipmentQueryObject);

		// Then
		Assert.assertEquals(1, pagedShipmentDetails.getTotalPages().intValue());
		Assert.assertEquals(0, pagedShipmentDetails.getNextPage().intValue());
		Assert.assertEquals(0, pagedShipmentDetails.getPreviousPage().intValue());
		Assert.assertEquals(1, pagedShipmentDetails.getTotalRecords().intValue());
	}

	@Test
	public void testFindByQuery()
	{
		final ShipmentQueryObject shipmentQueryObject = new ShipmentQueryObject();

		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startCalendar = new Date();
		try
		{
			startCalendar = formatter.parse("2010-01-01");
		}
		catch (final ParseException e)
		{
			startCalendar = null;
		}

		Date endCalendar = new Date();
		try
		{
			endCalendar = formatter.parse("2012-12-31");
		}
		catch (final ParseException e)
		{
			endCalendar = null;
		}

		shipmentQueryObject.setStartDate(startCalendar);
		shipmentQueryObject.setEndDate(endCalendar);

		final Pageable<Shipment> listOfShipment = this.shipmentFacade.findShipmentsByQuery(shipmentQueryObject);
		Assert.assertNotNull(listOfShipment);
	}

	@Test
	public void testUpdateShipmentStatus()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());
		final Shipment shipment = this.shipmentFacade.updateShipmentStatus(shipments.get(0).getShipmentId(), STATUS_ALLOCATED);
		Assert.assertEquals(STATUS_ALLOCATED, shipment.getOlqsStatus());
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldFailUpdateShipmentStatusShipmentNotFound()
	{
		this.shipmentFacade.updateShipmentStatus("999", STATUS_ALLOCATED);
	}

	@Test(expected = EntityValidationException.class)
	public void shouldFailUpdateShipmentStatusShipmentNaN()
	{
		this.shipmentFacade.updateShipmentStatus("INVALID", STATUS_ALLOCATED);
	}

	@Test(expected = EntityValidationException.class)
	public void shouldFailUpdateShipmentStatusInvalidStatusCode()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());
		this.shipmentFacade.updateShipmentStatus(shipments.get(0).getShipmentId(), "INVALID");
	}

	/**
	 * To be moved to {@link ShipmentWorkflowIntegrationTest} once pick and pack are in the workflow.
	 */
	@Test
	public void shouldPickShipment()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());
		final Shipment shipment = this.shipmentFacade.pickShipment(shipments.get(0).getShipmentId());
		Assert.assertEquals(STATUS_PICKED, shipment.getOlqsStatus());
	}

	@Test
	public void shouldPackShipment()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());
		final Shipment shipment = this.shipmentFacade.packShipment(shipments.get(0).getShipmentId());
		Assert.assertEquals(STATUS_PACKED, shipment.getOlqsStatus());
	}

	/* SHIPMENT RE-ALLOCATION TEST CASES */
	@Test
	public void shouldReallocateShipment()
	{
		final String newLocationId = this.generateLocationId();
		final Location newLocation = this.buildLocation(newLocationId);
		this.inventoryFacade.createStockRoomLocation(newLocation);

		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.reallocateShipment(shipmentId, newLocationId);
		this.delay();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		Assert.assertEquals(STATUS_ALLOCATED, shipment.getOlqsStatus());

		final List<OrderLineQuantity> olqs = orderFacade.getOrderByOrderId(shipment.getOrderId()).getListOrderLinesQuantities();
		for (final OrderLineQuantity olq : olqs)
		{
			Assert.assertEquals(STATUS_ALLOCATED, olq.getStatus().getStatusCode());
			Assert.assertEquals(newLocationId, olq.getLocation());
		}
	}

	@Test(expected = EntityValidationException.class)
	public void shouldFailReallocateLocationNotFound()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.reallocateShipment(shipmentId, LOCATION_ID_INVALID);
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailReallocateShipmentAlreadyConfirmed()
	{
		final String newLocationId = this.generateLocationId();
		final Location newLocation = this.buildLocation(newLocationId);
		this.inventoryFacade.createStockRoomLocation(newLocation);

		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.confirmShipment(shipmentId);
		this.delay();
		this.shipmentFacade.reallocateShipment(shipmentId, newLocationId);
	}

	@Test(expected = InvalidOperationException.class)
	@Ignore
	public void shouldFailReallocateShipmentAlreadyCancelled()
	{
		final String newLocationId = this.generateLocationId();
		final Location newLocation = this.buildLocation(newLocationId);
		this.inventoryFacade.createStockRoomLocation(newLocation);

		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.cancelShipment(shipmentId);
		this.delay();
		this.shipmentFacade.reallocateShipment(shipmentId, newLocationId);
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldFailReallocateShipmentAlreadyDeclined()
	{
		final String newLocationId = this.generateLocationId();
		final Location newLocation = this.buildLocation(newLocationId);
		this.inventoryFacade.createStockRoomLocation(newLocation);

		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.declineShipment(shipmentId);
		this.delay();
		this.shipmentFacade.reallocateShipment(shipmentId, newLocationId);
	}

	// SPLIT TEST CASES

	@Test
	public void shouldSplitShipmentByOlqs()
	{
		final String newLocationId = this.generateLocationId();
		final Location newLocation = this.buildLocation(newLocationId);
		this.inventoryFacade.createStockRoomLocation(newLocation);

		final String shipmentId = this.buildShipmentsWithTwoOrderLines(this.inventory.getSkuId()).get(0).getShipmentId();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		final List<OrderLineQuantity> olqs = orderFacade.getOrderByOrderId(shipment.getOrderId()).getListOrderLinesQuantities();

		Assert.assertTrue(2 == olqs.size());

		final OrderLineQuantity olqToRemove = olqs.get(0);
		final ShipmentSplitResult result = this.shipmentFacade.splitShipmentByOlqs(shipmentId,
				Collections.singleton(olqToRemove.getOlqId()));

		Assert.assertTrue(1 == result.getOriginalShipment().getOlqIds().size());
		Assert.assertTrue(1 == result.getNewShipment().getOlqIds().size());
		Assert.assertTrue(olqToRemove.getOlqId().equals(result.getNewShipment().getOlqIds().get(0)));
	}

	@Test(expected = EntityValidationException.class)
	public void shouldFailSplitShipmentWithInvalidSplitOlq()
	{
		final String newLocationId = this.generateLocationId();
		final Location newLocation = this.buildLocation(newLocationId);
		this.inventoryFacade.createStockRoomLocation(newLocation);

		final String shipmentId = this.buildShipmentsWithTwoOrderLines(this.inventory.getSkuId()).get(0).getShipmentId();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		final List<OrderLineQuantity> olqs = orderFacade.getOrderByOrderId(shipment.getOrderId()).getListOrderLinesQuantities();
		final OrderLineQuantity validOlqToRemove = olqs.get(0);
		final String invalidOlqIdToRemove = validOlqToRemove.getOlqId() + "INVALID";
		this.shipmentFacade.splitShipmentByOlqs(shipmentId, Collections.singleton(invalidOlqIdToRemove));

	}
}
