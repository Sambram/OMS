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

import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.export.service.ats.ATSExportTriggerService;
import com.hybris.oms.export.service.manager.ExportManager;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;


/**
 * Default implementation of {@link ATSExportTriggerService} based on {@link ExportManager}.
 */

public class DefaultATSExportTriggerService implements ATSExportTriggerService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultATSExportTriggerService.class);

	private ExportManager exportManager;
	private PersistenceManager persistenceManager;

	@Override
	@Transactional
	public int triggerFullExport()
	{
		final List<ItemLocationData> itemLocations = this.persistenceManager.createCriteriaQuery(ItemLocationData.class)
				.resultList();
		for (final ItemLocationData itemLocation : itemLocations)
		{
			triggerExport(itemLocation.getItemId(), itemLocation.getStockroomLocation().getLocationId());
		}

		LOG.info("Put [" + itemLocations.size() + "] skus to export queue.");
		return itemLocations.size();
	}

	@Override
	@Transactional
	public void triggerExport(final String sku, final String locationId)
	{
		if (sku == null)
		{
			throw new IllegalArgumentException("Sku to export must not be null.");
		}
		if (locationId == null)
		{
			throw new IllegalArgumentException("Location id to export must not be null.");
		}

		this.exportManager.markSkuForExport(sku, locationId);
	}

	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	public void setExportManager(final ExportManager exportManager)
	{
		this.exportManager = exportManager;
	}

	protected PersistenceManager getPersistenceManager()
	{
		return this.persistenceManager;
	}

	protected ExportManager getExportManager()
	{
		return this.exportManager;
	}
}
