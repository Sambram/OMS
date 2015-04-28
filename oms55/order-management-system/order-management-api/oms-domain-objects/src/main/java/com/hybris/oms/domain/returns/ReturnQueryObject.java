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
package com.hybris.oms.domain.returns;

import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.SortDirection;

import java.util.List;


/**
 * A query object to be used when searching for
 * returns using different criteria.
 */
public class ReturnQueryObject extends QueryObject<ReturnQuerySupport>
{
	private static final long serialVersionUID = -2288697057149779189L;

	public ReturnQueryObject()
	{
		super();
		this.setSorting(ReturnQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public ReturnQueryObject(final String returnId, final List<String> orderIds, final String state, final Integer pageNumber,
			final Integer pageSize)
	{
		this();
		if (returnId != null)
		{
			this.setReturnId(returnId);
		}
		if (!orderIds.isEmpty())
		{
			this.setOrderIds(orderIds);
		}
		if (state != null)
		{
			this.setState(state);
		}
		this.buildPageNumberAndSize(pageNumber, pageSize);
	}

	public String getReturnId()
	{
		return super.getValue("returnId");
	}

	public final void setReturnId(final String returnId)
	{
		super.setValue("returnId", returnId);
	}

	public final void setOrderIds(final List<String> orderIds)
	{
		super.setValues("orderId", orderIds);
	}

	public List<String> getOrderIds()
	{
		return super.getValues("orderId");
	}

	public String getState()
	{
		return super.getValue("state");
	}

	public final void setState(final String state)
	{
		super.setValue("state", state);
	}

}
