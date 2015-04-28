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
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.returns.strategy.RefundCalculationStrategy;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class CalculateRefundWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(CalculateRefundWorkItemWorker.class);
	private ReturnService returnService;
	private RefundCalculationStrategy refundCalculationStrategy;


	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String returnId = this.getStringVariable(WorkflowConstants.KEY_RETURN_ID, parameters);
		final String previousCalculatedAmount = this
				.getStringVariable(WorkflowConstants.KEY_PREVIOUS_CALCULATED_AMOUNT, parameters);

		LOG.debug("Starting Calculate the Amount to be refunded for return id: {}", returnId);

		final boolean success = true;

		final ReturnData aReturn = returnService.findReturnById(Long.valueOf(returnId));

		final AmountVT refundAmount = refundCalculationStrategy.calculateRefundAmount(aReturn);
		aReturn.setCalculatedRefundAmount(refundAmount);

		if (previousCalculatedAmount != null && !previousCalculatedAmount.equalsIgnoreCase(refundAmount.getValue().toString()))
		{
			aReturn.setCustomRefundAmount(refundAmount);
		}

		if (previousCalculatedAmount == null)
		{
			parameters.put(WorkflowConstants.KEY_PREVIOUS_CALCULATED_AMOUNT, refundAmount.getValue().toString());
			aReturn.setCustomRefundAmount(refundAmount);
		}
		parameters.put(CoreWorkflowConstants.KEY_OUTCOME_NAME, success);
		LOG.debug("Done Calculate the Amount to be refunded for return id: {}. Outcome: {}", returnId, Boolean.toString(success));
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

	@Required
	public void setRefundCalculationStrategy(final RefundCalculationStrategy refundCalculationStrategy)
	{
		this.refundCalculationStrategy = refundCalculationStrategy;
	}

	protected RefundCalculationStrategy getRefundCalculationStrategy()
	{
		return refundCalculationStrategy;
	}
}
