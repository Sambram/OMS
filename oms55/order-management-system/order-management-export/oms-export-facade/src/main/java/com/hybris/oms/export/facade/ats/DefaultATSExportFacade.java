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

import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.oms.export.api.ats.ATSExportFacade;
import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;
import com.hybris.oms.export.api.ats.AvailabilityToSellQuantityDTO;
import com.hybris.oms.export.service.ats.ATSExportPollService;
import com.hybris.oms.export.service.ats.ATSExportTriggerService;
import com.hybris.oms.export.service.ats.SkuToAtsRows;
import com.hybris.oms.export.service.manager.ExportManager;
import com.hybris.oms.service.ats.AtsResult.AtsRow;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Default implementation of {@link ATSExportFacade}.
 */
public class DefaultATSExportFacade implements ATSExportFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultATSExportFacade.class);

	private ATSExportPollService atsExportPollService;
	private ATSExportTriggerService atsExportTriggerService;
	private ExportManager exportManager;

	@Override
	public AvailabilityToSellDTO pollChanges(final int amountLimit, final Long pollingTime, final String atsId)
	{
		final Collection<SkuToAtsRows> atsInput = this.atsExportPollService.pollChanges(amountLimit, pollingTime, atsId);
		final AvailabilityToSellDTO ret = new AvailabilityToSellDTO();

		for (final SkuToAtsRows ats : atsInput)
		{
			this.convert(ret, ats);
		}

		return ret;
	}

	protected void convert(final AvailabilityToSellDTO ret, final SkuToAtsRows ats)
	{
		for (final AtsRow row : ats.getAtsRows())
		{
			final Long rowLatestChange = row.getKey().getMillisTime();
			if ((ret.getLatestChange() == null) || (ret.getLatestChange() < rowLatestChange))
			{
				ret.setLatestChange(rowLatestChange);
			}

			final AvailabilityToSellQuantityDTO quantity = new AvailabilityToSellQuantityDTO();
			quantity.setSkuId(row.getKey().getSku());
			quantity.setLocationId(row.getKey().getLocationId());
			if (row.getValue() == null)
			{
				LoggerFactory.getLogger(this.getClass()).warn("ATS row with null quantity detected. Skipping it.");
				continue;
			}
			quantity.setQuantity(row.getValue().intValue());
			ret.getQuantities().add(quantity);
		}
	}

	@Override
	public int triggerFullExport()
	{
		return this.atsExportTriggerService.triggerFullExport();
	}

	@Override
	public void triggerExport(final String identifier1, final String identifier2)
	{
		if (StringUtils.isEmpty(identifier1))
		{
			throw new IllegalArgumentException("No sku provided to trigger an export for");
		}
		if (StringUtils.isEmpty(identifier2))
		{
			throw new IllegalArgumentException("No location id provided to trigger an export for");
		}

		try
		{
			this.atsExportTriggerService.triggerExport(identifier1, identifier2);
		}
		catch (@SuppressWarnings("unused") final PrimaryKeyViolationException ignored)
		{
			LOG.info("Concurrent creation of sku-location. Sku-location [" + identifier1 + ", " + identifier2
					+ "] already created for export.");
		}
	}

	@Override
	public void unmarkSkuForExport(final Long latestChange)
	{
		if (latestChange == null)
		{
			throw new IllegalArgumentException("No latest change time provided to unmark skus for export");
		}
		exportManager.unmarkSkuForExport(latestChange);
	}

	public void setAtsExportPollService(final ATSExportPollService atsExportPollService)
	{
		this.atsExportPollService = atsExportPollService;
	}

	public void setAtsExportTriggerService(final ATSExportTriggerService atsExportTriggerService)
	{
		this.atsExportTriggerService = atsExportTriggerService;
	}

	public void setExportManager(final ExportManager exportManager)
	{
		this.exportManager = exportManager;
	}

	protected ATSExportPollService getAtsExportPollService()
	{
		return atsExportPollService;
	}

	protected ATSExportTriggerService getAtsExportTriggerService()
	{
		return atsExportTriggerService;
	}
}
