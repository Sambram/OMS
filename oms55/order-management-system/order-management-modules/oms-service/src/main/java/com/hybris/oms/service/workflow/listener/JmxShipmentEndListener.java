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
package com.hybris.oms.service.workflow.listener;

import com.hybris.oms.service.workflow.jmx.WorkflowProcessMonitor;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;


/**
 * Activiti {@link ExecutionListener} triggered when the shipment workflow ends.
 */
public class JmxShipmentEndListener implements ExecutionListener
{
	private static final long serialVersionUID = 8612473059737898230L;

	private WorkflowProcessMonitor processMbean;

	@Override
	public void notify(final DelegateExecution execution)
	{
		processMbean.decrementRunningShipmentProcesses();
	}

	public void setProcessMbean(final WorkflowProcessMonitor processMbean)
	{
		this.processMbean = processMbean;
	}

}
