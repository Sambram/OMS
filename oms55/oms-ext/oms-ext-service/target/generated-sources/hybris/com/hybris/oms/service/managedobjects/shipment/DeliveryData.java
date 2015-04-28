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
 
package com.hybris.oms.service.managedobjects.shipment;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type DeliveryData first defined at extension <code>shipment</code>.
 * <p/>
 * Contains data on the Delivery.
 */
public interface DeliveryData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "DeliveryData";
	
	/** <i>Generated constant</i> - Attribute key of <code>DeliveryData.trackingUrl</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<DeliveryData, String> TRACKINGURL = new AttributeType<>("trackingUrl");
	/** <i>Generated constant</i> - Attribute key of <code>DeliveryData.trackingID</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<DeliveryData, String> TRACKINGID = new AttributeType<>("trackingID");
	/** <i>Generated constant</i> - Attribute key of <code>DeliveryData.version</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<DeliveryData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>DeliveryData.actualDeliveryDate</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<DeliveryData, java.util.Date> ACTUALDELIVERYDATE = new AttributeType<>("actualDeliveryDate");
	/** <i>Generated constant</i> - Attribute key of <code>DeliveryData.deliveryId</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<DeliveryData, Long> DELIVERYID = new AttributeType<>("deliveryId");
	/** <i>Generated constant</i> - Attribute key of <code>DeliveryData.deliveryAddress</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<DeliveryData, com.hybris.oms.service.managedobjects.types.AddressVT> DELIVERYADDRESS = new AttributeType<>("deliveryAddress");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_ADDRESSLINE1 = new AttributeType<>("deliveryAddress#addressLine1");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_ADDRESSLINE2 = new AttributeType<>("deliveryAddress#addressLine2");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_CITYNAME = new AttributeType<>("deliveryAddress#cityName");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_COUNTRYSUBENTITY = new AttributeType<>("deliveryAddress#countrySubentity");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_POSTALZONE = new AttributeType<>("deliveryAddress#postalZone");
	AttributeType<DeliveryData, Double> DELIVERYADDRESS_LATITUDEVALUE = new AttributeType<>("deliveryAddress#latitudeValue");
	AttributeType<DeliveryData, Double> DELIVERYADDRESS_LONGITUDEVALUE = new AttributeType<>("deliveryAddress#longitudeValue");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_COUNTRYISO3166ALPHA2CODE = new AttributeType<>("deliveryAddress#countryIso3166Alpha2Code");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_COUNTRYNAME = new AttributeType<>("deliveryAddress#countryName");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_NAME = new AttributeType<>("deliveryAddress#name");
	AttributeType<DeliveryData, String> DELIVERYADDRESS_PHONENUMBER = new AttributeType<>("deliveryAddress#phoneNumber");
	/** <i>Generated constant</i> - Attribute key of <code>DeliveryData.labelUrl</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<DeliveryData, String> LABELURL = new AttributeType<>("labelUrl");

	/** <i>Generated constant</i> - Index of <code>DeliveryData</code> type defined at extension <code>shipment</code>. */
	UniqueIndexSingle<DeliveryData, Long> UX_DELIVERIES_DELIVERYID = new UniqueIndexSingle<>("UX_deliveries_deliveryId", DeliveryData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryData.deliveryId</code> attribute defined at extension <code>shipment</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * Delivery Id.
	 * 
	 * @return the deliveryId
	 */
	@javax.validation.constraints.NotNull
	long getDeliveryId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryData.actualDeliveryDate</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * actual delivery date.
	 * 
	 * @return the actualDeliveryDate
	 */
	java.util.Date getActualDeliveryDate();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryData.trackingID</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * tracking id.
	 * 
	 * @return the trackingID
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTrackingID();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryData.labelUrl</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * label URL.
	 * 
	 * @return the labelUrl
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLabelUrl();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryData.trackingUrl</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * tracking URL.
	 * 
	 * @return the trackingUrl
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTrackingUrl();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryData.deliveryAddress</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * delivery address.
	 * 
	 * @return the deliveryAddress
	 */
	com.hybris.oms.service.managedobjects.types.AddressVT getDeliveryAddress();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeliveryData.version</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>DeliveryData.deliveryId</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Delivery Id.
	 *
	 * @param value the deliveryId
	 */
	void setDeliveryId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeliveryData.actualDeliveryDate</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * actual delivery date.
	 *
	 * @param value the actualDeliveryDate
	 */
	void setActualDeliveryDate(final java.util.Date value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeliveryData.trackingID</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * tracking id.
	 *
	 * @param value the trackingID
	 */
	void setTrackingID(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeliveryData.labelUrl</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * label URL.
	 *
	 * @param value the labelUrl
	 */
	void setLabelUrl(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeliveryData.trackingUrl</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * tracking URL.
	 *
	 * @param value the trackingUrl
	 */
	void setTrackingUrl(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeliveryData.deliveryAddress</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * delivery address.
	 *
	 * @param value the deliveryAddress
	 */
	void setDeliveryAddress(final com.hybris.oms.service.managedobjects.types.AddressVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>DeliveryData.version</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
