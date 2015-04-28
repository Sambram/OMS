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

import com.hybris.oms.service.ats.FormulaSyntaxException;
import com.hybris.oms.service.ats.StatusRealm;
import com.hybris.oms.service.ats.StatusRealmRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;


/**
 * Creates a {@link ParsedFormula} from the given formula.
 */
public class FormulaParser implements InitializingBean
{
	private static final String REALM_PREFIXES = "$REALM_PREFIXES$";
	private static final String QUESTION_MARK = "?";
	private static final String REALM_SEPARATOR = "|";
	private static final String SIGN_PATTERN = "([\\+-])";
	private static final String DEFAULT_OPERAND_PATTERN = "((($REALM_PREFIXES$)\\[([^\\[\\]]+)\\])|T)";
	private static final int GROUP_SIGN = 1;
	private static final int DEFAULT_GROUP_REALM = 4;
	private static final int DEFAULT_GROUP_STATUS = 5;
	private static final int DEFAULT_GROUP_THRESHOLD = 2;
	private static final String MINUS = "-";
	private static final String THRESHOLD = "T";

	private Pattern firstOperand;
	private Pattern operand;

	private StatusRealmRegistry realmRegistry;
	private String operandPattern = DEFAULT_OPERAND_PATTERN;
	private int statusGroup = DEFAULT_GROUP_STATUS;
	private int realmGroup = DEFAULT_GROUP_REALM;
	private int thresholdGroup = DEFAULT_GROUP_THRESHOLD;

	/**
	 * Parses a given formula string.
	 * 
	 * @return {@link ParsedFormula}
	 * @throws FormulaSyntaxException if the formula cannot be parsed
	 */
	public ParsedFormula parseFormula(final String atsId, final String formula)
	{
		if (StringUtils.isBlank(formula))
		{
			throw new FormulaSyntaxException("missing formula");
		}
		final List<Operand> operands = new ArrayList<>();
		final boolean hasThreshold = parseInternal(formula, operands);
		if (operands.isEmpty())
		{
			throw new FormulaSyntaxException("invalid formula: At least one operand expected (" + formula + ')');
		}
		return new ParsedFormula(atsId, operands, hasThreshold);
	}

	/**
	 * Parses a list of {@link Operand} from the given formula and returns <tt>true</tt> if the formula contains a
	 * threshold.
	 * 
	 * @return <tt>true</tt> if the formula contains a threshold
	 * @throws FormulaSyntaxException if the formula cannot be parsed
	 */
	protected boolean parseInternal(final String formula, final List<Operand> operands)
	{
		boolean hasThreshold = false;
		boolean first = true;
		Matcher matcher = firstOperand.matcher(formula);
		int length = formula.length();
		for (int endIdx = 0; endIdx < length;)
		{
			if (!matcher.find(endIdx))
			{
				throw new FormulaSyntaxException("invalid formula: only a part of the formula could be matched (" + formula + ')');
			}
			if (matchThreshold(matcher, hasThreshold))
			{
				hasThreshold = true;
			}
			else
			{
				operands.add(matchOperand(matcher));
			}
			if (first && matcher.end() < formula.length())
			{
				first = false;
				final String formulaPart = formula.substring(matcher.end());
				matcher = operand.matcher(formulaPart);
				endIdx = 0;
				length = formulaPart.length();
			}
			else
			{
				endIdx = matcher.end();
			}
		}
		return hasThreshold;
	}

	/**
	 * Matches a single operand using the given {@link Matcher} and the configured statusPattern, statusGroup and
	 * statusRealm.
	 * 
	 * @return {@link Operand}
	 */
	protected Operand matchOperand(final Matcher matcher)
	{
		final boolean negativeSign = MINUS.equals(matcher.group(GROUP_SIGN));
		final StatusRealm realm = realmRegistry.getRealmForPrefix(matcher.group(realmGroup));
		final String statusCode = matcher.group(statusGroup);
		return new Operand(negativeSign, realm, statusCode);
	}

	/**
	 * Matches a threshold using the given {@link Matcher}.
	 * 
	 * @return <tt>true</tt> if the expression represents a threshold.
	 * @throws FormulaSyntaxException if the threshold is defined twice or is added.
	 */
	protected boolean matchThreshold(final Matcher matcher, final boolean hasThreshold)
	{
		boolean result = false;
		final String threshold = matcher.group(thresholdGroup);
		if (THRESHOLD.equals(threshold))
		{
			final boolean negativeSign = MINUS.equals(matcher.group(GROUP_SIGN));
			if (hasThreshold)
			{
				throw new FormulaSyntaxException("Duplicate threshold definition in formula");
			}
			if (!negativeSign)
			{
				throw new FormulaSyntaxException("Threshold may only be subtracted");
			}
			result = true;
		}
		return result;
	}

	@Override
	public void afterPropertiesSet()
	{
		final StringBuffer realmPattern = new StringBuffer();
		for (final StatusRealm realm : realmRegistry.getRegisteredRealms())
		{
			if (realmPattern.length() > 0)
			{
				realmPattern.append(REALM_SEPARATOR);
			}
			realmPattern.append(realm.getPrefix());
		}
		this.firstOperand = Pattern.compile((SIGN_PATTERN + QUESTION_MARK + operandPattern).replace(REALM_PREFIXES, realmPattern));
		this.operand = Pattern.compile((SIGN_PATTERN + operandPattern).replace(REALM_PREFIXES, realmPattern));
	}

	public Pattern getFirstOperand()
	{
		return firstOperand;
	}

	public void setFirstOperand(final Pattern firstOperand)
	{
		this.firstOperand = firstOperand;
	}

	public Pattern getOperand()
	{
		return operand;
	}

	public void setOperand(final Pattern operand)
	{
		this.operand = operand;
	}

	@Required
	protected StatusRealmRegistry getRealmRegistry()
	{
		return realmRegistry;
	}

	public void setRealmRegistry(final StatusRealmRegistry realmRegistry)
	{
		this.realmRegistry = realmRegistry;
	}

	protected String getOperandPattern()
	{
		return operandPattern;
	}

	public void setOperandPattern(final String operandPattern)
	{
		this.operandPattern = operandPattern;
	}

	protected int getStatusGroup()
	{
		return statusGroup;
	}

	public void setStatusGroup(final int statusGroup)
	{
		this.statusGroup = statusGroup;
	}

	protected int getRealmGroup()
	{
		return realmGroup;
	}

	public void setRealmGroup(final int realmGroup)
	{
		this.realmGroup = realmGroup;
	}

	protected int getThresholdGroup()
	{
		return thresholdGroup;
	}

	public void setThresholdGroup(final int thresholdGroup)
	{
		this.thresholdGroup = thresholdGroup;
	}

}
