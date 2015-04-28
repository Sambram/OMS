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
package com.hybris.oms.export.service.ats;

/**
 * Service for triggering an export of ats values.
 */
public interface ATSExportTriggerService
{
	/**
	 * Triggers an export of ats values related to all skus.
	 *
	 * @return the amount of skus/ats values triggered for export
	 */
	int triggerFullExport();

	/**
	 * Triggers the export of the ats value related to the sku passed.
	 *
	 * @param sku sku identifying the ats values to export
	 * @param locationId location id of the ats values to export
	 */
	public void triggerExport(final String sku, final String locationId);
}
