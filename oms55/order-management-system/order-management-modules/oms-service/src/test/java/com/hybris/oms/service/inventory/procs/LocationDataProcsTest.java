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
package com.hybris.oms.service.inventory.procs;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.inventory.impl.LocationDataStaticUtils;
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


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class LocationDataProcsTest
{

	private static final String LOCATION_HYBRIS_ID = "single|StockroomLocationData|1";

	@Autowired
	private ImportService importService;

	@Autowired
	private PersistenceManager persistenceManager;

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	private StockroomLocationData location;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/inventory/test-inventory-data-import.mcsv")[0]);
		this.location = this.persistenceManager.get(HybrisId.valueOf(LOCATION_HYBRIS_ID));
	}

	@Transactional
	@Test
	public void testHasValidAddressGeocodesTrue()
	{

		this.location.setAddress(this.changeLatLonPostalValues(this.location.getAddress(), 45.45, 75.65, "55555"));

		final boolean result = LocationDataStaticUtils.hasValidAddressGeocodes(this.location);
		Assert.assertTrue(result);
	}

	@Transactional
	@Test
	public void testHasValidAddressGeocodesFalse()
	{
		this.location.setAddress(this.changeLatLonPostalValues(this.location.getAddress(), 30.5, -500d, this.location.getAddress()
				.getPostalZone()));
		boolean result = LocationDataStaticUtils.hasValidAddressGeocodes(this.location);
		Assert.assertFalse(result);

		this.location.setAddress(this.changeLatLonPostalValues(this.location.getAddress(), null, 75.65, this.location.getAddress()
				.getPostalZone()));
		result = LocationDataStaticUtils.hasValidAddressGeocodes(this.location);
		Assert.assertFalse(result);

		this.location.setAddress(this.changeLatLonPostalValues(this.location.getAddress(), 45.45, null, this.location.getAddress()
				.getPostalZone()));
		result = LocationDataStaticUtils.hasValidAddressGeocodes(this.location);
		Assert.assertFalse(result);
	}

	private AddressVT changeLatLonPostalValues(final AddressVT source, final Double latitudeValue,
			final Double longitudeValue, final String postalZone)
	{
		return new AddressVT(source.getAddressLine1(), source.getAddressLine2(), source.getCityName(),
				source.getCountrySubentity(), postalZone, latitudeValue, longitudeValue, source.getCountryIso3166Alpha2Code(),
				source.getCountryName(), source.getName(), source.getPhoneNumber());
	}

}
