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

import com.hybris.oms.service.sourcing.builder.SourcingComparatorFactory;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.SourcingLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Integration test for {@link SourcingComparatorFactory}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class SourcingComparatorFactoryIntegrationTest
{

	@Autowired
	private SourcingComparatorFactory factory;

	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectMissingDescriptor()
	{
		this.factory.createComparator(" ");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectUnknownDescriptor()
	{
		this.factory.createComparator("NotFound");
	}

	@Test
	public void shouldSortByAts()
	{
		final Comparator<SourcingLocation> comparator = this.factory.createComparator("ATS");
		final List<SourcingLocation> locations = new ArrayList<>();
		locations.add(new SourcingLocation(new LocationInfo(1, "1", 0), 10));
		locations.add(new SourcingLocation(new LocationInfo(1, "2", 0), 20));
		Collections.sort(locations, comparator);
		Assert.assertEquals("Higher ATS ranked higher", 20, locations.get(0).getAts());
	}

	@Test
	public void shouldSortByDefaultIfAtsIsEqual()
	{
		final Comparator<SourcingLocation> comparator = this.factory.createComparator("ATS");
		final List<SourcingLocation> locations = new ArrayList<>();
		locations.add(new SourcingLocation(new LocationInfo(1, "2", 0), 10));
		locations.add(new SourcingLocation(new LocationInfo(1, "1", 0), 10));
		Collections.sort(locations, comparator);
		Assert.assertEquals("Equal ATS ranked by locationId", "1", locations.get(0).getInfo().getLocationId());
	}

	@Test
	public void shouldSortByDistance()
	{
		final Comparator<SourcingLocation> comparator = this.factory.createComparator("DISTANCE");
		final List<SourcingLocation> locations = new ArrayList<>();
		locations.add(new SourcingLocation(new LocationInfo(1, "1", 20d), 10));
		locations.add(new SourcingLocation(new LocationInfo(1, "2", 10d), 10));
		Collections.sort(locations, comparator);
		Assert.assertEquals("ranked by distance", "2", locations.get(0).getInfo().getLocationId());
	}

	@Test
	public void shouldSortByDefaultIfDistanceIsEqual()
	{
		final Comparator<SourcingLocation> comparator = this.factory.createComparator("DISTANCE");
		final List<SourcingLocation> locations = new ArrayList<>();
		locations.add(new SourcingLocation(new LocationInfo(1, "2", 10d), 10));
		locations.add(new SourcingLocation(new LocationInfo(1, "1", 10d), 10));
		Collections.sort(locations, comparator);
		Assert.assertEquals("Equal distance ranked by locationId", "1", locations.get(0).getInfo().getLocationId());
	}

	@Test
	public void shouldSortByPriority()
	{
		final Comparator<SourcingLocation> comparator = this.factory.createComparator("SEQUENCE");
		final List<SourcingLocation> locations = new ArrayList<>();
		locations.add(new SourcingLocation(new LocationInfo(2, "1", 10d), 10));
		locations.add(new SourcingLocation(new LocationInfo(1, "2", 10d), 10));
		Collections.sort(locations, comparator);
		Assert.assertEquals("ranked by priority", "2", locations.get(0).getInfo().getLocationId());
	}

	@Test
	public void shouldSortByDefaultIfPriorityIsEqual()
	{
		final Comparator<SourcingLocation> comparator = this.factory.createComparator("SEQUENCE");
		final List<SourcingLocation> locations = new ArrayList<>();
		locations.add(new SourcingLocation(new LocationInfo(1, "2", 10d), 10));
		locations.add(new SourcingLocation(new LocationInfo(1, "1", 10d), 10));
		Collections.sort(locations, comparator);
		Assert.assertEquals("ranked by locationId", "1", locations.get(0).getInfo().getLocationId());
	}

	@Test
	public void shouldChainSorting()
	{
		final Comparator<SourcingLocation> comparator = this.factory.createComparator("ATS,DISTANCE,SEQUENCE");
		final List<SourcingLocation> locations = new ArrayList<>();
		locations.add(new SourcingLocation(new LocationInfo(1, "4", 5d), 10));
		locations.add(new SourcingLocation(new LocationInfo(1, "3", 5d), 10));
		locations.add(new SourcingLocation(new LocationInfo(2, "2", 2d), 10));
		locations.add(new SourcingLocation(new LocationInfo(2, "1", 5d), 20));
		Collections.sort(locations, comparator);
		Assert.assertEquals("ranked by ats", "1", locations.get(0).getInfo().getLocationId());
		Assert.assertEquals("ranked by distance", "2", locations.get(1).getInfo().getLocationId());
		Assert.assertEquals("ranked by priority", "3", locations.get(2).getInfo().getLocationId());
		Assert.assertEquals("ranked by locationId", "4", locations.get(3).getInfo().getLocationId());
	}

}
