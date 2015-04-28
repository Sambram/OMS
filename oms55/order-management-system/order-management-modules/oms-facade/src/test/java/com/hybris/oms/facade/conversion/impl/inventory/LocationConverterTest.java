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
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class LocationConverterTest
{

	private static final String COUNTRY_NAME = "country1";
	private static final String COUNTRY_CODE = "code1";
	private static final boolean LOC_USE_PERC_THRSHLD = true;
	private static final int LOC_PERC_INV_THRSHLD = 1;
	private static final int LOC_ABS_INV_THRSHLD = 1;
	private static final boolean LOCATION_ACTIVE = true;
	private static final Address LOCATION_ADDRESS = AddressBuilder.anAddress().buildAddressDTO();
	private static final int LOCATION_PRIORITY = 1;
	private static final String LOCATION_TAX_AREA_ID = "taxAreaId";
	private static final String LOCATION_STORE_NAME = "storeName";
	private static final String LOCATION_DESCRIPTION = "description";
	private static final String LOCATION_LOCATION_ID = "locationId";
	private static final Set<LocationRole> LOCATION_ROLES = ImmutableSet.of(LocationRole.SHIPPING);
	private static final String BASESTORE1_NAME = "baseStoreName1";
	private static final String BASESTORE2_NAME = "baseStoreName2";

	@Autowired
	private Converter<StockroomLocationData, Location> locationConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedLocationData()
	{
		// given
		final StockroomLocationData locationData = this.createLocationData();

		// when
		final Location location = this.locationConverter.convert(locationData);

		// then
		Assert.assertEquals(LOCATION_LOCATION_ID, location.getLocationId());
		Assert.assertEquals(LOCATION_DESCRIPTION, location.getDescription());
		Assert.assertEquals(LOCATION_STORE_NAME, location.getStoreName());
		Assert.assertEquals(LOCATION_TAX_AREA_ID, location.getTaxAreaId());
		Assert.assertEquals(LOCATION_PRIORITY, location.getPriority());
		Assert.assertEquals(LOCATION_ADDRESS, location.getAddress());
		Assert.assertEquals(LOCATION_ACTIVE, location.getActive());
		Assert.assertEquals(LOC_ABS_INV_THRSHLD, location.getAbsoluteInventoryThreshold());
		Assert.assertEquals(LOC_PERC_INV_THRSHLD, location.getPercentageInventoryThreshold());
		Assert.assertEquals(LOC_USE_PERC_THRSHLD, location.getUsePercentageThreshold());
		Assert.assertEquals(LOCATION_ROLES, location.getLocationRoles());
		Assert.assertEquals(1, location.getShipToCountriesCodes().size());
		Assert.assertEquals(2, location.getBaseStores().size());
		Assert.assertTrue(location.getBaseStores().contains(BASESTORE1_NAME));
		Assert.assertTrue(location.getBaseStores().contains(BASESTORE2_NAME));
	}

	@Transactional
	@Test
	public void convertingFlushedLocationData()
	{
		// given
		final StockroomLocationData locationData = this.createLocationData();

		this.persistenceManager.flush();

		// when
		final Location location = this.locationConverter.convert(locationData);

		// then
		Assert.assertEquals(LOCATION_LOCATION_ID, location.getLocationId());
		Assert.assertEquals(LOCATION_DESCRIPTION, location.getDescription());
		Assert.assertEquals(LOCATION_STORE_NAME, location.getStoreName());
		Assert.assertEquals(LOCATION_TAX_AREA_ID, location.getTaxAreaId());
		Assert.assertEquals(LOCATION_PRIORITY, location.getPriority());
		Assert.assertEquals(LOCATION_ADDRESS, location.getAddress());
		Assert.assertEquals(LOCATION_ACTIVE, location.getActive());
		Assert.assertEquals(LOC_ABS_INV_THRSHLD, location.getAbsoluteInventoryThreshold());
		Assert.assertEquals(LOC_PERC_INV_THRSHLD, location.getPercentageInventoryThreshold());
		Assert.assertEquals(LOC_USE_PERC_THRSHLD, location.getUsePercentageThreshold());
		Assert.assertEquals(LOCATION_ROLES, location.getLocationRoles());
		Assert.assertEquals(1, location.getShipToCountriesCodes().size());
		Assert.assertEquals(2, location.getBaseStores().size());
		Assert.assertTrue(location.getBaseStores().contains(BASESTORE1_NAME));
		Assert.assertTrue(location.getBaseStores().contains(BASESTORE2_NAME));
	}

	private StockroomLocationData createLocationData()
	{
		final StockroomLocationData locationData = this.persistenceManager.create(StockroomLocationData.class);

		locationData.setLocationId(LOCATION_LOCATION_ID);
		locationData.setDescription(LOCATION_DESCRIPTION);
		locationData.setStoreName(LOCATION_STORE_NAME);
		locationData.setTaxAreaId(LOCATION_TAX_AREA_ID);
		locationData.setPriority(LOCATION_PRIORITY);
		locationData.setAddress(AddressBuilder.anAddress().buildAddressVT());
		locationData.setActive(LOCATION_ACTIVE);
		locationData.setAbsoluteInventoryThreshold(LOC_ABS_INV_THRSHLD);
		locationData.setPercentageInventoryThreshold(LOC_PERC_INV_THRSHLD);
		locationData.setUsePercentageThreshold(LOC_USE_PERC_THRSHLD);
		locationData.setLocationRoles(ImmutableSet.of(com.hybris.oms.service.managedobjects.inventory.LocationRole.SHIPPING
				.getCode()));
		locationData.setShipToCountries(ImmutableSet.of(createCountry(COUNTRY_NAME, COUNTRY_CODE)));

		final BaseStoreData baseStoreData1 = persistenceManager.create(BaseStoreData.class);
		baseStoreData1.setName(BASESTORE1_NAME);

		final BaseStoreData baseStoreData2 = persistenceManager.create(BaseStoreData.class);
		baseStoreData2.setName(BASESTORE2_NAME);

		locationData.setBaseStores(ImmutableSet.of(baseStoreData1, baseStoreData2));
		return locationData;
	}

	private CountryData createCountry(final String countryName, final String countryCode)
	{
		final CountryData result = this.persistenceManager.create(CountryData.class);
		result.setName(countryName);
		result.setCode(countryCode);
		return result;
	}
}
