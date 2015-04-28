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
package com.hybris.oms.facade.validation.impl.order;

import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.domain.shipping.Delivery;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Contact;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.impl.valuetype.DefaultAddressValidator;
import com.hybris.oms.service.basestore.BaseStoreService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings({"PMD.ExcessiveImports"})
public class OrderValidatorTest
{
	private static final String BASE_STORE_NAME = "baseStore1";
	private static final String COUNTRY_CODE_VALID = "US";

	private ValidationContext context;
	private Order order;

	@InjectMocks
	private OrderValidator orderValidator;

	@Mock
	private Validator<OrderLine> orderLineValidator;

	@Mock
	private PaymentInfoValidator paymentInfoValidator;

	@Mock
	private final Validator<Address> addressValidator = new DefaultAddressValidator();

	@Mock
	private Validator<String> currencyValidator;

	@Mock
	private InventoryService inventoryService;

	@Mock
	private BaseStoreService baseStoreService;

	@Before
	public void setUp()
	{
		context = new ValidationContext();
		order = buildOrder();
		addOrderLines();
		addPaymentInfos();
	}

	@Test
	public void shouldFailOnBaseStoreValidationIfNotExists()
	{
		// Given
		order.setBaseStoreName(BASE_STORE_NAME);
		BDDMockito.given(baseStoreService.findBaseStoreByName(BASE_STORE_NAME)).willReturn(null);

		// When
		this.orderValidator.validate("Order", this.context, this.order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.INVALID, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Order.baseStoreName", this.context.getFailures().get(0).getFieldName());
		BDDMockito.verify(baseStoreService, Mockito.times(1)).findBaseStoreByName(BASE_STORE_NAME);
	}

	@Test
	public void shouldPassOnBaseStoreValidationIfExists()
	{
		// Given
		order.setBaseStoreName(BASE_STORE_NAME);
		final BaseStoreData baseStoreData = Mockito.mock(BaseStoreData.class);
		BDDMockito.given(baseStoreService.findBaseStoreByName(BASE_STORE_NAME)).willReturn(baseStoreData);

		// When
		this.orderValidator.validate("Order", this.context, this.order);

		// Then
		Assert.assertFalse(this.context.containsFailures());
		BDDMockito.verify(baseStoreService, Mockito.times(1)).findBaseStoreByName(BASE_STORE_NAME);
	}

	@Test
	public void shouldPassOnBaseStoreValidationIfBaseStoreNameNull()
	{
		// Given
		order.setBaseStoreName(null);

		// When
		this.orderValidator.validate("Order", this.context, this.order);

		// Then
		Assert.assertFalse(this.context.containsFailures());
		BDDMockito.verify(baseStoreService, Mockito.times(0)).findBaseStoreByName(BASE_STORE_NAME);
	}

	@Test
	public void shouldFailOnPaymentInfoValidationWithNullPaymentInfo()
	{
		// Given
		order.setPaymentInfos(null);

		// When
		this.orderValidator.validate("Order", context, order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		BDDMockito.verifyZeroInteractions(paymentInfoValidator);
	}

	@Test
	public void shouldFailOnPaymentInfoValidationWithEmptyPaymentInfo()
	{
		// Given
		order.setPaymentInfos(new ArrayList<PaymentInfo>());

		// When
		this.orderValidator.validate("Order", context, order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		BDDMockito.verifyZeroInteractions(paymentInfoValidator);
	}

	@Test
	public void shouldPassWhenSubscribedToPaymentServiceAndPaymentInfoIsOk()
	{
		// Given

		// When
		this.orderValidator.validate("Order", context, order);

		// Then
		Assert.assertFalse(this.context.containsFailures());
		BDDMockito.verify(paymentInfoValidator, Mockito.times(1)).validateInternal((ValidationContext) Mockito.anyObject(),
				(PaymentInfo) Mockito.anyObject());
	}

	@Test
	public void shouldPassWhenAllOrderLinesArePickupAndShippingInfoNull()
	{
		// Given
		for (final OrderLine orderLine : order.getOrderLines())
		{
			orderLine.setPickupStoreId("StoreId01");
		}
		order.setShippingAddress(null);
		order.setShippingAndHandling(null);
		order.setShippingFirstName(null);
		order.setShippingLastName(null);
		order.setShippingMethod(null);

		// When
		this.orderValidator.validate("Order", this.context, order);

		// Then
		Assert.assertFalse(this.context.containsFailures());
		BDDMockito.verifyZeroInteractions(addressValidator);
	}

	@Test
	public void shouldFailWhenNotAllOrderLinesArePickupAndShippingAddressNull()
	{
		// Given
		order.setShippingAddress(null);

		// When
		this.orderValidator.validate("Order", this.context, order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_NULL, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Order.shippingAddress", this.context.getFailures().get(0).getFieldName());
		BDDMockito.verify(addressValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (Address) Mockito.anyObject());
		BDDMockito.verify(orderLineValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (OrderLine) Mockito.anyObject(), (FailureHandler) Mockito.anyObject());
	}

	@Test
	public void shouldFailWhenNotAllOrderLinesArePickupAndShippingAndHandlingNull()
	{
		// Given
		order.setShippingAndHandling(null);

		// When
		this.orderValidator.validate("Order", this.context, order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_NULL, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Order.shippingAndHandling", this.context.getFailures().get(0).getFieldName());
		BDDMockito.verify(addressValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (Address) Mockito.anyObject());
		BDDMockito.verify(orderLineValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (OrderLine) Mockito.anyObject(), (FailureHandler) Mockito.anyObject());
	}

	@Test
	public void shouldFailWhenNotAllOrderLinesArePickupAndShippingFirstNameNull()
	{
		// Given
		order.setShippingFirstName("");

		// When
		this.orderValidator.validate("Order", this.context, order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Order.shippingFirstName", this.context.getFailures().get(0).getFieldName());
		BDDMockito.verify(addressValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (Address) Mockito.anyObject());
		BDDMockito.verify(orderLineValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (OrderLine) Mockito.anyObject(), (FailureHandler) Mockito.anyObject());
	}

	@Test
	public void shouldFailWhenNotAllOrderLinesArePickupAndShippingLastNameNull()
	{
		// Given
		order.setShippingLastName("");

		// When
		this.orderValidator.validate("Order", this.context, order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Order.shippingLastName", this.context.getFailures().get(0).getFieldName());
		BDDMockito.verify(addressValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (Address) Mockito.anyObject());
		BDDMockito.verify(orderLineValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (OrderLine) Mockito.anyObject(), (FailureHandler) Mockito.anyObject());
	}

	@Test
	public void shouldFailWhenNotAllOrderLinesArePickupAndShippingMethodNull()
	{
		// Given
		order.setShippingMethod("");

		// When
		this.orderValidator.validate("Order", this.context, order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Order.shippingMethod", this.context.getFailures().get(0).getFieldName());
		BDDMockito.verify(addressValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (Address) Mockito.anyObject());
		BDDMockito.verify(orderLineValidator, Mockito.atLeast(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), (OrderLine) Mockito.anyObject(), (FailureHandler) Mockito.anyObject());
	}

	public void shouldFailWhenShippingTaxCategoryNull()
	{
		// Given
		order.setShippingTaxCategory("");

		// When
		this.orderValidator.validate("Order", this.context, order);

		// Then
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Order.shippingTaxCategory", this.context.getFailures().get(0).getFieldName());
	}

	@Test
	public void shouldPassWhenShippingTaxCategoryOk()
	{
		// Given

		// When
		this.orderValidator.validate("Order", this.context, order);

		// Then
		Assert.assertFalse(this.context.containsFailures());
		BDDMockito.verify(currencyValidator, Mockito.times(1)).validate(Mockito.anyString(),
				(ValidationContext) Mockito.anyObject(), Mockito.anyString());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldPassWhenOrderLinePickupStoreIdIsInOrderLocationIds()
	{
		// Given
		order.setLocationIds(Lists.newArrayList("LOC1", "LOC2"));
		order.getOrderLines().get(0).setPickupStoreId("LOC1");
		final List<StockroomLocationData> locationDatas = Mockito.mock(List.class);
		BDDMockito.given(locationDatas.size()).willReturn(2);
		BDDMockito.given(inventoryService.findLocationsByLocationIds(Mockito.anySet())).willReturn(locationDatas);

		// When
		this.orderValidator.validate("Order", this.context, this.order);

		// Then
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailOrderLineIdValidationWithTwoOrderLinesWithSameIds()
	{
		// Give set 2 order lines to have the same id
		order.getOrderLines().get(0).setOrderLineId("1");
		order.getOrderLines().get(1).setOrderLineId("1");

		// When we call validation
		this.orderValidator.validateInternal(this.context, this.order);

		// Then failure will be reported
		Assert.assertTrue(this.context.containsFailures());
	}

	@Test
	public void shouldFailOnOlqIdValidationWithInvalidOlqAndShipmentLink()
	{
		// Given
		addOrderLineQuantities(this.order.getOrderLines().get(0));
		final Shipment shipment = buildShipment();
		shipment.setOlqIds(Lists.newArrayList("olqId1", "olqId2", "olqId3"));
		this.order.getOrderLines().get(0).getOrderLineQuantities().get(0).setShipment(shipment);
		this.order.getOrderLines().get(0).getOrderLineQuantities().get(1).setShipment(shipment);

		// When
		this.orderValidator.validate("Order", context, this.order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
	}

	@Test
	public void shouldFailWhenScheduledShippingDateIsBeforeIssueDate()
	{
		// Given
		this.populateShippingDateFromIssueDate(order, -2);

		// When
		this.orderValidator.validate("Order", context, this.order);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
	}

	private void populateShippingDateFromIssueDate(final Order order, final int daysToRoll)
	{
		final Calendar cal = Calendar.getInstance();
		final Date date = cal.getTime();
		order.setIssueDate(date);

		cal.add(Calendar.DAY_OF_YEAR, daysToRoll);
		order.setScheduledShippingDate(cal.getTime());
	}

	@Test
	public void shouldPassOnOlqIdValidationWithValidOlqAndShipmentLink()
	{
		// Given
		addOrderLineQuantities(this.order.getOrderLines().get(0));
		final Shipment shipment = buildShipment();
		shipment.setOlqIds(Lists.newArrayList("olqId1", "olqId2"));
		this.order.getOrderLines().get(0).getOrderLineQuantities().get(0).setShipment(shipment);
		this.order.getOrderLines().get(0).getOrderLineQuantities().get(1).setShipment(shipment);

		// When
		this.orderValidator.validate("Order", context, this.order);

		// Then
		Assert.assertEquals(0, this.context.getFailureCount());
	}


	/**
	 * Build a fully valid order.
	 *
	 * @return order
	 */
	private Order buildOrder()
	{
		final Order tempOrder = new Order();
		tempOrder.setOrderId("Order 1234");
		tempOrder.setUsername("IntegrationTest");
		tempOrder.setFirstName("Chuck");
		tempOrder.setLastName("Norris");
		tempOrder.setShippingFirstName("Chuck");
		tempOrder.setShippingLastName("Norris");
		tempOrder.setEmailid("chuck.norris@hybris.com");
		tempOrder.setShippingTaxCategory("AE514");

		this.populateShippingDateFromIssueDate(tempOrder, 2);

		final Address shippingAddress = new Address("2207 7th Avenue", null, "New York", "NY", "10027", 40.812356, -73.945857,
				COUNTRY_CODE_VALID, "U.S.A.", null, null);

		tempOrder.setContact(new Contact(null, "arvato", null, null, "1234567", null, null));

		tempOrder.setShippingAddress(shippingAddress);
		tempOrder.setShippingMethod("DOM.EP");
		tempOrder.setPriorityLevelCode("STANDARD");

		final ShippingAndHandling shippingAndHandling = new ShippingAndHandling();
		shippingAndHandling.setOrderId(tempOrder.getOrderId());
		shippingAndHandling.setShippingPrice(new Price(new Amount("USD", 2d), new Amount("USD", 0.5d), new Amount("USD", 0d)));
		tempOrder.setShippingAndHandling(shippingAndHandling);

		return tempOrder;
	}

	private void addOrderLines()
	{
		final OrderLine orderLine = new OrderLine();
		orderLine.setOrderLineId("Order Line Id 1234");
		orderLine.setSkuId("Sku ABC");
		orderLine.setQuantity(new Quantity("unit", 2));
		orderLine.setQuantityUnassigned(new Quantity("unit", 2));
		orderLine.setUnitPrice(new Amount("USD", 1d));
		orderLine.setUnitTax(new Amount("USD", 0.15d));
		orderLine.setTaxCategory("AE514");


		final OrderLine orderLine2 = new OrderLine();
		orderLine2.setOrderLineId("Order Line Id 4567");
		orderLine2.setSkuId("Sku DEF");
		orderLine2.setQuantity(new Quantity("unit", 5));
		orderLine2.setQuantityUnassigned(new Quantity("unit", 2));
		orderLine2.setUnitPrice(new Amount("USD", 10d));
		orderLine2.setUnitTax(new Amount("USD", 1.5d));
		orderLine2.setTaxCategory("AE514");

		this.order.setOrderLines(Lists.newArrayList(orderLine, orderLine2));
	}

	private void addOrderLineQuantities(final OrderLine orderLine)
	{
		final OrderLineQuantityStatus olqStatus = new OrderLineQuantityStatus();
		olqStatus.setStatusCode("code");

		final OrderLineQuantity olq1 = new OrderLineQuantity();
		olq1.setOlqId("olqId1");
		olq1.setLocation("somewhere");
		olq1.setStatus(olqStatus);

		final OrderLineQuantity olq2 = new OrderLineQuantity();
		olq2.setOlqId("olqId2");
		olq2.setLocation("somewhere");
		olq2.setStatus(olqStatus);

		orderLine.setOrderLineQuantities(Lists.newArrayList(olq1, olq2));
	}

	private Shipment buildShipment()
	{
		final Shipment tempShipment = new Shipment();
		tempShipment.setShipmentId("999");
		tempShipment.setShippingMethod("DOM.EP");
		tempShipment.setOrderId(this.order.getOrderId());

		final Delivery delivery = new Delivery();
		delivery.setDeliveryId("d1");

		tempShipment.setDelivery(delivery);

		final ShippingAndHandling shippingAndHandling = new ShippingAndHandling();
		shippingAndHandling.setOrderId(tempShipment.getOrderId());
		shippingAndHandling.setShippingPrice(new Price(new Amount("USD", 2d), new Amount("USD", 0.5d), new Amount("USD", 0d)));
		tempShipment.setShippingAndHandling(shippingAndHandling);

		return tempShipment;
	}

	private void addPaymentInfos()
	{
		final PaymentInfo paymentInfo = new PaymentInfo();

		paymentInfo.setPaymentInfoType("Visa");
		paymentInfo.setAuthUrl("http://www.hybris.com");
		paymentInfo.setBillingAddress(new Address("2207 7th Avenue", null, "New York", "NY", "10027", 40.812356, -73.945857,
				COUNTRY_CODE_VALID, "U.S.A.", null, null));
		this.order.setPaymentInfos(Lists.newArrayList(paymentInfo));
	}

}
