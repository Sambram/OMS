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



import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class SkuQuantityOrderLineQuantityStatusList.
 */
@XmlRootElement(name = "rules")
public class RuleList
{
	private List<Rule> list = new ArrayList<>();

	/**
	 * Adds the.
	 * 
	 * @param element the element
	 */
	public void add(final Rule element)
	{
		if (this.list == null)
		{
			this.list = new ArrayList<>();
		}
		this.list.add(element);
	}

	/**
	 * Gets the list.
	 * 
	 * @return the list
	 */
	public List<Rule> getList()
	{
		return this.list;
	}

	@XmlElement(name = "rule")
	public List<Rule> getRuleForJaxb()
	{
		return this.list;
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public int getSize()
	{
		return this.list.size();
	}

	/**
	 * Initialize.
	 * 
	 * @param newList the list
	 */
	public void initialize(final List<Rule> newList)
	{
		assert this.list.isEmpty();
		for (final Rule element : newList)
		{
			this.add(element);
		}
	}

	/**
	 * Removes the.
	 * 
	 * @param element the element
	 */
	public void remove(final Rule element)
	{
		this.list.remove(element);
	}

	public void setRuleForJaxb(final List<Rule> rule)
	{
		this.list = rule;
	}


}
