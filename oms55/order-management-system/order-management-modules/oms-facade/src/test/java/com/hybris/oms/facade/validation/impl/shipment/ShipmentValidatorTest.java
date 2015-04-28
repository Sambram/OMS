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
package com.hybris.oms.facade.validation.impl.shipment;

import com.hybris.oms.domain.shipping.Delivery;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ShipmentValidatorTest
{
	private ValidationContext context;
	private Shipment shipment;

	@InjectMocks
	private ShipmentValidator shipmentValidator;

	@Before
	public void setUp()
	{
		context = new ValidationContext();
		shipment = buildShipment();
	}

	@Test
	public void shouldFailOnDeliveryValidationWithNullDeliveryId()
	{
		// Given
		shipment.getDelivery().setDeliveryId(null);

		// When
		this.shipmentValidator.validate("Shipment", context, shipment);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Shipment.delivery.deliveryId", this.context.getFailures().get(0).getFieldName());
	}

	@Test
	public void shouldFailOnShippingAndHandlingValidationWithNullShippingAndHandling()
	{
		// Given
		shipment.setShippingAndHandling(null);

		// When
		this.shipmentValidator.validate("Shipment", context, shipment);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_NULL, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Shipment.shippingAndHandling", this.context.getFailures().get(0).getFieldName());
	}

	@Test
	public void shouldFailWhenShipmentIdIsNull()
	{
		// Given
		shipment.setShipmentId(null);

		// When
		this.shipmentValidator.validate("Shipment", context, shipment);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Shipment.shipmentId", this.context.getFailures().get(0).getFieldName());
	}

	@Test
	public void shouldFailWhenShipmentMethodIsNull()
	{
		// Given
		shipment.setShippingMethod(null);

		// When
		this.shipmentValidator.validate("Shipment", context, shipment);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Shipment.shippingMethod", this.context.getFailures().get(0).getFieldName());
	}

	@Test
	public void shouldFailWhenOrderIdIsNull()
	{
		// Given
		shipment.setOrderId(null);

		// When
		this.shipmentValidator.validate("Shipment", context, shipment);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("Shipment.orderId", this.context.getFailures().get(0).getFieldName());
	}

	/**
	 * Build a valid shipment.
	 * 
	 * @return shipment
	 */
	private Shipment buildShipment()
	{
		final Shipment tempShipment = new Shipment();
		tempShipment.setShipmentId("999");
		tempShipment.setShippingMethod("DOM.EP");
		tempShipment.setOrderId("888");

		final Delivery delivery = new Delivery();
		delivery.setDeliveryId("d1");

		tempShipment.setDelivery(delivery);

		final ShippingAndHandling shippingAndHandling = new ShippingAndHandling();
		shippingAndHandling.setOrderId(tempShipment.getOrderId());
		shippingAndHandling.setShippingPrice(new Price(new Amount("USD", 2d), new Amount("USD", 0.5d), new Amount("USD", 0d)));
		tempShipment.setShippingAndHandling(shippingAndHandling);

		return tempShipment;
	}
}
