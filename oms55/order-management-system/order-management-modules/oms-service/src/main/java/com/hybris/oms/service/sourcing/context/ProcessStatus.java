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
package com.hybris.oms.service.sourcing.context;

import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.strategy.SourcingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Provides the process status during sourcing.
 */
public class ProcessStatus
{
	private Map<SourcingLine, String> processedLines;
	private Map<SourcingLine, String> lineActions;
	private boolean processFinished;
	private List<SourcingOLQ> sourcingOlqs;
	private String strategyName;
	private Map<String, Object> properties;

	public List<SourcingOLQ> getSourcingOlqs()
	{
		return this.sourcingOlqs == null ? Collections.<SourcingOLQ>emptyList() : Collections.unmodifiableList(this.sourcingOlqs);
	}

	public Map<SourcingLine, String> getProcessedLines()
	{
		return this.processedLines == null ? Collections.<SourcingLine, String>emptyMap() : Collections
				.unmodifiableMap(this.processedLines);
	}

	public Map<SourcingLine, String> getLineActions()
	{
		return this.lineActions == null ? Collections.<SourcingLine, String>emptyMap() : Collections
				.unmodifiableMap(this.lineActions);
	}

	public boolean isProcessFinished()
	{
		return processFinished;
	}

	public String getStrategyName()
	{
		return this.strategyName;
	}

	/**
	 * adds a single {@link SourcingOLQ} to the result.
	 */
	public void addOLQ(final SourcingOLQ olq)
	{
		if (this.sourcingOlqs == null)
		{
			this.sourcingOlqs = new ArrayList<>();
		}
		this.sourcingOlqs.add(olq);
	}

	/**
	 * Returns <tt>true</tt> if all lines are marked as processed, otherwise <tt>false</tt>.
	 * 
	 * @return <tt>true</tt> if all lines are marked as processed, otherwise <tt>false</tt>
	 */
	public boolean allLinesProcessed(final List<SourcingLine> lines)
	{
		return getProcessedLines().keySet().containsAll(lines);
	}

	/**
	 * Returns the reserved quantity for a {@link SourcingLine}.
	 * 
	 * @param line instance of {@link SourcingLine}
	 * @return the quantity assigned for the line or 0
	 */
	public int getReservedForLine(final SourcingLine line)
	{
		return this.getQuantityForSku(line.getSku(), null, line.getLineId());
	}

	/**
	 * Returns the reserved quantity of a specific sku at a location.
	 * 
	 * @return the quantity assigned to the location or 0
	 */
	public int getReservedForLocation(final String sku, final String locationId)
	{
		return this.getQuantityForSku(sku, locationId, null);
	}

	public boolean isEmpty()
	{
		return CollectionUtils.isEmpty(this.sourcingOlqs);
	}

	public void setActionForUnprocessedLine(final SourcingLine line, final String action)
	{
		setActionForUnprocessedLines(Collections.singletonList(line), action);
	}


	public void setActionForUnprocessedLines(final List<SourcingLine> lines, final String action)
	{
		if (lineActions == null)
		{
			lineActions = new HashMap<>();
		}
		for (final SourcingLine line : lines)
		{
			if (isUnprocessedLine(line))
			{
				lineActions.put(line, action);
			}
		}
	}

	/**
	 * Returns <tt>true</tt> if the line is not marked as processed, otherwise <tt>false</tt>.
	 * 
	 * @return <tt>true</tt> if the line is not marked as processed, otherwise <tt>false</tt>
	 */
	public boolean isUnprocessedLine(final SourcingLine line)
	{
		return !getProcessedLines().keySet().contains(line);
	}

	/**
	 * Marks the {@link SourcingLine} as processed.
	 */
	public void markLineAsProcessed(final SourcingLine line, final Class<? extends SourcingStrategy> strategyClass)
	{
		if (this.processedLines == null)
		{
			this.processedLines = new HashMap<>();
		}
		this.processedLines.put(line, getClassName(strategyClass));
	}

	/**
	 * Stores the process status for a list of {@link SourcingLine}s if these are unprocessed. Otherwise the existing
	 * status is not changed.
	 */
	public void markAllLinesAsProcessed(final List<SourcingLine> lines, final Class<? extends SourcingStrategy> strategyClass)
	{
		for (final SourcingLine line : lines)
		{
			if (this.isUnprocessedLine(line))
			{
				this.markLineAsProcessed(line, strategyClass);
			}
		}
	}

	/**
	 * Marks the result as processed remembering the strategy. This will prevent any strategies following in the chain to
	 * be executed.
	 */
	public void markResultAsProcessed(final Class<? extends SourcingStrategy> strategyClass)
	{
		processFinished = true;
		strategyName = getClassName(strategyClass);
	}

	public Map<String, Object> getProperties()
	{
		if (properties == null)
		{
			properties = new HashMap<>();
		}
		return properties;
	}

	public void setProperties(final Map<String, Object> properties)
	{
		this.properties = properties;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("processFinished", this.processFinished)
				.append("processedLines", this.processedLines).append("olqs", this.sourcingOlqs).toString();
	}

	protected String getClassName(final Class<? extends SourcingStrategy> strategyClass)
	{
		return strategyClass == null ? null : strategyClass.getSimpleName();
	}

	protected int getQuantityForSku(final String sku, final String filterLocationId, final String filterLineId)
			throws IllegalArgumentException
	{
		int result = 0;
		for (final SourcingOLQ olq : this.getSourcingOlqs())
		{
			final boolean matchesLocationId = filterLocationId == null || olq.getLocationId().equals(filterLocationId);
			final boolean matchesLineId = filterLineId == null || olq.getLineId().equals(filterLineId);
			if (sku.equals(olq.getSku()) && matchesLocationId && matchesLineId)
			{
				result += olq.getQuantity();
			}
		}
		return result;
	}

}
