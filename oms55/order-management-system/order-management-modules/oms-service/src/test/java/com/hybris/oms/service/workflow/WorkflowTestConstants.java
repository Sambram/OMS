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
 * Constants used in Activiti workflow unit/integration tests.
 */
public final class WorkflowTestConstants
{
	/* Workflow Process IDs */
	public static final String WORKFLOW_ORDER_NAME = "OrderProcess";
	public static final String WORKFLOW_SHIPMENT_NAME = "ShipmentProcess";
	public static final String WORKFLOW_RETURN_NAME = "ReturnProcess";

	/* Order and Shipment ids used by the various work item worker stubs. */
	public static final String FAIL_GEOCODING_ORDER_ID = "FAIL_GEOCODING";
	public static final String FAIL_GEOCODING_AND_FULFILLMENT_ORDER_ID = "FAIL_GEOCODING_AND_FULFILLMENT";
	public static final String FAIL_FULFILLMENT_ORDER_ID = "FAIL_FULFILLMENT";
	public static final String PARTIAL_FULFILLMENT_ORDER_ID = "PARTIAL_FULFILLMENT";
	public static final String FAIL_PAYMENT_SHIPMENT_ID = "FAIL_PAYMENT";
	public static final String FAIL_TAXES_SHIPMENT_ID = "FAIL_TAXES";
	public static final String SUCCESS_SHIPMENT_ID = "998";
	public static final String FAIL_SHIPMENT_STATE_SHIPMENT_ID = "FAIL_SHIPMENT_STATE";
	public static final String SUCCESS_ORDER_ID = "123";
	public static final String FAIL_ORDER_STATE_ORDER_ID = "FAIL_ORDER_STATE";

	public static final String FAILURE_MSG = "unexpected failure";

	public static final String TENANT = "single";
	public static final String ACTION = "HELLO_WORLD";

	public static final String ACTIVITY_TYPE_START = "startEvent";
	public static final String ACTIVITY_TYPE_END = "endEvent";
	public static final String ACTIVITY_TYPE_EX_GATEWAY = "exclusiveGateway";

	/* Tenant preference keys (these will be populated under these names automatically) */
	public static final String KEY_FULFILLMENT = "fulfillment";
	public static final String KEY_PAYMENT_CAPTURE = "paymentCapture";
	public static final String KEY_TAX_INVOICE = "taxInvoice";

	/* Activiti activity ids as per the diagrams */
	public static final String ACTIVITY_ID_GEOCODING = "Geocoding";
	public static final String ACTIVITY_ID_FULFILLMENT = "Fulfillment";
	public static final String ACTIVITY_ID_SKIPPED_FULFILLMENT = "SKIPPED_FULFILLMENT";
	public static final String ACTIVITY_ID_FAILED_GEOCODING = "FAILED_GEOCODING";
	public static final String ACTIVITY_ID_FAILED_FULFILLMENT = "FAILED_FULFILLMENT";
	public static final String ACTIVITY_ID_DECLINED = "DECLINED";
	public static final String ACTIVITY_ID_PAYMENT_CAPTURE = "PaymentCapture";
	public static final String ACTIVITY_ID_FAILED_PAYMENT = "FAILED_PAYMENT";
	public static final String ACTIVITY_ID_TAX_INVOICE = "TaxInvoice";
	public static final String ACTIVITY_ID_FAILED_TAXES = "FAILED_TAXES";
	public static final String ACTIVITY_ID_DONE = "DONE";
	public static final String ACTIVITY_ID_CANCELLED = "CANCELLED";
	public static final String ACTIVITY_ID_SHIPMENT_ACTION = "ShipmentAction";
	public static final String ACTIVITY_ID_PARTIAL_FULFILLMENT = "PARTIAL_FULFILLMENT";
	public static final String ACTIVITY_ID_SHIPMENTS_DONE_VALIDATION = "ShipmentsDoneValidation";
	public static final String ACTIVITY_ID_SHIPMENT_DONE_CATCH_EVENT = "ShipmentDoneCatchEvent";


	public static final String ACTIVITY_ID_RETURN_CONFIRM_REFUND_ACTION = "ConfirmRefund";

	public static final String SIGNAL_SHIPMENT_DONE = "shipmentDone";

	/* KEYS used in testing */
	public static final String KEY_MESSAGE_NAME = "MESSAGE";

	private WorkflowTestConstants()
	{
		// Private constructor
	}
}
