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
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.impl.order.OrderLineQuantityIdValidator;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


@RunWith(MockitoJUnitRunner.class)
public class ShipmentOlqValidatorTest
{
	private static final String ONE = "1";
	private static final String TWO = "2";
	private static final String THREE = "3";

	@InjectMocks
	private final ShipmentOlqValidator shipmentOlqValidator = new ShipmentOlqValidator();

	@Mock
	private OrderLineQuantityIdValidator mockOrderLineQuantityIdValidator;

	private ValidationContext context;
	private Shipment shipment;
	private Set<String> olqIds;
	private ShipmentOlqDto shipmentOlqDto;

	@Before
	public void setUp()
	{
		context = new ValidationContext();
		olqIds = Sets.newHashSet(ONE, TWO, THREE);
		shipment = buildShipment();
		shipmentOlqDto = new ShipmentOlqDto(shipment, olqIds);
	}

	@Test
	public void shouldValidate()
	{
		this.shipmentOlqValidator.validate("ShipmentOlqs", this.context, shipmentOlqDto);
		Assert.assertFalse(this.context.containsFailures());
	}

	@Test
	public void shouldFailValidation_OlqNotInShipment()
	{
		shipment.setOlqIds(Lists.newArrayList(ONE, TWO));
		this.shipmentOlqValidator.validate("ShipmentOlqs", this.context, shipmentOlqDto);
		Assert.assertEquals(1, context.getFailureCount());
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
		tempShipment.setOlqIds(Lists.newArrayList(olqIds));

		return tempShipment;
	}
}
