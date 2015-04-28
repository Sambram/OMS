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

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of the {@link AtsExportFilter} that implements the <tt>composite pattern</tt>.
 *
 * <p>
 * The composite will allow to export if and only if all {@link AtsExportFilter} items in the composite allow to export.
 * </p>
 */
public class CompositeAtsExportFilter implements AtsExportFilter
{
	private Set<AtsExportFilter> atsExportFilters = new HashSet<>();

	@Override
	public boolean isExportable(final KernelEvent event)
	{
		boolean export = true;
		for (final AtsExportFilter atsExportableFilter : atsExportFilters)
		{
			if (!atsExportableFilter.isExportable(event))
			{
				export = false;
				break;
			}
		}
		return export;
	}

	public Set<AtsExportFilter> getAtsExportFilters()
	{
		return atsExportFilters;
	}

	@Required
	public void setAtsExportFilters(final Set<AtsExportFilter> atsExportFilters)
	{
		this.atsExportFilters = atsExportFilters;
	}

}
