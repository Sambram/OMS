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

import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;
import com.hybris.oms.service.sourcing.context.PropertiesBuilderSupport;
import com.hybris.oms.service.sourcing.context.SourcingLocation;

import java.util.List;
import java.util.Map;

import org.jgroups.util.Tuple;


/**
 * Builder for {@link SourcingLocation}.
 */
public class SourcingLocationBuilder
{
	private List<PropertiesBuilder<Tuple<SourcingLine, StockroomLocationData>>> propertiesBuilders;

	private LocationInfoBuilder locationInfoBuilder;

	public SourcingLocation build(final LocationInfo locationInfo, final int ats, final Map<String, Object> properties)
	{
		return new SourcingLocation(locationInfo, ats, properties);
	}

	public SourcingLocation build(final SourcingLine line, final StockroomLocationData location, final int ats,
			final RadianCoordinates shipToCoordinates, final Map<String, LocationInfo> locationInfos)
	{
		LocationInfo info = locationInfos.get(location.getLocationId());
		if (info == null)
		{
			info = locationInfoBuilder.build(location, shipToCoordinates);
			locationInfos.put(location.getLocationId(), info);
		}
		return new SourcingLocation(info, ats, PropertiesBuilderSupport.buildProperties(new Tuple<>(line, location),
				propertiesBuilders));
	}

	public void setPropertiesBuilders(final List<PropertiesBuilder<Tuple<SourcingLine, StockroomLocationData>>> propertiesBuilders)
	{
		this.propertiesBuilders = propertiesBuilders;
	}

	public void setLocationInfoBuilder(final LocationInfoBuilder locationInfoBuilder)
	{
		this.locationInfoBuilder = locationInfoBuilder;
	}

	protected List<PropertiesBuilder<Tuple<SourcingLine, StockroomLocationData>>> getPropertiesBuilders()
	{
		return propertiesBuilders;
	}

	protected LocationInfoBuilder getLocationInfoBuilder()
	{
		return locationInfoBuilder;
	}

}
