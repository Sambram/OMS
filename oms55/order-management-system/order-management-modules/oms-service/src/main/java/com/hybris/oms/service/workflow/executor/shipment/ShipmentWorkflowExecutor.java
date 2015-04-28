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

import static com.hybris.oms.service.workflow.CoreWorkflowConstants.KEY_ACTION_QUEUE;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_SHIPMENT_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_QUEUED;

import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.executor.AbstractWorkflowExecutor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Manages execution of an shipment workflow.
 */
public class ShipmentWorkflowExecutor extends AbstractWorkflowExecutor<ShipmentData>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipmentWorkflowExecutor.class);
	private ShipmentService shipmentService;

	@Override
	protected Map<String, Object> getWorkflowParameters(final ShipmentData shipment)
	{
		final Map<String, Object> workflowParameters = new HashMap<>();
		workflowParameters.put(KEY_SHIPMENT_ID, Long.toString(shipment.getShipmentId()));
		workflowParameters.put(KEY_ORDER_ID, shipment.getOrderFk().getOrderId());
		workflowParameters.put(KEY_ACTION_QUEUE, null);
		populateTenantPreferences(workflowParameters);
		return workflowParameters;
	}

	@Override
	protected void beforeWorkflowStart(final ShipmentData shipment)
	{
		LOGGER.debug("Starting shipment workflow for shipment {}.", shipment.getShipmentId());
		shipment.setState(STATE_QUEUED);
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

}
