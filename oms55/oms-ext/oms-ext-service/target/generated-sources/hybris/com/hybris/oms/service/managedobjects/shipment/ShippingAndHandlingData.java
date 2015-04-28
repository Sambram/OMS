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
 * Generated managedobject class for type ShippingAndHandlingData first defined at extension <code>shipment</code>.
 * <p/>
 * Contains data on shipping and handling costs.
 */
public interface ShippingAndHandlingData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ShippingAndHandlingData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ShippingAndHandlingData.shippingPrice</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShippingAndHandlingData, com.hybris.oms.service.managedobjects.types.PriceVT> SHIPPINGPRICE = new AttributeType<>("shippingPrice");
	AttributeType<ShippingAndHandlingData, String> SHIPPINGPRICE_SUBTOTALCURRENCYCODE = new AttributeType<>("shippingPrice#subTotalCurrencyCode");
	AttributeType<ShippingAndHandlingData, Double> SHIPPINGPRICE_SUBTOTALVALUE = new AttributeType<>("shippingPrice#subTotalValue");
	AttributeType<ShippingAndHandlingData, String> SHIPPINGPRICE_TAXCURRENCYCODE = new AttributeType<>("shippingPrice#taxCurrencyCode");
	AttributeType<ShippingAndHandlingData, Double> SHIPPINGPRICE_TAXVALUE = new AttributeType<>("shippingPrice#taxValue");
	AttributeType<ShippingAndHandlingData, String> SHIPPINGPRICE_TAXCOMMITTEDCURRENCYCODE = new AttributeType<>("shippingPrice#taxCommittedCurrencyCode");
	AttributeType<ShippingAndHandlingData, Double> SHIPPINGPRICE_TAXCOMMITTEDVALUE = new AttributeType<>("shippingPrice#taxCommittedValue");
	/** <i>Generated constant</i> - Attribute key of <code>ShippingAndHandlingData.version</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShippingAndHandlingData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>ShippingAndHandlingData.orderId</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShippingAndHandlingData, String> ORDERID = new AttributeType<>("orderId");
	/** <i>Generated constant</i> - Attribute key of <code>ShippingAndHandlingData.firstShipmentId</code> attribute defined at extension <code>shipment</code>. */
	AttributeType<ShippingAndHandlingData, String> FIRSTSHIPMENTID = new AttributeType<>("firstShipmentId");

	/** <i>Generated constant</i> - Index of <code>ShippingAndHandlingData</code> type defined at extension <code>shipment</code>. */
	UniqueIndexSingle<ShippingAndHandlingData, String> UX_SHIPPINGANDHANDLING_ORDERID = new UniqueIndexSingle<>("UX_shippingAndHandling_orderId", ShippingAndHandlingData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ShippingAndHandlingData.orderId</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * The id of the order related to this shipment.
	 * 
	 * @return the orderId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getOrderId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShippingAndHandlingData.firstShipmentId</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Id of an order's first shipment.
	 * 
	 * @return the firstShipmentId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getFirstShipmentId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShippingAndHandlingData.shippingPrice</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * Contains the shipment's shipping price and tax price.
	 * 
	 * @return the shippingPrice
	 */
	com.hybris.oms.service.managedobjects.types.PriceVT getShippingPrice();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ShippingAndHandlingData.version</code> attribute defined at extension <code>shipment</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ShippingAndHandlingData.orderId</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * The id of the order related to this shipment.
	 *
	 * @param value the orderId
	 */
	void setOrderId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShippingAndHandlingData.firstShipmentId</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Id of an order's first shipment.
	 *
	 * @param value the firstShipmentId
	 */
	void setFirstShipmentId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShippingAndHandlingData.shippingPrice</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * Contains the shipment's shipping price and tax price.
	 *
	 * @param value the shippingPrice
	 */
	void setShippingPrice(final com.hybris.oms.service.managedobjects.types.PriceVT value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ShippingAndHandlingData.version</code> attribute defined at extension <code>shipment</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
