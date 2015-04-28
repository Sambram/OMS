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

import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.SortDirection;

import java.util.List;

import org.apache.commons.lang.StringUtils;


public class LocationQueryObject extends QueryObject<LocationQuerySortSupport>
{
	private static final long serialVersionUID = 4724786400164693243L;

	private static final String LOCATION_ID = "locationId";

	public LocationQueryObject()
	{
		this.setSorting(LocationQuerySortSupport.DEFAULT, SortDirection.ASC);
	}

	public LocationQueryObject(final String locationId, final String locationName, final String priority,
			final Integer pageNumber, final Integer pageSize)
	{
		this();

		if (!StringUtils.isEmpty(locationId))
		{
			this.setLocationId(locationId);
		}

		if (!StringUtils.isEmpty(locationName))
		{
			this.setLocationName(locationName);
		}

		if (!StringUtils.isEmpty(priority))
		{
			this.setPriority(priority);
		}

		buildPageNumberAndSize(pageNumber, pageSize);
	}

	public String getLocationId()
	{
		return this.getValue(LOCATION_ID);
	}

	public final void setLocationId(final String locationId)
	{
		this.setValue(LOCATION_ID, locationId);
	}

	public String getLocationName()
	{
		return super.getValue("locationName");
	}

	public final void setLocationName(final String locationName)
	{
		super.setValue("locationName", locationName);
	}

	public String getPriority()
	{
		return super.getValue("priority");
	}

	public final void setPriority(final String priority)
	{
		super.setValue("priority", priority);
	}

	public final List<String> getBaseStores()
	{
		return super.getValues("baseStore");
	}

	public final void setBaseStores(final List<String> baseStores)
	{
		super.setValues("baseStore", baseStores);
	}

	public final List<String> getCountries()
	{
		return super.getValues("country");
	}

	public final List<String> getLocationIds()
	{
		return super.getValues("locationId");
	}

	public final void setCountries(final List<String> countries)
	{
		super.setValues("country", countries);
	}

	public final void setLocationIds(final List<String> locationIds)
	{
		super.setValues("locationId", locationIds);
	}

	public final List<String> getLocationRoles()
	{
		return super.getValues("locationRole");
	}

	public final void setLocationRoles(final List<String> locationRoles)
	{
		super.setValues("locationRole", locationRoles);
	}
}
