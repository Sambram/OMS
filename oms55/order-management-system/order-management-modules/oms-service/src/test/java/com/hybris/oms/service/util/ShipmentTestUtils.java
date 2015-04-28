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
package com.hybris.oms.service.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.shipment.DeliveryData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;


public final class ShipmentTestUtils
{
	public static final String STATE = "dummyState";
	public static final String ORDER_ID = "dummyOrderId";
	public static final String FIRST_NAME = "dummyFirstName";
	public static final String LAST_NAME = "dummyLastName";
	public static final long SHIPMENT_ID = 19;
	public static final String SHIPPING_METHOD = "ShippingMethod";
	public static final String OLQS_STATUS = "OlqsStatus";
	public static final Calendar calendar;
	static
	{
		calendar = GregorianCalendar.getInstance();
		calendar.set(2013, 1, 1);
	}
	public static final Date ISSUE_DATE = calendar.getTime();
	public static final String LOCATION_ID = "LocationId";
	public static final String LOCATION_NAME = "LocationName";
	private static final String SAH_FIRST_SHIPMENT_ID = "SAH_FirstShipmentId";
	private static final int DELIVERY_ID = 422;
	private static final int TOTAL_GOODS_ITEM_QUANTITY_VALUE = 17;
	private static final float WIDTH_VALUE = 1.8f;
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
	private static final String PRIORITY_LEVEL_CODE = "PriorityLevelCode";
	private static final String NET_WEIGHT_UNIT_CODE = "NetWeightUnitCode";
	private static final String LENGTH_UNIT_CODE = "LengthUnitCode";
	private static final String LAST_EXIT_LOCATION_ID = "LastExitLocationId";
	private static final String INSURANCE_VALUE_AMOUNT_CURRENCY_CODE = "InsuranceValueAmountCurrencyCode";
	private static final String HEIGHT_UNIT_CODE = "HeightUnitCode";
	private static final String GROSS_WEIGHT_UNIT_CODE = "GrossWeightUnitCode";
	private static final String GROSS_VOLUME_UNIT_CODE = "GrossVolumeUnitCode";
	private static final String FIRST_ARRIVAL_LOCATION_ID = "FirstArrivalLocationId";
	private static final String CURRENCY_CODE = "CurrencyCode";
	private static final String AMOUNT_CAPTURED_CURRENCY_CODE = "AmountCapturedCurrencyCode";

	private ShipmentTestUtils()
	{
		// do not instantiate
	}

	public static ShipmentData createShipmentData(final PersistenceManager persistenceManager)
	{
		final AddressVT shipFromAddress = new AddressVT("al1", "al2", "cN", "cS", "pZ", 1.23, 4.56, "cIso", "cName", null, null);
		final List<String> authUrls = Arrays.asList("dummyUrl1", "dummyUrl2");

		final ShipmentData resultData = persistenceManager.create(ShipmentData.class);

		populateShipment(shipFromAddress, authUrls, resultData);

		final OrderData order = createOrder(persistenceManager);
		resultData.setOrderFk(order);
		resultData.setShippingAndHandling(order.getShippingAndHandling());
		resultData.setDelivery(createDelivery(persistenceManager));

		return resultData;
	}

	private static void populateShipment(final AddressVT shipFromAddress, final List<String> authUrls,
			final ShipmentData resultData)
	{
		resultData.setState(STATE);
		final Price merchandisePrice = new Price(new Amount("USD", 1.3), new Amount("USD", 2.4), new Amount("USD", 3.5));
		resultData.setAmountCapturedCurrencyCode(AMOUNT_CAPTURED_CURRENCY_CODE);
		resultData.setAmountCapturedValue(AMOUNT_CAPTURED_VALUE);
		resultData.setAuthUrls(authUrls);
		resultData.setCurrencyCode(CURRENCY_CODE);
		resultData.setFirstArrivalStockroomLocationId(FIRST_ARRIVAL_LOCATION_ID);
		resultData.setGrossVolumeUnitCode(GROSS_VOLUME_UNIT_CODE);
		resultData.setGrossVolumeValue(GROSS_VOLUME_VALUE);
		resultData.setGrossWeightUnitCode(GROSS_WEIGHT_UNIT_CODE);
		resultData.setGrossWeightValue(GROSS_WEIGHT_VALUE);
		resultData.setHeightUnitCode(HEIGHT_UNIT_CODE);
		resultData.setHeightValue(HEIGHT_VALUE);
		resultData.setInsuranceValueAmountCurrencyCode(INSURANCE_VALUE_AMOUNT_CURRENCY_CODE);
		resultData.setInsuranceValueAmountValue(INSURANCE_VALUE_AMOUNT_VALUE);
		resultData.setLastExitStockroomLocationId(LAST_EXIT_LOCATION_ID);
		resultData.setLengthUnitCode(LENGTH_UNIT_CODE);
		resultData.setLengthValue(LENGTH_VALUE);
		resultData.setStockroomLocationId(LOCATION_ID);
		final PriceVT priceVT = new PriceVT(merchandisePrice.getSubTotal().getCurrencyCode(), merchandisePrice.getSubTotal()
				.getValue(), merchandisePrice.getTax().getCurrencyCode(), merchandisePrice.getTax().getValue(), merchandisePrice
				.getTaxCommitted().getCurrencyCode(), merchandisePrice.getTaxCommitted().getValue());
		resultData.setMerchandisePrice(priceVT);
		resultData.setNetWeightUnitCode(NET_WEIGHT_UNIT_CODE);
		resultData.setNetWeightValue(NET_WEIGHT_VALUE);
		resultData.setOlqsStatus(OLQS_STATUS);
		resultData.setPriorityLevelCode(PRIORITY_LEVEL_CODE);
		resultData.setShipFrom(shipFromAddress);
		resultData.setShipmentId(SHIPMENT_ID);
		resultData.setShippingMethod(SHIPPING_METHOD);
		resultData.setTaxCategory(TAX_CATEGORY);
		resultData.setTotalGoodsItemQuantityUnitCode(TOTAL_GOODS_ITEM_QUANTITY_UNIT_CODE);
		resultData.setTotalGoodsItemQuantityValue(TOTAL_GOODS_ITEM_QUANTITY_VALUE);
		resultData.setWidthUnitCode(WIDTH_UNIT_CODE);
		resultData.setWidthValue(WIDTH_VALUE);
	}

	private static DeliveryData createDelivery(final PersistenceManager persistenceManager)
	{
		final DeliveryData delivery = persistenceManager.create(DeliveryData.class);
		final AddressVT deliveryAddress = new AddressVT("al7", "al1", "cNx", "cSU", "pZ5", 13.23, 41.5, "cIs0", "cNam3", null, null);

		delivery.setActualDeliveryDate(ISSUE_DATE);
		delivery.setDeliveryAddress(deliveryAddress);
		delivery.setDeliveryId(DELIVERY_ID);
		delivery.setLabelUrl("LABEL_URL");
		delivery.setTrackingID("TRACKING_ID");
		delivery.setTrackingUrl("TRACKING_URL");
		return delivery;
	}

	private static ShippingAndHandlingData createShippingAndHandling(final PersistenceManager persistenceManager)
	{
		final ShippingAndHandlingData sahData = persistenceManager.create(ShippingAndHandlingData.class);

		final Price sahPrice = new Price(new Amount("EUR", 1.7), new Amount("EUR", 7.1), new Amount("EUR", 6.6));
		final PriceVT priceVT = new PriceVT(sahPrice.getSubTotal().getCurrencyCode(), sahPrice.getSubTotal().getValue(), sahPrice
				.getTax().getCurrencyCode(), sahPrice.getTax().getValue(), sahPrice.getTaxCommitted().getCurrencyCode(), sahPrice
				.getTaxCommitted().getValue());


		sahData.setFirstShipmentId(SAH_FIRST_SHIPMENT_ID);
		sahData.setOrderId("OrderId");
		sahData.setShippingPrice(priceVT);

		return sahData;
	}

	public static OrderData createOrder(final PersistenceManager persistenceManager)
	{
		final OrderData order = persistenceManager.create(OrderData.class);
		order.setShippingAddress(new AddressVT("al2", "al3", "cN", "cS", "pZ", 1.23, 4.56, "cIso", "cName", null, null));
		order.setFirstName(FIRST_NAME);
		order.setLastName(LAST_NAME);
		order.setEmailid("user@test.de");
		order.setOrderId(ORDER_ID);
		order.setIssueDate(ISSUE_DATE);
		order.setShippingMethod("dummyShippingMethod");
		order.setShippingAndHandling(createShippingAndHandling(persistenceManager));

		final PaymentInfoData pid = persistenceManager.create(PaymentInfoData.class);
		order.setPaymentInfos(Arrays.asList(pid));
		pid.setAuthUrl("dummyAuthUrl");
		pid.setBillingAddress(new AddressVT("al1", "al5", "cN3", "cS2", "pZ4", 1.23, 4.56, "cIso1", "cName3", null, null));
		pid.setPaymentInfoType("dummyPaymentInfoType");

		final OrderLineData orderLine = persistenceManager.create(OrderLineData.class);
		order.setOrderLines(Arrays.asList(orderLine));
		orderLine.setMyOrder(order);
		orderLine.setOrderLineId("dummyOrderLineId");
		orderLine.setQuantityUnassignedValue(2);
		orderLine.setQuantityValue(3);
		orderLine.setSkuId("dummySkuId");
		orderLine.setTaxCategory("dummyTaxCategory");
		orderLine.setUnitPriceValue(5.56);
		orderLine.setUnitTaxValue(7.62);
		orderLine.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));

		return order;
	}

}
