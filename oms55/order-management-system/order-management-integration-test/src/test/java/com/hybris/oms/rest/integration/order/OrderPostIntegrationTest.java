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
 */
package com.hybris.oms.rest.integration.order;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * End-to-end integration tests.
 * This class should contain only methods that manipulate data (POST, PUT, DELETE).
 */
public class OrderPostIntegrationTest extends RestClientIntegrationTest
{

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private InventoryFacade inventoryFacade;


	@Before
	public void setUp()
	{
		try
		{
			Thread.sleep(100L);
		}
		catch (final InterruptedException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * OrderRestClient.createOrder
	 * 
	 * Notes:
	 * - All possible violation constraints are met.
	 * - Geo codes are present and valid (should not call CIS mock)
	 */
	@Test
	public void testCreateOrder()
	{
		Order order = this.buildOrder("sku");
		order = this.orderFacade.createOrder(order);
		Assert.assertEquals("chuck.norris@hybris.com", order.getEmailid());
	}

	@Test(expected = DuplicateEntityException.class)
	public void testCreateDuplicateOrder()
	{
		final Order order = this.buildOrder("sku");
		this.orderFacade.createOrder(order);
		this.orderFacade.createOrder(order);
	}

	/**
	 * OrderRestClient.createOrder
	 * 
	 * Notes:
	 * - All possible violation constraints are met.
	 * - Special characters in address
	 */
	@Test
	public void testCreateOrderWithSpecialCharacters()
	{
		Order order = this.buildOrder();
		order.getShippingAddress().setAddressLine1("éâäàåçêëèïîìÄÅÉôöòûùÿÖÜ¢áíóúñÑ");
		order = this.orderFacade.createOrder(order);
		Assert.assertEquals("éâäàåçêëèïîìÄÅÉôöòûùÿÖÜ¢áíóúñÑ", order.getShippingAddress().getAddressLine1());
	}

	/**
	 * Tests OrderRestClient.createOrder.
	 * 
	 * Notes:
	 * - orderId is missing (and a lot of other fields)
	 * - Should throw OrderValidationException
	 */
	@Test(expected = EntityValidationException.class)
	public void testCreateOrderThrowsValidationException()
	{
		final Order order = new Order();
		order.setOrderId("222");
		order.setShippingAddress(new Address("2207 7th Avenue", null, "New York", "NY", "10027", 40.812356, -73.945857, "US",
				"U.S.A.", null, null));

		this.orderFacade.createOrder(order);
	}

	@Test
	public void testCreateOrderLineQuantityStatus()
	{
		final String statusCode = this.generateRandomString();
		OrderLineQuantityStatus olqStatus = new OrderLineQuantityStatus();
		olqStatus.setStatusCode(statusCode);
		olqStatus.setActive(true);
		olqStatus.setDescription("Description");

		olqStatus = this.orderFacade.createOrderLineQuantityStatus(olqStatus);
		Assert.assertEquals(statusCode, olqStatus.getStatusCode());
		Assert.assertTrue(olqStatus.getActive());
	}

	@Test
	public void testUpdateOrderLineQuantityStatus()
	{

		final OrderLineQuantityStatus status = new OrderLineQuantityStatus();
		status.setStatusCode(generateRandomString());
		status.setDescription("test status");
		status.setActive(true);
		orderFacade.createOrderLineQuantityStatus(status);
		status.setDescription("testintegration");
		status.setActive(false);
		final OrderLineQuantityStatus newStatus = this.orderFacade.updateOrderLineQuantityStatus(status);
		Assert.assertEquals("testintegration", newStatus.getDescription());
		Assert.assertFalse(newStatus.getActive());
		Assert.assertEquals(status.getStatusCode(), newStatus.getStatusCode());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testUpdateOrderLineQuantityStatusNotFound()
	{
		final OrderLineQuantityStatus status = new OrderLineQuantityStatus();
		status.setStatusCode("unknown");
		status.setDescription("Description");
		this.orderFacade.updateOrderLineQuantityStatus(status);
	}

	@Test
	public void testCreateOrderWithOlqAndShipment()
	{
		// Create a location with random locationId
		final String locationId = this.generateLocationId();
		final Location location = this.buildLocation(locationId);
		final Address address = new Address("502 MAIN ST N", "26th floor", "MONTREAL", "QC", "H2B1A0", 40.812356, -73.945857, "CA",
				"Canada", "companyName", "4388888888");
		location.setAddress(address);
		this.inventoryFacade.createStockRoomLocation(location);

		final Order order = this.buildOrderWithOLQAndShipment(locationId);

		// Create order
		this.orderFacade.createOrder(order);
		super.delay(1000);

		final Order postSourcingOrder = this.orderFacade.getOrderByOrderId(order.getOrderId());

		Assert.assertEquals(order.getEmailid(), postSourcingOrder.getEmailid());
		Assert.assertEquals(order.getBaseStoreName(), postSourcingOrder.getBaseStoreName());
		Assert.assertEquals(order.getCurrencyCode(), postSourcingOrder.getCurrencyCode());
		Assert.assertEquals(order.getCustomerLocale(), postSourcingOrder.getCustomerLocale());
		Assert.assertEquals(order.getFirstName(), postSourcingOrder.getFirstName());
		Assert.assertEquals(order.getLastName(), postSourcingOrder.getLastName());
		Assert.assertEquals(order.getNumberOfOrderLines(), postSourcingOrder.getNumberOfOrderLines());
		Assert.assertEquals(order.getOrderId(), postSourcingOrder.getOrderId());
		Assert.assertEquals(order.getPriorityLevelCode(), postSourcingOrder.getPriorityLevelCode());
		Assert.assertEquals(order.getShippingFirstName(), postSourcingOrder.getShippingFirstName());
		Assert.assertEquals(order.getShippingLastName(), postSourcingOrder.getShippingLastName());
		Assert.assertEquals(order.getShippingMethod(), postSourcingOrder.getShippingMethod());
		Assert.assertEquals(order.getShippingTaxCategory(), postSourcingOrder.getShippingTaxCategory());
		Assert.assertEquals(order.getShippingAndHandling(), postSourcingOrder.getShippingAndHandling());
		this.assertAddress(order.getShippingAddress(), postSourcingOrder.getShippingAddress());
	}

	private void assertAddress(final Address expectedAddress, final Address actualAddress)
	{
		Assert.assertEquals(expectedAddress.getAddressLine1(), actualAddress.getAddressLine1());
		Assert.assertEquals(expectedAddress.getAddressLine2(), actualAddress.getAddressLine2());
		Assert.assertEquals(expectedAddress.getCityName(), actualAddress.getCityName());
		Assert.assertEquals(expectedAddress.getCountryIso3166Alpha2Code(), actualAddress.getCountryIso3166Alpha2Code());
		Assert.assertEquals(expectedAddress.getCountryName(), actualAddress.getCountryName());
		Assert.assertEquals(expectedAddress.getCountrySubentity(), actualAddress.getCountrySubentity());
		Assert.assertEquals(expectedAddress.getName(), actualAddress.getName());
		Assert.assertEquals(expectedAddress.getPhoneNumber(), actualAddress.getPhoneNumber());
		Assert.assertEquals(expectedAddress.getPostalZone(), actualAddress.getPostalZone());
	}
}
