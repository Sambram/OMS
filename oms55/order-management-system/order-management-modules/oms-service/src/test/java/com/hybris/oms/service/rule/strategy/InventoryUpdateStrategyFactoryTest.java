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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;


/**
 *
 */
public class InventoryUpdateStrategyFactoryTest
{
	private static final String KEY_1 = "KEY_1";
	private static final String KEY_2 = "KEY_2";

	private final InventoryUpdateStrategyFactory strategyFactory = new InventoryUpdateStrategyFactory();

	@After
	public void tearDown()
	{
		strategyFactory.setStrategyMap(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailInit_MapEmpty()
	{
		strategyFactory.afterPropertiesSet();
	}

	@Test
	public void shouldGetStrategyKeys()
	{
		strategyFactory.setStrategyMap(buildStrategyMap());
		final Set<String> keys = strategyFactory.getStrategyKeys();
		Assert.assertEquals(1, keys.size());
	}

	@Test
	public void shouldGetStrategy()
	{
		strategyFactory.setStrategyMap(buildStrategyMap());
		final InventoryUpdateStrategy strategy = strategyFactory.getStrategy(KEY_1);
		Assert.assertNotNull(strategy);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailGetStrategy_NotFound()
	{
		strategyFactory.setStrategyMap(buildStrategyMap());
		strategyFactory.getStrategy(KEY_2);
	}

	private Map<String, InventoryUpdateStrategy> buildStrategyMap()
	{
		final Map<String, InventoryUpdateStrategy> map = new HashMap<>();
		map.put(KEY_1, new TestInventoryUpdateStrategy());
		return map;
	}

	private static class TestInventoryUpdateStrategy implements InventoryUpdateStrategy
	{

		@Override
		public int calculateInventoryUpdateQuantity(final InventoryUpdateDto dto)
		{
			return 0;
		}
	}
}
