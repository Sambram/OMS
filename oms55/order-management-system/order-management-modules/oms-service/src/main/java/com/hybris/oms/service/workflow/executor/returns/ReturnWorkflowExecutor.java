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
package com.hybris.oms.service.workflow.executor.returns;

import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_APPROVAL;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ONLINE_RETURN;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_RETURN_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_QUEUED;

import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.returns.strategy.ReturnApprovalStrategy;
import com.hybris.oms.service.workflow.executor.AbstractWorkflowExecutor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Executes a Return BORiS workflow.
 */
public class ReturnWorkflowExecutor extends AbstractWorkflowExecutor<ReturnData>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnWorkflowExecutor.class);

	private ReturnApprovalStrategy returnApprovalStrategy;

	@Override
	protected Map<String, Object> getWorkflowParameters(final ReturnData returnData)
	{
		final Map<String, Object> workflowParameters = new HashMap<>();
		workflowParameters.put(KEY_ORDER_ID, returnData.getOrder().getOrderId());
		workflowParameters.put(KEY_RETURN_ID, returnData.getReturnId());
		workflowParameters.put(KEY_ONLINE_RETURN, returnData.getReturnShipment() != null);
		populateTenantPreferences(workflowParameters);
		final boolean requiresApproval = returnApprovalStrategy.requiresApproval(returnData);
		workflowParameters.put(KEY_APPROVAL, requiresApproval);
		return workflowParameters;
	}

	@Override
	protected void beforeWorkflowStart(final ReturnData returnData)
	{
		LOGGER.debug("Starting workflow for return {}.", returnData.getReturnId());
		returnData.setState(STATE_QUEUED);
	}

	public ReturnApprovalStrategy getReturnApprovalStrategy()
	{
		return returnApprovalStrategy;
	}

	@Required
	public void setReturnApprovalStrategy(final ReturnApprovalStrategy returnApprovalStrategy)
	{
		this.returnApprovalStrategy = returnApprovalStrategy;
	}

}
