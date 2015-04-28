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
package com.hybris.oms.facade.validation.impl.inventory;

import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.facade.validation.ValidationContext;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;


public class LocationValidatorTest
{
	private static final String LOCATION_ID = "locationId";
	private final LocationValidator locationValidator = new LocationValidator();

	@Test
	public void testLocationRolesNull()
	{
		final ValidationContext context = new ValidationContext();
		final Location location = new Location(LOCATION_ID);
		locationValidator.validate("Location", context, location);
		Assert.assertEquals(0, context.getFailureCount());
	}

	@Test
	public void testNullLocationId()
	{
		final ValidationContext context = new ValidationContext();
		final Location location = new Location();
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		locationValidator.validate("Location", context, location);
		Assert.assertEquals(1, context.getFailureCount());
		Assert.assertEquals("Location.locationId", context.getFailures().get(0).getFieldName());
	}

	@Test
	public void testPercentageInventoryThresholdTooBig()
	{
		final ValidationContext context = new ValidationContext();
		final Location location = new Location(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setPercentageInventoryThreshold(120);
		locationValidator.validate("Location", context, location);
		Assert.assertEquals(1, context.getFailureCount());
		Assert.assertEquals("Location.percentageInventoryThreshold", context.getFailures().get(0).getFieldName());
	}

	@Test
	public void testPercentageInventoryThresholdTooSmall()
	{
		final ValidationContext context = new ValidationContext();
		final Location location = new Location(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setPercentageInventoryThreshold(-1);
		locationValidator.validate("Location", context, location);
		Assert.assertEquals(1, context.getFailureCount());
		Assert.assertEquals("Location.percentageInventoryThreshold", context.getFailures().get(0).getFieldName());
	}

	@Test
	public void testAbsoluteInventoryThresholdNegative()
	{
		final ValidationContext context = new ValidationContext();
		final Location location = new Location(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setAbsoluteInventoryThreshold(-1);
		locationValidator.validate("Location", context, location);
		Assert.assertEquals(1, context.getFailureCount());
		Assert.assertEquals("Location.absoluteInventoryThreshold", context.getFailures().get(0).getFieldName());
	}

	@Test
	public void testAllWrong()
	{
		final ValidationContext context = new ValidationContext();
		final Location location = new Location();
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setPercentageInventoryThreshold(-1);
		locationValidator.validate("Location", context, location);
		Assert.assertEquals(2, context.getFailureCount());
		Assert.assertEquals("Location.locationId", context.getFailures().get(0).getFieldName());
		Assert.assertEquals("Location.percentageInventoryThreshold", context.getFailures().get(1).getFieldName());
	}

	@Test
	public void testAllGood()
	{
		final ValidationContext context = new ValidationContext();
		final Location location = new Location(LOCATION_ID);
		location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING));
		location.setPercentageInventoryThreshold(0);
		locationValidator.validate("Location", context, location);
		Assert.assertEquals(0, context.getFailureCount());

		location.setPercentageInventoryThreshold(100);
		locationValidator.validate("Location", context, location);
		Assert.assertEquals(0, context.getFailureCount());
	}

}
