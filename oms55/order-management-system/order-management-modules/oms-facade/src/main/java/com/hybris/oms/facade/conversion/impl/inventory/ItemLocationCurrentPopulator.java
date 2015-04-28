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
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.CurrentItemQuantity;
import com.hybris.oms.domain.inventory.CurrentItemQuantityStatus;
import com.hybris.oms.domain.inventory.ItemLocationCurrent;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Converter for item location data to item location current dto.
 */
public class ItemLocationCurrentPopulator extends AbstractPopulator<ItemLocationData, ItemLocationCurrent>
{
	private static final String SEPARATOR = "-";

	private Converter<StockroomLocationData, Location> locationConverter;

	private Converter<CurrentItemQuantityData, CurrentItemQuantity> currentItemQuantityConverter;

	private Converter<BinData, Bin> binConverter;

	@Override
	public void populate(final ItemLocationData source, final ItemLocationCurrent target) throws ConversionException
	{
		if (source.isFuture())
		{
			throw new ConversionException("isFuture cannot be true!");
		}
		target.setFuture(source.isFuture());
		target.setItemId(source.getItemId());
		target.setLocation(this.locationConverter.convert(source.getStockroomLocation()));
		target.setId(source.getItemId() + SEPARATOR + source.getStockroomLocation().getLocationId());

		if (CollectionUtils.isNotEmpty(source.getItemQuantities()))
		{
			final Map<CurrentItemQuantityStatus, CurrentItemQuantity> quantities = new HashMap<>();
			for (final ItemQuantityData quantity : source.getItemQuantities())
			{
				final CurrentItemQuantityStatus currentItemQuantityStatus = new CurrentItemQuantityStatus(quantity.getStatusCode());
				final CurrentItemQuantity currentItemQuantity = this.currentItemQuantityConverter
						.convert((CurrentItemQuantityData) quantity);
				quantities.put(currentItemQuantityStatus, currentItemQuantity);
			}
			target.setItemQuantities(quantities);
		}

		if (source.getBin() != null && !source.getBin().getBinCode().equals(InventoryServiceConstants.DEFAULT_BIN))
		{
			target.setBin((binConverter.convert(source.getBin())));
		}
	}

	@Required
	public void setLocationConverter(final Converter<StockroomLocationData, Location> locationConverter)
	{
		this.locationConverter = locationConverter;
	}

	@Required
	public void setCurrentItemQuantityConverter(
			final Converter<CurrentItemQuantityData, CurrentItemQuantity> currentItemQuantityConverter)
	{
		this.currentItemQuantityConverter = currentItemQuantityConverter;
	}

	@Required
	public void setBinConverter(final Converter<BinData, Bin> binConverter)
	{
		this.binConverter = binConverter;
	}
}
