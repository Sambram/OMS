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

import com.hybris.oms.domain.order.OrderLineQuantity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class OrderLineQuantitiesList.
 * 
 * @author jsjules
 */

@XmlRootElement(name = "orderLineQuantities")
public class OrderLineQuantitiesList implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<OrderLineQuantity> orderLineQuantities = new ArrayList<>();

	/**
	 * Adds the order.
	 * 
	 * @param orderLineQuantity the order line quantity
	 */
	public void addOrder(final OrderLineQuantity orderLineQuantity)
	{
		if (this.orderLineQuantities == null)
		{
			this.orderLineQuantities = new ArrayList<>();
		}
		this.orderLineQuantities.add(orderLineQuantity);
	}

	/**
	 * Gets the number of order line quantities.
	 * 
	 * @return the number of order line quantities
	 */
	public int getNumberOfOrderLineQuantities()
	{
		return this.orderLineQuantities.size();
	}

	/**
	 * Gets the order line quantities.
	 * 
	 * @return the order line quantities
	 */
	public List<OrderLineQuantity> getOrderLineQuantities()
	{
		return this.orderLineQuantities;
	}

	@XmlElement(name = "orderLineQuantity")
	public List<OrderLineQuantity> getOrderLineQuantityForJaxb()
	{
		return this.orderLineQuantities;
	}

	/**
	 * Initialize order line quantities.
	 * 
	 * @param newOrderLineQuantities the order line quantities
	 */
	public void initializeOrderLineQuantities(final List<OrderLineQuantity> newOrderLineQuantities)
	{
		assert this.orderLineQuantities.isEmpty();
		for (final OrderLineQuantity orderLineQuantity : newOrderLineQuantities)
		{
			this.addOrder(orderLineQuantity);
		}
	}


	/**
	 * Removes the order.
	 * 
	 * @param orderLineQuantity the order line quantity
	 */
	public void removeOrder(final OrderLineQuantity orderLineQuantity)
	{
		this.orderLineQuantities.remove(orderLineQuantity);
	}

	public void setOrderLineQuantityForJaxb(final List<OrderLineQuantity> orderLineQuantity)
	{
		this.orderLineQuantities = orderLineQuantity;
	}

	@SuppressWarnings("unused")
	private void setOrderLineQuantities(final List<OrderLineQuantity> orderLineQuantities)
	{
		this.orderLineQuantities = orderLineQuantities;
	}
}
