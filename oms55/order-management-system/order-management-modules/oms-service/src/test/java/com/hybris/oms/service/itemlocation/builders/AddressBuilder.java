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
package com.hybris.oms.service.itemlocation.builders;

import com.hybris.oms.domain.address.Address;
import com.hybris.oms.service.managedobjects.types.AddressVT;


/**
 * The Class AddressBuilder.
 */
public final class AddressBuilder
{

	private final String addressLine1;
	private final String addressLine2;
	private final String cityName;
	private final String countrySubentity;
	private String countryIso3166Alpha2Code;
	private final String countryName;
	private final String name;
	private final String phoneNumber;
	private String postalZone;
	private Double latitudeValue;
	private Double longitudeValue;

	/**
	 * Instantiates a new address builder.
	 */
	private AddressBuilder()
	{
		// Get some reasonable defaults
		this.addressLine1 = "123 Fake Street";
		this.addressLine2 = "addressLine2";
		this.cityName = "Montreal";
		this.countrySubentity = "QC";
		this.postalZone = "H9J 3X0";
		this.latitudeValue = 40.0d;
		this.longitudeValue = 40.0d;
		this.countryIso3166Alpha2Code = "CA";
		this.countryName = "Canada";
		this.name = "Name";
		this.phoneNumber = "Phone";
	}

	/**
	 * An address.
	 * 
	 * @return the address builder
	 */
	public static AddressBuilder anAddress()
	{
		return new AddressBuilder();
	}

	/**
	 * Builds the.
	 * 
	 * @return the temp address
	 */
	public AddressVT build()
	{
		return new AddressVT(this.addressLine1, this.addressLine2, this.cityName, this.countrySubentity, this.postalZone,
				this.latitudeValue, this.longitudeValue, this.countryIso3166Alpha2Code, this.countryName, this.name, this.phoneNumber);
	}

	public AddressVT buildAddressVT()
	{
		return new AddressVT(this.addressLine1, this.addressLine2, this.cityName, this.countrySubentity, this.postalZone,
				this.latitudeValue, this.longitudeValue, this.countryIso3166Alpha2Code, this.countryName, this.name, this.phoneNumber);
	}

	public Address buildAddressDTO()
	{
		return new Address(this.addressLine1, this.addressLine2, this.cityName, this.countrySubentity, this.postalZone,
				this.latitudeValue, this.longitudeValue, this.countryIso3166Alpha2Code, this.countryName, this.name, this.phoneNumber);
	}

	/**
	 * With postal zone.
	 * 
	 * @param newPostalZone
	 *           the postal zone
	 * @return the address builder
	 */
	public AddressBuilder withPostalZone(final String newPostalZone)
	{
		this.postalZone = newPostalZone;
		return this;
	}

	public AddressBuilder withLatitude(final Double newLatitude)
	{
		this.latitudeValue = newLatitude;
		return this;
	}

	public AddressBuilder withLongitude(final Double newLongitude)
	{
		this.longitudeValue = newLongitude;
		return this;
	}

	public AddressBuilder withCountryCode(final String countryCode)
	{
		this.countryIso3166Alpha2Code = countryCode;
		return this;
	}

}
