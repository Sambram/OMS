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

import static org.junit.Assert.assertTrue;

import com.hybris.oms.service.managedobjects.returns.*;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class ReturnReviewWorkItemWorkerUnitTest
{
	final String COMPLETE = "COMPLETE";
	final String REJECTED = "REJECTED";
	final String INCOMPLETE = "INCOMPLETE";

	@Test
	public void simpleCaseToIncompleteReturnReviewWorker()
	{
		ReturnOrderLineDataPojo completeReturnOrderLine = new ReturnOrderLineDataPojo();
		ReturnOrderLineDataPojo incompleteReturnOrderLine = new ReturnOrderLineDataPojo();

		ReturnLineRejectionDataPojo completeReturnLineRejection = new ReturnLineRejectionDataPojo();
		ReturnLineRejectionDataPojo incompleteReturnLineRejection = new ReturnLineRejectionDataPojo();

		List<ReturnLineRejectionData> completeReturnLineRejectionDataList = new ArrayList<>();
		completeReturnLineRejectionDataList.add(completeReturnLineRejection);

		List<ReturnLineRejectionData> incompleteReturnLineRejectionDataList = new ArrayList<>();
		incompleteReturnLineRejectionDataList.add(incompleteReturnLineRejection);

		List<ReturnOrderLineData> returnOrderLineDataList = new ArrayList<>();
		returnOrderLineDataList.add(completeReturnOrderLine);
		returnOrderLineDataList.add(incompleteReturnOrderLine);


		completeReturnLineRejection.setRejectionId(1);
		completeReturnLineRejection.setQuantity(5);
		completeReturnLineRejection.setMyReturnOrderLine(completeReturnOrderLine);

		incompleteReturnLineRejection.setRejectionId(2);
		incompleteReturnLineRejection.setQuantity(2);
		incompleteReturnLineRejection.setMyReturnOrderLine(incompleteReturnOrderLine);


		completeReturnOrderLine.setQuantity(new QuantityVT(null, 5));
		completeReturnOrderLine.setReturnLineRejections(completeReturnLineRejectionDataList);

		incompleteReturnOrderLine.setQuantity(new QuantityVT(null, 3));
		incompleteReturnOrderLine.setReturnLineRejections(incompleteReturnLineRejectionDataList);


		ReturnDataPojo returnDataPojo = new ReturnDataPojo();
		returnDataPojo.setReturnOrderLines(returnOrderLineDataList);

		ReturnReviewWorkItemWorker worker = new ReturnReviewWorkItemWorker();
		String nextStep = worker.getWorkflowNextStep(returnDataPojo);

		assertTrue(INCOMPLETE.equals(nextStep));
	}

	@Test
	public void returnOrderLineWithoutRejectionsShouldCompleteReturnReviewWorker()
	{
		ReturnOrderLineDataPojo firstReturnOrderLine = new ReturnOrderLineDataPojo();
		ReturnOrderLineDataPojo secondReturnOrderLine = new ReturnOrderLineDataPojo();

		ReturnLineRejectionDataPojo secondReturnLineRejection = new ReturnLineRejectionDataPojo();

		List<ReturnLineRejectionData> secondReturnLineRejectionDataList = new ArrayList<>();
		secondReturnLineRejectionDataList.add(secondReturnLineRejection);

		List<ReturnOrderLineData> returnOrderLineDataList = new ArrayList<>();
		returnOrderLineDataList.add(firstReturnOrderLine);
		returnOrderLineDataList.add(secondReturnOrderLine);

		secondReturnLineRejection.setQuantity(0);
		secondReturnLineRejection.setMyReturnOrderLine(secondReturnOrderLine);


		firstReturnOrderLine.setQuantity(new QuantityVT(null, 0));

		secondReturnOrderLine.setQuantity(new QuantityVT(null, 3));
		secondReturnOrderLine.setReturnLineRejections(secondReturnLineRejectionDataList);


		ReturnDataPojo returnDataPojo = new ReturnDataPojo();
		returnDataPojo.setReturnOrderLines(returnOrderLineDataList);

		ReturnReviewWorkItemWorker worker = new ReturnReviewWorkItemWorker();
		String nextStep = worker.getWorkflowNextStep(returnDataPojo);

		assertTrue(COMPLETE.equals(nextStep));
	}

	@Test
	public void simpleCaseToCompleteReturnReviewWorker()
	{
		ReturnOrderLineDataPojo firstReturnOrderLine = new ReturnOrderLineDataPojo();
		ReturnOrderLineDataPojo secondReturnOrderLine = new ReturnOrderLineDataPojo();

		ReturnLineRejectionDataPojo firstReturnLineRejection = new ReturnLineRejectionDataPojo();
		ReturnLineRejectionDataPojo secondReturnLineRejection = new ReturnLineRejectionDataPojo();

		List<ReturnLineRejectionData> firstReturnLineRejectionDataList = new ArrayList<>();
		firstReturnLineRejectionDataList.add(firstReturnLineRejection);

		List<ReturnLineRejectionData> secondReturnLineRejectionDataList = new ArrayList<>();
		secondReturnLineRejectionDataList.add(secondReturnLineRejection);

		List<ReturnOrderLineData> returnOrderLineDataList = new ArrayList<>();
		returnOrderLineDataList.add(firstReturnOrderLine);
		returnOrderLineDataList.add(secondReturnOrderLine);

		firstReturnLineRejection.setQuantity(0);
		firstReturnLineRejection.setMyReturnOrderLine(firstReturnOrderLine);

		secondReturnLineRejection.setQuantity(0);
		secondReturnLineRejection.setMyReturnOrderLine(secondReturnOrderLine);


		firstReturnOrderLine.setQuantity(new QuantityVT(null, 5));
		firstReturnOrderLine.setReturnLineRejections(firstReturnLineRejectionDataList);

		secondReturnOrderLine.setQuantity(new QuantityVT(null, 3));
		secondReturnOrderLine.setReturnLineRejections(secondReturnLineRejectionDataList);


		ReturnDataPojo returnDataPojo = new ReturnDataPojo();
		returnDataPojo.setReturnOrderLines(returnOrderLineDataList);

		ReturnReviewWorkItemWorker worker = new ReturnReviewWorkItemWorker();
		String nextStep = worker.getWorkflowNextStep(returnDataPojo);

		assertTrue(COMPLETE.equals(nextStep));
	}

	/*
	 * If we have more than one rejection line but quantity of the last one is zero,
	 * then it's considered completed.
	 */
	@Test
	public void complexCaseToCompleteReturnReviewWorker()
	{
		ReturnOrderLineDataPojo completeReturnOrderLine = new ReturnOrderLineDataPojo();

		ReturnLineRejectionDataPojo incompleteReturnLineRejection = new ReturnLineRejectionDataPojo();
		ReturnLineRejectionDataPojo completeReturnLineRejection = new ReturnLineRejectionDataPojo();

		List<ReturnLineRejectionData> completeReturnLineRejectionDataList = new ArrayList<>();
		completeReturnLineRejectionDataList.add(incompleteReturnLineRejection);
		completeReturnLineRejectionDataList.add(completeReturnLineRejection);

		List<ReturnOrderLineData> returnOrderLineDataList = new ArrayList<>();
		returnOrderLineDataList.add(completeReturnOrderLine);


		incompleteReturnLineRejection.setQuantity(3);
		incompleteReturnLineRejection.setMyReturnOrderLine(completeReturnOrderLine);

		completeReturnLineRejection.setQuantity(0);
		completeReturnLineRejection.setMyReturnOrderLine(completeReturnOrderLine);

		completeReturnOrderLine.setQuantity(new QuantityVT(null, 5));
		completeReturnOrderLine.setReturnLineRejections(completeReturnLineRejectionDataList);

		ReturnDataPojo returnDataPojo = new ReturnDataPojo();
		returnDataPojo.setReturnOrderLines(returnOrderLineDataList);

		ReturnReviewWorkItemWorker worker = new ReturnReviewWorkItemWorker();
		String nextStep = worker.getWorkflowNextStep(returnDataPojo);

		assertTrue(COMPLETE.equals(nextStep));
	}

	/*
    * If we have more than one rejection line but quantity of the last one is zero,
    * then it's considered completed (tested with unsorted list)
    */
	@Test
	public void complexCaseToCompleteReturnReviewWorkerWithUnsortedList()
	{
		ReturnOrderLineDataPojo completeReturnOrderLine = new ReturnOrderLineDataPojo();

		ReturnLineRejectionDataPojo incompleteReturnLineRejection = new ReturnLineRejectionDataPojo();
		ReturnLineRejectionDataPojo completeReturnLineRejection = new ReturnLineRejectionDataPojo();

		List<ReturnLineRejectionData> completeReturnLineRejectionDataList = new ArrayList<>();
		completeReturnLineRejectionDataList.add(completeReturnLineRejection);
		completeReturnLineRejectionDataList.add(incompleteReturnLineRejection);

		List<ReturnOrderLineData> returnOrderLineDataList = new ArrayList<>();
		returnOrderLineDataList.add(completeReturnOrderLine);

		incompleteReturnLineRejection.setRejectionId(1);
		incompleteReturnLineRejection.setQuantity(3);
		incompleteReturnLineRejection.setMyReturnOrderLine(completeReturnOrderLine);

		completeReturnLineRejection.setRejectionId(2);
		completeReturnLineRejection.setQuantity(0);
		completeReturnLineRejection.setMyReturnOrderLine(completeReturnOrderLine);

		completeReturnOrderLine.setQuantity(new QuantityVT(null, 5));
		completeReturnOrderLine.setReturnLineRejections(completeReturnLineRejectionDataList);

		ReturnDataPojo returnDataPojo = new ReturnDataPojo();
		returnDataPojo.setReturnOrderLines(returnOrderLineDataList);

		ReturnReviewWorkItemWorker worker = new ReturnReviewWorkItemWorker();
		String nextStep = worker.getWorkflowNextStep(returnDataPojo);

		assertTrue(COMPLETE.equals(nextStep));
	}

	@Test
	public void simpleCaseToRejectedReturnReviewWorker()
	{
		ReturnOrderLineDataPojo firstReturnOrderLine = new ReturnOrderLineDataPojo();
		ReturnOrderLineDataPojo secondReturnOrderLine = new ReturnOrderLineDataPojo();

		ReturnLineRejectionDataPojo firstReturnLineRejection = new ReturnLineRejectionDataPojo();
		ReturnLineRejectionDataPojo secondReturnLineRejection = new ReturnLineRejectionDataPojo();

		List<ReturnLineRejectionData> firstReturnLineRejectionDataList = new ArrayList<>();
		firstReturnLineRejectionDataList.add(firstReturnLineRejection);

		List<ReturnLineRejectionData> secondReturnLineRejectionDataList = new ArrayList<>();
		secondReturnLineRejectionDataList.add(secondReturnLineRejection);

		List<ReturnOrderLineData> returnOrderLineDataList = new ArrayList<>();
		returnOrderLineDataList.add(firstReturnOrderLine);
		returnOrderLineDataList.add(secondReturnOrderLine);


		firstReturnLineRejection.setRejectionId(1);
		firstReturnLineRejection.setQuantity(5);
		firstReturnLineRejection.setMyReturnOrderLine(firstReturnOrderLine);

		secondReturnLineRejection.setRejectionId(2);
		secondReturnLineRejection.setQuantity(3);
		secondReturnLineRejection.setMyReturnOrderLine(secondReturnOrderLine);


		firstReturnOrderLine.setQuantity(new QuantityVT(null, 5));
		firstReturnOrderLine.setReturnLineRejections(firstReturnLineRejectionDataList);

		secondReturnOrderLine.setQuantity(new QuantityVT(null, 3));
		secondReturnOrderLine.setReturnLineRejections(secondReturnLineRejectionDataList);


		ReturnDataPojo returnDataPojo = new ReturnDataPojo();
		returnDataPojo.setReturnOrderLines(returnOrderLineDataList);

		ReturnReviewWorkItemWorker worker = new ReturnReviewWorkItemWorker();
		String nextStep = worker.getWorkflowNextStep(returnDataPojo);

		assertTrue(REJECTED.equals(nextStep));
	}

	/*
	 * If we have more than one return line rejecting to the same return order line,
	 * but the sum of these quantity (from return line rejection) is the same of
	 * the quantity of return order line, it's considered all rejected.
	 */
	@Test
	public void complexCaseToRejectedReturnReviewWorker()
	{
		ReturnOrderLineDataPojo firstReturnOrderLine = new ReturnOrderLineDataPojo();
		ReturnOrderLineDataPojo secondReturnOrderLine = new ReturnOrderLineDataPojo();

		ReturnLineRejectionDataPojo first01ReturnLineRejection = new ReturnLineRejectionDataPojo();
		ReturnLineRejectionDataPojo first02ReturnLineRejection = new ReturnLineRejectionDataPojo();
		ReturnLineRejectionDataPojo secondReturnLineRejection = new ReturnLineRejectionDataPojo();

		List<ReturnLineRejectionData> firstReturnLineRejectionDataList = new ArrayList<>();
		firstReturnLineRejectionDataList.add(first01ReturnLineRejection);
		firstReturnLineRejectionDataList.add(first02ReturnLineRejection);

		List<ReturnLineRejectionData> secondReturnLineRejectionDataList = new ArrayList<>();
		secondReturnLineRejectionDataList.add(secondReturnLineRejection);

		List<ReturnOrderLineData> returnOrderLineDataList = new ArrayList<>();
		returnOrderLineDataList.add(firstReturnOrderLine);
		returnOrderLineDataList.add(secondReturnOrderLine);


		first01ReturnLineRejection.setRejectionId(1);
		first01ReturnLineRejection.setQuantity(3);
		first01ReturnLineRejection.setMyReturnOrderLine(firstReturnOrderLine);

		first02ReturnLineRejection.setRejectionId(2);
		first02ReturnLineRejection.setQuantity(2);
		first02ReturnLineRejection.setMyReturnOrderLine(firstReturnOrderLine);

		secondReturnLineRejection.setRejectionId(3);
		secondReturnLineRejection.setQuantity(3);
		secondReturnLineRejection.setMyReturnOrderLine(secondReturnOrderLine);


		firstReturnOrderLine.setQuantity(new QuantityVT(null, 5));
		firstReturnOrderLine.setReturnLineRejections(firstReturnLineRejectionDataList);

		secondReturnOrderLine.setQuantity(new QuantityVT(null, 3));
		secondReturnOrderLine.setReturnLineRejections(secondReturnLineRejectionDataList);


		ReturnDataPojo returnDataPojo = new ReturnDataPojo();
		returnDataPojo.setReturnOrderLines(returnOrderLineDataList);

		ReturnReviewWorkItemWorker worker = new ReturnReviewWorkItemWorker();
		String nextStep = worker.getWorkflowNextStep(returnDataPojo);

		assertTrue(REJECTED.equals(nextStep));
	}

}
