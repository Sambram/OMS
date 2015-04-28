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
package com.hybris.oms.rest.web.inventory;

import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.domain.inventory.Bin;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class BinTest
{

	@Mock
	private InventoryFacade inventoryServiceApi;
	@InjectMocks
	private BinResource binResource;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testDeleteBin()
	{
		// call tested method
		final Response response = this.binResource.deleteBin("bin100", "loc100");

		Assert.assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

		// verify if façade was called
		Mockito.verify(this.inventoryServiceApi).deleteBinByBinCodeLocationId(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testGetBin()
	{
		// call tested method
		final Response response = this.binResource.getBin("bin100", "loc100");

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

		// verify if façade was called
		Mockito.verify(this.inventoryServiceApi).getBinByBinCodeLocationId(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testUpdateBin()
	{
		final Bin bin = new Bin();
		bin.setBinCode("bin100");

		Mockito.when(inventoryServiceApi.updateBin(bin)).thenReturn(bin);

		// call tested method
		final Response response = this.binResource.updateBin("bin100", "location1", bin);

		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(bin, response.getEntity());

		// verify if façade was called
		Mockito.verify(this.inventoryServiceApi).updateBin(bin);
	}
}
