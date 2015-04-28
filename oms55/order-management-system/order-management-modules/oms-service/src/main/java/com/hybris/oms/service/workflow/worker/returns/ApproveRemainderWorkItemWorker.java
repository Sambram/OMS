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

import static com.hybris.oms.service.workflow.CoreWorkflowConstants.KEY_OUTCOME_NAME;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_RETURN_ID;

import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Worker to approve all non-rejected items in a return.
 */
public class ApproveRemainderWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(ApproveRemainderWorkItemWorker.class);

	private ReturnService returnService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String returnId = this.getStringVariable(KEY_RETURN_ID, parameters);
		LOG.debug("Starting remainder approval return id: {}", returnId);

		final ReturnData theReturn = returnService.findReturnById(Long.valueOf(returnId));
		returnService.acceptAllNonRejectedItems(theReturn);

		parameters.put(KEY_OUTCOME_NAME, Boolean.TRUE);
		LOG.debug("Done remainder approval for return id: {}.", returnId);
	}

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

	protected ReturnService getReturnService()
	{
		return returnService;
	}

}
