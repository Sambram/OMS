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
import com.hybris.commons.conversion.Populator;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.types.QuantityVT;


public class ReturnOrderLineReversePopulator implements Populator<ReturnOrderLine, ReturnOrderLineData>
{

	@Override
	public void populateFinals(final ReturnOrderLine source, final ReturnOrderLineData target) throws ConversionException,
			IllegalArgumentException
	{
		if (shouldPopulateLine(source))
		{
			target.setReturnOrderLineId(Long.parseLong(source.getReturnOrderLineId()));
		}
	}

	protected boolean shouldPopulateLine(final ReturnOrderLine source)
	{
		return source.getReturnOrderLineId() != null && !source.getReturnOrderLineId().isEmpty() && source.getQuantity() != null
				&& source.getQuantity().getValue() != 0;
	}

	@Override
	public void populate(final ReturnOrderLine source, final ReturnOrderLineData target) throws ConversionException
	{
		target.setReturnOrderLineStatus(source.getReturnOrderLineStatus());
		target.setOrderLineId(source.getOrderLine().getOrderLineId());
		target.setQuantity(new QuantityVT(source.getQuantity().getUnitCode(), source.getQuantity().getValue()));
	}

}
