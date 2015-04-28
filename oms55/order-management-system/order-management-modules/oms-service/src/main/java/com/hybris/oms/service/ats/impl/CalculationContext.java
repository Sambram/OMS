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
package com.hybris.oms.service.ats.impl;

import java.util.Collections;
import java.util.List;


/**
 * Represents common parameters used during ATS calculation.
 */
public class CalculationContext
{
	private final AggregateValues aggregateValues;
	private final LocationThreshold threshold;
	private final AtsFilter atsFilter;
	private final boolean global;
	private final List<ParsedFormula> parsedFormulas;

	public CalculationContext(final AtsFilter atsFilter, final AggregateValues aggregateValues, final LocationThreshold threshold,
			final boolean global, final List<ParsedFormula> parsedFormulas)
	{
		this.atsFilter = atsFilter;
		this.aggregateValues = aggregateValues;
		this.threshold = threshold;
		this.global = global;
		this.parsedFormulas = parsedFormulas;
	}

	public AtsFilter getAtsFilter()
	{
		return atsFilter;
	}

	public AggregateValues getAggregateValues()
	{
		return aggregateValues;
	}

	public LocationThreshold getThreshold()
	{
		return threshold;
	}

	public boolean isGlobal()
	{
		return global;
	}

	public List<ParsedFormula> getParsedFormulas()
	{
		return Collections.unmodifiableList(parsedFormulas);
	}

}
