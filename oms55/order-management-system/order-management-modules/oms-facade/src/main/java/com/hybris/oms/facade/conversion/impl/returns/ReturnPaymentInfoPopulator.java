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
package com.hybris.oms.facade.conversion.impl.returns;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData;


public class ReturnPaymentInfoPopulator extends AbstractPopulator<ReturnPaymentInfoData, ReturnPaymentInfo>
{

	@Override
	public void populate(final ReturnPaymentInfoData source, final ReturnPaymentInfo target) throws ConversionException
	{
		target.setReturnPaymentInfoId(Long.toString(source.getReturnPaymentInfoId()));
		target.setReturnPaymentType(source.getReturnPaymentType());
		if (source.getReturnPaymentAmount() != null)
		{
			target.setReturnPaymentAmount(new Amount(source.getReturnPaymentAmount().getCurrencyCode(), source
					.getReturnPaymentAmount().getValue()));
		}
		else
		{
			target.setReturnPaymentAmount(new Amount());
		}

		if (source.getTaxReversed() != null)
		{
			target.setTaxReversed(new Amount(source.getTaxReversed().getCurrencyCode(), source.getTaxReversed().getValue()));
		}
		else
		{
			target.setTaxReversed(new Amount());
		}
	}
}
