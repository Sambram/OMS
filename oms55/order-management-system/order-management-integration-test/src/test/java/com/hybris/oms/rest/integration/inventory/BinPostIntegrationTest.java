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

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import org.junit.Test;


/**
 * End-to-end integration tests.
 * This class should contain only methods that manipulate data (POST, PUT, DELETE).
 */
public class BinPostIntegrationTest extends RestClientIntegrationTest
{
	@Test(expected = EntityNotFoundException.class)
	public void testDeleteBin()
	{
		final String locationId = generateLocationId();
		final String binCode = generateBinCode();
		final Location location = this.buildLocation(locationId);

		// create location
		this.getInventoryFacade().createStockRoomLocation(location);

		final Bin bin = this.buildBin(binCode, locationId);

		this.getInventoryFacade().createBin(bin);

		this.getInventoryFacade().deleteBinByBinCodeLocationId(binCode, locationId);

		// this should throw EntityNotFoundException as the entity has been deleted
		this.getInventoryFacade().getBinByBinCodeLocationId(binCode, locationId);
	}
}
