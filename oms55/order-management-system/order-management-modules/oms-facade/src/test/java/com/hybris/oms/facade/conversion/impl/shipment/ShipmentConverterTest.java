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
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.facade.conversion.common.PriceReverseConverter;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.util.ShipmentTestUtils;

import java.util.Arrays;

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
public class ShipmentConverterTest
{

	private static final String SAH_FIRST_SHIPMENT_ID = "SAH_FirstShipmentId";
	private static final String ORDER_ID = "dummyOrderId";
	private static final String DELIVERY_ID_STRING = "422";
	private static final int TOTAL_GOODS_ITEM_QUANTITY_VALUE = 17;
	private static final float WIDTH_VALUE = 1.8f;
	private static final String SHIPMENT_ID_STRING = "19";
	private static final float NET_WEIGHT_VALUE = 1.6f;
	private static final float LENGTH_VALUE = 1.5f;
	private static final double INSURANCE_VALUE_AMOUNT_VALUE = 1.4;
	private static final float HEIGHT_VALUE = 1.3f;
	private static final float GROSS_WEIGHT_VALUE = 1.2f;
	private static final float GROSS_VOLUME_VALUE = 1.1f;
	private static final double AMOUNT_CAPTURED_VALUE = 1.0;
	private static final String WIDTH_UNIT_CODE = "WidthUnitCode";
	private static final String TOTAL_GOODS_ITEM_QUANTITY_UNIT_CODE = "TotalGoodsItemQuantityUnitCode";
	private static final String TAX_CATEGORY = "TaxCategory";
	private static final String SHIPPING_METHOD = "ShippingMethod";
	private static final String PRIORITY_LEVEL_CODE = "PriorityLevelCode";
	private static final String OLQS_STATUS = "OlqsStatus";
	private static final String NET_WEIGHT_UNIT_CODE = "NetWeightUnitCode";
	private static final String LOCATION_ID = "LocationId";
	private static final String LENGTH_UNIT_CODE = "LengthUnitCode";
	private static final String LAST_EXIT_LOCATION_ID = "LastExitLocationId";
	private static final String INSURANCE_VALUE_AMOUNT_CURRENCY_CODE = "InsuranceValueAmountCurrencyCode";
	private static final String HEIGHT_UNIT_CODE = "HeightUnitCode";
	private static final String GROSS_WEIGHT_UNIT_CODE = "GrossWeightUnitCode";
	private static final String GROSS_VOLUME_UNIT_CODE = "GrossVolumeUnitCode";
	private static final String FIRST_ARRIVAL_LOCATION_ID = "FirstArrivalLocationId";
	private static final String CURRENCY_CODE = "CurrencyCode";
	private static final String AMOUNT_CAPTURED_CURRENCY_CODE = "AmountCapturedCurrencyCode";
	private static final String STOCKROOM_LOCATION_ID = "location";
	private static final String OLQSTATUS_DESCRIPTION = "description";
	private static final String OLQSTATUS_CODE = "statusCode";
	private static final String QUANTITY_UNIT_CODE = "QuantityUnitCode";
	private static final int QUANTITY_UNIT_VALUE = 1;

	@Autowired
	private Converter<ShipmentData, Shipment> shipmentConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private Converter<AddressVT, Address> addressConverter;

	@Autowired
	private PriceReverseConverter priceReverseConverter;

	private ShipmentData shipmentData;

	@Before
	public void setUp()
	{
		// given
		this.shipmentData = ShipmentTestUtils.createShipmentData(persistenceManager);
	}

	@Transactional
	@Test
	public void testConvertingShipmentData()
	{
		// when
		final Shipment shipment = this.shipmentConverter.convert(this.shipmentData);

		// then
		assertValid(shipment);
	}

	@Transactional
	@Test
	public void testConvertingShipmentDataWithFlush()
	{
		this.persistenceManager.flush();

		// when
		final Shipment shipment = this.shipmentConverter.convert(this.shipmentData);

		// then
		assertValid(shipment);
	}

	private void assertValid(final Shipment shipment)
	{
		Assert.assertEquals(AMOUNT_CAPTURED_CURRENCY_CODE, shipment.getAmountCaptured().getCurrencyCode());
		Assert.assertEquals(AMOUNT_CAPTURED_VALUE, shipment.getAmountCaptured().getValue(), 0.001d);
		Assert.assertEquals(this.shipmentData.getAuthUrls(), shipment.getAuthUrls());
		Assert.assertEquals(CURRENCY_CODE, shipment.getCurrencyCode());
		Assert.assertEquals(DELIVERY_ID_STRING, shipment.getDelivery().getDeliveryId());
		Assert.assertEquals(this.addressConverter.convert(this.shipmentData.getDelivery().getDeliveryAddress()), shipment
				.getDelivery().getDeliveryAddress());
		Assert.assertEquals(FIRST_ARRIVAL_LOCATION_ID, shipment.getFirstArrivalLocationId());
		Assert.assertEquals(GROSS_VOLUME_UNIT_CODE, shipment.getGrossVolume().getUnitCode());
		Assert.assertEquals(GROSS_VOLUME_VALUE, shipment.getGrossVolume().getValue(), 0.001d);
		Assert.assertEquals(GROSS_WEIGHT_UNIT_CODE, shipment.getGrossWeight().getUnitCode());
		Assert.assertEquals(GROSS_WEIGHT_VALUE, shipment.getGrossWeight().getValue(), 0.001d);
		Assert.assertEquals(HEIGHT_UNIT_CODE, shipment.getHeight().getUnitCode());
		Assert.assertEquals(HEIGHT_VALUE, shipment.getHeight().getValue(), 0.001d);
		Assert.assertEquals(INSURANCE_VALUE_AMOUNT_CURRENCY_CODE, shipment.getInsuranceValueAmount().getCurrencyCode());
		Assert.assertEquals(INSURANCE_VALUE_AMOUNT_VALUE, shipment.getInsuranceValueAmount().getValue(), 0.001d);
		Assert.assertEquals(LAST_EXIT_LOCATION_ID, shipment.getLastExitLocationId());
		Assert.assertEquals(LENGTH_UNIT_CODE, shipment.getLength().getUnitCode());
		Assert.assertEquals(LENGTH_VALUE, shipment.getLength().getValue(), 0.001d);
		Assert.assertEquals(LOCATION_ID, shipment.getLocation());
		Assert.assertEquals(this.shipmentData.getMerchandisePrice(),
				this.priceReverseConverter.convert(shipment.getMerchandisePrice()));
		Assert.assertEquals(NET_WEIGHT_UNIT_CODE, shipment.getNetWeight().getUnitCode());
		Assert.assertEquals(NET_WEIGHT_VALUE, shipment.getNetWeight().getValue(), 0.001d);
		Assert.assertEquals(OLQS_STATUS, shipment.getOlqsStatus());
		Assert.assertEquals(ORDER_ID, shipment.getOrderId());
		Assert.assertEquals(PRIORITY_LEVEL_CODE, shipment.getPriorityLevelCode());
		Assert.assertEquals(this.addressConverter.convert(this.shipmentData.getShipFrom()), shipment.getShipFrom());
		Assert.assertEquals(SHIPMENT_ID_STRING, shipment.getShipmentId());
		Assert.assertEquals(SAH_FIRST_SHIPMENT_ID, shipment.getShippingAndHandling().getFirstShipmentId());
		Assert.assertEquals(this.shipmentData.getShippingAndHandling().getShippingPrice(),
				this.priceReverseConverter.convert(shipment.getShippingAndHandling().getShippingPrice()));
		Assert.assertEquals(SHIPPING_METHOD, shipment.getShippingMethod());
		Assert.assertEquals(TAX_CATEGORY, shipment.getTaxCategory());
		Assert.assertEquals(TOTAL_GOODS_ITEM_QUANTITY_UNIT_CODE, shipment.getTotalGoodsItemQuantity().getUnitCode());
		Assert.assertEquals(TOTAL_GOODS_ITEM_QUANTITY_VALUE, shipment.getTotalGoodsItemQuantity().getValue());
		Assert.assertEquals(WIDTH_UNIT_CODE, shipment.getWidth().getUnitCode());
		Assert.assertEquals(WIDTH_VALUE, shipment.getWidth().getValue(), 0.001d);
	}

	@Transactional
	@Test
	public void testConvertingShipmentDataWithOlqIds()
	{
		// given
		final OrderLineQuantityStatusData orderLineQuantityStatusData = this.persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData.setActive(Boolean.TRUE);
		orderLineQuantityStatusData.setDescription(OLQSTATUS_DESCRIPTION);
		orderLineQuantityStatusData.setStatusCode(OLQSTATUS_CODE);
		final OrderLineQuantityData olqData = this.persistenceManager.create(OrderLineQuantityData.class);
		final OrderData orderData = this.shipmentData.getOrderFk();
		olqData.setOlqId(1);
		olqData.setOrderLine(orderData.getOrderLines().get(0));
		olqData.setQuantityUnitCode(QUANTITY_UNIT_CODE);
		olqData.setQuantityValue(QUANTITY_UNIT_VALUE);
		olqData.setShipment(this.shipmentData);
		olqData.setStatus(orderLineQuantityStatusData);
		olqData.setStockroomLocationId(STOCKROOM_LOCATION_ID);
		this.persistenceManager.flush();

		// when
		final Shipment shipment = this.shipmentConverter.convert(this.shipmentData);

		// then
		Assert.assertEquals(Arrays.asList("1"), shipment.getOlqIds());
	}
}
