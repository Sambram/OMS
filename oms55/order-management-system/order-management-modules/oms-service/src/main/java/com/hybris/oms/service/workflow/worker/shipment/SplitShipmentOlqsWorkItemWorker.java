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
package com.hybris.oms.service.workflow.worker.shipment;

import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Work item worker to split a shipment.
 */
public class SplitShipmentOlqsWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DeclineShipmentWorkItemWorker.class);

	private ShipmentService shipmentService;
	private OrderService orderService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String shipmentId = this.getStringVariable(WorkflowConstants.KEY_SHIPMENT_ID, parameters);
		LOGGER.debug("Starting shipment split work item on shipment id: {}", shipmentId);
		final ShipmentData shipment = shipmentService.getShipmentById(Long.valueOf(shipmentId));
		String newShipmentId = null;

		@SuppressWarnings("unchecked")
		final Set<String> data = (Set<String>) super.getData(parameters);
		final List<Long> olqIds = new ArrayList<>();
		for (final String olqId : data)
		{
			olqIds.add(Long.valueOf(olqId));
		}

		final List<OrderLineQuantityData> olqs = orderService.getOrderLineQuantitiesByOlqIds(olqIds);
		final ShipmentData newShipment = shipmentService.splitShipmentByOlqs(shipment, olqs);
		newShipmentId = Long.toString(newShipment.getShipmentId());
		parameters.put(WorkflowConstants.KEY_SPLIT_SHIPMENT_ID, newShipmentId);
		super.clearData(parameters);
	}

	protected ShipmentService getShipmentService()
	{
		return shipmentService;
	}

	@Required
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
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
