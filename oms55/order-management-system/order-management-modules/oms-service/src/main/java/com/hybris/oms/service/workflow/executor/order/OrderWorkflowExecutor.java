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

import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_TIMEOUT_RETRY_FULFILLMENT;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_TIMEOUT_WAIT_SHIPMENT;

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.workflow.executor.AbstractWorkflowExecutor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Manages execution of an order workflow.
 */
public class OrderWorkflowExecutor extends AbstractWorkflowExecutor<OrderData>
{

	private OrderService orderService;
	private String waitShipmentTimeout;
	private String fulfillmentRetryTimeout;

	@Override
	protected Map<String, Object> getWorkflowParameters(final OrderData order)
	{
		final Map<String, Object> workflowParameters = new HashMap<>();
		workflowParameters.put(KEY_ORDER_ID, order.getOrderId());
		workflowParameters.put(KEY_TIMEOUT_WAIT_SHIPMENT, waitShipmentTimeout);
		workflowParameters.put(KEY_TIMEOUT_RETRY_FULFILLMENT, fulfillmentRetryTimeout);
		populateTenantPreferences(workflowParameters);
		return workflowParameters;
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	public String getWaitShipmentTimeout()
	{
		return waitShipmentTimeout;
	}

	@Required
	public void setWaitShipmentTimeout(final String waitShipmentTimeout)
	{
		this.waitShipmentTimeout = waitShipmentTimeout;
	}

	public String getFulfillmentRetryTimeout()
	{
		return fulfillmentRetryTimeout;
	}

	@Required
	public void setFulfillmentRetryTimeout(final String fulfillmentRetryTimeout)
	{
		this.fulfillmentRetryTimeout = fulfillmentRetryTimeout;
	}

}
