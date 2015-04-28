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
 
package com.hybris.oms.service.managedobjects.returns;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type ReturnShipmentData first defined at extension <code>return</code>.
 * <p/>
 * Contains data about the return shipment.
 */
public interface ReturnShipmentData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ReturnShipmentData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.grossWeightUnitCode</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> GROSSWEIGHTUNITCODE = new AttributeType<>("grossWeightUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.lengthValue</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, Float> LENGTHVALUE = new AttributeType<>("lengthValue");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.version</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.note</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> NOTE = new AttributeType<>("note");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.shippingMethod</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> SHIPPINGMETHOD = new AttributeType<>("shippingMethod");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.widthValue</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, Float> WIDTHVALUE = new AttributeType<>("widthValue");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.labelUrl</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> LABELURL = new AttributeType<>("labelUrl");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.trackingId</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> TRACKINGID = new AttributeType<>("trackingId");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.insuranceValueAmountValue</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, Double> INSURANCEVALUEAMOUNTVALUE = new AttributeType<>("insuranceValueAmountValue");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.insuranceValueAmountCurrencyCode</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> INSURANCEVALUEAMOUNTCURRENCYCODE = new AttributeType<>("insuranceValueAmountCurrencyCode");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.heightUnitCode</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> HEIGHTUNITCODE = new AttributeType<>("heightUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.lengthUnitCode</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> LENGTHUNITCODE = new AttributeType<>("lengthUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.heightValue</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, Float> HEIGHTVALUE = new AttributeType<>("heightValue");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.returnShipmentId</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, Long> RETURNSHIPMENTID = new AttributeType<>("returnShipmentId");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.myReturn</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, com.hybris.oms.service.managedobjects.returns.ReturnData> MYRETURN = new AttributeType<>("myReturn");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.grossWeightValue</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, Float> GROSSWEIGHTVALUE = new AttributeType<>("grossWeightValue");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.packageDescription</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> PACKAGEDESCRIPTION = new AttributeType<>("packageDescription");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.trackingUrl</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> TRACKINGURL = new AttributeType<>("trackingUrl");
	/** <i>Generated constant</i> - Attribute key of <code>ReturnShipmentData.widthUnitCode</code> attribute defined at extension <code>return</code>. */
	AttributeType<ReturnShipmentData, String> WIDTHUNITCODE = new AttributeType<>("widthUnitCode");

	/** <i>Generated constant</i> - Index of <code>ReturnShipmentData</code> type defined at extension <code>return</code>. */
	UniqueIndexSingle<ReturnShipmentData, Long> UX_RETURNSHIPMENTS_RETURNSHIPMENTID = new UniqueIndexSingle<>("UX_returnShipments_returnShipmentId", ReturnShipmentData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.returnShipmentId</code> attribute defined at extension <code>return</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * Return shipment id.
	 * 
	 * @return the returnShipmentId
	 */
	@javax.validation.constraints.NotNull
	long getReturnShipmentId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.myReturn</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * A handle to the parent return.
	 * 
	 * @return the myReturn
	 */
	com.hybris.oms.service.managedobjects.returns.ReturnData getMyReturn();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.shippingMethod</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Shipping Method.
	 * 
	 * @return the shippingMethod
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getShippingMethod();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.labelUrl</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * URL to the shipping label.
	 * 
	 * @return the labelUrl
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLabelUrl();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.trackingId</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Tracking id of the shipment with the carrier.
	 * 
	 * @return the trackingId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTrackingId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.trackingUrl</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Tracking URL for the shipment's carrier.
	 * 
	 * @return the trackingUrl
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTrackingUrl();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.packageDescription</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Package description.
	 * 
	 * @return the packageDescription
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getPackageDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.note</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Notes.
	 * 
	 * @return the note
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getNote();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.insuranceValueAmountCurrencyCode</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Shipping Insurance Value Amount.
	 * 
	 * @return the insuranceValueAmountCurrencyCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getInsuranceValueAmountCurrencyCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.insuranceValueAmountValue</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Shipping Insurance Value Amount.
	 * 
	 * @return the insuranceValueAmountValue
	 */
	double getInsuranceValueAmountValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.heightUnitCode</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Package height.
	 * 
	 * @return the heightUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getHeightUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.heightValue</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Package height.
	 * 
	 * @return the heightValue
	 */
	float getHeightValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.lengthUnitCode</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Package length.
	 * 
	 * @return the lengthUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLengthUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.lengthValue</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Package length.
	 * 
	 * @return the lengthValue
	 */
	float getLengthValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.widthValue</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Package width.
	 * 
	 * @return the widthValue
	 */
	float getWidthValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.widthUnitCode</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Package width.
	 * 
	 * @return the widthUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getWidthUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.grossWeightUnitCode</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Shipping Gross Weight.
	 * 
	 * @return the grossWeightUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getGrossWeightUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.grossWeightValue</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * Shipping Gross Weight.
	 * 
	 * @return the grossWeightValue
	 */
	float getGrossWeightValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnShipmentData.version</code> attribute defined at extension <code>return</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.returnShipmentId</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Return shipment id.
	 *
	 * @param value the returnShipmentId
	 */
	void setReturnShipmentId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.myReturn</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * A handle to the parent return.
	 *
	 * @param value the myReturn
	 */
	void setMyReturn(final com.hybris.oms.service.managedobjects.returns.ReturnData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.shippingMethod</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Shipping Method.
	 *
	 * @param value the shippingMethod
	 */
	void setShippingMethod(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.labelUrl</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * URL to the shipping label.
	 *
	 * @param value the labelUrl
	 */
	void setLabelUrl(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.trackingId</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Tracking id of the shipment with the carrier.
	 *
	 * @param value the trackingId
	 */
	void setTrackingId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.trackingUrl</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Tracking URL for the shipment's carrier.
	 *
	 * @param value the trackingUrl
	 */
	void setTrackingUrl(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.packageDescription</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Package description.
	 *
	 * @param value the packageDescription
	 */
	void setPackageDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.note</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Notes.
	 *
	 * @param value the note
	 */
	void setNote(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.insuranceValueAmountCurrencyCode</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Shipping Insurance Value Amount.
	 *
	 * @param value the insuranceValueAmountCurrencyCode
	 */
	void setInsuranceValueAmountCurrencyCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.insuranceValueAmountValue</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Shipping Insurance Value Amount.
	 *
	 * @param value the insuranceValueAmountValue
	 */
	void setInsuranceValueAmountValue(final double value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.heightUnitCode</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Package height.
	 *
	 * @param value the heightUnitCode
	 */
	void setHeightUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.heightValue</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Package height.
	 *
	 * @param value the heightValue
	 */
	void setHeightValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.lengthUnitCode</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Package length.
	 *
	 * @param value the lengthUnitCode
	 */
	void setLengthUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.lengthValue</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Package length.
	 *
	 * @param value the lengthValue
	 */
	void setLengthValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.widthValue</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Package width.
	 *
	 * @param value the widthValue
	 */
	void setWidthValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.widthUnitCode</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Package width.
	 *
	 * @param value the widthUnitCode
	 */
	void setWidthUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.grossWeightUnitCode</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Shipping Gross Weight.
	 *
	 * @param value the grossWeightUnitCode
	 */
	void setGrossWeightUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.grossWeightValue</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * Shipping Gross Weight.
	 *
	 * @param value the grossWeightValue
	 */
	void setGrossWeightValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ReturnShipmentData.version</code> attribute defined at extension <code>return</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
