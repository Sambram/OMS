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
package com.hybris.oms.service.ats.impl;

import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.preference.TenantPreferenceService;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Retrieves the threshold used for local and global ATS calculations.
 */
public class ThresholdBuilder
{
	private static final Logger LOG = LoggerFactory.getLogger(ThresholdBuilder.class);

	private static final int HUNDRED_PERCENT = 100;

	private InventoryService inventoryService;

	private TenantPreferenceService tenantPreferenceService;

	/**
	 * Retrieves the {@link Threshold} for the given locationIds. If globalAts is <tt>true</tt>, retrieves the global
	 * threshold setting, otherwise location thresholds.
	 * 
	 * @return {@link LocationThreshold}
	 */
	public LocationThreshold getLocationThreshold(final boolean global, final Set<String> locationIds)
	{
		LocationThreshold result;
		if (global)
		{
			result = new LocationThreshold();
			result.addThreshold(AtsConstants.GLOBAL_LOC, getGlobalThreshold());
		}
		else
		{
			result = getLocalThreshold(locationIds);
		}
		return result;
	}


	/**
	 * Retrieves the global ATS {@link Threshold}.
	 * 
	 * @return {@link Threshold}
	 */
	protected Threshold getGlobalThreshold()
	{
		int threshold = 0;
		final TenantPreferenceData pref = this.tenantPreferenceService
				.getOptionalTenantPreferenceByKey(TenantPreferenceConstants.PREF_KEY_ATS_GLOBAL_THRHOLD);
		if (pref != null)
		{
			try
			{
				threshold = Integer.valueOf(pref.getValue());
			}
			catch (final NumberFormatException e)
			{
				LOG.warn("Global inventory threshold is not numeric (%s), defaulting to 0", pref.getValue());
			}
		}
		return new Threshold(threshold, false);
	}

	/**
	 * Retrieves the {@link LocationThreshold} for the given location ids.
	 * 
	 * @param locationIds if <tt>null</tt> or empty, return the threshold for all locations
	 * @return {@link LocationThreshold}
	 */
	protected LocationThreshold getLocalThreshold(final Set<String> locationIds)
	{
		final LocationThreshold result = new LocationThreshold();
		if (CollectionUtils.isNotEmpty(locationIds))
		{
			for (final StockroomLocationData location : this.inventoryService.findLocationsByLocationIds(locationIds))
			{
				final String locId = location.getLocationId();
				final Threshold threshold;
				if (location.isUsePercentageThreshold())
				{
					final int percentage = location.getPercentageInventoryThreshold();
					if (percentage < 0 || percentage > HUNDRED_PERCENT)
					{
						LOG.warn("Invalid threshold setting for location %s, ignoring threshold [%s]", locId,
								Integer.valueOf(percentage));
						threshold = new Threshold(0, true);
					}
					else
					{
						threshold = new Threshold(percentage, true);
					}
				}
				else
				{
					threshold = new Threshold(location.getAbsoluteInventoryThreshold(), false);
				}
				result.addThreshold(locId, threshold);
			}
		}
		return result;
	}

	@Required
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	@Required
	public void setTenantPreferenceService(final TenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}
}
