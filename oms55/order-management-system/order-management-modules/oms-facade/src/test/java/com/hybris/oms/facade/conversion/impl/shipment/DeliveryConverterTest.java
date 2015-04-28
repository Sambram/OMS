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
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.shipping.Delivery;
import com.hybris.oms.service.managedobjects.shipment.DeliveryData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import java.util.Date;

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
public class DeliveryConverterTest
{
	private static final long DELIVERY_ID = 13L;
	private static final String DELIVERY_ID_STRING = "13";
	private static final String TRACKING_URL = "trackingURL";
	private static final String TRACKING_ID = "trackingID";
	private static final String LABEL_URL = "labelURL";

	@Autowired
	private Converter<DeliveryData, Delivery> deliveryConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private DeliveryData deliveryData;

	private Date testDate;
	private Address testAddress;

	@Before
	public void setUp()
	{
		// given
		this.deliveryData = this.persistenceManager.create(DeliveryData.class);
		this.testDate = new Date();
		this.testAddress = new Address("al1", "al2", "cN", "cS", "pZ", 1.23, 4.56, "cIso", "cName", null, null);
		final AddressVT testAddressVT = new AddressVT("al1", "al2", "cN", "cS", "pZ", 1.23, 4.56, "cIso", "cName", null, null);

		this.deliveryData.setActualDeliveryDate(this.testDate);
		this.deliveryData.setDeliveryAddress(testAddressVT);
		this.deliveryData.setDeliveryId(DELIVERY_ID);
		this.deliveryData.setLabelUrl(LABEL_URL);
		this.deliveryData.setTrackingID(TRACKING_ID);
		this.deliveryData.setTrackingUrl(TRACKING_URL);
	}

	@Transactional
	@Test
	public void testConvertingDeliveryData()
	{
		// when
		final Delivery delivery = this.deliveryConverter.convert(this.deliveryData);

		// then
		assertValid(delivery);
	}

	@Transactional
	@Test
	public void testConvertingDeliveryDataWithFlush()
	{
		this.persistenceManager.flush();

		// when
		final Delivery delivery = this.deliveryConverter.convert(this.deliveryData);

		// then
		assertValid(delivery);
	}

	private void assertValid(final Delivery delivery)
	{
		Assert.assertEquals(this.testDate, delivery.getActualDeliveryDate());
		Assert.assertEquals(this.testAddress, delivery.getDeliveryAddress());
		Assert.assertEquals(DELIVERY_ID_STRING, delivery.getDeliveryId());
		Assert.assertEquals(LABEL_URL, delivery.getLabelUrl());
		Assert.assertEquals(TRACKING_ID, delivery.getTrackingID());
		Assert.assertEquals(TRACKING_URL, delivery.getTrackingUrl());
	}
}
