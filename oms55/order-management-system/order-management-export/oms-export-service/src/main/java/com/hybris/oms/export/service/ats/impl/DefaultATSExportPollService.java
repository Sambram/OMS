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

import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.export.service.ats.ATSExportPollService;
import com.hybris.oms.export.service.ats.SkuToAtsRows;
import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;
import com.hybris.oms.export.service.manager.ExportManager;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsResult.AtsRow;
import com.hybris.oms.service.ats.AtsResult.Key;
import com.hybris.oms.service.ats.AtsService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


/**
 * Default implementation of {@link ATSExportPollService} based on {@link ExportManager}.
 */
public class DefaultATSExportPollService implements ATSExportPollService
{
	private static Logger LOG = LoggerFactory.getLogger(DefaultATSExportPollService.class);

	private ExportManager exportManager;
	private AtsService atsService;
	private PlatformTransactionManager transactionManager;

	@Override
	public Collection<SkuToAtsRows> pollChanges(final int limit, final long pollingTime, final String atsId)
	{
		final Collection<SkuToAtsRows> ret = new ArrayList<>();
		final Set<ExportSkus> skus = exportManager.findSkusMarkedForExport(limit, pollingTime);
		LOG.info("Found skus " + skus.toString() + " marked for export.");

		if (skus.isEmpty())
		{
			return ret;
		}

		// initialize the map with empty SkuToAtsRows (as we want to have a result for every sku,
		// regardless if ATS value exists or not)
		final Map<String, SkuToAtsRows> skuToATSRowsMap = new HashMap<>();
		for (final ExportSkus sku : skus)
		{
			final SkuToAtsRows skuToAtsRows = new SkuToAtsRows(sku.getSku());
			skuToATSRowsMap.put(sku.getSku(), skuToAtsRows);
		}

		final TransactionTemplate template = new TransactionTemplate(this.transactionManager);
		final AtsResult atsResult = template.execute(new TransactionCallback<AtsResult>()
		{
			@Override
			public AtsResult doInTransaction(final TransactionStatus arg0)
			{
				return getAtsChanges(skus, atsId);
			}
		});

		this.addAtsRows(skuToATSRowsMap, atsResult.getResults());

		// add changed ats values
		ret.addAll(skuToATSRowsMap.values());

		return ret;
	}

	/**
	 * Converts collection of ATSRow into collection of SkuToAtsRows.
	 * Each SkuToAtsRow corresponds to a number of ATSRows which have the same sku id.
	 *
	 * @param skuToATSRowsMap map to which the atsRows will be added
	 * @param atsRows collection of ATSRow
	 */
	protected void addAtsRows(final Map<String, SkuToAtsRows> skuToATSRowsMap, final Collection<AtsRow> atsRows)
	{
		// add skus with calculated values:
		for (final AtsRow row : atsRows)
		{
			final String sku = row.getKey().getSku();

			final SkuToAtsRows skuToAtsRows = skuToATSRowsMap.get(sku);

			if (skuToAtsRows != null)
			{
				skuToAtsRows.addAtsRow(row);
			}
			else
			{ // should not get here
				throw new IllegalArgumentException("There is no entry for sku '" + sku + "' in the map");
			}
		}
	}

	/**
	 * Return Ats changes
	 *
	 * @param skus list of sku ids for which to get changes
	 * @param atsId the ATS formula to use
	 * @return AtsResult
	 */
	protected AtsResult getAtsChanges(final Set<ExportSkus> skus, final String atsId)
	{
		final AtsResult atsResult = new AtsResult();

		final Set<String> formulaIds = new HashSet<String>();
		formulaIds.add(atsId);

		final Set<ExportSkus> localExportSkus = new HashSet<>();
		for (final ExportSkus sku : skus)
		{
			// Only get local ATS here
			if (!"GLOBAL".equals(sku.getLocationId()))
			{
				localExportSkus.add(sku);
			}
		}

		if (!localExportSkus.isEmpty())
		{
			final Set<String> justSkus = extractSkus(localExportSkus);
			for (final String justSku : justSkus)
			{
				atsResult.addAll(this.atsService.getLocalAts(Collections.singleton(justSku),
						extractLocationIdsForSku(justSku, localExportSkus), formulaIds));
			}
		}
		return setMaxLatestChangeinAtsResult(localExportSkus, atsResult);
	}

	/**
	 * Sets the largestTimeStamp from ExportSkus to the ATSResult
	 *
	 * @param skus
	 * @param atsResult
	 * @return
	 */
	private AtsResult setMaxLatestChangeinAtsResult(final Set<ExportSkus> skus, final AtsResult atsResult)
	{
		Long maxLatestChange = null;
		final Iterator<ExportSkus> skusIterator = skus.iterator();
		while (skusIterator.hasNext())
		{
			final Long skuLatestChange = skusIterator.next().getLatestChange();
			if (maxLatestChange == null || maxLatestChange < skuLatestChange)
			{
				maxLatestChange = skuLatestChange;
			}
		}

		final AtsResult atsNewResult = new AtsResult();
		final List<AtsRow> atsRows = atsResult.getResults();
		final ListIterator<AtsRow> atsRowsIterator = atsRows.listIterator();
		while (atsRowsIterator.hasNext())
		{
			final AtsRow atsRow = atsRowsIterator.next();
			final Key key = atsRow.getKey();
			final Integer atsValue = atsRow.getValue();
			atsNewResult.addResult(key.getLocationId(), key.getSku(), key.getAtsId(), atsValue, maxLatestChange);
		}

		return atsNewResult;

	}

	protected Set<String> extractSkus(final Set<ExportSkus> skus)
	{
		final Set<String> skusOnly = new HashSet<>();
		for (final ExportSkus sku : skus)
		{
			try
			{
				skusOnly.add(sku.getSku());
			}
			catch (@SuppressWarnings("unused") final IllegalArgumentException ignored)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Found duplicate sku: " + sku.getSku());
				}
			}
		}
		return skusOnly;
	}

	protected Set<String> extractLocationIdsForSku(final String justSku, final Set<ExportSkus> skus)
	{
		final Set<String> locationIdOnly = new HashSet<>();
		for (final ExportSkus sku : skus)
		{
			try
			{
				if (sku.getSku().equals(justSku))
				{
					locationIdOnly.add(sku.getLocationId());
				}
			}
			catch (@SuppressWarnings("unused") final IllegalArgumentException ignored)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Found duplicate LocationId: " + sku.getLocationId());
				}
			}
		}
		return locationIdOnly;
	}

	protected void removeNonexistantAtsIdFromFilterSet(final Set<String> filterFormulas)
	{
		if (filterFormulas != null)
		{
			final Iterator<String> iterator = filterFormulas.iterator();
			while (iterator.hasNext())
			{
				final String atsId = iterator.next();

				try
				{
					atsService.getFormulaById(atsId);
				}
				catch (@SuppressWarnings("unused") final EntityNotFoundException | IllegalArgumentException ex)
				{
					iterator.remove();
				}
			}
		}
	}

	public void setAtsService(final AtsService atsService)
	{
		this.atsService = atsService;
	}

	public void setExportManager(final ExportManager exportManager)
	{
		this.exportManager = exportManager;
	}

	protected AtsService getAtsService()
	{
		return this.atsService;
	}

	protected ExportManager getExportManager()
	{
		return this.exportManager;
	}

	public PlatformTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	@Required
	public void setTransactionManager(final PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}
}
