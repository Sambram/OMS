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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import com.hybris.oms.domain.inventory.CurrentItemQuantity;
import com.hybris.oms.domain.inventory.CurrentItemQuantityStatus;


/**
 * The Class ItemQuantityCurrentMapElements.
 */
public class ItemQuantityCurrentMapElements implements Serializable
{
	private static final long serialVersionUID = 8324028570448700480L;

	private CurrentItemQuantityStatus key;

	private CurrentItemQuantity value;

	/**
	 * Instantiates a new item quantity current map elements.
	 * 
	 * @param key
	 *           the key
	 * @param value
	 *           the value
	 */
	public ItemQuantityCurrentMapElements(final CurrentItemQuantityStatus key, final CurrentItemQuantity value)
	{
		this.key = key;
		this.value = value;
	}

	@SuppressWarnings("unused")
	private ItemQuantityCurrentMapElements()
	{
		// Required by JAXB, don't remove
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	@XmlElement
	public CurrentItemQuantityStatus getKey()
	{
		return this.key;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	@XmlElement
	public CurrentItemQuantity getValue()
	{
		return this.value;
	}


	/**
	 * Sets the key.
	 * 
	 * @param key the key to set
	 */
	public void setKey(final CurrentItemQuantityStatus key)
	{
		this.key = key;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the value to set
	 */
	public void setValue(final CurrentItemQuantity value)
	{
		this.value = value;
	}
}
