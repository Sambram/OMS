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

import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DeclineShipmentWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DeclineShipmentWorkItemWorker.class);

	private ShipmentService shipmentService;

    private InventoryService inventoryService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String shipmentId = this.getStringVariable(WorkflowConstants.KEY_SHIPMENT_ID, parameters);

		LOGGER.debug("Starting shipment decline work item on shipment id: {}", shipmentId);

        final ShipmentData shipment = this.shipmentService.getShipmentById(Long.valueOf(shipmentId));

        for (OrderLineData orderLineData : shipment.getOrderFk().getOrderLines())
        {
            inventoryService.banItemLocations(orderLineData.getSkuId(), shipment.getStockroomLocationId());
        }

		shipmentService.declineShipment(shipment);

		parameters.put(CoreWorkflowConstants.KEY_OUTCOME_NAME, true);
		LOGGER.debug("Done shipment decline work item on shipment id: {}. Outcome: {}", shipmentId, Boolean.TRUE);
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

    protected InventoryService getInventoryService()
    {
        return inventoryService;
    }

    @Required
    public void setInventoryService(final InventoryService inventoryService)
    {
        this.inventoryService = inventoryService;
    }

}
