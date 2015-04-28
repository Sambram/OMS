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
package com.hybris.oms.rest.integration.test;

import com.hybris.oms.api.ats.AtsFacade;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.basestore.BaseStore;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.rule.RuleShort;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/**
 * Tests the client by connecting them to a deployed rest-webapp, must be executed in integration-test phase.
 * Note that all CIS calls have to be mocked for integration tests. This is achieved by enabling the CIS mock in the
 * oms-rest-webapp
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-integration-test-spring.xml"})
@Ignore
public abstract class RestClientIntegrationTest
{
	public static final String KEY_PROPERTY_WIDTH_VALUE = "shipmentWidthValue";
	public static final String KEY_PROPERTY_LENGTH_VALUE = "shipmentLengthValue";
	public static final String KEY_PROPERTY_HEIGHT_VALUE = "shipmentHeightValue";
	public static final String KEY_PROPERTY_HEIGHT_UNIT_CODE = "shipmentHeightUnitCode";
	public static final String KEY_PROPERTY_GROSS_WEIGHT_VALUE = "shipmentGrossWeightValue";
	public static final String KEY_PROPERTY_GROSS_WEIGHT_UNIT_CODE = "shipmentGrossWeightUnitCode";
	public static final String KEY_PROPERTY_INSURANCE_VALUE_AMOUNT_VALUE = "shipmentInsuranceValueAmountValue";
	public static final String KEY_PROPERTY_DESCRIPTION = "shipmentDescription";
	public static final String MODULE_DEFAULT = "oms";

	public static final String ORDERLINE_ATTRIBUTE_VALUE = "OrderLine.AttributeValue";
	public static final String ORDERLINE_ATTRIBUTE_ID = "OrderLIne.AttributeId";

	protected static final String ON_HAND = "ON_HAND";
	protected static final String NOT_AVAILABLE = "NOT_AVAILABLE";

	protected static final String STATUS_ALLOCATED = "ALLOCATED";
	protected static final String STATUS_SOURCED = "SOURCED";
	protected static final String STATUS_PICKED = "PICKED";
	protected static final String STATUS_PACKED = "PACKED";
	protected static final String STATUS_SHIPPED = "SHIPPED";
	protected static final String STATUS_PAYMENT_CAPTURED = "PAYMENT_CAPTURED";
	protected static final String STATUS_TAX_INVOICED = "TAX_INVOICED";
	protected static final String STATUS_CANCELLED = "CANCELLED";
	protected static final String STATUS_ON_HOLD = "ON_HOLD";
	protected static final String STATUS_DECLINED = "DECLINED";
	protected static final String UPDATE_STRATEGY = "OLQ_STRATEGY";

	protected static final String PAYMENT_AUTH = "/cisPaymentCybersource/authorizations/9876/3602733983730176056428/";

	protected static final String BIN_DESCRIPTION = "description";
	protected static final int BIN_PRIORITY = 1;

	private static final int DELAY_MILLIS = 500;
	private static final int RETRIES = 50;

	@Value("${hybris.gateway.uri}")
	private String gatewayUri;

	@Autowired
	private PreferenceFacade tenantPreferenceFacade;

	@Autowired
	private OrderFacade orderFacade;

	@Autowired
	private InventoryFacade inventoryFacade;

	@Autowired
	private ShipmentFacade shipmentFacade;

	@Autowired
	private AtsFacade atsFacade;

	protected OmsInventory buildInventory(final String sku, final String locationId, final String binCode,
			final String statusCode, final Date deliveryDate, final int quantity)
	{
		final OmsInventory inventory = new OmsInventory();
		inventory.setSkuId(sku);
		inventory.setLocationId(locationId);
		inventory.setBinCode(binCode);
		inventory.setStatus(statusCode);
		inventory.setDeliveryDate(deliveryDate);
		inventory.setQuantity(quantity);
		return inventory;
	}

	protected OmsInventory buildInventory(final String sku, final String locationId, final String statusCode,
			final Date deliveryDate, final int quantity)
	{
		return buildInventory(sku, locationId, null, statusCode, deliveryDate, quantity);
	}

	protected Location buildLocation(final String locationId)
	{
		return buildLocation(locationId, LocationRole.SHIPPING, null);
	}

	protected Location buildLocation(final String locationId, final LocationRole locationRole, final Set<String> baseStores)
	{
		final Location location = new Location();
		location.setLocationId(locationId);
		location.setDescription("description");
		location.setPriority(1);
		location.setAddress(this.buildAddress());
		location.setActive(true);
		location.setShipToCountriesCodes(Sets.newHashSet(location.getAddress().getCountryIso3166Alpha2Code()));
		final HashSet<LocationRole> roles = new HashSet<>();
		roles.add(locationRole);
		location.setLocationRoles(roles);
		location.setBaseStores(baseStores);
		return location;
	}

    protected void deleteInventory(OmsInventory inventory)
    {
        this.inventoryFacade.deleteInventory(inventory);
    }

	protected OmsInventory createInventory()
	{
		final String locationId = this.generateLocationId();
		final String sku = this.generateSku();
		return createInventory(sku, locationId);
	}

	protected OmsInventory createInventory(final String binCode)
	{
		final String locationId = this.generateLocationId();
		final String sku = this.generateSku();
		return createInventory(sku, locationId, LocationRole.SHIPPING, binCode, null);
	}

	protected OmsInventory createInventory(final String sku, final String locationId)
	{
		return createInventory(sku, locationId, LocationRole.SHIPPING);
	}

	protected OmsInventory createInventory(final String sku, final String locationId, final Set<String> baseStores)
	{
		return createInventory(sku, locationId, LocationRole.SHIPPING, null, baseStores);
	}

	protected OmsInventory createInventory(final String sku, final String locationId, final LocationRole locationRole)
	{
		return createInventory(sku, locationId, locationRole, null, null);
	}

	protected OmsInventory createInventory(final String sku, final String locationId, final LocationRole locationRole,
			final String binCode, final Set<String> baseStores)
	{
        return createInventory(sku, locationId, locationRole, binCode, baseStores, null);
	}

    protected OmsInventory createInventory(final String sku, final String locationId, final LocationRole locationRole,
                                           final String binCode, final Set<String> baseStores, final Date expectedDeliveryDate)
    {
        final Location location = this.buildLocation(locationId, locationRole, baseStores);

        try{
            this.inventoryFacade.getStockRoomLocationByLocationId(locationId);
        } catch(EntityNotFoundException enfe){
            this.inventoryFacade.createStockRoomLocation(location);
        }

        final OmsInventory inventory = this.buildInventory(sku, locationId, binCode, ON_HAND, expectedDeliveryDate, 100);
        this.inventoryFacade.createUpdateInventory(inventory);
        waitForAggregation(sku, locationId, ON_HAND);
        return inventory;
    }

	protected OmsInventory createInventoryWithBin(final String sku, final String locationId, final String binCode)
	{
		final OmsInventory inventory = this.buildInventory(sku, locationId, binCode, ON_HAND, null, 0);
		this.inventoryFacade.createUpdateInventory(inventory);
		waitForAggregation(sku, locationId, ON_HAND);
		return inventory;
	}

	protected OmsInventory updateInventory(final String sku, final String locationId, final String status, final String quantity)
	{
		return updateInventory(sku, locationId, null, status, quantity);
	}

	protected OmsInventory updateInventory(final String sku, final String locationId, final String binCode, final String status,
			final String quantity)
	{
		final OmsInventory inventory = this.buildInventory(sku, locationId, binCode, status, null,
				(quantity == null) ? 0 : Integer.valueOf(quantity));
		this.inventoryFacade.createUpdateInventory(inventory);
		waitForAggregation(sku, locationId, status);
		return inventory;
	}

	protected Location createLocation(final String locationId, final LocationRole locationRole, final Set<String> baseStores)
	{
		final Location location = this.buildLocation(locationId, locationRole, baseStores);
		this.inventoryFacade.createStockRoomLocation(location);
		return location;
	}

	private void waitForAggregation(final String sku, final String locationId, final String status)
	{
		for (int i = 0; i < RETRIES; i++)
		{
			if (!atsFacade
					.findLocalAts(Collections.singleton(sku), Collections.singleton(locationId), Collections.singleton(status))
					.isEmpty())
			{
				break;
			}
			delay();
		}
	}

	protected Address buildAddress()
	{
		final Address address = new Address("502 MAIN ST N", "26th floor", "MONTREAL", "QC", "H2B1A0", null, null, "CA", "Canada",
				"companyName", "4388888888");

		return address;
	}

	protected ItemStatus buildItemStatus(final String statusCode, final String description)
	{
		final ItemStatus itemStatus = new ItemStatus();
		itemStatus.setDescription(description);
		itemStatus.setStatusCode(statusCode);
		return itemStatus;
	}

	protected Order buildOrder()
	{
		return this.buildOrder(this.generateSku());
	}

	protected BaseStore buildBaseStore(final String baseStoreName)
	{
		final BaseStore baseStore = new BaseStore();
		baseStore.setName(baseStoreName);
		baseStore.setDescription("description");
		return baseStore;
	}

	protected Order buildOrder(final String sku)
	{
		final String orderId = "order_" + this.generateRandomString();
		return this.buildOrder(sku, orderId);
	}

	protected Order buildOrder(final String sku, final String orderId)
	{
		return buildOrder(sku, orderId, null);
	}

	protected Order buildOrder(final String sku, final String orderId, final String orderLineId)
	{
		return buildOrder(sku, orderId, orderLineId, 2);
	}

	protected Order buildOrder(final String sku, final String orderId, final String orderLineId, final int quantity)
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

		final Address shippingAddress = new Address("502 MAIN ST N", "26th floor", "MONTREAL", "QC", "H2B1A0", null, null, "CA",
				"Canada", orderId, orderId);

		order.setContact(new Contact(null, "arvato", null, null, "1234567", null, null));
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod("DOM.EP");
		order.setPriorityLevelCode("STANDARD");

		final OrderLine orderLine = this.buildOrderLine(orderLineId, sku, new Quantity("unit", quantity), new Quantity("unit",
				quantity), new Amount("USD", 1d), new Amount("USD", 0.15d), "AE514", LocationRole.SHIPPING, new OrderLineAttribute(
				ORDERLINE_ATTRIBUTE_VALUE, ORDERLINE_ATTRIBUTE_ID));

		order.setOrderLines(Lists.newArrayList(orderLine));
		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setPaymentInfoType("Visa");
		paymentInfo.setAuthUrl(getPaymentAuthUrl());
		paymentInfo.setBillingAddress(new Address("2207 7th Avenue", null, "New York", "NY", "10027", 40.812356, -73.945857, "US",
				"U.S.A.", orderId, orderId));

		order.setPaymentInfos(Lists.newArrayList(paymentInfo));
		return order;
	}

	protected OrderLine buildOrderLine(final String orderLineId, final String skuId, final Quantity quantity,
			final Quantity quantityUnassigned, final Amount unitPrice, final Amount unitTax, final String taxCategory,
			final LocationRole locationRole, final OrderLineAttribute orderlineAttribute)
	{
		final OrderLine orderLine = new OrderLine();

		if (orderLineId == null || orderLineId.trim().length() == 0)
		{
			orderLine.setOrderLineId(UUID.randomUUID().toString());
		}
		else
		{
			orderLine.setOrderLineId(orderLineId);
		}

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

	/**
	 * Build a fully valid order with a shipping Address.
	 */
	protected Order buildOrder(final Address shippingAddress)
	{
		final Order order = this.buildOrder();
		order.setShippingAddress(shippingAddress);
		return order;
	}

	/**
	 * Build a fully valid order.
	 */
	public Order buildOrderWithOLQ()
	{
		final Order order = this.buildOrder();
		final OrderLine orderLine = order.getOrderLines().get(0);

		final List<OrderLineQuantity> lstOLQ = new ArrayList<OrderLineQuantity>();
		final OrderLineQuantity olq = new OrderLineQuantity();
		olq.setOlqId("1");
		olq.setQuantity(new Quantity("DDT", 2));
		olq.setLocation("Loc");

		final OrderLineQuantityStatus olqs = new OrderLineQuantityStatus();
		olqs.setActive(true);
		olqs.setDescription("Cancelled");
		olqs.setStatusCode("CANCELLED");

		olq.setStatus(olqs);

		lstOLQ.add(olq);
		orderLine.setOrderLineQuantities(lstOLQ);
		order.setOrderLines(null);
		order.setOrderLines(Lists.newArrayList(orderLine));

		final PaymentInfo paymentInfo = new PaymentInfo();

		paymentInfo.setPaymentInfoType("Visa");
		paymentInfo.setAuthUrl(getPaymentAuthUrl());
		paymentInfo.setBillingAddress(new Address("2207 7th Avenue", null, "New York", "NY", "10027", 40.812356, -73.945857, "US",
				"U.S.A.", null, null));

		order.setPaymentInfos(Lists.newArrayList(paymentInfo));
		return order;
	}

	/**
	 * Build a valid sourced order.
	 */
	public Order buildOrderWithOLQAndShipment(final String locationId)
	{
		final Order order = this.buildOrder();
		final OrderLine orderLine = order.getOrderLines().get(0);

		orderLine.setQuantityUnassigned(new Quantity("unit", 0));

		final List<OrderLineQuantity> lstOLQ = new ArrayList<OrderLineQuantity>();
		final OrderLineQuantity olq = new OrderLineQuantity();
		olq.setOlqId(this.generateLongIdAsString());
		olq.setQuantity(new Quantity("DDT", 2));
		olq.setLocation(locationId);

		final OrderLineQuantityStatus olqs = new OrderLineQuantityStatus();
		olqs.setActive(true);
		olqs.setDescription("Sourced");
		olqs.setStatusCode("SOURCED");

		olq.setStatus(olqs);

		final Shipment shipment = new Shipment();
		shipment.setAmountCaptured(new Amount("USD", 2d));
		shipment.setAuthUrls(Arrays.asList("authurl"));
		shipment.setCurrencyCode("USD");
		shipment.setDelivery(null);
		shipment.setShipmentId(this.generateLongIdAsString());
		shipment.setShippingMethod(order.getShippingMethod());
		shipment.setOrderId(order.getOrderId());
		shipment.setShippingAndHandling(order.getShippingAndHandling());

		olq.setShipment(shipment);

		lstOLQ.add(olq);
		orderLine.setOrderLineQuantities(lstOLQ);
		order.setOrderLines(Lists.newArrayList(orderLine));

		final PaymentInfo paymentInfo = new PaymentInfo();

		paymentInfo.setPaymentInfoType("Visa");
		paymentInfo.setAuthUrl(getPaymentAuthUrl());
		paymentInfo.setBillingAddress(new Address("2207 7th Avenue", null, "New York", "NY", "10027", 40.812356, -73.945857, "US",
				"U.S.A.", null, null));

		order.setPaymentInfos(Lists.newArrayList(paymentInfo));

		return order;
	}

	protected List<Shipment> buildShipments(final String sku, final String orderId, final String locationId)
	{
		return buildShipments(sku, orderId, locationId, null);
	}

	protected List<Shipment> buildShipments(final String sku, final String orderId, final String locationId,
			final String orderLineId)
	{

		return buildShipments(sku, orderId, locationId, orderLineId, 2);
	}

	protected List<Shipment> buildShipments(final String sku, final String orderId, final String locationId,
			final String orderLineId, final int quantity)
	{
		Order order = this.buildOrder(sku, orderId, orderLineId, quantity);

		if (locationId != null)
		{
			order.setLocationIds(Arrays.asList(locationId));
		}

		order.getPaymentInfos().get(0).setAuthUrl(getPaymentAuthUrl());
		order = this.orderFacade.createOrder(order);
		this.delay(1000);

		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		queryObject.setOrderIds(Collections.singletonList(order.getOrderId()));
		return shipmentFacade.findShipmentsByQuery(queryObject).getResults();
	}

	protected List<Shipment> buildShipments(final String sku)
	{
		return this.buildShipments(sku, "order_" + this.generateRandomString(), null);
	}

	protected List<Shipment> buildShipmentsWithTwoOrderLines(final String sku, final int olq1, final int olq2)
	{
		Order order = this.buildOrderTwoOrderLines(sku, olq1, olq2);
		order.getPaymentInfos().get(0).setAuthUrl(getPaymentAuthUrl());
		order = this.orderFacade.createOrder(order);
		this.delay(1000);

		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		queryObject.setOrderIds(Collections.singletonList(order.getOrderId()));
		return shipmentFacade.findShipmentsByQuery(queryObject).getResults();
	}

	protected List<Shipment> buildShipmentsWithTwoOrderLines(final String sku)
	{

		return buildShipmentsWithTwoOrderLines(sku, 2, 2);
	}

	protected RuleShort buildRuleShort()
	{
		final RuleShort ruleShort = new RuleShort();
		ruleShort.setOlqFromStatus("SOURCED");
		ruleShort.setOlqToStatus("ALLOCATED");
		ruleShort.setInventoryStatus("ON_HAND");
		ruleShort.setChange("1");
		ruleShort.setUpdateStrategy(UPDATE_STRATEGY);

		return ruleShort;
	}

	protected Order buildOrderTwoOrderLines(final String sku, final int olq1, final int olq2)
	{
		final String orderId = "order_" + this.generateRandomString();
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

		final Address shippingAddress = new Address("502 MAIN ST N", "26th floor", "MONTREAL", "QC", "H2B1A0", null, null, "CA",
				"Canada", orderId, orderId);

		order.setContact(new Contact(null, "arvato", null, null, "1234567", null, null));
		order.setShippingAddress(shippingAddress);
		order.setShippingMethod("DOM.EP");
		order.setPriorityLevelCode("STANDARD");

		final List<OrderLine> orderLines = new ArrayList<OrderLine>();

		final OrderLine orderLine = this.buildOrderLine(UUID.randomUUID().toString(), sku, new Quantity("unit", olq1),
				new Quantity("unit", 2), new Amount("USD", 1d), new Amount("USD", 0.15d), "AE514", LocationRole.SHIPPING,
				new OrderLineAttribute(ORDERLINE_ATTRIBUTE_VALUE, ORDERLINE_ATTRIBUTE_ID));
		orderLines.add(orderLine);

		final OrderLine orderLine2 = this.buildOrderLine(UUID.randomUUID().toString(), sku, new Quantity("unit", olq2),
				new Quantity("unit", 2), new Amount("USD", 1d), new Amount("USD", 0.15d), "AE514", LocationRole.SHIPPING,
				new OrderLineAttribute(ORDERLINE_ATTRIBUTE_VALUE, ORDERLINE_ATTRIBUTE_ID));

		orderLines.add(orderLine2);
		order.setOrderLines(orderLines);

		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setPaymentInfoType("Visa");
		paymentInfo.setAuthUrl(getPaymentAuthUrl());
		paymentInfo.setBillingAddress(new Address("2207 7th Avenue", null, "New York", "NY", "10027", 40.812356, -73.945857, "US",
				"U.S.A.", orderId, orderId));

		order.setPaymentInfos(Lists.newArrayList(paymentInfo));
		return order;
	}

	protected Order buildOrderTwoOrderLines(final String sku)
	{

		return buildOrderTwoOrderLines(sku, 2, 2);
	}

	protected Bin buildBin(final String binCode, final String locationId)
	{
		return this.buildBin(binCode, locationId, BIN_PRIORITY, BIN_DESCRIPTION);
	}

	protected Bin buildBin(final String binCode, final String locationId, final int priority, final String description)
	{
		final Bin bin = new Bin();
		bin.setBinCode(binCode);
		bin.setLocationId(locationId);
		bin.setDescription(description);
		bin.setPriority(priority);
		return bin;
	}

	protected String generateLongIdAsString()
	{
		final SecureRandom random = new SecureRandom();
		return String.valueOf(random.nextInt(999999));
	}

	protected String generateLocationId()
	{
		return "loc_" + this.generateRandomString();
	}

	protected String generateBaseStoreId()
	{
		return "base_" + this.generateRandomString();
	}

	protected String generateSku()
	{
		return "sku_" + this.generateRandomString();
	}

	protected String generateRandomString()
	{
		return UUID.randomUUID().toString();
	}

	protected String generateBinCode()
	{
		return "bin_" + this.generateRandomString();
	}

	protected String getPaymentAuthUrl()
	{
		return gatewayUri + PAYMENT_AUTH;
	}

	protected void delay()
	{
		try
		{
			Thread.sleep(DELAY_MILLIS);
		}
		catch (final InterruptedException e)
		{
			//
		}
	}

	protected void delay(final long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (final InterruptedException e)
		{
			//
		}
	}

	protected InventoryFacade getInventoryFacade()
	{
		return inventoryFacade;
	}
}
