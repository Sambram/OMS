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
package com.hybris.oms.service.order;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.service.Flushable;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;

import java.util.Date;
import java.util.List;


/**
 * The Interface OrderService.
 */
public interface OrderService extends Flushable
{
	/**
	 * @param queryObject The {@link com.hybris.oms.domain.QueryObject} that determines how
	 *                    the search should be performed.
	 * @return A pageable collection of OrderData.
	 */
	Page<OrderData> findPagedOrdersByQuery(OrderQueryObject queryObject);

	/**
	 * Find all order line quantity statuses.
	 *
	 * @return list of olqs
	 */
	List<OrderLineQuantityStatusData> getAllOrderLineQuantityStatuses();

	/**
	 * Find the order by an olq id.
	 *
	 * @param olqId the olq id
	 * @return Order
	 */
	OrderData getOrderByOlqId(Long olqId);

	/**
	 * Find all order line quantities by multiple olq ids.
	 *
	 * @param olqIds the olq ids
	 * @return list of olq
	 */
	List<OrderLineQuantityData> getOrderLineQuantitiesByOlqIds(List<Long> olqIds);

	/**
	 * Find the order line quantity by olq id.
	 *
	 * @param olqId the olq id
	 * @return olq
	 */
	OrderLineQuantityData getOrderLineQuantityByOlqId(Long olqId);

	/**
	 * Find an order line quantity by status code.
	 *
	 * @param statusCode the status code
	 * @return olqs
	 * @throws EntityNotFoundException
	 */
	OrderLineQuantityStatusData getOrderLineQuantityStatusByStatusCode(String statusCode) throws EntityNotFoundException;

	/**
	 * Get an olq status by a tenant preference key. First get the Tenant Preference, then look for the olq status by
	 * this value.
	 *
	 * @param key
	 * @return OrderLineQuantityStatus
	 * @throws EntityNotFoundException
	 */
	OrderLineQuantityStatusData getOrderLineQuantityStatusByTenantPreferenceKey(String key) throws EntityNotFoundException;

	/**
	 * Find an order by order id.
	 *
	 * @param orderId the order id
	 * @return order
	 * @throws EntityNotFoundException
	 */
	OrderData getOrderByOrderId(String orderId) throws EntityNotFoundException;

	/**
	 * Update order line quantity status.
	 * Only this method should be used to update olq status (instead of direct use of
	 * {@link OrderLineQuantityData#setStatus(OrderLineQuantityStatusData)}),
	 * since it performs additional steps necessary when updating olq status: synchronizing shipment status, invoking
	 * business rules, etc.
	 *
	 * @param olq       must not be null
	 * @param newStatus must not be null
	 */
	void updateOrderLineQuantityStatus(OrderLineQuantityData olq, OrderLineQuantityStatusData newStatus);

	/**
	 * Finds all orders updated after a date.
	 *
	 * @param aDate the cut-off date
	 * @return orders updated after a date
	 */
	Page<OrderData> findOrdersUpdatedAfter(Date aDate);

	/**
	 * Get an array of all order line quantity ids for given shipment.
	 *
	 * @param shipment the shipment
	 * @return List of olqIds
	 * @deprecated use {@link OrderDataStaticUtils#getOlqIdsForShipment(ShipmentData)} instead
	 */
	@Deprecated List<Long> getOlqIdsForShipment(final ShipmentData shipment);

	/**
	 * Cancel an order.
	 *
	 * @param orderToCancel the order to cancel
	 */
	void cancelOrder(OrderData orderToCancel);

	/**
	 * Determines if an order is cancellable.
	 *
	 * @param order order check if cancellable.
	 * @return boolean.
	 * @deprecated - Use {@link WorkflowExecutor#getActions(com.hybris.kernel.api.ManagedObject)} to get available
	 * actions on an {@link OrderData} entity.
	 */
	@Deprecated boolean isOrderCancellable(OrderData order);

	/**
	 * Determines if order is pickup in store
	 *
	 * @param order order to check if pick up in store
	 * @return boolean.
	 */
	boolean isPickUpOnlyOrder(final OrderData order);

	/**
	 * Finds all order lines by shipment.
	 *
	 * @param shipment the shipment
	 * @return List of {@link OrderLineData}
	 */
	List<OrderLineData> getOrderLinesByShipment(final ShipmentData shipment);

	/**
	 * Deletes a list of order line quantities
	 *
	 * @param olqs - The order line quantities to be deleted
	 */
	void deleteOrderLineQuantities(List<OrderLineQuantityData> olqs);

	/**
	 * Finds an order line by order line ID.
	 *
	 * @param orderId     the order Id
	 * @param orderLineId the order Line Id
	 * @return {@link OrderLineData}
	 * @throws EntityNotFoundException
	 */
	OrderLineData getOrderLineByOrderIdAndOrderLineId(final String orderId, final String orderLineId)
			throws EntityNotFoundException;

	/**
	 * Removes unassigned quantities from each {@link OrderLineData} in the {@link OrderData}.
	 * <p/>
	 * <p>
	 * This will set {@link OrderLineData#QUANTITYUNASSIGNEDVALUE} to <tt>ZERO</tt> and reduce the
	 * {@link OrderLineData#QUANTITYVALUE} by {@link OrderLineData#QUANTITYUNASSIGNEDVALUE} for each order line in the
	 * order.
	 * </p>
	 *
	 * @param order
	 */
	void removeUnassignedQuantities(final OrderData order);
}
