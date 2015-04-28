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
package com.hybris.oms.facade.conversion.impl.shipment;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.facade.conversion.common.PriceReverseConverter;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ShippingAndHandlingReverseConverterTest
{
	private static final String HYBRIS_ID = "dummyHybrisId";
	private static final String ORDER_ID = "orderId";
	private static final String FIRST_SHIPMENT_ID = "firstShipmentId";

	@Autowired
	private Converter<ShippingAndHandling, ShippingAndHandlingData> shippingAndHandlingReverseConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private PriceReverseConverter priceReverseConverter;

	@Transactional
	@Test
	public void reverseConvertingShippingAndHandlingDTO()
	{
		final Price price = new Price(new Amount("USD", 1.0), new Amount("USD", 2.1), new Amount("USD", 3.2));

		// given
		final ShippingAndHandling sah = new ShippingAndHandling();
		sah.setFirstShipmentId(FIRST_SHIPMENT_ID);
		sah.setOrderId(ORDER_ID);
		sah.setShippingPrice(price);

		// when
		final ShippingAndHandlingData sahData = this.shippingAndHandlingReverseConverter.convert(sah);

		// then
		Assert.assertTrue(sahData.getId() == null);
		Assert.assertEquals(FIRST_SHIPMENT_ID, sahData.getFirstShipmentId());
		Assert.assertEquals(ORDER_ID, sahData.getOrderId());
		Assert.assertEquals(this.priceReverseConverter.convert(price), sahData.getShippingPrice());
	}

	@Transactional
	@Test
	public void reverseConvertingShippingAndHandlingDTOWithFlush()
	{
		final Price price = new Price(new Amount("EUR", 1.0), new Amount("EUR", 2.1), new Amount("EUR", 3.2));

		// given
		final ShippingAndHandling sah = new ShippingAndHandling();
		sah.setFirstShipmentId(FIRST_SHIPMENT_ID);
		sah.setOrderId(ORDER_ID);
		sah.setShippingPrice(price);

		// when
		final ShippingAndHandlingData sahData = this.shippingAndHandlingReverseConverter.convert(sah);
		this.persistenceManager.flush();

		// then
		Assert.assertTrue(sahData.getId() != null);
		Assert.assertNotSame(HYBRIS_ID, sahData.getId());
		Assert.assertEquals(FIRST_SHIPMENT_ID, sahData.getFirstShipmentId());
		Assert.assertEquals(ORDER_ID, sahData.getOrderId());
		Assert.assertEquals(this.priceReverseConverter.convert(price), sahData.getShippingPrice());
	}
}
