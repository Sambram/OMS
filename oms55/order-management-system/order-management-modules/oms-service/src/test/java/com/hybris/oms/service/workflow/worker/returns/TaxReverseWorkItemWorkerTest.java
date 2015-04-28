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


public class TaxReverseWorkItemWorkerTest extends ReturnWorkerBaseUnitTest
{
	@InjectMocks
	TaxReverseWorkItemWorker taxReverseWorker = new TaxReverseWorkItemWorker();

	@Test
	public void taxReverseExecutedWithSuccess()
	{

		// WHEN:
		taxReverseWorker.executeInternal(workflowParameters);

		// THEN:
		verify(mockCisService, times(1)).reverseTax(returnData);
		assertThat(getWorkflowResult()).isEqualTo(SUCCESS);
	}

	@Test
	public void taxReverseFails()
	{
		// GIVEN:
		doThrow(new RemoteRequestException("Connection failed !")).when(mockCisService).reverseTax(returnData);

		// WHEN:
		taxReverseWorker.executeInternal(workflowParameters);

		// THEN:
		verify(mockCisService, times(1)).reverseTax(returnData);
		assertThat(getWorkflowResult()).isEqualTo(FAILURE);
	}



}
