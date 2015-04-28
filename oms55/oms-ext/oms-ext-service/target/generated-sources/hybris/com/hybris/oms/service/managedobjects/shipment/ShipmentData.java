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
 * Generated managedobject class for type ShipmentData first defined at extension <code>shipment</code>.
 * <p/>
 * Contains data on the Shipment.
 */
public interface ShipmentData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ShipmentData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.shipFrom</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, com.hybris.oms.service.managedobjects.types.AddressVT> SHIPFROM = new AttributeType<>("shipFrom");
	AttributeType<ShipmentData, String> SHIPFROM_ADDRESSLINE1 = new AttributeType<>("shipFrom#addressLine1");
	AttributeType<ShipmentData, String> SHIPFROM_ADDRESSLINE2 = new AttributeType<>("shipFrom#addressLine2");
	AttributeType<ShipmentData, String> SHIPFROM_CITYNAME = new AttributeType<>("shipFrom#cityName");
	AttributeType<ShipmentData, String> SHIPFROM_COUNTRYSUBENTITY = new AttributeType<>("shipFrom#countrySubentity");
	AttributeType<ShipmentData, String> SHIPFROM_POSTALZONE = new AttributeType<>("shipFrom#postalZone");
	AttributeType<ShipmentData, Double> SHIPFROM_LATITUDEVALUE = new AttributeType<>("shipFrom#latitudeValue");
	AttributeType<ShipmentData, Double> SHIPFROM_LONGITUDEVALUE = new AttributeType<>("shipFrom#longitudeValue");
	AttributeType<ShipmentData, String> SHIPFROM_COUNTRYISO3166ALPHA2CODE = new AttributeType<>("shipFrom#countryIso3166Alpha2Code");
	AttributeType<ShipmentData, String> SHIPFROM_COUNTRYNAME = new AttributeType<>("shipFrom#countryName");
	AttributeType<ShipmentData, String> SHIPFROM_NAME = new AttributeType<>("shipFrom#name");
	AttributeType<ShipmentData, String> SHIPFROM_PHONENUMBER = new AttributeType<>("shipFrom#phoneNumber");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.stockroomLocationId</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> STOCKROOMLOCATIONID = new AttributeType<>("stockroomLocationId");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.widthUnitCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> WIDTHUNITCODE = new AttributeType<>("widthUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.orderLinesFulfillmentType</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType> ORDERLINESFULFILLMENTTYPE = new AttributeType<>("orderLinesFulfillmentType");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.totalGoodsItemQuantityValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Integer> TOTALGOODSITEMQUANTITYVALUE = new AttributeType<>("totalGoodsItemQuantityValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.netWeightValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Float> NETWEIGHTVALUE = new AttributeType<>("netWeightValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.insuranceValueAmountValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Double> INSURANCEVALUEAMOUNTVALUE = new AttributeType<>("insuranceValueAmountValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.grossVolumeValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Float> GROSSVOLUMEVALUE = new AttributeType<>("grossVolumeValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.currencyCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> CURRENCYCODE = new AttributeType<>("currencyCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.lengthUnitCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> LENGTHUNITCODE = new AttributeType<>("lengthUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.shipmentId</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Long> SHIPMENTID = new AttributeType<>("shipmentId");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.olqsStatus</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> OLQSSTATUS = new AttributeType<>("olqsStatus");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.shippingMethod</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> SHIPPINGMETHOD = new AttributeType<>("shippingMethod");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.orderFk</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, com.hybris.oms.service.managedobjects.order.OrderData> ORDERFK = new AttributeType<>("orderFk");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.heightValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Float> HEIGHTVALUE = new AttributeType<>("heightValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.grossWeightValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Float> GROSSWEIGHTVALUE = new AttributeType<>("grossWeightValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.shippingAndHandling</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData> SHIPPINGANDHANDLING = new AttributeType<>("shippingAndHandling");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.location</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> LOCATION = new AttributeType<>("location");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.taxCategory</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> TAXCATEGORY = new AttributeType<>("taxCategory");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.state</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> STATE = new AttributeType<>("state");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.priorityLevelCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> PRIORITYLEVELCODE = new AttributeType<>("priorityLevelCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.grossVolumeUnitCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> GROSSVOLUMEUNITCODE = new AttributeType<>("grossVolumeUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.heightUnitCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> HEIGHTUNITCODE = new AttributeType<>("heightUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.amountCapturedValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Double> AMOUNTCAPTUREDVALUE = new AttributeType<>("amountCapturedValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.packageDescription</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> PACKAGEDESCRIPTION = new AttributeType<>("packageDescription");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.delivery</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, com.hybris.oms.service.managedobjects.shipment.DeliveryData> DELIVERY = new AttributeType<>("delivery");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.widthValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Float> WIDTHVALUE = new AttributeType<>("widthValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.originalShipmentId</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Long> ORIGINALSHIPMENTID = new AttributeType<>("originalShipmentId");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.grossWeightUnitCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> GROSSWEIGHTUNITCODE = new AttributeType<>("grossWeightUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.totalGoodsItemQuantityUnitCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> TOTALGOODSITEMQUANTITYUNITCODE = new AttributeType<>("totalGoodsItemQuantityUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.netWeightUnitCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> NETWEIGHTUNITCODE = new AttributeType<>("netWeightUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.version</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.firstArrivalStockroomLocationId</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> FIRSTARRIVALSTOCKROOMLOCATIONID = new AttributeType<>("firstArrivalStockroomLocationId");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.authUrls</code> attribute defined at extension <code>shipment</code>. */
	CollectionAttributeType<ShipmentData, String> AUTHURLS = new CollectionAttributeType<>("authUrls");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.insuranceValueAmountCurrencyCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> INSURANCEVALUEAMOUNTCURRENCYCODE = new AttributeType<>("insuranceValueAmountCurrencyCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.lastExitStockroomLocationId</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> LASTEXITSTOCKROOMLOCATIONID = new AttributeType<>("lastExitStockroomLocationId");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.amountCapturedCurrencyCode</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, String> AMOUNTCAPTUREDCURRENCYCODE = new AttributeType<>("amountCapturedCurrencyCode");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.pickupInStore</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Boolean> PICKUPINSTORE = new AttributeType<>("pickupInStore");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.merchandisePrice</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, com.hybris.oms.service.managedobjects.types.PriceVT> MERCHANDISEPRICE = new AttributeType<>("merchandisePrice");
	AttributeType<ShipmentData, String> MERCHANDISEPRICE_SUBTOTALCURRENCYCODE = new AttributeType<>("merchandisePrice#subTotalCurrencyCode");
	AttributeType<ShipmentData, Double> MERCHANDISEPRICE_SUBTOTALVALUE = new AttributeType<>("merchandisePrice#subTotalValue");
	AttributeType<ShipmentData, String> MERCHANDISEPRICE_TAXCURRENCYCODE = new AttributeType<>("merchandisePrice#taxCurrencyCode");
	AttributeType<ShipmentData, Double> MERCHANDISEPRICE_TAXVALUE = new AttributeType<>("merchandisePrice#taxValue");
	AttributeType<ShipmentData, String> MERCHANDISEPRICE_TAXCOMMITTEDCURRENCYCODE = new AttributeType<>("merchandisePrice#taxCommittedCurrencyCode");
	AttributeType<ShipmentData, Double> MERCHANDISEPRICE_TAXCOMMITTEDVALUE = new AttributeType<>("merchandisePrice#taxCommittedValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShipmentData.lengthValue</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShipmentData, Float> LENGTHVALUE = new AttributeType<>("lengthValue");

	/** <i>Generated constant</i> - Index of <code>ShipmentData</code> type defined at extension <code>shipment</code>. */
	UniqueIndexSingle<ShipmentData, Long> UX_SHIPMENTS_SHIPMENTID = new UniqueIndexSingle<>("UX_shipments_shipmentId", ShipmentData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.state</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Current state of the shipment.
	 * 
	 * @return the state
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getState();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.amountCapturedCurrencyCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Insurance Value Amount.
	 * 
	 * @return the amountCapturedCurrencyCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getAmountCapturedCurrencyCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.amountCapturedValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Insurance Value Amount.
	 * 
	 * @return the amountCapturedValue
	 */
	double getAmountCapturedValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.authUrls</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Authorization URLs.
	 * 
	 * @return the authUrls
	 */
	java.util.List<java.lang.String> getAuthUrls();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.currencyCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Currency code.
	 * 
	 * @return the currencyCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getCurrencyCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.delivery</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping delivery.
	 * 
	 * @return the delivery
	 */
	com.hybris.oms.service.managedobjects.shipment.DeliveryData getDelivery();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.firstArrivalStockroomLocationId</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping firstArrivalLocation.
	 * 
	 * @return the firstArrivalStockroomLocationId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getFirstArrivalStockroomLocationId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.grossVolumeUnitCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Gross Volume.
	 * 
	 * @return the grossVolumeUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getGrossVolumeUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.grossVolumeValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Gross Volume.
	 * 
	 * @return the grossVolumeValue
	 */
	float getGrossVolumeValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.grossWeightUnitCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Gross Weight.
	 * 
	 * @return the grossWeightUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getGrossWeightUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.grossWeightValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Gross Weight.
	 * 
	 * @return the grossWeightValue
	 */
	float getGrossWeightValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.heightUnitCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Package height.
	 * 
	 * @return the heightUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getHeightUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.heightValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Package height.
	 * 
	 * @return the heightValue
	 */
	float getHeightValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.insuranceValueAmountCurrencyCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Insurance Value Amount.
	 * 
	 * @return the insuranceValueAmountCurrencyCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getInsuranceValueAmountCurrencyCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.insuranceValueAmountValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Insurance Value Amount.
	 * 
	 * @return the insuranceValueAmountValue
	 */
	double getInsuranceValueAmountValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.lastExitStockroomLocationId</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping lastExitLocation.
	 * 
	 * @return the lastExitStockroomLocationId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLastExitStockroomLocationId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.lengthUnitCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Package length.
	 * 
	 * @return the lengthUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLengthUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.lengthValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Package length.
	 * 
	 * @return the lengthValue
	 */
	float getLengthValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.stockroomLocationId</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Stockroom Location Id.
	 * 
	 * @return the stockroomLocationId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getStockroomLocationId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.merchandisePrice</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping MerchandisePrice.
	 * 
	 * @return the merchandisePrice
	 */
	com.hybris.oms.service.managedobjects.types.PriceVT getMerchandisePrice();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.netWeightUnitCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Net Weight.
	 * 
	 * @return the netWeightUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getNetWeightUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.netWeightValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Net Weight.
	 * 
	 * @return the netWeightValue
	 */
	float getNetWeightValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.olqsStatus</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * status of the olqs.
	 * 
	 * @return the olqsStatus
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getOlqsStatus();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.orderFk</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Order.
	 * 
	 * @return the orderFk
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.order.OrderData getOrderFk();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.priorityLevelCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Priority Level Code.
	 * 
	 * @return the priorityLevelCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getPriorityLevelCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.shipFrom</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping from.
	 * 
	 * @return the shipFrom
	 */
	com.hybris.oms.service.managedobjects.types.AddressVT getShipFrom();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.shipmentId</code> attribute defined at extension <code>shipment</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * Shipment Id.
	 * 
	 * @return the shipmentId
	 */
	@javax.validation.constraints.NotNull
	long getShipmentId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.originalShipmentId</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Id of the shipment that this shipment was split from.
	 * 
	 * @return the originalShipmentId
	 */
	long getOriginalShipmentId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.shippingAndHandling</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping and handling costs.
	 * 
	 * @return the shippingAndHandling
	 */
	com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData getShippingAndHandling();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.shippingMethod</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Method.
	 * 
	 * @return the shippingMethod
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getShippingMethod();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.taxCategory</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Shipping Tax Category.
	 * 
	 * @return the taxCategory
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTaxCategory();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.totalGoodsItemQuantityUnitCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Total quantity of all OLQs in this shipment.
	 * 
	 * @return the totalGoodsItemQuantityUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTotalGoodsItemQuantityUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.totalGoodsItemQuantityValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Total quantity of all OLQs in this shipment.
	 * 
	 * @return the totalGoodsItemQuantityValue
	 */
	int getTotalGoodsItemQuantityValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.widthUnitCode</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Package width.
	 * 
	 * @return the widthUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getWidthUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.widthValue</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Package width.
	 * 
	 * @return the widthValue
	 */
	float getWidthValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.pickupInStore</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * BOPIS.
	 * 
	 * @return the pickupInStore
	 */
	boolean isPickupInStore();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.packageDescription</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Package description.
	 * 
	 * @return the packageDescription
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getPackageDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.location</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Location URI coming from Tax Invoice.
	 * 
	 * @return the location
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getLocation();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.orderLinesFulfillmentType</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Fulfillment type for orderlines contained in the Shipment(REGULAR, PRE_ORDERLINE, BACK_ORDERLINE).
	 * 
	 * @return the orderLinesFulfillmentType
	 */
	com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType getOrderLinesFulfillmentType();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShipmentData.version</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.state</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Current state of the shipment.
	 *
	 * @param value the state
	 */
	void setState(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.amountCapturedCurrencyCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Insurance Value Amount.
	 *
	 * @param value the amountCapturedCurrencyCode
	 */
	void setAmountCapturedCurrencyCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.amountCapturedValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Insurance Value Amount.
	 *
	 * @param value the amountCapturedValue
	 */
	void setAmountCapturedValue(final double value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.authUrls</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Authorization URLs.
	 *
	 * @param value the authUrls
	 */
	void setAuthUrls(final java.util.List<java.lang.String> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.currencyCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Currency code.
	 *
	 * @param value the currencyCode
	 */
	void setCurrencyCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.delivery</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping delivery.
	 *
	 * @param value the delivery
	 */
	void setDelivery(final com.hybris.oms.service.managedobjects.shipment.DeliveryData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.firstArrivalStockroomLocationId</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping firstArrivalLocation.
	 *
	 * @param value the firstArrivalStockroomLocationId
	 */
	void setFirstArrivalStockroomLocationId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.grossVolumeUnitCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Gross Volume.
	 *
	 * @param value the grossVolumeUnitCode
	 */
	void setGrossVolumeUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.grossVolumeValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Gross Volume.
	 *
	 * @param value the grossVolumeValue
	 */
	void setGrossVolumeValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.grossWeightUnitCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Gross Weight.
	 *
	 * @param value the grossWeightUnitCode
	 */
	void setGrossWeightUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.grossWeightValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Gross Weight.
	 *
	 * @param value the grossWeightValue
	 */
	void setGrossWeightValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.heightUnitCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Package height.
	 *
	 * @param value the heightUnitCode
	 */
	void setHeightUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.heightValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Package height.
	 *
	 * @param value the heightValue
	 */
	void setHeightValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.insuranceValueAmountCurrencyCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Insurance Value Amount.
	 *
	 * @param value the insuranceValueAmountCurrencyCode
	 */
	void setInsuranceValueAmountCurrencyCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.insuranceValueAmountValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Insurance Value Amount.
	 *
	 * @param value the insuranceValueAmountValue
	 */
	void setInsuranceValueAmountValue(final double value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.lastExitStockroomLocationId</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping lastExitLocation.
	 *
	 * @param value the lastExitStockroomLocationId
	 */
	void setLastExitStockroomLocationId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.lengthUnitCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Package length.
	 *
	 * @param value the lengthUnitCode
	 */
	void setLengthUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.lengthValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Package length.
	 *
	 * @param value the lengthValue
	 */
	void setLengthValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.stockroomLocationId</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Stockroom Location Id.
	 *
	 * @param value the stockroomLocationId
	 */
	void setStockroomLocationId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.merchandisePrice</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping MerchandisePrice.
	 *
	 * @param value the merchandisePrice
	 */
	void setMerchandisePrice(final com.hybris.oms.service.managedobjects.types.PriceVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.netWeightUnitCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Net Weight.
	 *
	 * @param value the netWeightUnitCode
	 */
	void setNetWeightUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.netWeightValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Net Weight.
	 *
	 * @param value the netWeightValue
	 */
	void setNetWeightValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.olqsStatus</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * status of the olqs.
	 *
	 * @param value the olqsStatus
	 */
	void setOlqsStatus(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.orderFk</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Order.
	 *
	 * @param value the orderFk
	 */
	void setOrderFk(final com.hybris.oms.service.managedobjects.order.OrderData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.priorityLevelCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Priority Level Code.
	 *
	 * @param value the priorityLevelCode
	 */
	void setPriorityLevelCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.shipFrom</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping from.
	 *
	 * @param value the shipFrom
	 */
	void setShipFrom(final com.hybris.oms.service.managedobjects.types.AddressVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.shipmentId</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipment Id.
	 *
	 * @param value the shipmentId
	 */
	void setShipmentId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.originalShipmentId</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Id of the shipment that this shipment was split from.
	 *
	 * @param value the originalShipmentId
	 */
	void setOriginalShipmentId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.shippingAndHandling</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping and handling costs.
	 *
	 * @param value the shippingAndHandling
	 */
	void setShippingAndHandling(final com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.shippingMethod</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Method.
	 *
	 * @param value the shippingMethod
	 */
	void setShippingMethod(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.taxCategory</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Shipping Tax Category.
	 *
	 * @param value the taxCategory
	 */
	void setTaxCategory(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.totalGoodsItemQuantityUnitCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Total quantity of all OLQs in this shipment.
	 *
	 * @param value the totalGoodsItemQuantityUnitCode
	 */
	void setTotalGoodsItemQuantityUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.totalGoodsItemQuantityValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Total quantity of all OLQs in this shipment.
	 *
	 * @param value the totalGoodsItemQuantityValue
	 */
	void setTotalGoodsItemQuantityValue(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.widthUnitCode</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Package width.
	 *
	 * @param value the widthUnitCode
	 */
	void setWidthUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.widthValue</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Package width.
	 *
	 * @param value the widthValue
	 */
	void setWidthValue(final float value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.pickupInStore</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * BOPIS.
	 *
	 * @param value the pickupInStore
	 */
	void setPickupInStore(final boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.packageDescription</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Package description.
	 *
	 * @param value the packageDescription
	 */
	void setPackageDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.location</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Location URI coming from Tax Invoice.
	 *
	 * @param value the location
	 */
	void setLocation(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.orderLinesFulfillmentType</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Fulfillment type for orderlines contained in the Shipment(REGULAR, PRE_ORDERLINE, BACK_ORDERLINE).
	 *
	 * @param value the orderLinesFulfillmentType
	 */
	void setOrderLinesFulfillmentType(final com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShipmentData.version</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
