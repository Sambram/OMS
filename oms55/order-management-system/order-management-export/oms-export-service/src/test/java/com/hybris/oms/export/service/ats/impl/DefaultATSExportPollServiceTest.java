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
package com.hybris.oms.export.service.ats.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.oms.export.service.ats.SkuToAtsRows;
import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;
import com.hybris.oms.export.service.manager.ExportManager;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsResult.AtsRow;
import com.hybris.oms.service.ats.AtsResult.Key;
import com.hybris.oms.service.ats.AtsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;


@RunWith(MockitoJUnitRunner.class)
public class DefaultATSExportPollServiceTest
{
	private static final String ATS_ID = "WEB";
	private static final String LOCATION_ID = "location";
	private static final long POLLING_TIME = new Date().getTime();

	@InjectMocks
	private final DefaultATSExportPollService pollService = new DefaultATSExportPollService();

	@Mock
	private AtsService atsService;
	@Mock
	private ExportManager exportManager;
	@Mock
	private PlatformTransactionManager transactionManager;

	@Test
	public void testPollWithEmptyQueue()
	{
		final Collection<SkuToAtsRows> result = this.pollService.pollChanges(-1, POLLING_TIME, ATS_ID);
		assertThat(result, is(notNullValue()));
		assertThat(result.isEmpty(), is(true));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPollWithAts()
	{
		final Set<ExportSkus> skus = generateSkuSet(0, 30);
		when(exportManager.findSkusMarkedForExport(-1, POLLING_TIME)).thenReturn(skus);
		when(this.atsService.getGlobalAts(anySet(), anySet())).thenReturn(new AtsResult());
		when(this.atsService.getLocalAts(anySet(), anySet(), anySet())).thenReturn(new AtsResult());

		final Collection<SkuToAtsRows> result = this.pollService.pollChanges(-1, POLLING_TIME, ATS_ID);

		assertThat(result, is(notNullValue()));
		assertThat(result.size(), equalTo(30));

	}


	@SuppressWarnings("unchecked")
	@Test
	public void testPollWithPagingAndCustomLimit()
	{
		final Set<ExportSkus> skus1 = generateSkuSet(0, 1);
		final Set<ExportSkus> skus2 = generateSkuSet(1, 2);
		final Set<ExportSkus> skus3 = generateSkuSet(2, 2);
		when(exportManager.findSkusMarkedForExport(1, POLLING_TIME)).thenReturn(skus1).thenReturn(skus2).thenReturn(skus3);
		when(this.atsService.getGlobalAts(anySet(), anySet())).thenReturn(new AtsResult());
		when(this.atsService.getLocalAts(anySet(), anySet(), anySet())).thenReturn(new AtsResult());

		Collection<SkuToAtsRows> result = this.pollService.pollChanges(1, POLLING_TIME, ATS_ID);

		assertThat(result, is(notNullValue()));
		assertThat(result.size(), equalTo(1));

		result = this.pollService.pollChanges(1, POLLING_TIME, ATS_ID);

		assertThat(result, is(notNullValue()));
		assertThat(result.size(), equalTo(1));

		result = this.pollService.pollChanges(1, POLLING_TIME, ATS_ID);

		assertThat(result, is(notNullValue()));
		assertThat(result.isEmpty(), is(true));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPollWithDefaultLimit()
	{
		final Set<ExportSkus> skus1 = generateSkuSet(0, 2);
		final Set<ExportSkus> skus2 = generateSkuSet(2, 2);
		when(exportManager.findSkusMarkedForExport(-1, POLLING_TIME)).thenReturn(skus1).thenReturn(skus2);
		when(this.atsService.getGlobalAts(anySet(), anySet())).thenReturn(new AtsResult());
		when(this.atsService.getLocalAts(anySet(), anySet(), anySet())).thenReturn(new AtsResult());

		Collection<SkuToAtsRows> result = this.pollService.pollChanges(-1, POLLING_TIME, ATS_ID);

		assertThat(result, is(notNullValue()));
		assertThat(result.size(), equalTo(2));

		result = this.pollService.pollChanges(-1, POLLING_TIME, ATS_ID);

		assertThat(result, is(notNullValue()));
		assertThat(result.isEmpty(), is(true));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPollWithEmptyFilters()
	{
		final Set<ExportSkus> skus1 = generateSkuSet(0, 1);
		final Set<ExportSkus> skus2 = generateSkuSet(1, 2);
		when(exportManager.findSkusMarkedForExport(1, POLLING_TIME)).thenReturn(skus1).thenReturn(skus2);
		when(this.atsService.getGlobalAts(anySet(), anySet())).thenReturn(new AtsResult());
		when(this.atsService.getLocalAts(anySet(), anySet(), anySet())).thenReturn(new AtsResult());

		// if formulas is empty the getLocalAts method should be called (now for the second time) with null
		this.pollService.pollChanges(1, POLLING_TIME, ATS_ID);

		// if formulas is null the getLocalAts method should be called with null
		this.pollService.pollChanges(1, POLLING_TIME, ATS_ID);

		verify(atsService, times(2)).getLocalAts(anySet(), anySet(), anySet());
	}

	@Test
	public void testAddAtsRows()
	{

		final Collection<AtsRow> atsRows = new ArrayList<>();
		atsRows.add(new AtsRow(new Key("sku1", "ats1", "loc1", POLLING_TIME), 100));
		atsRows.add(new AtsRow(new Key("sku1", "ats1", "loc1", POLLING_TIME), 100));
		atsRows.add(new AtsRow(new Key("sku1", "ats1", "loc1", POLLING_TIME), 100));
		atsRows.add(new AtsRow(new Key("sku1", "ats1", "loc1", POLLING_TIME), 100));
		atsRows.add(new AtsRow(new Key("sku2", "ats1", "loc1", POLLING_TIME), 100));
		atsRows.add(new AtsRow(new Key("sku2", "ats1", "loc1", POLLING_TIME), 100));

		final Map<String, SkuToAtsRows> skuToATSRowsMap = new HashMap<>();
		skuToATSRowsMap.put("sku1", new SkuToAtsRows("sku1"));
		skuToATSRowsMap.put("sku2", new SkuToAtsRows("sku2"));

		this.pollService.addAtsRows(skuToATSRowsMap, atsRows);
		final Collection<SkuToAtsRows> skuToAtsRows = skuToATSRowsMap.values();

		assertThat(skuToAtsRows.size(), equalTo(2));

		final Iterator<SkuToAtsRows> it = skuToAtsRows.iterator();
		final SkuToAtsRows s1 = it.next();
		final SkuToAtsRows s2 = it.next();

		final Collection<AtsRow> aRows1 = s1.getAtsRows();
		for (final AtsRow ar : aRows1)
		{
			assertThat(ar.getKey().getSku(), equalTo(s1.getSku()));
		}

		final Collection<AtsRow> aRows2 = s2.getAtsRows();
		for (final AtsRow ar : aRows2)
		{
			assertThat(ar.getKey().getSku(), equalTo(s2.getSku()));
		}

		if ("sku1".equals(s1.getSku()))
		{
			assertThat(aRows1.size(), equalTo(4));
			assertThat(aRows2.size(), equalTo(2));
		}
		else
		{
			assertThat(aRows1.size(), equalTo(2));
			assertThat(aRows2.size(), equalTo(4));
		}

	}

	private Set<ExportSkus> generateSkuSet(final int startIndex, final int endIndex)
	{
		final Set<ExportSkus> skus = new LinkedHashSet<>();
		for (int i = startIndex; i < endIndex; i++)
		{
			final ExportSkus exportSku = new ExportSkusPojo();
			exportSku.setSku(String.valueOf(i));
			exportSku.setLocationId(LOCATION_ID);
			exportSku.setLatestChange(new Date().getTime());

			skus.add(exportSku);
		}
		return skus;
	}
}
