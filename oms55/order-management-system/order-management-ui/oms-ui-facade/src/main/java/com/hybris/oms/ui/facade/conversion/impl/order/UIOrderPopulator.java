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
package com.hybris.oms.ui.facade.conversion.impl.order;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.ui.api.order.UIOrder;


/**
 * Converter for order data to dto.
 */
public class UIOrderPopulator extends AbstractPopulator<OrderData, UIOrder>
{

	@Override
	public void populate(final OrderData source, final UIOrder target) throws ConversionException
	{
		target.setCurrencyCode(source.getCurrencyCode());
		target.setCustomerLocale(source.getCustomerLocale());
		target.setEmailid(source.getEmailid());
		target.setFirstName(source.getFirstName());
		target.setIssueDate(source.getIssueDate());
		target.setScheduledShippingDate(source.getScheduledShippingDate());
		target.setLastName(source.getLastName());
		target.setOrderId(source.getOrderId());

		target.setPriorityLevelCode(source.getPriorityLevelCode());
		this.populateShipping(source, target);
		target.setUsername(source.getUsername());
		target.setLocationIds(source.getStockroomLocationIds());

		populateBaseStore(source, target);
	}

	protected void populateBaseStore(final OrderData source, final UIOrder target)
	{
		final BaseStoreData baseStoreData = source.getBaseStore();
		if (baseStoreData != null)
		{
			target.setBaseStoreName(baseStoreData.getName());
		}
	}

	protected void populateShipping(final OrderData source, final UIOrder target)
	{
		target.setShippingFirstName(source.getShippingFirstName());
		target.setShippingLastName(source.getShippingLastName());
		target.setShippingMethod(source.getShippingMethod());
		target.setShippingTaxCategory(source.getShippingTaxCategory());
	}

}
