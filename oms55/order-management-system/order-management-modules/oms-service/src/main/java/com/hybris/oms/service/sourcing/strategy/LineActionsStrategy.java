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
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.context.ProcessStatus;


/**
 * Interface to react on line actions as recorded during sourcing in {@link ProcessStatus}.
 */
public interface LineActionsStrategy
{

	/**
	 * Extension to process any line actions.
	 * 
	 * @param order order
	 * @param sourcingResult sourcing result potentially containing line actions
	 */
	void processLineActions(OrderData order, SourcingResult sourcingResult);

}
