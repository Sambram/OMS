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
 
package com.hybris.oms.service.managedobjects.types;


/**
 * Generated valuetype class for type AddressVT first defined at extension <code>valuetypes</code>.
 * <p/>
 * Describes an address.
 */
@SuppressWarnings("serial")
public class AddressVT implements java.io.Serializable
{
	/**<i>Generated value type code constant.</i>*/
	public String _TYPECODE = "AddressVT";

	
	private final String addressLine1;
	private final String addressLine2;
	private final String cityName;
	private final String countrySubentity;
	private final String postalZone;
	private final Double latitudeValue;
	private final Double longitudeValue;
	private final String countryIso3166Alpha2Code;
	private final String countryName;
	private final String name;
	private final String phoneNumber;


	public AddressVT(final String addressLine1, final String addressLine2, final String cityName, final String countrySubentity, final String postalZone, final Double latitudeValue, final Double longitudeValue, final String countryIso3166Alpha2Code, final String countryName, final String name, final String phoneNumber) {
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.cityName = cityName;
		this.countrySubentity = countrySubentity;
		this.postalZone = postalZone;
		this.latitudeValue = latitudeValue;
		this.longitudeValue = longitudeValue;
		this.countryIso3166Alpha2Code = countryIso3166Alpha2Code;
		this.countryName = countryName;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.addressLine1</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return this.addressLine1;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.addressLine2</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return this.addressLine2;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.cityName</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the cityName
	 */
	public String getCityName() {
		return this.cityName;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.countrySubentity</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the countrySubentity
	 */
	public String getCountrySubentity() {
		return this.countrySubentity;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.postalZone</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the postalZone
	 */
	public String getPostalZone() {
		return this.postalZone;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.latitudeValue</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the latitudeValue
	 */
	public Double getLatitudeValue() {
		return this.latitudeValue;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.longitudeValue</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the longitudeValue
	 */
	public Double getLongitudeValue() {
		return this.longitudeValue;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.countryIso3166Alpha2Code</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the countryIso3166Alpha2Code
	 */
	public String getCountryIso3166Alpha2Code() {
		return this.countryIso3166Alpha2Code;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.countryName</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the countryName
	 */
	public String getCountryName() {
		return this.countryName;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.name</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

		
	/**
	 * <i>Generated method</i> - Getter of the <code>AddressVT.phoneNumber</code> attribute defined at extension <code>valuetypes</code>.
	 * <p/>
	 * 
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	

	/**
	 * Generated hashCode() method
	 */
	@Override
	public int hashCode()
	{
	return com.google.common.base.Objects.hashCode(
		this.addressLine1, this.addressLine2, this.cityName, this.countrySubentity, this.postalZone, this.latitudeValue, this.longitudeValue, this.countryIso3166Alpha2Code, this.countryName, this.name, this.phoneNumber);
	}
	
	/**
	 * Generated equals() method
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}

		final AddressVT other = (AddressVT) obj;
		
		return com.google.common.base.Objects.equal(this.addressLine1, other.addressLine1)
			&& com.google.common.base.Objects.equal(this.addressLine2, other.addressLine2)
			&& com.google.common.base.Objects.equal(this.cityName, other.cityName)
			&& com.google.common.base.Objects.equal(this.countrySubentity, other.countrySubentity)
			&& com.google.common.base.Objects.equal(this.postalZone, other.postalZone)
			&& com.google.common.base.Objects.equal(this.latitudeValue, other.latitudeValue)
			&& com.google.common.base.Objects.equal(this.longitudeValue, other.longitudeValue)
			&& com.google.common.base.Objects.equal(this.countryIso3166Alpha2Code, other.countryIso3166Alpha2Code)
			&& com.google.common.base.Objects.equal(this.countryName, other.countryName)
			&& com.google.common.base.Objects.equal(this.name, other.name)
			&& com.google.common.base.Objects.equal(this.phoneNumber, other.phoneNumber);
	}
	
	/**
	 * Generated toString() method
	 */
	@Override
	public String toString()
	{
		return com.google.common.base.Objects.toStringHelper(this)
			.addValue(this.addressLine1) //
			.addValue(this.addressLine2) //
			.addValue(this.cityName) //
			.addValue(this.countrySubentity) //
			.addValue(this.postalZone) //
			.addValue(this.latitudeValue) //
			.addValue(this.longitudeValue) //
			.addValue(this.countryIso3166Alpha2Code) //
			.addValue(this.countryName) //
			.addValue(this.name) //
			.addValue(this.phoneNumber) //
			.toString();
	}
}