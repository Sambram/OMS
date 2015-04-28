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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.export.service.manager.ExportManager;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultATSExportTriggerServiceTest
{
	@InjectMocks
	private final DefaultATSExportTriggerService triggerService = new DefaultATSExportTriggerService();

	@Mock
	private ExportManager exportManager;
	@Mock
	private PersistenceManager persistenceManager;

	@Test(expected = IllegalArgumentException.class)
	public void testExportSkuWithNull()
	{
		this.triggerService.triggerExport(null, null);
	}

	@Test
	public void testExportSku()
	{
		this.triggerService.triggerExport("test", "location1");

		verify(this.exportManager, times(1)).markSkuForExport("test", "location1");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFullExport()
	{
		final StockroomLocationData stockroomLocationData1 = mock(StockroomLocationData.class);
		final ItemLocationData item1 = mock(ItemLocationData.class);
		when(item1.getItemId()).thenReturn("item1");
		when(item1.getStockroomLocation()).thenReturn(stockroomLocationData1);
		when(item1.getStockroomLocation().getLocationId()).thenReturn("location1");

		final StockroomLocationData stockroomLocationData2 = mock(StockroomLocationData.class);
		final ItemLocationData item2 = mock(ItemLocationData.class);
		when(item2.getItemId()).thenReturn("item2");
		when(item2.getStockroomLocation()).thenReturn(stockroomLocationData2);
		when(item2.getStockroomLocation().getLocationId()).thenReturn("location2");

		final CriteriaQuery<ItemLocationData> query = mock(CriteriaQuery.class);
		when(this.persistenceManager.createCriteriaQuery(ItemLocationData.class)).thenReturn(query);
		when(query.resultList()).thenReturn(Arrays.asList(item1, item2));

		final int result = this.triggerService.triggerFullExport();

		verify(this.exportManager, times(1)).markSkuForExport("item1", "location1");
		verify(this.exportManager, times(1)).markSkuForExport("item2", "location2");
		Assert.assertEquals(2, result);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFullExportEmpty()
	{
		final CriteriaQuery<ItemLocationData> query = mock(CriteriaQuery.class);
		when(this.persistenceManager.createCriteriaQuery(ItemLocationData.class)).thenReturn(query);
		when(query.resultList()).thenReturn(Collections.<ItemLocationData>emptyList());

		final int result = this.triggerService.triggerFullExport();

		verify(this.exportManager, times(0)).markSkuForExport("item1", "location1");
		Assert.assertEquals(0, result);
	}
}
