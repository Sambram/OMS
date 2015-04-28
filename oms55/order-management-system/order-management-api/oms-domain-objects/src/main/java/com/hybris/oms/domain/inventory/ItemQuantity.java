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

import com.hybris.commons.dto.impl.PropertyAwareEntityDto;
import com.hybris.oms.domain.types.Quantity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * The Class ItemQuantity.
 */
@XmlRootElement
@XmlSeeAlso({CurrentItemQuantity.class, FutureItemQuantity.class})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ItemQuantity extends PropertyAwareEntityDto
{
	private static final long serialVersionUID = -5446271967652105802L;

	@XmlElement
	private String id;

	/**
	 * Gets the quantity.
	 * 
	 * @return the quantity
	 */
	public abstract Quantity getQuantity();

	/**
	 * Sets the quantity.
	 * 
	 * @param qty the new quantity
	 */
	public abstract void setQuantity(Quantity qty);

	@Override
	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

}
