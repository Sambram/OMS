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

import com.hybris.oms.domain.adapter.ItemQuantityFutureMapAdapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity holds information about incoming supply (reserved quantities,
 * amount on backorder, and amount allocated to backorders for items).
 * 
 * @author fsavard
 * @version 1.0
 * @created 17-May-2012 2:49:11 PM
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemLocationFuture extends ItemLocation
{
	private static final long serialVersionUID = -2477071152367974547L;

	@XmlJavaTypeAdapter(ItemQuantityFutureMapAdapter.class)
	private Map<FutureItemQuantityStatus, FutureItemQuantity> itemQuantities = new HashMap<>();

	/**
	 * Instantiates a new item location future.
	 */
	public ItemLocationFuture()
	{
		//
	}

	/**
	 * Instantiates a new item location future.
	 * 
	 * @param itemId the item id
	 * @param location the location
	 */
	public ItemLocationFuture(final String itemId, final Location location)
	{
		super();
		this.setItemId(itemId);
		this.setLocation(location);
	}

	/**
	 * Adds the item quantity.
	 * 
	 * @param quantity the quantity
	 * @param forStatusAndDate the for status and date
	 */
	public void addItemQuantity(final FutureItemQuantity quantity, final FutureItemQuantityStatus forStatusAndDate)
	{
		this.itemQuantities.put(forStatusAndDate, quantity);
	}

	@Override
	public Map<ItemQuantityStatus, ItemQuantity> getItemQuantities()
	{
		final Map<ItemQuantityStatus, ItemQuantity> toreturn = new HashMap<>();
		toreturn.putAll(this.itemQuantities);
		return toreturn;
	}

	/**
	 * Gets the item quantity supply.
	 * 
	 * @param status the status
	 * @return the item quantity supply
	 */
	public ItemQuantity getItemQuantitySupply(final ItemQuantityStatus status)
	{
		return this.itemQuantities.get(status);
	}

	/**
	 * Sets the item quantities.
	 * 
	 * @param itemQuantities the item quantities
	 */
	public void setItemQuantities(final Map<FutureItemQuantityStatus, FutureItemQuantity> itemQuantities)
	{
		this.itemQuantities = itemQuantities;
	}

	@Override
	public String toString()
	{
		return "ItemLocationFuture [itemId=" + this.getItemId() + ", itemQuantities=" + this.itemQuantities + ", location="
				+ this.getLocation() + ", bin=" + this.getBin() + ']';
	}
}
