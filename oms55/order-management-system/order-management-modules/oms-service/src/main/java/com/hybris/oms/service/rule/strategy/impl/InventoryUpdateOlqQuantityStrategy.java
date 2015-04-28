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
import com.hybris.oms.service.rule.strategy.InventoryUpdateStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Strategy for calculating inventory update quantity by existing quantity.
 */
public class InventoryUpdateOlqQuantityStrategy implements InventoryUpdateStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(InventoryUpdateOlqQuantityStrategy.class);

	@Override
	public int calculateInventoryUpdateQuantity(final InventoryUpdateDto dto)
	{
		LOG.info("Applying inventory update olq stantity strategy with values: {}", dto);

		final int factor = dto.isPositive() ? 1 : -1;
		return factor * dto.getQuantity();
	}

}
