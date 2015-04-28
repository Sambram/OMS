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
package com.hybris.oms.service.workflow.worker.order;

import static com.hybris.oms.service.workflow.CoreWorkflowConstants.KEY_OUTCOME_NAME;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;

import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Fulfillment completion will check if order is completely sourced.
 */
public class FulfillmentCompletionWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(FulfillmentCompletionWorkItemWorker.class);

	private OrderService orderService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String orderId = this.getStringVariable(KEY_ORDER_ID, parameters);

		LOG.debug("Starting validation of order sourcing completion for order id: {}", orderId);

		final OrderData order = orderService.getOrderByOrderId(orderId);

		boolean refulfill = false;
		if (!OrderDataStaticUtils.isCompletelySourced(order))
		{
			refulfill = true;
		}

		parameters.put(KEY_OUTCOME_NAME, refulfill);
		LOG.debug("Done validation of order sourcing completion for order id: {}. refulfill : {}", orderId,
				Boolean.toString(refulfill));
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
}
