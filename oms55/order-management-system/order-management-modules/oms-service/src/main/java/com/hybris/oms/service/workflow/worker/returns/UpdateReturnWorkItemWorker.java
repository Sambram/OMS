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

import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.returns.impl.DefaultReturnService;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class UpdateReturnWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(UpdateReturnWorkItemWorker.class);

	private ReturnService returnService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{

		final String returnId = this.getStringVariable(WorkflowConstants.KEY_RETURN_ID, parameters);

		LOG.debug("Starting Update for return id: {}", returnId);

		LOG.debug("Done Update for return id: {}.", returnId);
	}

	@Required
	public void setReturnService(final DefaultReturnService returnService)
	{
		this.returnService = returnService;
	}

	protected ReturnService getReturnService()
	{
		return returnService;
	}
}
