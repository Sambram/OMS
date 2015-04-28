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
 * Activity {@link org.activiti.engine.delegate.ExecutionListener} triggered when the return workflow starts.
 */
public class JmxReturnStartListener implements ExecutionListener
{
	private static final long serialVersionUID = 1214336964476696514L;

	private WorkflowProcessMonitor processMbean;

	@Override
	public void notify(final DelegateExecution execution)
	{
		processMbean.incrementNumReturnProcesses();
	}

	public void setProcessMbean(final WorkflowProcessMonitor processMbean)
	{
		this.processMbean = processMbean;
	}

}
