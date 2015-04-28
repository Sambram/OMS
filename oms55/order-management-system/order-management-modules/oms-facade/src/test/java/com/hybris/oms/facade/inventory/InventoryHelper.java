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
package com.hybris.oms.facade.inventory;

import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;

import java.util.Date;
import java.util.Set;


public final class InventoryHelper
{
	private InventoryHelper()
	{
		// avoid instantiation
	}

	public static Location buildLocation(final String locationId, final Set<LocationRole> locationRoles)
	{
		final Address address = new Address();
		address.setAddressLine1("default");
		final Location location = new Location();
		location.setAddress(address);
		location.setLocationId(locationId);
		location.setLocationRoles(locationRoles);
		return location;
	}

	public static ItemStatus buildItemStatus(final String statusCode, final String description)
	{
		final ItemStatus itemStatus = new ItemStatus();
		itemStatus.setDescription(description);
		itemStatus.setStatusCode(statusCode);
		return itemStatus;
	}

	public static OmsInventory buildInventory(final String skuId, final String locationId, final String binCode,
			final String statusCode, final Date deliveryDate, final int quantity)
	{
		final OmsInventory inventory = new OmsInventory();
		inventory.setSkuId(skuId);
		inventory.setLocationId(locationId);
		inventory.setStatus(statusCode);
		inventory.setDeliveryDate(deliveryDate);
		inventory.setQuantity(quantity);
		inventory.setBinCode(binCode);
		return inventory;
	}

	public static Bin buildBin(final String binCode, final String locationId, final String description, final int priority)
	{
		final Bin bin = new Bin();
		bin.setBinCode(binCode);
		bin.setLocationId(locationId);
		bin.setDescription(description);
		bin.setPriority(priority);
		return bin;
	}
}
