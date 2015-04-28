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
package com.hybris.oms.domain.rule;

import com.hybris.commons.dto.Dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class RuleShort implements Dto
{
	private static final long serialVersionUID = 5842303289355941836L;

	private String olqFromStatus;
	private String olqToStatus;
	private String inventoryStatus;
	private String updateStrategy;
	private boolean positive;
	static final int NEGATIVE_INVENTORY_CHANGE = -1;
	static final int POSITIVE_INVENTORY_CHANGE = 0;

	@Deprecated
	private String change;

	public RuleShort()
	{
		//
	}

	/**
	 * Instantiates a new rule.
	 * 
	 * @param olqFromStatus the olq from status
	 * @param olqToStatus the olq to status
	 * @param inventoryStatus the inventory status
	 * @param change the change
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           olqFromStatus must not be blank.
	 *           <dd>
	 *           olqToStatus must not be blank.
	 *           <dd>
	 *           inventoryStatus must not be blank.
	 *           <dd>
	 *           change must not be blank.
	 * @throws IllegalStateException if olqFromStatus == null || olqToStatus == null || inventoryStatus == null || change
	 *            == null
	 */
	public RuleShort(final String olqFromStatus, final String olqToStatus, final String inventoryStatus, final String change)
			throws IllegalStateException
	{
		super();
		this.olqFromStatus = olqFromStatus;
		this.olqToStatus = olqToStatus;
		this.inventoryStatus = inventoryStatus;
		this.change = change;

		if (olqFromStatus == null)
		{
			throw new IllegalStateException("From status must not be null");
		}
		if (olqToStatus == null)
		{
			throw new IllegalStateException("To status must not be null");
		}
		if (inventoryStatus == null)
		{
			throw new IllegalStateException("Inventory status must not be null");
		}
		if (change == null)
		{
			throw new IllegalStateException("Change must not be null");
		}
	}

	/**
	 * Gets the olq from status.
	 * 
	 * @return the olqFromStatus
	 */
	@XmlElement(required = true)
	public String getOlqFromStatus()
	{
		return this.olqFromStatus;
	}

	/**
	 * Gets the serial version uid.
	 * 
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	/**
	 * Sets the olq to status.
	 * 
	 * @param olqToStatus the olqToStatus to set
	 */
	public void setOlqToStatus(final String olqToStatus)
	{
		this.olqToStatus = olqToStatus;
	}

	/**
	 * Sets the inventory status.
	 * 
	 * @param inventoryStatus the inventoryStatus to set
	 */
	public void setInventoryStatus(final String inventoryStatus)
	{
		this.inventoryStatus = inventoryStatus;
	}

	/**
	 * Sets the change.
	 * 
	 * @param change the change to set
	 * @deprecated Use {@link RuleShort#setPositive(boolean)} instead
	 */
	@Deprecated
	public void setChange(final String change)
	{
		this.change = change;
		try
		{

			final int changeNum = Integer.parseInt(change);

			if (changeNum == NEGATIVE_INVENTORY_CHANGE)
			{
				this.setPositive(false);
			}
			else if (changeNum == POSITIVE_INVENTORY_CHANGE)
			{
				this.setPositive(true);
			}
		}
		catch (final NumberFormatException numberFormatException)
		{
			throw new IllegalArgumentException("Invalid Inventory Change Value: " + change);
		}


	}

	/**
	 * Sets the olq from status.
	 * 
	 * @param olqFromStatus the olqFromStatus to set
	 */
	public void setOlqFromStatus(final String olqFromStatus)
	{
		this.olqFromStatus = olqFromStatus;
	}

	/**
	 * Gets the olq to status.
	 * 
	 * @return the olqToStatus
	 */
	@XmlElement(required = true)
	public String getOlqToStatus()
	{
		return this.olqToStatus;
	}

	/**
	 * Gets the inventory status.
	 * 
	 * @return the inventoryStatus
	 */
	@XmlElement(required = true)
	public String getInventoryStatus()
	{
		return this.inventoryStatus;
	}

	/**
	 * Gets the change.
	 * 
	 * @return the change
	 * @deprecated Use {@link RuleShort#isPositive()}
	 */
	@Deprecated
	@XmlElement(required = true)
	public String getChange()
	{
		return this.change;
	}

	/**
	 * @return the updateStrategy
	 */
	@XmlElement(required = true)
	public String getUpdateStrategy()
	{
		return updateStrategy;
	}

	/**
	 * @param updateStrategy the updateStrategy to set
	 */
	public void setUpdateStrategy(final String updateStrategy)
	{
		this.updateStrategy = updateStrategy;
	}

	/**
	 * @return the positive
	 */

	public boolean isPositive()
	{
		return positive;
	}

	/**
	 * @param positive the positive to set
	 */
	public void setPositive(final boolean positive)
	{
		this.positive = positive;
	}
}
