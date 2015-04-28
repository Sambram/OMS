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
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Work item worker to reallocate a shipment to a new location.
 */
public class ReallocateShipmentWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReallocateShipmentWorkItemWorker.class);

	private InventoryService inventoryService;
	private ShipmentService shipmentService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String shipmentId = this.getStringVariable(WorkflowConstants.KEY_SHIPMENT_ID, parameters);
		LOGGER.debug("Starting shipment reallocate work item on shipment id: {}", shipmentId);
		final ShipmentData shipment = shipmentService.getShipmentById(Long.valueOf(shipmentId));

		final String locationId = this.getStringVariable(WorkflowConstants.KEY_LOCATION_ID, parameters);
		final StockroomLocationData location = this.inventoryService.getLocationByLocationId(locationId);

		this.shipmentService.reallocateShipment(shipment, location);
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
