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
package com.hybris.oms.export.facade.ats;

import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.export.api.ats.AvailabilityToSellQuantityDTO;
import com.hybris.oms.export.service.ats.ATSExportPollService;
import com.hybris.oms.export.service.ats.ATSExportTriggerService;
import com.hybris.oms.export.service.ats.SkuToAtsRows;
import com.hybris.oms.service.ats.AtsResult.AtsRow;
import com.hybris.oms.service.ats.AtsResult.Key;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class DefaultATSExportFacadeTest
{
	private DefaultATSExportFacade facade;

	@Mock
	private ATSExportTriggerService triggerService;

	@Mock
	private ATSExportPollService pollService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		this.facade = new DefaultATSExportFacade();
		this.facade.setAtsExportTriggerService(this.triggerService);
		this.facade.setAtsExportPollService(this.pollService);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTriggerExportWithNull()
	{
		this.facade.triggerExport(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTriggerExportWithEmpty()
	{
		this.facade.triggerExport("", "");
	}

	@Test
	public void testPollChangesEmpty()
	{
		Mockito.when(this.pollService.pollChanges(Mockito.anyInt(), Mockito.anyLong(), Mockito.anyString())).thenReturn(
				Collections.<SkuToAtsRows>emptyList());

		final AvailabilityToSellDTO result = this.facade.pollChanges(10, 1L, "");
		Assert.assertNotNull(result);
		Assert.assertEquals(result.getQuantities().size(), 0);

		Mockito.verify(this.pollService, Mockito.times(1)).pollChanges(10, 1L, "");
	}

	@Test
	public void testPollChanges()
	{

		final SkuToAtsRows ats1 = new SkuToAtsRows("sku1");
		ats1.addAtsRow(new AtsRow(new Key("sku1", "atsId1", "loc1", (new Date()).getTime()), 10));

		final SkuToAtsRows ats2 = new SkuToAtsRows("sku2");
		ats2.addAtsRow(new AtsRow(new Key("sku2", "atsId1", "loc1", (new Date()).getTime()), 34));

		Mockito.when(this.pollService.pollChanges(Mockito.anyInt(), Mockito.anyLong(), Mockito.anyString())).thenReturn(
				Arrays.asList(new SkuToAtsRows[]{ats1, ats2}));

		final AvailabilityToSellDTO result = this.facade.pollChanges(10, 1L, "");
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.getQuantities().size());

		final Iterator<AvailabilityToSellQuantityDTO> atsQuantityResult1Iter = result.getQuantities().iterator();
		final AvailabilityToSellQuantityDTO atsQuantityResult11 = atsQuantityResult1Iter.next();
		this.assertQuantity(atsQuantityResult11, "sku1", "loc1", 10);

		final AvailabilityToSellQuantityDTO atsQuantityResult21 = atsQuantityResult1Iter.next();
		this.assertQuantity(atsQuantityResult21, "sku2", "loc1", 34);

		Mockito.verify(this.pollService, Mockito.times(1)).pollChanges(10, 1L, "");
	}

	private void assertQuantity(final AvailabilityToSellQuantityDTO quantity, final String skuId, final String loc, final int qty)
	{
		Assert.assertEquals(skuId, quantity.getSkuId());
		Assert.assertEquals(loc, quantity.getLocationId());
		Assert.assertEquals(qty, quantity.getQuantity());
	}

}
