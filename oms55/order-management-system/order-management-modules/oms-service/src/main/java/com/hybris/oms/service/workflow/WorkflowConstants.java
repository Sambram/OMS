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
package com.hybris.oms.service.workflow;

/**
 * Workflow constants.
 */
public final class WorkflowConstants
{

	public static final String KEY_LOCATION_ID = "locationId";
	public static final String KEY_ORDER_ID = "orderId";
	public static final String KEY_SHIPMENT_ID = "shipmentId";
	public static final String KEY_SPLIT_SHIPMENT_ID = "splitShipmentId";
	public static final String KEY_RETURN_ID = "returnId";
	public static final String KEY_OLQ_STATUS = "olqStatus";
	public static final String KEY_APPROVAL = "approval";
	public static final String KEY_ONLINE_RETURN = "onlineReturn";
	public static final String KEY_PROCESSED = "processed";
	public static final String KEY_PREVIOUS_CALCULATED_AMOUNT = "previousCalculatedAmount";

	public static final String KEY_TIMEOUT_WAIT_SHIPMENT = "waitShipmentTimeout";
	public static final String KEY_TIMEOUT_RETRY_FULFILLMENT = "fulfillmentRetryTimeout";

	public static final String STATE_QUEUED = "QUEUED";
	public static final String STATE_ORDER_NEW = "NEW_ORDER";
	public static final String STATE_SHIPMENT_NEW = "NEW_SHIPMENT";
	public static final String STATE_SHIPMENT_ERROR = "ERROR_SHIPMENT";
	public static final String STATE_SHIPMENT_UPDATE = "SHIPMENT_UPDATE";
	public static final String STATE_MANUAL_SET_STATUS = "SET_STATUS";
	public static final String STATE_SOURCING_PREFIX = "SOURCING_";
	public static final String STATE_RETURN_NEW = "NEW_RETURN";
	public static final String STATE_RETURN_UPDATE = "UPDATE_RETURN";
	public static final String STATE_RETURN_CANCELED = "CANCELED";
	public static final String STATE_RETURN_FAIL_TAX_REVERSE = "WFE_FAIL_TAX_REVERSE";
	public static final String STATE_RETURN_TAX_REVERSE_FIXED = "WFE_TAX_REVERSE_FIXED";

	public static final String CONFIRM_INSTORE_REFUND_USER_TASK = "ConfirmInStoreRefund_UserTask";
	public static final String CONFIRM_ONLINE_REFUND_USER_TASK = "ConfirmOnlineRefund_UserTask";
	public static final String FIX_TAX_REVERSE_USER_TASK = "FixTaxReverse_UserTask";
	public static final String WAIT_FOR_SHIPMENT_USER_TASK = "WaitForShipment_UserTask";
	public static final String FULFILL_INCOMPLETE_USER_TASK = "FulfillIncomplete_UserTask";
	public static final String APPROVE_RETURN_USER_TASK = "ApproveReturn_UserTask";
	public static final String WAIT_FOR_GOODS_USER_TASK = "WaitForGoods_UserTask";
	public static final String CAPTURE_ACTION_USER_TASK = "CaptureAction_UserTask";


	public static final String SIGNAL_SHIPMENT_DONE = "shipmentDone";


	private WorkflowConstants()
	{
		//
	}

}
