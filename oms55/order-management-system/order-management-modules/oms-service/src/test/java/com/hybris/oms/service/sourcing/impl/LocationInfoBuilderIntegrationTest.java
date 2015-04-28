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
package com.hybris.oms.service.sourcing.impl;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.builder.LocationInfoBuilder;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.PropertiesBuilder;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


/**
 * Integration test for {@link LocationInfoBuilder}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class LocationInfoBuilderIntegrationTest
{
	private static final String LOC_ID = "loc1";

	private static final String ATTRIBUTE_NAME = "color";
	private static final String ATTRIBUTE_VALUE = "blue";

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private LocationInfoBuilder builder;

	@Autowired
	@Qualifier("schemalessPropertiesBuilder")
	private PropertiesBuilder<StockroomLocationData> propertiesBuilder;

	private StockroomLocationData location;

	@Before
	public void setUp()
	{
		this.location = this.persistenceManager.create(StockroomLocationData.class);
		this.location.setLocationId(LOC_ID);
		this.location.setPriority(4);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		final AddressVT address = new AddressVT(null, null, null, null, null, 77d, 0d, null, null, null, null);
		this.location.setAddress(address);
		this.location.setProperty(ATTRIBUTE_NAME, ATTRIBUTE_VALUE);
		this.persistenceManager.flush();
		builder.setPropertiesBuilders(Collections.singletonList(propertiesBuilder));
	}

	@Test
	@Transactional
	public void shouldIgnoreNullLocationCoords()
	{
		assertValidInfo(this.builder.build(location, null), Double.MAX_VALUE);
	}

	@Test
	@Transactional
	public void shouldReturnDistanceZero()
	{
		assertValidInfo(this.builder.build(location, RadianCoordinates.fromDegrees(this.location.getAddress().getLatitudeValue(),
				this.location.getAddress().getLongitudeValue())), 0d);
	}

	@Test
	@Transactional
	public void shouldReturnDistance()
	{
		final Double latitude = this.location.getAddress().getLatitudeValue();
		assertValidInfo(this.builder.build(location, RadianCoordinates.fromDegrees(Double.valueOf(latitude.doubleValue() + 1),
				this.location.getAddress().getLongitudeValue())), 111.226d);
	}

	private void assertValidInfo(final LocationInfo info, final double distance)
	{
		Assert.assertEquals(LOC_ID, info.getLocationId());
		Assert.assertEquals("valid locationId", LOC_ID, info.getLocationId());
		Assert.assertEquals("valid priority", this.location.getPriority(), info.getPriority());
		Assert.assertEquals("valid distance", distance, info.getDistance(), 0.001d);
		Assert.assertEquals("valid schemaless attribute", info.getProperties().get(ATTRIBUTE_NAME), ATTRIBUTE_VALUE);
	}

}
