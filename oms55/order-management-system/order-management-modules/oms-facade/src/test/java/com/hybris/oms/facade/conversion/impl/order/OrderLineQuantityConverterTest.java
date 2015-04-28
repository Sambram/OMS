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

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.util.ShipmentTestUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OrderLineQuantityConverterTest
{

	@Autowired
	private Converter<OrderLineQuantityData, OrderLineQuantity> orderLineQuantityConverter;

	@Autowired
	private PersistenceManager persistenceManager;


	private OrderLineQuantityData orderLineQuantityData;

	@Before
	public void setUp()
	{
		// given
		this.orderLineQuantityData = this.prepareData();
	}

	@Transactional
	@Test
	public void convertingNotFlushedOrderLineQuantityData()
	{
		// when
		final OrderLineQuantity orderLineQuantity = this.orderLineQuantityConverter.convert(this.orderLineQuantityData);

		// then
		assertValid(orderLineQuantity);
	}

	@Transactional
	@Test
	public void convertingFlushedOrderLineQuantityData()
	{
		this.persistenceManager.flush();

		// when
		final OrderLineQuantity orderLineQuantity = this.orderLineQuantityConverter.convert(this.orderLineQuantityData);

		// then
		assertValid(orderLineQuantity);
	}

	private OrderLineQuantityData prepareData()
	{
		final OrderLineQuantityStatusData orderLineQuantityStatusData = this.persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData.setActive(Boolean.TRUE);
		orderLineQuantityStatusData.setDescription("description");
		orderLineQuantityStatusData.setStatusCode("statusCode");

		final OrderLineQuantityData resultData = this.persistenceManager.create(OrderLineQuantityData.class);
		resultData.setStockroomLocationId("location");
		resultData.setOlqId(1);
		resultData.setQuantityUnitCode("unitCode");
		resultData.setQuantityValue(1);
		final ShipmentData shipmentData =ShipmentTestUtils.createShipmentData(persistenceManager);
		resultData.setShipment(shipmentData);
		resultData.setStatus(orderLineQuantityStatusData);
		resultData.setOrderLine(shipmentData.getOrderFk().getOrderLines().get(0));

		return resultData;
	}

	private void assertValid(final OrderLineQuantity orderLineQuantity)
	{
		Assert.assertEquals("location", orderLineQuantity.getLocation());
		Assert.assertEquals("1", orderLineQuantity.getOlqId());
		Assert.assertEquals("unitCode", orderLineQuantity.getQuantity().getUnitCode());
		Assert.assertEquals(1, orderLineQuantity.getQuantity().getValue());
		Assert.assertNotNull(orderLineQuantity.getShipment());
		Assert.assertNotNull(orderLineQuantity.getStatus());
	}

}
