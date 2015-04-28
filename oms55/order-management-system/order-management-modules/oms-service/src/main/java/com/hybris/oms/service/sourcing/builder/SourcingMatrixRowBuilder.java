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
package com.hybris.oms.service.sourcing.builder;

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.strategy.LocationsFilterStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Builder responsible for retrieving locations and ATS and building matrix rows. Interacts with
 * {@link SourcingLocationBuilder} to build {@link SourcingLocation}s.
 */
public class SourcingMatrixRowBuilder
{
	private static final Logger LOG = LoggerFactory.getLogger(SourcingMatrixRowBuilder.class);

	private AtsService atsService;
	private InventoryService inventoryService;

	private LocationsFilterStrategy<SourcingLine, List<StockroomLocationData>> lineLocationsFilterStrategy;

	private SourcingLocationBuilder sourcingLocationBuilder;

	/**
	 * Returns a new matrix row for the sourcing line. This includes calculating the distance, retrieving location data
	 * and ATS.
	 *
	 * @return matrix row, can be <tt>null</tt> if no ATS or no matching locations is available
	 */
	public List<SourcingLocation> getMatrixRow(final SourcingLine line, final String atsId, final Set<String> filterLocationIds,
			final RadianCoordinates shipToCoordinates, final AtsResult atsResult, final Map<String, LocationInfo> locationInfos)
	{
		List<SourcingLocation> result = null;
		final List<StockroomLocationData> locations = lineLocationsFilterStrategy.filter(line, filterLocationIds);
		if (CollectionUtils.isEmpty(locations))
		{
			LOG.warn("No suitable locations found for line {}", line);
		}
		else
		{
			final Set<String> locationIds = new HashSet<>(locations.size());
			for (final StockroomLocationData location : locations)
			{
				final List<ItemLocationData> itemLocations = inventoryService.findAllItemLocationsBySkuAndLocation(line.getSku(),
						location.getLocationId());
				for (final ItemLocationData itemLocation : itemLocations)
				{
					if (!itemLocation.isBanned())
					{
						locationIds.add(location.getLocationId());
					}
				}
			}

			LOG.debug("Using locationIds {} for line {}", locationIds, line);
			final AtsResult localAtsResult = this.atsService.getLocalAts(Collections.singleton(line.getSku()), locationIds,
					Collections.singleton(atsId));
			if (localAtsResult.isEmpty())
			{
				LOG.warn("No ATS available for locationIds {} and line {}", locationIds, line);
			}
			else
			{
				result = new ArrayList<>();
				for (final StockroomLocationData location : locations)
				{
					if (locationIds.contains(location.getLocationId()))
					{
						result.add(sourcingLocationBuilder.build(line, location,
								localAtsResult.getResult(line.getSku(), atsId, location.getLocationId(), (new Date()).getTime()),
								shipToCoordinates, locationInfos));
					}
				}
				atsResult.addAll(localAtsResult);
			}
		}
		return result;
	}

	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	public void setAtsService(final AtsService atsService)
	{
		this.atsService = atsService;
	}

	public void setSourcingLocationBuilder(final SourcingLocationBuilder sourcingLocationBuilder)
	{
		this.sourcingLocationBuilder = sourcingLocationBuilder;
	}

	protected LocationsFilterStrategy<SourcingLine, List<StockroomLocationData>> getLineLocationsFilterStrategy()
	{
		return lineLocationsFilterStrategy;
	}

	public void setLineLocationsFilterStrategy(
			final LocationsFilterStrategy<SourcingLine, List<StockroomLocationData>> lineLocationsFilterStrategy)
	{
		this.lineLocationsFilterStrategy = lineLocationsFilterStrategy;
	}

}
