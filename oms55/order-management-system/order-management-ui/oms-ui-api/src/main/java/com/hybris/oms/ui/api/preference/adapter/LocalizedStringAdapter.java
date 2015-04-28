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
package com.hybris.oms.ui.api.preference.adapter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * The Class LocalizedStringAdapter.
 */
public class LocalizedStringAdapter extends XmlAdapter<LocalizedStringMapType, Map<Locale, String>>
{


	@Override
	public LocalizedStringMapType marshal(final Map<Locale, String> arg0)
	{
		final LocalizedStringMapType myMapType = new LocalizedStringMapType();
		if (arg0 != null)
		{
			for (final Entry<Locale, String> entry : arg0.entrySet())
			{
				final LocalizedStringEntryType myMapEntryType = new LocalizedStringEntryType();
				myMapEntryType.setKey(entry.getKey().toLanguageTag());
				myMapEntryType.setValue(entry.getValue());
				myMapType.getEntry().add(myMapEntryType);
			}
		}
		return myMapType;
	}


	@Override
	public Map<Locale, String> unmarshal(final LocalizedStringMapType arg0)
	{
		final HashMap<Locale, String> hashMap = new HashMap<>();
		for (final LocalizedStringEntryType myEntryType : arg0.getEntry())
		{
			hashMap.put(Locale.forLanguageTag(myEntryType.getKey()), myEntryType.getValue());
		}
		return hashMap;
	}
}
