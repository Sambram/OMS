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
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.sourcing.SourcingService;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Fulfillment Worker will perform order fulfillment in 1 step: Sourcing + Allocation.
 */
public class FulfillmentWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(FulfillmentWorkItemWorker.class);

	private SourcingService sourcingService;
	private ShipmentService shipmentService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String orderId = this.getStringVariable(KEY_ORDER_ID, parameters);
		LOG.debug("Starting fulfillment work item on order id: {}", orderId);

		final OrderData order = this.sourcingService.sourceOrder(orderId);

		// Check if sourcing was complete or not
		boolean complete = false;
		if (OrderDataStaticUtils.isCompletelySourced(order))
		{
			complete = true;
		}

		// Create shipments.
		this.shipmentService.createShipmentsForOrder(order);

		parameters.put(KEY_OUTCOME_NAME, complete);
		LOG.debug("Done fulfillment work item on order id: {}. Complete: {}", orderId, complete);
	}

	protected ShipmentService getShipmentService()
	{
		return shipmentService;
	}

	protected SourcingService getSourcingService()
	{
		return sourcingService;
	}

	@Required
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
	}

	@Required
	public void setSourcingService(final SourcingService sourcingService)
	{
		this.sourcingService = sourcingService;
	}
}
