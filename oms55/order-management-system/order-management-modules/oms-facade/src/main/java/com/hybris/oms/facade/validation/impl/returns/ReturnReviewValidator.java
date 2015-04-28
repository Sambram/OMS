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

import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;

import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.domain.returns.ReturnReview;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.facade.validation.impl.AbstractValidator;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.returns.ReturnService;


/**
 * Validate required fields and constraints to Return Review.
 */
public class ReturnReviewValidator extends AbstractValidator<ReturnReview>
{
	private ReturnService returnService;

	private Validator<ReturnLineRejection> returnLineRejectionValidator;

	@Override
	protected void validateInternal(final ValidationContext context, final ReturnReview returnReview)
	{
		if (StringUtils.isEmpty(returnReview.getReturnId()))
		{
			context.reportFailure(getClass().getName(), new Failure(FieldValidationType.IS_EMPTY,
					"ReturnReview.returnId", "empty", null, "ReturnId cannot be null or empty!"));
		}
		else
		{
			if (CollectionUtils.isEmpty(returnReview.getReturnLineRejections()))
			{
				context.reportFailure(getClass().getName(), new Failure(FieldValidationType.IS_EMPTY,
						"ReturnReview.returnLineRejections", "empty", null, "Return Line Rejections cannot be null or empty!"));
			}
			else
			{
				for (final ReturnLineRejection returnLineRejection : returnReview.getReturnLineRejections())
				{
					returnLineRejectionValidator.validate("ReturnReview.ReturnLineRejection", context, returnLineRejection);
				}
			}

			if (!context.containsFailures())
			{
				validateReturnOrderLineIdsChildrenOfReturn(context, returnReview);
			}
		}
	}

	protected void validateReturnOrderLineIdsChildrenOfReturn(final ValidationContext context, final ReturnReview returnReview)
	{
		ReturnData returnData = returnService.findReturnById(Long.valueOf(returnReview.getReturnId()));
		for(final ReturnLineRejection returnLineRejection : returnReview.getReturnLineRejections())
		{
			int quantityRequested = 0;
			int rejectedSum = returnLineRejection.getQuantity();
			boolean validReturnOrderLine = false;
			for (ReturnOrderLineData returnOrderLineData : returnData.getReturnOrderLines())
			{
				if (returnOrderLineData.getReturnOrderLineId() == Long.valueOf(returnLineRejection.getReturnOrderLineId()))
				{
					validReturnOrderLine = true;
					quantityRequested = returnOrderLineData.getQuantity().getValue();
					for (ReturnLineRejectionData returnLineRejectionData : returnOrderLineData.getReturnLineRejections())
					{
						rejectedSum = rejectedSum + returnLineRejectionData.getQuantity();
					}
				}
			}
			if (!validReturnOrderLine)
			{
				context.reportFailure(getClass().getName(), new Failure(FieldValidationType.INVALID,
						"ReturnLineRejection.returnOrderLineId", returnLineRejection.getReturnOrderLineId(), null,
						"Return doesn't contains the Return Order Line sent in the list!"));
			}
			else
			{
				validateRejectionQuantity(context, rejectedSum, quantityRequested);
			}
		}
	}

	protected void validateRejectionQuantity(final ValidationContext context, final int rejectedSum, final int quantityRequested)
	{
		if (quantityRequested < rejectedSum)
		{
			context.reportFailure(getClass().getName(),new Failure(FieldValidationType.INVALID,
					"ReturnLineRejection.quantity", "", null,
					"Quantity of Return Line Rejection can't be more than quantity of ReturnOrderLine!"));
		}
	}

	@Required
	public void setReturnService(ReturnService returnService)
	{
		this.returnService = returnService;
	}

	@Required
	public void setReturnLineRejectionValidator(final ReturnLineRejectionValidator returnLineRejectionValidator)
	{
		this.returnLineRejectionValidator = returnLineRejectionValidator;
	}
}
