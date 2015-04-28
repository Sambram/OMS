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

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.KernelEvent;
import com.hybris.kernel.api.KernelEventType;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.persistence.event.DefaultKernelEvent;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class AbstractFieldChangeAtsExportableStrategyTest
{

	private static final String FIELD2 = "field2";
	private static final String FIELD1 = "field1";

	private final AbstractFieldChangeAtsExportFilter emptyExportFieldsStrategy = new EmptyExportFields();
	private final AbstractFieldChangeAtsExportFilter invalidExportFieldsStrategy = new InvalidExportFields();
	private final AbstractFieldChangeAtsExportFilter validStrategy = new ValidExportFields();

	private KernelEvent event;

	@Before
	public void setUp()
	{
		event = createKernelEvent_NoChange();
	}

	@Test
	public void shouldFailExport_EmptyExportFields()
	{
		// WHEN
		final boolean export = emptyExportFieldsStrategy.isExportable(event);

		// THEN
		assertFalse(export);
	}

	@Test
	public void shouldFailExport_NoMatchNoChange()
	{
		// WHEN
		final boolean export = invalidExportFieldsStrategy.isExportable(event);

		// THEN
		assertFalse(export);
	}

	@Test
	public void shouldFailExport_NoMatchWithChange()
	{
		// GIVEN
		event = createKernelEvent_WithChange();

		// WHEN
		final boolean export = invalidExportFieldsStrategy.isExportable(event);

		// THEN
		assertFalse(export);
	}

	@Test
	public void shouldFailExport_WithMatchNoChange()
	{
		// WHEN
		final boolean export = validStrategy.isExportable(event);

		// THEN
		assertFalse(export);
	}

	@Test
	public void shouldExport_WithMatchWithChange()
	{
		// GIVEN
		event = createKernelEvent_WithChange();

		// WHEN
		final boolean export = validStrategy.isExportable(event);

		// THEN
		assertTrue(export);
	}

	private static class EmptyExportFields extends AbstractFieldChangeAtsExportFilter
	{
		@Override
		public Set<String> getExportFields()
		{
			return Collections.<String>emptySet();
		}
	}

	private static class InvalidExportFields extends AbstractFieldChangeAtsExportFilter
	{
		@Override
		public Set<String> getExportFields()
		{
			return Sets.newHashSet("INVALID");
		}
	}

	private static class ValidExportFields extends AbstractFieldChangeAtsExportFilter
	{
		@Override
		public Set<String> getExportFields()
		{
			return Sets.newHashSet(FIELD1);
		}
	}

	private KernelEvent createKernelEvent_NoChange()
	{
		final HybrisId id = HybrisId.valueOf("tenant|CurrentItemQuantityData|value");

		final Map<String, Object> mapPrevious = new HashMap<>();
		mapPrevious.put(ManagedObject.MODIFIEDTIME.name(), new Date());
		mapPrevious.put(FIELD1, "foo");
		mapPrevious.put(FIELD2, "bar");


		final Map<String, Object> mapCurrent = new HashMap<>();
		mapCurrent.put(ManagedObject.MODIFIEDTIME.name(), new Date());
		mapCurrent.put(FIELD1, "foo");
		mapCurrent.put(FIELD2, "bar");

		return new DefaultKernelEvent(id, KernelEventType.CREATE, mapCurrent, mapPrevious);
	}

	private KernelEvent createKernelEvent_WithChange()
	{
		final HybrisId id = HybrisId.valueOf("tenant|CurrentItemQuantityData|value");

		final Map<String, Object> mapPrevious = new HashMap<>();
		mapPrevious.put(ManagedObject.MODIFIEDTIME.name(), new Date());
		mapPrevious.put(FIELD1, "foo");
		mapPrevious.put(FIELD2, "bar");


		final Map<String, Object> mapCurrent = new HashMap<>();
		mapCurrent.put(ManagedObject.MODIFIEDTIME.name(), new Date());
		mapCurrent.put(FIELD1, "new foo");
		mapCurrent.put(FIELD2, "bar");

		return new DefaultKernelEvent(id, KernelEventType.CREATE, mapCurrent, mapPrevious);
	}
}
