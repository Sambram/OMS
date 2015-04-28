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

import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.builder.SourcingMatrixBuilder;
import com.hybris.oms.service.sourcing.builder.SourcingMatrixRowBuilder;
import com.hybris.oms.service.sourcing.builder.SourcingMatrixTotalsRowBuilder;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.context.SourcingMatrix;

import java.util.Collections;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for {@link SourcingMatrix}.
 */
public class SourcingMatrixBuilderTest
{
	private static final String OL_ID = "ol";
	private static final String ATS_ID = "ats";
	private static final String LOC_ID = "loc";
	private static final String SKU = "SKU";

	private final SourcingLine LINE = new SourcingLine(SKU, 1, OL_ID);

	@Mock
	private SourcingMatrixRowBuilder sourcingMatrixRowBuilder;

	@Mock
	private SourcingMatrixTotalsRowBuilder sourcingMatrixTotalsRowBuilder;

	@InjectMocks
	private final SourcingMatrixBuilder builder = new SourcingMatrixBuilder();

	private final LocationInfo info1 = new LocationInfo(LOC_ID);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldRejectBlankAtsId()
	{
		this.builder.createSourcingMatrix(Collections.<SourcingLine>emptyList(), null, null, null);
	}

	@Test
	public void shouldCreateEmptyMatrix()
	{
		final SourcingMatrix matrix = this.builder.createSourcingMatrix(Collections.<SourcingLine>emptyList(), ATS_ID, null, null);
		Assertions.assertThat(matrix.getSourcingLocations(OL_ID, null)).isEmpty();
		Assert.assertTrue(matrix.isEmpty());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void shouldCallRowBuilder()
	{
		final Set<String> filterLocationIds = Collections.singleton(LOC_ID);
		final RadianCoordinates shipToCoordinates = new RadianCoordinates(22d, 11d);
		Mockito
				.when(sourcingMatrixRowBuilder.getMatrixRow(Mockito.eq(LINE), Mockito.eq(ATS_ID), Mockito.eq(filterLocationIds),
						Mockito.eq(shipToCoordinates), Mockito.any(AtsResult.class), Mockito.anyMapOf(String.class, LocationInfo.class)))
				.thenReturn(Collections.singletonList(new SourcingLocation(info1, 1)));
		Mockito.when(
				sourcingMatrixTotalsRowBuilder.calculateTotals(Mockito.eq(Collections.singletonList(LINE)), Mockito.anyMap(),
						Mockito.any(AtsResult.class), Mockito.anyMapOf(String.class, LocationInfo.class))).thenReturn(
				Collections.singletonList(new SourcingLocation(info1, 10)));
		final SourcingMatrix matrix = this.builder.createSourcingMatrix(Collections.singletonList(LINE), ATS_ID, filterLocationIds,
				shipToCoordinates);
		Assert.assertEquals(0, matrix.getAtsForLocationId(LINE, LOC_ID));
		Assert.assertEquals(1, matrix.getSourcingLocations(OL_ID, null).size());
		Assert.assertEquals(1, matrix.getSourcingLocations(OL_ID, null).get(0).getAts());
		Assert.assertEquals(1, matrix.getTotalSourcingLocations(null).size());
		Assert.assertEquals(10, matrix.getTotalSourcingLocations(null).get(0).getAts());
	}
}
