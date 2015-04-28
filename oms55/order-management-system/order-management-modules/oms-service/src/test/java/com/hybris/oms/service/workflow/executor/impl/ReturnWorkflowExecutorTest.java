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
package com.hybris.oms.service.workflow.executor.impl;

import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_QUEUED;

import com.hybris.commons.tenant.TenantContextService;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderDataPojo;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnDataPojo;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.returns.strategy.ReturnApprovalStrategy;
import com.hybris.oms.service.workflow.activiti.ActivitiResourceDeployer;
import com.hybris.oms.service.workflow.executor.returns.ReturnWorkflowExecutor;
import com.hybris.oms.service.workflow.parser.ProcessIdBpmnParser;

import java.util.Collections;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.Resource;


@RunWith(MockitoJUnitRunner.class)
public class ReturnWorkflowExecutorTest
{
	@InjectMocks
	private final ReturnWorkflowExecutor returnWorkflowExecutor = new ReturnWorkflowExecutor();

	@Mock
	private Resource deploymentResource;
	@Mock
	private RuntimeService runtimeService;
	@Mock
	private TaskService taskService;
	@Mock
	private FormService formService;
	@Mock
	private HistoryService historyService;
	@Mock
	private TenantContextService tenantContextService;
	@Mock
	private TenantPreferenceService tenantPreferenceService;
	@Mock
	private ActivitiResourceDeployer resourceDeployer;
	@Mock
	private ProcessIdBpmnParser processIdProcessIdBpmnParser;
	@Mock
	private ReturnApprovalStrategy returnApprovalStrategy;

	@Before
	public void setUp()
	{
		Mockito.when(tenantPreferenceService.findAllTenantPreferences()).thenReturn(Collections.<TenantPreferenceData>emptyList());
	}

	@Test
	public void shouldSetStateToQueued()
	{
		final ReturnData theReturn = createReturn();

		returnWorkflowExecutor.execute(theReturn);
		Assert.assertEquals(STATE_QUEUED, theReturn.getState());
	}

	@Test
	public void shouldCallApprovalStrategy()
	{
		final ReturnData theReturn = createReturn();

		returnWorkflowExecutor.execute(theReturn);
		Mockito.verify(returnApprovalStrategy).requiresApproval(theReturn);
	}

	/**
	 * Create a return with fields required when executing the return workflow
	 *
	 * @return the return
	 */
	private ReturnData createReturn()
	{
		final OrderData order = new OrderDataPojo();
		order.setOrderId("ORDER_ID");

		final ReturnData theReturn = new ReturnDataPojo();
		theReturn.setReturnId(1l);
		theReturn.setOrder(order);
		theReturn.setState(null);

		return theReturn;
	}

}
