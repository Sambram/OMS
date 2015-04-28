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
import com.hybris.commons.conversion.Populator;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;


/**
 * Converter for order line quantity status dto to data.
 */
public class OrderLineQuantityStatusReversePopulator implements Populator<OrderLineQuantityStatus, OrderLineQuantityStatusData>
{

	@Override
	public void populateFinals(final OrderLineQuantityStatus source, final OrderLineQuantityStatusData target)
			throws ConversionException
	{
		target.setStatusCode(source.getStatusCode());
	}

	@Override
	public void populate(final OrderLineQuantityStatus source, final OrderLineQuantityStatusData target)
			throws ConversionException
	{
		target.setActive(Boolean.valueOf(source.getActive()));
		target.setDescription(source.getDescription());
	}

}
