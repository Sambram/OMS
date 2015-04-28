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
package com.hybris.oms.service.returns;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.returns.ReturnQueryObject;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.service.Flushable;

import java.util.List;


/**
 * Return service.
 */
public interface ReturnService extends Flushable
{

	/**
	 * Get a Return by Id.
	 *
	 * @param returnId
	 * @return Return object
	 */
	ReturnData findReturnById(final long returnId);

	/**
	 * Get a Return order Line by Id.
	 *
	 * @param returnOrderLineId
	 * @return ReturnOrderLine object
	 */
	ReturnOrderLineData findReturnOrderLineById(final long returnOrderLineId);


	/**
	 * Find paged returns by ReturnQueryObject Returns paged returns filtered by how the ReturnQueryObject is populated.
	 *
	 * @param returnQueryObject
	 * @return Page of ReturnData
	 */
	Page<ReturnData> findPagedReturnsByQuery(final ReturnQueryObject returnQueryObject);

	/**
	 * Find list of Returns by order Id
	 *
	 * @param orderId
	 * @return
	 */
	List<ReturnData> findReturnsByOrderId(final String orderId);

	/**
	 * Get the returnable Quantity for a return, an order and a sku. The return might be null in case of a creation. But
	 * the order id and the sku id have to be provided.
	 *
	 * @param returnData
	 * @param orderData
	 * @param skuId
	 *
	 * @return Quantity object
	 */
	QuantityVT getReturnableQuantity(final ReturnData returnData, final OrderData orderData, final String orderLineId,
			final String skuId);

	/**
	 * checks if Shipping cost has been refunded excluding Returns with CANCELED state
	 *
	 * @param returnData
	 * @param orderData
	 * @return true if one (and only one) Return for the father order had refunded for shipping cost false if non of the
	 *         Return(s) is refunded yet.
	 */
	Boolean shippingPreviouslyRefunded(final ReturnData returnData, final OrderData orderData);


	/**
	 *
	 * @return the list of return reason codes supported by the current configuration.
	 */
	List<String> getReturnReasonCodes();

	/**
	 * Accept all items in a return that are not rejected.
	 *
	 * @param theReturn
	 */
	void acceptAllNonRejectedItems(final ReturnData theReturn);

	/**
	 * Get the number of items that are rejected for the given return order line.
	 *
	 * @param returnOrderLine
	 * @return number of rejected items; never <tt>null</tt>
	 */
	Integer getQuantityRejected(final ReturnOrderLineData returnOrderLine);

}
