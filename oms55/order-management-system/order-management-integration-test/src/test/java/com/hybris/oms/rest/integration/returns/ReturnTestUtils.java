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
package com.hybris.oms.rest.integration.returns;

import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.domain.returns.ReturnShipment;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Measure;
import com.hybris.oms.domain.types.Quantity;

import com.google.common.collect.ImmutableList;




public class ReturnTestUtils
{

	public static final String TRACKING_URL = "TRACKING_URL";
	public static final String TRACKING_ID = "TRACKING_ID";
	public static final String PACKAGE_DESCRIPTION = "PACKAGE_DESCRIPTION";
	public static final String SHIPPING_METHOD = "SHIPPING_METHOD";
	public static final String NOTE = "NOTE";
	public static final String LABEL_URL = "LABEL_URL";
	public static final String STORE_NAME = "STORE_NAME";
	public static final String LOCATION_ID = "LOCATION_ID";
	public static final String COUNTRY_UNITED_STATES_OF_AMERICA = "United States of America";
	public static final String COUNTRY_US = "US";

	public static final String RETURN_ID = "1";
	public static final String RETURN_ORDER_LINE_ID = "123546";
	public static final long RETURN_PAYMENT_INFO_ID = 3L;

	public static final String STATUS = "statusTest";
	public static final String UNIT = "CAD";
	public static final String SKU_ID = "skuId";
	public static final String AUTH_URL = "dummyAuthUrl";
	public static final String AUTH_URL_2 = "dummyAuthUrl2";
	public static final String RETURN_PAYMENT_TYPE = "cashTest";
	public static final String TEST_ORDER_ID = "TestOrderId";

	public static final String RETURNREASON_LATEDELIVERY = "LATEDELIVERY";
	public static final String RETURNREASON_CHANGMIND = "CHANGMIND";

	public static Return createReturnDto(final String orderId, final String sku, final int quantity)
	{
		final Return aReturn = new Return();
		aReturn.setOrderId(orderId);
		aReturn.setReturnId(RETURN_ID);
		aReturn.setCustomTotalRefundAmount(new Amount(UNIT, 0.0));
		aReturn.setReturnReasonCode(RETURNREASON_LATEDELIVERY);
		aReturn.setReturnOrderLines(ImmutableList.of(createReturnOrderLineDto(sku, quantity)));
		aReturn.setReturnPaymentInfos(createReturnPaymentInfoDto());
		aReturn.setShippingRefunded(false);
		return aReturn;
	}

	public static Return createReturnDto(final String orderId, final String sku)
	{

		return createReturnDto(orderId, sku, 1);
	}

	public static ReturnOrderLine createReturnOrderLineDto(final String sku)
	{
		return createReturnOrderLineDto(sku, 1);
	}

	public static ReturnOrderLine createReturnOrderLineDto(final String sku, final int quantity)
	{

		return createReturnOrderLineDto(sku, quantity, RETURN_ORDER_LINE_ID);
	}

	public static ReturnOrderLine createReturnOrderLineDto(final String sku, final int quantity, final String returnOrderLineId)
	{

		final ReturnOrderLine returnOrderLine = new ReturnOrderLine();
		returnOrderLine.setReturnOrderLineStatus(STATUS);

		final OrderLine orderLine = new OrderLine();
		orderLine.setSkuId(sku);
		returnOrderLine.setOrderLine(orderLine);
		returnOrderLine.setQuantity(new Quantity(UNIT, quantity));
		returnOrderLine.setReturnOrderLineId(returnOrderLineId);

		return returnOrderLine;
	}

	public static ReturnShipment createReturnShipmentDto()
	{
		final ReturnShipment returnShipment = new ReturnShipment();
		returnShipment.setGrossWeight(new Measure(UNIT, 5.0f));
		returnShipment.setHeight(new Measure(UNIT, 10.0f));
		returnShipment.setInsuranceValueAmount(new Amount(UNIT, 9.99d));
		returnShipment.setLabelUrl(LABEL_URL);
		returnShipment.setLength(new Measure(UNIT, 15.0f));
		returnShipment.setNote(NOTE);
		returnShipment.setPackageDescription(PACKAGE_DESCRIPTION);
		returnShipment.setShippingMethod(SHIPPING_METHOD);
		returnShipment.setTrackingId(TRACKING_ID);
		returnShipment.setTrackingUrl(TRACKING_URL);
		returnShipment.setWidth(new Measure(UNIT, 20.0f));
		return returnShipment;
	}

	public static ReturnPaymentInfo createReturnPaymentInfoDto()
	{

		final ReturnPaymentInfo returnPaymentInfo = new ReturnPaymentInfo();
		returnPaymentInfo.setReturnPaymentAmount(new Amount(UNIT, 5.2));
		returnPaymentInfo.setReturnPaymentInfoId(String.valueOf(RETURN_PAYMENT_INFO_ID));
		returnPaymentInfo.setReturnPaymentType(RETURN_PAYMENT_TYPE);
		return returnPaymentInfo;
	}

}
