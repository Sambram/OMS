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
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.service.managedobjects.inventory.BinData;


/**
 * Converter to convert {@link BinData} managed object to {@link Bin} dto.
 */
public class BinPopulator extends AbstractPopulator<BinData, Bin>
{

	@Override
	public void populate(final BinData source, final Bin target) throws ConversionException
	{
		target.setBinCode(source.getBinCode().toLowerCase());
		target.setLocationId(source.getStockroomLocation().getLocationId());
		target.setDescription(source.getDescription());
		target.setPriority(source.getPriority());
	}

}
