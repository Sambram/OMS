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
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import org.springframework.beans.factory.annotation.Required;


/**
 * Reverse converter to convert {@link Bin} dto to {@link BinData} managed object.
 */
public class BinReversePopulator implements Populator<Bin, BinData>
{

	private PersistenceManager persistenceManager;

	@Override
	public void populateFinals(final Bin source, final BinData target) throws ConversionException
	{
		target.setBinCode(source.getBinCode().toLowerCase());
	}

	@Override
	public void populate(final Bin source, final BinData target) throws ConversionException
	{
		target.setStockroomLocation(this.persistenceManager.getByIndex(StockroomLocationData.UX_STOCKROOMLOCATIONS_LOCATIONID,
				source.getLocationId()));
		target.setDescription(source.getDescription());
		target.setPriority(source.getPriority());
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

}
