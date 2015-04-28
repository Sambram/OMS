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
package com.hybris.oms.service.workflow.listener;

import static com.hybris.oms.service.workflow.CoreWorkflowConstants.KEY_TENANT_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_ORDER_ID;
import static com.hybris.oms.service.workflow.WorkflowConstants.WAIT_FOR_SHIPMENT_USER_TASK;

import com.hybris.commons.tenant.TenantContextService;
import com.hybris.commons.tenant.TenantContextTemplate;
import com.hybris.oms.domain.exception.InvalidOperationException;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.workflow.UserTaskForm;
import com.hybris.oms.service.workflow.WorkflowException;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;
import com.hybris.oms.service.workflow.executor.order.OrderAction;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Activiti {@link ExecutionListener} triggered when the shipment workflow ends.
 */
public class SignalShipmentEndListener implements ExecutionListener
{
	private static final long serialVersionUID = 3445470556011750583L;
	private static final Logger LOG = LoggerFactory.getLogger(SignalShipmentEndListener.class);

	private WorkflowExecutor<OrderData> orderWorkflowExecutor;
	private OrderService orderService;

	private TenantContextService tenantContextService;

	@Override
	public void notify(final DelegateExecution execution)
	{
		final String tenantId = (String) execution.getVariable(KEY_TENANT_ID);
		final String orderId = (String) execution.getVariable(KEY_ORDER_ID);

		this.tenantContextService.executeWithTenant(new TenantContextTemplate<Void>(tenantId)
		{
			@Override
			public Void execute()
			{
				final OrderData order = orderService.getOrderByOrderId(orderId);
				final UserTaskForm form = new UserTaskForm();
				form.putAction(OrderAction.CONTINUE.name());
				form.addTaskDefinitionKey(WAIT_FOR_SHIPMENT_USER_TASK);

				try
				{
					orderWorkflowExecutor.completeUserTask(order, form);
				}
				catch (final WorkflowException | InvalidOperationException e)
				{
					LOG.warn("Unable to signal order workflow for orderId [{}]. " + e.getMessage(), orderId);
				}
				return null;
			}
		});
	}

	@Required
	public void setOrderWorkflowExecutor(final WorkflowExecutor<OrderData> orderWorkflowExecutor)
	{
		this.orderWorkflowExecutor = orderWorkflowExecutor;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}

	@Required
	public void setTenantContextService(final TenantContextService tenantContextService)
	{
		this.tenantContextService = tenantContextService;
	}
}
