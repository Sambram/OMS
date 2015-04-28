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
package com.hybris.oms.facade.workflow;

import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.JobSchedulerService;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.activiti.engine.RuntimeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-facade-workflow-spring-test.xml")
@Ignore("Run Manually with a prod DB.")
public class WorkflowFulfillmentLoadTest
{
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowFulfillmentLoadTest.class);

	private static final String LOC_ID = "LOCATION_ID";
	private static final String SKU = "SKU";

	private static final String ON_HAND = "ON_HAND";
	private static final int QUANTITY = 10;
	private static final String ORDERLINE_ATTRIBUTE_VALUE = "OrderLine.AttributeValue";
	private static final String ORDERLINE_ATTRIBUTE_ID = "OrderLine.AttributeId";

	private static final int NUM_ORDERS = 100;

	@Autowired
	private OrderFacade orderFacade;
	@Autowired
	private ShipmentFacade shipmentFacade;
	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private ImportService importService;
	@Autowired
	private AggregationService aggregationService;
	@Autowired
	private JdbcPersistenceEngine persistenceEngine;
	@Autowired
	private JobSchedulerService scheduler;

	@Autowired
	private RuntimeService runtimeService;

	@Before
	public void setUp()
	{
		OmsTestUtils.unscheduleJobs(scheduler);
		this.importService.loadMcsvResource(new ClassPathResource("/META-INF/essential-data-import.mcsv"));
		createLocationAndInventory();
		OmsTestUtils.waitForInventory(aggregationService, SKU);
	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
		OmsTestUtils.clearAggregates(aggregationService);
	}

	@Test
	public void runFulfillment() throws Throwable
	{
		final long timeBefore = System.currentTimeMillis();

		// Insert orders
		for (int i = 1; i <= NUM_ORDERS; i++)
		{
			orderFacade.createOrder(buildOrder(Integer.toString(i)));
		}

		// Confirm shipments
		Collection<Shipment> shipments = null;
		for (int i = 1; i <= NUM_ORDERS; i++)
		{
			shipments = getShipments(Integer.toString(i));
			for (final Shipment shipment : shipments)
			{
				shipmentFacade.confirmShipment(shipment.getShipmentId());
			}
		}
		OmsTestUtils.waitForProcesses(runtimeService);

		final long timeAfter = System.currentTimeMillis();
		LOG.warn("Total run time for {} orders: {}ms", NUM_ORDERS, (timeAfter - timeBefore));

		// Assert that all orders are completely fulfilled
		Order order = null;
		for (int i = 1; i <= NUM_ORDERS; i++)
		{
			order = this.orderFacade.getOrderByOrderId(Integer.toString(i));
			Assert.assertEquals(0, order.getOrderLines().get(0).getQuantityUnassigned().getValue());
		}
	}

	/**
	 * Get the shipment from the order.
	 *
	 * @param order
	 * @return shipments
	 */
	private List<Shipment> getShipments(final String orderId)
	{
		List<Shipment> shipments = new ArrayList<>();
		boolean present = false;
		while (!present)
		{
			try
			{
				shipments = (List<Shipment>) shipmentFacade.getShipmentsByOrderId(orderId);
				if (shipments.isEmpty())
				{
					throw new IllegalStateException("No Shipments found for order " + orderId);
				}
				present = true;
			}
			catch (final IllegalStateException e)
			{
				OmsTestUtils.delay();
			}
		}
		return shipments;
	}

	/**
	 * Create a new {@link Location} and add on hand {@link OmsInventory} to it.
	 */
	private void createLocationAndInventory()
	{
		final Location location = new Location();
		location.setLocationId(LOC_ID);
		location.setPriority(1);
		location.setShipToCountriesCodes(Collections.singleton("CA"));
		location.setLocationRoles(Collections.singleton(LocationRole.SHIPPING));
		location.setAddress(AddressBuilder.anAddress().buildAddressDTO());
		this.inventoryFacade.createStockRoomLocation(location);

		final OmsInventory inventory = new OmsInventory();
		inventory.setSkuId(SKU);
		inventory.setLocationId(LOC_ID);
		inventory.setStatus(ON_HAND);
		inventory.setQuantity(QUANTITY * NUM_ORDERS);
		this.inventoryFacade.createInventory(inventory);
	}

	/**
	 * Build a new {@link Order} object.
	 *
	 * @return a new order
	 */
	private Order buildOrder(final String orderId)
	{
		final Order order = new Order();
		order.setOrderId(orderId);

		order.setUsername("IntegrationTest");
		order.setFirstName("Chuck");
		order.setLastName("Norris");
		order.setEmailid("chuck.norris@hybris.com");
		order.setShippingFirstName("shippingFirstName");
		order.setShippingLastName("shippingLastName");
		order.setShippingTaxCategory("shippingTaxCategory");
		order.setIssueDate(Calendar.getInstance().getTime());
		order.setCurrencyCode("USD");

		final ShippingAndHandling shippingAndHandling = new ShippingAndHandling();
		shippingAndHandling.setOrderId(order.getOrderId());
		shippingAndHandling.setShippingPrice(new Price(new Amount("USD", 2d), new Amount("USD", 0.5d), new Amount("USD", 0d)));

		order.setShippingAndHandling(shippingAndHandling);
		order.setContact(new Contact(null, "arvato", null, null, "1234567", null, null));
		order.setShippingAddress(AddressBuilder.anAddress().buildAddressDTO());
		order.setShippingMethod("DOM.EP");
		order.setPriorityLevelCode("STANDARD");

		final OrderLine orderLine = this.buildOrderLine(orderId, SKU, new Quantity("unit", QUANTITY),
				new Quantity("unit", QUANTITY), new Amount("USD", 1d), new Amount("USD", 0.15d), "AE514", LocationRole.SHIPPING,
				new OrderLineAttribute(ORDERLINE_ATTRIBUTE_VALUE, ORDERLINE_ATTRIBUTE_ID));
		order.setOrderLines(Lists.newArrayList(orderLine));

		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setPaymentInfoType("Visa");
		paymentInfo.setAuthUrl("http://authURL.hybris.com");
		paymentInfo.setBillingAddress(AddressBuilder.anAddress().buildAddressDTO());

		order.setPaymentInfos(Lists.newArrayList(paymentInfo));
		return order;
	}

	/**
	 * Build a new {@link OrderLine} object.
	 *
	 * @return a new OrderLine
	 */
	private OrderLine buildOrderLine(final String orderLineId, final String skuId, final Quantity quantity,
			final Quantity quantityUnassigned, final Amount unitPrice, final Amount unitTax, final String taxCategory,
			final LocationRole locationRole, final OrderLineAttribute orderlineAttribute)
	{
		final OrderLine orderLine = new OrderLine();
		orderLine.setOrderLineId(orderLineId);
		orderLine.setSkuId(skuId);
		orderLine.setQuantity(quantity);
		orderLine.setQuantityUnassigned(quantityUnassigned);
		orderLine.setUnitPrice(unitPrice);
		orderLine.setUnitTax(unitTax);
		orderLine.setTaxCategory(taxCategory);

		final HashSet<LocationRole> roles = new HashSet<>();
		roles.add(locationRole);
		orderLine.setLocationRoles(roles);
		orderLine.addOrderLineAttribute(orderlineAttribute);
		return orderLine;
	}

}
