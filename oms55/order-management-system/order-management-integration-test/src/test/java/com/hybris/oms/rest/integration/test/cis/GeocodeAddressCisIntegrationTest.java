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
package com.hybris.oms.rest.integration.test.cis;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;


public class GeocodeAddressCisIntegrationTest extends RestClientIntegrationTest
{

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private OrderFacade orderFacade;

	@Test
	public void testCreateOrderWithValidAddress()
	{
		final Address addressValid = new Address("Ralph Wilson Stadium", "1 Bills Drive", "Orchard Park", "NY", "14127", 50.812356,
				-83.945857, "US", "U.S.A.", null, null);
		final Order testedOrder = this.buildOrder(addressValid);

		// Remove previous location Coordinate
		testedOrder.getShippingAddress().setLatitudeValue(null);
		testedOrder.getShippingAddress().setLongitudeValue(null);

		final Order createdOrder = this.orderFacade.createOrder(testedOrder);
		Assert.assertEquals("Ralph Wilson Stadium", createdOrder.getShippingAddress().getAddressLine1());
		Assert.assertEquals("1 Bills Drive", createdOrder.getShippingAddress().getAddressLine2());
		Assert.assertNull("GeoCoding is a later workflow step", createdOrder.getShippingAddress().getLatitudeValue());
		Assert.assertNull("GeoCoding is a later workflow step", createdOrder.getShippingAddress().getLongitudeValue());
	}

	@Test
	public void testCreateStockRoomLocationWithValidAddress()
	{
		final String locationId = this.generateLocationId();
		final Location location = this.buildLocation(locationId);

		location.setAddress(new Address("Madison Square Garden", "2 Penn Plaza", "New York", "NY", "10121", null, null, "US",
				"U.S.A.", locationId, locationId));

		final Location createdLocation = this.inventoryFacade.createStockRoomLocation(location);
		Assert.assertNotNull(createdLocation.getAddress().getLatitudeValue());
		Assert.assertEquals(Double.valueOf(0.0), createdLocation.getAddress().getLatitudeValue());
		Assert.assertEquals(Double.valueOf(0.0), createdLocation.getAddress().getLongitudeValue());
	}
}
