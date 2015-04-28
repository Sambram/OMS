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
package com.hybris.oms.facade.conversion.impl.order;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;


public class OrderTestUtils
{
	public static final String STATE = "state";
	public static final String ORDER_ID = "dummyOrderId2";
	public static final String ORDER_LINE_ID_1 = "dummyOrderLineId";
	public static final String ORDER_LINE_ID_2 = "dummyOrderLineId2";

	private static final String SKU = "SKU";
	private static final int QUANTITY = 10;
	private static final String ORDERLINE_ATTRIBUTE_VALUE = "OrderLine.AttributeValue";
	private static final String ORDERLINE_ATTRIBUTE_ID = "OrderLIne.AttributeId";

	private OrderData orderData;
	private OrderLineData orderLineData;
	private OrderLineQuantityData orderLineQuantityData;
	private OrderLineAttributeData orderLineAttributeData;
	private PaymentInfoData paymentInfoData;
	private ShippingAndHandlingData shippingAndHandlingData;

	public OrderData getOrderData()
	{
		return this.orderData;
	}

	public OrderLineData getOrderLineData()
	{
		return this.orderLineData;
	}

	public OrderLineQuantityData getOrderLineQuantityData()
	{
		return this.orderLineQuantityData;
	}

	public OrderLineAttributeData getOrderLineAttributeData()
	{
		return this.orderLineAttributeData;
	}

	public PaymentInfoData getPaymentInfoData()
	{
		return this.paymentInfoData;
	}

	public ShippingAndHandlingData getShippingAndHandlingData()
	{
		return this.shippingAndHandlingData;
	}

	public void createObjects(final PersistenceManager persistenceManager)
	{
		createOrderData(persistenceManager);

		createShippingData(persistenceManager);

		createPaymentInfoData(persistenceManager);

		createOrderLineData(persistenceManager);

		createOlqData(persistenceManager);

		createOrderLineAttributeData(persistenceManager);
	}

	private void createOlqData(final PersistenceManager persistenceManager)
	{
		final OrderLineQuantityStatusData orderLineQuantityStatusData = persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData.setActive(Boolean.TRUE);
		orderLineQuantityStatusData.setDescription("description");
		orderLineQuantityStatusData.setStatusCode("statusCode");

		this.orderLineQuantityData = persistenceManager.create(OrderLineQuantityData.class);
		this.orderLineQuantityData.setStockroomLocationId("location");
		this.orderLineQuantityData.setOlqId(1);
		this.orderLineQuantityData.setQuantityUnitCode("unitCode");
		this.orderLineQuantityData.setQuantityValue(1);
		this.orderLineQuantityData.setStatus(orderLineQuantityStatusData);
		this.orderLineQuantityData.setOrderLine(this.orderLineData);
	}

	private void createOrderLineAttributeData(final PersistenceManager persistenceManager)
	{
		this.orderLineAttributeData = persistenceManager.create(OrderLineAttributeData.class);
		orderLineAttributeData.setDescription("description");
		orderLineAttributeData.setAttributeId("attributeId");
		orderLineAttributeData.setOrderLine(this.orderLineData);
	}

	private void createOrderLineData(final PersistenceManager persistenceManager)
	{
		this.orderLineData = persistenceManager.create(OrderLineData.class);
		this.orderData.setOrderLines(Arrays.asList(this.orderLineData));
		this.orderLineData.setMyOrder(this.orderData);
		this.orderLineData.setOrderLineId(ORDER_LINE_ID_1);
		this.orderLineData.setQuantityUnassignedValue(2);
		this.orderLineData.setQuantityValue(2);
		this.orderLineData.setSkuId("dummySkuId");
		this.orderLineData.setTaxCategory("dummyTaxCategory");
		this.orderLineData.setUnitPriceValue(5.56);
		this.orderLineData.setUnitTaxValue(7.62);
		this.orderLineData.setUnitPriceCurrencyCode("CAD");
		this.orderLineData.setUnitTaxCurrencyCode("CAD");
		this.orderLineData.setLocationRoles(ImmutableSet.of(com.hybris.oms.service.managedobjects.inventory.LocationRole.SHIPPING
				.getCode()));
	}

	private void createPaymentInfoData(final PersistenceManager persistenceManager)
	{
		this.paymentInfoData = persistenceManager.create(PaymentInfoData.class);
		this.paymentInfoData.setAuthUrl("dummyAuthUrl");
		this.paymentInfoData.setBillingAddress(new AddressVT("al1", "al5", "cN3", "cS2", "pZ4", 1.23, 4.56, "CA", "cName3", null,
				null));
		this.paymentInfoData.setPaymentInfoType("dummyPaymentInfoType");
		this.paymentInfoData.setMyOrder(this.orderData);
		this.orderData.setPaymentInfos(Arrays.asList(this.paymentInfoData));
	}

	private void createShippingData(final PersistenceManager persistenceManager)
	{
		final Price shippingPrice = new Price(new Amount(null, 3.4), new Amount(null, 4.5), null);
		final PriceVT priceVT = new PriceVT(shippingPrice.getSubTotal().getCurrencyCode(), shippingPrice.getSubTotal().getValue(),
				shippingPrice.getTax().getCurrencyCode(), shippingPrice.getTax().getValue(), null, null);


		this.shippingAndHandlingData = persistenceManager.create(ShippingAndHandlingData.class);
		this.shippingAndHandlingData.setOrderId(ORDER_ID);
		this.shippingAndHandlingData.setShippingPrice(priceVT);
		this.orderData.setShippingAndHandling(this.shippingAndHandlingData);
	}

	private void createOrderData(final PersistenceManager persistenceManager)
	{
		this.orderData = persistenceManager.create(OrderData.class);
		this.orderData.setShippingAddress(new AddressVT("al2", "al3", "cN", "cS", "pZ", 1.23, 4.56, "CA", "cName", null, null));
		this.orderData.setState(STATE);
		this.orderData.setFirstName("dummyFirstName");
		this.orderData.setLastName("dummyLastName");
		this.orderData.setEmailid("dummy@test.de");
		this.orderData.setOrderId(ORDER_ID);
		this.orderData.setIssueDate(new Date());
		this.orderData.setShippingMethod("dummyShippingMethod");
		this.orderData.setShippingFirstName("shippingFirstName");
		this.orderData.setShippingLastName("shippingLastName");
		this.orderData.setShippingTaxCategory("shippingTaxCategory");
		this.orderData.setUsername("username");
		this.orderData.setCurrencyCode("USD");
	}

	/**
	 * Build a new {@link Order} object.
	 * 
	 * @return a new order
	 */
	public static Order buildTestOrder(final String orderId)
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

		final OrderLine orderLine = buildOrderLine(ORDER_LINE_ID_2, SKU, new Quantity("unit", QUANTITY), new Quantity("unit",
				QUANTITY), new Amount("USD", 1d), new Amount("USD", 0.15d), "AE514", LocationRole.SHIPPING, new OrderLineAttribute(
				ORDERLINE_ATTRIBUTE_VALUE, ORDERLINE_ATTRIBUTE_ID));

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
	private static OrderLine buildOrderLine(final String orderLineId, final String skuId, final Quantity quantity,
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
