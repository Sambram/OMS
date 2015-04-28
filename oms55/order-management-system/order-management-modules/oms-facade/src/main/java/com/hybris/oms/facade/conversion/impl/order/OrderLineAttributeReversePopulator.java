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
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;


/**
 * Converts a {@link OrderLineAttribute} dto to {@link OrderLineAttributeData}.
 */
public class OrderLineAttributeReversePopulator extends
		com.hybris.commons.conversion.impl.AbstractPopulator<OrderLineAttribute, OrderLineAttributeData>
{

	@Override
	public void populate(final OrderLineAttribute source, final OrderLineAttributeData target) throws ConversionException
	{
		target.setAttributeId(source.getId());
		target.setDescription(source.getDescription());
	}

}
