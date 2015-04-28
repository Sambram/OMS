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
package com.hybris.oms.service.cis;

import com.hybris.cis.api.geolocation.model.CisLocationRequest;
import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisOrder;
import com.hybris.cis.api.payment.model.CisPaymentRequest;
import com.hybris.cis.api.payment.model.CisPaymentTransactionResult;
import com.hybris.cis.api.shipping.model.CisShipment;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.AmountVT;


/**
 * The Interface CisConverter.
 * */
public interface CisConverter
{

	/**
	 * Returns a pair of Double values for Latitude and Longitude.
	 * 
	 * @param address
	 *           from CIS
	 * @return a pair of Double values for Latitude (element at index 0) and Longitude (element at index 1)
	 */
	Double[] convertCisAddressToOmsLocationCoordinates(CisAddress cisAddress);

	/**
	 * Call the convertPaymentFromCis.
	 * 
	 * @param CisPaymentRequest
	 *           from CIS
	 * @return Amount from OMS
	 */
	AmountVT convertCisPaymentRequestToOmsAmount(CisPaymentRequest cisPaymentRequest);

	/**
	 * Call the convertPaymentToCis.
	 * 
	 * @param Amount
	 *           from OMS
	 * @return CisPaymentRequest from CIS
	 */
	CisPaymentRequest convertOmsAmountToCisPaymentRequest(AmountVT amount);


	/**
	 * Call the convertZipCodesToCisLocationRequest.
	 * 
	 * @param TempAddress
	 *           from OMS
	 * @return CisLocationRequest from CIS
	 */
	CisLocationRequest convertOmsAddressToCisLocationRequest(final AddressVT address);

	/**
	 * Call the convertShipmentDataToCis.
	 * 
	 * @param ShipmentData
	 *           from OMS
	 * @return CisShipment from CIS
	 */
	CisShipment convertOmsShipmentToCisShipment(final OrderData order, final ShipmentData shipment);

	/**
	 * Convert the OMS ShipmentData format to a CIS CisOrder format.
	 * 
	 * @param shipment
	 *           - Shipment to be converted to CIS format.
	 * @return A CisOrder object.
	 */
	CisOrder convertOmsShipmentToCisOrder(final ShipmentData shipment);

	/**
	 * Convert the OMS ShipmentData format to a CIS CisPaymentRequest format.
	 * 
	 * @param shipment
	 * @return a CisPaymentRequest object
	 */
	CisPaymentRequest convertOmsShipmentToCisPaymentRequest(ShipmentData shipment);

	/**
	 * Convert the CIS CisPaymentTransactionResult format to OMS Amount format.
	 * 
	 * @param result
	 * @return A Amount object
	 */
	AmountVT convertCisPaymentTransactionResultToOmsAmount(CisPaymentTransactionResult result);

	/**
	 * converts the oms ReturnData format to CIS CisPaymentRequest format
	 * 
	 * @param aReturn
	 * @return
	 */
	CisPaymentRequest convertOmsReturnToCisPaymentRequest(ReturnData aReturn);

	/**
	 * converts the oms return object to a CIS order
	 * 
	 * @param aReturn
	 * @return
	 */
	CisOrder convertOmsReturnToCisOrder(ReturnData aReturn);

}
