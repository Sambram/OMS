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
package com.hybris.oms.rest.integration.ui.shipment;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;
import com.hybris.oms.ui.api.shipment.OrderLineShipmentPickSlipBinInfo;
import com.hybris.oms.ui.api.shipment.PickSlipBinInfo;
import com.hybris.oms.ui.api.shipment.ShipmentDetail;
import com.hybris.oms.ui.api.shipment.UIShipment;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;


public class UIShipmentGetIntegrationTest extends RestClientIntegrationTest
{
	private static final String SHIPMENT_ID_INVALID = "99";
	private static final String BIN_CODE = "bincode1";
	private static final String BIN_CODE_2 = "bincode2";
	private static final String BIN_CODE_3 = "bincode3";
	private static final int BIN_PRIORITY_1 = 1;
	
	@Autowired
	private UiShipmentFacade uiShipmentFacade;
	@Autowired
	private InventoryFacade inventoryFacade;
	
	private OmsInventory inventory;
	private List<Shipment> shipments;

	@Before
	public void setUp()
	{
		inventory = createInventory();
		this.shipments = this.buildShipments(inventory.getSkuId());
	}

	@After
	public void tearDown()
	{
		if (this.inventory != null)
		{
			this.inventoryFacade.deleteInventory(this.inventory);
		}
	}

	@Test
	public void getOrderShipmentById()
	{
		final String shipmentId = this.shipments.get(0).getShipmentId();
		final UIShipment UIShipment = this.uiShipmentFacade.getUIShipmentById(shipmentId);
		Assert.assertEquals(shipmentId, UIShipment.getShipmentId().toString());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getOrderShipmentByIdThatDoesNotExist()
	{
		this.uiShipmentFacade.getUIShipmentById(SHIPMENT_ID_INVALID);
	}

	@Test
	public void getShipmentDetailById()
	{
		final String shipmentId = this.shipments.get(0).getShipmentId();
		final ShipmentDetail shipmentDetail = this.uiShipmentFacade.getShipmentDetailById(shipmentId);
		assertEquals(shipmentId, shipmentDetail.getShipment().getShipmentId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void getShipmentDetailByIdThatDoesNotExist()
	{
		this.uiShipmentFacade.getShipmentDetailById(SHIPMENT_ID_INVALID);
	}

	private List<Shipment> buildBinsTest(final String locationId, final String skuId, final String binCode)
	{
		createInventoryWithBin(skuId, locationId, binCode);
		return this.buildShipments(skuId);
	}

	private List<Shipment> buildBinsTwoOrderLinesTest(final String locationId, final String skuId, final String binCode,
			final String binCode2)
	{
		createInventoryWithBin(skuId, locationId, binCode);
		createInventoryWithBin(skuId, locationId, binCode2);
		return this.buildShipmentsWithTwoOrderLines(skuId);
	}

	private Bin createBin(final String locationId, final String binCode, final int priority, final String description)
	{
		final Bin bin = this.buildBin(binCode, locationId, priority, description);
		return this.inventoryFacade.createBin(bin);
	}

	@Test
	public void testGetPickShipmentWithSingleBin()
	{
		this.createBin(inventory.getLocationId(), BIN_CODE, BIN_PRIORITY_1, BIN_CODE);
		final List<Shipment> shipmentList = this.buildBinsTest(inventory.getLocationId(), inventory.getSkuId(), BIN_CODE);
		final String shipmentId = shipmentList.get(0).getShipmentId();
		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(shipmentId);
		Assert.assertEquals(shipmentId, shipmentBins.getShipmentId());
		final List<OrderLineShipmentPickSlipBinInfo> orderLineBins = shipmentBins.getOrderLineBins();
		final List<Bin> bins = orderLineBins.get(0).getBins();
		Assert.assertEquals(1, bins.size());
		Assert.assertEquals(BIN_CODE, bins.get(0).getBinCode());
	}

	@Test
	public void testGetBinsByShipmentIdMultipleBinsSortedByBinCodeASC()
	{
		this.createBin(inventory.getLocationId(), BIN_CODE_2, BIN_PRIORITY_1, BIN_CODE_2);
		this.createBin(inventory.getLocationId(), BIN_CODE_3, BIN_PRIORITY_1, BIN_CODE_3);
		final List<Shipment> shipmentList = this.buildBinsTwoOrderLinesTest(inventory.getLocationId(), inventory.getSkuId(),
				BIN_CODE_2, BIN_CODE_3);
		final String shipmentId = shipmentList.get(0).getShipmentId();
		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(shipmentId);
		Assert.assertEquals(shipmentId, shipmentBins.getShipmentId());
		final List<OrderLineShipmentPickSlipBinInfo> orderLineBins = shipmentBins.getOrderLineBins();
		final List<Bin> bins = orderLineBins.get(0).getBins();
		Assert.assertEquals(2, bins.size());
		Assert.assertEquals(BIN_CODE_2, bins.get(0).getBinCode());
		Assert.assertEquals(BIN_CODE_3, bins.get(1).getBinCode());
	}

	@Test
	public void testGetPickShipmentWithNoBin()
	{
		final OmsInventory inventory3 = createInventory();
		final List<Shipment> shipmentList = this.buildShipments(inventory3.getSkuId());

		final String shipmentId = shipmentList.get(0).getShipmentId();
		final PickSlipBinInfo shipmentBins = this.uiShipmentFacade.getBinInfoForPickSlipByShipmentId(shipmentId);
		Assert.assertEquals(shipmentId, shipmentBins.getShipmentId());
		Assert.assertEquals(1, shipmentBins.getOrderLineBins().size());
		final List<OrderLineShipmentPickSlipBinInfo> orderLineBins = shipmentBins.getOrderLineBins();
		Assert.assertNull(orderLineBins.get(0).getBins());
	}
}
