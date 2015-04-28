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

import com.hybris.oms.service.ats.StatusRealm;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.base.Preconditions;


/**
 * Represents the status of a formula after parsing by {@link FormulaParser}.
 */
public class ParsedFormula
{
	private final String atsId;

	private final List<Operand> operands;

	private final boolean threshold;

	/**
	 * @param operands
	 * @param threshold <dt><b>Preconditions:</b>
	 *           <dd>operands must not be empty.
	 * @throws IllegalArgumentException if preconditions are not met.
	 */
	public ParsedFormula(final String atsId, final List<Operand> operands, final boolean threshold)
			throws IllegalArgumentException
	{
		Preconditions.checkArgument(CollectionUtils.isNotEmpty(operands));
		this.atsId = atsId;
		this.operands = operands;
		this.threshold = threshold;
	}

	public List<Operand> getOperands()
	{
		return Collections.unmodifiableList(this.operands);
	}

	/**
	 * Retrieves the status codes used in the formula within a given realm.
	 * 
	 * @param realm {@link StatusRealm}, must not be null.
	 * @return a set of status codes, never <tt>null</tt>
	 * @throws IllegalArgumentException if realm is null
	 */
	public Set<String> getStatusCodes(final StatusRealm realm)
	{
		Preconditions.checkArgument(realm != null, "realm must not be null");
		Set<String> result = null;
		for (final Operand operand : this.operands)
		{
			if (operand.getStatusRealm().equals(realm))
			{
				if (result == null)
				{
					result = new HashSet<>();
				}
				result.add(operand.getStatusCode());
			}
		}
		return result == null ? Collections.<String>emptySet() : result;
	}

	public boolean isThreshold()
	{
		return this.threshold;
	}

	public String getAtsId()
	{
		return atsId;
	}

	/**
	 * Calculates a single ATS value for the given sku and locationId. The calculation
	 * iterates over all {@link Operand} values, retrieves the associated aggregate value from {@link AggregateValues}
	 * and adds or subtracts the value from the sum. If no aggregate values are available, <tt>null</tt> is returned.
	 * 
	 * @return ATS value for the given sku and locationId or <tt>null</tt> if no aggregate values are existing for these
	 *         parameters.
	 */
	public Integer calculate(final String sku, final String locationId, final KeyBuilder keyBuilder,
			final CalculationContext context)
	{
		Integer result = null;
		for (final Operand operand : getOperands())
		{
			final Integer value = operand.calculate(sku, locationId, keyBuilder, context);
			if (value != null)
			{
				int ats = value.intValue();
				if (result != null)
				{
					ats += result.intValue();
				}
				result = Integer.valueOf(ats);
			}
		}
		return result;
	}

}
