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
package com.hybris.oms.service.returns.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.domain.returns.ReturnQuerySupport;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.service.AbstractQueryFactory;


public class ReturnQueryFactory extends AbstractQueryFactory
{

	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<>();
	static
	{
		ReturnQueryFactory.QUERY_SUPPORT_MAPPING.put(ReturnQuerySupport.DEFAULT.name(), "returnId");
		ReturnQueryFactory.QUERY_SUPPORT_MAPPING.put(ReturnQuerySupport.RETURN_ID.name(), "returnId");
		ReturnQueryFactory.QUERY_SUPPORT_MAPPING.put(ReturnQuerySupport.ORDER_ID.name(), "ordat.orderId");
		ReturnQueryFactory.QUERY_SUPPORT_MAPPING.put(ReturnQuerySupport.STATE.name(), "state");
	}

	/**
	 * Get a query for a fetching a Return object by its unique id
	 * 
	 * @param returnId
	 * @return query
	 */
	public CriteriaQuery<ReturnData> getReturnById(final long returnId)
	{
		return this.query(ReturnData.class).where(Restrictions.eq(ReturnData.RETURNID, returnId));
	}

	public CriteriaQuery<ReturnOrderLineData> getReturnOrderLineById(final long returnOrderLineId)
	{
		return this.query(ReturnOrderLineData.class).where(
				Restrictions.eq(ReturnOrderLineData.RETURNORDERLINEID, returnOrderLineId));
	}

	/**
	 * Returns a query for Returns by {@link ReturnQueryObject} supporting various filter criteria and sorting options.
	 * 
	 * @param queryObject
	 *           query
	 * @return CriteriaQuery<ReturnData>
	 */
	public CriteriaQuery<ReturnData> findReturnsByQuery(final ReturnQueryObject queryObject)
	{
		CriteriaQuery<ReturnData> criteriaQuery = this.query(ReturnData.class);
		final List<Restriction> restrictions = new ArrayList<>();

		criteriaQuery = criteriaQuery.join(OrderData.class, "ordat").on(ReturnData.ORDER);

		// Filtering on "returnId"
		if (queryObject.getReturnId() != null)
		{
			restrictions.add(RawRestrictions.eq("returnId", queryObject.getReturnId()));
		}
		// Filtering on "orderId"
		if (!queryObject.getOrderIds().isEmpty())
		{
			restrictions.add(RawRestrictions.eq("ordat.orderId", queryObject.getOrderIds()));
		}

		if (!restrictions.isEmpty())
		{
			criteriaQuery = criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = criteriaQuery.and(restriction);
			}

		}

		criteriaQuery.order(ReturnQueryFactory.QUERY_SUPPORT_MAPPING.get(queryObject.getSorting().getAttribute()),
				SortDirection.ASC.equals(queryObject.getSorting().getDirection()));
		return criteriaQuery;
	}

}
