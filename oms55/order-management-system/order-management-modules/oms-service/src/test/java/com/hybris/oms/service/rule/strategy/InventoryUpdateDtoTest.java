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

import com.hybris.oms.service.rule.strategy.InventoryUpdateDto;

import org.junit.Assert;
import org.junit.Test;


/**
 *
 */
public class InventoryUpdateDtoTest
{
	@Test(expected = IllegalArgumentException.class)
	public void shouldFailCreation_QuantityLessThanZero()
	{
		new InventoryUpdateDto(null, null, -1, Boolean.TRUE);
	}

	@Test
	public void shouldCreateValidDto()
	{
		final InventoryUpdateDto dto = new InventoryUpdateDto(null, null, 0, Boolean.TRUE);
		Assert.assertNotNull(dto);
	}
}
