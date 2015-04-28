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
 * Generated managedobject class for type BinData first defined at extension <code>inventory</code>.
 * <p/>
 * Contains data for bins.
 */
public interface BinData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "BinData";
	
	/** <i>Generated constant</i> - Attribute key of <code>BinData.version</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<BinData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>BinData.binCode</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<BinData, String> BINCODE = new AttributeType<>("binCode");
	/** <i>Generated constant</i> - Attribute key of <code>BinData.description</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<BinData, String> DESCRIPTION = new AttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>BinData.priority</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<BinData, Integer> PRIORITY = new AttributeType<>("priority");
	/** <i>Generated constant</i> - Attribute key of <code>BinData.stockroomLocation</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<BinData, com.hybris.oms.service.managedobjects.inventory.StockroomLocationData> STOCKROOMLOCATION = new AttributeType<>("stockroomLocation");

	/** <i>Generated constant</i> - Index of <code>BinData</code> type defined at extension <code>inventory</code>. */
	UniqueIndexDiadic<BinData, String, com.hybris.oms.service.managedobjects.inventory.StockroomLocationData> UQ_BIN_BINCODESKULOC = new UniqueIndexDiadic<>("UQ_Bin_binCodeSkuLoc", BinData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>BinData.binCode</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Location id.
	 * 
	 * @return the binCode
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getBinCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BinData.stockroomLocation</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * The location that this bin is located in.
	 * 
	 * @return the stockroomLocation
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.inventory.StockroomLocationData getStockroomLocation();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BinData.priority</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Priority.
	 * 
	 * @return the priority
	 */
	int getPriority();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BinData.description</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Location description.
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BinData.version</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>BinData.binCode</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Location id.
	 *
	 * @param value the binCode
	 */
	void setBinCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>BinData.stockroomLocation</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * The location that this bin is located in.
	 *
	 * @param value the stockroomLocation
	 */
	void setStockroomLocation(final com.hybris.oms.service.managedobjects.inventory.StockroomLocationData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>BinData.priority</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Priority.
	 *
	 * @param value the priority
	 */
	void setPriority(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>BinData.description</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Location description.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>BinData.version</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
