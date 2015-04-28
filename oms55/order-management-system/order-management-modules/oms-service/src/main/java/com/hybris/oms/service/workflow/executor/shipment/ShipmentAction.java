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
package com.hybris.oms.service.workflow.executor.shipment;

/**
 * Represents the valid actions that can be performed when a shipment is waiting in the user task.
 */
public enum ShipmentAction
{
	/**
	 * User wishes to perform cancel shipment action.
	 */
	CANCEL,

	/**
	 * User wishes to perform decline shipment action.
	 */
	DECLINE,

	/**
	 * User wishes to perform confirm shipment action.
	 */
	CONFIRM,

	/**
	 * User wishes to split a shipment by olqs.
	 */
	SPLIT_OLQ,

	/**
	 * User wishes to split a shipment by olq quantities.
	 */
	SPLIT_QUANTITIES,

	/**
	 * User wishes to reallocate a shipment to a different location.
	 */
	REALLOCATE,

	/**
	 * User wishes to update the shipment status.
	 */
	STATUS_UPDATE,


	/**
	 * User wishes to manually capture payment
	 */
	MANUAL_CAPTURE;
}
