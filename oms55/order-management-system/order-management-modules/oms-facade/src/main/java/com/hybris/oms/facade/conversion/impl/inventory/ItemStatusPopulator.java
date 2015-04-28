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
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;


/**
 * Converter for tenant preference data to dto.
 */
public class ItemStatusPopulator extends AbstractPopulator<ItemStatusData, ItemStatus>
{

	@Override
	public void populate(final ItemStatusData source, final ItemStatus target) throws ConversionException
	{
		target.setDescription(source.getDescription());
		target.setStatusCode(source.getStatusCode());
	}

}
