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
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.PriceVT;

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
public class ShippingAndHandlingConverterTest
{
	private static final double TAX_COMMITED_VALUE = 3.2;
	private static final double TAX_VALUE = 2.1;
	private static final double SUBTOTAL_VALUE = 1.0;
	private static final String CURRENCY_CODE = "EUR";
	private static final String ORDER_ID = "orderId";
	private static final String FIRST_SHIPMENT_ID = "firstShipmentId";

	@Autowired
	private Converter<ShippingAndHandlingData, ShippingAndHandling> shippingAndHandlingConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private ShippingAndHandlingData sahData;

	private Price price;

	@Before
	public void setUp()
	{
		this.sahData = this.persistenceManager.create(ShippingAndHandlingData.class);

		this.price = new Price(new Amount(CURRENCY_CODE, SUBTOTAL_VALUE), new Amount(CURRENCY_CODE, TAX_VALUE), new Amount(
				CURRENCY_CODE, TAX_COMMITED_VALUE));
		final PriceVT priceVT = new PriceVT(CURRENCY_CODE, SUBTOTAL_VALUE, CURRENCY_CODE, TAX_VALUE, CURRENCY_CODE,
				TAX_COMMITED_VALUE);

		this.sahData.setFirstShipmentId(FIRST_SHIPMENT_ID);
		this.sahData.setOrderId(ORDER_ID);
		this.sahData.setShippingPrice(priceVT);
	}

	@Transactional
	@Test
	public void convertingShippingAndHandlingData()
	{
		// when
		final ShippingAndHandling sah = this.shippingAndHandlingConverter.convert(this.sahData);

		// then
		assertValid(sah);
	}

	@Transactional
	@Test
	public void convertingShippingAndHandlingDataWithFlush()
	{
		this.persistenceManager.flush();

		// when
		final ShippingAndHandling sah = this.shippingAndHandlingConverter.convert(this.sahData);

		// then
		assertValid(sah);
	}

	private void assertValid(final ShippingAndHandling sah)
	{
		Assert.assertEquals(ORDER_ID, sah.getOrderId());
		Assert.assertEquals(FIRST_SHIPMENT_ID, sah.getFirstShipmentId());
		Assert.assertEquals(this.price, sah.getShippingPrice());
	}
}
