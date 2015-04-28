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

import com.hybris.oms.domain.adapter.DateAdaptor;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Indicates the status of an Item that is expected at a date.
 * 
 * @author fsavard
 * @version 1.0
 * @created 17-May-2012 2:49:16 PM
 */
@XmlType(name = "FutureItemQuantityStatus")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FutureItemQuantityStatus extends ItemQuantityStatus
{
	private static final long serialVersionUID = -7142897320830547693L;

	@XmlJavaTypeAdapter(DateAdaptor.class)
	private Date expectedDeliveryDate;

	/**
	 * Instantiates a new future item quantity status.
	 */
	public FutureItemQuantityStatus()
	{
		//
	}

	/**
	 * Instantiates a new future item quantity status.
	 * 
	 * @param expectedDeliveryDate
	 *           the expected delivery date
	 * @param statusCode
	 *           the status code
	 */
	public FutureItemQuantityStatus(final Date expectedDeliveryDate, final String statusCode)
	{
		super(statusCode);
		this.expectedDeliveryDate = expectedDeliveryDate;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass() && super.equals(obj))
		{
			final FutureItemQuantityStatus other = (FutureItemQuantityStatus) obj;

			if (this.getExpectedDeliveryDate() == null)
			{
				if (other.getExpectedDeliveryDate() != null)
				{
					return false;
				}
			}
			else if (!this.getExpectedDeliveryDate().equals(other.getExpectedDeliveryDate()))
			{
				return false;
			}
		}

		return false;
	}

	@Override
	public Date getExpectedDeliveryDate()
	{
		if (this.expectedDeliveryDate == null)
		{
			return null;
		}
		return new Date(this.expectedDeliveryDate.getTime());
	}

	@Override
	public boolean hasExpectedDeliveryDate()
	{
		return this.getExpectedDeliveryDate() != null;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((this.getExpectedDeliveryDate() == null) ? 0 : this.getExpectedDeliveryDate().hashCode());
		return result;
	}

	@Override
	public void setExpectedDeliveryDate(final Date date)
	{
		if (expectedDeliveryDate == null)
		{
			this.expectedDeliveryDate = null;
		}
		else
		{
			this.expectedDeliveryDate = new Date(date.getTime());
		}
	}

	@Override
	public String toString()
	{
		return "FutureItemQuantityStatus [expectedDeliveryDate=" + this.getExpectedDeliveryDate() + ", statusCode="
				+ this.getStatusCode() + ']';
	}
}
