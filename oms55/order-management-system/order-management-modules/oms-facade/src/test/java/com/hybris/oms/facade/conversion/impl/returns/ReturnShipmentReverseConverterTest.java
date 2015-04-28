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
import static org.junit.Assert.assertTrue;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.returns.ReturnShipment;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ReturnShipmentReverseConverterTest
{
	@Autowired
	private Converter<ReturnShipment, ReturnShipmentData> returnShipmentReverseConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void reverseConvertingNotFlushedOrderLineData()
	{
		// given
		final ReturnShipment returnShipment = ReturnTestUtils.createReturnShipmentDto();

		// when
		final ReturnShipmentData returnShipmentData = this.returnShipmentReverseConverter.convert(returnShipment);
		doAssertions(returnShipmentData);
	}

	@Transactional
	@Test
	public void reverseConvertingFlushedOrderLineData()
	{
		// given
		final ReturnShipment returnShipment = ReturnTestUtils.createReturnShipmentDto();
		persistenceManager.flush();

		// when
		final ReturnShipmentData returnShipmentData = this.returnShipmentReverseConverter.convert(returnShipment);
		doAssertions(returnShipmentData);
	}

	private void doAssertions(final ReturnShipmentData returnShipmentData)
	{
		assertEquals(ReturnTestUtils.UNIT, returnShipmentData.getGrossWeightUnitCode());
		assertEquals(Float.valueOf(5.0f), Float.valueOf(returnShipmentData.getGrossWeightValue()));
		assertEquals(ReturnTestUtils.UNIT, returnShipmentData.getHeightUnitCode());
		assertEquals(Float.valueOf(10.0f), Float.valueOf(returnShipmentData.getHeightValue()));
		assertEquals(ReturnTestUtils.UNIT, returnShipmentData.getLengthUnitCode());
		assertEquals(Float.valueOf(15.0f), Float.valueOf(returnShipmentData.getLengthValue()));
		assertEquals(ReturnTestUtils.UNIT, returnShipmentData.getWidthUnitCode());
		assertEquals(Float.valueOf(20.0f), Float.valueOf(returnShipmentData.getWidthValue()));
		assertEquals(ReturnTestUtils.UNIT, returnShipmentData.getInsuranceValueAmountCurrencyCode());
		assertEquals(Double.valueOf(9.99d), Double.valueOf(returnShipmentData.getInsuranceValueAmountValue()));
		assertEquals(ReturnTestUtils.LABEL_URL, returnShipmentData.getLabelUrl());
		assertEquals(ReturnTestUtils.NOTE, returnShipmentData.getNote());
		assertEquals(ReturnTestUtils.PACKAGE_DESCRIPTION, returnShipmentData.getPackageDescription());
		assertTrue(returnShipmentData.getReturnShipmentId() > 0);
		assertEquals(ReturnTestUtils.SHIPPING_METHOD, returnShipmentData.getShippingMethod());
		assertEquals(ReturnTestUtils.TRACKING_ID, returnShipmentData.getTrackingId());
		assertEquals(ReturnTestUtils.TRACKING_URL, returnShipmentData.getTrackingUrl());
	}
}
