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

import com.hybris.oms.domain.adapter.ItemQuantityCurrentMapAdapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Information about the properties of an Item as they relate to specific
 * quantities and/or specific locations.
 * 
 * @author fsavard
 * @version 1.0
 * @created 12-Mar-2012 3:36:26 PM
 * @updated 17-May-2012 2:47:58 PM
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemLocationCurrent extends ItemLocation
{

	private static final long serialVersionUID = -6711202876235432914L;

	@XmlJavaTypeAdapter(ItemQuantityCurrentMapAdapter.class)
	private Map<CurrentItemQuantityStatus, CurrentItemQuantity> itemQuantities = new HashMap<>();

	/**
	 * Instantiates a new item location current.
	 */
	public ItemLocationCurrent()
	{
		//
	}

	/**
	 * Instantiates a new item location current.
	 * 
	 * @param itemId
	 *           the item id
	 * @param location
	 *           the location
	 */
	public ItemLocationCurrent(final String itemId, final Location location)
	{
		super();
		this.setItemId(itemId);
		this.setLocation(location);
	}

	/**
	 * Adds the item quantity.
	 * 
	 * @param quantity
	 *           the quantity
	 * @param forStatus
	 *           the for status
	 */
	public void addItemQuantity(final CurrentItemQuantity quantity, final CurrentItemQuantityStatus forStatus)
	{
		this.itemQuantities.put(forStatus, quantity);
	}

	@Override
	public Map<ItemQuantityStatus, ItemQuantity> getItemQuantities()
	{
		final Map<ItemQuantityStatus, ItemQuantity> toreturn = new HashMap<>();
		toreturn.putAll(this.itemQuantities);
		return toreturn;
	}

	/**
	 * Gets the item quantity.
	 * 
	 * @param statusCode
	 *           the status code
	 * @return the item quantity
	 */
	public CurrentItemQuantity getItemQuantity(final String statusCode)
	{
		return this.itemQuantities.get(new CurrentItemQuantityStatus(statusCode));
	}

	/**
	 * Checks for quantities.
	 * 
	 * @return the boolean
	 */
	public Boolean hasQuantities()
	{
		if (this.itemQuantities != null)
		{
			for (final CurrentItemQuantityStatus currentItemQuantityStatus : this.itemQuantities.keySet())
			{
				if (this.itemQuantities.get(currentItemQuantityStatus) != null
						&& this.itemQuantities.get(currentItemQuantityStatus).getQuantity().getValue() > 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Sets the item quantities.
	 * 
	 * @param itemQuantities
	 *           the item quantities
	 */
	public void setItemQuantities(final Map<CurrentItemQuantityStatus, CurrentItemQuantity> itemQuantities)
	{
		this.itemQuantities = itemQuantities;
	}

	@Override
	public String toString()
	{
		return "ItemLocationCurrent [itemId=" + this.getItemId() + ", itemQuantities=" + this.itemQuantities + ", location="
				+ this.getLocation() + ", bin=" + this.getBin() + ']';
	}

}
