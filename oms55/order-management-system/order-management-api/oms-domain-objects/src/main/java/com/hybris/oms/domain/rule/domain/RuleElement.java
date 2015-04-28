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
package com.hybris.oms.domain.rule.domain;

import com.hybris.commons.dto.Dto;
import com.hybris.oms.domain.rule.enums.RuleParameterKey;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class RuleElement.
 */
@XmlRootElement
public abstract class RuleElement implements Dto
{
	private static final long serialVersionUID = 2316708703979187155L;

	private List<RuleParameter> parameters = new ArrayList<>();
	private int sequenceNr = 1;

	/**
	 * default constructor.
	 */
	public RuleElement()
	{
		// default constructor.
	}

	/**
	 * full constructor.
	 * 
	 * @param ruleParameters the rule parameters
	 */
	public RuleElement(final List<RuleParameter> ruleParameters)
	{
		this.parameters = ruleParameters;
	}

	/**
	 * Add a parameter of this rule condition.
	 * 
	 * @param ruleParameter the rule parameter
	 */
	public void addParameter(final RuleParameter ruleParameter)
	{
		this.parameters.add(ruleParameter);
	}

	public abstract String getKind();

	/**
	 * Gets the param.
	 * 
	 * @param parameterKey the parameter key
	 * @return the param
	 */
	public RuleParameter getParam(final RuleParameterKey parameterKey)
	{
		for (final RuleParameter param : this.getParameters())
		{
			if (parameterKey == param.getKey())
			{
				return param;
			}
		}
		return null;
	}

	/**
	 * Get the parameters associated with this rule condition.
	 * 
	 * @return the parameters
	 */
	@XmlElement
	public List<RuleParameter> getParameters()
	{
		return this.parameters;
	}

	/**
	 * Returns the value of a parameter with the specified key.
	 * 
	 * @param key
	 *           - The key of the parameter to be returned
	 * @return the value of the parameter with the specified key or "" if no
	 *         matching parameter was found.
	 */
	public String getParamValue(final RuleParameterKey key)
	{
		final RuleParameter ruleParameter = this.getParam(key);
		return (ruleParameter != null) ? ruleParameter.getValue() : "";
	}

	/**
	 * Gets the sequence nr.
	 * 
	 * @return the sequence nr
	 */
	@XmlElement
	public int getSequenceNr()
	{
		return this.sequenceNr;
	}

	/**
	 * Set the parameters of this rule condition.
	 * 
	 * @param parameters the new parameters
	 */
	public void setParameters(final List<RuleParameter> parameters)
	{
		this.parameters = parameters;
	}

	/**
	 * Set the sequenceNr for the condition/action per rule.
	 * 
	 * @param sequenceNr
	 *           the sequence number of the rule
	 */
	public void setSequenceNr(final int sequenceNr)
	{
		if (sequenceNr == 0L)
		{
			this.sequenceNr = 1;
		}
		else
		{
			this.sequenceNr = sequenceNr;
		}
	}

	@Override
	public String toString()
	{
		return this.getClass().getName() + ", parameters=" + this.parameters + ']';
	}


}
