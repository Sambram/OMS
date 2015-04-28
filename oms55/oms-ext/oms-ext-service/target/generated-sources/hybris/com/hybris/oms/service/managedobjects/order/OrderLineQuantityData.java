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
 * Generated managedobject class for type OrderLineQuantityData first defined at extension <code>order</code>.
 * <p/>
 * Contains data on each order line quantity.
 */
public interface OrderLineQuantityData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "OrderLineQuantityData";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityData.version</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityData.status</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityData, com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData> STATUS = new AttributeType<>("status");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityData.orderLine</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityData, com.hybris.oms.service.managedobjects.order.OrderLineData> ORDERLINE = new AttributeType<>("orderLine");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityData.quantityUnitCode</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityData, String> QUANTITYUNITCODE = new AttributeType<>("quantityUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityData.stockroomLocationId</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityData, String> STOCKROOMLOCATIONID = new AttributeType<>("stockroomLocationId");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityData.quantityValue</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityData, Integer> QUANTITYVALUE = new AttributeType<>("quantityValue");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityData.shipment</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityData, com.hybris.oms.service.managedobjects.shipment.ShipmentData> SHIPMENT = new AttributeType<>("shipment");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityData.olqId</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityData, Long> OLQID = new AttributeType<>("olqId");

	/** <i>Generated constant</i> - Index of <code>OrderLineQuantityData</code> type defined at extension <code>order</code>. */
	UniqueIndexSingle<OrderLineQuantityData, Long> UX_ORDERLINEQUANTITIES_OLQID = new UniqueIndexSingle<>("UX_orderLineQuantities_olqId", OrderLineQuantityData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityData.stockroomLocationId</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity location.
	 * 
	 * @return the stockroomLocationId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getStockroomLocationId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityData.olqId</code> attribute defined at extension <code>order</code>.
	* <b>Value for this attribute is auto-generated at entity creation time using available sequence generator!</b>
	 * <p/>
	 * Unique order line quantity identifier.
	 * 
	 * @return the olqId
	 */
	@javax.validation.constraints.NotNull
	long getOlqId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityData.orderLine</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * A handle to the parent order line..
	 * 
	 * @return the orderLine
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.order.OrderLineData getOrderLine();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityData.quantityUnitCode</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity quantity.
	 * 
	 * @return the quantityUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getQuantityUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityData.quantityValue</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity quantity.
	 * 
	 * @return the quantityValue
	 */
	@javax.validation.constraints.NotNull
	int getQuantityValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityData.shipment</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * A handle to a shipment..
	 * 
	 * @return the shipment
	 */
	com.hybris.oms.service.managedobjects.shipment.ShipmentData getShipment();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityData.status</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity status.
	 * 
	 * @return the status
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData getStatus();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityData.version</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityData.stockroomLocationId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity location.
	 *
	 * @param value the stockroomLocationId
	 */
	void setStockroomLocationId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityData.olqId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Unique order line quantity identifier.
	 *
	 * @param value the olqId
	 */
	void setOlqId(final long value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityData.orderLine</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * A handle to the parent order line..
	 *
	 * @param value the orderLine
	 */
	void setOrderLine(final com.hybris.oms.service.managedobjects.order.OrderLineData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityData.quantityUnitCode</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity quantity.
	 *
	 * @param value the quantityUnitCode
	 */
	void setQuantityUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityData.quantityValue</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity quantity.
	 *
	 * @param value the quantityValue
	 */
	void setQuantityValue(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityData.shipment</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * A handle to a shipment..
	 *
	 * @param value the shipment
	 */
	void setShipment(final com.hybris.oms.service.managedobjects.shipment.ShipmentData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityData.status</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity status.
	 *
	 * @param value the status
	 */
	void setStatus(final com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityData.version</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
