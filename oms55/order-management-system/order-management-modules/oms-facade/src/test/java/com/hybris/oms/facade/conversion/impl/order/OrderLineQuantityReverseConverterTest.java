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
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OrderLineQuantityReverseConverterTest
{
	private static final int NUMBER_INT = 1;
	private static final String CODE = "code";
	private static final String OLQ_ID = "1";
	private static final String SHIPMENT_ID = "1";
	private static final String LOCATION_ID = "LOC";
	private static final String STATUS = "ON_HAND";

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private Converter<OrderLineQuantity, OrderLineQuantityData> orderLineQuantityReverseConverter;

	@Transactional
	@Test
	public void reverseConvertingNotFlushedOrderLineQuantityData()
	{
		// given
		final OrderLineQuantityStatusData statusData = this.persistenceManager.create(OrderLineQuantityStatusData.class);
		statusData.setStatusCode(STATUS);
		this.persistenceManager.flush();

		final OrderLineQuantityStatus status = new OrderLineQuantityStatus();
		status.setStatusCode(STATUS);

		final Shipment shipment = new Shipment();
		shipment.setShipmentId(SHIPMENT_ID);

		final OrderLineQuantity orderLineQuantity = new OrderLineQuantity();
		orderLineQuantity.setLocation(LOCATION_ID);
		orderLineQuantity.setOlqId(OLQ_ID);
		orderLineQuantity.setQuantity(new Quantity(CODE, NUMBER_INT));
		orderLineQuantity.setShipment(shipment);
		orderLineQuantity.setStatus(status);

		// when
		final OrderLineQuantityData orderLineQuantityData = this.orderLineQuantityReverseConverter.convert(orderLineQuantity);

		// then
		Assert.assertTrue(orderLineQuantityData.getId() == null);
		Assert.assertEquals(LOCATION_ID, orderLineQuantityData.getStockroomLocationId());
		Assert.assertEquals(1, orderLineQuantityData.getOlqId());
		Assert.assertEquals(CODE, orderLineQuantityData.getQuantityUnitCode());
		Assert.assertEquals(NUMBER_INT, orderLineQuantityData.getQuantityValue());
		Assert.assertNotNull(orderLineQuantityData.getShipment());
		Assert.assertNotNull(orderLineQuantityData.getStatus());
	}
}
