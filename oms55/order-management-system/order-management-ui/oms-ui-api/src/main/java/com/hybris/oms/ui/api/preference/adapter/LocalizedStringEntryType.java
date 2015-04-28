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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


// @XmlAccessorType(XmlAccessType.FIELD)

/**
 * The Class LocalizedStringEntryType.
 */
@XmlRootElement(name = "entry")
public class LocalizedStringEntryType
{

	/**
	 * The key.
	 */
	private String key;

	/**
	 * The value.
	 */
	private String value;

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	@XmlAttribute
	public String getKey()
	{
		return this.key;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	@XmlAttribute
	public String getValue()
	{
		return this.value;
	}

	/**
	 * Sets the key.
	 * 
	 * @param key the new key
	 */
	public void setKey(final String key)
	{
		this.key = key;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the new value
	 */
	public void setValue(final String value)
	{
		this.value = value;
	}
}
