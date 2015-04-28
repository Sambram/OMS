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

import com.hybris.oms.domain.exception.RemoteRequestException;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CaptureRefundWorkItemWorkerTest extends ReturnWorkerBaseUnitTest
{
	@InjectMocks
	CaptureRefundWorkItemWorker refundWorker = new CaptureRefundWorkItemWorker();

	@Test
	public void refundPaymentExecutedWithSuccess()
	{
		// WHEN:
		refundWorker.executeInternal(workflowParameters);

		// THEN:
		verify(mockCisService, times(1)).refundPayment(returnData);
		assertThat(getWorkflowResult()).isEqualTo(SUCCESS);
	}

	@Test
	public void refundPaymentFails()
	{
		// GIVEN:
		doThrow(new RemoteRequestException("Connection failed !")).when(mockCisService).refundPayment(returnData);

		// WHEN:
		refundWorker.executeInternal(workflowParameters);

		// THEN:
		verify(mockCisService, times(1)).refundPayment(returnData);
		assertThat(getWorkflowResult()).isEqualTo(FAILURE);
	}

}
