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
import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * This class is used to test shipment actions that trigger the workflow engine.
 */
public class ShipmentWorkflowIntegrationTest extends RestClientIntegrationTest
{
	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private ShipmentFacade shipmentFacade;

	@Autowired
	private UiShipmentFacade uiShipmentFacade;

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private PreferenceFacade preferenceFacade;
	private TenantPreference tenantPaymentPreference;
	private static final String TENANT_PAYMENT_CAPTURE_PREFERENCE_KEY = "workflow.execution.task.paymentCapture";
	private String previousPaymentDefault = null;
	private TenantPreference tenantTaxPreference;
	private static final String TENANT_TAX_CAPTURE_PREFERENCE_KEY = "workflow.execution.task.taxInvoice";
	private String previousTaxDefault = null;
	private static final String ZERO = "0";
	private static final String HUNDRED = "100";
	private static final String THOUSAND = "1000";

	private OmsInventory inventory;
	private Location location;

	@Before
	public void setUp()
	{
		final String locationId = this.generateLocationId();
		this.location = this.buildLocation(locationId);
		this.inventoryFacade.createStockRoomLocation(this.location);
		this.inventory = createInventory();

		tenantPaymentPreference = this.preferenceFacade.getTenantPreferenceByKey(TENANT_PAYMENT_CAPTURE_PREFERENCE_KEY);
		previousPaymentDefault = tenantPaymentPreference.getValue();
		tenantTaxPreference = this.preferenceFacade.getTenantPreferenceByKey(TENANT_TAX_CAPTURE_PREFERENCE_KEY);
		previousTaxDefault = tenantTaxPreference.getValue();
	}

	@After
	public void tearDown()
	{
		this.inventoryFacade.deleteInventory(this.inventory);

		tenantPaymentPreference.setValue(previousPaymentDefault);
		this.preferenceFacade.updateTenantPreference(tenantPaymentPreference);
		tenantTaxPreference.setValue(previousTaxDefault);
		this.preferenceFacade.updateTenantPreference(tenantTaxPreference);
	}

	/* SHIPMENT CONFIRM TEST CASES */
	@Test
	public void shouldConfirmShipment()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.confirmShipment(shipmentId);
		this.delay();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		Assert.assertEquals(STATUS_SHIPPED, shipment.getOlqsStatus());
	}

	@Test
	public void shouldConfirmShipmentPaymentSkipped()
	{
		tenantPaymentPreference.setValue("FALSE");
		this.preferenceFacade.updateTenantPreference(tenantPaymentPreference);

		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.confirmShipment(shipmentId);
		this.delay();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		Assert.assertEquals(STATUS_SHIPPED, shipment.getOlqsStatus());
	}

	@Test
	public void shouldConfirmShipmentTaxSkipped()
	{
		tenantTaxPreference.setValue("FALSE");
		this.preferenceFacade.updateTenantPreference(tenantTaxPreference);

		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.confirmShipment(shipmentId);
		this.delay();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		Assert.assertEquals(STATUS_SHIPPED, shipment.getOlqsStatus());
	}

	@Test
	public void shouldConfirmShipmentPaymentAndTaxSkipped()
	{
		tenantPaymentPreference.setValue("FALSE");
		this.preferenceFacade.updateTenantPreference(tenantPaymentPreference);
		tenantTaxPreference.setValue("FALSE");
		this.preferenceFacade.updateTenantPreference(tenantTaxPreference);

		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.confirmShipment(shipmentId);
		this.delay();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		Assert.assertEquals(STATUS_SHIPPED, shipment.getOlqsStatus());
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailConfirmShipmentAlreadyCancelled()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.cancelShipment(shipmentId);
		this.delay();
		this.shipmentFacade.confirmShipment(shipmentId);
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldFailConfirmShipmentAlreadyDeclined()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.declineShipment(shipmentId);
		this.delay();
		this.shipmentFacade.confirmShipment(shipmentId);
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailConfirmShipmentAlreadyConfirmed()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.confirmShipment(shipmentId);
		this.delay();
		this.shipmentFacade.confirmShipment(shipmentId);
	}

	/* SHIPMENT CANCELLATION TEST CASES */
	@Test
	public void shouldCancelShipment()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.cancelShipment(shipmentId);
		this.delay();
		final Shipment shipment = this.shipmentFacade.getShipmentById(shipmentId);
		Assert.assertEquals(STATUS_CANCELLED, shipment.getOlqsStatus());
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailCancelShipmentAlreadyConfirmed()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.confirmShipment(shipmentId);
		this.delay();
		this.shipmentFacade.cancelShipment(shipmentId);
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldFailCancelShipmentAlreadyDeclined()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.declineShipment(shipmentId);
		this.delay();
		this.shipmentFacade.cancelShipment(shipmentId);
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailCancelShipmentAlreadyCancelled()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.cancelShipment(shipmentId);
		this.delay();
		this.shipmentFacade.cancelShipment(shipmentId);
	}

	/* SHIPMENT DECLINE TEST CASES */
	@Test
	public void shouldDeclineShipment()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.declineShipment(shipmentId);
		this.delay();
		try
		{
			this.shipmentFacade.getShipmentById(shipmentId);
			Assert.fail("Expected EntityNotFoundException because decline workflow should have deleted the shipment.");
		}
		catch (final EntityNotFoundException e)
		{
			// This is OK because after decline workflow finishes, the shipment will be deleted and therefore cannot be
			// found here.
		}
	}

	@Test
	public void shouldSourceFromDifferentLocationAfterShipmentDecline()
	{
		// Generate random Ids
		final String skuId = this.generateSku();
		final String firstLocationId = this.generateLocationId();
		final String secondLocationId = this.generateLocationId();

		// Create 2 locations
		this.createLocation(firstLocationId, LocationRole.SHIPPING, null);
		this.createLocation(secondLocationId, LocationRole.SHIPPING, null);

		// Update inventory for sku at both locations
		final OmsInventory sku1Loc1_ON_HAND = this.updateInventory(skuId, firstLocationId, ON_HAND, THOUSAND);
		final OmsInventory sku1Loc1_NOT_AVAILABLE = this.updateInventory(skuId, firstLocationId, NOT_AVAILABLE, ZERO);
		final OmsInventory sku1Loc2_ON_HAND = this.updateInventory(skuId, secondLocationId, ON_HAND, HUNDRED);
		final OmsInventory sku1Loc2_NOT_AVAILABLE = this.updateInventory(skuId, secondLocationId, NOT_AVAILABLE, ZERO);

		// Create 2 Orders for the same sku and assert they were sourced from same location
		final Shipment shipment1 = this.buildShipments(skuId).get(0);
		final Shipment shipment2 = this.buildShipments(skuId).get(0);
		Assert.assertTrue(shipment2.getLocation().equals(shipment1.getLocation()));

		// Decline a shipment
		this.shipmentFacade.declineShipment(shipment1.getShipmentId());
		this.delay();

		// Create 1 more Order for the same sku and assert that this time it was sourced from a different location than
		// the one of the declined shipment
		final Shipment shipment3 = this.buildShipments(skuId).get(0);
		Assert.assertFalse(shipment3.getLocation().equals(shipment1.getLocation()));
		Assert.assertFalse(shipment3.getLocation().equals(shipment2.getLocation()));

		// Remove created data
		this.inventoryFacade.deleteInventory(sku1Loc1_ON_HAND);
		this.inventoryFacade.deleteInventory(sku1Loc1_NOT_AVAILABLE);
		this.inventoryFacade.deleteInventory(sku1Loc2_ON_HAND);
		this.inventoryFacade.deleteInventory(sku1Loc2_NOT_AVAILABLE);
	}

	@Test
	public void shouldNotAffectSourcingAfterShipmentDeclineWhenSkusAreNotTheSame()
	{
		// Generate random Ids
		final String skuId = this.generateSku();
		final String anotherSkuId = this.generateSku();
		final String firstLocationId = this.generateLocationId();
		final String secondLocationId = this.generateLocationId();

		// Create first location
		this.createLocation(firstLocationId, LocationRole.SHIPPING, null);
		// Update inventory for both skus at first location
		final OmsInventory sku1Loc1_ON_HAND = this.updateInventory(skuId, firstLocationId, ON_HAND, THOUSAND);
		final OmsInventory sku1Loc1_NOT_AVAILABLE = this.updateInventory(skuId, firstLocationId, NOT_AVAILABLE, ZERO);
		final OmsInventory sku2Loc1_ON_HAND = this.updateInventory(anotherSkuId, firstLocationId, ON_HAND, THOUSAND);
		final OmsInventory sku2Loc1_NOT_AVAILABLE = this.updateInventory(anotherSkuId, firstLocationId, NOT_AVAILABLE, ZERO);

		// Create second location
		this.createLocation(secondLocationId, LocationRole.SHIPPING, null);
		// Update inventory for both skus at second location
		final OmsInventory sku1Loc2_ON_HAND = this.updateInventory(skuId, secondLocationId, ON_HAND, HUNDRED);
		final OmsInventory sku1Loc2_NOT_AVAILABLE = this.updateInventory(skuId, secondLocationId, NOT_AVAILABLE, ZERO);
		final OmsInventory sku2Loc2_ON_HAND = this.updateInventory(anotherSkuId, secondLocationId, ON_HAND, HUNDRED);
		final OmsInventory sku2Loc2_NOT_AVAILABLE = this.updateInventory(anotherSkuId, secondLocationId, NOT_AVAILABLE, ZERO);

		// Create 1 Order for the first sku
		final Shipment shipment1 = this.buildShipments(skuId).get(0);

		// Decline a shipment
		this.shipmentFacade.declineShipment(shipment1.getShipmentId());
		this.delay();

		// Create another Order for the second sku and assert that it will be sourced from the location of the declined
		// shipment
		final Shipment shipment2 = this.buildShipments(anotherSkuId).get(0);
		Assert.assertTrue(shipment2.getLocation().equals(shipment1.getLocation()));

		// Remove created data
		this.inventoryFacade.deleteInventory(sku1Loc1_ON_HAND);
		this.inventoryFacade.deleteInventory(sku1Loc1_NOT_AVAILABLE);
		this.inventoryFacade.deleteInventory(sku2Loc1_ON_HAND);
		this.inventoryFacade.deleteInventory(sku2Loc1_NOT_AVAILABLE);
		this.inventoryFacade.deleteInventory(sku1Loc2_ON_HAND);
		this.inventoryFacade.deleteInventory(sku1Loc2_NOT_AVAILABLE);
		this.inventoryFacade.deleteInventory(sku2Loc2_ON_HAND);
		this.inventoryFacade.deleteInventory(sku2Loc2_NOT_AVAILABLE);
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailDeclineShipmentAlreadyConfirmed()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.confirmShipment(shipmentId);
		this.delay();
		this.shipmentFacade.declineShipment(shipmentId);
	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailDeclineShipmentAlreadyCancelled()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.cancelShipment(shipmentId);
		this.delay();
		this.shipmentFacade.declineShipment(shipmentId);
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldFailDeclineShipmentAlreadyDeclined()
	{
		final String shipmentId = this.buildShipments(this.inventory.getSkuId()).get(0).getShipmentId();
		this.shipmentFacade.declineShipment(shipmentId);
		this.delay();
		this.shipmentFacade.declineShipment(shipmentId);
	}

}
