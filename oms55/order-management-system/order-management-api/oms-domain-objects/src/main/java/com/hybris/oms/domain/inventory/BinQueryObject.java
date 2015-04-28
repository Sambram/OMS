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

import org.apache.commons.lang.StringUtils;


/**
 * A query object to be used when searching for orders using different criteria.
 */
public class BinQueryObject extends QueryObject<BinQuerySupport>
{
    private static final long serialVersionUID = 6028250692288484030L;

	private static final String PRIORITY = "priority";
	private static final String BIN_CODE = "binCode";
	private static final String DESCRIPTION = "description";
	private static final String SKU_ID = "skuId";
    private static final String LOCATION_ID = "locationId";

    public BinQueryObject()
	{
		this.setSorting(BinQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public BinQueryObject(final String binCode, final String locationId, final String skuId)
	{
		this();
		if (StringUtils.isNotEmpty(binCode))
		{
			this.setBinCode(binCode);
		}

		if (StringUtils.isNotEmpty(locationId))
		{
			this.setLocationId(locationId);
		}
		if (StringUtils.isNotEmpty(skuId))
		{
			this.setSku(skuId);
		}
	}

	public BinQueryObject(final String binCode, final String locationId)
	{
		this();
		if (StringUtils.isNotEmpty(binCode))
		{
			this.setBinCode(binCode);
		}

		if (StringUtils.isNotEmpty(locationId))
		{
			this.setLocationId(locationId);
		}
	}

    public String getLocationId()
    {
        return this.getValue(LOCATION_ID);
    }

    public final void setLocationId(final String locationId)
    {
        this.setValue(LOCATION_ID, locationId);
    }

	public String getBinCode()
	{
		return super.getValue(BIN_CODE);
	}

	public final void setBinCode(final String binCode)
	{
		super.setValue(BIN_CODE, binCode);
	}

	public String getPriority()
	{
		return super.getValue(PRIORITY);
	}

	public final void setPriority(final String priority)
	{
		super.setValue(PRIORITY, priority);
	}



	public String getDescription()
	{
		return super.getValue(DESCRIPTION);
	}

	public final void setDescription(final String description)
	{
		super.setValue(DESCRIPTION, description);
	}


	public String getSku()
	{
		return super.getValue(SKU_ID);
	}

	public final void setSku(final String skuId)
	{
		super.setValue(SKU_ID, skuId);
	}

}
