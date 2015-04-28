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
 * Generated managedobject class for type OrderLineData first defined at extension <code>order</code>.
 * <p/>
 * Contains data on each order line.
 */
public interface OrderLineData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "OrderLineData";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.version</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.unitPriceValue</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, Double> UNITPRICEVALUE = new AttributeType<>("unitPriceValue");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.skuId</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> SKUID = new AttributeType<>("skuId");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.taxCategory</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> TAXCATEGORY = new AttributeType<>("taxCategory");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.orderLineId</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> ORDERLINEID = new AttributeType<>("orderLineId");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.pickupStoreId</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> PICKUPSTOREID = new AttributeType<>("pickupStoreId");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.quantityValue</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, Integer> QUANTITYVALUE = new AttributeType<>("quantityValue");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.orderLineAttributes</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineAttributeData>> ORDERLINEATTRIBUTES = new AttributeType<>("orderLineAttributes");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.quantityUnassignedUnitCode</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> QUANTITYUNASSIGNEDUNITCODE = new AttributeType<>("quantityUnassignedUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.note</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> NOTE = new AttributeType<>("note");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.unitPriceCurrencyCode</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> UNITPRICECURRENCYCODE = new AttributeType<>("unitPriceCurrencyCode");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.orderLineQuantities</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineQuantityData>> ORDERLINEQUANTITIES = new AttributeType<>("orderLineQuantities");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.fulfillmentType</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType> FULFILLMENTTYPE = new AttributeType<>("fulfillmentType");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.unitTaxValue</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, Double> UNITTAXVALUE = new AttributeType<>("unitTaxValue");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.myOrder</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, com.hybris.oms.service.managedobjects.order.OrderData> MYORDER = new AttributeType<>("myOrder");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.unitTaxCurrencyCode</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> UNITTAXCURRENCYCODE = new AttributeType<>("unitTaxCurrencyCode");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.locationRoles</code> attribute defined at extension <code>order</code>. */
	CollectionAttributeType<OrderLineData, String> LOCATIONROLES = new CollectionAttributeType<>("locationRoles");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.orderLineStatus</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> ORDERLINESTATUS = new AttributeType<>("orderLineStatus");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.quantityUnitCode</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, String> QUANTITYUNITCODE = new AttributeType<>("quantityUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineData.quantityUnassignedValue</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineData, Integer> QUANTITYUNASSIGNEDVALUE = new AttributeType<>("quantityUnassignedValue");

	/** <i>Generated constant</i> - Index of <code>OrderLineData</code> type defined at extension <code>order</code>. */
	UniqueIndexDiadic<OrderLineData, com.hybris.oms.service.managedobjects.order.OrderData, String> UQ_ORDER_ORDERLINEID = new UniqueIndexDiadic<>("UQ_Order_OrderLineId", OrderLineData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.note</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Note about the order line.
	 * 
	 * @return the note
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getNote();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.myOrder</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * A handle to the parent order.
	 * 
	 * @return the myOrder
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.order.OrderData getMyOrder();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.orderLineId</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line id.
	 * 
	 * @return the orderLineId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getOrderLineId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.orderLineQuantities</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line details.
	 * 
	 * @return the orderLineQuantities
	 */
	java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineQuantityData> getOrderLineQuantities();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.orderLineStatus</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * order line status.
	 * 
	 * @return the orderLineStatus
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getOrderLineStatus();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.quantityUnassignedUnitCode</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity still not assigned.
	 * 
	 * @return the quantityUnassignedUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getQuantityUnassignedUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.quantityUnassignedValue</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity still not assigned.
	 * 
	 * @return the quantityUnassignedValue
	 */
	@javax.validation.constraints.NotNull
	int getQuantityUnassignedValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.quantityUnitCode</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity.
	 * 
	 * @return the quantityUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getQuantityUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.quantityValue</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity.
	 * 
	 * @return the quantityValue
	 */
	@javax.validation.constraints.NotNull
	int getQuantityValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.skuId</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line item being ordered sku Id..
	 * 
	 * @return the skuId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getSkuId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.taxCategory</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the taxCategory
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getTaxCategory();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.unitPriceCurrencyCode</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line item price value.
	 * 
	 * @return the unitPriceCurrencyCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getUnitPriceCurrencyCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.unitPriceValue</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line item price value.
	 * 
	 * @return the unitPriceValue
	 */
	@javax.validation.constraints.NotNull
	double getUnitPriceValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.unitTaxCurrencyCode</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line item tax value.
	 * 
	 * @return the unitTaxCurrencyCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getUnitTaxCurrencyCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.unitTaxValue</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line item tax value.
	 * 
	 * @return the unitTaxValue
	 */
	@javax.validation.constraints.NotNull
	double getUnitTaxValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.pickupStoreId</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Store id.
	 * 
	 * @return the pickupStoreId
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getPickupStoreId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.locationRoles</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Location Roles (shipping, pickup).
	 * 
	 * @return the locationRoles
	 */
	@javax.validation.constraints.NotNull
	java.util.Set<java.lang.String> getLocationRoles();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.orderLineAttributes</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line attributes.
	 * 
	 * @return the orderLineAttributes
	 */
	java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineAttributeData> getOrderLineAttributes();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.fulfillmentType</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Fulfillment Type for orderline(REGULAR, PRE_ORDERLINE, BACK_ORDERLINE).
	 * 
	 * @return the fulfillmentType
	 */
	com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType getFulfillmentType();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineData.version</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.note</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Note about the order line.
	 *
	 * @param value the note
	 */
	void setNote(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.myOrder</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * A handle to the parent order.
	 *
	 * @param value the myOrder
	 */
	void setMyOrder(final com.hybris.oms.service.managedobjects.order.OrderData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.orderLineId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line id.
	 *
	 * @param value the orderLineId
	 */
	void setOrderLineId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.orderLineQuantities</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line details.
	 *
	 * @param value the orderLineQuantities
	 */
	void setOrderLineQuantities(final java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineQuantityData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.orderLineStatus</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * order line status.
	 *
	 * @param value the orderLineStatus
	 */
	void setOrderLineStatus(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.quantityUnassignedUnitCode</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity still not assigned.
	 *
	 * @param value the quantityUnassignedUnitCode
	 */
	void setQuantityUnassignedUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.quantityUnassignedValue</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity still not assigned.
	 *
	 * @param value the quantityUnassignedValue
	 */
	void setQuantityUnassignedValue(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.quantityUnitCode</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity.
	 *
	 * @param value the quantityUnitCode
	 */
	void setQuantityUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.quantityValue</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity.
	 *
	 * @param value the quantityValue
	 */
	void setQuantityValue(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.skuId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line item being ordered sku Id..
	 *
	 * @param value the skuId
	 */
	void setSkuId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.taxCategory</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the taxCategory
	 */
	void setTaxCategory(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.unitPriceCurrencyCode</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line item price value.
	 *
	 * @param value the unitPriceCurrencyCode
	 */
	void setUnitPriceCurrencyCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.unitPriceValue</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line item price value.
	 *
	 * @param value the unitPriceValue
	 */
	void setUnitPriceValue(final double value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.unitTaxCurrencyCode</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line item tax value.
	 *
	 * @param value the unitTaxCurrencyCode
	 */
	void setUnitTaxCurrencyCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.unitTaxValue</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line item tax value.
	 *
	 * @param value the unitTaxValue
	 */
	void setUnitTaxValue(final double value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.pickupStoreId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Store id.
	 *
	 * @param value the pickupStoreId
	 */
	void setPickupStoreId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.locationRoles</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Location Roles (shipping, pickup).
	 *
	 * @param value the locationRoles
	 */
	void setLocationRoles(final java.util.Set<java.lang.String> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.orderLineAttributes</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line attributes.
	 *
	 * @param value the orderLineAttributes
	 */
	void setOrderLineAttributes(final java.util.List<com.hybris.oms.service.managedobjects.order.OrderLineAttributeData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.fulfillmentType</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Fulfillment Type for orderline(REGULAR, PRE_ORDERLINE, BACK_ORDERLINE).
	 *
	 * @param value the fulfillmentType
	 */
	void setFulfillmentType(final com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineData.version</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
