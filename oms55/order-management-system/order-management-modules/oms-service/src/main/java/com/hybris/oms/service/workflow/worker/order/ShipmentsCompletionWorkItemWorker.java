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
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Shipments completion Worker will check if all shipment workflows are completed for an order.
 */
public class ShipmentsCompletionWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(ShipmentsCompletionWorkItemWorker.class);
	private static final String REFULFILL = "refulfill";

	private OrderService orderService;
	private ShipmentService shipmentService;
	private WorkflowExecutor<ShipmentData> shipmentWorkflowExecutor;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String orderId = this.getStringVariable(KEY_ORDER_ID, parameters);

		LOG.debug("Starting validation of order completion for order id: {}", orderId);
		boolean shipmentsDone = true;

		final OrderData order = orderService.getOrderByOrderId(orderId);
		final List<ShipmentData> shipments = shipmentService.findShipmentsByOrder(order);

		final List<OrderLineData> orderLines = order.getOrderLines();
		boolean hasMoreUnassignedQuantities = false;
		for (final OrderLineData orderLine : orderLines)
		{
			final int orderLineQuantityUnassigned = orderLine.getQuantityUnassignedValue();
			if (orderLineQuantityUnassigned > 0)
			{
				hasMoreUnassignedQuantities = true;
				break;
			}
		}

		for (final ShipmentData shipment : shipments)
		{
			if (shipmentWorkflowExecutor.isWorkflowInProgress(shipment))
			{
				shipmentsDone = false;
				LOG.debug("Workflow for shipment id [{}] is still in progress", Long.toString(shipment.getShipmentId()));
				break;
			}
		}

		parameters.put(KEY_OUTCOME_NAME, shipmentsDone);
		parameters.put(REFULFILL, hasMoreUnassignedQuantities);
		LOG.debug("Done validation of order completion for order id: {}. shipmentsDone : {}", orderId,
				Boolean.toString(shipmentsDone));
	}

	protected OrderService getOrderService()
	{
		return orderService;
	}

	protected ShipmentService getShipmentService()
	{
		return shipmentService;
	}

	protected WorkflowExecutor<ShipmentData> getShipmentWorkflowExecutor()
	{
		return shipmentWorkflowExecutor;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	@Required
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
	}

	@Required
	public void setShipmentWorkflowExecutor(final WorkflowExecutor<ShipmentData> shipmentWorkflowExecutor)
	{
		this.shipmentWorkflowExecutor = shipmentWorkflowExecutor;
	}
}
