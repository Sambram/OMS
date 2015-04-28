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
package com.hybris.oms.facade.conversion.impl.inventory;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Populator;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;


/**
 * Reverse converter for {@link ItemStatus} dto to {@link ItemStatusData} managed object.
 */
public class ItemStatusReversePopulator implements Populator<ItemStatus, ItemStatusData>
{

	@Override
	public void populate(final ItemStatus source, final ItemStatusData target) throws ConversionException
	{
		target.setStatusCode(source.getStatusCode());
		target.setDescription(source.getDescription());
	}

	@Override
	public void populateFinals(final ItemStatus source, final ItemStatusData target) throws ConversionException
	{
		target.setStatusCode(source.getStatusCode());
	}

}
