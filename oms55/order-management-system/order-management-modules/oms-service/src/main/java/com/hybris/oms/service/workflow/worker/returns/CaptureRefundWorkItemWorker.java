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

import com.hybris.oms.domain.exception.HybrisSystemException;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class CaptureRefundWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(CaptureRefundWorkItemWorker.class);
	private CisService cisService;
	private ReturnService returnService;

	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String returnId = this.getStringVariable(WorkflowConstants.KEY_RETURN_ID, parameters);

		LOG.debug("Starting Capture the refund Amount of return id: {}", returnId);

		boolean success = true;

		try
		{
			final ReturnData aReturn = this.returnService.findReturnById(Long.valueOf(returnId));

			cisService.refundPayment(aReturn);
		}
		catch (final HybrisSystemException e)
		{
			LOG.error("Refund payment failed !", e);
			success = false;
		}

		parameters.put(CoreWorkflowConstants.KEY_OUTCOME_NAME, success);

		LOG.debug("Done Capture refund Amount of return id: {}. Outcome: {}", returnId, Boolean.toString(success));
	}

	@Required
	public void setCisService(final CisService cisService)
	{
		this.cisService = cisService;
	}

	public CisService getCisService()
	{
		return cisService;
	}

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}

	public ReturnService getReturnService()
	{
		return returnService;
	}
}
