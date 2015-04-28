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

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ShippingLabelCisIntegrationTest extends RestClientIntegrationTest
{
	private OmsInventory inventory;

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private ShipmentFacade shipmentFacade;

	@Before
	public void setUp()
	{
		this.inventory = this.createInventory();
	}

	@After
	public void tearDown()
	{
		this.inventoryFacade.deleteInventory(this.inventory);
	}

	@Test
	public void testGetShippingLabels()
	{
		final List<Shipment> shipments = this.buildShipments(this.inventory.getSkuId());
		final byte[] label = this.shipmentFacade.retrieveShippingLabelsByShipmentId(shipments.get(0).getShipmentId());
		Assert.assertNotNull(label);
	}
}
