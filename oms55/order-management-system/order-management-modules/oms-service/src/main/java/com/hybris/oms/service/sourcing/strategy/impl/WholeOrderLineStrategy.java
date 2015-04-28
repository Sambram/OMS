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
 * Assigns whole order lines to the best matching location.
 */
public class WholeOrderLineStrategy implements SourcingStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(WholeOrderLineStrategy.class);

	private SourcingOLQBuilder olqBuilder;

	@Override
	public void source(final SourcingContext context)
	{
		LOG.debug("Applying strategy");
		final ProcessStatus status = context.getProcessStatus();
		boolean allLinesProcessed = true;
		for (final SourcingLine line : context.getSourcingLines())
		{
			if (status.isUnprocessedLine(line) && this.processLine(line, context))
			{
				LOG.debug("Line {} successfully processed", line.getLineId());
			}
			else
			{
				allLinesProcessed = false;
			}
		}
		if (allLinesProcessed)
		{
			status.markResultAsProcessed(this.getClass());
			LOG.debug("Sourcing finished");
		}
	}

	protected boolean processLine(final SourcingLine line, final SourcingContext context)
	{
		LOG.debug("Starting to process {}", line);
		boolean lineProcessed = false;
		final ProcessStatus status = context.getProcessStatus();
		for (final SourcingLocation location : context.getSourcingMatrix().getSourcingLocations(line.getLineId(),
				context.getConfiguration().getOrderLineComparator()))
		{
			final int ats = location.getAts() - status.getReservedForLocation(line.getSku(), location.getInfo().getLocationId());
			if (ats >= line.getQuantity())
			{
				status.addOLQ(olqBuilder.buildSourcingOLQ(line, line.getQuantity(), location, this.getClass()));
				status.markLineAsProcessed(line, this.getClass());
				lineProcessed = true;
				break;
			}
		}
		return lineProcessed;
	}

	public void setOlqBuilder(final SourcingOLQBuilder olqBuilder)
	{
		this.olqBuilder = olqBuilder;
	}

}
