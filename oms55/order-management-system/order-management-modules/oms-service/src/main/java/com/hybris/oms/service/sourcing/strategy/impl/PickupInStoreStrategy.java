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

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Strategy to source from a single location defined in pickupLocationId. Assign as many items as possible. Mark the
 * line
 * as SUCCESS if there is sufficient ATS available, otherwise mark it as FAILURE.
 */
public class PickupInStoreStrategy implements SourcingStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(PickupInStoreStrategy.class);

	private SourcingOLQBuilder olqBuilder;

	@Override
	public void source(final SourcingContext context)
	{
		LOG.debug("Applying strategy");
		boolean onlyPickupLines = true;
		final ProcessStatus status = context.getProcessStatus();
		for (final SourcingLine line : context.getSourcingLines())
		{
			if (line.getPickupLocationId() == null)
			{
				onlyPickupLines = false;
			}
			else if (status.isUnprocessedLine(line))
			{
				processLine(context, status, line);
			}
		}
		if (onlyPickupLines)
		{
			status.markResultAsProcessed(this.getClass());
			LOG.debug("Only pickup lines found, sourcing finished");
		}
	}

	protected void processLine(final SourcingContext context, final ProcessStatus status, final SourcingLine line)
	{
		LOG.debug("Starting to process {}", line);
		final List<SourcingLocation> locations = context.getSourcingMatrix().getSourcingLocations(line.getLineId(), null);

		if (CollectionUtils.isNotEmpty(locations))
		{
			final SourcingLocation location = locations.get(0);
			final String locationId = line.getPickupLocationId();
			final int reservedForLoc = context.getProcessStatus().getReservedForLocation(line.getSku(), locationId);
			final int ats = location.getAts() - reservedForLoc;
			if (ats > 0)
			{
				final int reserved = Math.min(line.getQuantity(), ats);
				status.addOLQ(olqBuilder.buildSourcingOLQ(line, reserved, location, this.getClass()));
			}
		}
		status.markLineAsProcessed(line, this.getClass());
		LOG.debug("Line {} processed with result {}", line.getLineId());
	}

	public void setOlqBuilder(final SourcingOLQBuilder olqBuilder)
	{
		this.olqBuilder = olqBuilder;
	}

}
