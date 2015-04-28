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

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class FutureItemQuantity.
 * 
 * @author fsavard
 * @version 1.0
 * @created 17-May-2012 2:49:14 PM
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FutureItemQuantity extends ItemQuantity
{
	private static final long serialVersionUID = -3270552547564530122L;

	private Date expectedDeliveryDate;

	private Quantity quantity;

	/**
	 * Instantiates a new future item quantity.
	 */
	public FutureItemQuantity()
	{
		//
	}

	/**
	 * Instantiates a new future item quantity.
	 * 
	 * @param quantity the quantity
	 */
	public FutureItemQuantity(final Quantity quantity)
	{
		super();
		this.quantity = quantity;
	}

	public Date getExpectedDeliveryDate()
	{
		if (this.expectedDeliveryDate == null)
		{
			return null;
		}
		return new Date(this.expectedDeliveryDate.getTime());
	}

	@Override
	public final Quantity getQuantity()
	{
		return this.quantity;
	}

	public void setExpectedDeliveryDate(final Date expectedDeliveryDate)
	{
		if (expectedDeliveryDate == null)
		{
			this.expectedDeliveryDate = null;
		}
		else
		{
			this.expectedDeliveryDate = new Date(expectedDeliveryDate.getTime());
		}
	}

	@Override
	public final void setQuantity(final Quantity quantity)
	{
		this.quantity = quantity;
		// CHECKSTYLE IGNORE StrictDuplicateCode NEXT 18 LINES
	}

	@Override
	public String toString()
	{
		return "ItemQuantity [quantity=" + this.getQuantity() + "expectedDeliveryDate=" + this.getExpectedDeliveryDate() + ']';
	}

}
