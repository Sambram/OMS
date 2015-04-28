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
package com.hybris.oms.rest.web.shipment;

import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.shipping.Shipment;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class ShipmentConfirmTest
{
	private static final String SHIPMENT_ID = "1234";

	@Mock
	private Shipment shipment;
	@Mock
	private ShipmentFacade shipmentServiceApi;
	@InjectMocks
	private ShipmentConfirmResource shipmentsConfirm;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(this.shipmentServiceApi.confirmShipment(Mockito.anyString())).thenReturn(this.shipment);
	}

	@Test
	public void testShipmentPick()
	{
		// call tested method
		final Response response = this.shipmentsConfirm.confirmShipment(SHIPMENT_ID);
		// verify if update shipment status method in ShipmentService was called
		Mockito.verify(this.shipmentServiceApi).confirmShipment(SHIPMENT_ID);
		// verify if response was OK(200).
		Assert.assertEquals(Response.ok().build().getStatus(), response.getStatus());
	}
}
