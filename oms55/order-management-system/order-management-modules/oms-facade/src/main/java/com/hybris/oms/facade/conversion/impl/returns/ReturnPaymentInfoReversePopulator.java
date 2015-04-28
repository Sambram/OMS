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
import com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData;
import com.hybris.oms.service.managedobjects.types.AmountVT;



public class ReturnPaymentInfoReversePopulator extends AbstractPopulator<ReturnPaymentInfo, ReturnPaymentInfoData>
{

	@Override
	public void populateFinals(final ReturnPaymentInfo source, final ReturnPaymentInfoData target) throws ConversionException,
			IllegalArgumentException
	{
		if (source.getReturnPaymentInfoId() != null && !source.getReturnPaymentInfoId().isEmpty())
		{
			target.setReturnPaymentInfoId(Long.parseLong(source.getReturnPaymentInfoId()));
		}
	}

	@Override
	public void populate(final ReturnPaymentInfo source, final ReturnPaymentInfoData target) throws ConversionException
	{
		target.setReturnPaymentType(source.getReturnPaymentType());
		if (source.getReturnPaymentAmount() != null)
		{
			target.setReturnPaymentAmount(new AmountVT(source.getReturnPaymentAmount().getCurrencyCode(), source
					.getReturnPaymentAmount().getValue()));
		}
		if (source.getTaxReversed() != null)
		{
			target.setTaxReversed(new AmountVT(source.getTaxReversed().getCurrencyCode(), source.getTaxReversed().getValue()));
		}
	}
}
