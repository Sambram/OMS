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

import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class RuleParameter.
 */
@XmlRootElement
public class RuleParameter implements Dto
{
	private static final long serialVersionUID = -6210324715176340968L;

	private String displayText;
	private RuleParameterKey key;
	private Long parameterId;
	private String value;

	/**
	 * No argument constructor.
	 */
	public RuleParameter()
	{
		// default constructor
	}

	/**
	 * minimal constructor.
	 * 
	 * @param paramKey the param key
	 * @param paramValue the param value
	 */
	public RuleParameter(final RuleParameterKey paramKey, final String paramValue)
	{
		this.key = paramKey;
		this.value = paramValue;
	}

	/**
	 * full constructor.
	 * 
	 * @param displayText the display text
	 * @param paramKey the param key
	 * @param paramValue the param value
	 */
	public RuleParameter(final String displayText, final RuleParameterKey paramKey, final String paramValue)
	{
		this.displayText = displayText;
		this.key = paramKey;
		this.value = paramValue;
	}

	/**
	 * Get the display text for this parameter.
	 * 
	 * @return the display text
	 */
	public String getDisplayText()
	{
		return this.displayText;
	}

	/**
	 * Get the parameter key.
	 * 
	 * @return the key
	 */
	public RuleParameterKey getKey()
	{
		return this.key;
	}

	/**
	 * Gets the parameter id.
	 * 
	 * @return the parameter id
	 */
	public Long getParameterId()
	{
		return this.parameterId;
	}

	/**
	 * Get the parameter value.
	 * 
	 * @return the value
	 */
	public String getValue()
	{
		return this.value;
	}

	/**
	 * Set the text to be displayed for this parameter. For example, the display
	 * text for a sku code id long might be the actual text sku code
	 * 
	 * @param displayText the new display text
	 */
	public void setDisplayText(final String displayText)
	{
		this.displayText = displayText;
	}

	/**
	 * Set the parameter key.
	 * 
	 * @param key the new key
	 */
	public void setKey(final RuleParameterKey key)
	{
		this.key = key;
	}

	/**
	 * Set the parameter value.
	 * 
	 * @param value the new value
	 */
	public void setValue(final String value)
	{
		this.value = value;
	}

	/**
	 * Sets the parameter id.
	 * 
	 * @param parameterId the new parameter id
	 */
	protected void setParameterId(final Long parameterId)
	{
		this.parameterId = parameterId;
	}

}
