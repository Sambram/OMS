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
package com.hybris.oms.rest.integration.order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;
import com.hybris.oms.ui.api.shipment.UiShipmentFacade;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * This class is used to test shipment actions that trigger the workflow engine.
 */
public class OrderWorkflowIntegrationTest extends RestClientIntegrationTest
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
	private TenantPreference tenantFulfillmentPreference;
	private static final String TENANT_FULFILLMENT_PREFERENCE_KEY = "workflow.execution.task.fulfillment";
	private String previousFulfillmentDefault = null;
	private static final String TENANT_TAX_CAPTURE_PREFERENCE_KEY = "workflow.execution.task.taxInvoice";
	private final String previousTaxDefault = null;
	private static final String ZERO = "0";
	private static final String ONE = "1";
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

		tenantFulfillmentPreference = this.preferenceFacade.getTenantPreferenceByKey(TENANT_FULFILLMENT_PREFERENCE_KEY);
		previousFulfillmentDefault = tenantFulfillmentPreference.getValue();

	}

	@After
	public void tearDown()
	{
		this.inventoryFacade.deleteInventory(this.inventory);

		tenantFulfillmentPreference.setValue(previousFulfillmentDefault);
		this.preferenceFacade.updateTenantPreference(tenantFulfillmentPreference);

	}

	@Test(expected = InvalidOperationException.class)
	public void shouldFailFulfillment_SkipedFulfillment()
	{
		tenantFulfillmentPreference.setValue("FALSE");
		this.preferenceFacade.updateTenantPreference(tenantFulfillmentPreference);
		Order order = this.buildOrder(this.inventory.getSkuId());
		final String orderId = order.getOrderId();
		order = this.orderFacade.createOrder(order);
		this.orderFacade.fulfill(orderId);
	}

	@Test
	public void shouldRefulfill_IncompleteFulfillment()
	{
		final String skuId = this.generateSku();

		// Create a order attributes
		final String locationId = "location+" + this.generateRandomString();
		final String orderId = "order_" + this.generateRandomString();
		final String orderLineId = "orderLine_" + this.generateRandomString();
		this.createLocation(locationId, LocationRole.SHIPPING, null);

		// Update inventory for both skus at first location
		final OmsInventory sku1Loc1 = this.updateInventory(skuId, locationId, ON_HAND, ONE);
		Order order = this.buildOrder(sku1Loc1.getSkuId(), orderId, orderLineId, 2);
		order = this.orderFacade.createOrder(order);
		this.delay();

		order = orderFacade.getOrderByOrderId(orderId);
		// Assert the order is not fulfilled
		assertTrue(order.getOrderLines().get(0).getQuantityUnassigned().getValue() > 0);

		// Update the inventory
		this.updateInventory(skuId, locationId, ON_HAND, ONE);
		this.delay(5500); // wait so the workflow can retry to fulfill the order
		order = orderFacade.getOrderByOrderId(orderId); // refresh the order

		// Assert the order is fulfilled
		assertEquals(0, order.getOrderLines().get(0).getQuantityUnassigned().getValue());
		this.inventoryFacade.deleteInventory(sku1Loc1);
	}

	@Test
	public void shouldRetryFulfill_IncompleteFulfillment()
	{
		final String skuId = this.generateSku();

		// Create a order attributes
		final String locationId = "location+" + this.generateRandomString();
		final String orderId = "order_" + this.generateRandomString();
		final String orderLineId = "orderLine_" + this.generateRandomString();
		this.createLocation(locationId, LocationRole.SHIPPING, null);

		// Update inventory for both skus at first location
		final OmsInventory sku1Loc1 = this.updateInventory(skuId, locationId, ON_HAND, ONE);
		Order order = this.buildOrder(sku1Loc1.getSkuId(), orderId, orderLineId, 2);
		order = this.orderFacade.createOrder(order);
		this.delay();

		order = orderFacade.getOrderByOrderId(orderId);
		// Assert the order is not fulfilled
		assertTrue(order.getOrderLines().get(0).getQuantityUnassigned().getValue() > 0);

		// Update the inventory
		this.updateInventory(skuId, locationId, ON_HAND, ONE);

		// retry fulfillment
		this.orderFacade.fulfill(orderId);

		order = orderFacade.getOrderByOrderId(orderId);

		// Assert the order is fulfilled
		assertTrue(order.getOrderLines().get(0).getQuantityUnassigned().getValue() == 0);
		assertTrue(order.getOrderLines().get(0).getQuantity().getValue() == 2);
		this.inventoryFacade.deleteInventory(sku1Loc1);
	}

	@Test
	public void shouldCancel_IncompleteFulfillment()
	{

		final String skuId = this.generateSku();

		// Create a order attributes
		final String locationId = "location+" + this.generateRandomString();
		final String orderId = "order_" + this.generateRandomString();
		final String orderLineId = "orderLine_" + this.generateRandomString();
		this.createLocation(locationId, LocationRole.SHIPPING, null);

		// Update inventory for both skus at first location
		final OmsInventory sku1Loc1 = this.updateInventory(skuId, locationId, ON_HAND, ONE);
		Order order = this.buildOrder(sku1Loc1.getSkuId(), orderId, orderLineId, 2);
		order = this.orderFacade.createOrder(order);
		this.delay();

		order = orderFacade.getOrderByOrderId(orderId);
		// Assert the order is not fulfilled
		assertTrue(order.getOrderLines().get(0).getQuantityUnassigned().getValue() > 0);

		// Cancel unfulfilled
		this.orderFacade.cancelUnfulfilled(orderId);
		order = orderFacade.getOrderByOrderId(orderId);

		// Assert the order is fulfilled
		assertEquals(0, order.getOrderLines().get(0).getQuantityUnassigned().getValue());
		assertEquals(1, order.getOrderLines().get(0).getQuantity().getValue());
		this.inventoryFacade.deleteInventory(sku1Loc1);
	}
}
