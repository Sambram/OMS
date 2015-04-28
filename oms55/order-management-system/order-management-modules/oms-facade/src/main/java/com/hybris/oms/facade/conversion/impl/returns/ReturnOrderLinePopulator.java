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
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.returns.ReturnLineRejection;
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnLineRejectionData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.order.OrderService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


public class ReturnOrderLinePopulator extends AbstractPopulator<ReturnOrderLineData, ReturnOrderLine>
{
	private Converter<OrderLineData, OrderLine> orderLineConverter;
	private Converter<ReturnLineRejectionData, ReturnLineRejection> returnLineRejectionConverter;
	private OrderService orderService;

	@Override
	public void populate(final ReturnOrderLineData source, final ReturnOrderLine target) throws ConversionException
	{
		target.setReturnOrderLineId(String.valueOf(source.getReturnOrderLineId()));
		target.setOrderLine(orderLineConverter.convert(orderService.getOrderLineByOrderIdAndOrderLineId(source.getMyReturn()
				.getOrder().getOrderId(), source.getOrderLineId())));
		target.setReturnOrderLineStatus(source.getReturnOrderLineStatus());
		target.setQuantity(new Quantity(source.getQuantity().getUnitCode(), source.getQuantity().getValue()));

		if (!CollectionUtils.isEmpty(source.getReturnLineRejections()))
		{
			final List<ReturnLineRejection> returnLineRejectionList = new ArrayList<>();
			for (final ReturnLineRejectionData returnLineRejectionData : source.getReturnLineRejections())
			{
				returnLineRejectionList.add(returnLineRejectionConverter.convert(returnLineRejectionData));
			}
			target.setReturnLineRejections(returnLineRejectionList);
		}
	}

	@Required
	public void setOrderLineConverter(final Converter<OrderLineData, OrderLine> orderLineConverter)
	{
		this.orderLineConverter = orderLineConverter;
	}

	@Required
	public void setReturnLineRejectionConverter(Converter<ReturnLineRejectionData, ReturnLineRejection> returnLineRejectionConverter)
	{
		this.returnLineRejectionConverter = returnLineRejectionConverter;
	}

	@Required
	public void setOrderService(final OrderService orderService)
	{
		this.orderService = orderService;
	}
}
