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
package com.hybris.oms.export.api;

import com.hybris.oms.export.api.ats.AvailabilityToSellDTO;



/**
 * Facade for exporting oms data. The export is separated into two steps, first mark data for export (by a trigger
 * method) and second get all data marked for export (by poll method). With this you can export data in a batch way, by
 * first collecting data which needs to get exported over time (as they changed) and then getting all data marked over
 * time at once.
 */
public interface ExportFacade<T>
{
	/**
	 * Retrieves the data marked for export.
	 *
	 * @category PLATFORM EXTENSION - omsats
	 *
	 * @param amountLimit
	 *           the maximum of updates to retrieve with this call, use zero or non-negative number to not set an
	 *           explicit limit.
	 * @param pollingTime
	 *           the time when the polling is requested
	 * @param atsId
	 *           id of the ATS formula that request the polling
	 * @return a collection of data for export with a maximum size of passed limit or empty collection if no data was
	 *         marked for export
	 */
	AvailabilityToSellDTO pollChanges(final int amountLimit, final Long pollingTime, final String atsId);

	/**
	 * Triggers export of the given entity identified by <code>identifiers</code>.
	 * This means the data identified by the given identifier will be marked for export, with that returned on subsequent
	 * {@link #pollChanges(int, Long, String)} requests.
	 *
	 * @category OMS-UI
	 *
	 * @param identifier1 identifier the identifier for the trigger
	 * @param identifier2 identifier the identifier for the trigger
	 */
	void triggerExport(final String identifier1, String identifier2);

	/**
	 * Triggers export of all currently available entities in the system.
	 * This means all currently available entities will be marked for export, with that returned on subsequent
	 * {@link #pollChanges(int, Long, String)} requests.
	 *
	 * @return the amount of data marked for export
	 */
	int triggerFullExport();

	/**
	 * Expose the unmark sku for export.
	 *
	 * @param latestChange milliseconds used as a search by last updated records
	 */
	void unmarkSkuForExport(final Long latestChange);
}
