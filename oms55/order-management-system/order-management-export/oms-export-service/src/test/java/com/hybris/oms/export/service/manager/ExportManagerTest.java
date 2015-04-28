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
package com.hybris.oms.export.service.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/oms-export-service-spring-test.xml")
public class ExportManagerTest
{
	@Autowired
	private ExportManager atsExportManager;
	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void shouldMarkGetAndReset()
	{
		atsExportManager.markSkuForExport("sku1", "location1");
		atsExportManager.markSkuForExport("sku2", "location1");
		persistenceManager.flush();

		Collection<ExportSkus> result = atsExportManager.findSkusMarkedForExport(-1, null);
		assertEquals(2, result.size());
		assertTrue("sku1".equals(result.iterator().next().getSku()));

		unmarkSkus(null);
		persistenceManager.flush();

		result = atsExportManager.findSkusMarkedForExport(-1, null);
		assertTrue(result.isEmpty());
	}

	@Transactional
	@Test
	public void shouldMarkGetAndReset_MultipleBatchSize() throws InterruptedException
	{
		atsExportManager.markSkuForExport("sku1", "location1");
		Thread.sleep(1000l);
		final long timeOne = new Date().getTime();
		persistenceManager.flush();

		atsExportManager.markSkuForExport("sku2", "location1");
		persistenceManager.flush();
		atsExportManager.markSkuForExport("sku3", "location1");
		Thread.sleep(1000l);
		final long timeThree = new Date().getTime();
		persistenceManager.flush();

		atsExportManager.markSkuForExport("sku4", "location1");
		Thread.sleep(1000l);
		final long timeFour = new Date().getTime();
		persistenceManager.flush();

		Collection<ExportSkus> result = atsExportManager.findSkusMarkedForExport(1, null);
		assertEquals(1, result.size());
		assertTrue("sku1".equals(result.iterator().next().getSku()));
		unmarkSkus(timeOne);
		persistenceManager.flush();

		result = atsExportManager.findSkusMarkedForExport(2, null);
		final Iterator<ExportSkus> iterator = result.iterator();
		assertEquals(2, result.size());
		assertTrue("sku2".equals(iterator.next().getSku()));
		assertTrue("sku3".equals(iterator.next().getSku()));
		unmarkSkus(timeThree);
		persistenceManager.flush();

		result = atsExportManager.findSkusMarkedForExport(100, null);
		assertEquals(1, result.size());
		assertTrue("sku4".equals(result.iterator().next().getSku()));
		unmarkSkus(timeFour);
		persistenceManager.flush();

		result = atsExportManager.findSkusMarkedForExport(100, null);
		assertTrue(result.isEmpty());
	}

	@Test
	@Transactional
	public void shouldUnmarkAllSkus_MoreThan1Page() throws InterruptedException
	{
		for (int i = 0; i < 20; i++)
		{
			atsExportManager.markSkuForExport("sku", "location_" + i);
		}
		persistenceManager.flush();
		Thread.sleep(100);
		final long time = new Date().getTime();

		atsExportManager.unmarkSkuForExport(time);
		Assert.assertFalse(persistenceManager.createQuery(ExportSkus.class).resultList().iterator().hasNext());
	}

	@Test
	@Transactional
	public void shouldNotUnmarkAllSkus_MoreThanMaxLimitPages() throws InterruptedException
	{
		for (int i = 0; i < 105; i++)
		{
			atsExportManager.markSkuForExport("sku", "location_" + i);
		}
		persistenceManager.flush();
		Thread.sleep(100);
		final long time = new Date().getTime();

		atsExportManager.unmarkSkuForExport(time);
		Assert.assertEquals(5, Iterables.size(persistenceManager.createQuery(ExportSkus.class).resultList()));
	}

	/**
	 * Unmark for export all skus.
	 */
	private void unmarkSkus(final Long lastChange)
	{
		Long timestamp = lastChange;
		if (timestamp == null)
		{
			timestamp = new Date().getTime();
		}

		atsExportManager.unmarkSkuForExport(timestamp);
	}
}
