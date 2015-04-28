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
package com.hybris.oms.service.sourcing.strategy.impl;

import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_SOURCING_PREFIX;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.strategy.SourcingResultPersistenceStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Preconditions;


/**
 * Default implementation of {@link SourcingResultPersistenceStrategy}.
 */
public class DefaultSourcingResultPersistenceStrategy implements SourcingResultPersistenceStrategy
{

	private OrderService orderService;

	private PersistenceManager persistenceManager;

	@Override
	public void persistSourcingResult(final OrderData order, final SourcingResult result)
	{
		Preconditions.checkArgument(order != null, "order cannot be null");
		Preconditions.checkArgument(result != null, "sourcing result cannot be null");
		if (!result.isEmpty())
		{
			final OrderLineQuantityStatusData olqStatus = this.orderService
					.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SOURCED);

			final List<OrderLineData> orderLines = order.getOrderLines();
			final Set<String> orderLineIds = new HashSet<>(orderLines.size());
			for (final OrderLineData orderLine : orderLines)
			{
				orderLineIds.add(orderLine.getOrderLineId());
				this.processOLQs(result, olqStatus, orderLine);
			}
			validateOrderLineIds(result, order.getOrderId(), orderLineIds);


			this.persistenceManager.flush();
			persistenceManager.refresh(order);
		}
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	protected void processOLQs(final SourcingResult result, final OrderLineQuantityStatusData olqStatus,
			final OrderLineData orderLine)
	{
		int quantity = 0;
		final String orderLineId = orderLine.getOrderLineId();
		for (final SourcingOLQ olq : result.getSourcingOlqs())
		{
			if (olq.getLineId().equals(orderLineId))
			{
				this.createOLQ(olqStatus, orderLine, olq);
				quantity += olq.getQuantity();
			}
		}
		if (quantity > 0)
		{
			updateQuantityUnassigned(orderLine, quantity);
		}
	}

	protected void createOLQ(final OrderLineQuantityStatusData olqStatus, final OrderLineData orderLine, final SourcingOLQ olq)
	{
		final OrderLineQuantityData olqData = this.persistenceManager.create(OrderLineQuantityData.class);
		olqData.setStockroomLocationId(olq.getLocationId());
		olqData.setOrderLine(orderLine);
		olqData.setQuantityValue(olq.getQuantity());
		olqData.setStatus(olqStatus);
		if (MapUtils.isNotEmpty(olq.getProperties()))
		{
			for (final Entry<String, Object> entry : olq.getProperties().entrySet())
			{
				olqData.setProperty(entry.getKey(), entry.getValue());
			}
		}
		orderLine.getOrderLineQuantities().add(olqData);
	}

	protected void updateQuantityUnassigned(final OrderLineData orderLine, final int quantity)
	{
		final int quantityUnassigned = orderLine.getQuantityUnassignedValue();
		Preconditions.checkState(orderLine.getQuantityUnassignedValue() >= quantity,
				"Quantity unassigned %s less than assigned %s", Integer.valueOf(quantityUnassigned), Integer.valueOf(quantity));
		orderLine.setQuantityUnassignedValue(quantityUnassigned - quantity);
	}

	protected void validateOrderLineIds(final SourcingResult result, final String orderId, final Set<String> orderLineIds)
	{
		for (final SourcingOLQ olq : result.getSourcingOlqs())
		{
			if (!orderLineIds.contains(olq.getLineId()))
			{
				throw new EntityNotFoundException("OrderLine " + olq.getLineId() + " not found for orderId " + orderId);
			}
		}
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	protected PersistenceManager getPersistenceManager()
	{
		return persistenceManager;
	}
}
