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
package com.hybris.oms.service.rule.strategy.impl;

import com.hybris.oms.service.rule.strategy.InventoryUpdateDto;

import org.junit.Assert;
import org.junit.Test;


/**
 *
 */
public class InventoryUpdateOlqQuantityStrategyTest
{

	private static final int FIVE = 5;
	private static final int MINUS_FIVE = -5;
	private static final String SKU = "SKU";
	private static final String LOCATION_ID = "LOCATION_ID";

	private final InventoryUpdateOlqQuantityStrategy strategy = new InventoryUpdateOlqQuantityStrategy();

	@Test
	public void shouldComputeValue_Positive()
	{
		final int result = strategy.calculateInventoryUpdateQuantity(new InventoryUpdateDto(LOCATION_ID, SKU, FIVE, Boolean.TRUE));
		Assert.assertEquals(FIVE, result);
	}

	@Test
	public void shouldComputeValue_Negative()
	{
		final int result = strategy.calculateInventoryUpdateQuantity(new InventoryUpdateDto(LOCATION_ID, SKU, FIVE, Boolean.FALSE));
		Assert.assertEquals(MINUS_FIVE, result);
	}

}
