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
package com.hybris.oms.service.workflow.executor.returns;

/**
 * Represents the valid actions that can be performed when a Return is waiting in the user task.
 */
public enum ReturnAction
{


	/**
	 * User wishes to perform auto-update return action.
	 */
	AUTO_REFUND,

	/**
	 * User wishes to perform manual-update return action.
	 */
	MANUAL_REFUND,

	/**
	 * User wishes to perform update return action.
	 */
	UPDATE,

	/**
	 * User wishes to perform cancel return action.
	 */
	CANCEL,

	/**
	 * User wishes to complete the process blocked by a tax reverse failure.
	 */
	TAX_FIXED,

	/**
	 * User wishes to create return review actions.
	 */
	REVIEW

}
