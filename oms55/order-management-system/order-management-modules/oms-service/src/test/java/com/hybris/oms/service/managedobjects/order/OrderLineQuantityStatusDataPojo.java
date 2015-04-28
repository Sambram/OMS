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
package com.hybris.oms.service.managedobjects.order;

import com.hybris.kernel.api.HybrisId;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


public class OrderLineQuantityStatusDataPojo implements OrderLineQuantityStatusData
{
	public OrderLineQuantityStatusDataPojo(String statusCode)
	{
		this.statusCode = statusCode;
	}

	@Override public Boolean getActive()
	{
		return null;
	}

	@Override public String getStatusCode()
	{
		return statusCode;
	}

	@Override public String getDescription()
	{
		return null;
	}

	@Override public Long getVersion()
	{
		return null;
	}

	@Override public void setActive(Boolean value)
	{

	}

	String statusCode;
	@Override public void setStatusCode(String value)
	{
		this.statusCode = value;
	}

	@Override public void setDescription(String value)
	{

	}

	@Override public void setVersion(Long value)
	{

	}

	@Override public HybrisId getId()
	{
		return null;
	}

	@Override public Date getCreationTime()
	{
		return null;
	}

	@Override public Date getModifiedTime()
	{
		return null;
	}

	@Override public Object getProperty(String attributeName)
	{
		return null;
	}

	@Override public Object getProperty(String attributeName, Locale locale)
	{
		return null;
	}

	@Override public <T> T getProperty(String attributeName, Class<T> clazz)
	{
		return null;
	}

	@Override public <T> T getProperty(String attributeName, Locale locale, Class<T> clazz)
	{
		return null;
	}

	@Override public void setProperty(String attributeName, Object value)
	{

	}

	@Override public void setProperty(String attributeName, Object value, Locale locale)
	{

	}

	@Override public Map<String, Object> getAllProperties()
	{
		return null;
	}

	@Override public Collection<String> getAllPropertyAttributeNames()
	{
		return null;
	}
}
