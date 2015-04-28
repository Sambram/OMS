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
import com.hybris.oms.service.shipment.ShipmentService;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;



/**
 * Payment capture shipment worker.
 */
public class TaxesWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(TaxesWorkItemWorker.class);

	private ShipmentService shipmentService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String shipmentId = this.getStringVariable(WorkflowConstants.KEY_SHIPMENT_ID, parameters);

		LOG.debug("Starting shipment invoice taxes work item on shipment id: {}", shipmentId);
		final ShipmentData shipment = this.shipmentService.getShipmentById(Long.valueOf(shipmentId));
		this.shipmentService.invoiceTaxes(shipment);
		parameters.put(CoreWorkflowConstants.KEY_OUTCOME_NAME, true);
		LOG.debug("Done shipment invoice taxes work item on shipment id: {}. Outcome: {}", shipmentId, Boolean.TRUE);
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
