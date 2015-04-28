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
 
package com.hybris.oms.service.managedobjects.order;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type PaymentInfoData first defined at extension <code>order</code>.
 * <p/>
 * Contains payment information.
 */
public interface PaymentInfoData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "PaymentInfoData";
	
	/** <i>Generated constant</i> - Attribute key of <code>PaymentInfoData.myOrder</code> attribute defined at extension <code>order</code>. */
	AttributeType<PaymentInfoData, com.hybris.oms.service.managedobjects.order.OrderData> MYORDER = new AttributeType<>("myOrder");
	/** <i>Generated constant</i> - Attribute key of <code>PaymentInfoData.paymentInfoType</code> attribute defined at extension <code>order</code>. */
	AttributeType<PaymentInfoData, String> PAYMENTINFOTYPE = new AttributeType<>("paymentInfoType");
	/** <i>Generated constant</i> - Attribute key of <code>PaymentInfoData.captureId</code> attribute defined at extension <code>order</code>. */
	AttributeType<PaymentInfoData, String> CAPTUREID = new AttributeType<>("captureId");
	/** <i>Generated constant</i> - Attribute key of <code>PaymentInfoData.paymentInfoId</code> attribute defined at extension <code>order</code>. */
	AttributeType<PaymentInfoData, Long> PAYMENTINFOID = new AttributeType<>("paymentInfoId");
	/** <i>Generated constant</i> - Attribute key of <code>PaymentInfoData.authUrl</code> attribute defined at extension <code>order</code>. */
	AttributeType<PaymentInfoData, String> AUTHURL = new AttributeType<>("authUrl");
	/** <i>Generated constant</i> - Attribute key of <code>PaymentInfoData.version</code> attribute defined at extension <code>order</code>. */
	AttributeType<PaymentInfoData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>PaymentInfoData.billingAddress</code> attribute defined at extension <code>order</code>. */
	AttributeType<PaymentInfoData, com.hybris.oms.service.managedobjects.types.AddressVT> BILLINGADDRESS = new AttributeType<>("billingAddress");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_ADDRESSLINE1 = new AttributeType<>("billingAddress#addressLine1");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_ADDRESSLINE2 = new AttributeType<>("billingAddress#addressLine2");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_CITYNAME = new AttributeType<>("billingAddress#cityName");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_COUNTRYSUBENTITY = new AttributeType<>("billingAddress#countrySubentity");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_POSTALZONE = new AttributeType<>("billingAddress#postalZone");
	AttributeType<PaymentInfoData, Double> BILLINGADDRESS_LATITUDEVALUE = new AttributeType<>("billingAddress#latitudeValue");
	AttributeType<PaymentInfoData, Double> BILLINGADDRESS_LONGITUDEVALUE = new AttributeType<>("billingAddress#longitudeValue");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_COUNTRYISO3166ALPHA2CODE = new AttributeType<>("billingAddress#countryIso3166Alpha2Code");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_COUNTRYNAME = new AttributeType<>("billingAddress#countryName");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_NAME = new AttributeType<>("billingAddress#name");
	AttributeType<PaymentInfoData, String> BILLINGADDRESS_PHONENUMBER = new AttributeType<>("billingAddress#phoneNumber");

	/** <i>Generated constant</i> - Index of <code>PaymentInfoData</code> type defined at extension <code>order</code>. */
	UniqueIndexSingle<PaymentInfoData, Long> UX_PAYMENTINFO_PAYMENTINFOID = new UniqueIndexSingle<>("UX_paymentInfo_paymentInfoId", PaymentInfoData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfoData.paymentInfoId</code> attribute defined at extension <code>order</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * Unique paymentInfo identifier.
	 * 
	 * @return the paymentInfoId
	 */
	@javax.validation.constraints.NotNull
	long getPaymentInfoId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfoData.authUrl</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Authorization URL.
	 * 
	 * @return the authUrl
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getAuthUrl();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfoData.paymentInfoType</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Payment Info Type.
	 * 
	 * @return the paymentInfoType
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getPaymentInfoType();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfoData.billingAddress</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the billingAddress
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.types.AddressVT getBillingAddress();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfoData.captureId</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * CaptureId after Payment Capture.
	 * 
	 * @return the captureId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getCaptureId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfoData.myOrder</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * A handle to the parent order.
	 * 
	 * @return the myOrder
	 */
	com.hybris.oms.service.managedobjects.order.OrderData getMyOrder();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PaymentInfoData.version</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>PaymentInfoData.paymentInfoId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Unique paymentInfo identifier.
	 *
	 * @param value the paymentInfoId
	 */
	void setPaymentInfoId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentInfoData.authUrl</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Authorization URL.
	 *
	 * @param value the authUrl
	 */
	void setAuthUrl(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentInfoData.paymentInfoType</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Payment Info Type.
	 *
	 * @param value the paymentInfoType
	 */
	void setPaymentInfoType(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentInfoData.billingAddress</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the billingAddress
	 */
	void setBillingAddress(final com.hybris.oms.service.managedobjects.types.AddressVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentInfoData.captureId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * CaptureId after Payment Capture.
	 *
	 * @param value the captureId
	 */
	void setCaptureId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentInfoData.myOrder</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * A handle to the parent order.
	 *
	 * @param value the myOrder
	 */
	void setMyOrder(final com.hybris.oms.service.managedobjects.order.OrderData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>PaymentInfoData.version</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
