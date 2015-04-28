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
 * Generated managedobject class for type OrderLineAttributeData first defined at extension <code>order</code>.
 * <p/>
 * Contains Orderline Attribute information.
 */
public interface OrderLineAttributeData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "OrderLineAttributeData";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineAttributeData.description</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineAttributeData, String> DESCRIPTION = new AttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineAttributeData.orderLine</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineAttributeData, com.hybris.oms.service.managedobjects.order.OrderLineData> ORDERLINE = new AttributeType<>("orderLine");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineAttributeData.version</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineAttributeData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineAttributeData.attributeId</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineAttributeData, String> ATTRIBUTEID = new AttributeType<>("attributeId");


	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineAttributeData.attributeId</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Attribute id.
	 * 
	 * @return the attributeId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getAttributeId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineAttributeData.description</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Attribute description.
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineAttributeData.orderLine</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * A handle to the parent order line..
	 * 
	 * @return the orderLine
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.order.OrderLineData getOrderLine();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineAttributeData.version</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineAttributeData.attributeId</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Attribute id.
	 *
	 * @param value the attributeId
	 */
	void setAttributeId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineAttributeData.description</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Attribute description.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineAttributeData.orderLine</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * A handle to the parent order line..
	 *
	 * @param value the orderLine
	 */
	void setOrderLine(final com.hybris.oms.service.managedobjects.order.OrderLineData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineAttributeData.version</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
