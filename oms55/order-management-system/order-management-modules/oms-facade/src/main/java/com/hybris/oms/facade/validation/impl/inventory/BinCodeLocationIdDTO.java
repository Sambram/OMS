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
package com.hybris.oms.facade.validation.impl.inventory;

/**
 * DTO to hold the bin code and location id. Used by {@link BinCodeValidator}
 */
public class BinCodeLocationIdDTO
{

	private String binCode;
	private String locationId;

	public BinCodeLocationIdDTO(final String binCode, final String locationId)
	{
		this.binCode = binCode;
		this.locationId = locationId;
	}

	public String getBinCode()
	{
		return binCode;
	}

	public void setBinCode(final String binCode)
	{
		this.binCode = binCode;
	}

	public String getLocationId()
	{
		return locationId;
	}

	public void setLocationId(final String locationId)
	{
		this.locationId = locationId;
	}
}
