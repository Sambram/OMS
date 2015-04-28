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
package com.hybris.oms.export.service.manager.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.Pageable;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.services.query.QueryPageImpl;
import com.hybris.oms.export.service.ats.impl.ExportSkusPojo;
import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class JdbcExportManagerTest
{

	private static final String SKU = "sku";
	private static final String LOCATION = "location";

	@InjectMocks
	private final JdbcExportManager exportManager = new JdbcExportManager();

	@Mock
	private PersistenceManager persistenceManager;
	@Mock
	private CriteriaQuery<ExportSkus> exportSkusQueryBuilder;

	@SuppressWarnings("deprecation")
	@Before
	public void setUp()
	{
		exportManager.setDefaultLimit(10);
		exportManager.setMaxLimit(20);

		when(persistenceManager.create(ExportSkus.class)).thenReturn(new ExportSkusPojo());
		when(persistenceManager.getByIndex(ExportSkus.UQ_SKU_LOCATIONID, SKU, LOCATION)).thenThrow(
				new ManagedObjectNotFoundException()).thenReturn(new ExportSkusPojo(SKU));

		when(persistenceManager.createCriteriaQuery(ExportSkus.class)).thenReturn(exportSkusQueryBuilder);
		when(
				persistenceManager.createCriteriaQuery(ExportSkus.class).where(
						Restrictions.ge(ExportSkus.LATESTCHANGE, Mockito.anyLong()))).thenReturn(exportSkusQueryBuilder);
		when(
				persistenceManager.createCriteriaQuery(ExportSkus.class)
						.where(Restrictions.ge(ExportSkus.LATESTCHANGE, Mockito.anyLong())).order(ExportSkus.LATESTCHANGE)).thenReturn(
						exportSkusQueryBuilder);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetDefaultLimitNegative()
	{
		exportManager.setDefaultLimit(-5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetDefaultLimitZero()
	{
		exportManager.setDefaultLimit(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetMaxLimitNegative()
	{
		exportManager.setMaxLimit(-5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetMaxLimitZero()
	{
		exportManager.setMaxLimit(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMarkWithNullSku()
	{
		exportManager.markSkuForExport(null, LOCATION);
		exportManager.markSkuForExport(SKU, null);
	}

	@Test
	public void shouldMarkSku()
	{
		exportManager.markSkuForExport(SKU, LOCATION);

		verify(persistenceManager).getByIndex(ExportSkus.UQ_SKU_LOCATIONID, SKU, LOCATION);
		verify(persistenceManager).create(ExportSkus.class);
	}

	@Test
	public void shouldMarkSku_OnlyOnce()
	{
		exportManager.markSkuForExport(SKU, LOCATION);
		exportManager.markSkuForExport(SKU, LOCATION);

		verify(persistenceManager, times(2)).getByIndex(ExportSkus.UQ_SKU_LOCATIONID, SKU, LOCATION);
		verify(persistenceManager, times(1)).create(ExportSkus.class);
	}

	@Test
	public void shouldGet_ZeroBatchSize()
	{
		when(exportSkusQueryBuilder.resultList(Mockito.any(Pageable.class))).thenReturn(newPageOfTestSkus(10));

		final Collection<ExportSkus> result = exportManager.findSkusMarkedForExport(0, null);

		assertEquals(10, result.size());
		verify(persistenceManager, times(3)).createCriteriaQuery(ExportSkus.class);
	}

	@Test
	public void shouldGet_NegativeBatchSize()
	{
		when(exportSkusQueryBuilder.resultList(Mockito.any(Pageable.class))).thenReturn(newPageOfTestSkus(10));

		final Collection<ExportSkus> result = exportManager.findSkusMarkedForExport(-5, null);

		assertEquals(10, result.size());
		verify(persistenceManager, times(3)).createCriteriaQuery(ExportSkus.class);
	}

	@Test
	public void shouldGet_MaxSize()
	{
		when(exportSkusQueryBuilder.resultList(Mockito.any(Pageable.class))).thenReturn(newPageOfTestSkus(10));

		final Collection<ExportSkus> result = exportManager.findSkusMarkedForExport(100, null);

		assertEquals(10, result.size());
		verify(persistenceManager, times(3)).createCriteriaQuery(ExportSkus.class);
	}

	@Test
	public void shouldGet_BiggerBatchSize()
	{
		when(exportSkusQueryBuilder.resultList(Mockito.any(Pageable.class))).thenReturn(newPageOfTestSkus(1));

		final Collection<ExportSkus> result = exportManager.findSkusMarkedForExport(10, null);

		assertEquals(1, result.size());
		assertEquals("sku0", result.iterator().next().getSku());
		verify(persistenceManager, times(3)).createCriteriaQuery(ExportSkus.class);
	}

	@Test
	public void shouldGet_SmallerBatchSize()
	{
		final List<ExportSkus> dtosFirstCall = new ArrayList<>();
		final List<ExportSkus> dtosSecondCall = new ArrayList<>();
		dtosFirstCall.add(newTestSku("sku1", "location1"));
		dtosFirstCall.add(newTestSku("sku2", "location1"));
		dtosSecondCall.add(dtosFirstCall.get(1));
		when(exportSkusQueryBuilder.resultList(Mockito.any(Pageable.class))).thenReturn(newPageOfTestSkus(1)).thenReturn(
				newPageOfTestSkus(1));

		Collection<ExportSkus> result = exportManager.findSkusMarkedForExport(1, null);

		assertEquals(1, result.size());
		assertEquals("sku0", result.iterator().next().getSku());

		result = exportManager.findSkusMarkedForExport(1, null);
		assertEquals(1, result.size());
		assertEquals("sku0", result.iterator().next().getSku());
		verify(persistenceManager, times(4)).createCriteriaQuery(ExportSkus.class);
	}

	@Test
	public void shouldGet_NoFindings()
	{
		when(exportSkusQueryBuilder.resultList(Mockito.any(Pageable.class))).thenReturn(newPageOfTestSkus(0));

		final Collection<ExportSkus> result = exportManager.findSkusMarkedForExport(1, null);

		assertThat(result.isEmpty(), is(true));
		verify(persistenceManager, times(3)).createCriteriaQuery(ExportSkus.class);
	}

	@Test
	public void shouldUnmarkSku_ItemFound()
	{
		when(exportSkusQueryBuilder.resultList(Mockito.any(Pageable.class))).thenReturn(newPageOfTestSkus(1));

		exportManager.unmarkSkuForExport(new Date().getTime());

		verify(persistenceManager).remove(any(ExportSkus.class));
	}

	private ExportSkus newTestSku(final String sku, final String locationId)
	{
		final ExportSkus dto = new ExportSkusPojo();
		dto.setSku(sku);
		return dto;
	}

	private List<ExportSkus> newSetOfTestSkus(final int amount)
	{
		final List<ExportSkus> result = new ArrayList<>();
		for (int i = 0; i < amount; i++)
		{
			final ExportSkus dto = new ExportSkusPojo();
			dto.setSku(SKU + i);
			result.add(dto);
		}
		return result;
	}

	private Page<ExportSkus> newPageOfTestSkus(final int amount)
	{
		return new QueryPageImpl<ExportSkus>(newSetOfTestSkus(amount));
	}

}
