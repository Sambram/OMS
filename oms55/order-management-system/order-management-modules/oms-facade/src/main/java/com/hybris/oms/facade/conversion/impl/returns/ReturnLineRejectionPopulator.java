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
import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.domain.returns.ReviewReason;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;


/**
 * Converts return line rejection data to dto.
 */
public class ReturnLineRejectionPopulator extends AbstractPopulator<ReturnLineRejectionData, ReturnLineRejection>
{
	@Override
	public void populate(final ReturnLineRejectionData source, final ReturnLineRejection target) throws ConversionException,
			IllegalArgumentException
	{
		target.setRejectionId(Long.toString(source.getRejectionId()));
		if (source.getReason() != null)
		{
			target.setReason(ReviewReason.valueOf(source.getReason()));
		}
		target.setQuantity(source.getQuantity());
		target.setResponsible(source.getResponsible());

		if (source.getMyReturnOrderLine() != null)
		{
			target.setReturnOrderLineId(Long.toString(source.getMyReturnOrderLine().getReturnOrderLineId()));
		}
	}
}
