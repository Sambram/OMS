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
package com.hybris.oms.service.rule.strategy;

import com.hybris.commons.dto.Dto;

import com.google.common.base.Preconditions;


/**
 * Data transfer object used in {@link InventoryUpdateStrategy}
 */
public class InventoryUpdateDto implements Dto
{
	private static final long serialVersionUID = -3765781181302865393L;

	private final String locationId;
	private final String sku;
	private final int quantity;
	private final boolean positive;

	/**
	 * Create a new immutable dto.
	 * 
	 * @param locationId
	 * @param sku
	 * @param quantity - cannot be a negative integer
	 * @param positive - set to true for the result of a {@link InventoryUpdateStrategy} to be positive, set to false for
	 *           a negative result
	 */
	public InventoryUpdateDto(final String locationId, final String sku, final int quantity, final boolean positive)
	{
		Preconditions.checkArgument(quantity >= 0);

		this.locationId = locationId;
		this.sku = sku;
		this.quantity = quantity;
		this.positive = positive;
	}

	public String getLocationId()
	{
		return locationId;
	}

	public String getSku()
	{
		return sku;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public boolean isPositive()
	{
		return positive;
	}
}
