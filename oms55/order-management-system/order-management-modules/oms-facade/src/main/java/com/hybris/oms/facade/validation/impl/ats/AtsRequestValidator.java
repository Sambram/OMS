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
package com.hybris.oms.facade.validation.impl.ats;


import com.hybris.oms.facade.ats.AtsRequest;
import com.hybris.oms.facade.validation.ValidationContext;
import com.hybris.oms.facade.validation.field.FieldValidatorFactory;
import com.hybris.oms.facade.validation.impl.AbstractValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default ats request validator.
 */
public class AtsRequestValidator extends AbstractValidator<AtsRequest>
{
	private static final Logger LOG = LoggerFactory.getLogger(AtsRequestValidator.class);

	@Override
	public void validateInternal(final ValidationContext context, final AtsRequest atsRequest)
	{
		LOG.debug("Validating ats request");

		FieldValidatorFactory.getGenericFieldValidator(context).notNull("atsRequest.skus", atsRequest.getSkus())
				.collectionIsNotEmpty("atsRequest", atsRequest.getSkus());

		if (atsRequest.getSkus() != null)
		{
			for (final String sku : atsRequest.getSkus())
			{
				FieldValidatorFactory.getStringFieldValidator(context).notBlank("atsRequest.sku", sku);
			}
		}

	}

}
