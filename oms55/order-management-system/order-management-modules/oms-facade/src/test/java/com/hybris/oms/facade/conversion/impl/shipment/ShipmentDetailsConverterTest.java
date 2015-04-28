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
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.util.ShipmentTestUtils;
import com.hybris.oms.domain.shipping.ShipmentDetails;

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
public class ShipmentDetailsConverterTest
{
	private static final float WIDTH_VALUE = 1.4f;
	private static final float WEIGHT_VALUE = 1.3f;
	private static final String WEIGHT_UNIT_CODE = "weightUnitCode";
	private static final String SHIPPING_METHOD = "shippingMethod";
	private static final float LENGTH_VALUE = 1.2f;
	private static final double INSURANCE_VALUE_AMOUNT_VALUE = 2.2;
	private static final float HEIGHT_VALUE = 1.1f;
	private static final String HEIGHT_UNIT_CODE = "heightUnitCode";

	@Autowired
	private Converter<ShipmentData, ShipmentDetails> shipmentDetailsConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private ShipmentData shipmentData;

	@Before
	public void setUp()
	{
		// given
		this.shipmentData = this.persistenceManager.create(ShipmentData.class);

		this.shipmentData.setHeightUnitCode(HEIGHT_UNIT_CODE);
		this.shipmentData.setHeightValue(HEIGHT_VALUE);
		this.shipmentData.setInsuranceValueAmountValue(INSURANCE_VALUE_AMOUNT_VALUE);
		this.shipmentData.setLengthValue(LENGTH_VALUE);
		this.shipmentData.setShippingMethod(SHIPPING_METHOD);
		this.shipmentData.setGrossWeightUnitCode(WEIGHT_UNIT_CODE);
		this.shipmentData.setGrossWeightValue(WEIGHT_VALUE);
		this.shipmentData.setWidthValue(WIDTH_VALUE);
	}

	@Transactional
	@Test
	public void convertingShipmentDetailsData()
	{
		// when
		final ShipmentDetails shipmentDetails = this.shipmentDetailsConverter.convert(this.shipmentData);

		// then
		assertValid(shipmentDetails);
	}

	@Transactional
	@Test
	public void convertingShipmentDetailsDataWithFlush()
	{
		final OrderData order = ShipmentTestUtils.createOrder(persistenceManager);
		this.shipmentData.setOrderFk(order);

		this.persistenceManager.flush();

		// when
		final ShipmentDetails shipmentDetails = this.shipmentDetailsConverter.convert(this.shipmentData);

		// then
		assertValid(shipmentDetails);
	}

	private void assertValid(final ShipmentDetails shipmentDetails)
	{
		Assert.assertEquals(HEIGHT_UNIT_CODE, shipmentDetails.getHeightUnitCode());
		Assert.assertEquals(HEIGHT_VALUE, shipmentDetails.getHeightValue(), 0.001d);
		Assert.assertEquals(INSURANCE_VALUE_AMOUNT_VALUE, shipmentDetails.getInsuranceValueAmountValue(), 0.001d);
		Assert.assertEquals(LENGTH_VALUE, shipmentDetails.getLengthValue(), 0.001d);
		Assert.assertEquals(SHIPPING_METHOD, shipmentDetails.getShippingMethod());
		Assert.assertEquals(WEIGHT_UNIT_CODE, shipmentDetails.getWeightUnitCode());
		Assert.assertEquals(WEIGHT_VALUE, shipmentDetails.getWeightValue(), 0.001d);
		Assert.assertEquals(WIDTH_VALUE, shipmentDetails.getWidthValue(), 0.001d);
	}
}
