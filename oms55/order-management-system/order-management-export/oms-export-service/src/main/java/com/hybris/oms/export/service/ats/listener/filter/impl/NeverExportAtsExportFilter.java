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
package com.hybris.oms.export.service.ats.listener.filter.impl;

import com.hybris.kernel.api.KernelEvent;
import com.hybris.oms.export.service.ats.listener.filter.AtsExportFilter;


/**
 * Implementation of {@link AtsExportFilter} that simply always allows to export (return {@link Boolean#TRUE}).
 */
public class NeverExportAtsExportFilter implements AtsExportFilter
{

	@Override
	public boolean isExportable(final KernelEvent event)
	{
		return Boolean.FALSE.booleanValue();
	}

}
