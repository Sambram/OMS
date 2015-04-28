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
 * Generated managedobject class for type OrderLineQuantityStatusData first defined at extension <code>order</code>.
 * <p/>
 * Contains data for inventory status.
 */
public interface OrderLineQuantityStatusData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "OrderLineQuantityStatusData";
	
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityStatusData.active</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityStatusData, Boolean> ACTIVE = new AttributeType<>("active");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityStatusData.description</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityStatusData, String> DESCRIPTION = new AttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityStatusData.statusCode</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityStatusData, String> STATUSCODE = new AttributeType<>("statusCode");
	/** <i>Generated constant</i> - Attribute key of <code>OrderLineQuantityStatusData.version</code> attribute defined at extension <code>order</code>. */
	AttributeType<OrderLineQuantityStatusData, Long> VERSION = new AttributeType<>("version");

	/** <i>Generated constant</i> - Index of <code>OrderLineQuantityStatusData</code> type defined at extension <code>order</code>. */
	UniqueIndexSingle<OrderLineQuantityStatusData, String> UX_ORDERLINEQUANTITYSTATUSES_STATUSCODE = new UniqueIndexSingle<>("UX_orderLineQuantityStatuses_statusCode", OrderLineQuantityStatusData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityStatusData.active</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity status activity indicator.
	 * 
	 * @return the active
	 */
	java.lang.Boolean getActive();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityStatusData.statusCode</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity status code.
	 * 
	 * @return the statusCode
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getStatusCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityStatusData.description</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * Order line quantity status description.
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>OrderLineQuantityStatusData.version</code> attribute defined at extension <code>order</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityStatusData.active</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity status activity indicator.
	 *
	 * @param value the active
	 */
	void setActive(final java.lang.Boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityStatusData.statusCode</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity status code.
	 *
	 * @param value the statusCode
	 */
	void setStatusCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityStatusData.description</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * Order line quantity status description.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>OrderLineQuantityStatusData.version</code> attribute defined at extension <code>order</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
