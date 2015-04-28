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
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Measure;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ShipmentReverseConverterTest
{
	private static final String CURRENCY_CODE = "code";
	private static final String ID = "1";
	private static final String SHIPPING_METHOD = "method";
	private static final String STATUS = "status";
	private static final String TAX_CATEGORY = "cat";
	private static final String UNIT_CODE = "unit";
	private static final Double AMOUNT_VALUE = 5D;
	private static final float MEASURE_VALUE = 5f;
	private static final int QUANTITY_VALUE = 5;

	private Amount amount;
	private Measure measure;
	private AddressVT address;

	@Autowired
	private Converter<Shipment, ShipmentData> shipmentReverseConverter;

	@Before
	public void setUp()
	{
		this.amount = new Amount(CURRENCY_CODE, AMOUNT_VALUE);
		this.measure = new Measure(UNIT_CODE, MEASURE_VALUE);
		this.address = AddressBuilder.anAddress().buildAddressVT();
	}

	@Transactional
	@Test
	public void reverseConvertingNotFlushedShipmentData()
	{
		// given
		final Shipment shipment = createShipment();

		// when
		final ShipmentData shipmentData = this.shipmentReverseConverter.convert(shipment);

		// then
		assertAmounts(shipmentData);

		assertMeasures(shipmentData);

		Assert.assertTrue(shipmentData.getId() == null);

		Assert.assertEquals(ID, shipmentData.getStockroomLocationId());

		Assert.assertEquals(CURRENCY_CODE, shipmentData.getCurrencyCode());

		Assert.assertEquals(STATUS, shipmentData.getOlqsStatus());

		Assert.assertEquals(false, shipmentData.isPickupInStore());

		Assert.assertEquals(this.address, shipmentData.getShipFrom());

		Assert.assertEquals(1L, shipmentData.getShipmentId());

		Assert.assertEquals(SHIPPING_METHOD, shipmentData.getShippingMethod());

		Assert.assertEquals(TAX_CATEGORY, shipmentData.getTaxCategory());

		Assert.assertNotNull(shipmentData.getAuthUrls());
		Assert.assertEquals(1, shipmentData.getAuthUrls().size());

		Assert.assertNull(shipmentData.getShippingAndHandling());

		Assert.assertNotNull(shipmentData.getMerchandisePrice());
	}

	private void assertAmounts(final ShipmentData shipmentData)
	{
		Assert.assertEquals(CURRENCY_CODE, shipmentData.getAmountCapturedCurrencyCode());
		Assert.assertEquals(AMOUNT_VALUE, shipmentData.getAmountCapturedValue(), 0.001d);

		Assert.assertEquals(CURRENCY_CODE, shipmentData.getInsuranceValueAmountCurrencyCode());
		Assert.assertEquals(AMOUNT_VALUE, shipmentData.getInsuranceValueAmountValue(), 0.001d);

		Assert.assertEquals(UNIT_CODE, shipmentData.getTotalGoodsItemQuantityUnitCode());
		Assert.assertEquals(QUANTITY_VALUE, shipmentData.getTotalGoodsItemQuantityValue());
	}

	private void assertMeasures(final ShipmentData shipmentData)
	{
		Assert.assertEquals(UNIT_CODE, shipmentData.getGrossVolumeUnitCode());
		Assert.assertEquals(MEASURE_VALUE, shipmentData.getGrossVolumeValue(), 0.001d);

		Assert.assertEquals(UNIT_CODE, shipmentData.getGrossWeightUnitCode());
		Assert.assertEquals(MEASURE_VALUE, shipmentData.getGrossWeightValue(), 0.001d);

		Assert.assertEquals(UNIT_CODE, shipmentData.getHeightUnitCode());
		Assert.assertEquals(MEASURE_VALUE, shipmentData.getHeightValue(), 0.001d);

		Assert.assertEquals(UNIT_CODE, shipmentData.getLengthUnitCode());
		Assert.assertEquals(MEASURE_VALUE, shipmentData.getLengthValue(), 0.001d);

		Assert.assertEquals(UNIT_CODE, shipmentData.getWidthUnitCode());
		Assert.assertEquals(MEASURE_VALUE, shipmentData.getWidthValue(), 0.001d);

		Assert.assertEquals(UNIT_CODE, shipmentData.getNetWeightUnitCode());
		Assert.assertEquals(MEASURE_VALUE, shipmentData.getNetWeightValue(), 0.001d);
	}

	private Shipment createShipment()
	{
		final Shipment shipment = new Shipment();
		shipment.setAmountCaptured(this.amount);
		shipment.setAuthUrls(ImmutableList.of(""));
		shipment.setCurrencyCode(CURRENCY_CODE);
		shipment.setDelivery(null);
		shipment.setGrossVolume(this.measure);
		shipment.setGrossWeight(this.measure);
		shipment.setHeight(this.measure);
		shipment.setInsuranceValueAmount(this.amount);
		shipment.setLength(this.measure);
		shipment.setLocation(ID);
		shipment.setMerchandisePrice(new Price(new Amount("CAD", 10d), new Amount("CAD", 10d), new Amount("CAD", 10d)));
		shipment.setNetWeight(this.measure);
		shipment.setOlqsStatus(STATUS);
		shipment.setOrderId(ID);
		shipment.setPickupInStore(false);
		shipment.setShipFrom(AddressBuilder.anAddress().buildAddressDTO());
		shipment.setShipmentId(ID);
		shipment.setShippingAndHandling(new ShippingAndHandling());
		shipment.getShippingAndHandling().setShippingPrice(
				new Price(new Amount("CAD", 10d), new Amount("CAD", 10d), new Amount("CAD", 10d)));
		shipment.getShippingAndHandling().getShippingPrice().setSubTotal(new Amount());
		shipment.getShippingAndHandling().getShippingPrice().getSubTotal().setValue(AMOUNT_VALUE);
		shipment.getShippingAndHandling().getShippingPrice().setTax(new Amount());
		shipment.getShippingAndHandling().getShippingPrice().getTax().setValue(AMOUNT_VALUE);
		shipment.setShippingMethod(SHIPPING_METHOD);
		shipment.setTaxCategory(TAX_CATEGORY);
		shipment.setTotalGoodsItemQuantity(new Quantity(UNIT_CODE, QUANTITY_VALUE));
		shipment.setWidth(this.measure);

		return shipment;
	}
}
