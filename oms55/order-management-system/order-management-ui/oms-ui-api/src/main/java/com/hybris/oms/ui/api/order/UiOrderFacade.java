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
package com.hybris.oms.ui.api.order;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderQueryObject;


/**
 * The Interface OrderServices Api.
 */
public interface UiOrderFacade
{

	/**
	 * Search orders regarding how the query (a OrderQueryObject) is populated. See OrderQueryObject to find out
	 * available features.
	 * 
	 * @category OMS-UI
	 * 
	 * @param queryObject The {@link com.hybris.oms.domain.QueryObject} that determines how
	 *           the search should be performed.
	 *           <p/>
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           queryObject.pageNumber must not be null.
	 *           <dd>
	 *           queryObject.pageNumber must not be less than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be null.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than max allowed page size.
	 * @return A pageable results of {@link Order}.
	 * @throws EntityValidationException if preconditions are not met.
	 */
	Pageable<UIOrder> findOrdersByQuery(OrderQueryObject queryObject) throws EntityValidationException;

    /**
     * Gets a specific UIOrderDetails with a specific Order id.
     *
     * @category OMS-UI
     *
     * @param orderId the order id to fetch
     * @return the UIOrderDetails searched
     * @throws EntityNotFoundException if UIOrderDetails was not found
     */
    UIOrderDetails getUIOrderDetailsByOrderId(final String orderId) throws EntityNotFoundException;

    /**
     * Gets a specific UIOrder with a specific Order id.
     *
     * @category OMS-UI
     *
     * @param orderId the order id to fetch
     * @return the UIOrder searched
     * @throws EntityNotFoundException if UIOrder was not found
     */
    UIOrder getUIOrderByOrderId(final String orderId) throws EntityNotFoundException;

}
