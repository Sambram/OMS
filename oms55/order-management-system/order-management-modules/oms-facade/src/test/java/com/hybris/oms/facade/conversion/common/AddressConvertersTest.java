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
package com.hybris.oms.facade.conversion.common;

import com.hybris.commons.conversion.Converter;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class AddressConvertersTest
{
	private static final double LONGITUDE = 12.3;
	private static final double LATITUDE = 11.1;
	private static final String PHONE_NUMBER = "phoneNumber";
	private static final String NAME = "name";
	private static final String COUNTRY_NAME = "countryName";
	private static final String COUNTRY_ISO3166_ALPHA2_CODE = "countryIso3166Alpha2Code";
	private static final String POSTAL_ZONE = "postalZone";
	private static final String COUNTRY_SUBENTITY = "countrySubentity";
	private static final String CITY_NAME = "cityName";
	private static final String ADDRESS_LINE2 = "addressLine2";
	private static final String ADDRESS_LINE1 = "addressLine1";

	@Autowired
	private Converter<AddressVT, Address> addressConverter;

	@Autowired
	private AddressReverseConverter addressReverseConverter;

	@Test
	public void testConvertingAddressVT()
	{
		// given
		final AddressVT addressVT = new AddressVT(ADDRESS_LINE1, ADDRESS_LINE2, CITY_NAME, COUNTRY_SUBENTITY, POSTAL_ZONE,
				LATITUDE, LONGITUDE, COUNTRY_ISO3166_ALPHA2_CODE, COUNTRY_NAME, NAME, PHONE_NUMBER);

		// when
		final Address converted = this.addressConverter.convert(addressVT);

		// then
		Assert.assertEquals(ADDRESS_LINE1, converted.getAddressLine1());
		Assert.assertEquals(ADDRESS_LINE2, converted.getAddressLine2());
		Assert.assertEquals(CITY_NAME, converted.getCityName());
		Assert.assertEquals(COUNTRY_ISO3166_ALPHA2_CODE, converted.getCountryIso3166Alpha2Code());
		Assert.assertEquals(COUNTRY_NAME, converted.getCountryName());
		Assert.assertEquals(COUNTRY_SUBENTITY, converted.getCountrySubentity());
		Assert.assertEquals(Double.valueOf(LATITUDE), converted.getLatitudeValue());
		Assert.assertEquals(Double.valueOf(LONGITUDE), converted.getLongitudeValue());
		Assert.assertEquals(NAME, converted.getName());
		Assert.assertEquals(PHONE_NUMBER, converted.getPhoneNumber());
		Assert.assertEquals(POSTAL_ZONE, converted.getPostalZone());
	}

	@Test
	public void testReverseConvertingAddressVT()
	{
		// given
		final Address address = new Address(ADDRESS_LINE1, ADDRESS_LINE2, CITY_NAME, COUNTRY_SUBENTITY, POSTAL_ZONE, LATITUDE,
				LONGITUDE, COUNTRY_ISO3166_ALPHA2_CODE, COUNTRY_NAME, NAME, PHONE_NUMBER);

		// when
		final AddressVT converted = this.addressReverseConverter.convert(address);

		// then
		Assert.assertEquals(ADDRESS_LINE1, converted.getAddressLine1());
		Assert.assertEquals(ADDRESS_LINE2, converted.getAddressLine2());
		Assert.assertEquals(CITY_NAME, converted.getCityName());
		Assert.assertEquals(COUNTRY_ISO3166_ALPHA2_CODE, converted.getCountryIso3166Alpha2Code());
		Assert.assertEquals(COUNTRY_NAME, converted.getCountryName());
		Assert.assertEquals(COUNTRY_SUBENTITY, converted.getCountrySubentity());
		Assert.assertEquals(Double.valueOf(LATITUDE), converted.getLatitudeValue());
		Assert.assertEquals(Double.valueOf(LONGITUDE), converted.getLongitudeValue());
		Assert.assertEquals(NAME, converted.getName());
		Assert.assertEquals(PHONE_NUMBER, converted.getPhoneNumber());
		Assert.assertEquals(POSTAL_ZONE, converted.getPostalZone());
	}

}
