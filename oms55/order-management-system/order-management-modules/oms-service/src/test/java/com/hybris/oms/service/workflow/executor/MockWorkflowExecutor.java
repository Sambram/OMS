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
package com.hybris.oms.service.workflow.executor;

import com.hybris.kernel.api.ManagedObject;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.service.workflow.UserTaskForm;
import com.hybris.oms.service.workflow.WorkflowException;

import java.util.HashSet;
import java.util.Set;


/**
 * A stub {@link WorkflowExecutor} which simply does nothing. This will ensure that the workflow is not started.
 */
public class MockWorkflowExecutor<T extends ManagedObject> implements WorkflowExecutor<T>
{
	private static final String BUSINESS_KEY = "TENANT|TYPECODE|ID";

	@Override
	public void execute(final T Object)
	{
		// Do Nothing
	}

	@Override
	public String getBusinessKey(final T object)
	{
		return BUSINESS_KEY;
	}

	@Override
	public boolean hasExistingWorkflow(final T object)
	{
		return false;
	}

	@Override
	public boolean isWorkflowInProgress(final T object)
	{
		return false;
	}

	@Override
	public void signal(final T object, final String signal) throws InvalidOperationException
	{
		// Do Nothing
	}

	@Override
	public void completeUserTask(final T object, final UserTaskForm form) throws InvalidOperationException, WorkflowException
	{
		// Do Nothing
	}

	@Override
	public void completeUserTask(final T object, final UserTaskForm form, final UserTaskData data)
			throws InvalidOperationException, WorkflowException
	{
		// Do Nothing
	}

	@Override
	public Object getParameter(final T object, final String key) throws InvalidOperationException
	{
		return null;
	}

	@Override
	public Set<String> getActions(final T object)
	{
		return new HashSet<String>();
	}

}
