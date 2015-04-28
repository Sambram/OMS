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

import com.hybris.oms.domain.inventory.FutureItemQuantity;
import com.hybris.oms.domain.inventory.FutureItemQuantityStatus;


/**
 * The Class ItemQuantityFutureMapAdapter.
 */
public class ItemQuantityFutureMapAdapter extends
		XmlAdapter<ItemQuantityFutureMapElements[], Map<FutureItemQuantityStatus, FutureItemQuantity>>
{
	@Override
	public ItemQuantityFutureMapElements[] marshal(final Map<FutureItemQuantityStatus, FutureItemQuantity> arg0)
	{
		final ItemQuantityFutureMapElements[] mapElements = new ItemQuantityFutureMapElements[arg0.size()];
		int count = 0;
		for (final Map.Entry<FutureItemQuantityStatus, FutureItemQuantity> entry : arg0.entrySet())
		{
			mapElements[count++] = new ItemQuantityFutureMapElements(entry.getKey(), entry.getValue());
		}

		return mapElements;
	}


	@Override
	public Map<FutureItemQuantityStatus, FutureItemQuantity> unmarshal(final ItemQuantityFutureMapElements[] arg0)
	{
		final Map<FutureItemQuantityStatus, FutureItemQuantity> map = new HashMap<FutureItemQuantityStatus, FutureItemQuantity>();
		for (final ItemQuantityFutureMapElements mapelement : arg0)
		{
			map.put(mapelement.getKey(), mapelement.getValue());
		}
		return map;
	}
}
