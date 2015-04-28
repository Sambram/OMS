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
package com.hybris.oms.export.service.ats.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.KernelEvent;
import com.hybris.kernel.api.KernelEventType;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.persistence.event.DefaultKernelEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


/**
 * Do not need to test for empty map since the map will either be null or contain at least the modified_time.
 */
public class ATSAggregationUtilsTest
{
	@Test
	public void shouldReturnNull_BothMapsNull()
	{
		// GIVEN
		final KernelEvent event = createKernelEvent(null, null);

		// WHEN
		final Map<String, Object> values = ATSAggregationUtils.getDefaultValues(event);

		// THEN
		assertNull(values);
	}

	@Test
	public void shouldReturnPreviousMap_CurrentMapNull()
	{
		// GIVEN
		final Map<String, Object> map = createPopulatedMap();
		final KernelEvent event = createKernelEvent(map, null);

		// WHEN
		final Map<String, Object> values = ATSAggregationUtils.getDefaultValues(event);

		// THEN
		assertEquals(map, values);
	}

	@Test
	public void shouldReturnCurrentMap_PreviousMapNull()
	{
		// GIVEN
		final Map<String, Object> map = createPopulatedMap();
		final KernelEvent event = createKernelEvent(null, map);

		// WHEN
		final Map<String, Object> values = ATSAggregationUtils.getDefaultValues(event);

		// THEN
		assertEquals(map, values);
	}

	@Test
	public void shouldReturnCurrentMap_BothMapsNotNull()
	{
		// GIVEN
		final Map<String, Object> mapPrevious = createPopulatedMap();
		final Map<String, Object> mapCurrent = createPopulatedMap();
		final KernelEvent event = createKernelEvent(mapPrevious, mapCurrent);

		// WHEN
		final Map<String, Object> values = ATSAggregationUtils.getDefaultValues(event);

		// THEN
		assertEquals(mapCurrent, values);
	}

	private KernelEvent createKernelEvent(final Map<String, Object> previousValues, final Map<String, Object> currentValues)
	{
		final HybrisId id = HybrisId.valueOf("tenant|typecode|value");
		return new DefaultKernelEvent(id, KernelEventType.ALL, currentValues, previousValues);
	}

	private Map<String, Object> createPopulatedMap()
	{
		final Map<String, Object> map = new HashMap<>();
		map.put(ManagedObject.MODIFIEDTIME.name(), new Date());
		return map;
	}


}
