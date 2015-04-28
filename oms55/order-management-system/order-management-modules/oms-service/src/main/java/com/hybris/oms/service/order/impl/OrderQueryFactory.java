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
package com.hybris.oms.service.order.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.domain.order.OrderQuerySupport;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.service.AbstractQueryFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


public class OrderQueryFactory extends AbstractQueryFactory
{
	private static final String WILDCARD = "%";
	private static final String SHIPMENT_ALIAS = "s";
	private static final String SHIPMENT_ALIAS_DOT = SHIPMENT_ALIAS + ".";
	private static final String OLQ_ALIAS = "q";
	private static final String OLQ_ALIAS_DOT = OLQ_ALIAS + ".";

	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<>();
	static
	{
		OrderQueryFactory.QUERY_SUPPORT_MAPPING.put(OrderQuerySupport.DEFAULT.name(), "orderId");
		OrderQueryFactory.QUERY_SUPPORT_MAPPING.put(OrderQuerySupport.ORDER_ID.name(), "orderId");
		OrderQueryFactory.QUERY_SUPPORT_MAPPING.put(OrderQuerySupport.LAST_NAME.name(), "lastName");
		OrderQueryFactory.QUERY_SUPPORT_MAPPING.put(OrderQuerySupport.FIRST_NAME.name(), "firstName");
		OrderQueryFactory.QUERY_SUPPORT_MAPPING.put(OrderQuerySupport.ORDER_DATE.name(), "issueDate");
		OrderQueryFactory.QUERY_SUPPORT_MAPPING.put(OrderQuerySupport.SCHEDULED_SHIPPING_DATE.name(), "scheduledShippingDate");
	}

	/**
	 * Returns a query for Orders by {@link OrderQueryObject} supporting various filter criteria and sorting options.
	 *
	 * @param query query
	 * @return CriteriaQuery<OrderData>
	 */
	public CriteriaQuery<OrderData> findOrdersByQuery(final OrderQueryObject query)
	{
		final CriteriaQuery<OrderData> criteriaQuery = this.query(OrderData.class);
		final List<Restriction> restrictions = new ArrayList<>();
		this.filteringOnNames(query.getUserName(), query.getFirstName(), query.getLastName(), restrictions);

		final boolean hasLocationIds = CollectionUtils.isNotEmpty(query.getLocationIds());
		final boolean hasShipmentStatusIds = CollectionUtils.isNotEmpty(query.getShipmentStatusIds());

		if (hasLocationIds || hasShipmentStatusIds)
		{
			criteriaQuery.join(ShipmentData.class, SHIPMENT_ALIAS);
			restrictions.add(RawRestrictions.attrEq(OrderData.ID.name(), SHIPMENT_ALIAS_DOT + ShipmentData.ORDERFK.name()));
			if (hasLocationIds)
			{
				restrictions.add(RawRestrictions.in(SHIPMENT_ALIAS_DOT + ShipmentData.STOCKROOMLOCATIONID.name(), query
						.getLocationIds().toArray()));
			}
			if (hasShipmentStatusIds)
			{
				restrictions.add(RawRestrictions.in(SHIPMENT_ALIAS_DOT + ShipmentData.OLQSSTATUS.name(), query.getShipmentStatusIds()
						.toArray()));
			}
		}

		this.filteringOnDates(query.getStartDate(), query.getEndDate(), restrictions);
		this.filteringOnScheduledShippingDates(query.getStartScheduledDate(), query.getEndScheduledDate(), restrictions);
		applyingCollectedRestrictionsToCriteriaQuery(criteriaQuery, restrictions);
		criteriaQuery.order(OrderQueryFactory.QUERY_SUPPORT_MAPPING.get(query.getSorting().getAttribute()),
				SortDirection.ASC.equals(query.getSorting().getDirection()));
		return criteriaQuery;
	}

	public CriteriaQuery<OrderLineQuantityStatusData> getAllOrderLineQuantityStatusesQuery()
	{
		return this.query(OrderLineQuantityStatusData.class);
	}

	/**
	 * Gets the order line by id query.
	 *
	 * @param orderLineId the order line id
	 * @return CriteriaQuery<OrderLineData>
	 */
	public CriteriaQuery<OrderLineData> getOrderLineByIdQuery(final String orderLineId)
	{
		return this.query(OrderLineData.class).where(Restrictions.eq(OrderLineData.ORDERLINEID, orderLineId));
	}

	public CriteriaQuery<OrderLineQuantityData> getOrderLineQuantityByOlqIdsQuery(final List<Long> olqIds)
	{
		return this.query(OrderLineQuantityData.class).where(
				Restrictions.in(OrderLineQuantityData.OLQID, olqIds.toArray(new Long[olqIds.size()])));
	}

	public CriteriaQuery<OrderLineQuantityData> getOrderLineQuantityByShipmentQuery(final ShipmentData shipment)
	{
		return this.query(OrderLineQuantityData.class).where(Restrictions.eq(OrderLineQuantityData.SHIPMENT, shipment));
	}

	public CriteriaQuery<OrderLineQuantityStatusData> getOrderLineQuantityStatusByStatusCode(final String statusCode)
	{
		return this.query(OrderLineQuantityStatusData.class).where(
				Restrictions.eq(OrderLineQuantityStatusData.STATUSCODE, statusCode));
	}

	public CriteriaQuery<OrderData> getOrdersByIdQuery(final String orderId)
	{
		return this.query(OrderData.class).where(Restrictions.eq(OrderData.ORDERID, orderId));
	}

	/**
	 * Gets query to find all orders whose {@link OrderData#getModifiedTime()} is greater than the date provided.
	 *
	 * @param date
	 * @return CriteriaQuery<OrderData>
	 */
	public CriteriaQuery<OrderData> getOrdersByModifiedTimeGreaterThanQuery(final Date date)
	{
		return this.query(OrderData.class).where(Restrictions.ge(OrderData.MODIFIEDTIME, date));
	}

	protected void filteringOnDates(final Date startDate, final Date endDate, final List<Restriction> restrictions)
	{
		// Filtering on "startDate" and "endDate"
		if (startDate != null)
		{

			restrictions.add(RawRestrictions.ge(OrderData.ISSUEDATE.name(), startDate));

		}
		if (endDate != null)
		{

			restrictions.add(RawRestrictions.le(OrderData.ISSUEDATE.name(), endDate));

		}
	}

	protected void filteringOnScheduledShippingDates(final Date startScheduledDate, final Date endScheduledDate,
			final List<Restriction> restrictions)
	{
		// Filtering on "startScheduledDate" and "endScheduledDate"
		if (startScheduledDate != null)
		{
			restrictions.add(RawRestrictions.ge(OrderData.SCHEDULEDSHIPPINGDATE.name(), startScheduledDate));
		}
		if (endScheduledDate != null)
		{
			restrictions.add(RawRestrictions.le(OrderData.SCHEDULEDSHIPPINGDATE.name(), endScheduledDate));
		}
	}

	protected void filteringOnNames(final String userName, final String firstName, final String lastName,
			final List<Restriction> restrictions)
	{
		// Filtering on "UserName"
		if (StringUtils.isNotEmpty(userName))
		{
			restrictions.add(RawRestrictions.eq(OrderData.USERNAME.name(), userName));
		}

		// Filtering on "LastName"
		if (StringUtils.isNotEmpty(lastName))
		{
			restrictions.add(RawRestrictions.like(OrderData.LASTNAME.name(), WILDCARD + lastName + WILDCARD));
		}

		// Filtering on "FirstName"
		if (StringUtils.isNotEmpty(firstName))
		{
			restrictions.add(RawRestrictions.like(OrderData.FIRSTNAME.name(), WILDCARD + firstName + WILDCARD));
		}
	}

	/**
	 * Gets order lines by shipment.
	 *
	 * @param shipment the shipment
	 * @return list of {@link OrderLineData}
	 */
	public CriteriaQuery<OrderLineData> getOrderLinesByShipmentQuery(final ShipmentData shipment)
	{
		final CriteriaQuery<OrderLineData> query = this
				.query(OrderLineData.class)
				.join(OrderLineQuantityData.class, OLQ_ALIAS)
				.join(ShipmentData.class, SHIPMENT_ALIAS)
				.where(RawRestrictions.attrEq(OrderLineData.ID.name(), OLQ_ALIAS_DOT + OrderLineQuantityData.ORDERLINE.name()))
				.and(RawRestrictions.attrEq(OLQ_ALIAS_DOT + OrderLineQuantityData.SHIPMENT.name(), SHIPMENT_ALIAS_DOT
						+ ShipmentData.ID.name()))
				.and(RawRestrictions.eq(SHIPMENT_ALIAS_DOT + ShipmentData.ID.name(), shipment.getId()));

		return query;
	}

	protected CriteriaQuery<OrderData> applyingCollectedRestrictionsToCriteriaQuery(final CriteriaQuery<OrderData> origQuery,
			final List<Restriction> restrictions)
	{
		CriteriaQuery<OrderData> criteriaQuery = origQuery;
		if (!restrictions.isEmpty())
		{
			criteriaQuery = criteriaQuery.where(restrictions.remove(0));

			for (final Restriction restriction : restrictions)
			{
				criteriaQuery = criteriaQuery.and(restriction);
			}

		}
		return criteriaQuery;
	}

}
