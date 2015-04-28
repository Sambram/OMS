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


import org.springframework.util.StringUtils;

import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.impl.AbstractValidator;


/**
 * Validate required fields and constraints to Return Line Rejection.
 */
public class ReturnLineRejectionValidator extends AbstractValidator<ReturnLineRejection>
{
	private String validatorName = getClass().getName();

	@Override
	protected void validateInternal(ValidationContext context, ReturnLineRejection returnLineRejection)
	{
		if (StringUtils.isEmpty(returnLineRejection.getReturnOrderLineId()))
		{
			context.reportFailure(validatorName, new Failure(FieldValidationType.IS_EMPTY, "ReturnLineRejection.returnOrderLineId",
					"empty", null, "Return Order Line ID cannot be null or empty!"));
		}
		else
		{
			try
			{
				Long.valueOf(returnLineRejection.getReturnOrderLineId());
			}
			catch (NumberFormatException e)
			{
				context.reportFailure(validatorName, new Failure(FieldValidationType.INVALID,
						"ReturnLineRejection.returnOrderLineId", returnLineRejection.getReturnOrderLineId(), null,
						"Return Order Line ID is invalid not a valid number!"));
			}
		}

		if (returnLineRejection.getQuantity() == null)
		{
			context.reportFailure(validatorName, new Failure(FieldValidationType.IS_NULL, "ReturnLineRejection.quantity", "null",
					null, "Quantity cannot be null!"));
		}
		else if (returnLineRejection.getQuantity() < 0)
		{
			context.reportFailure(validatorName, new Failure(FieldValidationType.LESS_THAN, "ReturnLineRejection.quantity", "0",
					null, "Quantity cannot be less than zero!"));
		}

		if (returnLineRejection.getReason() == null)
		{
			context.reportFailure(validatorName, new Failure(FieldValidationType.IS_EMPTY, "ReturnLineRejection.reviewReason",
					"empty", null, "Return reason cannot be null or empty!"));
		}

		if (StringUtils.isEmpty(returnLineRejection.getResponsible()))
		{
			context.reportFailure(validatorName, new Failure(FieldValidationType.IS_EMPTY, "ReturnLineRejection.responsible",
					"empty", null, "Responsible cannot be null or empty!"));
		}

	}
}
