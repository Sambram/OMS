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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;


/**
 * Builder to construct a {@link CalculationContext}.
 */
public class CalculationContextBuilder
{
	private ThresholdBuilder thresholdBuilder;

	private AggregateValuesBuilder aggregateValuesBuilder;

	private FormulaParser formulaParser;


	/**
	 * Builds a new {@link CalculationContext} used to calculate ATS.
	 */
	public CalculationContext buildContext(final Set<String> filterSkus, final Set<String> filterLocations,
			final Set<String> atsIds, final boolean global, final List<AtsFormulaData> formulas)
	{
		final List<ParsedFormula> parsedFormulas = new ArrayList<>();
		for (AtsFormulaData formula : formulas)
		{
			parsedFormulas.add(formulaParser.parseFormula(formula.getAtsId(), formula.getFormula()));
		}
		final AtsFilter atsFilter = new AtsFilter(filterSkus, filterLocations, atsIds);
		final Set<StatusRealm> statusRealms = findStatusRealms(parsedFormulas);
		final AggregateValues aggregateValues = aggregateValuesBuilder.build(atsFilter, global, statusRealms);
		final LocationThreshold locationThreshold = thresholdBuilder.getLocationThreshold(global, aggregateValues.getLocationIds());
		return buildContext(global, atsFilter, aggregateValues, locationThreshold, parsedFormulas);
	}

	protected Set<StatusRealm> findStatusRealms(List<ParsedFormula> parsedFormulas)
	{
		Set<StatusRealm> statusRealms = new HashSet<>();
		for (ParsedFormula parsedFormula : parsedFormulas)
		{
			for (Operand operand : parsedFormula.getOperands())
			{
				statusRealms.add(operand.getStatusRealm());
			}
		}
		return statusRealms;
	}

	protected CalculationContext buildContext(final boolean global, final AtsFilter atsFilter,
			final AggregateValues aggregateValues, final LocationThreshold locationThreshold,
			final List<ParsedFormula> parsedFormulas)
	{
		return new CalculationContext(atsFilter, aggregateValues, locationThreshold, global, parsedFormulas);
	}

	@Required
	protected ThresholdBuilder getThresholdBuilder()
	{
		return thresholdBuilder;
	}

	public void setThresholdBuilder(final ThresholdBuilder thresholdBuilder)
	{
		this.thresholdBuilder = thresholdBuilder;
	}

	@Required
	protected AggregateValuesBuilder getAggregateValuesBuilder()
	{
		return aggregateValuesBuilder;
	}

	public void setAggregateValuesBuilder(final AggregateValuesBuilder aggregateValuesBuilder)
	{
		this.aggregateValuesBuilder = aggregateValuesBuilder;
	}

	protected FormulaParser getFormulaParser()
	{
		return formulaParser;
	}

	@Required
	public void setFormulaParser(final FormulaParser formulaParser)
	{
		this.formulaParser = formulaParser;
	}

}
