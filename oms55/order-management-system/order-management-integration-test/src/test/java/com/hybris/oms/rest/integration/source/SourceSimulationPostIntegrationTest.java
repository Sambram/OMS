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
package com.hybris.oms.rest.integration.source;

import com.hybris.oms.api.fulfillment.SourceSimulationFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.SkuQuantity;
import com.hybris.oms.domain.order.jaxb.SourceSimulationParameter;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.rest.integration.test.RestClientIntegrationTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



public class SourceSimulationPostIntegrationTest extends RestClientIntegrationTest
{
	private static final String ATS_ID = "WEB";

	@Autowired
	private SourceSimulationFacade sourceSimulationFacade;

	private OmsInventory inventory;

	private final Address shippingAddress = new Address();

	@Before
	public void setUp()
	{
		inventory = createInventory();
	}

	@Test
	public void testFindBestSourcingLocationByAts()
	{

		final List<SkuQuantity> skuQuantityList = this.buildSkuQuantityList();
		final SourceSimulationParameter sourceSimulationParameter = this.createSourceSimulationParameter(skuQuantityList, ATS_ID,
				shippingAddress, null);
		final Location location = this.sourceSimulationFacade.findBestSourcingLocation(sourceSimulationParameter);
		Assert.assertNotNull(location);
	}

	@Test
	public void testFindBestSourcingLocationBySequence()
	{

		final List<SkuQuantity> skuQuantityList = this.buildSkuQuantityList();
		final SourceSimulationParameter sourceSimulationParameter = this.createSourceSimulationParameter(skuQuantityList, null,
				shippingAddress, null);
		final Location location = this.sourceSimulationFacade.findBestSourcingLocation(sourceSimulationParameter);
		Assert.assertNotNull(location);
	}

	private SourceSimulationParameter createSourceSimulationParameter(final List<SkuQuantity> skuQuantities, final String atsId,
			final Address address, final List<String> locationIds)
	{

		final SourceSimulationParameter sourceSimulationParameter = new SourceSimulationParameter();
		sourceSimulationParameter.setSkuQuantities(skuQuantities);
		sourceSimulationParameter.setAtsId(atsId);
		sourceSimulationParameter.setAddress(address);
		sourceSimulationParameter.setLocationIds(locationIds);
		return sourceSimulationParameter;

	}

	@Test
	public void testFindBestSourcingLocationByDistance()
	{
		final List<SkuQuantity> skuQuantityList = this.buildSkuQuantityList();

		final Address address = new Address();
		address.setCityName("Ottawa");
		address.setCountryIso3166Alpha2Code("CA");
		address.setCountrySubentity("ON");
		address.setPostalZone("K1A 0B1");

		address.setLatitudeValue(10d);
		address.setLongitudeValue(10d);
		final SourceSimulationParameter sourceSimulationParameter = this.createSourceSimulationParameter(skuQuantityList, null,
				address, null);
		final Location location = this.sourceSimulationFacade.findBestSourcingLocation(sourceSimulationParameter);
		Assert.assertNotNull(location);
	}

	private List<SkuQuantity> buildSkuQuantityList()
	{
		final List<SkuQuantity> skuQuantityList = new ArrayList<SkuQuantity>();
		final SkuQuantity skuQuantity = new SkuQuantity();
		skuQuantity.setQuantity(new Quantity("units", 20));
		skuQuantity.setSku(inventory.getSkuId());
		skuQuantityList.add(skuQuantity);

		return skuQuantityList;
	}
}
