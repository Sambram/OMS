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
package com.hybris.oms.export.service.ats.listener;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.hybris.kernel.api.KernelEventType;
import com.hybris.oms.export.service.ats.listener.filter.AtsExportFilter;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AtsChangeTypecodeHandlerTest
{
	private static final String SKU = "ABC";
	private static final String LOC = "123";

	@InjectMocks
	private final SimpleAtsChangeTypecodeHandler atsChangeTypecodeHandler = new SimpleAtsChangeTypecodeHandler();

	@Mock
	private AtsExportFilter createFilter;
	@Mock
	private AtsExportFilter updateFilter;
	@Mock
	private AtsExportFilter deleteFilter;

	@Before
	public void setUp()
	{
		atsChangeTypecodeHandler.setCreationAtsExportFilter(createFilter);
		atsChangeTypecodeHandler.setUpdateAtsExportFilter(updateFilter);
		atsChangeTypecodeHandler.setDeletionAtsExportFilter(deleteFilter);
	}

	@Test
	public void shouldSetAllStrategies() throws Exception
	{
		// WHEN
		atsChangeTypecodeHandler.afterPropertiesSet();

		// THEN
		assertThat(atsChangeTypecodeHandler.getAtsExportFiltersMap().size(), equalTo(3));
	}

	@Test
	public void shouldGetCreateHandler() throws Exception
	{
		// GIVEN
		atsChangeTypecodeHandler.afterPropertiesSet();

		// WHEN
		final AtsExportFilter strategy = atsChangeTypecodeHandler.getAtsExportFilter(KernelEventType.CREATE);

		// THEN
		assertThat(strategy, equalTo(createFilter));
	}

	@Test
	public void shouldGetUpdateHandler() throws Exception
	{
		// GIVEN
		atsChangeTypecodeHandler.afterPropertiesSet();

		// WHEN
		final AtsExportFilter strategy = atsChangeTypecodeHandler.getAtsExportFilter(KernelEventType.UPDATE);

		// THEN
		assertThat(strategy, equalTo(updateFilter));
	}

	@Test
	public void shouldGetDeleteHandler() throws Exception
	{
		// GIVEN
		atsChangeTypecodeHandler.afterPropertiesSet();

		// WHEN
		final AtsExportFilter strategy = atsChangeTypecodeHandler.getAtsExportFilter(KernelEventType.DELETE);

		// THEN
		assertThat(strategy, equalTo(deleteFilter));
	}

	private static class SimpleAtsChangeTypecodeHandler extends AtsChangeTypecodeHandler<CurrentItemQuantityData>
	{

		@Override
		public String getSku(final Map<String, Object> values) throws AtsChangeListenerException
		{
			return SKU;
		}

		@Override
		public String getLocationId(final Map<String, Object> values) throws AtsChangeListenerException
		{
			return LOC;
		}
	}

}
