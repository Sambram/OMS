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
 * Generated managedobject class for type ItemStatusData first defined at extension <code>inventory</code>.
 * <p/>
 * Contains description for inventory status.
 */
public interface ItemStatusData extends ManagedObject, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "ItemStatusData";
	
	/** <i>Generated constant</i> - Attribute key of <code>ItemStatusData.statusCode</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemStatusData, String> STATUSCODE = new AttributeType<>("statusCode");
	/** <i>Generated constant</i> - Attribute key of <code>ItemStatusData.description</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemStatusData, String> DESCRIPTION = new AttributeType<>("description");
	/** <i>Generated constant</i> - Attribute key of <code>ItemStatusData.version</code> attribute defined at extension <code>inventory</code>. */
	AttributeType<ItemStatusData, Long> VERSION = new AttributeType<>("version");

	/** <i>Generated constant</i> - Index of <code>ItemStatusData</code> type defined at extension <code>inventory</code>. */
	UniqueIndexSingle<ItemStatusData, String> UX_ITEMSTATUSES_STATUSCODE = new UniqueIndexSingle<>("UX_itemStatuses_statusCode", ItemStatusData.class);

	/**
	 * <i>Generated method</i> - Getter of the <code>ItemStatusData.statusCode</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Inventory status code.
	 * 
	 * @return the statusCode
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getStatusCode();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemStatusData.description</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * Inventory status description.
	 * 
	 * @return the description
	 */
	@javax.validation.constraints.NotNull
	@javax.validation.constraints.Size(max=255)
	java.lang.String getDescription();
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemStatusData.version</code> attribute defined at extension <code>inventory</code>.
	 * <p/>
	 * .
	 * 
	 * @return the version
	 */
	java.lang.Long getVersion();
	

	/**
	 * <i>Generated method</i> - Setter of <code>ItemStatusData.statusCode</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Inventory status code.
	 *
	 * @param value the statusCode
	 */
	void setStatusCode(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemStatusData.description</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * Inventory status description.
	 *
	 * @param value the description
	 */
	void setDescription(final java.lang.String value);
	
	/**
	 * <i>Generated method</i> - Setter of <code>ItemStatusData.version</code> attribute defined at extension <code>inventory</code>.  
	 * <p/>
	 * .
	 *
	 * @param value the version
	 */
	void setVersion(final java.lang.Long value);
	
}
