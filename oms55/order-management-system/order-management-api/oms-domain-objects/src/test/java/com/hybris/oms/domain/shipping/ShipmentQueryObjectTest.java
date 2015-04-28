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
package com.hybris.oms.domain.shipping;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ShipmentQueryObjectTest
{

	private ShipmentQueryObject shipmentQueryObject;

	@Before
	public void setUp()
	{
		this.shipmentQueryObject = new ShipmentQueryObject();
	}

	@Test
	public void setOrderIdTest()
	{
		final List<String> orderIds = new ArrayList<String>();
		orderIds.add("9999|5555");
		this.shipmentQueryObject.setOrderIds(orderIds);

		Assert.assertEquals(1, this.shipmentQueryObject.getOrderIds().size());
	}

	@Test
	public void setShipmentStatusIdsTest()
	{
		final List<String> statusIds = new ArrayList<String>();
		statusIds.add("1");
		statusIds.add("2");
		statusIds.add("3");
		this.shipmentQueryObject.setShipmentStatusIds(statusIds);

		Assert.assertEquals(3, this.shipmentQueryObject.getShipmentStatusIds().size());
	}

	@Test
	public void setLocationIdsTest()
	{
		final List<String> locationIds = new ArrayList<>();
		locationIds.add("1");
		locationIds.add("5");
		this.shipmentQueryObject.setLocationIds(locationIds);

		Assert.assertEquals(2, this.shipmentQueryObject.getLocationIds().size());
	}
}
