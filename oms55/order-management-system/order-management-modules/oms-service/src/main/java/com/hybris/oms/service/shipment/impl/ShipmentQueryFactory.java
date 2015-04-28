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
package com.hybris.oms.service.shipment.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.RawRestrictions;
import com.hybris.kernel.api.Restriction;
import com.hybris.kernel.api.Restrictions;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShipmentQuerySupport;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.service.AbstractQueryFactory;


/**
 * A factory for creating ShipmentQuery objects.
 */
public class ShipmentQueryFactory extends AbstractQueryFactory
{
	private static final Map<String, String> QUERY_SUPPORT_MAPPING = new HashMap<>();
	static
	{
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.DEFAULT.name(), "shipmentId");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.CUSTOMER_FIRSTNAME.name(), "ordat.firstName");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.CUSTOMER_LASTNAME.name(), "ordat.lastName");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.ISSUE_DATE.name(), "ordat.issueDate");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.SCHEDULED_SHIPPING_DATE.name(),
				"ordat.scheduledShippingDate");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.LOCATION.name(), "loc.description");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.LOCATION_STORENAME.name(), "loc.storeName");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.OLQSSTATUS.name(), "olqsStatus");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.ORDER_ID.name(), "ordat.orderId");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.SHIPMENT_ID.name(), "shipmentId");
		ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.put(ShipmentQuerySupport.SHIPPING_METHOD.name(), "shippingMethod");
	}

	public CriteriaQuery<ShipmentData> findAllShipmentsByOrder(final OrderData order)
	{
		return this.query(ShipmentData.class).where(Restrictions.eq(ShipmentData.ORDERFK, order));
	}

	public CriteriaQuery<ShipmentData> findShipmentsByQuery(final ShipmentQueryObject queryObject)
	{
		final CriteriaQuery<ShipmentData> criteriaQuery = this.query(ShipmentData.class);

		final List<Restriction> restrictions = new ArrayList<>();

		// Detect sorting requirements
		final boolean customerFirstNameSort = ShipmentQuerySupport.CUSTOMER_FIRSTNAME.name().equals(
				queryObject.getSorting().getAttribute());
		final boolean customerLastNameSort = ShipmentQuerySupport.CUSTOMER_LASTNAME.name().equals(
				queryObject.getSorting().getAttribute());
		final boolean orderIssueDateSort = ShipmentQuerySupport.ISSUE_DATE.name().equals(queryObject.getSorting().getAttribute());
		final boolean orderScheduledShippingDateSort = ShipmentQuerySupport.SCHEDULED_SHIPPING_DATE.name().equals(
				queryObject.getSorting().getAttribute());
		final boolean orderIdSort = ShipmentQuerySupport.ORDER_ID.name().equals(queryObject.getSorting().getAttribute());
		final boolean locationDescriptionSort = ShipmentQuerySupport.LOCATION.name()
				.equals(queryObject.getSorting().getAttribute());
		final boolean locationStoreNameSort = ShipmentQuerySupport.LOCATION_STORENAME.name().equals(
				queryObject.getSorting().getAttribute());

		final Date startDate = queryObject.getStartDate();
		final Date endDate = queryObject.getEndDate();

		final Date startScheduledDate = queryObject.getStartScheduledDate();
		final Date endScheduledDate = queryObject.getEndScheduledDate();


		// Detect filtering requirements
		final boolean orderIdsFilter = (queryObject.getOrderIds() != null) && (!queryObject.getOrderIds().isEmpty());
		final boolean orderLastNameFilter = (queryObject.getCustomerLastName() != null);
		final boolean preOrderFilter = (queryObject.getPreOrder() != null);

		final boolean olqStatusFilter = (queryObject.getShipmentStatusIds() != null)
				&& (!queryObject.getShipmentStatusIds().isEmpty());
		final boolean locationIdsFilter = (queryObject.getLocationIds() != null) && (!queryObject.getLocationIds().isEmpty());
		final boolean orderDateFilter = (startDate != null) || (endDate != null);
		final boolean scheduledShippingDateFilter = (startScheduledDate != null) || (endScheduledDate != null);
		final boolean pickupFilter = (queryObject.getPickupInStore() != null);

		// Detect join requirements
		final boolean customerJoins = customerFirstNameSort || customerLastNameSort;
		final boolean orderIdJoins = orderIdSort || orderIdsFilter;
		final boolean otherFieldJoins = orderIssueDateSort || orderScheduledShippingDateSort || orderLastNameFilter
				|| orderDateFilter || scheduledShippingDateFilter;
		final boolean orderIdJoin = customerJoins || orderIdJoins || otherFieldJoins;
		final boolean locationJoin = locationDescriptionSort;


		final FillQueryParameter parameterObject = new FillQueryParameter();


		parameterObject.setQueryObject(queryObject);
		parameterObject.setCriteriaQuery(criteriaQuery);
		parameterObject.setRestrictions(restrictions);
		parameterObject.setLocationStoreNameSort(locationStoreNameSort);
		parameterObject.setStartDate(startDate);
		parameterObject.setEndDate(endDate);
		parameterObject.setStartScheduledDate(startScheduledDate);
		parameterObject.setEndScheduledDate(endScheduledDate);
		parameterObject.setOrderIdsFilter(orderIdsFilter);
		parameterObject.setOrderLastNameFilter(orderLastNameFilter);
		parameterObject.setOlqStatusFilter(olqStatusFilter);
		parameterObject.setLocationIdsFilter(locationIdsFilter);
		parameterObject.setPickupFilter(pickupFilter);
		parameterObject.setOrderIdJoin(orderIdJoin);
		parameterObject.setLocationJoin(locationJoin);
		parameterObject.setPreOrderFilter(preOrderFilter);

		return fillQuery(parameterObject);
	}

	protected CriteriaQuery<ShipmentData> fillQuery(final FillQueryParameter parameterObject)
	{
		CriteriaQuery<ShipmentData> queryToFill = parameterObject.getCriteriaQuery();

		filteringOnOrderId(parameterObject.getQueryObject(), parameterObject.getRestrictions(), parameterObject.isOrderIdsFilter());
		filteringOnStatusIds(parameterObject.getQueryObject(), parameterObject.getRestrictions(),
				parameterObject.isOlqStatusFilter());
		filteringOnLocIds(parameterObject.getQueryObject(), parameterObject.getRestrictions(),
				parameterObject.isLocationIdsFilter());
		addPreOrderFilter(parameterObject.getQueryObject(), parameterObject.getRestrictions(), parameterObject.isPreOrderFilter());
		addPickupFilter(parameterObject.getQueryObject(), parameterObject.getRestrictions(), parameterObject.isPickupFilter());
		queryToFill = joiningToOrdersTable(parameterObject.getQueryObject(), queryToFill, parameterObject.getRestrictions(),
				parameterObject.isOrderLastNameFilter(), parameterObject.isOrderIdJoin());
		queryToFill = joiningToLocationTables(queryToFill, parameterObject.getRestrictions(),
				parameterObject.isLocationStoreNameSort(), parameterObject.isLocationJoin());
		filteringOnStartDateAndEndDate(parameterObject.getRestrictions(), parameterObject.getStartDate(),
				parameterObject.getEndDate());
		filteringOnScheduledShippingStartAndEndDate(parameterObject.getRestrictions(), parameterObject.getStartScheduledDate(),
				parameterObject.getEndScheduledDate());
		queryToFill = applyingCollectedRestrictionsToTheCriteriaQueryObject(queryToFill, parameterObject.getRestrictions());

		// Apply ordering
		queryToFill = queryToFill.order(
				ShipmentQueryFactory.QUERY_SUPPORT_MAPPING.get(parameterObject.getQueryObject().getSorting().getAttribute()),
				SortDirection.ASC.equals(parameterObject.getQueryObject().getSorting().getDirection()));

		return queryToFill;
	}

	/**
	 * Find a shipment by shipment id.
	 *
	 * @param shipmentId
	 *           the shipment id
	 * @return ShipmentData
	 */
	public CriteriaQuery<ShipmentData> getShipmentById(final Long shipmentId)
	{
		return this.query(ShipmentData.class).where(Restrictions.eq(ShipmentData.SHIPMENTID, shipmentId));
	}

	protected void addPreOrderFilter(final ShipmentQueryObject queryObject, final List<Restriction> restrictions,
			final boolean preOrderFilter)
	{
		if (preOrderFilter && !queryObject.getPreOrder())
		{
			restrictions.add(RawRestrictions.notIn(ShipmentData.ORDERLINESFULFILLMENTTYPE.name(),
					OrderlineFulfillmentType.PRE_ORDERLINE, OrderlineFulfillmentType.BACK_ORDERLINE));
		}
	}

	protected void addPickupFilter(final ShipmentQueryObject queryObject, final List<Restriction> restrictions,
			final boolean pickupFilter)
	{
		if (pickupFilter)
		{
			restrictions.add(Restrictions.eq(ShipmentData.PICKUPINSTORE, queryObject.getPickupInStore()));
		}
	}

	protected CriteriaQuery<ShipmentData> applyingCollectedRestrictionsToTheCriteriaQueryObject(
			final CriteriaQuery<ShipmentData> origCriteria, final List<Restriction> restrictions)
	{
		CriteriaQuery<ShipmentData> criteriaQuery = origCriteria;
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

	protected void filteringOnLocIds(final ShipmentQueryObject queryObject, final List<Restriction> restrictions,
			final boolean locationIdsFilter)
	{
		if (locationIdsFilter)
		{
			restrictions.add(Restrictions.in(ShipmentData.STOCKROOMLOCATIONID,
					queryObject.getLocationIds().toArray(new String[queryObject.getLocationIds().size()])));
		}
	}

	protected void filteringOnOrderId(final ShipmentQueryObject queryObject, final List<Restriction> restrictions,
			final boolean orderIdFilter)
	{
		if (orderIdFilter)
		{
			restrictions.add(RawRestrictions.eq("ordat.orderId", queryObject.getOrderIds()));
		}
	}

	protected void filteringOnStartDateAndEndDate(final List<Restriction> restrictions, final Date startDate, final Date endDate)
	{
		if (startDate != null)
		{
			restrictions.add(RawRestrictions.ge("ordat." + OrderData.ISSUEDATE.name(), startDate));
		}
		if (endDate != null)
		{
			restrictions.add(RawRestrictions.le("ordat." + OrderData.ISSUEDATE.name(), endDate));
		}
	}

	protected void filteringOnScheduledShippingStartAndEndDate(final List<Restriction> restrictions,
			final Date startScheduledDate, final Date endScheduledDate)
	{
		if (startScheduledDate != null)
		{
			restrictions.add(RawRestrictions.ge("ordat." + OrderData.SCHEDULEDSHIPPINGDATE.name(), startScheduledDate));
		}
		if (endScheduledDate != null)
		{
			restrictions.add(RawRestrictions.le("ordat." + OrderData.SCHEDULEDSHIPPINGDATE.name(), endScheduledDate));
		}
	}

	protected void filteringOnStatusIds(final ShipmentQueryObject queryObject, final List<Restriction> restrictions,
			final boolean olqStatusFilter)
	{
		if (olqStatusFilter)
		{
			restrictions.add(RawRestrictions.in(ShipmentData.OLQSSTATUS.name(), queryObject.getShipmentStatusIds().toArray()));
		}
	}

	protected CriteriaQuery<ShipmentData> joiningToLocationTables(final CriteriaQuery<ShipmentData> origCriteria,
			final List<Restriction> restrictions, final boolean locationStoreNameSort, final boolean locationJoin)
	{
		CriteriaQuery<ShipmentData> criteriaQuery = origCriteria;
		if (locationJoin || locationStoreNameSort)
		{
			criteriaQuery = criteriaQuery //
					.join(StockroomLocationData.class, "loc"); //
			restrictions.add(RawRestrictions.attrEq("stockroomLocationId", "loc.locationId")); //
		}
		return criteriaQuery;
	}

	protected CriteriaQuery<ShipmentData> joiningToOrdersTable(final ShipmentQueryObject queryObject,
			final CriteriaQuery<ShipmentData> origCriteria, final List<Restriction> restrictions, final boolean orderLastNameFilter,
			final boolean orderIdJoin)
	{
		CriteriaQuery<ShipmentData> criteriaQuery = origCriteria;
		if (orderIdJoin)
		{
			criteriaQuery = criteriaQuery.join(OrderData.class, "ordat").on(ShipmentData.ORDERFK);
			if (orderLastNameFilter)
			{
				restrictions.add(RawRestrictions.like("ordat.lastName", queryObject.getCustomerLastName())); //
			}
		}
		return criteriaQuery;
	}

	public CriteriaQuery<ShipmentData> getShipmentsByModifiedTimeGreaterThanQuery(final Date date)
	{
		return this.query(ShipmentData.class).where(Restrictions.ge(ShipmentData.MODIFIEDTIME, date));
	}

	public static class FillQueryParameter
	{
		private ShipmentQueryObject queryObject;

		private CriteriaQuery<ShipmentData> criteriaQuery;

		private List<Restriction> restrictions;

		private boolean locationStoreNameSort;

		private Date startDate;

		private Date endDate;

		private Date startScheduledDate;

		private Date endScheduledDate;

		private boolean orderIdsFilter;

		private boolean orderLastNameFilter;

		private boolean olqStatusFilter;

		private boolean locationIdsFilter;

		private boolean pickupFilter;

		private boolean orderIdJoin;

		private boolean olqStatusJoin;

		private boolean locationJoin;

		private boolean preOrderFilter;

		public ShipmentQueryObject getQueryObject()
		{
			return queryObject;
		}

		public void setQueryObject(final ShipmentQueryObject queryObject)
		{
			this.queryObject = queryObject;
		}

		public CriteriaQuery<ShipmentData> getCriteriaQuery()
		{
			return criteriaQuery;
		}

		public void setCriteriaQuery(final CriteriaQuery<ShipmentData> criteriaQuery)
		{
			this.criteriaQuery = criteriaQuery;
		}

		public List<Restriction> getRestrictions()
		{
			return restrictions;
		}

		public void setRestrictions(final List<Restriction> restrictions)
		{
			this.restrictions = restrictions;
		}

		public boolean isLocationStoreNameSort()
		{
			return locationStoreNameSort;
		}

		public void setLocationStoreNameSort(final boolean locationStoreNameSort)
		{
			this.locationStoreNameSort = locationStoreNameSort;
		}

		public Date getStartDate()
		{
			return startDate;
		}

		public void setStartDate(final Date startDate)
		{
			this.startDate = startDate;
		}

		public Date getEndDate()
		{
			return endDate;
		}

		public void setEndDate(final Date endDate)
		{
			this.endDate = endDate;
		}

		public boolean isOrderIdsFilter()
		{
			return orderIdsFilter;
		}

		public void setOrderIdsFilter(final boolean orderIdsFilter)
		{
			this.orderIdsFilter = orderIdsFilter;
		}

		public boolean isOrderLastNameFilter()
		{
			return orderLastNameFilter;
		}

		public void setOrderLastNameFilter(final boolean orderLastNameFilter)
		{
			this.orderLastNameFilter = orderLastNameFilter;
		}

		public boolean isOlqStatusFilter()
		{
			return olqStatusFilter;
		}

		public void setOlqStatusFilter(final boolean olqStatusFilter)
		{
			this.olqStatusFilter = olqStatusFilter;
		}

		public boolean isLocationIdsFilter()
		{
			return locationIdsFilter;
		}

		public void setLocationIdsFilter(final boolean locationIdsFilter)
		{
			this.locationIdsFilter = locationIdsFilter;
		}

		public boolean isPickupFilter()
		{
			return pickupFilter;
		}

		public void setPickupFilter(final boolean pickupFilter)
		{
			this.pickupFilter = pickupFilter;
		}

		public boolean isOrderIdJoin()
		{
			return orderIdJoin;
		}

		public void setOrderIdJoin(final boolean orderIdJoin)
		{
			this.orderIdJoin = orderIdJoin;
		}

		public boolean isOlqStatusJoin()
		{
			return olqStatusJoin;
		}

		public void setOlqStatusJoin(final boolean olqStatusJoin)
		{
			this.olqStatusJoin = olqStatusJoin;
		}

		public boolean isLocationJoin()
		{
			return locationJoin;
		}

		public void setLocationJoin(final boolean locationJoin)
		{
			this.locationJoin = locationJoin;
		}

		public void setPreOrderFilter(final boolean preOrderFilter)
		{
			this.preOrderFilter = preOrderFilter;
		}

		public boolean isPreOrderFilter()
		{
			return preOrderFilter;
		}

		public Date getStartScheduledDate()
		{
			return startScheduledDate;
		}

		public void setStartScheduledDate(final Date startScheduledDate)
		{
			this.startScheduledDate = startScheduledDate;
		}

		public Date getEndScheduledDate()
		{
			return endScheduledDate;
		}

		public void setEndScheduledDate(final Date endScheduledDate)
		{
			this.endScheduledDate = endScheduledDate;
		}
	}

}
