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
package com.hybris.oms.rest.web.source;

import com.hybris.oms.api.fulfillment.SourceSimulationFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.SkuQuantity;
import com.hybris.oms.domain.order.jaxb.SourceSimulationParameter;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.rest.web.sourcing.SourcingResource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class SourceSimulationTest
{
	private static final String ATSID = "WEB";

	@Mock
	private SourceSimulationFacade sourcingSimulationServices;

	@InjectMocks
	private SourcingResource sourceSimulation;

	private SourceSimulationParameter sourceSimulationParameter;
	private final Address address = new Address();
	private final Location location = new Location();

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final String sku = "23213";
		this.sourceSimulationParameter = new SourceSimulationParameter();
		final SkuQuantity skuQuantity = new SkuQuantity(sku, new Quantity("units", 5));
		final List<SkuQuantity> skuQuantityList = new ArrayList<SkuQuantity>();
		skuQuantityList.add(skuQuantity);
		this.sourceSimulationParameter.setSkuQuantities(skuQuantityList);
		this.location.setActive(true);

		Mockito.when(this.sourcingSimulationServices.findBestSourcingLocation(Mockito.any(SourceSimulationParameter.class)))
				.thenReturn(this.location);
	}

	@Test
	public void testSourceSimulationAtsId()
	{
		this.sourceSimulationParameter.setAtsId(ATSID);
		final Response response = this.sourceSimulation.sourceBestLocation(this.sourceSimulationParameter);

		Assert.assertEquals(Response.ok().build().getStatus(), response.getStatus());
	}

	@Test
	public void testSourceSimulationByAddress()
	{
		this.sourceSimulationParameter.setAddress(this.address);
		final Response response = this.sourceSimulation.sourceBestLocation(this.sourceSimulationParameter);

		Assert.assertEquals(Response.ok().build().getStatus(), response.getStatus());
	}

	@Test
	public void testSourceSimulationBySequence()
	{
		final Response response = this.sourceSimulation.sourceBestLocation(this.sourceSimulationParameter);

		Assert.assertEquals(Response.ok().build().getStatus(), response.getStatus());
	}
}
