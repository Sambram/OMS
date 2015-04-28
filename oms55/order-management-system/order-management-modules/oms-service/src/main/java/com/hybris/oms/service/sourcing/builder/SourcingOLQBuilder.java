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

import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;
import com.hybris.oms.service.sourcing.context.PropertiesBuilderSupport;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.strategy.SourcingStrategy;

import java.util.List;

import org.jgroups.util.Tuple;


/**
 * Builder for {@link SourcingOLQ}.
 */
public class SourcingOLQBuilder
{
	private List<PropertiesBuilder<Tuple<SourcingLine, SourcingLocation>>> propertiesBuilders;

	/**
	 * Builds a new {@link SourcingOLQ} from the parameter list.
	 */
	public SourcingOLQ buildSourcingOLQ(final SourcingLine line, final int quantity, final SourcingLocation location,
			final Class<? extends SourcingStrategy> strategyClass)
	{
		return new SourcingOLQ(line.getSku(), quantity, location.getInfo().getLocationId(), line.getLineId(), strategyClass,
				PropertiesBuilderSupport.buildProperties(new Tuple<>(line, location), propertiesBuilders));
	}

	public void setPropertiesBuilders(final List<PropertiesBuilder<Tuple<SourcingLine, SourcingLocation>>> propertiesBuilders)
	{
		this.propertiesBuilders = propertiesBuilders;
	}

}
