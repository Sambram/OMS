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
package com.hybris.oms.domain.preference;

/**
 * The Class TenantPreferenceConstants.
 */
public final class TenantPreferenceConstants
{
	// Tenant preferences types

	/**
	 * default ATS formula setting.
	 */
	public static final String PREF_TYPE_ATS_CALCULATOR = "preftype_atscalculator";

	/**
	 * ATS global inventory threshold.
	 */
	public static final String PREF_TYPE_ATS_GLOBAL_THRHOLD = "preftype_atsglobalthreshold";

	public static final String PREF_TYPE_OLQ_STATUS = "preftype_olqstatus";

	public static final String PREF_TYPE_ITEM_GROUPING = "preftype_itemgrouping";

	public static final String PREF_TYPE_WORKFLOW_TASK = "preftype_workflowtask";

	public static final String PREF_TYPE_ORDER_SPLIT_STRATEGIES = "preftype_ordersplitstrategy";

	public static final String PREF_TYPE_OLINE_SPLIT_STRATEGIES = "preftype_orderlinesplitstrategy";

	public static final String PREF_TYPE_SOURCING_ORDER_LEVEL = "preftype_sourcingorderlevel";

	public static final String PREF_TYPE_SOURCING_ORDERLINE_LEVEL = "preftype_sourcingorderlinelevel";

	public static final String PREF_TYPE_SOURCING_OLQ_LEVEL = "preftype_sourcingorderlinequantitylevel";

	public static final String PREF_TYPE_ATTRIBUTES = "preftype_attribute";

	public static final String PREF_TYPE_MODULE_PROPERTIES = "preftype_moduleprop";

	public static final String PREF_SHIPPED_STATUS = "preftype_shipped_status";

	// Tenant preferences keys
	/**
	 * Key for the default ATS formula setting.
	 */
	public static final String PREF_KEY_ATS_CALCULATOR = "ats.calculator";

	/**
	 * Key for the ATS global inventory threshold.
	 */
	public static final String PREF_KEY_ATS_GLOBAL_THRHOLD = "ats.global.threshold";

	public static final String PREF_KEY_OLQSTATUS_SOURCED = "olqstatus.sourced";

	public static final String PREF_KEY_OLQSTATUS_ALLOCATED = "olqstatus.allocated";

	public static final String PREF_KEY_OLQSTATUS_SHIPPED = "olqstatus.shipped";
	public static final String PREF_KEY_OLQSTATUS_FAILEDCAPTURE = "olqstatus.failedpayment";
	public static final String PREF_KEY_OLQSTATUS_FAILEDTAX = "olqstatus.failedtax";

	public static final String PREF_KEY_OLQSTATUS_PICKED = "olqstatus.picked";

	public static final String PREF_KEY_OLQSTATUS_PACKED = "olqstatus.packed";

	public static final String PREF_KEY_OLQSTATUS_MANUAL_DECLINED = "olqstatus.manual_declined";

	public static final String PREF_KEY_OLQSTATUS_PAYMENT_CAPTURED = "olqstatus.payment_captured";

	public static final String PREF_KEY_OLQSTATUS_TAX_INVOICED = "olqstatus.tax_invoiced";

	public static final String PREF_KEY_OLQSTATUS_CANCELED = "olqstatus.canceled";

	public static final String PREF_KEY_OLQSTATUS_ON_HOLD = "olqstatus.on_hold";

	public static final String PREF_KEY_ITEM_GROUPING = "itemgrouping";

	public static final String PREF_KEY_ORDER_SPLITSTRATEGY = "order.split.strategy";

	public static final String PREF_KEY_ORDERLINE_SPLITSTRATEGY = "orderline.split.strategy";

	public static final String PREF_KEY_SOURCING_ORDER_LEVEL = "sourcinglevel.order";

	public static final String PREF_KEY_SOURCING_OLINE_LVL = "sourcinglevel.orderline";

	public static final String PREF_KEY_SOURCING_OLQ_LEVEL = "sourcinglevel.orderlinequantity";

	public static final String PREF_KEY_WF_EXEC_TASK_FULFILLMENT = "workflow.execution.task.fulfillment";

	public static final String PREF_KEY_WF_EXEC_TASK_PAYMENTCAPTURE = "workflow.execution.task.paymentCapture";

	public static final String PREF_KEY_WF_EXEC_TASK_TAXINVOICE = "workflow.execution.task.taxInvoice";

	public static final String PREF_KEY_TITLE_SETTING_ATTRIBUTE_1 = "attribute.attribute1";

	public static final String PREF_KEY_TITLE_SETTING_ATTRIBUTE_2 = "attribute.attribute2";

	public static final String PREF_KEY_TITLE_SETTING_ATTRIBUTE_3 = "attribute.attribute3";

	public static final String PREF_KEY_BIN_SEQUENCING = "bin.sequencing";

	public static final String PREF_KEY_BIN_NUMBER = "bin.number";

	public static final String PREF_KEY_BIN_DIRECTION = "bin.direction";

	public static final String PREF_KEY_RETURN_INCL_SHIPPING_COST = "workflow.return.include.shipping.cost";

	public static final String PREF_KEY_RETURN_SHIPPED_STATUS = "return.shipped.status";

	public static final String PREF_KEY_SHIPPING_WITHIN_DAYS = "ship.in.days";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_DESCRIPTION = "moduleprop.shipmentDescription";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_WIDTH_VALUE = "moduleprop.shipmentWidthValue";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_LENGTH_VALUE = "moduleprop.shipmentLengthValue";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_LENGTH_UNIT_CODE = "moduleprop.shipmentLengthUnitCode";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_HEIGHT_VALUE = "moduleprop.shipmentHeightValue";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_HEIGHT_UNIT_CODE = "moduleprop.shipmentHeightUnitCode";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_GROSS_WEIGHT_VALUE = "moduleprop.shipmentGrossWeightValue";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_GROSS_WEIGHT_UNIT_CODE = "moduleprop.shipmentGrossWeightUnitCode";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_INSURANCE_VALUE_AMOUNT_VALUE = "moduleprop.shipmentInsuranceValueAmountValue";

	public static final String PREF_KEY_SHIPMENT_PROPERTY_INSURANCE_VALUE_CURRENCY_CODE = "moduleprop.shipmentInsuranceValueAmountCurrencyCode";


	private TenantPreferenceConstants()
	{
		// do not instantiate
	}

}
