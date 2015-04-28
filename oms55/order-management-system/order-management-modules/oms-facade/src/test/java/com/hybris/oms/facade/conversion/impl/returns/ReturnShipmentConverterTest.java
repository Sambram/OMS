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
package com.hybris.oms.facade.conversion.impl.returns;

import static org.junit.Assert.assertEquals;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.returns.ReturnShipment;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Measure;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ReturnShipmentConverterTest
{

	@Autowired
	private Converter<ReturnShipmentData, ReturnShipment> returnShipmentConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private ReturnTestUtils returnTestUtils;

	private ReturnShipmentData returnShipmentData;

	@Before
	public void setUp()
	{
		// given
		this.returnTestUtils = new ReturnTestUtils();
		this.returnTestUtils.createObjects(this.persistenceManager);
		this.returnShipmentData = returnTestUtils.getReturnShipmentData();
	}

	@Transactional
	@Test
	public void shouldConvertSuccess()
	{
		persistenceManager.flush();

		// when
		final ReturnShipment returnShipment = this.returnShipmentConverter.convert(this.returnShipmentData);

		// then
		assertEquals(new Measure(ReturnTestUtils.UNIT, 5.0f), returnShipment.getGrossWeight());
		assertEquals(new Measure(ReturnTestUtils.UNIT, 10.0f), returnShipment.getHeight());
		assertEquals(new Measure(ReturnTestUtils.UNIT, 15.0f), returnShipment.getLength());
		assertEquals(new Measure(ReturnTestUtils.UNIT, 20.0f), returnShipment.getWidth());
		assertEquals(new Amount(ReturnTestUtils.UNIT, 9.99d), returnShipment.getInsuranceValueAmount());
		assertEquals(ReturnTestUtils.LABEL_URL, returnShipment.getLabelUrl());
		assertEquals(ReturnTestUtils.NOTE, returnShipment.getNote());
		assertEquals(ReturnTestUtils.PACKAGE_DESCRIPTION, returnShipment.getPackageDescription());
		assertEquals(Long.valueOf(ReturnTestUtils.RETURN_SHIPMENT_ID), Long.valueOf(returnShipment.getReturnShipmentId()));
		assertEquals(ReturnTestUtils.SHIPPING_METHOD, returnShipment.getShippingMethod());
		assertEquals(ReturnTestUtils.TRACKING_ID, returnShipment.getTrackingId());
		assertEquals(ReturnTestUtils.TRACKING_URL, returnShipment.getTrackingUrl());
	}

}
