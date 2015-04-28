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
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;


/**
 * Converter for current item quantity data to dto.
 */

public class OmsInventoryPopulator extends AbstractPopulator<ItemQuantityData, OmsInventory>
{

	@Override
	public void populate(final ItemQuantityData source, final OmsInventory target) throws ConversionException
	{
		if (source.getOwner() != null)
		{
			final ItemLocationData owner = source.getOwner();
			target.setSkuId(owner.getItemId());
			if (owner.getStockroomLocation() != null)
			{
				target.setLocationId(owner.getStockroomLocation().getLocationId());
			}

			if (owner.getBin() != null && !owner.getBin().getBinCode().equals(InventoryServiceConstants.DEFAULT_BIN))
			{
				target.setBinCode(owner.getBin().getBinCode());
			}
		}

		if (source.getStatusCode() != null)
		{
			target.setStatus(source.getStatusCode());
		}

		if (source instanceof FutureItemQuantityData)
		{
			target.setDeliveryDate(((FutureItemQuantityData) source).getExpectedDeliveryDate());
		}

		if (source.getQuantityUnitCode() != null)
		{
			target.setUnitCode(source.getQuantityUnitCode());
		}
		target.setQuantity(source.getQuantityValue());
	}

}
