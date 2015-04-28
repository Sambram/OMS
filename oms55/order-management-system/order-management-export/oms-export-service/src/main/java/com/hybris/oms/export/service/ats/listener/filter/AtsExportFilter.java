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
package com.hybris.oms.export.service.ats.listener.filter;

import com.hybris.kernel.api.KernelEvent;


/**
 * This filter will contain the condition that must evaluate to true before an ATS sku can be marked for export.
 */
public interface AtsExportFilter
{
	/**
	 * Determines whether a kernel event should trigger a sku to be marked for export
	 *
	 * @param event
	 * @return <tt>true</tt> if the event should cause an export; <tt>false</tt> otherwise
	 */
	boolean isExportable(final KernelEvent event);
}
