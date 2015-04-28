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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.returns.*;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Measure;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.conversion.impl.order.OrderTestUtils;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.managedobjects.types.QuantityVT;


public class ReturnTestUtils
{

	public static final String TRACKING_URL = "TRACKING_URL";
	public static final String TRACKING_ID = "TRACKING_ID";
	public static final long RETURN_SHIPMENT_ID = 99l;
	public static final String PACKAGE_DESCRIPTION = "PACKAGE_DESCRIPTION";
	public static final String SHIPPING_METHOD = "SHIPPING_METHOD";
	public static final String NOTE = "NOTE";
	public static final String LABEL_URL = "LABEL_URL";
	public static final String STORE_NAME = "STORE_NAME";
	public static final String LOCATION_ID = "LOCATION_ID";
	public static final String COUNTRY_UNITED_STATES_OF_AMERICA = "United States of America";
	public static final String COUNTRY_US = "US";

	public static final long RETURN_ID = 12354l;
	public static final long RETURN_ORDER_LINE_ID = 123456l;
	public static final long RETURN_PAYMENT_INFO_ID = 3l;

	public static final String STATUS = "statusTest";
	public static final String UNIT = "CAD";
	public static final String SKU_ID = "dummySkuId";
	public static final String TAX_CATEGORY = "dummyTaxCategory";
	public static final String AUTH_URL = "http://qa-cis-1.yrdrt.fra.hybris.com:8080/hybris-cis-cybersource-payment-web/psp/cisPaymentCybersource/authorizations/30e12178f6884910cb724c8b10f6a1a9e339ac6b-1/3957825832700176056470/refunds";
	public static final String AUTH_URL_2 = "dummyAuthUrl2";
	public static final String RETURN_PAYMENT_TYPE = "cashTest";
	public static final String STATE = "state";
	public static final String IMPORTED_ORDER_ID = "83";
	public static final long RETURN_PAYMENT_INFO_DATA_ID = 1L;


	public static final String RETURNREASON_LATEDELIVERY = "LATEDELIVERY";
	public static final String RETURNREASON_WRONGSIZE = "WRONGSIZE";
	public static final String RETURNREASON_CHANGMIND = "CHANGMIND";

	private ReturnData returnData;
	private ReturnOrderLineData returnOrderLineData;
	private OrderLineData orderLineData;
	private ReturnPaymentInfoData returnPaymentInfoData;
	private StockroomLocationData locationData;
	private ReturnShipmentData returnShipmentData;
	private ReturnLineRejectionData returnLineRejectionData;


	public ReturnData getReturnData()
	{
		return this.returnData;
	}

	public ReturnOrderLineData getReturnOrderLineData()
	{
		return this.returnOrderLineData;
	}

	public ReturnShipmentData getReturnShipmentData()
	{
		return returnShipmentData;
	}

	public ReturnPaymentInfoData getReturnPaymentInfoData()
	{
		return this.returnPaymentInfoData;
	}

	public StockroomLocationData getLocationData()
	{
		return locationData;
	}

	public void createObjects(final PersistenceManager persistenceManager)
	{
		createLocationData(persistenceManager);
		createReturnPaymentInfoData(persistenceManager);
		createReturnShipmentData(persistenceManager);
		createReturnData(persistenceManager);
		createReturnOrderLineData(persistenceManager);
		createReturnReviewData(persistenceManager);
	}

	private void createLocationData(final PersistenceManager persistenceManager)
	{
		final CountryData usa = persistenceManager.create(CountryData.class);
		usa.setCode(COUNTRY_US);
		usa.setName(COUNTRY_UNITED_STATES_OF_AMERICA);

		locationData = persistenceManager.create(StockroomLocationData.class);
		locationData.setLocationId(LOCATION_ID);
		locationData.setAddress(new AddressVT("100", "Broadway", "New York City", "NY", "10001", null, null, COUNTRY_US,
				COUNTRY_UNITED_STATES_OF_AMERICA, "name", "phone"));
		locationData.setLocationRoles(Collections.singleton(LocationRole.SHIPPING.name()));
		locationData.setShipToCountries(Collections.singleton(usa));
		locationData.setStoreName(STORE_NAME);
	}

	private void createReturnOrderLineData(final PersistenceManager persistenceManager)
	{
		this.returnOrderLineData = persistenceManager.create(ReturnOrderLineData.class);
		this.returnOrderLineData.setReturnOrderLineId(RETURN_ORDER_LINE_ID);
		this.returnOrderLineData.setReturnOrderLineStatus(STATUS);
		this.returnOrderLineData.setOrderLineId(this.orderLineData.getOrderLineId());
		this.returnOrderLineData.setQuantity(new QuantityVT(UNIT, 2));
		this.returnOrderLineData.setMyReturn(this.returnData);
	}

	private void createReturnShipmentData(final PersistenceManager persistenceManager)
	{
		returnShipmentData = persistenceManager.create(ReturnShipmentData.class);
		returnShipmentData.setGrossWeightUnitCode(UNIT);
		returnShipmentData.setGrossWeightValue(5.0f);
		returnShipmentData.setHeightUnitCode(UNIT);
		returnShipmentData.setHeightValue(10.0f);
		returnShipmentData.setInsuranceValueAmountCurrencyCode(UNIT);
		returnShipmentData.setInsuranceValueAmountValue(9.99d);
		returnShipmentData.setLabelUrl(LABEL_URL);
		returnShipmentData.setLengthUnitCode(UNIT);
		returnShipmentData.setLengthValue(15.0f);
		returnShipmentData.setNote(NOTE);
		returnShipmentData.setPackageDescription(PACKAGE_DESCRIPTION);
		returnShipmentData.setReturnShipmentId(RETURN_SHIPMENT_ID);
		returnShipmentData.setShippingMethod(SHIPPING_METHOD);
		returnShipmentData.setTrackingId(TRACKING_ID);
		returnShipmentData.setTrackingUrl(TRACKING_URL);
		returnShipmentData.setWidthUnitCode(UNIT);
		returnShipmentData.setWidthValue(20.0f);
		returnShipmentData.setMyReturn(this.returnData);
	}

	private void createReturnPaymentInfoData(final PersistenceManager persistenceManager)
	{
		this.returnPaymentInfoData = persistenceManager.create(ReturnPaymentInfoData.class);
		this.returnPaymentInfoData.setReturnPaymentInfoId(RETURN_PAYMENT_INFO_DATA_ID);
		this.returnPaymentInfoData.setReturnPaymentType(RETURN_PAYMENT_TYPE);
		this.returnPaymentInfoData.setReturnPaymentAmount(new AmountVT(UNIT, 5.0));
		this.returnPaymentInfoData.setMyReturn(this.returnData);
	}

	private void createReturnData(final PersistenceManager persistenceManager)
	{
		final OrderTestUtils orderUtil = new OrderTestUtils();
		orderUtil.createObjects(persistenceManager);
		this.orderLineData = orderUtil.getOrderLineData();

		this.returnData = persistenceManager.create(ReturnData.class);
		this.returnData.setReturnReasonCode(RETURNREASON_LATEDELIVERY);
		this.returnData.setState(STATE);
		this.returnData.setReturnId(RETURN_ID);
		this.returnData.setShippingRefunded(false);
		// order shouldn't be null
		this.returnData.setOrder(orderUtil.getOrderData());
		this.returnData.setCalculatedRefundAmount(new AmountVT(UNIT, 100.0));
		this.returnData.setCustomRefundAmount(new AmountVT(UNIT, 100.0));
		this.returnData.setReturnPaymentInfos(this.returnPaymentInfoData);
		this.returnData.setReturnOrderLines(Arrays.asList(this.returnOrderLineData));
		returnData.setReturnLocation(locationData);
		returnData.setReturnShipment(returnShipmentData);
	}

	public void createReturnReviewData(final PersistenceManager persistenceManager)
	{
		returnLineRejectionData = persistenceManager.create(ReturnLineRejectionData.class);
		returnLineRejectionData.setQuantity(5);
		returnLineRejectionData.setReason(ReviewReason.OTHER.name());
		returnLineRejectionData.setResponsible("John");
		returnLineRejectionData.setMyReturnOrderLine(returnOrderLineData);
	}

	public static Location createLocationDto()
	{
		final Location location = new Location();
		location.setAddress(new Address("100", "Broadway", "New York City", "NY", "10001", null, null, COUNTRY_US,
				COUNTRY_UNITED_STATES_OF_AMERICA, "name", "phone"));
		location.setLocationId(LOCATION_ID);
		location.setLocationRoles(Collections.singleton(LocationRole.SHIPPING));
		location.setShipToCountriesCodes(Collections.singleton(COUNTRY_US));
		location.setStoreName(STORE_NAME);
		return location;
	}

	public static Return createBorisReturnDto()
	{
		final Return aReturn = new Return();
		aReturn.setOrderId(IMPORTED_ORDER_ID);
		aReturn.setReturnId(String.valueOf(RETURN_ID));
		aReturn.setCustomTotalRefundAmount(new Amount(UNIT, 0.0));
		aReturn.setReturnReasonCode(RETURNREASON_LATEDELIVERY);
		aReturn.setReturnOrderLines(ImmutableList.of(createReturnOrderLineDto()));
		aReturn.setReturnPaymentInfos(createReturnPaymentInfoDto());
		aReturn.setShippingRefunded(false);
		return aReturn;
	}

	public static ReturnReview createReturnReviewDto(final String returnOrderLineId)
	{
		final ReturnLineRejection firstRejectedRLR = new ReturnLineRejection();
		firstRejectedRLR.setQuantity(1);
		firstRejectedRLR.setResponsible("John");
		firstRejectedRLR.setReason(ReviewReason.EXPIRED);
		firstRejectedRLR.setReturnOrderLineId(returnOrderLineId);

		final ReturnLineRejection secondRejectedRLR = new ReturnLineRejection();
		secondRejectedRLR.setQuantity(2);
		secondRejectedRLR.setResponsible("John");
		secondRejectedRLR.setReason(ReviewReason.EXPIRED);
		secondRejectedRLR.setReturnOrderLineId(returnOrderLineId);

		final List<ReturnLineRejection> returnLineRejections = new ArrayList<>();
		returnLineRejections.add(firstRejectedRLR);
		returnLineRejections.add(secondRejectedRLR);

		final ReturnReview aReturnReview = new ReturnReview();
		aReturnReview.setReturnLineRejections(returnLineRejections);
		return aReturnReview;
	}

	public static Return createOnlineReturnDto()
	{
		final Return aReturn = createBorisReturnDto();
		aReturn.setReturnShipment(createReturnShipmentDto());
		return aReturn;
	}

	public static ReturnOrderLine createReturnOrderLineDto()
	{
		final OrderLine orderLine = new OrderLine();
		orderLine.setOrderLineId(OrderTestUtils.ORDER_LINE_ID_1);
		orderLine.setSkuId(SKU_ID);

		final ReturnOrderLine returnOrderLine = new ReturnOrderLine();
		returnOrderLine.setReturnOrderLineStatus(STATUS);
		returnOrderLine.setOrderLine(orderLine);
		returnOrderLine.setQuantity(new Quantity(UNIT, 5));
		returnOrderLine.setReturnOrderLineId(String.valueOf(RETURN_ORDER_LINE_ID));

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
		returnShipment.setReturnShipmentId(Long.toString(RETURN_SHIPMENT_ID));
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
