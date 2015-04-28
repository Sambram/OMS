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
package com.hybris.oms.service.sourcing.impl;

import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.SourcingResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Default implementation of {@link SourcingResult}.
 */
public class DefaultSourcingResult implements SourcingResult
{
	private List<SourcingLine> inputLines;
	private Map<String, String> lineActions;
	private Map<String, SourcingResult.ResultEnum> lineStatus;
	private SourcingResult.ResultEnum status;
	private List<SourcingOLQ> sourcingOlqs;
	private String strategyName;
	private Map<String, Object> properties;

	public DefaultSourcingResult()
	{
		status = ResultEnum.EMPTY;
	}

	@Override
	public Map<String, String> getLineActions()
	{
		return lineActions == null ? Collections.<String, String>emptyMap() : Collections.unmodifiableMap(lineActions);
	}

	public void setLineActions(final Map<String, String> lineActions)
	{
		this.lineActions = lineActions;
	}

	@Override
	public List<SourcingOLQ> getSourcingOlqs()
	{
		return sourcingOlqs == null ? Collections.<SourcingOLQ>emptyList() : Collections.unmodifiableList(sourcingOlqs);
	}

	public void setSourcingOlqs(final List<SourcingOLQ> sourcingOlqs)
	{
		this.sourcingOlqs = sourcingOlqs;
	}

	@Override
	public String getStrategyName()
	{
		return strategyName;
	}

	public void setStrategyName(final String strategyName)
	{
		this.strategyName = strategyName;
	}

	@Override
	public Map<String, Object> getProperties()
	{
		return properties == null ? Collections.<String, Object>emptyMap() : Collections.unmodifiableMap(properties);
	}

	public void setProperties(final Map<String, Object> properties)
	{
		this.properties = properties;
	}

	@Override
	public SourcingResult.ResultEnum getStatus()
	{
		return status;
	}

	public void setStatus(final SourcingResult.ResultEnum status)
	{
		this.status = status;
	}

	@Override
	public List<SourcingLine> getInputLines()
	{
		return inputLines == null ? Collections.<SourcingLine>emptyList() : Collections.unmodifiableList(inputLines);
	}

	public void setInputLines(final List<SourcingLine> inputLines)
	{
		this.inputLines = inputLines;
	}

	@Override
	public Map<String, SourcingResult.ResultEnum> getLineStatus()
	{
		return lineStatus == null ? Collections.<String, SourcingResult.ResultEnum>emptyMap() : Collections
				.unmodifiableMap(lineStatus);
	}

	public void setLineStatus(final Map<String, SourcingResult.ResultEnum> lineStatus)
	{
		this.lineStatus = lineStatus;
	}

	@Override
	public boolean isEmpty()
	{
		return getSourcingOlqs().isEmpty() && getLineActions().isEmpty();
	}

	@Override
	public String getActionForLineId(final String lineId)
	{
		return getLineActions().get(lineId);
	}

	@Override
	public ResultEnum getStatusForLineId(final String lineId)
	{
		return getLineStatus().get(lineId);
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("status", this.status)
				.append("lineStatus", this.lineStatus).append("lineActions", this.lineActions).append("olqs", this.sourcingOlqs)
				.toString();
	}
}
