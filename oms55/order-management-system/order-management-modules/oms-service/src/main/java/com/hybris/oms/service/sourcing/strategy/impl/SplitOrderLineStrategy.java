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

import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.builder.SourcingOLQBuilder;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.context.SourcingContext;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.strategy.SourcingStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Splits order lines to the best matching locations.
 */
public class SplitOrderLineStrategy implements SourcingStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(SplitOrderLineStrategy.class);

	private SourcingOLQBuilder olqBuilder;

	@Override
	public void source(final SourcingContext context)
	{
		LOG.debug("Applying strategy");
		final ProcessStatus status = context.getProcessStatus();
		for (final SourcingLine line : context.getSourcingLines())
		{
			if (status.isUnprocessedLine(line))
			{
				LOG.debug("Starting to process {}", line);
				if (context.getConfiguration().getOrderLineSplit().allowSplitting())
				{
					this.processLine(line, context);
				}
				else
				{
					LOG.debug("Splitting is not allowed, marking line as failure: {}", line);
					status.setActionForUnprocessedLine(line, context.getConfiguration().getOrderLineSplit().getAction());
					status.markLineAsProcessed(line, this.getClass());
				}
			}
		}
		status.markResultAsProcessed(this.getClass());
		LOG.debug("Sourcing finished");
	}

	protected void processLine(final SourcingLine line, final SourcingContext context)
	{
		boolean fullyAllocated = false;
		final ProcessStatus status = context.getProcessStatus();
		for (final SourcingLocation location : context.getSourcingMatrix().getSourcingLocations(line.getLineId(),
				context.getConfiguration().getOlqComparator()))
		{
			final int ats = location.getAts() - status.getReservedForLocation(line.getSku(), location.getInfo().getLocationId());
			if (ats > 0)
			{
				final int requested = line.getQuantity() - status.getReservedForLine(line);
				final int reserved = Math.min(requested, ats);
				status.addOLQ(olqBuilder.buildSourcingOLQ(line, reserved, location, this.getClass()));
				if (reserved == requested)
				{
					LOG.debug("Line {} successfully processed", line.getLineId());
					fullyAllocated = true;
					break;
				}
			}
		}
		status.markLineAsProcessed(line, this.getClass());
		if (!fullyAllocated)
		{
			LOG.debug("Line {} could not be allocated completely", line.getLineId());
		}
	}

	public void setOlqBuilder(final SourcingOLQBuilder olqBuilder)
	{
		this.olqBuilder = olqBuilder;
	}

}
