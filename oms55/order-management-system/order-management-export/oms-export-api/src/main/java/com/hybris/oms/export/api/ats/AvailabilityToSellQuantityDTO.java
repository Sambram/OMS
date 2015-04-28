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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;



/**
 * Represents an ats value of a sku, identified by it's location and ats formula.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AvailabilityToSellQuantityDTO implements Serializable
{
	private static final long serialVersionUID = -8537710473819716759L;

	@XmlAttribute
	private String locationId;

	@XmlAttribute
	private String skuId;

	@XmlValue
	private int quantity;

	/**
	 * Gets the location id identifying this ats value.
	 *
	 * @return the location id
	 */
	public String getLocationId()
	{
		return this.locationId;
	}

	/**
	 * Sets the location id to use for this ats value.
	 *
	 * @param locationId the location to use
	 */
	public void setLocationId(final String locationId)
	{
		this.locationId = locationId;
	}

	/**
	 * Gets the ats value of a product in relation to the location and ats formula.
	 *
	 * @return the ats value
	 */
	public int getQuantity()
	{
		return this.quantity;
	}

	/**
	 * Sets the ats value for this location and ats formula.
	 *
	 * @param quantity the ats value to use
	 */
	public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Gets the sku identifier.
	 *
	 * @return the skuId
	 */
	public String getSkuId()
	{
		return this.skuId;
	}

	/**
	 * Sets the sku identifier.
	 *
	 * @param skuId
	 */
	public void setSkuId(final String skuId)
	{
		this.skuId = skuId;
	}
}
