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
import com.hybris.oms.domain.inventory.FutureItemQuantity;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;


/**
 * Converter for future item quantity data to dto. It's designed to be used internally by
 * {@link ItemLocationFuturePopulator}.
 */
public class FutureItemQuantityPopulator extends AbstractPopulator<FutureItemQuantityData, FutureItemQuantity>
{

	private static final String SEPARATOR = "-";

	@Override
	public void populate(final FutureItemQuantityData source, final FutureItemQuantity target) throws ConversionException
	{
		target.setQuantity(new Quantity(source.getQuantityUnitCode(), source.getQuantityValue()));
		target.setExpectedDeliveryDate(source.getExpectedDeliveryDate());
		target.setId(source.getOwner().getStockroomLocation().getLocationId() + SEPARATOR + source.getStatusCode() + SEPARATOR
				+ source.getExpectedDeliveryDate().getTime());
	}

}
