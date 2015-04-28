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

import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;


public class AbstractUpdateShipmentStatusWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUpdateShipmentStatusWorkItemWorker.class);

	private OrderService orderService;
	private ShipmentService shipmentService;
	private String statusPreferenceKey;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String shipmentId = this.getStringVariable(WorkflowConstants.KEY_SHIPMENT_ID, parameters);
		LOGGER.debug("Starting update shipment status work item on shipment id: {}", shipmentId);

		final ShipmentData shipment = this.shipmentService.getShipmentById(Long.valueOf(shipmentId));
		this.shipmentService.updateShipmentStatus(shipment, statusPreferenceKey);

		final String orderId = this.getStringVariable(WorkflowConstants.KEY_ORDER_ID, parameters);

		parameters.put(CoreWorkflowConstants.KEY_OUTCOME_NAME, true);
		LOGGER.debug("Done update shipment status work item on shipment id: {}. Outcome: {}", shipmentId, Boolean.TRUE);
	}

	protected ShipmentService getShipmentService()
	{
		return this.shipmentService;
	}

	@Required
	public void setShipmentService(final ShipmentService shipmentService)
	{
		this.shipmentService = shipmentService;
	}

	public String getStatusPreferenceKey()
	{
		return this.statusPreferenceKey;
	}

	@Required
	public void setStatusPreferenceKey(final String statusPreferenceKey)
	{
		this.statusPreferenceKey = statusPreferenceKey;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}
}
