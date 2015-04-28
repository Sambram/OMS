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
package com.hybris.oms.domain.order.jaxb;

import com.hybris.oms.domain.order.OrderLineQuantityStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "orderLineQuantityStatuses")
public class OrderLineQuantityStatusesList implements Serializable
{
	private static final long serialVersionUID = -2137648917376155855L;
	private List<OrderLineQuantityStatus> list = new ArrayList<>();

	/**
	 * Adds the.
	 * 
	 * @param element the element
	 */
	public void add(final OrderLineQuantityStatus element)
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
	public List<OrderLineQuantityStatus> getList()
	{
		return Collections.unmodifiableList(this.list);
	}

	@XmlElement(name = "orderLineQuantityStatus")
	public List<OrderLineQuantityStatus> getOrderLineQuantityStatusForJaxb()
	// CHECKSTYLE IGNORE StrictDuplicateCode NEXT 20 LINES
	{
		return this.list;
	}

	/**
	 * Gets the list size.
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
	public void initialize(final List<OrderLineQuantityStatus> newList)
	{
		assert this.list.isEmpty();
		for (final OrderLineQuantityStatus element : newList)
		{
			this.add(element);
		}
	}

	/**
	 * Removes the.
	 * 
	 * @param element the element
	 */
	public void remove(final OrderLineQuantityStatus element)
	{
		this.list.remove(element);
	}

	public void setOrderLineQuantityStatusForJaxb(final List<OrderLineQuantityStatus> orderLineQuantityStatus)
	{
		this.list = orderLineQuantityStatus;
	}


}
