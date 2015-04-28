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

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * Date: 5/23/12 Time: 4:38 PM.
 */
@XmlRootElement
@XmlSeeAlso({ItemLocationCurrent.class, ItemLocationFuture.class})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ItemLocation extends PropertyAwareEntityDto
{
	private static final long serialVersionUID = 545822795781238290L;

	private String itemId;

	private Location location;

	private boolean future;

	@XmlElement
	private String id;

	private Bin bin;

	/**
	 * Gets the inventory item.
	 * 
	 * @return the inventory item
	 */
	public String getItemId()
	{
		return this.itemId;
	}

	/**
	 * Gets the item quantities.
	 * 
	 * @return the item quantities
	 */
	public abstract Map<ItemQuantityStatus, ItemQuantity> getItemQuantities();

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public Location getLocation()
	{
		return this.location;
	}

	/**
	 * Sets the inventory item.
	 * 
	 * @param itemId the new inventory item
	 */
	public void setItemId(final String itemId)
	{
		this.itemId = itemId;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location the new location
	 */
	public void setLocation(final Location location)
	{

		this.location = location;
	}

	public boolean isFuture()
	{
		return this.future;
	}

	public void setFuture(final boolean future)
	{
		this.future = future;
	}

	@Override
	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public Bin getBin()
	{
		return bin;
	}

	public void setBin(final Bin bin)
	{
		this.bin = bin;
	}

}
