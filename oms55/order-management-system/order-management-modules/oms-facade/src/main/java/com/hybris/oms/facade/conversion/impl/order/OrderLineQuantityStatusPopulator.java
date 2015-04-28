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
package com.hybris.oms.facade.conversion.impl.order;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;


/**
 * Converter for order line quantity status data to dto.
 */
public class OrderLineQuantityStatusPopulator extends AbstractPopulator<OrderLineQuantityStatusData, OrderLineQuantityStatus>
{

	@Override
	public void populate(final OrderLineQuantityStatusData source, final OrderLineQuantityStatus target)
			throws ConversionException
	{
		if (source.getActive() != null)
		{
			target.setActive(source.getActive().booleanValue());
		}
		target.setDescription(source.getDescription());
		target.setStatusCode(source.getStatusCode());
	}

}
