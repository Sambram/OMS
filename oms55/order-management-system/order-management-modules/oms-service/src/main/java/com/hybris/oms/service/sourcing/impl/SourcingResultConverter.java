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
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.SourcingResult.ResultEnum;
import com.hybris.oms.service.sourcing.context.ProcessStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.CollectionUtils;


/**
 * Builder to create the {@link SourcingResult}.
 */
public abstract class SourcingResultConverter
{
	public SourcingResult buildSourcingResult(final List<SourcingLine> sourcingLines, final ProcessStatus status)
	{
		final DefaultSourcingResult result = getSourcingResult();
		result.setInputLines(sourcingLines);
		result.setSourcingOlqs(status.getSourcingOlqs());
		Map<String, String> lineActions = null;
		for (final Entry<SourcingLine, String> entry : status.getLineActions().entrySet())
		{
			if (lineActions == null)
			{
				lineActions = new HashMap<>();
			}
			lineActions.put(entry.getKey().getOrderLineId(), entry.getValue());
		}
		result.setLineActions(lineActions);
		if (CollectionUtils.isEmpty(sourcingLines))
		{
			result.setStatus(ResultEnum.EMPTY);
		}
		else
		{
			int totalReserved = 0;
			int totalRequested = 0;
			final Map<String, ResultEnum> lineStatus = new HashMap<>();
			for (final SourcingLine line : sourcingLines)
			{
				final int reservedForLine = status.getReservedForLine(line);
				totalReserved += reservedForLine;
				totalRequested += line.getQuantity();
				lineStatus.put(line.getLineId(), retrieveStatus(reservedForLine, line.getQuantity()));
			}
			result.setStatus(retrieveStatus(totalReserved, totalRequested));
			result.setLineStatus(lineStatus);
		}
		result.setStrategyName(status.getStrategyName());
		return result;
	}

	public abstract DefaultSourcingResult getSourcingResult();

	protected ResultEnum retrieveStatus(final int reserved, final int requested)
	{
		ResultEnum result = ResultEnum.FAILURE;
		if (reserved == requested)
		{
			result = ResultEnum.SUCCESS;
		}
		else if (reserved > 0)
		{
			result = ResultEnum.PARTIAL;
		}
		return result;
	}
}
