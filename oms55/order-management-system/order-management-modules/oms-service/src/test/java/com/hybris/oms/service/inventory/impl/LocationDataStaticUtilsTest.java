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
package com.hybris.oms.service.inventory.impl;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class LocationDataStaticUtilsTest
{
	@Autowired
	private PersistenceManager persistenceManager;

	/**
	 * Test has valid coordinates.
	 */
	@Test
	@Transactional
	public void testHasValidCoordinates()
	{
		final StockroomLocationData location = this.persistenceManager.create(StockroomLocationData.class);
		Assert.assertFalse(LocationDataStaticUtils.hasValidCoordinates(location));
		final AddressVT address = new AddressVT(null, null, null, null, null, 50.0, 50.0, null, null, null, null);
		location.setAddress(address);
		Assert.assertTrue(LocationDataStaticUtils.hasValidCoordinates(location));
	}
}
