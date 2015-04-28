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

import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.strategy.LocationsFilterStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;


/**
 * Strategy for retrieving a location from the pickupLocationId if it is not blank.
 */
public class PickupLocationsFilterStrategy implements LocationsFilterStrategy<SourcingLine, List<StockroomLocationData>>
{
	private InventoryService inventoryService;

	@Override
	public List<StockroomLocationData> filter(final SourcingLine line, final Set<String> filterLocationIds)
	{
		List<StockroomLocationData> result = null;
		if (StringUtils.isNotBlank(line.getPickupLocationId()))
		{
			final StockroomLocationData location = inventoryService.getLocationByLocationId(line.getPickupLocationId());
			if (location.getLocationRoles().contains(LocationRole.PICKUP.toString()))
			{
				result = Collections.singletonList(location);
			}
		}
		return result;
	}

	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

}
