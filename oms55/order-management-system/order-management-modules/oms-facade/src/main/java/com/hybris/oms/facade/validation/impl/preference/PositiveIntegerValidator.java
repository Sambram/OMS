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
package com.hybris.oms.facade.validation.impl.preference;

import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.facade.validation.Failure;
import com.hybris.oms.facade.validation.FieldValidationType;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.impl.AbstractValidator;


/**
 * Validates that a value is positive and smaller than Integer.MAX_VALUE.
 * Applied to {@link TenantPreferenceConstants#PREF_KEY_ATS_GLOBAL_THRHOLD} and
 * {@link TenantPreferenceConstants#PREF_KEY_SHIPPING_WITHIN_DAYS}
 */
public class PositiveIntegerValidator extends AbstractValidator<String>
{
	@Override
	protected void validateInternal(final ValidationContext context, final String value)
	{
		if (!value.matches("^\\d+$"))
		{
			context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.LESS_THAN, "tenantPreference.value",
					value, "Tenant preference is not a valid positive number!"));

		}
		else if (Integer.parseInt(value) > Integer.MAX_VALUE)
		{
			context.reportFailure(this.getClass().getName(), new Failure(FieldValidationType.LESS_THAN, "tenantPreference.value",
					value, "Tenant preference value cannot exceed Integer.MAX_VALUE!"));
		}
	}
}
