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

import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.rule.strategy.InventoryUpdateDto;
import com.hybris.oms.service.rule.strategy.InventoryUpdateStrategy;

import java.util.Collections;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Strategy for calculating inventory update quantity by local ATS value.
 */
public class InventoryUpdateLocalAtsStrategy implements InventoryUpdateStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(InventoryUpdateLocalAtsStrategy.class);

	private AtsService atsService;

	@Override
	public int calculateInventoryUpdateQuantity(final InventoryUpdateDto dto)
	{
		LOG.info("Applying inventory update local ATS strategy with values: {}", dto);

		final String defaultAtsId = this.atsService.getDefaultAtsId();
		final int factor = dto.isPositive() ? 1 : -1;
		final int atsQuantity = this.atsService
				.getLocalAts(Collections.singleton(dto.getSku()), Collections.singleton(dto.getLocationId()),
						Collections.singleton(defaultAtsId))
						.getResult(dto.getSku(), defaultAtsId, dto.getLocationId(), (new Date()).getTime()).intValue();
		final int result = factor * atsQuantity;

		return (result < 0) ? 0 : result;
	}

	protected AtsService getAtsService()
	{
		return atsService;
	}

	@Required
	public void setAtsService(final AtsService atsService)
	{
		this.atsService = atsService;
	}
}
