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
package com.hybris.oms.service.workflow.executor.order;

/**
 * Represents the valid actions that can be performed when an order is waiting in a user task.
 */
public enum OrderAction
{
	/**
	 * User wishes to perform cancel_unfulfilled order action.
	 */
	CANCEL_UNFULFILLED,

	/**
	 * User wishes to perform fulfill order action.
	 */
	FULFILL,

	/**
	 * System wishes to unblock the order process.
	 */
	CONTINUE;

}
