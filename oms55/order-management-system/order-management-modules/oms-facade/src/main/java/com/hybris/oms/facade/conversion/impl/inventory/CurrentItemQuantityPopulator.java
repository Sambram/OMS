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
import com.hybris.oms.domain.inventory.CurrentItemQuantity;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;


/**
 * Converter for current item quantity data to dto. It's designed to be used internally by
 * {@link ItemLocationCurrentPopulator}.
 */
public class CurrentItemQuantityPopulator extends AbstractPopulator<CurrentItemQuantityData, CurrentItemQuantity>
{
	private static final String SEPARATOR = "-";

	@Override
	public void populate(final CurrentItemQuantityData source, final CurrentItemQuantity target) throws ConversionException
	{
		target.setQuantity(new Quantity(source.getQuantityUnitCode(), source.getQuantityValue()));
		target.setId(source.getOwner().getStockroomLocation().getLocationId() + SEPARATOR + source.getStatusCode());
	}

}
