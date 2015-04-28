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

import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_RETURN_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.returns.strategy.RefundCalculationStrategy;
import com.hybris.oms.service.workflow.CoreWorkflowConstants;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
abstract public class ReturnWorkerBaseUnitTest
{
	@Mock
	ReturnService mockReturnService;

	@Mock
	RefundCalculationStrategy mockDefaultRefundCalculationStrategy;

	@Mock
	CisService mockCisService;

	Map<String, Object> workflowParameters = new HashMap<>();

	final Long returnId = 10L;

	ReturnData returnData;

	public static final boolean FAILURE = false;
	public static final boolean SUCCESS = true;

	@Before
	public void setup()
	{
		returnData = mock(ReturnData.class);
		when(mockReturnService.findReturnById(Long.valueOf(returnId))).thenReturn(returnData);

		workflowParameters.put(KEY_RETURN_ID, returnId);
	}

	boolean getWorkflowResult()
	{
		return (boolean) workflowParameters.get(CoreWorkflowConstants.KEY_OUTCOME_NAME);
	}

}
