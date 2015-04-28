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
package com.hybris.oms.domain.order;

import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.SortDirection;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * A query object to be used when searching for
 * orders using different criteria.
 */
public class OrderQueryObject extends QueryObject<OrderQuerySupport>
{
	private static final long serialVersionUID = -2475284893507354585L;

	public OrderQueryObject()
	{
		this.setSorting(OrderQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public Date getEndDate()
	{
		return this.getDateValue("endDate");
	}

	public void setEndDate(final Date endDate)
	{
		this.superSetValue("endDate", endDate);

	}

	public final Date getStartScheduledDate()
	{
		return super.getDateValue("startScheduledDate");
	}

	public final void setStartScheduledDate(final Date startScheduledDate)
	{
		this.superSetValue("startScheduledDate", startScheduledDate);
	}

	public Date getEndScheduledDate()
	{
		return super.getDateValue("endScheduledDate");
	}

	public final void setEndScheduledDate(final Date endScheduledDate)
	{
		this.superSetValue("endScheduledDate", endScheduledDate);
	}

	public String getFirstName()
	{
		return super.getValue("firstName");
	}

	public void setFirstName(final String firstName)
	{
		this.superSetValue("firstName", firstName);
	}

	public String getLastName()
	{
		return super.getValue("lastName");
	}

	public void setLastName(final String lastName)
	{
		this.superSetValue("lastName", lastName);

	}

	public List<String> getLocationIds()
	{
		return super.getValues("locId");
	}

	public void setLocationIds(final List<String> locationIds)
	{
		this.superSetValues("locId", locationIds);
	}

	public List<String> getShipmentStatusIds()
	{
		return super.getValues("statusId");
	}

	public void setShipmentStatusIds(final List<String> shipmentStatusIds)
	{
		this.superSetValues("statusId", shipmentStatusIds);
	}

	public Date getStartDate()
	{
		return this.getDateValue("startDate");
	}

	public void setStartDate(final Date startDate)
	{
		this.superSetValue("startDate", startDate);
	}

	public String getUserName()
	{
		return super.getValue("userName");
	}

	public void setUserName(final String userName)
	{
		this.superSetValue("userName", userName);
	}

	public void setUpdatedSince(final Date aDate)
	{
		this.superSetValue("lastModified", aDate);
	}

	private void superSetValue(final String attributeName, final Boolean attributeValue)
	{
		if (attributeValue != null)
		{
			super.setValue(attributeName, attributeValue);
		}
	}

	private void superSetValue(final String attributeName, final Date attributeValue)
	{
		if (attributeValue != null)
		{
			super.setValue(attributeName, attributeValue);
		}
	}

	private void superSetValue(final String attributeName, final String attributeValue)
	{
		if (attributeValue != null)
		{
			super.setValue(attributeName, attributeValue);
		}
	}

	private void superSetValues(final String attributeName, final List<String> attributeValue)
	{
		if (CollectionUtils.isNotEmpty(attributeValue))
		{
			super.setValues(attributeName, attributeValue);
		}
	}
}
