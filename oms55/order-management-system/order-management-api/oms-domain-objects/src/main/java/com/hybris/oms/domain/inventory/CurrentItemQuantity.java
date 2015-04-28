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
package com.hybris.oms.domain.inventory;

import com.hybris.oms.domain.types.Quantity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class CurrentItemQuantity.
 * 
 * @author fsavard
 * @version 1.0
 * @created 17-May-2012 2:49:14 PM
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrentItemQuantity extends ItemQuantity
{
	private static final long serialVersionUID = -3270552547564530122L;

	private Quantity quantity;

	/**
	 * Instantiates a new current item quantity.
	 */
	public CurrentItemQuantity()
	{
		//
	}

	/**
	 * Instantiates a new current item quantity.
	 * 
	 * @param quantity the quantity
	 */
	public CurrentItemQuantity(final Quantity quantity)
	{
		super();
		this.quantity = quantity;
	}

	@Override
	public final Quantity getQuantity()
	{
		return this.quantity;
	}

	@Override
	public final void setQuantity(final Quantity quantity)
	{
		this.quantity = quantity;
	}

	@Override
	public String toString()
	{
		return "CurrentItemQuantity [quantity=" + this.getQuantity() + ']';
	}

}
