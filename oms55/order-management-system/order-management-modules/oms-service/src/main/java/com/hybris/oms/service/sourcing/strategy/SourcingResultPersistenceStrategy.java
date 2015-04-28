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
package com.hybris.oms.service.sourcing.strategy;

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.context.ProcessStatus;


/**
 * Strategy for persisting a sourcing result.
 */
public interface SourcingResultPersistenceStrategy
{
	/**
	 * Persists the {@link ProcessStatus} creating new
	 * {@link com.hybris.oms.service.managedobjects.order.OrderLineQuantityData} instances for every {@link SourcingOLQ}
	 * contained in the result.
	 */
	void persistSourcingResult(OrderData order, final SourcingResult result);
}
