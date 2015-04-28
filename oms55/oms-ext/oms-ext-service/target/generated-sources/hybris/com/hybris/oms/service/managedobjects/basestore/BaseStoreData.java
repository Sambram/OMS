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
 
package com.hybris.oms.service.managedobjects.basestore;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type BaseStoreData first defined at extension <code>basestore</code>.
 * <p/>
 * Contains data for base store.
 */
public interface BaseStoreData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "BaseStoreData";
	
	/** <i>Generated constant</i> - Attribute key of <code>BaseStoreData.stockroomLocations</code> attribute defined at extension <code>basestore</code>. */
	AttributeType<BaseStoreData, java.util.Set<com.hybris.oms.service.managedobjects.inventory.StockroomLocationData>> STOCKROOMLOCATIONS = new AttributeType<>("stockroomLocations");
	/** <i>Generated constant</i> - Attribute key of <code>BaseStoreData.name</code> attribute defined at extension <code>basestore</code>. */
	AttributeType<BaseStoreData, String> NAME = new AttributeType<>("name");
	/** <i>Generated constant</i> - Attribute key of <code>BaseStoreData.description</code> attribute defined at extension <code>basestore</code>. */
	AttributeType<BaseStoreData, String> DESCRIPTION = new AttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>BaseStoreData.version</code> attribute defined at extension <code>basestore</code>. */
	AttributeType<BaseStoreData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>BaseStoreData.orders</code> attribute defined at extension <code>basestore</code>. */
	AttributeType<BaseStoreData, java.util.Set<com.hybris.oms.service.managedobjects.order.OrderData>> ORDERS = new AttributeType<>("orders");

	/** <i>Generated constant</i> - Index of <code>BaseStoreData</code> type defined at extension <code>basestore</code>. */
	UniqueIndexSingle<BaseStoreData, String> UX_BASESTORES_NAME = new UniqueIndexSingle<>("UX_baseStores_name", BaseStoreData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStoreData.name</code> attribute defined at extension <code>basestore</code>.
	 * <p/>
	 * Base store name.
	 * 
	 * @return the name
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getName();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStoreData.description</code> attribute defined at extension <code>basestore</code>.
	 * <p/>
	 * Base store description.
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStoreData.stockroomLocations</code> attribute defined at extension <code>basestore</code>.
	 * <p/>
	 * locations.
	 * 
	 * @return the stockroomLocations
	 */
	java.util.Set<com.hybris.oms.service.managedobjects.inventory.StockroomLocationData> getStockroomLocations();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStoreData.orders</code> attribute defined at extension <code>basestore</code>.
	 * <p/>
	 * orders.
	 * 
	 * @return the orders
	 */
	java.util.Set<com.hybris.oms.service.managedobjects.order.OrderData> getOrders();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStoreData.version</code> attribute defined at extension <code>basestore</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>BaseStoreData.name</code> attribute defined at extension <code>basestore</code>.  
	 * <p/>
	 * Base store name.
	 *
	 * @param value the name
	 */
	void setName(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseStoreData.description</code> attribute defined at extension <code>basestore</code>.  
	 * <p/>
	 * Base store description.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseStoreData.stockroomLocations</code> attribute defined at extension <code>basestore</code>.  
	 * <p/>
	 * locations.
	 *
	 * @param value the stockroomLocations
	 */
	void setStockroomLocations(final java.util.Set<com.hybris.oms.service.managedobjects.inventory.StockroomLocationData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseStoreData.orders</code> attribute defined at extension <code>basestore</code>.  
	 * <p/>
	 * orders.
	 *
	 * @param value the orders
	 */
	void setOrders(final java.util.Set<com.hybris.oms.service.managedobjects.order.OrderData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>BaseStoreData.version</code> attribute defined at extension <code>basestore</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
