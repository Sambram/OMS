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

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;


/**
 * Factory will provide instances of {@link InventoryUpdateStrategy} to other beans.
 */
public class InventoryUpdateStrategyFactory implements InitializingBean
{
	private Map<String, InventoryUpdateStrategy> strategyMap;

	public InventoryUpdateStrategy getStrategy(final String key)
	{
		Preconditions.checkNotNull(key, "key cannot be null");

		final InventoryUpdateStrategy strategy = this.strategyMap.get(key);
		if (strategy == null)
		{
			throw new IllegalArgumentException("No strategy exists with key: " + key);
		}
		return strategy;
	}

	/**
	 * Gets all of the names of the various inventory update strategies.
	 * 
	 * @return the keySet of the strategyMap
	 */
	public Set<String> getStrategyKeys()
	{
		return ImmutableSet.copyOf(strategyMap.keySet());
	}

	@Required
	public void setStrategyMap(final Map<String, InventoryUpdateStrategy> strategyMap)
	{
		this.strategyMap = strategyMap;
	}

	@Override
	public void afterPropertiesSet()
	{
		if (this.strategyMap == null || this.strategyMap.isEmpty())
		{
			throw new IllegalArgumentException("strategyMap is mandatory but it is not set");
		}
	}
}
