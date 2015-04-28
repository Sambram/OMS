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
package com.hybris.oms.service.common;

import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AddressStaticUtilsTest
{
	private AddressVT address1;

	@Before
	public final void setUp()
	{
		this.address1 = new AddressVT(null, null, null, null, null, null, null, null, null, null, null);
	}

	@Test
	public final void testHasValidCoordinates()
	{
		Assert.assertFalse(AddressStaticUtils.hasValidCoordinates(this.address1));

		Assert.assertTrue(AddressStaticUtils.hasValidCoordinates(this.cloneAddress(this.address1, 50.0, 50.0)));
	}

	private AddressVT cloneAddress(final AddressVT address, final Double latitude, final Double longitude)
	{
		return new AddressVT(address.getAddressLine1(), address.getAddressLine2(), address.getCityName(),
				address.getCountrySubentity(), address.getPostalZone(), latitude, longitude, address.getCountryIso3166Alpha2Code(),
				address.getCountryName(), address.getName(), address.getPhoneNumber());
	}
}
