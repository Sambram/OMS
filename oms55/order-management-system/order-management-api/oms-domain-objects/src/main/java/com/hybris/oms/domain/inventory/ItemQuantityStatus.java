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
package com.hybris.oms.domain.inventory;

import com.hybris.commons.dto.impl.PropertyAwareEntityDto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * The Class ItemQuantityStatus.
 */
@XmlSeeAlso({CurrentItemQuantityStatus.class, FutureItemQuantityStatus.class})
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ItemQuantityStatus extends PropertyAwareEntityDto
{
	private static final long serialVersionUID = -8642227144905390829L;

	@XmlElement
	private String statusCode;

	/**
	 * Instantiates a new item quantity status.
	 */
	public ItemQuantityStatus()
	{
		this.statusCode = null;
	}

	/**
	 * Instantiates a new item quantity status.
	 * 
	 * @param statusCode the status code
	 */
	public ItemQuantityStatus(final String statusCode)
	{
		this.statusCode = statusCode;
	}


	/**
	 * Gets the expected delivery date.
	 * 
	 * @return the date this item is expected at this status, or null
	 */
	// @XmlJavaTypeAdapter(DateAdaptor.class)
	public abstract Date getExpectedDeliveryDate();

	/**
	 * Gets the status code.
	 * 
	 * @return the status code
	 */
	public final String getStatusCode()
	{
		return this.statusCode;
	}

	/**
	 * Checks for expected delivery date.
	 * 
	 * @return true if this item has an expected delivery date
	 */
	public abstract boolean hasExpectedDeliveryDate();

	/**
	 * Sets the expected delivery date.
	 * 
	 * @param date the expected delivery date
	 */
	public abstract void setExpectedDeliveryDate(final Date date);

	/**
	 * Sets the status code.
	 * 
	 * @param statusCode the new status code
	 */
	public void setStatusCode(final String statusCode)
	{
		this.statusCode = statusCode;
	}

	@Override
	public String getId()
	{
		return statusCode;
	}

}
