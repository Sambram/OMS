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

import com.hybris.oms.service.ats.AggregationToFormulaMatcher;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * Default implementation of aggregation to formula matcher.
 */
public class DefaultAggregationToFormulaMatcher implements AggregationToFormulaMatcher
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultAggregationToFormulaMatcher.class);
	private AtsService atsService;
	private FormulaParser formulaParser;

	@Override
	public boolean hasMatchingFormula(final String aggregationStatusCode, final StatusRealm statusRealm)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(aggregationStatusCode), "Aggregation status code is not valid");
		Preconditions.checkArgument(statusRealm != null, "Status realm cannot be null");

		final List<AtsFormulaData> formulas = findAllFormulas();
		return isAggregateMatchToFormula(aggregationStatusCode, statusRealm, formulas);
	}

	@Override
	public boolean hasMatchingFormula(final String aggregationStatusCode, final StatusRealm statusRealm,
			final List<String> formulaIds)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(aggregationStatusCode), "Aggregation status code is not valid");
		Preconditions.checkArgument(statusRealm != null, "Status realm cannot be null");
		Preconditions.checkArgument(formulaIds != null, "Formula ids cannot be null");

		final List<AtsFormulaData> formulas = findFormulas(formulaIds);
		return isAggregateMatchToFormula(aggregationStatusCode, statusRealm, formulas);
	}

	protected boolean isAggregateMatchToFormula(final String aggregationStatusCode, final StatusRealm statusRealm,
			final List<AtsFormulaData> formulas)
	{
		final List<ParsedFormula> parsedFormulas = parseFormulas(formulas);

		for (final ParsedFormula parsedFormula : parsedFormulas)
		{
			final Set<String> statusCodes = parsedFormula.getStatusCodes(statusRealm);
			if (statusCodes.contains(aggregationStatusCode))
			{
				return true;
			}
		}
		return false;
	}

	protected List<ParsedFormula> parseFormulas(final List<AtsFormulaData> formulas)
	{
		final List<ParsedFormula> parsedFormulaList = new ArrayList<>();
		for (final AtsFormulaData formula : formulas)
		{
			parsedFormulaList.add(formulaParser.parseFormula(formula.getAtsId(), formula.getFormula()));
		}
		return parsedFormulaList;
	}

	/**
	 * Finds all {@link AtsFormulaData}s for the given formulaIds. Logs a warning if any formulaId cannot be found.
	 * 
	 * @param formulaIds atsIds to search for, cannot be <tt>null</tt>
	 * @return list with {@link AtsFormulaData}
	 */
	protected List<AtsFormulaData> findFormulas(final List<String> formulaIds)
	{
		final Set<String> atsIds = new HashSet<String>(formulaIds);
		final List<AtsFormulaData> formulas = atsService.findFormulas(atsIds);
		for (final AtsFormulaData formula : formulas)
		{
			atsIds.remove(formula.getAtsId());
		}
		if (!atsIds.isEmpty())
		{
			LOG.warn("Formulas could not be be found. Unknown atsIds: " + atsIds);
		}
		return formulas;
	}

	protected List<AtsFormulaData> findAllFormulas()
	{
		return atsService.findFormulas(null);
	}

	@Required
	public void setAtsService(final AtsService atsService)
	{
		this.atsService = atsService;
	}

	@Required
	public void setFormulaParser(final FormulaParser formulaParser)
	{
		this.formulaParser = formulaParser;
	}

}
