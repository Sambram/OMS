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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class ItemLocationList.
 * 
 * @author SRNaidu
 */
@XmlRootElement(name = "itemLocations")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemLocationList implements Serializable
{
	private static final long serialVersionUID = 3409893665446914805L;
	/**
	 * The item locations.
	 */
	@XmlElement(name = "itemLocation")
	private List<ItemLocation> itemLocations = new ArrayList<>();

	/**
	 * Adds the item location.
	 * 
	 * @param itemLocation the item location
	 */
	public void addItemLocation(final ItemLocation itemLocation)
	{
		if (this.itemLocations == null)
		{
			this.itemLocations = new ArrayList<>();
		}
		this.itemLocations.add(itemLocation);
	}

	/**
	 * Gets the number of item locations.
	 * 
	 * @return the number of item locations
	 */
	public int getNumberOfItemLocations()
	{
		return this.itemLocations.size();
	}

	/**
	 * Gets the item locations.
	 * 
	 * @return the item locations
	 */
	public List<ItemLocation> getItemLocations()
	{
		return Collections.unmodifiableList(this.itemLocations);
	}

	/**
	 * Initialize item locations.
	 * 
	 * @param newItemLocations the item locations
	 */
	public void initializeItemLocations(final List<ItemLocation> newItemLocations)
	{
		assert this.itemLocations.isEmpty();
		for (final ItemLocation itemLocation : newItemLocations)
		{
			this.addItemLocation(itemLocation);
		}
	}

	/**
	 * Removes the item location.
	 * 
	 * @param itemLocation the item location
	 */
	public void removeItemLocation(final ItemLocation itemLocation)
	{
		this.itemLocations.remove(itemLocation);
	}
}
