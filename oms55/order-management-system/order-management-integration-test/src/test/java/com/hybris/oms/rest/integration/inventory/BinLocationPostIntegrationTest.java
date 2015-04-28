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
package com.hybris.oms.rest.integration.inventory;

import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * End-to-end integration tests.
 * This class should contain only methods that manipulate data (POST, PUT, DELETE).
 */
public class BinLocationPostIntegrationTest extends RestClientIntegrationTest
{
	@Test
	public void testCreateBin()
	{
		final String locationId = generateLocationId();
		final Location location = this.buildLocation(locationId);

		// create location
		this.getInventoryFacade().createStockRoomLocation(location);
		final String binCode = generateBinCode();
		final Bin bin = this.buildBin(binCode, locationId);

		this.getInventoryFacade().createBin(bin);

		final BinQueryObject binQueryObject = new BinQueryObject();
		binQueryObject.setLocationId(locationId);
		binQueryObject.setBinCode(binCode);
		final Bin retrieved = this.getInventoryFacade().getBinByBinCodeLocationId(binCode, locationId);
		Assert.assertNotNull(retrieved);
		Assert.assertEquals(bin.getBinCode(), retrieved.getBinCode());
		Assert.assertEquals(bin.getLocationId(), retrieved.getLocationId());
		Assert.assertEquals(bin.getDescription(), retrieved.getDescription());
	}

	@Test
	public void testUpdateBin()
	{
		final String binCode = generateBinCode();
		final String locationId = generateLocationId();
		final Location location = this.buildLocation(locationId);

		// create location
		this.getInventoryFacade().createStockRoomLocation(location);

		final Bin bin = this.buildBin(binCode, locationId);

		this.getInventoryFacade().createBin(bin);
		Assert.assertNotNull(bin);

		bin.setDescription("updated description");
		bin.setPriority(101);

		this.getInventoryFacade().updateBin(bin);

		final BinQueryObject binQueryObject = new BinQueryObject();
		binQueryObject.setLocationId(locationId);
		binQueryObject.setBinCode(binCode);
		final Bin retrieved = this.getInventoryFacade().getBinByBinCodeLocationId(binCode, locationId);
		Assert.assertNotNull(retrieved);
		Assert.assertEquals(bin.getBinCode(), retrieved.getBinCode());
		Assert.assertEquals(bin.getLocationId(), retrieved.getLocationId());
		Assert.assertEquals(bin.getDescription(), retrieved.getDescription());
		Assert.assertEquals(bin.getPriority(), retrieved.getPriority());
	}
}
