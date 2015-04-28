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
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.worker.impl.AbstractWorkItemWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Return review worker will create rejections to a specific return.
 * <p>
 * <ul>
 * <li>Set variable <i>processed</i> to "COMPLETE" if all items in the return are approved or if all items are processed
 * in a mix of approved and rejected.</li>
 * <li>Set variable <i>processed</i> to "REJECTED" if all items in the return are rejected.</li>
 * <li>Set variable <i>processed</i> to "INCOMPLETE" if none or only some of the items are processed.</li>
 * </ul>
 * </p>
 */
public class ReturnReviewWorkItemWorker extends AbstractWorkItemWorker
{
	private static final Logger LOG = LoggerFactory.getLogger(ReturnReviewWorkItemWorker.class);
	private static final String COMPLETE = "COMPLETE";
	private static final String REJECTED = "REJECTED";
	private static final String INCOMPLETE = "INCOMPLETE";

	private ReturnService returnService;

	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(final Map<String, Object> parameters)
	{
		final String returnId = this.getStringVariable(WorkflowConstants.KEY_RETURN_ID, parameters);
		LOG.debug("Starting return review return id: {}", returnId);

		final List<ReturnLineRejectionData> returnLineRejectionDataList = (List<ReturnLineRejectionData>) super.getData(parameters);
		linkReviewsToReturn(returnLineRejectionDataList);

		returnService.flush();

		final ReturnData returnData = returnLineRejectionDataList.get(0).getMyReturnOrderLine().getMyReturn();

		parameters.put(WorkflowConstants.KEY_PROCESSED, getWorkflowNextStep(returnData));
		LOG.debug("Done return review for return id: {}.", returnId);
	}

	protected void linkReviewsToReturn(final List<ReturnLineRejectionData> returnLineRejectionDataList)
	{
		if (!CollectionUtils.isEmpty(returnLineRejectionDataList))
		{
			final ReturnData returnData = returnLineRejectionDataList.get(0).getMyReturnOrderLine().getMyReturn();

			for (final ReturnOrderLineData returnOrderLineData : returnData.getReturnOrderLines())
			{
				for (final ReturnLineRejectionData returnLineRejectionData : returnLineRejectionDataList)
				{
					if (returnOrderLineData.getReturnOrderLineId() == returnLineRejectionData.getMyReturnOrderLine()
							.getReturnOrderLineId())
					{
						final List<ReturnLineRejectionData> rejectionDatas = new ArrayList<>();
						rejectionDatas.add(returnLineRejectionData);
						returnOrderLineData.setReturnLineRejections(rejectionDatas);
					}
				}
			}
		}
	}

	protected String getWorkflowNextStep(final ReturnData returnData)
	{
		int statusIndex = 0;
		final String[] rejectionsStatus = new String[returnData.getReturnOrderLines().size()];

		for (final ReturnOrderLineData returnOrderLineData : returnData.getReturnOrderLines())
		{
			if (isStatusComplete(returnOrderLineData))
			{
				rejectionsStatus[statusIndex] = COMPLETE;
			}
			else
			{
				int totalRejected = 0;
				for (final ReturnLineRejectionData returnLineRejectionData : returnOrderLineData.getReturnLineRejections())
				{
					totalRejected += returnLineRejectionData.getQuantity();
				}
				rejectionsStatus[statusIndex] = totalRejected == returnOrderLineData.getQuantity().getValue() ? REJECTED : INCOMPLETE;
			}
			statusIndex += 1;
		}

		return getFinalStatus(rejectionsStatus);
	}

	private boolean isStatusComplete(final ReturnOrderLineData returnOrderLineData)
	{
		if (CollectionUtils.isEmpty(returnOrderLineData.getReturnLineRejections()))
		{
			return true;
		}
		return lastQuantityIsZero(returnOrderLineData.getReturnLineRejections());
	}

	private String getFinalStatus(final String[] rejectionsStatus)
	{
		int totalCompleted = 0;
		int totalRejected = 0;
		for (final String rejectionStatus : rejectionsStatus)
		{
			if (INCOMPLETE.equals(rejectionStatus))
			{
				return INCOMPLETE;
			}
			else if (COMPLETE.equals(rejectionStatus))
			{
				totalCompleted += 1;
				if (rejectionsStatus.length == totalCompleted)
				{
					return COMPLETE;
				}
			}
			else if (REJECTED.equals(rejectionStatus))
			{
				totalRejected += 1;
				if (rejectionsStatus.length == totalRejected)
				{
					return REJECTED;
				}
			}
		}
		return null;
	}

	private boolean lastQuantityIsZero(final List<ReturnLineRejectionData> returnLineRejectionDataList)
	{
		int lastQuantity = 0;
		long lastId = 0;
		for (final ReturnLineRejectionData returnLineRejectionData : returnLineRejectionDataList)
		{
			if (returnLineRejectionData.getRejectionId() > lastId)
			{
				lastId = returnLineRejectionData.getRejectionId();
				lastQuantity = returnLineRejectionData.getQuantity();
			}
		}

		return lastQuantity == 0;
	}

	@Required
	public void setReturnService(final ReturnService returnService)
	{
		this.returnService = returnService;
	}
}
