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

import com.hybris.oms.domain.inventory.FutureItemQuantity;
import com.hybris.oms.domain.inventory.FutureItemQuantityStatus;


/**
 * The Class ItemQuantityFutureMapElements.
 */
public class ItemQuantityFutureMapElements implements Serializable
{
	private static final long serialVersionUID = 1194304321160059832L;

	private FutureItemQuantityStatus key;
	private FutureItemQuantity value;

	/**
	 * Instantiates a new item quantity future map elements.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public ItemQuantityFutureMapElements(final FutureItemQuantityStatus key, final FutureItemQuantity value)
	{
		this.setKey(key);
		this.setValue(value);
	}

	@SuppressWarnings("unused")
	private ItemQuantityFutureMapElements()
	{
		// Required by JAXB, don't remove
	}

	@XmlElement
	public FutureItemQuantityStatus getKey()
	{
		return this.key;
	}

	@XmlElement
	public FutureItemQuantity getValue()
	{
		return this.value;
	}

	public final void setKey(final FutureItemQuantityStatus key)
	{
		this.key = key;
	}

	public final void setValue(final FutureItemQuantity value)
	{
		this.value = value;
	}
}
