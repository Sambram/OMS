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

import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

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
import org.springframework.util.CollectionUtils;


@RunWith(MockitoJUnitRunner.class)
public class OrderLineQuantityValidatorTest
{
	private ValidationContext context;
	private OrderLineQuantity orderLineQuantity;

	@InjectMocks
	private OrderLineQuantityValidator orderLineQuantityValidator;

	@Mock
	private Validator<OrderLineQuantityStatus> orderLineQuantityStatusValidator;

	@Mock
	private Validator<Shipment> shipmentValidator;

	@Mock
	private InventoryService inventoryService;

	@Before
	public void setUp()
	{
		context = new ValidationContext();
		buildOrderLineQuantity();
	}

	@Test
	public void shouldFailWhenOlqIdIsNull()
	{
		// Given
		this.orderLineQuantity.setOlqId(null);
		final List<StockroomLocationData> locationDatas = Mockito.mock(List.class);
		BDDMockito.given(locationDatas.size()).willReturn(1);
		BDDMockito.given(inventoryService.findLocationsByLocationIds(Mockito.anySet())).willReturn(locationDatas);

		// When
		this.orderLineQuantityValidator.validate("OrderLineQuantity", context, this.orderLineQuantity);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.NOT_BLANK, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("OrderLineQuantity.olqId", this.context.getFailures().get(0).getFieldName());
	}

	@Test
	public void shouldFailWhenLocationIsNull()
	{
		// Given
		this.orderLineQuantity.setLocation(null);

		// When
		this.orderLineQuantityValidator.validate("OrderLineQuantity", context, this.orderLineQuantity);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.INVALID, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("OrderLineQuantity.location", this.context.getFailures().get(0).getFieldName());
	}

	@Test
	public void shouldFailWhenLocationDoesNotExistInOMS()
	{
		// Given
		orderLineQuantity.setLocation("INVALID");
		final List<StockroomLocationData> locationDatas = Mockito.mock(List.class);
		BDDMockito.given(CollectionUtils.isEmpty(locationDatas)).willReturn(true);
		BDDMockito.given(inventoryService.findLocationsByLocationIds(Mockito.anySet())).willReturn(locationDatas);

		// When
		this.orderLineQuantityValidator.validateInternal(this.context, this.orderLineQuantity);

		// Then
		Assert.assertEquals(1, this.context.getFailureCount());
		Assert.assertEquals(FieldValidationType.INVALID, this.context.getFailures().get(0).getFailureType());
		Assert.assertEquals("OrderLineQuantity.location", this.context.getFailures().get(0).getFieldName());
		BDDMockito.verify(inventoryService, Mockito.times(1)).findLocationsByLocationIds(Mockito.anySet());
	}

	@Test
	public void shouldCallOrderLineQuantityStatusValidator()
	{
		this.orderLineQuantityValidator.validate("OrderLineQuantity", context, this.orderLineQuantity);

		BDDMockito.verify(orderLineQuantityStatusValidator, Mockito.times(1)).validate("OrderLineQuantity.orderLineQuantityStatus",
				context, this.orderLineQuantity.getStatus());
	}

	@Test
	public void shouldCallShipmentValidatorWhenShipmentExists()
	{
		// Given
		this.orderLineQuantity.setShipment(new Shipment());

		// When
		this.orderLineQuantityValidator.validate("OrderLineQuantity", context, this.orderLineQuantity);

		// Then
		BDDMockito.verify(shipmentValidator, Mockito.times(1)).validate("OrderLineQuantity.shipment", context,
				this.orderLineQuantity.getShipment());
	}

	@Test
	public void shouldNotCallShipmentValidatorWhenShipmentIsNull()
	{
		// Given
		this.orderLineQuantity.setShipment(null);

		// When
		this.orderLineQuantityValidator.validate("OrderLineQuantity", context, this.orderLineQuantity);

		// Then
		BDDMockito.verify(shipmentValidator, Mockito.times(0)).validate("OrderLineQuantity.shipment", context,
				this.orderLineQuantity.getShipment());
	}

	private void buildOrderLineQuantity()
	{
		this.orderLineQuantity = new OrderLineQuantity();
		this.orderLineQuantity.setOlqId("555");
		this.orderLineQuantity.setLocation("somewhere");

		final OrderLineQuantityStatus olqStatus = new OrderLineQuantityStatus();
		olqStatus.setStatusCode("code");

		this.orderLineQuantity.setStatus(olqStatus);
	}

}
