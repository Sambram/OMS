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

import com.hybris.cis.common.utils.StringUtils;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.rule.RuleService;
import com.hybris.oms.service.service.AbstractHybrisService;
import com.hybris.oms.service.shipment.ShipmentService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Date;
import java.util.List;


public class DefaultOrderService extends AbstractHybrisService implements OrderService
{

	private OrderQueryFactory orderQueries;
	private TenantPreferenceService tenantPreferenceService;
	private RuleService ruleService;
	private ShipmentService shipmentService;
	private PersistenceManager persistenceManager;
	private int orderPollPageSize;


	@Override
	public Page<OrderData> findPagedOrdersByQuery(final OrderQueryObject queryObject)
	{
		final int[] pageNumberAndSize = this.getPageNumberAndSize(queryObject, 0, this.getQueryPageSizeDefault());

		return this.findPaged(this.orderQueries.findOrdersByQuery(queryObject), pageNumberAndSize[0], pageNumberAndSize[1]);
	}

	@Override
	public List<OrderLineQuantityStatusData> getAllOrderLineQuantityStatuses()
	{
		return this.findAll(this.orderQueries.getAllOrderLineQuantityStatusesQuery());
	}

	@Override
	public OrderData getOrderByOlqId(final Long olqId)
	{
		final OrderLineQuantityData olqData = this.getOrderLineQuantityByOlqId(olqId);
		return olqData.getOrderLine().getMyOrder();
	}

	@Override
	public OrderData getOrderByOrderId(final String orderId)
	{
		try
		{
			return this.findOneSingle(this.orderQueries.getOrdersByIdQuery(orderId));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Order Id is not correct, " + orderId, e);
		}
	}

	@Override
	public OrderLineData getOrderLineByOrderIdAndOrderLineId(final String orderId, final String orderLineId)
			throws EntityNotFoundException
	{
		try
		{
			final OrderData orderData = persistenceManager.getByIndex(OrderData.UX_ORDERS_ORDERID, orderId);
			return persistenceManager.getByIndex(OrderLineData.UQ_ORDER_ORDERLINEID, orderData, orderLineId);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Order Line Id is not correct, " + orderLineId, e);
		}
	}

	protected OrderLineData getOrderLineById(final String orderLineId)
	{
		if (StringUtils.isBlank(orderLineId))
		{
			throw new IllegalArgumentException("order line id may not be empty");
		}
		try
		{
			return this.findOneSingle(this.orderQueries.getOrderLineByIdQuery(orderLineId));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("OrderLine not found. OrderLineId: " + orderLineId, e);
		}
	}

	public OrderLineData getOrderLineByOlqId(final Long olqId)
	{
		final OrderLineQuantityData olqData = this.getOrderLineQuantityByOlqId(olqId);
		return olqData.getOrderLine();
	}

	@Override
	public List<OrderLineQuantityData> getOrderLineQuantitiesByOlqIds(final List<Long> olqIds)
	{
		return this.findAll(this.orderQueries.getOrderLineQuantityByOlqIdsQuery(olqIds));
	}

	@Override
	public OrderLineQuantityData getOrderLineQuantityByOlqId(final Long olqId)
	{
		try
		{
			return this.findOneSingle(this.orderQueries.getOrderLineQuantityByOlqIdsQuery(Collections.singletonList(olqId)));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Order line quantity not found. OlqID : " + olqId, e);
		}
	}

	@Override
	public OrderLineQuantityStatusData getOrderLineQuantityStatusByStatusCode(final String statusCode)
	{
		if (statusCode == null)
		{
			throw new IllegalArgumentException("Status cannot be null.");
		}

		try
		{
			return this.findOneSingle(this.orderQueries.getOrderLineQuantityStatusByStatusCode(statusCode));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Order line quantity status not found. Status Code: " + statusCode, e);
		}
	}

	@Override
	public OrderLineQuantityStatusData getOrderLineQuantityStatusByTenantPreferenceKey(final String key)
			throws EntityNotFoundException
	{
		final TenantPreferenceData tenantPreference = this.tenantPreferenceService.getTenantPreferenceByKey(key);
		return this.getOrderLineQuantityStatusByStatusCode(tenantPreference.getValue());
	}

	@Override
	public Page<OrderData> findOrdersUpdatedAfter(final Date aDate)
	{

		return this.findPaged(this.orderQueries.getOrdersByModifiedTimeGreaterThanQuery(aDate), 0, orderPollPageSize);
	}

	@Override
	public void updateOrderLineQuantityStatus(final OrderLineQuantityData olq, final OrderLineQuantityStatusData newStatus)
	{
		final OrderLineQuantityStatusData previousStatus = olq.getStatus();
		olq.setStatus(newStatus);

		if (olq.getShipment() != null && !newStatus.getStatusCode().equals(olq.getShipment().getOlqsStatus()))
		{
			olq.getShipment().setOlqsStatus(newStatus.getStatusCode());
		}
		handleOlqStatusChange(olq, previousStatus, newStatus);
	}

	@Override
	public List<Long> getOlqIdsForShipment(final ShipmentData shipment)
	{
		return OrderDataStaticUtils.getOlqIdsForShipment(shipment);
	}

	// FIXME: This is just bad!!! Order Cancellation should be in the order workflow. (OMS-236)
	@Override
	@Deprecated
	public void cancelOrder(final OrderData orderToCancel)
	{
		boolean actionOccured = false;
		final OrderLineQuantityStatusData orderLineQuantityStatusPaymentCaptured = this
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PAYMENT_CAPTURED);
		final OrderLineQuantityStatusData orderLineQuantityStatusTaxInvoiced = this
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_TAX_INVOICED);

		if (orderToCancel.getOrderLines() != null)
		{
			for (final ShipmentData shipment : shipmentService.findAllShipmentsByOrderId(orderToCancel.getOrderId()))
			{
				try
				{
					final String olqStatus = shipment.getOlqsStatus() != null ? shipment.getOlqsStatus() : StringUtils.EMPTY;
					if (!olqStatus.equalsIgnoreCase(orderLineQuantityStatusPaymentCaptured.getStatusCode())
							&& !olqStatus.equalsIgnoreCase(orderLineQuantityStatusTaxInvoiced.getStatusCode()))
					{
						this.shipmentService.cancelShipment(shipment);
						actionOccured = true;
					}
				}
				catch (final IllegalStateException e)
				{
					// ignore this exception because if one shipment is cancelled, other shipments should still be possible
					// to cancel
				}
			}
			updateOrderLineQuantitiesStatus(orderToCancel.getOrderLines(), TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED);
		}
		if (!actionOccured)
		{
			throw new IllegalStateException(
					String.format("Cannot perform requested action, order[%s] shipments already cancelled or confirmed.",
							orderToCancel.getOrderId()));
		}
	}

	/**
	 * Check if the order is cancellable by checking that the status of at least one olq is not TAX_INVOICED no CANCELLED
	 * 
	 * @param order the order to check its status
	 * @return true if the order is cancellable, false otherwise
	 */
	@Override
	@Deprecated
	public boolean isOrderCancellable(final OrderData order)
	{
		final OrderLineQuantityStatusData orderLineQuantityStatusCancelled = this
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED);

		final OrderLineQuantityStatusData orderLineQuantityStatusTaxInvoiced = this
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_TAX_INVOICED);

		final OrderLineQuantityStatusData orderLineQuantityStatusPaymentCaptured = this
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PAYMENT_CAPTURED);

		final OrderLineQuantityStatusData orderLineQuantityStatusShipped = this
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);

		final OrderLineQuantityStatusData orderLineQuantityStatusDeclined = this
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_MANUAL_DECLINED);

		for (final OrderLineData orderLine : order.getOrderLines())
		{
			if (orderLine.getOrderLineQuantities() != null)
			{
				for (final OrderLineQuantityData orderLineQuantity : orderLine.getOrderLineQuantities())
				{
					if (!orderLineQuantity.getStatus().getStatusCode().equals(orderLineQuantityStatusCancelled.getStatusCode())
							&& !orderLineQuantity.getStatus().getStatusCode().equals(orderLineQuantityStatusTaxInvoiced.getStatusCode())
							&& !orderLineQuantity.getStatus().getStatusCode()
									.equals(orderLineQuantityStatusPaymentCaptured.getStatusCode())
							&& !orderLineQuantity.getStatus().getStatusCode().equals(orderLineQuantityStatusShipped.getStatusCode())
							&& !orderLineQuantity.getStatus().getStatusCode().equals(orderLineQuantityStatusDeclined.getStatusCode()))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPickUpOnlyOrder(final OrderData order)
	{
		for (final OrderLineData line : order.getOrderLines())
		{
			if (org.apache.commons.lang.StringUtils.isBlank(line.getPickupStoreId()))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public List<OrderLineData> getOrderLinesByShipment(final ShipmentData shipment)
	{
		return this.findAll(this.orderQueries.getOrderLinesByShipmentQuery(shipment));
	}

	@Override
	public void deleteOrderLineQuantities(final List<OrderLineQuantityData> olqs)
	{
		try
		{
			for (final OrderLineQuantityData olq : olqs)
			{
				this.getPersistenceManager().remove(olq.getId());
			}
		}
		catch (final IllegalArgumentException | ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Olq not found.", e);
		}
	}

	@Override
	public void removeUnassignedQuantities(final OrderData order)
	{
		int quantity = 0;
		int quantityUnassigned = 0;
		for (final OrderLineData orderLine : order.getOrderLines())
		{
			quantity = orderLine.getQuantityValue();
			quantityUnassigned = orderLine.getQuantityUnassignedValue();

			orderLine.setQuantityValue(quantity - quantityUnassigned);
			orderLine.setQuantityUnassignedValue(0);
		}
	}

	// FIXME: This is really bad!
	@Deprecated
	protected void updateOrderLineQuantitiesStatus(final List<OrderLineData> orderLines, final String status)
	{
		final OrderLineQuantityStatusData orderLineQuantityStatusCancelled = getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_CANCELED);
		final OrderLineQuantityStatusData orderLineQuantityStatusTaxInvoiced = getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_TAX_INVOICED);
		final OrderLineQuantityStatusData orderLineQuantityStatusShipped = getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SHIPPED);
		final OrderLineQuantityStatusData orderLineQuantityStatusPaymnentCaptured = getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PAYMENT_CAPTURED);
		final OrderLineQuantityStatusData orderLineQuantityStatusManualDeclined = getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_MANUAL_DECLINED);
		for (final OrderLineData orderLineData : orderLines)
		{
			if (orderLineData.getOrderLineQuantities() != null)
			{
				for (final OrderLineQuantityData orderLineQuantityData : orderLineData.getOrderLineQuantities())
				{
					if (!orderLineQuantityData.getStatus().getStatusCode().equals(orderLineQuantityStatusCancelled.getStatusCode())
							&& !orderLineQuantityData.getStatus().getStatusCode()
									.equals(orderLineQuantityStatusTaxInvoiced.getStatusCode())
							&& !orderLineQuantityData.getStatus().getStatusCode().equals(orderLineQuantityStatusShipped.getStatusCode())
							&& !orderLineQuantityData.getStatus().getStatusCode()
									.equals(orderLineQuantityStatusPaymnentCaptured.getStatusCode())
							&& !orderLineQuantityData.getStatus().getStatusCode()
									.equals(orderLineQuantityStatusManualDeclined.getStatusCode()))
					{
						this.updateOrderLineQuantityStatus(orderLineQuantityData,
								this.getOrderLineQuantityStatusByTenantPreferenceKey(status));
					}
				}

				orderLineData.setQuantityUnassignedValue(0);
			}
		}
	}

	protected void handleOlqStatusChange(final OrderLineQuantityData olq, final OrderLineQuantityStatusData previousOLQStatus,
			final OrderLineQuantityStatusData currentOLQStatus)
	{
		// Only create an event if the olq has changed status.
		if ((previousOLQStatus != null) && !previousOLQStatus.equals(currentOLQStatus))
		{
			ruleService.executeOLQStatusChange(previousOLQStatus.getStatusCode(), currentOLQStatus.getStatusCode(), olq);
		}
	}

	protected OrderQueryFactory getOrderQueries()
	{
		return orderQueries;
	}

	@Required
	public void setOrderQueries(final OrderQueryFactory orderQueries)
	{
		this.orderQueries = orderQueries;
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}

	@Required
	public void setTenantPreferenceService(final TenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

	protected RuleService getRuleService()
	{
		return ruleService;
	}

	@Required
	public void setRuleService(final RuleService ruleService)
	{
		this.ruleService = ruleService;
	}

	protected ShipmentService getShipmentService()
	{
		return shipmentService;
	}

	@Required
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
	}

	@Override
	protected PersistenceManager getPersistenceManager()
	{
		return persistenceManager;
	}

	@Override
	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	protected int getOrderPollPageSize()
	{
		return orderPollPageSize;
	}

	@Required
	public void setOrderPollPageSize(int orderPollPageSize)
	{
		this.orderPollPageSize = orderPollPageSize;
	}
}
