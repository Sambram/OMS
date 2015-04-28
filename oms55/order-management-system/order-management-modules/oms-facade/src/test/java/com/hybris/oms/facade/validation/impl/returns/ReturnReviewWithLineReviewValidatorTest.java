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
package com.hybris.oms.facade.validation.impl.returns;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.hybris.oms.domain.returns.ReviewReason;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.domain.returns.ReturnReview;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.returns.ReturnService;


@RunWith(MockitoJUnitRunner.class)
public class ReturnReviewWithLineReviewValidatorTest
{
	ValidationContext context;
	ReturnReviewValidator returnReviewValidator;
	ReturnLineRejectionValidator returnLineRejectionValidator;
	ReturnReview returnReview;
	List<ReturnLineRejection> returnLineRejections;

	@Mock
	ReturnService returnService;

	@Mock
	ReturnData returnData;

	@Mock
	ReturnOrderLineData returnOrderLineData;

	@Before
	public void setup()
	{
		context = new ValidationContext();
		returnReviewValidator = new ReturnReviewValidator();
		returnLineRejectionValidator = new ReturnLineRejectionValidator();
		returnReview = new ReturnReview();
		returnLineRejections = new ArrayList<>();

		returnReviewValidator.setReturnLineRejectionValidator(returnLineRejectionValidator);
		returnReviewValidator.setReturnService(returnService);
	}

	@Test
	public void contextShouldContainFailsWhenReturnIdIsEmpty()
	{
		returnReviewValidator.validateInternal(context, returnReview);

		assertTrue(context.containsFailures());
		assertEquals(1, context.getFailureCount());

		Failure failure = context.getFailures().get(0);
		assertEquals(FieldValidationType.IS_EMPTY, failure.getFailureType());
	}

	@Test
	public void contextShouldContainFailsWhenReturnLineRejectionsAreEmpty()
	{
		returnReview.setReturnId("1");
		returnReviewValidator.validateInternal(context, returnReview);

		assertTrue(context.containsFailures());
		assertEquals(1, context.getFailureCount());

		Failure failure = context.getFailures().get(0);
		assertEquals(FieldValidationType.IS_EMPTY, failure.getFailureType());
	}

	@Test
	public void contextShouldContainFailsWhenQuantityIsNull()
	{
		ReturnLineRejection returnLineRejection = new ReturnLineRejection();
		returnLineRejection.setReturnOrderLineId("1");
		returnLineRejection.setReason(ReviewReason.OTHER);
		returnLineRejection.setResponsible("John");

		returnLineRejections.add(returnLineRejection);
		returnReview.setReturnId("1");
		returnReview.setReturnLineRejections(returnLineRejections);
		returnReviewValidator.validateInternal(context, returnReview);

		assertTrue(context.containsFailures());
		assertEquals(1, context.getFailureCount());

		Failure failure = context.getFailures().get(0);
		assertEquals(FieldValidationType.IS_NULL, failure.getFailureType());
	}

	@Test
	public void contextShouldContainFailsWhenQuantityLessThanZero()
	{
		ReturnLineRejection returnLineRejectionM = new ReturnLineRejection();
		returnLineRejectionM.setQuantity(-1);
		returnLineRejectionM.setReturnOrderLineId("1");
		returnLineRejectionM.setReason(ReviewReason.OTHER);
		returnLineRejectionM.setResponsible("John");

		returnLineRejections.add(returnLineRejectionM);
		returnReview.setReturnId("1");
		returnReview.setReturnLineRejections(returnLineRejections);
		returnReviewValidator.validateInternal(context, returnReview);

		assertTrue(context.containsFailures());
		assertEquals(1, context.getFailureCount());

		Failure failureMinusOne = context.getFailures().get(0);
		assertEquals(FieldValidationType.LESS_THAN, failureMinusOne.getFailureType());
	}

	@Test
	public void contextShouldContainFailsWhenReviewDoesNotHaveReason()
	{
		ReturnLineRejection returnLineRejection = new ReturnLineRejection();
		returnLineRejection.setQuantity(2);
		returnLineRejection.setReturnOrderLineId("1");
		returnLineRejection.setResponsible("John");

		returnLineRejections.add(returnLineRejection);
		returnReview.setReturnId("1");
		returnReview.setReturnLineRejections(returnLineRejections);
		returnReviewValidator.validateInternal(context, returnReview);

		assertTrue(context.containsFailures());
		assertEquals(1, context.getFailureCount());

		Failure failure = context.getFailures().get(0);
		assertEquals(FieldValidationType.IS_EMPTY, failure.getFailureType());
	}

	@Test
	public void contextShouldContainFailsWhenParentChildrenDontMatch()
	{
		ReturnLineRejection firstReturnLineRejection = new ReturnLineRejection();
		firstReturnLineRejection.setReason(ReviewReason.OTHER);
		firstReturnLineRejection.setQuantity(5);
		firstReturnLineRejection.setReturnOrderLineId("1");
		firstReturnLineRejection.setResponsible("John");

		ReturnLineRejection secondReturnLineRejection = new ReturnLineRejection();
		secondReturnLineRejection.setReason(ReviewReason.OTHER);
		secondReturnLineRejection.setQuantity(2);
		secondReturnLineRejection.setReturnOrderLineId("1");
		secondReturnLineRejection.setResponsible("John");

		mockReturnOrderLineDatas(10L,5);

		returnLineRejections.add(firstReturnLineRejection);
		returnLineRejections.add(secondReturnLineRejection);
		returnReview.setReturnId("1");
		returnReview.setReturnLineRejections(returnLineRejections);

		when(returnService.findReturnById(1)).thenReturn(returnData);

		returnReviewValidator.validateInternal(context, returnReview);

		assertTrue(context.containsFailures());
		assertEquals(2, context.getFailureCount());

		Failure failure = context.getFailures().get(0);
		assertEquals(FieldValidationType.INVALID, failure.getFailureType());
	}

	@Test
	public void contextShouldNotContainFails()
	{
		ReturnLineRejection firstReturnLineRejection = new ReturnLineRejection();
		firstReturnLineRejection.setReason(ReviewReason.OTHER);
		firstReturnLineRejection.setQuantity(5);
		firstReturnLineRejection.setReturnOrderLineId("1");
		firstReturnLineRejection.setResponsible("John");

		ReturnLineRejection secondReturnLineRejection = new ReturnLineRejection();
		secondReturnLineRejection.setReason(ReviewReason.OTHER);
		secondReturnLineRejection.setQuantity(2);
		secondReturnLineRejection.setReturnOrderLineId("1");
		secondReturnLineRejection.setResponsible("John");

		returnLineRejections.add(firstReturnLineRejection);
		returnLineRejections.add(secondReturnLineRejection);
		returnReview.setReturnId("1");
		returnReview.setReturnLineRejections(returnLineRejections);

		mockReturnOrderLineDatas(1L,5);
		when(returnService.findReturnById(1)).thenReturn(returnData);
		returnReviewValidator.validateInternal(context, returnReview);

		assertFalse(context.containsFailures());
	}

	@Test
	public void contextShouldContainFailsWhenReturnLineRejectionsQuantityIsGreaterThanRequested()
	{
		ReturnLineRejection firstReturnLineRejection = new ReturnLineRejection();
		firstReturnLineRejection.setReason(ReviewReason.DAMAGED);
		// 6 is rejected Quantity, 5 is the requested quantity
		firstReturnLineRejection.setQuantity(6);
		firstReturnLineRejection.setReturnOrderLineId("1");
		firstReturnLineRejection.setResponsible("John");

		mockReturnOrderLineDatas(1L,5);

		returnLineRejections.add(firstReturnLineRejection);
		returnReview.setReturnId("1");
		returnReview.setReturnLineRejections(returnLineRejections);

		when(returnService.findReturnById(1)).thenReturn(returnData);


		returnReviewValidator.validateInternal(context, returnReview);

		assertTrue(context.containsFailures());
		assertEquals(1, context.getFailureCount());

		Failure failure = context.getFailures().get(0);
		assertEquals(FieldValidationType.INVALID, failure.getFailureType());
	}

	protected void mockReturnOrderLineDatas(Long returnOrderLineId,int quantity)

	{
		List<ReturnOrderLineData> returnOrderLineDatas = new ArrayList<>();
		returnOrderLineDatas.add(returnOrderLineData);

		when(returnOrderLineData.getReturnOrderLineId()).thenReturn(returnOrderLineId);
		when(returnOrderLineData.getQuantity()).thenReturn(new QuantityVT("",quantity));
		when(returnData.getReturnOrderLines()).thenReturn(returnOrderLineDatas);
	}

}
