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

import java.util.Collection;


/**
 * Service for getting all ats values which are marked for export.
 */
public interface ATSExportPollService
{
	/**
	 * Retrieves all ats values (in relation to it's sku) which are marked for export (changed since last poll).
	 *
	 * @param limit the maximal amount of changes you will get, use zero or negative value to not specify a limit
	 * @param pollingTime is the timestamp used for polling operation
	 * @param atsId is the ATS formula that request the polling operation
	 * @return mapping between each Sku polled from the queue and the collection of
	 *         corresponding AtsRows or empty collection
	 */
	Collection<SkuToAtsRows> pollChanges(final int limit, final long pollingTime, final String atsId);
}
