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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.hybris.kernel.api.KernelEvent;
import com.hybris.oms.export.service.ats.listener.filter.AtsExportFilter;
import com.hybris.oms.export.service.ats.listener.filter.impl.CompositeAtsExportFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;


@RunWith(MockitoJUnitRunner.class)
public class CompositeAtsExportableStrategyTest
{
	@InjectMocks
	private final CompositeAtsExportFilter atsExportFilter = new CompositeAtsExportFilter();

	@Mock
	private AtsExportFilter successFilter;
	@Mock
	private AtsExportFilter failureFilter;
	@Mock
	private KernelEvent event;

	@Before
	public void setUp()
	{
		when(successFilter.isExportable(event)).thenReturn(TRUE);
		when(failureFilter.isExportable(event)).thenReturn(FALSE);
	}

	@Test
	public void shouldExport()
	{
		// GIVEN
		atsExportFilter.setAtsExportFilters(Sets.newHashSet(successFilter));

		// WHEN
		final boolean export = atsExportFilter.isExportable(event);

		// THEN
		assertTrue(export);
	}

	@Test
	public void shouldFailExport()
	{
		// GIVEN
		atsExportFilter.setAtsExportFilters(Sets.newHashSet(failureFilter));

		// WHEN
		final boolean export = atsExportFilter.isExportable(event);

		// THEN
		assertFalse(export);
	}
}
