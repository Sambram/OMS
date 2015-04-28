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

import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;

    
/**
 * Generated managedobject class for type CurrentItemQuantityData first defined at extension <code>inventory</code>.
 * <p/>
 * Contains data on the quantity .
 */
public interface CurrentItemQuantityData extends ItemQuantityData, PropertyAware
{
	/**<i>Generated managed object type code constant.</i>*/
	String _TYPECODE = "CurrentItemQuantityData";
	

	/** <i>Generated constant</i> - Index of <code>CurrentItemQuantityData</code> type defined at extension <code>inventory</code>. */
	UniqueIndexTriadic<CurrentItemQuantityData, com.hybris.oms.service.managedobjects.inventory.ItemLocationData, String, java.util.Date> UQ_ITEMQUANTITY_OWNERSTATUSDELIVERY = new UniqueIndexTriadic<>("UQ_ItemQuantity_ownerStatusDelivery", CurrentItemQuantityData.class);


}
