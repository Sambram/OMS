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
package com.hybris.oms.service.workflow.worker.returns;

import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * a stub step that does nothing, place holder for partners to override and implement their own logic instead
 */
public class ReturnStubWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(ReturnStubWorkItemWorker.class);


	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String returnId = this.getStringVariable(WorkflowConstants.KEY_RETURN_ID, parameters);

		LOG.debug("Starting return stub step on return id: {}", returnId);
	}

}
