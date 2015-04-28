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

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.shipping.Delivery;
import com.hybris.oms.domain.types.Quantity;
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
public class DeliveryReverseConverterTest
{
	private static final String DUMMY_HYBRIS_ID = "hybrisId";
	private static final String TRACKING_URL = "trackingUrl";
	private static final String TRACKING_ID = "trackingID";
	private static final String LABEL_URL = "labelUrl";
	private static final String DELIVERY_ID_STRING = "12";
	private static final long DELIVERY_ID = 12L;

	@Autowired
	private Converter<Delivery, DeliveryData> deliveryReverseConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private Delivery delivery;
	private AddressVT testAddressVT;
	private Date testDate;

	@Before
	public void setUp()
	{
		// given
		this.delivery = new Delivery();
		this.testDate = new Date();
		final Date date = new Date(this.testDate.getTime() + 100000);

		this.testAddressVT = new AddressVT("al1", "al2", "cN", "cS", "pZ", 1.23, 4.56, "cIso", "cName", null, null);
		final Address testAddress = new Address("al1", "al2", "cN", "cS", "pZ", 1.23, 4.56, "cIso", "cName", null, null);
		final Quantity quantity = new Quantity("unitCode", 3);

		this.delivery.setActualDeliveryDate(this.testDate);
		this.delivery.setDeliveryAddress(testAddress);
		this.delivery.setDeliveryId(DELIVERY_ID_STRING);
		this.delivery.setDeliveryLocationId("deliveryLocationId");
		this.delivery.setLabelUrl(LABEL_URL);
		this.delivery.setLatestDeliveryDate(date);
		this.delivery.setQuantity(quantity);
		this.delivery.setQuantityUnitCode("quantityUnitCode");
		this.delivery.setQuantityValue(2);
		this.delivery.setTrackingID(TRACKING_ID);
		this.delivery.setTrackingUrl(TRACKING_URL);
	}

	@Transactional
	@Test(expected = ConversionException.class)
	public void shouldThrowExceptionWhenDeliveryIdNotLong()
	{
		this.delivery.setDeliveryId("NotNumber");
		this.deliveryReverseConverter.convert(this.delivery);
	}

	@Transactional
	@Test
	public void reverseConvertingDeliveryDTO()
	{
		// when
		final DeliveryData deliveryData = this.deliveryReverseConverter.convert(this.delivery);

		// then
		Assert.assertTrue(deliveryData.getId() == null);
		assertValid(deliveryData);
	}

	@Transactional
	@Test
	public void reverseConvertingDeliveryDTOWithFlush()
	{
		// when
		final DeliveryData deliveryData = this.deliveryReverseConverter.convert(this.delivery);
		this.persistenceManager.flush();

		// then
		Assert.assertTrue(deliveryData.getId() != null);
		Assert.assertNotSame(DUMMY_HYBRIS_ID, deliveryData.getId());
		assertValid(deliveryData);
	}

	private void assertValid(final DeliveryData deliveryData)
	{
		Assert.assertEquals(DELIVERY_ID, deliveryData.getDeliveryId());
		Assert.assertEquals(LABEL_URL, deliveryData.getLabelUrl());
		Assert.assertEquals(TRACKING_ID, deliveryData.getTrackingID());
		Assert.assertEquals(TRACKING_URL, deliveryData.getTrackingUrl());
		Assert.assertEquals(this.testDate, deliveryData.getActualDeliveryDate());
		Assert.assertEquals(this.testAddressVT, deliveryData.getDeliveryAddress());
	}

}
