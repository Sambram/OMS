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
package com.hybris.oms.export.api.ats;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Contains all ats values.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AvailabilityToSellDTO implements Serializable
{
	private static final long serialVersionUID = 9222744268784268772L;

	@XmlAttribute
	private Long latestChange;

	@XmlElement(name = "quantity")
	private Collection<AvailabilityToSellQuantityDTO> quantities;

	/**
	 * Gets the latestChange for the next poll.
	 *
	 * @return the latestChange
	 */
	public Long getLatestChange()
	{
		return this.latestChange;
	}

	/**
	 * Sets the latestChange to use.
	 *
	 * @param latestChange
	 *           the latestChange to use
	 */
	public void setLatestChange(final Long latestChange)
	{
		this.latestChange = latestChange;
	}

	/**
	 * Gets ats values for current polling operation.
	 *
	 * @return the ats values or empty collection if no available
	 */
	public Collection<AvailabilityToSellQuantityDTO> getQuantities()
	{
		if (this.quantities == null)
		{
			this.quantities = new LinkedList<AvailabilityToSellQuantityDTO>();
		}
		return this.quantities;
	}

	/**
	 * @param quantities the quantities to set
	 */
	public void setQuantities(final Collection<AvailabilityToSellQuantityDTO> quantities)
	{
		this.quantities = quantities;
	}


}
