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
package com.hybris.oms.service.workflow.worker.impl;

import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.ActionQueueWorkItemWorker;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;


/**
 *
 */
public class ActionQueueWorkItemWorkerTest
{
	private final ActionQueueWorkItemWorker worker = new ActionQueueWorkItemWorker();
	private final Map<String, Object> params = new HashMap<>();

	@Test
	public void shouldSetNextActionAndPopQueue()
	{
		params.put(CoreWorkflowConstants.KEY_ACTION_QUEUE, "1|2|3|");
		worker.executeInternal(params);
		Assert.assertEquals("1", params.get(CoreWorkflowConstants.KEY_ACTION_NAME));
		Assert.assertEquals("2|3|", params.get(CoreWorkflowConstants.KEY_ACTION_QUEUE));
	}

	@Test
	public void shouldSetNextActionAndEmptyQueue()
	{
		params.put(CoreWorkflowConstants.KEY_ACTION_QUEUE, "1");
		worker.executeInternal(params);
		Assert.assertEquals("1", params.get(CoreWorkflowConstants.KEY_ACTION_NAME));
		Assert.assertNull(params.get(CoreWorkflowConstants.KEY_ACTION_QUEUE));
	}
}
