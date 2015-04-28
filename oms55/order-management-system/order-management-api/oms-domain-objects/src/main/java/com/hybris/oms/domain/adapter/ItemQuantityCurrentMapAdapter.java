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
package com.hybris.oms.domain.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.hybris.oms.domain.inventory.CurrentItemQuantity;
import com.hybris.oms.domain.inventory.CurrentItemQuantityStatus;


/**
 * The Class ItemQuantityCurrentMapAdapter.
 */
public class ItemQuantityCurrentMapAdapter extends
		XmlAdapter<ItemQuantityCurrentMapElements[], Map<CurrentItemQuantityStatus, CurrentItemQuantity>>
{

	@Override
	public ItemQuantityCurrentMapElements[] marshal(final Map<CurrentItemQuantityStatus, CurrentItemQuantity> arg0)
	{
		final ItemQuantityCurrentMapElements[] mapElements = new ItemQuantityCurrentMapElements[arg0.size()];
		int count = 0;
		for (final Map.Entry<CurrentItemQuantityStatus, CurrentItemQuantity> entry : arg0.entrySet())
		{
			mapElements[count++] = new ItemQuantityCurrentMapElements(entry.getKey(), entry.getValue());
		}

		return mapElements;
	}

	@Override
	public Map<CurrentItemQuantityStatus, CurrentItemQuantity> unmarshal(final ItemQuantityCurrentMapElements[] arg0)
	{
		final Map<CurrentItemQuantityStatus, CurrentItemQuantity> map = new HashMap<>();
		for (final ItemQuantityCurrentMapElements mapelement : arg0)
		{
			map.put(mapelement.getKey(), mapelement.getValue());
		}
		return map;
	}
}
