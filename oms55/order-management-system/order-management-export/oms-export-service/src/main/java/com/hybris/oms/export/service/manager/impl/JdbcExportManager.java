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

import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.Restrictions;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.regioncache.PageRequest;
import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;
import com.hybris.oms.export.service.manager.ExportManager;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;


/**
 * Jdbc implementation of {@link ExportManager} uses {@link ExportSkus} managed object with map to store skus marked for
 * export.
 */
public class JdbcExportManager implements ExportManager
{

	private static Logger LOG = LoggerFactory.getLogger(JdbcExportManager.class);

	private PersistenceManager persistenceManager;
	private int defaultLimit;
	private int maxLimit;

	@Transactional
	@Override
	public void markSkuForExport(final String sku, final String locationId)
	{
		Preconditions.checkArgument(sku != null, "sku must not be null");

		try
		{
			// If sku-location is already present, then update timestamp.
			final ExportSkus exportSku = persistenceManager.getByIndex(ExportSkus.UQ_SKU_LOCATIONID, sku, locationId);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Updating sku-Location [" + sku + ", " + locationId + "] export time.");
			}
			exportSku.setLatestChange(new Date().getTime());
		}
		catch (@SuppressWarnings("unused") final ManagedObjectNotFoundException ignored)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Marking sku-location [" + sku + ", " + locationId + "] for export.");
			}

			// If sku is not present, then add it.
			final ExportSkus exportSku = persistenceManager.create(ExportSkus.class);
			exportSku.setSku(sku);
			exportSku.setLocationId(locationId);
			exportSku.setLatestChange(new Date().getTime());
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Set<ExportSkus> findSkusMarkedForExport(final int batchSize, final Long millisTime)
	{
		Long timeCondition = millisTime;
		if (millisTime == null)
		{
			timeCondition = 0L;
		}

		// Only process a certain batch size at a time.
		final int validBatchSize = determineBatchSize(batchSize);

		final Set<ExportSkus> result = new LinkedHashSet<>();
		final List<ExportSkus> exportSkus = persistenceManager.createCriteriaQuery(ExportSkus.class)
				.where(Restrictions.ge(ExportSkus.LATESTCHANGE, timeCondition)).order(ExportSkus.LATESTCHANGE)
				.resultList(new PageRequest(0, validBatchSize)).getContent();

		for (final ExportSkus exportSku : exportSkus)
		{
			if (StringUtils.isBlank(exportSku.getLocationId()))
			{
				exportSku.setLocationId("GLOBAL");
			}
			result.add(exportSku);
		}
		return result;
	}

	@Transactional
	@Override
	public void unmarkSkuForExport(final Long latestChange)
	{
		int counter = defaultLimit;
		boolean hasMore = Boolean.TRUE;

		// Get page of 'maxLimit' results while there are still some records left.
		while (hasMore)
		{
			final Page<ExportSkus> exportSkus = this.persistenceManager.createCriteriaQuery(ExportSkus.class)
					.where(Restrictions.lt(ExportSkus.LATESTCHANGE, latestChange)).resultList(new PageRequest(0, maxLimit));

			// Check to see if there are more pages to process
			if (exportSkus.getTotalPages() == 1)
			{
				hasMore = Boolean.FALSE;
			}

			for (final ExportSkus exportSku : exportSkus.getContent())
			{
				try
				{
					persistenceManager.remove(exportSku);
				}
				catch (@SuppressWarnings("unused") final RuntimeException ignored)
				{
					LOG.warn("An ExportSku could not be removed. This should be removed in the next pass.");
				}
			}
			persistenceManager.flush();

			// Do not process the loop more than '100' times. This is to prevent a potential infinite loop.
			counter--;
			if (counter == 0)
			{
				break;
			}
		}
	}

	/**
	 * Gets the number of skus to process.
	 *
	 * @param batchSize - requested batch size
	 * @return actual batch size
	 */
	protected int determineBatchSize(final int batchSize)
	{
		return batchSize > 0 ? Math.min(batchSize, maxLimit) : defaultLimit;
	}

	/**
	 * Sets the default limit used when no batch size is specified.
	 *
	 * @param defaultLimit the limit to use
	 */
	@Required
	public void setDefaultLimit(final int defaultLimit)
	{
		Preconditions.checkArgument(defaultLimit > 0, "default limit must be greater then zero");
		this.defaultLimit = defaultLimit;
	}

	protected int getDefaultLimit()
	{
		return this.defaultLimit;
	}

	/**
	 * Sets the maximal limit used when batch size is bigger then this value.
	 *
	 * @param maxLimit the limit to use
	 */
	@Required
	public void setMaxLimit(final int maxLimit)
	{
		Preconditions.checkArgument(maxLimit > 0, "max limit must be greater then zero");
		this.maxLimit = maxLimit;
	}

	protected int getMaxLimit()
	{
		return this.maxLimit;
	}

	public PersistenceManager getPersistenceManager()
	{
		return persistenceManager;
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

}
