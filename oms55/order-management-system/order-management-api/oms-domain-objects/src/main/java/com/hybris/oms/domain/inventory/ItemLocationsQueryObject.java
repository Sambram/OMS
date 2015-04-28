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

import com.hybris.oms.domain.QueryCriteria;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.QuerySorting;
import com.hybris.oms.domain.SortDirection;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * A query object to be used when searching for {@link ItemLocation}s using different criteria.
 */
public class ItemLocationsQueryObject extends QueryObject<ItemLocationQuerySupport>
{
	private static final long serialVersionUID = 4724786400164693243L;

	private static final String LOCATION_ID = "locationId";
	private static final String ITEM_ID = "skuId";

	public ItemLocationsQueryObject()
	{
		this.setSorting(ItemLocationQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public ItemLocationsQueryObject(final List<String> skuIds, final List<String> locationIds)
	{
		this();
		if (CollectionUtils.isNotEmpty(skuIds))
		{
			this.setSkuIds(skuIds);
		}

		if (CollectionUtils.isNotEmpty(locationIds))
		{
			this.setLocationIds(locationIds);
		}
	}

	public ItemLocationsQueryObject(final List<QueryCriteria> criteriaList, final Integer pageNumber, final Integer pageSize,
			final QuerySorting sorting)
	{
		super(criteriaList, pageNumber, pageSize, sorting);
		if (sorting == null)
		{
			this.setSorting(ItemLocationQuerySupport.DEFAULT, SortDirection.ASC);
		}
	}

	public List<String> getLocationIds()
	{
		return super.getValues(LOCATION_ID);
	}

	public final void setLocationIds(final List<String> locationIds)
	{
		if (CollectionUtils.isNotEmpty(locationIds))
		{
			this.setValues(LOCATION_ID, locationIds);
		}
	}


	public List<String> getSkuIds()
	{
		return super.getValues(ITEM_ID);
	}

	public final void setSkuIds(final List<String> skuIds)
	{
		if (CollectionUtils.isNotEmpty(skuIds))
		{
			this.setValues(ITEM_ID, skuIds);
		}
	}

}
