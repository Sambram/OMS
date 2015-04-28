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

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.rule.strategy.InventoryUpdateDto;

import java.util.Collections;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 *
 */
public class InventoryUpdateLocalAtsStrategyTest
{
	private static final int FIVE = 5;
	private static final int MINUS_FIVE = -5;
	private static final int ZERO = 0;
	private static final String SKU = "SKU";
	private static final String LOCATION_ID = "LOCATION_ID";
	private static final String ATS_WEB_CALCULATOR = "WEB";
	private static final Long MILLISTIME = null;

	@Mock
	private AtsService atsService;

	@InjectMocks
	private final InventoryUpdateLocalAtsStrategy strategy = new InventoryUpdateLocalAtsStrategy();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldComputePositiveValueWhenLocalAtsIsPositive()
	{
		final AtsResult atsResult = new AtsResult();
		atsResult.addResult(LOCATION_ID, SKU, ATS_WEB_CALCULATOR, FIVE, MILLISTIME);

		Mockito.when(this.atsService.getDefaultAtsId()).thenReturn(ATS_WEB_CALCULATOR);
		Mockito.when(
				this.atsService.getLocalAts(Collections.singleton(SKU), Collections.singleton(LOCATION_ID),
						Collections.singleton(ATS_WEB_CALCULATOR))).thenReturn(atsResult);

		final int result = this.strategy.calculateInventoryUpdateQuantity(new InventoryUpdateDto(LOCATION_ID, SKU, FIVE,
				Boolean.TRUE));

		Assert.assertTrue(FIVE == result);

		Mockito.verify(this.atsService, Mockito.times(1)).getDefaultAtsId();
		Mockito.verify(this.atsService, Mockito.times(1)).getLocalAts(Collections.singleton(SKU),
				Collections.singleton(LOCATION_ID), Collections.singleton(ATS_WEB_CALCULATOR));
	}

	@Test
	public void shouldComputeZeroValueWhenLocalAtsIsNegative()
	{
		final AtsResult atsResult = new AtsResult();
		atsResult.addResult(LOCATION_ID, SKU, ATS_WEB_CALCULATOR, MINUS_FIVE, MILLISTIME);

		Mockito.when(this.atsService.getDefaultAtsId()).thenReturn(ATS_WEB_CALCULATOR);
		Mockito.when(
				this.atsService.getLocalAts(Collections.singleton(SKU), Collections.singleton(LOCATION_ID),
						Collections.singleton(ATS_WEB_CALCULATOR))).thenReturn(atsResult);

		final int result = this.strategy.calculateInventoryUpdateQuantity(new InventoryUpdateDto(LOCATION_ID, SKU, FIVE,
				Boolean.TRUE));

		Assert.assertTrue(ZERO == result);

		Mockito.verify(this.atsService, Mockito.times(1)).getDefaultAtsId();
		Mockito.verify(this.atsService, Mockito.times(1)).getLocalAts(Collections.singleton(SKU),
				Collections.singleton(LOCATION_ID), Collections.singleton(ATS_WEB_CALCULATOR));
	}
}
