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
 * The Class StockRoomLocationList.
 * 
 * @author SRNaidu
 */
@XmlRootElement(name = "StockRoomLocations")
@XmlAccessorType(XmlAccessType.FIELD)
public class StockRoomLocationList implements Serializable
{
	private static final long serialVersionUID = -5397368552378341651L;

	@XmlElement(name = "location")
	private List<Location> locations = new ArrayList<>();

	/**
	 * Adds the location.
	 * 
	 * @param location the location
	 */
	public void addLocation(final Location location)
	{
		if (this.locations == null)
		{
			this.locations = new ArrayList<>();
		}
		this.locations.add(location);
	}

	public List<Location> getLocations()
	{
		return this.locations;
	}

	/**
	 * Gets the number of locations.
	 * 
	 * @return the number of locations
	 */
	public int getNumberOfLocations()
	{
		return this.locations.size();
	}

	/**
	 * Initialize locations.
	 * 
	 * @param newLocations the locations
	 */
	public void initializeLocations(final List<Location> newLocations)
	{
		assert this.locations.isEmpty();
		for (final Location location : newLocations)
		{
			this.addLocation(location);
		}
	}

	/**
	 * Removes the location.
	 * 
	 * @param location the location
	 */
	public void removeLocation(final Location location)
	{
		this.locations.remove(location);
	}

	public void setLocationForJaxb(final List<Location> location)
	{
		this.locations = location;
	}

}
