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
package com.hybris.oms.service.sourcing.strategy.impl;

import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.strategy.LocationsFilterStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Lists;


/**
 * Implementation of {@link LocationsFilterStrategy} which is filtering out locationIds for specific sourcing lines
 * based on
 * locationRoles and shippable countries.
 */
public class QueryBasedLocationsFilterStrategy implements LocationsFilterStrategy<SourcingLine, List<StockroomLocationData>>
{
	private static final Logger LOG = LoggerFactory.getLogger(QueryBasedLocationsFilterStrategy.class);

	private InventoryService inventoryService;

	@Override
	public List<StockroomLocationData> filter(final SourcingLine line, final Set<String> filterLocationIds)
	{
		final LocationQueryObject queryObject = getLocationQuery(line, filterLocationIds);
		return this.inventoryService.findAllLocationsByQueryObject(queryObject, false);
	}

	protected LocationQueryObject getLocationQuery(final SourcingLine source, final Set<String> filterLocationIds)
	{
		final LocationQueryObject queryObject = new LocationQueryObject();
		if (CollectionUtils.isNotEmpty(filterLocationIds))
		{
			LOG.debug("Limiting locations to {}", filterLocationIds);
			queryObject.setLocationIds(new ArrayList<>(filterLocationIds));
		}
		if (CollectionUtils.isNotEmpty(source.getLocationRoles()))
		{
			LOG.debug("Limiting to location roles {} for sourcing", source.getLocationRoles());
			queryObject.setLocationRoles(new ArrayList<>(source.getLocationRoles()));
		}
		if (StringUtils.isNotBlank(source.getCountry()))
		{
			LOG.debug("Limiting to country {} for sourcing", source.getCountry());
			queryObject.setCountries(Lists.newArrayList(source.getCountry()));
		}
		return queryObject;
	}

	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}

	@Required
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

}
