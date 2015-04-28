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

import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class ReturnStateWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(ReturnStateWorkItemWorker.class);

	private ReturnService returnService;

	private String state;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String returnId = this.getStringVariable(WorkflowConstants.KEY_RETURN_ID, parameters);

		LOG.debug("Starting update return state '{}' on return id: {}", state, returnId);
		final ReturnData aReturn = this.returnService.findReturnById(Long.valueOf(returnId));
		aReturn.setState(state);

		LOG.debug("Done update return state '{}' on return id: {}. Outcome: {}", state, returnId, Boolean.TRUE);
	}

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

	@Required
	public void setState(final String state)
	{
		this.state = state;
	}

	protected ReturnService geReturnService()
	{
		return returnService;
	}

	protected String getState()
	{
		return state;
	}

}
