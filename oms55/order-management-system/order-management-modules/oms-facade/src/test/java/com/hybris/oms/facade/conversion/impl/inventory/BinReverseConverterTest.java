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

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class BinReverseConverterTest
{

	private static final boolean LOC_USE_PERC_THRSHLD = true;
	private static final int LOC_PERC_INV_THRSHLD = 1;
	private static final int LOC_ABS_INV_THRSHLD = 1;
	private static final boolean LOCATION_ACTIVE = true;
	private static final int LOCATION_PRIORITY = 1;
	private static final String LOCATION_TAX_AREA_ID = "taxAreaId";
	private static final String LOCATION_STORE_NAME = "storeName";
	private static final String LOCATION_DESCRIPTION = "description";
	private static final String LOCATION_LOCATION_ID = "locationId";
	private static final String BIN_CODE = "binCode";
	private static final String BIN_CODE_LOWERCASE = "bincode";
	private static final String BIN_DESCRIPTION = "description";
	private static final int BIN_PRIORITY = 5;

	@Autowired
	private Converter<Bin, BinData> binReverseConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setUp()
	{
		this.createLocationData();
	}

	@Transactional
	@Test
	public void reverseConvertingNotFlushedBinData()
	{
		// given
		final Bin bin = this.createBin();

		// when
		final BinData binData = this.binReverseConverter.convert(bin);

		// then
		Assert.assertTrue(binData.getId() == null);
		Assert.assertEquals(LOCATION_LOCATION_ID, binData.getStockroomLocation().getLocationId());
		Assert.assertEquals(BIN_DESCRIPTION, binData.getDescription());
		Assert.assertEquals(BIN_CODE_LOWERCASE, binData.getBinCode());
		Assert.assertEquals(BIN_PRIORITY, binData.getPriority());
	}

	@Transactional
	@Test
	public void reverseConvertingFlushedBinData()
	{
		// given
		final Bin bin = this.createBin();

		// when
		final BinData binData = this.binReverseConverter.convert(bin);

		this.persistenceManager.flush();

		// then
		Assert.assertTrue(binData.getId() != null);
		Assert.assertEquals(LOCATION_LOCATION_ID, binData.getStockroomLocation().getLocationId());
		Assert.assertEquals(BIN_DESCRIPTION, binData.getDescription());
		Assert.assertEquals(BIN_CODE_LOWERCASE, binData.getBinCode());
		Assert.assertEquals(BIN_PRIORITY, binData.getPriority());
	}

	private Bin createBin()
	{
		final Bin bin = new Bin();
		bin.setBinCode(BIN_CODE);
		bin.setLocationId(LOCATION_LOCATION_ID);
		bin.setDescription(BIN_DESCRIPTION);
		bin.setPriority(BIN_PRIORITY);
		return bin;
	}

	private void createLocationData()
	{
		final StockroomLocationData locationData = this.persistenceManager.create(StockroomLocationData.class);
		locationData.setLocationId(LOCATION_LOCATION_ID);
		locationData.setDescription(LOCATION_DESCRIPTION);
		locationData.setStoreName(LOCATION_STORE_NAME);
		locationData.setTaxAreaId(LOCATION_TAX_AREA_ID);
		locationData.setPriority(LOCATION_PRIORITY);
		locationData.setAddress(new AddressVT(null, null, null, null, null, null, null, null, null, null, null));
		locationData.setActive(LOCATION_ACTIVE);
		locationData.setAbsoluteInventoryThreshold(LOC_ABS_INV_THRSHLD);
		locationData.setPercentageInventoryThreshold(LOC_PERC_INV_THRSHLD);
		locationData.setUsePercentageThreshold(LOC_USE_PERC_THRSHLD);
		locationData.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		this.persistenceManager.flush();
	}

}
