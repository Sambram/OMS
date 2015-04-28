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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SkuQuantity implements Serializable
{
	private static final long serialVersionUID = 3359257405898064101L;

	private Quantity quantity;
	private String sku;

	/**
	 * Instantiates a new status attribute.
	 */
	public SkuQuantity()
	{
		// default constructor
	}

	public SkuQuantity(final String sku, final Quantity quantity)
	{
		this.sku = sku;
		this.quantity = quantity;
	}

	public Quantity getQuantity()
	{
		return this.quantity;
	}

	public String getSku()
	{
		return this.sku;
	}

	public void setQuantity(final Quantity quantity)
	{
		this.quantity = quantity;
	}

	public void setSku(final String sku)
	{
		this.sku = sku;
	}

	@Override
	public String toString()
	{
		return "SquQuantity [Sku=" + this.sku + ", quantity=" + this.getQuantity() + ']';
	}





}
