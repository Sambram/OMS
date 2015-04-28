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
 
package com.hybris.oms.service.managedobjects.inventory;

import com.hybris.kernel.api.*;

import com.hybris.kernel.api.ManagedObject;

    
/**
 * Generated managedobject class for type ItemLocationData first defined at extension <code>inventory</code>.
 * <p/>
 * Abstract class for inventory.
 */
public interface ItemLocationData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ItemLocationData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemLocationData.version</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemLocationData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>ItemLocationData.itemQuantities</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemLocationData, java.util.List<com.hybris.oms.service.managedobjects.inventory.ItemQuantityData>> ITEMQUANTITIES = new AttributeType<>("itemQuantities");
	/** <i>Generated constant</i> - Attribute key of <code>ItemLocationData.itemId</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemLocationData, String> ITEMID = new AttributeType<>("itemId");
	/** <i>Generated constant</i> - Attribute key of <code>ItemLocationData.banned</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemLocationData, Boolean> BANNED = new AttributeType<>("banned");
	/** <i>Generated constant</i> - Attribute key of <code>ItemLocationData.bin</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemLocationData, com.hybris.oms.service.managedobjects.inventory.BinData> BIN = new AttributeType<>("bin");
	/** <i>Generated constant</i> - Attribute key of <code>ItemLocationData.stockroomLocation</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemLocationData, com.hybris.oms.service.managedobjects.inventory.StockroomLocationData> STOCKROOMLOCATION = new AttributeType<>("stockroomLocation");
	/** <i>Generated constant</i> - Attribute key of <code>ItemLocationData.future</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemLocationData, Boolean> FUTURE = new AttributeType<>("future");

	/** <i>Generated constant</i> - Index of <code>ItemLocationData</code> type defined at extension <code>inventory</code>. */
	UniqueIndexMultiple<ItemLocationData> UQ_ITEMLOC_SKULOCFUT = new UniqueIndexMultiple<>("UQ_ItemLoc_skuLocFut", ItemLocationData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ItemLocationData.itemId</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Inventory item sku id.
	 * 
	 * @return the itemId
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getItemId();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemLocationData.stockroomLocation</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Inventory location.
	 * 
	 * @return the stockroomLocation
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.inventory.StockroomLocationData getStockroomLocation();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemLocationData.future</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * false means current item location.
	 * 
	 * @return the future
	 */
	boolean isFuture();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemLocationData.itemQuantities</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * quantities.
	 * 
	 * @return the itemQuantities
	 */
	java.util.List<com.hybris.oms.service.managedobjects.inventory.ItemQuantityData> getItemQuantities();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemLocationData.bin</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Bin.
	 * 
	 * @return the bin
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.inventory.BinData getBin();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemLocationData.banned</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * true means the location will not be used to source from.
	 * 
	 * @return the banned
	 */
	@javax.validation.constraints.NotNull
	boolean isBanned();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemLocationData.version</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ItemLocationData.itemId</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Inventory item sku id.
	 *
	 * @param value the itemId
	 */
	void setItemId(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemLocationData.stockroomLocation</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Inventory location.
	 *
	 * @param value the stockroomLocation
	 */
	void setStockroomLocation(final com.hybris.oms.service.managedobjects.inventory.StockroomLocationData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemLocationData.future</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * false means current item location.
	 *
	 * @param value the future
	 */
	void setFuture(final boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemLocationData.itemQuantities</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * quantities.
	 *
	 * @param value the itemQuantities
	 */
	void setItemQuantities(final java.util.List<com.hybris.oms.service.managedobjects.inventory.ItemQuantityData> value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemLocationData.bin</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Bin.
	 *
	 * @param value the bin
	 */
	void setBin(final com.hybris.oms.service.managedobjects.inventory.BinData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemLocationData.banned</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * true means the location will not be used to source from.
	 *
	 * @param value the banned
	 */
	void setBanned(final boolean value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemLocationData.version</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
