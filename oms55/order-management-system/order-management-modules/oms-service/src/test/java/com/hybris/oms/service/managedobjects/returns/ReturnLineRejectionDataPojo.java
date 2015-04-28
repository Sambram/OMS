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

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


/**
 *
 */
public class ReturnLineRejectionDataPojo implements ReturnLineRejectionData
{

	private long rejectionId;
	private int quantity;
	private String responsible;
	private String reason;
	private ReturnOrderLineData returnOrderLine;

	@Override
	public HybrisId getId()
	{
		return HybrisId.valueOf("single|ReturnLineRejectionData|" + getRejectionId());
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
	public long getRejectionId()
	{
		return rejectionId;
	}

	@Override
	public int getQuantity()
	{
		return quantity;
	}

	@Override
	public String getResponsible()
	{
		return responsible;
	}

	@Override
	public String getReason()
	{
		return reason;
	}

	@Override
	public ReturnOrderLineData getMyReturnOrderLine()
	{
		return returnOrderLine;
	}

	@Override
	public void setRejectionId(final long value)
	{
		this.rejectionId = value;
	}

	@Override
	public void setQuantity(final int value)
	{
		this.quantity = value;
	}

	@Override
	public void setResponsible(final String value)
	{
		this.responsible = value;
	}

	@Override
	public void setReason(final String value)
	{
		this.reason = value;
	}

	@Override
	public void setMyReturnOrderLine(final ReturnOrderLineData value)
	{
		this.returnOrderLine = value;
	}

}
