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
package com.hybris.oms.export.service.ats.listener.strategy.filter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.hybris.kernel.api.KernelEvent;
import com.hybris.oms.export.service.ats.listener.filter.impl.AlwaysExportAtsExportFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AlwaysExportAtsExportableStrategyTest
{
	@InjectMocks
	private final AlwaysExportAtsExportFilter atsExportableStrategy = new AlwaysExportAtsExportFilter();

	@Test
	public void shouldExport()
	{
		// GIVEN
		final KernelEvent event = mock(KernelEvent.class);

		// WHEN
		final boolean export = atsExportableStrategy.isExportable(event);

		// THEN
		assertTrue(export);
	}
}
