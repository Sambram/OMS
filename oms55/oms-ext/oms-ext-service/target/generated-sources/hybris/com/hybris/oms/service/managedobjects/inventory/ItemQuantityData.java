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
 * Generated managedobject class for type ItemQuantityData first defined at extension <code>inventory</code>.
 * <p/>
 * Abstract class for inventory.
 */
public interface ItemQuantityData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ItemQuantityData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemQuantityData.statusCode</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemQuantityData, String> STATUSCODE = new AttributeType<>("statusCode");
	/** <i>Generated constant</i> - Attribute key of <code>ItemQuantityData.quantityValue</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemQuantityData, Integer> QUANTITYVALUE = new AttributeType<>("quantityValue");
	/** <i>Generated constant</i> - Attribute key of <code>ItemQuantityData.version</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemQuantityData, Long> VERSION = new AttributeType<>("version");
	/** <i>Generated constant</i> - Attribute key of <code>ItemQuantityData.expectedDeliveryDate</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemQuantityData, java.util.Date> EXPECTEDDELIVERYDATE = new AttributeType<>("expectedDeliveryDate");
	/** <i>Generated constant</i> - Attribute key of <code>ItemQuantityData.quantityUnitCode</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemQuantityData, String> QUANTITYUNITCODE = new AttributeType<>("quantityUnitCode");
	/** <i>Generated constant</i> - Attribute key of <code>ItemQuantityData.owner</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemQuantityData, com.hybris.oms.service.managedobjects.inventory.ItemLocationData> OWNER = new AttributeType<>("owner");

	/** <i>Generated constant</i> - Index of <code>ItemQuantityData</code> type defined at extension <code>inventory</code>. */
	UniqueIndexTriadic<ItemQuantityData, com.hybris.oms.service.managedobjects.inventory.ItemLocationData, String, java.util.Date> UQ_ITEMQUANTITY_OWNERSTATUSDELIVERY = new UniqueIndexTriadic<>("UQ_ItemQuantity_ownerStatusDelivery", ItemQuantityData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ItemQuantityData.owner</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Item Location Reference.
	 * 
	 * @return the owner
	 */
	@javax.validation.constraints.NotNull
	com.hybris.oms.service.managedobjects.inventory.ItemLocationData getOwner();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemQuantityData.statusCode</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * .
	 * 
	 * @return the statusCode
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getStatusCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemQuantityData.expectedDeliveryDate</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * .
	 * 
	 * @return the expectedDeliveryDate
	 */
	java.util.Date getExpectedDeliveryDate();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemQuantityData.quantityUnitCode</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Quantity.
	 * 
	 * @return the quantityUnitCode
	 */
	@javax.validation.constraints.Size(max=255)
	java.lang.String getQuantityUnitCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemQuantityData.quantityValue</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Quantity.
	 * 
	 * @return the quantityValue
	 */
	@javax.validation.constraints.NotNull
	int getQuantityValue();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemQuantityData.version</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ItemQuantityData.owner</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Item Location Reference.
	 *
	 * @param value the owner
	 */
	void setOwner(final com.hybris.oms.service.managedobjects.inventory.ItemLocationData value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemQuantityData.statusCode</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the statusCode
	 */
	void setStatusCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemQuantityData.expectedDeliveryDate</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the expectedDeliveryDate
	 */
	void setExpectedDeliveryDate(final java.util.Date value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemQuantityData.quantityUnitCode</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Quantity.
	 *
	 * @param value the quantityUnitCode
	 */
	void setQuantityUnitCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemQuantityData.quantityValue</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Quantity.
	 *
	 * @param value the quantityValue
	 */
	void setQuantityValue(final int value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemQuantityData.version</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
