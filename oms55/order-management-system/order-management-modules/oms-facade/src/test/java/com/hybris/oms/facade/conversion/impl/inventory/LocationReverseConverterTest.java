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
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import java.util.HashSet;
import java.util.Set;

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
public class LocationReverseConverterTest
{
	private static final String INVALID_BASESTORE_NAME = "not_a_basestore";
	private static final String COUNTRY1_CODE = "code1";
	private static final String COUNTRY1_NAME = "name1";
	private static final boolean LOC_USE_PERC_THRSHLD = true;
	private static final int LOC_PERC_INV_THRSHLD = 1;
	private static final int LOC_ABS_INV_THRSHLD = 1;
	private static final boolean LOCATION_ACTIVE = true;
	private static final AddressVT LOCATION_ADDRESS = new AddressVT(null, null, null, null, null, null, null, COUNTRY1_CODE, null,
			null, null);
	private static final int LOCATION_PRIORITY = 1;
	private static final String LOCATION_TAX_AREA_ID = "taxAreaId";
	private static final String LOCATION_STORE_NAME = "storeName";
	private static final String LOCATION_DESCRIPTION = "description";
	private static final String LOCATION_LOCATION_ID = "locationId";
	private static final Set<String> LOCATION_ROLES = ImmutableSet.of(LocationRole.SHIPPING.name());
	private static final String BASESTORE1_NAME = "baseStoreName1";
	private static final String BASESTORE2_NAME = "baseStoreName2";

	@Autowired
	private Converter<Location, StockroomLocationData> locationReverseConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setUp()
	{
		final CountryData countryData = this.persistenceManager.create(CountryData.class);
		countryData.setCode(COUNTRY1_CODE);
		countryData.setName(COUNTRY1_NAME);

		final BaseStoreData baseStoreData1 = persistenceManager.create(BaseStoreData.class);
		baseStoreData1.setName(BASESTORE1_NAME);

		final BaseStoreData baseStoreData2 = persistenceManager.create(BaseStoreData.class);
		baseStoreData2.setName(BASESTORE2_NAME);

		this.persistenceManager.flush();
	}

	@Transactional
	@Test
	public void reverseConvertingNotFlushedStockroomLocationData()
	{
		// given
		final Location location = this.createLocation();

		// when
		final StockroomLocationData locationData = this.locationReverseConverter.convert(location);

		// then
		Assert.assertTrue(locationData.getId() == null);
		Assert.assertEquals(LOCATION_LOCATION_ID, locationData.getLocationId());
		Assert.assertEquals(LOCATION_DESCRIPTION, locationData.getDescription());
		Assert.assertEquals(LOCATION_STORE_NAME, locationData.getStoreName());
		Assert.assertEquals(LOCATION_TAX_AREA_ID, locationData.getTaxAreaId());
		Assert.assertEquals(LOCATION_PRIORITY, locationData.getPriority());
		Assert.assertEquals(LOCATION_ADDRESS, locationData.getAddress());
		Assert.assertEquals(LOCATION_ACTIVE, locationData.isActive());
		Assert.assertEquals(LOC_ABS_INV_THRSHLD, locationData.getAbsoluteInventoryThreshold());
		Assert.assertEquals(LOC_PERC_INV_THRSHLD, locationData.getPercentageInventoryThreshold());
		Assert.assertEquals(LOC_USE_PERC_THRSHLD, locationData.isUsePercentageThreshold());
		Assert.assertEquals(LOCATION_ROLES, locationData.getLocationRoles());
		Assert.assertEquals(1, locationData.getShipToCountries().size());
		Assert.assertEquals(2, locationData.getBaseStores().size());
		final Set<String> baseStoreNames = new HashSet<String>();
		for (final BaseStoreData baseStore : locationData.getBaseStores())
		{
			baseStoreNames.add(baseStore.getName());
		}
		Assert.assertEquals(2, baseStoreNames.size());
		Assert.assertTrue(baseStoreNames.contains(BASESTORE1_NAME));
		Assert.assertTrue(baseStoreNames.contains(BASESTORE2_NAME));
	}

	@Transactional
	@Test
	public void reverseConvertingFlushedStockroomLocationData()
	{
		// given
		final Location location = this.createLocation();

		// when
		final StockroomLocationData locationData = this.locationReverseConverter.convert(location);

		this.persistenceManager.flush();

		// then
		Assert.assertTrue(locationData.getId() != null);
		Assert.assertEquals(LOCATION_LOCATION_ID, locationData.getLocationId());
		Assert.assertEquals(LOCATION_DESCRIPTION, locationData.getDescription());
		Assert.assertEquals(LOCATION_STORE_NAME, locationData.getStoreName());
		Assert.assertEquals(LOCATION_TAX_AREA_ID, locationData.getTaxAreaId());
		Assert.assertEquals(LOCATION_PRIORITY, locationData.getPriority());
		Assert.assertEquals(LOCATION_ADDRESS, locationData.getAddress());
		Assert.assertEquals(LOCATION_ACTIVE, locationData.isActive());
		Assert.assertEquals(LOC_ABS_INV_THRSHLD, locationData.getAbsoluteInventoryThreshold());
		Assert.assertEquals(LOC_PERC_INV_THRSHLD, locationData.getPercentageInventoryThreshold());
		Assert.assertEquals(LOC_USE_PERC_THRSHLD, locationData.isUsePercentageThreshold());
		Assert.assertEquals(LOCATION_ROLES, locationData.getLocationRoles());
		Assert.assertEquals(1, locationData.getShipToCountries().size());
		Assert.assertEquals(2, locationData.getBaseStores().size());
		final Set<String> baseStoreNames = new HashSet<String>();
		for (final BaseStoreData baseStore : locationData.getBaseStores())
		{
			baseStoreNames.add(baseStore.getName());
		}
		Assert.assertEquals(2, baseStoreNames.size());
		Assert.assertTrue(baseStoreNames.contains(BASESTORE1_NAME));
		Assert.assertTrue(baseStoreNames.contains(BASESTORE2_NAME));
	}

	@Transactional
	@Test
	public void shouldFailWhenBaseStoreNotExist()
	{
		// given
		final Location location = this.createLocation();
		location.setBaseStores(ImmutableSet.of(INVALID_BASESTORE_NAME));

		// when
		try
		{
			this.locationReverseConverter.convert(location);
			Assert.fail("ConversionException should be thrown");
		}
		catch (final ConversionException ex)
		{
			// Success
		}
	}

	@Transactional
	@Test
	public void shouldAddDefaultCountryCode()
	{
		// given
		final Location location = this.createLocation();
		location.setShipToCountriesCodes(null);

		// when
		final StockroomLocationData locationData = this.locationReverseConverter.convert(location);

		// then
		Assert.assertEquals(1, locationData.getShipToCountries().size());
		Assert.assertEquals(locationData.getAddress().getCountryIso3166Alpha2Code(), locationData.getShipToCountries().iterator()
				.next().getCode());
	}

	private Location createLocation()
	{
		final Address address = new Address();
		address.setCountryIso3166Alpha2Code(COUNTRY1_CODE);

		final Location location = new Location();
		location.setLocationId(LOCATION_LOCATION_ID);
		location.setDescription(LOCATION_DESCRIPTION);
		location.setStoreName(LOCATION_STORE_NAME);
		location.setTaxAreaId(LOCATION_TAX_AREA_ID);
		location.setPriority(LOCATION_PRIORITY);
		location.setAddress(address);
		location.setActive(LOCATION_ACTIVE);
		location.setAbsoluteInventoryThreshold(LOC_ABS_INV_THRSHLD);
		location.setPercentageInventoryThreshold(LOC_PERC_INV_THRSHLD);
		location.setUsePercentageThreshold(LOC_USE_PERC_THRSHLD);
		location.setLocationRoles(ImmutableSet.of(com.hybris.oms.domain.locationrole.LocationRole.SHIPPING));
		location.setShipToCountriesCodes(ImmutableSet.of(COUNTRY1_CODE));
		location.setBaseStores(ImmutableSet.of(BASESTORE1_NAME, BASESTORE2_NAME));

		return location;
	}
}
