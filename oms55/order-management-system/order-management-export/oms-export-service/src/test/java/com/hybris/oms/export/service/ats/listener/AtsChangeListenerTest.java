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

import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.KernelEvent;
import com.hybris.kernel.api.KernelEventType;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.persistence.event.DefaultKernelEvent;
import com.hybris.oms.export.service.ats.ATSExportTriggerService;
import com.hybris.oms.export.service.ats.listener.filter.AtsExportFilter;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;


@RunWith(MockitoJUnitRunner.class)
public class AtsChangeListenerTest
{
	private static final String SKU = "ABC";
	private static final String LOC = "123";

	@InjectMocks
	private final AtsChangeListener atsChangeListener = new AtsChangeListener();

	@Mock
	private AtsChangeTypecodeHandler<CurrentItemQuantityData> atsChangeTypecodeHandler;
	@Mock
	private AtsExportFilter atsExportableStrategy;
	@Mock
	private ATSExportTriggerService atsExportTriggerService;
	@Mock
	private PlatformTransactionManager transactionManager;
	@Mock
	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws AtsChangeListenerException
	{
		@SuppressWarnings("rawtypes")
		final Map<String, AtsChangeTypecodeHandler> handlers = new HashMap<>();
		handlers.put("CurrentItemQuantityData", atsChangeTypecodeHandler);
		atsChangeListener.setAtsChangeTypecodeHandlers(handlers);

		when(atsChangeTypecodeHandler.getSku(anyMap())).thenReturn(SKU);
		when(atsChangeTypecodeHandler.getLocationId(anyMap())).thenReturn(LOC);
		when(atsChangeTypecodeHandler.getAtsExportFilter(KernelEventType.CREATE)).thenReturn(atsExportableStrategy);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotTriggerExport_EmptySku() throws AtsChangeListenerException
	{
		// GIVEN
		when(atsChangeTypecodeHandler.getSku(anyMap())).thenReturn(new String());

		final KernelEvent event = createKernelEvent();

		// WHEN
		atsChangeListener.onEvent(event);

		// THEN
		verifyZeroInteractions(atsExportTriggerService);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotTriggerExport_ExceptionRetrievingSku() throws AtsChangeListenerException
	{
		// GIVEN
		when(atsChangeTypecodeHandler.getSku(anyMap())).thenThrow(new AtsChangeListenerException("Fail on purpose."));

		final KernelEvent event = createKernelEvent();

		// WHEN
		atsChangeListener.onEvent(event);

		// THEN
		verifyZeroInteractions(atsExportTriggerService);
	}

	@Test
	public void shouldNotTriggerExport_InvalidTypecode()
	{
		// GIVEN
		@SuppressWarnings("rawtypes")
		final Map<String, AtsChangeTypecodeHandler> handlers = new HashMap<>();
		handlers.put("INVALID", atsChangeTypecodeHandler);
		atsChangeListener.setAtsChangeTypecodeHandlers(handlers);

		final KernelEvent event = createKernelEvent();

		// WHEN
		atsChangeListener.onEvent(event);

		// THEN
		verifyZeroInteractions(atsExportTriggerService);
	}


	@Test
	public void shouldTriggerExport_StrategyIsExportable()
	{
		// GIVEN
		final KernelEvent event = createKernelEvent(KernelEventType.CREATE);
		when(atsExportableStrategy.isExportable(event)).thenReturn(Boolean.TRUE);

		// WHEN
		atsChangeListener.onEvent(event);

		// THEN
		verify(atsExportableStrategy).isExportable(event);
		verify(atsExportTriggerService).triggerExport(SKU, LOC);
	}

	@Test
	public void shouldNotTriggerExport_StrategyIsNotExportable()
	{
		// GIVEN
		final KernelEvent event = createKernelEvent(KernelEventType.CREATE);
		Mockito.when(atsExportableStrategy.isExportable(event)).thenReturn(Boolean.FALSE);

		// WHEN
		atsChangeListener.onEvent(event);

		// THEN
		verify(atsExportableStrategy).isExportable(event);
		verifyZeroInteractions(atsExportTriggerService);
	}

	private KernelEvent createKernelEvent(final KernelEventType eventType)
	{
		final HybrisId id = HybrisId.valueOf("tenant|CurrentItemQuantityData|value");

		final Map<String, Object> mapPrevious = new HashMap<>();
		mapPrevious.put(ManagedObject.MODIFIEDTIME.name(), new Date());


		final Map<String, Object> mapCurrent = new HashMap<>();
		mapCurrent.put(ManagedObject.MODIFIEDTIME.name(), new Date());

		return new DefaultKernelEvent(id, eventType, mapCurrent, mapPrevious);
	}

	private KernelEvent createKernelEvent()
	{
		return createKernelEvent(KernelEventType.CREATE);
	}

}
