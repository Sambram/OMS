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

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Represent the status of an Item that is current.
 */
@XmlRootElement
@XmlType(name = "CurrentItemQuantityStatusData")
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrentItemQuantityStatus extends ItemQuantityStatus
{
	private static final long serialVersionUID = 4383761501792635761L;

	/**
	 * Instantiates a new current item quantity status.
	 */
	public CurrentItemQuantityStatus()
	{
		//
	}

	/**
	 * Instantiates a new current item quantity status.
	 * 
	 * @param statusCode
	 *           the status code
	 */
	public CurrentItemQuantityStatus(final String statusCode)
	{
		super(statusCode);
	}

	@Override
	// @XmlJavaTypeAdapter(DateAdaptor.class)
	public Date getExpectedDeliveryDate()
	{
		return null;
	}

	@Override
	public boolean hasExpectedDeliveryDate()
	{
		return false;
	}

	@Override
	public void setExpectedDeliveryDate(final Date date)
	{
		//
	}

	@Override
	public String toString()
	{
		return "CurrentItemQuantityStatus [statusCode=" + this.getStatusCode() + ']';
	}
}
