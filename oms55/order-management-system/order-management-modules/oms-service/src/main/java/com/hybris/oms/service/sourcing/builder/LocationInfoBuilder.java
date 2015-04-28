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

import java.util.List;
import java.util.Map;

import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.DistanceUtils;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;
import com.hybris.oms.service.sourcing.context.PropertiesBuilderSupport;


/**
 * Builder for {@link LocationInfo} retrieving the necessary data from {@link StockroomLocationData}.
 */
public class LocationInfoBuilder
{
	private List<PropertiesBuilder<StockroomLocationData>> propertiesBuilders;

	/**
	 * Instantiates a {@link LocationInfo} from
	 * {@link com.hybris.oms.service.managedobjects.inventory.StockroomLocationData}.
	 * 
	 * @return {@link LocationInfo}
	 */
	public LocationInfo build(final StockroomLocationData location, final RadianCoordinates shipToCoordinates)
	{
		final double distance = calculateDistance(location, shipToCoordinates);
		final Map<String, Object> properties = PropertiesBuilderSupport.buildProperties(location, propertiesBuilders);
		return new LocationInfo(location.getPriority(), location.getLocationId(), distance, location.getLocationRoles(), properties);
	}

	protected double calculateDistance(final StockroomLocationData location, final RadianCoordinates shipToCoordinates)
	{
		double result = Double.MAX_VALUE;
		if (shipToCoordinates != null && location.getAddress() != null)
		{
			final RadianCoordinates locCoordinates = RadianCoordinates.fromOptionalDegrees(location.getAddress().getLatitudeValue(),
					location.getAddress().getLongitudeValue());
			if (locCoordinates != null)
			{
				result = DistanceUtils.haversineFormula(locCoordinates, shipToCoordinates);
			}
		}
		return result;
	}

	public void setPropertiesBuilders(final List<PropertiesBuilder<StockroomLocationData>> propertiesBuilders)
	{
		this.propertiesBuilders = propertiesBuilders;
	}

}
