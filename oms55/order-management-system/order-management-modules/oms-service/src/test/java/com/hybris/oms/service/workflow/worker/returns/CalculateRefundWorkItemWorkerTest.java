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

import static com.hybris.oms.service.workflow.WorkflowConstants.KEY_PREVIOUS_CALCULATED_AMOUNT;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.returns.strategy.RefundCalculationStrategy;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;



// TODO use Pojos to improve the test
public class CalculateRefundWorkItemWorkerTest extends ReturnWorkerBaseUnitTest
{
	@InjectMocks
	CalculateRefundWorkItemWorker calculateRefundWorker = new CalculateRefundWorkItemWorker();

	@Spy
	private final RefundCalculationStrategy refundCalculationStrategy = new RefundCalculationStrategy()
	{
		@Override
		public AmountVT calculateRefundAmount(final ReturnData theReturn) throws EntityNotFoundException
		{
			return new AmountVT("", 2d);
		}
	};


	@Test
	public void calculateTotalAmountExecutedWithSuccess()
	{
		// WHEN:
		calculateRefundWorker.executeInternal(workflowParameters);

		// THEN:
		verify(refundCalculationStrategy, times(1)).calculateRefundAmount(returnData);

		assertThat(getWorkflowResult()).isEqualTo(SUCCESS);
	}



	@Test
	public void calculateWithNoPreviouslyCalculatedAmount()
	{
		// WHEN:
		workflowParameters.put(KEY_PREVIOUS_CALCULATED_AMOUNT, null);
		when(returnData.getCustomRefundAmount()).thenReturn(new AmountVT("", 5d));

		calculateRefundWorker.executeInternal(workflowParameters);

		// THEN:
		verify(refundCalculationStrategy, times(1)).calculateRefundAmount(returnData);
		org.junit.Assert.assertTrue(workflowParameters.containsKey(KEY_PREVIOUS_CALCULATED_AMOUNT));
		org.junit.Assert.assertEquals("2.0", workflowParameters.get(KEY_PREVIOUS_CALCULATED_AMOUNT));

		assertThat(getWorkflowResult()).isEqualTo(SUCCESS);
	}



	@Test
	public void calculateWithPreviouslyCalculatedAmount()
	{
		// WHEN:
		workflowParameters.put(KEY_PREVIOUS_CALCULATED_AMOUNT, 3d);

		calculateRefundWorker.executeInternal(workflowParameters);

		// THEN:
		verify(refundCalculationStrategy, times(1)).calculateRefundAmount(returnData);
		org.junit.Assert.assertTrue(workflowParameters.containsKey(KEY_PREVIOUS_CALCULATED_AMOUNT));
		org.junit.Assert.assertTrue(returnData.getCustomRefundAmount() == returnData.getCalculatedRefundAmount());

		assertThat(getWorkflowResult()).isEqualTo(SUCCESS);
	}


}
