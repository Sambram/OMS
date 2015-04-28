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

import com.hybris.oms.export.service.managedobjects.ats.ExportSkus;

import java.util.Set;


/**
 * Manages the sku's to export. The manager provides functionality to mark a SKU to be exported later. Later the manager
 * can be asked for that markers to perform the actual export.
 * For example a SKU "a" can be marked for the export as a value of the sku has changed. Another thread can ask for all
 * sku's marked for export and will get "a" as result.
 */
public interface ExportManager
{
	/**
	 * Marks the given sku as to export. A marking of a sku twice has same effect as marking it once.
	 *
	 * @param sku - the sku to mark for export
	 * @param locationId - the location id to mark for export
	 */
	void markSkuForExport(final String sku, final String locationId);

	/**
	 * Gets all skus marked for export.
	 *
	 * @param batchSize the size of the result set, use positive number for setting a limit, use zero or negative number
	 *           for setting no explicit limit
	 * @param millisTime the time limit to mark the sku to be exported
	 * @return set of sku's with size less or equal then given batchSize, empty list if no sku's are available anymore
	 */
	Set<ExportSkus> findSkusMarkedForExport(int batchSize, Long millisTime);

	/**
	 * Gets all skus unmaked for export.
	 *
	 * @param latestChange
	 */
	void unmarkSkuForExport(Long latestChange);
}
