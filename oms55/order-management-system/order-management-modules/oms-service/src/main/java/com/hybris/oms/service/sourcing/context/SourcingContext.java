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

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Provides access to the {@link SourcingConfiguration} as well as input and status data during sourcing.
 */
public class SourcingContext
{
	private final SourcingConfiguration configuration;
	private final List<SourcingLine> sourcingLines;
	private final SourcingMatrix sourcingMatrix;
	private final ProcessStatus processStatus;

	/**
	 * Creates a new {@link SourcingContext}.
	 */
	public SourcingContext(final List<SourcingLine> sourcingLines, final SourcingMatrix sourcingMatrix,
			final SourcingConfiguration configuration)
	{
		this(sourcingLines, sourcingMatrix, configuration, new ProcessStatus());
	}

	/**
	 * Creates a new {@link SourcingContext}.
	 */
	public SourcingContext(final List<SourcingLine> sourcingLines, final SourcingMatrix sourcingMatrix,
			final SourcingConfiguration configuration, final ProcessStatus processStatus)
	{
		this.configuration = configuration;
		this.sourcingLines = sourcingLines;
		this.sourcingMatrix = sourcingMatrix;
		this.processStatus = processStatus;
	}

	public SourcingConfiguration getConfiguration()
	{
		return this.configuration;
	}

	public List<SourcingLine> getSourcingLines()
	{
		return Collections.unmodifiableList(this.sourcingLines);
	}

	public SourcingMatrix getSourcingMatrix()
	{
		return this.sourcingMatrix;
	}

	public ProcessStatus getProcessStatus()
	{
		return this.processStatus;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("configuration", this.configuration)
				.append("sourcingLines", this.sourcingLines).append("sourcingMatrix", this.sourcingMatrix)
				.append("processStatus", this.processStatus).toString();
	}

}
