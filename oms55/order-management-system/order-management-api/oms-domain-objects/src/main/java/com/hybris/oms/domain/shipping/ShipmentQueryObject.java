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
package com.hybris.oms.domain.shipping;

import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.QuerySorting;
import com.hybris.oms.domain.SortDirection;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * A query object to be used when searching for
 * shipments using different criteria.
 */
public class ShipmentQueryObject extends QueryObject<ShipmentQuerySupport>
{
	private static final long serialVersionUID = -2288697057149779189L;

	public ShipmentQueryObject()
	{
		super();
		this.setSorting(ShipmentQuerySupport.DEFAULT, SortDirection.ASC);
	}

	public ShipmentQueryObject(final List<String> orderIds, final List<String> statusIds, final List<String> locationIds,
			final Integer pageNumber, final Integer pageSize, final String customerLastName, final Boolean preOrder)
	{
		this();
		if (CollectionUtils.isNotEmpty(orderIds))
		{
			this.setOrderIds(orderIds);
		}
		if (CollectionUtils.isNotEmpty(statusIds))
		{
			this.setShipmentStatusIds(statusIds);
		}
		if (CollectionUtils.isNotEmpty(locationIds))
		{
			this.setLocationIds(locationIds);
		}
		if (customerLastName != null)
		{
			this.setCustomerLastName(customerLastName);
		}
		if (preOrder != null)
		{
			this.setPreOrder(preOrder);
		}
		this.buildPageNumberAndSize(pageNumber, pageSize);
	}

	@SuppressWarnings({"PMD.ExcessiveParameterList", "PMD.ExcessiveParameterList2"})
	public ShipmentQueryObject(final List<String> orderIds, final List<String> statusIds, final List<String> locationIds,
			final Integer pageNumber, final Integer pageSize, final String customerLastName, final String sortColumn,
			final String sortDirection, final String startDate, final String endDate, final String startScheduledDate,
			final String endSchedulingDate, final String pickupInStore, final Boolean preOrder)
	{
		this(orderIds, statusIds, locationIds, pageNumber, pageSize, customerLastName, preOrder);
		if (startDate != null)
		{
			final Date beginDate = new Date(Long.valueOf(startDate));
			this.setStartDate(beginDate);
		}
		if (endDate != null)
		{
			final Date endyDate = new Date(Long.valueOf(endDate));
			this.setEndDate(endyDate);
		}

		if (startScheduledDate != null)
		{
			final Date beginScheduledDate = new Date(Long.valueOf(startScheduledDate));
			this.setStartScheduledDate(beginScheduledDate);
		}
		if (endSchedulingDate != null)
		{
			final Date endScheduledDate = new Date(Long.valueOf(endSchedulingDate));
			this.setEndScheduledDate(endScheduledDate);
		}

		if (pickupInStore != null)
		{
			this.setPickupInStore(Boolean.valueOf(pickupInStore));
		}
		if (!StringUtils.isEmpty(sortColumn))
		{
			final ShipmentQuerySupport column = ShipmentQuerySupport.valueOf(sortColumn);
			final SortDirection direction = StringUtils.isEmpty(sortDirection) ? SortDirection.ASC : SortDirection
					.valueOf(sortDirection);
			this.setSorting(new QuerySorting(column.name(), direction));
		}
	}

	public String getCustomerLastName()
	{
		return super.getValue("customerLastName");
	}

	public Boolean getPreOrder()
	{
		return this.getBooleanValue("preOrder") != null ? this.getBooleanValue("preOrder") : Boolean.FALSE;
	}

	public Date getEndDate()
	{
		return super.getDateValue("endDate");
	}

	public Boolean getPickupInStore()
	{
		return super.getBooleanValue("pickupInStore");
	}

	public List<String> getLocationIds()
	{
		return super.getValues("locId");
	}

	public List<String> getOrderIds()
	{
		return super.getValues("orderId");
	}

	public List<String> getShipmentStatusIds()
	{
		return super.getValues("statusId");
	}

	public final Date getStartDate()
	{
		return super.getDateValue("startDate");
	}

	public final void setPickupInStore(final Boolean pickupInStore)
	{
		this.superSetValue("pickupInStore", pickupInStore);
	}

	public final void setCustomerLastName(final String customerLastName)
	{
		this.superSetValue("customerLastName", customerLastName);

	}

	public final void setPreOrder(final Boolean preOrder)
	{
		this.superSetValue("preOrder", preOrder);

	}

	public final void setEndDate(final Date endDate)

	{
		this.superSetValue("endDate", endDate);

	}

	public final void setLocationIds(final List<String> locationIds)
	{
		this.superSetValues("locId", locationIds);
	}

	public final void setOrderIds(final List<String> orderIds)
	{
		this.superSetValues("orderId", orderIds);
	}

	public final void setShipmentStatusIds(final List<String> shipmentStatusIds)
	{
		this.superSetValues("statusId", shipmentStatusIds);
	}

	public final void setStartDate(final Date startDate)
	{
		this.superSetValue("startDate", startDate);
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
