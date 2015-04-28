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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class ItemStatusList.
 * 
 * @author SRNaidu
 */
@XmlRootElement(name = "itemStatuses")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemStatusList implements Serializable
{
	private static final long serialVersionUID = -2579930584720122158L;

	@XmlElement(name = "itemStatus")
	private List<ItemStatus> itemStatuses = new ArrayList<>();

	/**
	 * Adds the item status.
	 * 
	 * @param itemStatus the item status
	 */
	public void addItemStatus(final ItemStatus itemStatus)
	{
		if (this.itemStatuses == null)
		{
			this.itemStatuses = new ArrayList<>();
		}
		this.itemStatuses.add(itemStatus);
	}

	public List<ItemStatus> getItemStatuses()
	{
		return this.itemStatuses;
	}

	/**
	 * Gets the number of item statuses.
	 * 
	 * @return the number of item statuses
	 */
	public int getNumberOfItemStatuses()
	{
		return this.itemStatuses.size();
	}

	/**
	 * Initialize item statuses.
	 * 
	 * @param newItemStatuses the item statuses
	 */
	public void initializeItemStatuses(final List<ItemStatus> newItemStatuses)
	{
		assert this.itemStatuses.isEmpty();
		for (final ItemStatus itemStatus : newItemStatuses)
		{
			this.addItemStatus(itemStatus);
		}
	}

	/**
	 * Removes the item status.
	 * 
	 * @param itemStatus the item status
	 */
	public void removeItemStatus(final ItemStatus itemStatus)
	{
		this.itemStatuses.remove(itemStatus);
	}

	public void setItemStatusesForJaxb(final List<ItemStatus> itemStatus)
	{
		this.itemStatuses = itemStatus;
	}
}
