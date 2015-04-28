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
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.builder.SourcingOLQBuilder;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.context.SourcingContext;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.strategy.SourcingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Sources the whole order using the sorting options defined on the order level.
 */
public class WholeOrderStrategy implements SourcingStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(WholeOrderStrategy.class);

	private SourcingOLQBuilder olqBuilder;

	@Override
	public void source(final SourcingContext context)
	{
		LOG.debug("Applying strategy");
		final ProcessStatus status = context.getProcessStatus();
		final List<SourcingLine> sourceLines = new ArrayList<>();
		for (final SourcingLocation location : context.getSourcingMatrix().getTotalSourcingLocations(
				context.getConfiguration().getOrderComparator()))
		{
			if (location.getInfo().getLocationRoles().contains(LocationRole.SHIPPING.toString()))
			{
				// handle the case of multiple orderLines for the same sku since this is not excluded in the data model
				final Map<String, Integer> skuReserved = new HashMap<>();
				final String locationId = location.getInfo().getLocationId();
				for (final SourcingLine line : context.getSourcingLines())
				{
					if (status.isUnprocessedLine(line))
					{
						if (processLine(line, locationId, skuReserved, context))
						{
							sourceLines.add(line);
						}
						else
						{
							sourceLines.clear();
							break;
						}
					}
				}
				if (!sourceLines.isEmpty())
				{
					this.assignOLQs(context, sourceLines, location);
					status.markResultAsProcessed(this.getClass());
					LOG.debug("Sourcing finished");
					break;
				}
			}
		}
		if (!status.isProcessFinished() && !context.getConfiguration().getOrderSplit().allowSplitting())
		{
			final String action = context.getConfiguration().getOrderSplit().getAction();
			LOG.debug("Order splitting is not allowed, setting action {}", action);
			status.setActionForUnprocessedLines(context.getSourcingLines(), action);
			status.markResultAsProcessed(this.getClass());
		}
	}

	protected boolean processLine(final SourcingLine line, final String locationId, final Map<String, Integer> skuReserved,
			final SourcingContext context)
	{
		boolean processed = false;
		final int reservedForLocation = context.getProcessStatus().getReservedForLocation(line.getSku(), locationId);
		final Integer reservedForSku = skuReserved.get(line.getSku());
		final int totalReservedForSku = (reservedForSku == null ? 0 : reservedForSku.intValue()) + reservedForLocation;
		final int requested = totalReservedForSku + line.getQuantity();
		if (context.getSourcingMatrix().getAtsForLocationId(line, locationId) >= requested)
		{
			skuReserved.put(line.getSku(), Integer.valueOf(requested));
			processed = true;
		}
		return processed;
	}

	protected void assignOLQs(final SourcingContext context, final List<SourcingLine> sourceLines, final SourcingLocation location)
	{
		final ProcessStatus status = context.getProcessStatus();
		for (final SourcingLine line : sourceLines)
		{
			status.markLineAsProcessed(line, this.getClass());
			status.addOLQ(olqBuilder.buildSourcingOLQ(line, line.getQuantity(), location, this.getClass()));
		}
	}

	public void setOlqBuilder(final SourcingOLQBuilder olqBuilder)
	{
		this.olqBuilder = olqBuilder;
	}

}
