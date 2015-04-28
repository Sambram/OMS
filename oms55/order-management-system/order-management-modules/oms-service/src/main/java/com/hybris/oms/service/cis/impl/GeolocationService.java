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
package com.hybris.oms.service.cis.impl;

import com.hybris.cis.api.geolocation.model.CisLocationRequest;
import com.hybris.cis.api.geolocation.model.GeoLocationResult;
import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.client.rest.geolocation.GeolocationClient;
import com.hybris.commons.client.RestResponse;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.remote.exception.InvalidGeolocationResponseException;
import com.hybris.oms.service.cis.CisConverter;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


public class GeolocationService
{
	private static final Logger LOG = LoggerFactory.getLogger(GeolocationService.class);

	private GeolocationClient geoLocationClient;

	private CisConverter cisConverter;

	/**
	 * Geocode the address. (Convert address to latitude/longitude).
	 * Sets the geo-codes into the address.
	 * 
	 * @param address
	 * @return new address with geocodes
	 * @throws RemoteRequestException
	 * @throws InvalidGeolocationResponseException
	 */
	public AddressVT geocodeAddress(final AddressVT address)
	{
		if (LOG.isInfoEnabled())
		{
			LOG.info("Start geo location");
			LOG.info(address.getCityName() + " | " + address.getAddressLine1() + " | " + address.getCountrySubentity());
		}

		final CisLocationRequest cisLocationRequest = convertToCisParamFormat(address);

		final RestResponse<GeoLocationResult> result = callCis(cisLocationRequest);

		final Double[] coordinates = convertToOMSParamFormat(result);

		// Return the new address with geocodes
		return setGeocodesInTheNewAddress(address, coordinates[0], coordinates[1]);
	}

	protected RestResponse<GeoLocationResult> callCis(final CisLocationRequest cisLocationRequest)
	{
		RestResponse<GeoLocationResult> result = null;
		try
		{
			result = this.geoLocationClient.getGeolocation("Not sent for any third party", cisLocationRequest);
		}
		catch (final RuntimeException e)
		{
			throw new RemoteRequestException("An error occurred during communication with CIS :" + e.getMessage(), e);
		}
		return result;
	}

	protected CisLocationRequest convertToCisParamFormat(final AddressVT address)
	{
		return this.cisConverter.convertOmsAddressToCisLocationRequest(address);
	}

	protected Double[] convertToOMSParamFormat(final RestResponse<GeoLocationResult> result)
	{
		if (CollectionUtils.isEmpty(result.getResult().getGeoLocations()))
		{
			throw new InvalidGeolocationResponseException("CIS did not return any geo-location addresses.");
		}

		if (LOG.isInfoEnabled())
		{
			LOG.info("CIS Latitude Result : " + result.getResult().getGeoLocations().get(0).getLatitude());
			LOG.info("CIS Longitude Result : " + result.getResult().getGeoLocations().get(0).getLongitude());
		}

		final CisAddress cisAddress = result.getResult().getGeoLocations().get(0);
		return this.cisConverter.convertCisAddressToOmsLocationCoordinates(cisAddress);
	}

	protected AddressVT setGeocodesInTheNewAddress(final AddressVT address, final Double latitude, final Double longitude)
	{
		final AddressVT newAddress = new AddressVT(address.getAddressLine1(), address.getAddressLine2(), address.getCityName(),
				address.getCountrySubentity(), address.getPostalZone(), latitude, longitude, address.getCountryIso3166Alpha2Code(),
				address.getCountryName(), address.getName(), address.getPhoneNumber());
		return newAddress;
	}

	protected CisConverter getCisConverter()
	{
		return cisConverter;
	}

	protected GeolocationClient getGeoLocationClient()
	{
		return geoLocationClient;
	}

	@Required
	public void setCisConverter(final CisConverter cisConverter)
	{
		this.cisConverter = cisConverter;
	}

	@Required
	public void setGeoLocationClient(final GeolocationClient geoLocationClient)
	{
		this.geoLocationClient = geoLocationClient;
	}

}
