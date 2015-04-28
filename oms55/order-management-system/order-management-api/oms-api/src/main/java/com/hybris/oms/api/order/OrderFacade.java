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
package com.hybris.oms.api.order;

import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.order.UpdatedSinceList;

import java.util.Date;
import java.util.List;


/**
 * The Interface OrderServices Api.
 */
public interface OrderFacade
{

	/**
	 * Creates a new order with given parameters.
	 * 
	 * @category PLATFORM EXTENSION - omsorders
	 * 
	 * @param order - the order values to use to create the new order. <dt><b>Preconditions:</b>
	 *           <dd>
	 *           Order.orderId is not empty and not null
	 *           <dd>
	 *           Order.userName is not empty and not null
	 *           <dd>
	 *           Order.issueDate is not null
	 *           <dd>
	 *           Order.orderLines {@link com.hybris.oms.domain.order.OrderLine}
	 *           <dd>
	 *           Order.currencyCode is not null
	 * @return the created order
	 * @throws EntityValidationException - order validation failed.
	 * @throws DuplicateEntityException - the order already exists.
	 */
	Order createOrder(final Order order) throws EntityValidationException, DuplicateEntityException;

	/**
	 * Fulfill (source + allocate) an order.
	 * 
	 * @param orderId - the id of the order to be fulfilled
	 * @throws EntityNotFoundException - if the order does not exist
	 * @throws InvalidOperationException - if the order is not in a state that allows it to be fulfilled
	 */
	void fulfill(final String orderId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Cancel all items that have not been fulfilled.
	 * 
	 * @param orderId - the id of the order to cancel unfulfilled items
	 * @throws EntityNotFoundException - if the order does not exist
	 * @throws InvalidOperationException - if the order is not in a state that allows it to cancel unfulfilled items
	 */
	void cancelUnfulfilled(final String orderId) throws EntityNotFoundException, InvalidOperationException;

	/**
	 * Cancels the order with the given ID.
	 * 
	 * @category OMS-UI
	 * 
	 * @param orderId the order to be cancelled. <dt><b>Preconditions:</b>
	 *           <dd>
	 *           Order.orderId is not empty and not null
	 * @return the cancelled order
	 * @throws EntityValidationException - if the order does not exist
	 * @throws InvalidOperationException - if the order is already cancelled or confirmed
	 * 
	 * @deprecated - Please use {@link OrderFacade#cancelUnfulfilled(String)} to cancel the cancellable portion of an
	 *             order.
	 */
	@Deprecated
	Order cancelOrder(final String orderId) throws EntityValidationException, InvalidOperationException;

	/**
	 * create a new OLQS with given values.
	 * 
	 * @category OMS-UI
	 * 
	 * @param newStatus the OLQS values to use to create the new status <dt><b>Preconditions:</b>
	 *           <dd>
	 *           orderLineQuantityStatusCode is not null
	 * @return the created order line quantity status
	 * @throws EntityValidationException - if preconditions are not met.
	 * @throws DuplicateEntityException - order line quantity status already exists
	 */
	OrderLineQuantityStatus createOrderLineQuantityStatus(final OrderLineQuantityStatus newStatus)
			throws EntityValidationException, DuplicateEntityException;

	/**
	 * Find all possible statuses for an order line.
	 * 
	 * @category OMS-UI
	 * 
	 * @return list of status available
	 */
	List<OrderLineQuantityStatus> findAllOrderLineQuantityStatuses();

	/**
	 * Gets a specific order with a specific id.
	 * 
	 * @category PLATFORM EXTENSION - omsorders
	 * @category OMS-UI
	 * 
	 * @param orderId the order id to fetch
	 * @return the order searched
	 * @throws EntityNotFoundException if Order was not found
	 */
	Order getOrderByOrderId(final String orderId) throws EntityNotFoundException;

	/**
	 * Updates an existing OLQS. {@link com.hybris.oms.domain.order.OrderLineQuantityStatus}
	 * 
	 * @category OMS-UI
	 * 
	 * @param status the status to update
	 * @return the updated order line quantity status
	 * @throws EntityNotFoundException - if the order line quantity status does not exist
	 */
	OrderLineQuantityStatus updateOrderLineQuantityStatus(final OrderLineQuantityStatus status) throws EntityNotFoundException;

	/**
	 * Returns only IDs of order that changed since a given date.
	 * 
	 * @category PLATFORM EXTENSION - omsorders
	 * 
	 * @param aDate cut-off date: every order changed after this date will be returned
	 * @return the list of IDs of changed orders
	 */
	UpdatedSinceList<String> getOrderIdsUpdatedAfter(final Date aDate);

}
