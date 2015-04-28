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

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Component to calculate Ats from a list of {@link AtsFormulaData}s and a prepared {@link CalculationContext}.
 */
public class AtsCalculator
{
	private KeyBuilder keyBuilder;

	private FormulaParser formulaParser;

	/**
	 * Calculate Ats from a list of {@link AtsFormulaData}s and a prepared {@link CalculationContext}.
	 */
	public AtsResult calculateAts(final List<AtsFormulaData> atsFormulas, final CalculationContext context)
	{
		boolean formulaExists = false;
		final AtsResult result = new AtsResult();
		for (final AtsFormulaData formula : atsFormulas)
		{
			final String atsId = formula.getAtsId();
			if (context.getAtsFilter().filterAtsId(atsId))
			{
				formulaExists = true;
				final ParsedFormula parsedFormula = formulaParser.parseFormula(atsId, formula.getFormula());
				for (final String sku : context.getAggregateValues().getSkus())
				{
					for (final String locationId : context.getAggregateValues().getLocationIds())
					{
						calculateAtsWithThresholdAndOffset(sku, locationId, parsedFormula, result, context);
					}
				}
			}
		}
		if (!context.getAtsFilter().getAtsIds().isEmpty() && !formulaExists)
		{
			throw new EntityNotFoundException("No formula found for given atsIds (" + context.getAtsFilter().getAtsIds() + ')');
		}
		return result;
	}

	/**
	 * Calculates a single ATS value for the given sku and locationId using the {@link ParsedFormula}. For global
	 * calculations, the quantity unassigned provided within {@link AggregateValues} is subtracted. If the formula
	 * defines a threshold, this is calculated by {@link LocationThreshold} and subtracted from the ATS value. Finally,
	 * the value is added to the {@link AtsResult}.
	 */
	protected void calculateAtsWithThresholdAndOffset(final String sku, final String locationId,
			final ParsedFormula parsedFormula, final AtsResult result, final CalculationContext context)
	{
		final Integer ats = parsedFormula.calculate(sku, locationId, keyBuilder, context);

		if (ats != null)
		{
			int atsValue = ats.intValue();
			final Integer offset = context.getAggregateValues().getUnassignedQuantity(sku);
			if (offset != null)
			{
				atsValue -= offset.intValue();
			}
			if (parsedFormula.isThreshold())
			{
				atsValue = context.getThreshold().calculateAtsWithThreshold(atsValue, locationId);
			}
			result.addResult(keyBuilder.getLocationKey(locationId), sku, parsedFormula.getAtsId(), atsValue, (new Date()).getTime());
		}
		// if ATS is null it is fine, some values are expected not to exist
	}

	@Required
	public void setKeyBuilder(final KeyBuilder keyBuilder)
	{
		this.keyBuilder = keyBuilder;
	}

	protected KeyBuilder getKeyBuilder()
	{
		return keyBuilder;
	}

	@Required
	public void setFormulaParser(final FormulaParser formulaParser)
	{
		this.formulaParser = formulaParser;
	}

	protected FormulaParser getFormulaParser()
	{
		return formulaParser;
	}
}
