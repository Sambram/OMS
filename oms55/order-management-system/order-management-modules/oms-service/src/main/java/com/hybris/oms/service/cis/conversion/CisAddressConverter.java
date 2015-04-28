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
package com.hybris.oms.service.cis.conversion;

import com.hybris.cis.api.model.CisAddress;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CisAddressConverter
{
	private static final Logger LOG = LoggerFactory.getLogger(CisAddressConverter.class);

	public CisAddress convertOmsAddressToCisAddress(final AddressVT address)
	{
		final CisAddress cisAddress = new CisAddress();
		cisAddress.setAddressLine1(address.getAddressLine1());
		cisAddress.setAddressLine2(address.getAddressLine2());
		cisAddress.setCity(address.getCityName());
		cisAddress.setCountry(address.getCountryIso3166Alpha2Code());
		cisAddress.setState(address.getCountrySubentity());
		cisAddress.setZipCode(address.getPostalZone());
		return cisAddress;
	}

	public Double[] convertCisAddressToOmsLocationCoordinate(final CisAddress cisAddress)
	{

		final Double[] result = new Double[2];

		try
		{
			result[0] = Double.parseDouble(cisAddress.getLatitude());
			result[1] = Double.parseDouble(cisAddress.getLongitude());
		}
		catch (final NumberFormatException e)
		{
			LOG.warn("Could not parse CIS geocodes to double. Latitude: {}; Longitude: {}.", cisAddress.getLatitude(),
					cisAddress.getLongitude());
			result[0] = null;
			result[1] = null;
		}

		return result;
	}

}
