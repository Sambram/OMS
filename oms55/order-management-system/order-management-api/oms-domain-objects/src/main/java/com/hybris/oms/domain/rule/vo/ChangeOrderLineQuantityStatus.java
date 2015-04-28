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
package com.hybris.oms.domain.rule.vo;

import com.hybris.commons.dto.Dto;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Class ChangeOrderLineQuantityStatus.
 * 
 * @author plaguemorin
 *         Date: 03/05/12
 *         Time: 6:35 PM
 */
@XmlRootElement
public class ChangeOrderLineQuantityStatus implements Dto
{
	private static final long serialVersionUID = 2566258742290842922L;

	/** The sku. */
	private String sku;

	/** The location. */
	private String location;

	/** The previous status. */
	private String previousStatus;

	/** The current status. */
	private String currentStatus;

	/** The quantity. */
	private int quantity;

	/**
	 * Instantiates a new change order line quantity status.
	 */
	public ChangeOrderLineQuantityStatus()
	{
		// Do nothing
	}

	/**
	 * Instantiates a new change order line quantity status.
	 * 
	 * @param sku the sku
	 * @param location the location
	 * @param quantity the quantity
	 * @param previousStatus the previous status
	 * @param currentStatus the current status
	 */
	public ChangeOrderLineQuantityStatus(final String sku, final String location, final int quantity, final String previousStatus,
			final String currentStatus)
	{
		this.sku = sku;
		this.location = location;
		this.quantity = quantity;
		this.previousStatus = previousStatus;
		this.currentStatus = currentStatus;
	}

	/**
	 * Gets the sku.
	 * 
	 * @return the sku
	 */
	public String getSku()
	{
		return this.sku;
	}

	/**
	 * Sets the sku.
	 * 
	 * @param sku the new sku
	 */
	public void setSku(final String sku)
	{
		this.sku = sku;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public String getLocation()
	{
		return this.location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location the new location
	 */
	public void setLocation(final String location)
	{
		this.location = location;
	}

	/**
	 * Gets the previous status.
	 * 
	 * @return the previous status
	 */
	public String getPreviousStatus()
	{
		return this.previousStatus;
	}

	/**
	 * Sets the previous status.
	 * 
	 * @param previousStatus the new previous status
	 */
	public void setPreviousStatus(final String previousStatus)
	{
		this.previousStatus = previousStatus;
	}

	/**
	 * Gets the current status.
	 * 
	 * @return the current status
	 */
	public String getCurrentStatus()
	{
		return this.currentStatus;
	}

	/**
	 * Sets the current status.
	 * 
	 * @param currentStatus the new current status
	 */
	public void setCurrentStatus(final String currentStatus)
	{
		this.currentStatus = currentStatus;
	}


	@Override
	public String toString()
	{
		return "ChangeOrderLineQuantityStatus [sku=" + this.sku + ", location=" + this.location + ", previousStatus="
				+ this.previousStatus + ", currentStatus=" + this.currentStatus + ']';
	}

	/**
	 * Gets the quantity.
	 * 
	 * @return the quantity
	 */
	public int getQuantity()
	{
		return this.quantity;
	}

	/**
	 * Sets the quantity.
	 * 
	 * @param quantity the new quantity
	 */
	public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}

}
