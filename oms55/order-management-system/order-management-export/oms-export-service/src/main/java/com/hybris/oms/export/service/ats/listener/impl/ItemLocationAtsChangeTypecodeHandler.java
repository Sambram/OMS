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
package com.hybris.oms.export.service.ats.listener.impl;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.export.service.ats.listener.AtsChangeListenerException;
import com.hybris.oms.export.service.ats.listener.AtsChangeTypecodeHandler;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.Map;


/**
 * Implementation of {@link AtsChangeTypecodeHandler} for typecode represented by {@link ItemLocationData}.
 */
public class ItemLocationAtsChangeTypecodeHandler extends AtsChangeTypecodeHandler<ItemLocationData>
{

	@Override
	public String getSku(final Map<String, Object> values) throws AtsChangeListenerException
	{
		return (String) values.get(ItemLocationData.ITEMID.name());
	}

	@Override
	public String getLocationId(final Map<String, Object> values) throws AtsChangeListenerException
	{
		final HybrisId id = (HybrisId) values.get(ItemLocationData.STOCKROOMLOCATION.name());
		try
		{
			final StockroomLocationData location = getPersistenceManager().get(id);
			return location.getLocationId();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new AtsChangeListenerException("Location Id not found.", e);
		}
	}

}
