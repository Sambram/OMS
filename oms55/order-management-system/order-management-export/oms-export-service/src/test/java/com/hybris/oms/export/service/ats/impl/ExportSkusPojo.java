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
package com.hybris.oms.export.service.ats.impl;

import com.hybris.kernel.api.HybrisId;
import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;

import java.util.Date;


/**
 * DTO implementation of {@link ExportSkus} for unit testing.
 */
public class ExportSkusPojo implements ExportSkus
{

	private Date creationTime;
	private Date modifiedTime;
	private HybrisId id;
	private String sku;
	private String locationId;
	private long latestChange;

	public ExportSkusPojo()
	{
		// Default Constructor
	}

	public ExportSkusPojo(final String sku)
	{
		this.sku = sku;
	}

	@Override
	public Date getCreationTime()
	{
		return creationTime;
	}

	@Override
	public HybrisId getId()
	{
		return id;
	}

	@Override
	public Date getModifiedTime()
	{
		return modifiedTime;
	}

	@Override
	public String getSku()
	{
		return sku;
	}

	@Override
	public void setSku(final String value)
	{
		sku = value;
	}

	@Override
	public String getLocationId()
	{
		return locationId;
	}

	@Override
	public long getLatestChange()
	{
		return latestChange;
	}

	@Override
	public void setLocationId(final String value)
	{
		locationId = value;
	}

	@Override
	public void setLatestChange(final long value)
	{
		latestChange = value;
	}

}
