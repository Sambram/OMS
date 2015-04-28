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

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.builder.SourcingComparatorFactory;
import com.hybris.oms.service.sourcing.builder.SourcingOLQBuilder;
import com.hybris.oms.service.sourcing.context.ProcessStatus;
import com.hybris.oms.service.sourcing.context.SourcingContext;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.strategy.SourcingStrategy;


/**
 * This strategy is only executed if item grouping is configured. Sources the order assigning as many items as possible
 * to the first location, assigning further items from the remaining locations. There might remain some unassigned
 * quantity if the process is finished. Stops processing the strategy list.
 * 
 * Sorting of {@link SourcingLocation}s is achieved using a configurable comparator descriptor set to 'ATS' by default.
 */
public class ItemGroupingStrategy implements InitializingBean, SourcingStrategy
{
	private static final Logger LOG = LoggerFactory.getLogger(ItemGroupingStrategy.class);

	private SourcingComparatorFactory comparatorFactory;

	private String sequenceDescriptor;

	private Comparator<SourcingLocation> comparator;

	private SourcingOLQBuilder olqBuilder;

	@Override
	public void source(final SourcingContext context)
	{
		if (context.getConfiguration().isMinimizeShipments())
		{
			LOG.debug("Applying strategy");
			this.processItemGrouping(context);
		}
	}

	protected void processItemGrouping(final SourcingContext context)
	{
		final ProcessStatus status = context.getProcessStatus();
		for (final SourcingLocation location : context.getSourcingMatrix().getTotalSourcingLocations(this.comparator))
		{
			if (location.getInfo().getLocationRoles().contains(LocationRole.SHIPPING.getCode()))
			{
				final String locationId = location.getInfo().getLocationId();
				for (final SourcingLine line : context.getSourcingLines())
				{
					if (status.isUnprocessedLine(line))
					{
						LOG.debug("Starting to process {}", line);
						final int ats = context.getSourcingMatrix().getAtsForLocationId(line, locationId)
								- status.getReservedForLocation(line.getSku(), locationId);
						if (ats > 0)
						{
							this.assignOLQ(line, location, ats, context);
						}
					}
				}
				if (status.allLinesProcessed(context.getSourcingLines()))
				{
					break;
				}
			}
		}
		status.markResultAsProcessed(this.getClass());
		status.markAllLinesAsProcessed(context.getSourcingLines(), this.getClass());
		LOG.debug("Sourcing finished");
	}

	protected void assignOLQ(final SourcingLine line, final SourcingLocation location, final int ats, final SourcingContext context)
	{
		final ProcessStatus status = context.getProcessStatus();
		final int requested = line.getQuantity() - status.getReservedForLine(line);
		final int reserved = Math.min(requested, ats);
		boolean assignOLQ = false;
		if (reserved == requested)
		{
			status.markLineAsProcessed(line, this.getClass());
			assignOLQ = true;
			LOG.debug("Line {} successfully processed", line.getLineId());
		}
		else
		{
			if (context.getConfiguration().getOrderLineSplit().allowSplitting())
			{
				assignOLQ = true;
				LOG.debug("Line {} partially processed ({} of {})", new Object[]
				{ line.getLineId(), Integer.valueOf(reserved), Integer.valueOf(requested) });
			}
			else
			{
				status.setActionForUnprocessedLine(line, context.getConfiguration().getOrderLineSplit().getAction());
				status.markLineAsProcessed(line, this.getClass());
			}
		}
		if (assignOLQ)
		{
			status.addOLQ(olqBuilder.buildSourcingOLQ(line, reserved, location, this.getClass()));
		}
	}

	@Required
	public void setSequenceDescriptor(final String sequenceDescriptor)
	{
		this.sequenceDescriptor = sequenceDescriptor;
	}

	@Required
	public void setComparatorFactory(final SourcingComparatorFactory comparatorFactory)
	{
		this.comparatorFactory = comparatorFactory;
	}

	@Override
	public void afterPropertiesSet()
	{
		this.comparator = this.comparatorFactory.createComparator(this.sequenceDescriptor);
	}

	public void setOlqBuilder(final SourcingOLQBuilder olqBuilder)
	{
		this.olqBuilder = olqBuilder;
	}

}
