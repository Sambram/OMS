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
package com.hybris.oms.service.managedobjects.returns;

import com.hybris.kernel.api.HybrisId;
import com.hybris.oms.service.managedobjects.types.QuantityVT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 *
 */
public class ReturnOrderLineDataPojo implements ReturnOrderLineData
{

	private long returnOrderLineId;
	private ReturnData theReturn;
	private String orderLineId;
	private String returnOrderLineStatus;
	private QuantityVT quantity;
	private List<ReturnLineRejectionData> returnLineRejections = new ArrayList<>();
	private long version;

	@Override
	public HybrisId getId()
	{
		return HybrisId.valueOf("single|ReturnOrderLineData|" + getReturnOrderLineId());
	}

	@Override
	public Date getCreationTime()
	{
		return null;
	}

	@Override
	public Date getModifiedTime()
	{
		return null;
	}

	@Override
	public Object getProperty(final String attributeName)
	{
		return null;
	}

	@Override
	public Object getProperty(final String attributeName, final Locale locale)
	{
		return null;
	}

	@Override
	public <T> T getProperty(final String attributeName, final Class<T> clazz)
	{
		return null;
	}

	@Override
	public <T> T getProperty(final String attributeName, final Locale locale, final Class<T> clazz)
	{
		return null;
	}

	@Override
	public void setProperty(final String attributeName, final Object value)
	{
		// Empty
	}

	@Override
	public void setProperty(final String attributeName, final Object value, final Locale locale)
	{
		// Empty

	}

	@Override
	public Map<String, Object> getAllProperties()
	{
		return null;
	}

	@Override
	public Collection<String> getAllPropertyAttributeNames()
	{
		return null;
	}

	@Override
	public ReturnData getMyReturn()
	{
		return theReturn;
	}

	@Override
	public long getReturnOrderLineId()
	{
		return returnOrderLineId;
	}

	@Override
	public String getReturnOrderLineStatus()
	{
		return returnOrderLineStatus;
	}

	@Override
	public String getOrderLineId()
	{
		return orderLineId;
	}

	@Override
	public QuantityVT getQuantity()
	{
		return quantity;
	}

	@Override
	public List<ReturnLineRejectionData> getReturnLineRejections()
	{
		return returnLineRejections;
	}

	@Override
	public Long getVersion()
	{
		return version;
	}

	@Override
	public void setMyReturn(final ReturnData value)
	{
		this.theReturn = value;
	}

	@Override
	public void setReturnOrderLineId(final long value)
	{
		this.returnOrderLineId = value;
	}

	@Override
	public void setReturnOrderLineStatus(final String value)
	{
		this.returnOrderLineStatus = value;
	}

	@Override
	public void setOrderLineId(final String value)
	{
		this.orderLineId = value;
	}

	@Override
	public void setQuantity(final QuantityVT value)
	{
		this.quantity = value;
	}

	@Override
	public void setReturnLineRejections(final List<ReturnLineRejectionData> value)
	{
		this.returnLineRejections = value;
	}

	@Override
	public void setVersion(final Long value)
	{
		this.version = value;
	}

}
