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
public class ShipmentDetailsReversePopulatorTest
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
	private ShipmentDetailsReversePopulator shipmentDetailsReversePopulator;

	@Autowired
	private PersistenceManager persistenceManager;

	private ShipmentDetails shipmentDetails;
	private ShipmentData shipmentData;

	@Before
	public void setUp()
	{
		// given
		this.shipmentData = this.persistenceManager.create(ShipmentData.class);

		this.shipmentDetails = new ShipmentDetails();
		this.shipmentDetails.setHeightUnitCode(HEIGHT_UNIT_CODE);
		this.shipmentDetails.setHeightValue(HEIGHT_VALUE);
		this.shipmentDetails.setInsuranceValueAmountValue(INSURANCE_VALUE_AMOUNT_VALUE);
		this.shipmentDetails.setLengthValue(LENGTH_VALUE);
		this.shipmentDetails.setShippingMethod(SHIPPING_METHOD);
		this.shipmentDetails.setWeightUnitCode(WEIGHT_UNIT_CODE);
		this.shipmentDetails.setWeightValue(WEIGHT_VALUE);
		this.shipmentDetails.setWidthValue(WIDTH_VALUE);
	}

	@Transactional
	@Test
	public void reverseConvertingShipmentDetailsDTO()
	{
		// when
		this.shipmentDetailsReversePopulator.populate(this.shipmentDetails, this.shipmentData);

		// then
		this.commonAssertions();
	}

	@Transactional
	@Test
	public void reverseConvertingShipmentDetailsDTOWithFlush()
	{
		final OrderData order = ShipmentTestUtils.createOrder(persistenceManager);
		this.shipmentData.setShippingMethod("dummyShippingMethod");
		this.shipmentData.setOrderFk(order);

		this.persistenceManager.flush();

		// when
		this.shipmentDetailsReversePopulator.populate(this.shipmentDetails, this.shipmentData);

		// then
		Assert.assertTrue(this.shipmentData.getId() != null);
		this.commonAssertions();
	}

	private void commonAssertions()
	{
		Assert.assertEquals(HEIGHT_UNIT_CODE, this.shipmentData.getHeightUnitCode());
		Assert.assertEquals(HEIGHT_VALUE, this.shipmentData.getHeightValue(), 0.001d);
		Assert.assertEquals(INSURANCE_VALUE_AMOUNT_VALUE, this.shipmentData.getInsuranceValueAmountValue(), 0.001d);
		Assert.assertEquals(LENGTH_VALUE, this.shipmentData.getLengthValue(), 0.001d);
		Assert.assertEquals(SHIPPING_METHOD, this.shipmentData.getShippingMethod());
		Assert.assertEquals(WEIGHT_UNIT_CODE, this.shipmentData.getGrossWeightUnitCode());
		Assert.assertEquals(WEIGHT_VALUE, this.shipmentData.getGrossWeightValue(), 0.001d);
		Assert.assertEquals(WIDTH_VALUE, this.shipmentData.getWidthValue(), 0.001d);
	}
}
