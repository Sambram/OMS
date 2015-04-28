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
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentDetails;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ShipmentDetailsIntegrationTest extends RestClientIntegrationTest
{
	private OmsInventory inventory;
	private List<Shipment> shipments;

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private ShipmentFacade shipmentFacade;

	@Before
	public void setUp()
	{
		inventory = createInventory();
		this.shipments = this.buildShipments(inventory.getSkuId());
	}

	@After
	public void tearDown()
	{
		this.inventoryFacade.deleteInventory(this.inventory);
	}

	@Test
	public void testUpdateShipmentDetails()
	{
		final String shipmentId = this.shipments.get(0).getShipmentId();

		final ShipmentDetails shipmentDetails = new ShipmentDetails();
		shipmentDetails.setHeightUnitCode("m");
		shipmentDetails.setHeightValue(10.0f);
		shipmentDetails.setInsuranceValueAmountValue(1.0d);
		shipmentDetails.setLengthValue(10.0f);
		shipmentDetails.setWeightUnitCode("LB");
		shipmentDetails.setWeightValue(10.0f);
		shipmentDetails.setWidthValue(10.0f);
		shipmentDetails.setShippingMethod("BUU");


		this.shipmentFacade.updateShipmentDetails(shipmentId, shipmentDetails);

		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		Assert.assertEquals(shipmentId, shipment.getShipmentId());

		Assert.assertEquals("m", shipment.getHeight().getUnitCode());
		Assert.assertEquals(10.0f, shipment.getHeight().getValue(), 0.001d);
		Assert.assertEquals(1.0d, shipment.getInsuranceValueAmount().getValue(), 0.001d);
		Assert.assertEquals(10.0f, shipment.getLength().getValue(), 0.001d);
		Assert.assertEquals("LB", shipment.getGrossWeight().getUnitCode());
		Assert.assertEquals(10.0f, shipment.getGrossWeight().getValue(), 0.001d);
		Assert.assertEquals(10.0f, shipment.getWidth().getValue(), 0.001d);
		Assert.assertEquals("BUU", shipment.getShippingMethod());
	}
}
