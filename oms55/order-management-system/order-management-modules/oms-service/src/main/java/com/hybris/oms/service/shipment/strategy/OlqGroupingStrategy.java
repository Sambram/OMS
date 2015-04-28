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
package com.hybris.oms.service.shipment.strategy;

import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;

import java.util.List;


/**
 * Internal strategy for grouping OLQs during allocation i.e. shipment creation.
 */
public interface OlqGroupingStrategy
{

	/**
	 * Group order line quantities into groups with olqs containing similar
	 * location and status.
	 * 
	 * @param olqs
	 *           the olqs
	 * @param usesPickupStoreId take pickupStoreLocationId into account for grouping
	 * @return List of SubLists each containing OrderLineQuantityData, never <tt>null</tt>
	 */
	List<List<OrderLineQuantityData>> groupOlqs(List<OrderLineQuantityData> olqs);

}
