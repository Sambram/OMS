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

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;


/**
 * End-to-end integration tests. This class should contain only methods to retrieve data (GET).
 */
public class BinGetIntegrationTest extends RestClientIntegrationTest
{


	private static final String BIN_CODE = "bincode1";

	@Test
	public void testFindPageableBinsByQuery()
	{

		final String locationId = new Date().toString();
		final Location location = this.buildLocation(locationId);
		final String binCode = generateBinCode();

		// create location
		this.getInventoryFacade().createStockRoomLocation(location);

		final Bin bin = this.buildBin(binCode, locationId);

		// create bin
		this.getInventoryFacade().createBin(bin);

		final BinQueryObject binQueryObject = new BinQueryObject();
		binQueryObject.setPageNumber(0);
		binQueryObject.setPageSize(1);
		binQueryObject.setLocationId(locationId);

		final Pageable<Bin> pagedBins = this.getInventoryFacade().findBinsByQuery(binQueryObject);

		Assert.assertEquals(1, pagedBins.getTotalRecords().intValue());

		Assert.assertEquals(1, pagedBins.getTotalPages().intValue());

		Assert.assertEquals(0, pagedBins.getNextPage().intValue());
		Assert.assertEquals(0, pagedBins.getPreviousPage().intValue());
		Assert.assertEquals(locationId, pagedBins.getResults().get(0).getLocationId());
		Assert.assertEquals(binCode, pagedBins.getResults().get(0).getBinCode());



	}


	@Test
	public void testFindPageableBinsByBinCodeQuery()
	{

		final String locationId = generateLocationId();

		final Location location = this.buildLocation(locationId);

		// create location
		this.getInventoryFacade().createStockRoomLocation(location);

		final Bin bin = this.buildBin(BIN_CODE, locationId);

		// create bin
		this.getInventoryFacade().createBin(bin);

		final BinQueryObject binQueryObject = new BinQueryObject();
		binQueryObject.setPageNumber(0);
		binQueryObject.setPageSize(1);
		binQueryObject.setBinCode(BIN_CODE);
		binQueryObject.setLocationId(locationId);

		final Pageable<Bin> pagedBins = this.getInventoryFacade().findBinsByQuery(binQueryObject);

		Assert.assertEquals(1, pagedBins.getTotalRecords().intValue());

		Assert.assertEquals(1, pagedBins.getTotalPages().intValue());

		Assert.assertEquals(0, pagedBins.getNextPage().intValue());
		Assert.assertEquals(0, pagedBins.getPreviousPage().intValue());
		Assert.assertTrue(pagedBins.getResults().get(0).getLocationId() != null);
		Assert.assertTrue(pagedBins.getResults().get(0).getId() != null);

	}

	@Test
	public void testGetBin()
	{

		final String locationId = generateLocationId();
		final String binCode = generateBinCode();

		final Location location = this.buildLocation(locationId);

		// create location
		this.getInventoryFacade().createStockRoomLocation(location);

		final Bin bin = this.buildBin(binCode, locationId);

		// create bin
		this.getInventoryFacade().createBin(bin);

		final Bin retrieved = this.getInventoryFacade().getBinByBinCodeLocationId(binCode, locationId);

		Assert.assertNotNull(retrieved);

		Assert.assertEquals(locationId, retrieved.getLocationId());
		Assert.assertEquals(binCode, retrieved.getBinCode());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testGetBinNotFound()
	{

		final String locationId = generateLocationId();
		final String binCode = generateBinCode();

		final Location location = this.buildLocation(locationId);

		// create location
		this.getInventoryFacade().createStockRoomLocation(location);

		final Bin bin = this.buildBin(binCode, locationId);

		// create bin
		this.getInventoryFacade().createBin(bin);

		this.getInventoryFacade().getBinByBinCodeLocationId(binCode, new Date().toString());
	}
}
