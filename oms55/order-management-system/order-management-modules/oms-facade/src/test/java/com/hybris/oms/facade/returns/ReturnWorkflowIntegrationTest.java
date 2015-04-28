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
package com.hybris.oms.facade.returns;

import static com.hybris.oms.service.util.OmsTestUtils.waitForProcesses;
import static com.hybris.oms.service.util.OmsTestUtils.waitForWorkflowTask;
import static org.junit.Assert.assertEquals;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.api.order.OrderFacade;
import com.hybris.oms.api.preference.PreferenceFacade;
import com.hybris.oms.api.returns.ReturnFacade;
import com.hybris.oms.domain.order.Order;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.preference.TenantPreference;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.returns.ReturnReview;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.conversion.impl.returns.ReturnTestUtils;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.returns.ReturnService;
import com.hybris.oms.service.util.OmsTestUtils;
import com.hybris.oms.service.workflow.WorkflowConstants;
import com.hybris.oms.service.workflow.WorkflowTestConstants;
import com.hybris.oms.service.workflow.executor.WorkflowExecutor;

import java.util.Arrays;

import javax.annotation.Resource;

import org.activiti.engine.RuntimeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Test to make sure Return Workflow works as expected.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/oms-facade-workflow-spring-test.xml"})
public class ReturnWorkflowIntegrationTest
{
	private static final String SUCCESS_REVERSE_TAX = "WFE_SUCCESS_TAX_REVERSE";
	private static final String SKU_1 = "1";
	private static final String SKU_2 = "2";
	private static final String UNIT = "unit";
	private static final String TRUE = "True";
	private static final String FALSE = "False";

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private ReturnFacade returnFacade;
	@Autowired
	private ImportService importService;
	@Autowired
	private ReturnService returnService;
	@Resource
	private JdbcPersistenceEngine persistenceEngine;
	@Resource
	private PreferenceFacade preferenceFacade;
	@Resource
	private OrderFacade orderFacade;

	@Autowired
	private WorkflowExecutor<ReturnData> returnWorkflowExecutor;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(new ClassPathResource("/order/test-order-data-import.mcsv"));
		this.importService.loadMcsvResource(new ClassPathResource("/shipment/test-shipment-data-import.mcsv"));
		this.importService.loadMcsvResource(new ClassPathResource("/tenantPreference/test-tenantPreference-data-import.mcsv"));
	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

	@Test
	public void createReturnWorkflow()
	{
		// Create return and run return workflow
		final Return returnDTO = ReturnTestUtils.createBorisReturnDto();

		final Order order = this.orderFacade.getOrderByOrderId(returnDTO.getOrderId());
		final OrderLine orderLine = order.getOrderLines().get(1);

		returnDTO.getReturnOrderLines().get(0).setOrderLine(orderLine);

		this.returnFacade.createReturn(returnDTO);

		waitForWorkflowTask(WorkflowConstants.CONFIRM_INSTORE_REFUND_USER_TASK,
				getReturnWorkflowBusinessKey(returnDTO), runtimeService);

		this.returnFacade.autoRefundReturn(returnDTO.getReturnId());

		waitForProcesses(this.runtimeService);

		final Return returnDB = this.returnFacade.getReturnById(String.valueOf(ReturnTestUtils.RETURN_ID));
		Assert.assertEquals(SUCCESS_REVERSE_TAX, returnDB.getState());
	}

	@Test
	public void calculateReturnOrderLineWorkflowWithNoShippingCost()
	{

		final TenantPreference tenantPreference = this.preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_RETURN_INCL_SHIPPING_COST);
		tenantPreference.setValue(FALSE);
		this.preferenceFacade.updateTenantPreference(tenantPreference);

		// Create return and run return workflow
		final Return returnDTO = ReturnTestUtils.createBorisReturnDto();

		// create 2 lines and assign them to the return
		final ReturnOrderLine returnOrderLine1 = ReturnTestUtils.createReturnOrderLineDto();
		returnOrderLine1.setReturnOrderLineId("2");

		final Order order = orderFacade.getOrderByOrderId(returnDTO.getOrderId());
		final OrderLine orderLine1 = order.getOrderLines().get(0);
		orderLine1.setQuantity(new Quantity(UNIT, 1));
		returnOrderLine1.setOrderLine(orderLine1);

		final ReturnOrderLine returnOrderLine2 = ReturnTestUtils.createReturnOrderLineDto();
		returnOrderLine2.setReturnOrderLineId("3");

		final OrderLine orderLine2 = order.getOrderLines().get(1);
		orderLine2.setQuantity(new Quantity(UNIT, 1));
		returnOrderLine2.setOrderLine(orderLine2);

		returnDTO.setReturnOrderLines((Arrays.asList(returnOrderLine1, returnOrderLine2)));

		// Create return and run return workflow
		this.returnFacade.createReturn(returnDTO);

		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_RETURN_CONFIRM_REFUND_ACTION,
				getReturnWorkflowBusinessKey(returnDTO), runtimeService);

		this.returnFacade.autoRefundReturn(returnDTO.getReturnId());
		waitForProcesses(this.runtimeService);

		final Return returnDB = this.returnFacade.getReturnById(String.valueOf(ReturnTestUtils.RETURN_ID));

		Assert.assertEquals(SUCCESS_REVERSE_TAX, returnDB.getState());
		// 5 (quantity) * ((1.5 (tax) + 15.0 (unit price))+(1.5 (tax) + 15.0 (unit price))) = 165.0
		Assert.assertEquals(Double.valueOf(165), returnDB.getCalculatedTotalRefundAmount().getValue());
	}

	@Test
	public void calculateReturnOrderLineWorkflowWithShippingCost()
	{

		final TenantPreference tenantPreference = this.preferenceFacade
				.getTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_RETURN_INCL_SHIPPING_COST);
		tenantPreference.setValue(TRUE);
		this.preferenceFacade.updateTenantPreference(tenantPreference);


		final Return returnDTO = ReturnTestUtils.createBorisReturnDto();

		// create 2 lines and assign them to the return
		final ReturnOrderLine returnOrderLine1 = ReturnTestUtils.createReturnOrderLineDto();
		returnOrderLine1.setReturnOrderLineId("5");
		returnOrderLine1.getOrderLine().setOrderLineId("23");
		returnOrderLine1.getOrderLine().setSkuId(SKU_1);
		returnOrderLine1.setQuantity(new Quantity(UNIT, 2));
		final ReturnOrderLine returnOrderLine2 = ReturnTestUtils.createReturnOrderLineDto();
		returnOrderLine2.setReturnOrderLineId("6");
		returnOrderLine2.getOrderLine().setOrderLineId("22");
		returnOrderLine2.getOrderLine().setSkuId(SKU_2);
		returnOrderLine2.setQuantity(new Quantity(UNIT, 3));
		returnDTO.setReturnOrderLines((Arrays.asList(returnOrderLine1, returnOrderLine2)));
		returnDTO.setShippingRefunded(true);
		// Create return and run return workflow
		this.returnFacade.createReturn(returnDTO);

		waitForWorkflowTask(WorkflowTestConstants.ACTIVITY_ID_RETURN_CONFIRM_REFUND_ACTION,
				getReturnWorkflowBusinessKey(returnDTO), runtimeService);

		this.returnFacade.autoRefundReturn(returnDTO.getReturnId());
		waitForProcesses(this.runtimeService);

		final Return returnDB = this.returnFacade.getReturnById(String.valueOf(ReturnTestUtils.RETURN_ID));

		Assert.assertEquals(SUCCESS_REVERSE_TAX, returnDB.getState());
		// 2 (quantity) * ((1.5 (tax) + 15.0 (unit price))+ 3 (quantity) *(1.5 (tax) + 15.0 (unit price))) + 5 (shipping)
		// = 87.5
		Assert.assertEquals(Double.valueOf(87.5), returnDB.getCalculatedTotalRefundAmount().getValue());
	}

	@Test
	public void createOnlineReturnWorkflow_AllApproved()
	{
		// Create online return and start return workflow
		Return returnDTO = ReturnTestUtils.createOnlineReturnDto();
		final Order order = orderFacade.getOrderByOrderId(returnDTO.getOrderId());
		final OrderLine orderLine = order.getOrderLines().get(1);
		returnDTO.getReturnOrderLines().get(0).setOrderLine(orderLine);
		final Return aReturn = returnFacade.createReturn(returnDTO);

		// Wait for confirm refund user task (since the calculate service task is asynchronous)
		waitForWorkflowTask(WorkflowConstants.CONFIRM_ONLINE_REFUND_USER_TASK,
				getReturnWorkflowBusinessKey(returnDTO), runtimeService);
		returnFacade.autoRefundReturn(returnDTO.getReturnId());
		returnDTO = returnFacade.getReturnById(returnDTO.getReturnId());

		// Wait for approve return user task
		waitForWorkflowTask(WorkflowConstants.APPROVE_RETURN_USER_TASK, getReturnWorkflowBusinessKey(returnDTO),
				runtimeService);

		// Create return review for approval
		final String returnOrderLineId = aReturn.getReturnOrderLines().get(0).getReturnOrderLineId();
		ReturnReview returnReview = ReturnTestUtils.createReturnReviewDto(returnOrderLineId);
		returnReview.getReturnLineRejections().get(0).setQuantity(0);
		returnReview.getReturnLineRejections().get(1).setQuantity(0);
		returnReview.setReturnId(returnDTO.getReturnId());
		returnFacade.createReturnReview(returnReview);

		// Wait for goods return user task - first time
		waitForWorkflowTask(WorkflowConstants.WAIT_FOR_GOODS_USER_TASK, getReturnWorkflowBusinessKey(returnDTO),
				runtimeService);

		// Create return review for waiting for goods
		returnReview = ReturnTestUtils.createReturnReviewDto(returnOrderLineId);
		returnReview.setReturnId(returnDTO.getReturnId());
		returnReview.getReturnLineRejections().get(0).setQuantity(0);
		returnReview.getReturnLineRejections().get(1).setQuantity(0);
		returnFacade.createReturnReview(returnReview);

		waitForProcesses(runtimeService);

		final Return returnDB = returnFacade.getReturnById(String.valueOf(ReturnTestUtils.RETURN_ID));
		assertEquals(SUCCESS_REVERSE_TAX, returnDB.getState());

		final ReturnOrderLine returnOrderLine = returnDB.getReturnOrderLines().get(0);
		assertEquals(4, returnOrderLine.getReturnLineRejections().size());
	}

	@Test
	public void createOnlineReturnWorkflow_AcceptRemainder()
	{
		// Create online return and start return workflow
		Return returnDTO = ReturnTestUtils.createOnlineReturnDto();
		final Order order = orderFacade.getOrderByOrderId(returnDTO.getOrderId());
		final OrderLine orderLine = order.getOrderLines().get(1);
		returnDTO.getReturnOrderLines().get(0).setOrderLine(orderLine);
		final Return aReturn = returnFacade.createReturn(returnDTO);

		// Wait for confirm refund user task (since the calculate service task is asynchronous)
		waitForWorkflowTask(WorkflowConstants.CONFIRM_ONLINE_REFUND_USER_TASK,
				getReturnWorkflowBusinessKey(returnDTO), runtimeService);
		returnFacade.autoRefundReturn(returnDTO.getReturnId());
		returnDTO = returnFacade.getReturnById(returnDTO.getReturnId());

		// Wait for approve return user task
		waitForWorkflowTask(WorkflowConstants.APPROVE_RETURN_USER_TASK, getReturnWorkflowBusinessKey(returnDTO),
				runtimeService);

		// Create return review for approval
		final String returnOrderLineId = aReturn.getReturnOrderLines().get(0).getReturnOrderLineId();
		final ReturnReview returnReview = ReturnTestUtils.createReturnReviewDto(returnOrderLineId);
		returnReview.setReturnId(returnDTO.getReturnId());
		returnFacade.createReturnReview(returnReview);

		// Wait for goods return user task - first time
		waitForWorkflowTask(WorkflowConstants.WAIT_FOR_GOODS_USER_TASK, getReturnWorkflowBusinessKey(returnDTO),
				runtimeService);

		// Simply accept all remaining items and go to refund
		returnFacade.autoRefundReturn(returnDTO.getReturnId());

		waitForProcesses(runtimeService);

		final Return returnDB = returnFacade.getReturnById(String.valueOf(ReturnTestUtils.RETURN_ID));
		Assert.assertEquals(SUCCESS_REVERSE_TAX, returnDB.getState());

		final ReturnOrderLine returnOrderLine = returnDB.getReturnOrderLines().get(0);
		assertEquals(3, returnOrderLine.getReturnLineRejections().size());
		assertEquals(0, returnOrderLine.getReturnLineRejections().get(2).getQuantity().intValue());
	}

	/**
	 * Get the workflow business key represented by the aReturn id.
	 *
	 * @param aReturn return object
	 * @return business key
	 */
	private String getReturnWorkflowBusinessKey(final Return aReturn)
	{
		return new TransactionTemplate(txManager).execute(new TransactionCallback<String>()
		{
			@Override
			public String doInTransaction(final TransactionStatus status)
			{
				return returnWorkflowExecutor.getBusinessKey(returnService.findReturnById(Long.valueOf(aReturn.getReturnId())));
			}
		});
	}
}
