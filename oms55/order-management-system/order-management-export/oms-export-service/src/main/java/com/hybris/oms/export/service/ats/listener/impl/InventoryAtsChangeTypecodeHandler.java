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
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;

import java.util.Map;


/**
 * Abstract implementation of {@link AtsChangeTypecodeHandler} for typecode represented by {@link ItemQuantityData}.
 */
public abstract class InventoryAtsChangeTypecodeHandler<T extends ItemQuantityData> extends AtsChangeTypecodeHandler<T>
{

	@Override
	public String getSku(final Map<String, Object> values) throws AtsChangeListenerException
	{
		final HybrisId id = (HybrisId) values.get(ItemQuantityData.OWNER.name());
		try
		{
			final ItemLocationData itemLocation = getPersistenceManager().get(id);
			return itemLocation.getItemId();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new AtsChangeListenerException("Sku not found.", e);
		}
	}

	@Override
	public String getLocationId(final Map<String, Object> values) throws AtsChangeListenerException
	{
		final HybrisId id = (HybrisId) values.get(ItemQuantityData.OWNER.name());
		try
		{
			final ItemLocationData itemLocation = getPersistenceManager().get(id);
			return itemLocation.getStockroomLocation().getLocationId();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new AtsChangeListenerException("Sku not found.", e);
		}
	}

}
