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
package com.hybris.oms.export.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * Represents the amount of data marked for export.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportTriggerResultDTO implements Serializable
{
	private static final long serialVersionUID = -1846545069636536475L;

	@XmlAttribute
	private int amount;

	/**
	 * Gets the amount of data marked for export.
	 * 
	 * @return the amount triggered
	 */
	public int getAmount()
	{
		return this.amount;
	}

	/**
	 * Sets the amount to use.
	 * 
	 * @param amount the amount to use
	 */
	public void setAmount(final int amount)
	{
		this.amount = amount;
	}
}
