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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class LocalizedStringMapType.
 */
@XmlRootElement
public class LocalizedStringMapType
{

	/**
	 * The entry.
	 */
	private List<LocalizedStringEntryType> entry = new ArrayList<>();

	/**
	 * Gets the entry.
	 * 
	 * @return the entry
	 */
	@XmlElement(name = "entry", required = true)
	public List<LocalizedStringEntryType> getEntry()
	{
		return this.entry;
	}

	/**
	 * Sets the entry.
	 * 
	 * @param entry the new entry
	 */
	public final void setEntry(final List<LocalizedStringEntryType> entry)
	{
		this.entry = entry;
	}
}
