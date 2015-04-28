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
package com.hybris.oms.service.health.impl;

import com.hybris.commons.monitoring.collector.MetricCollector;
import com.hybris.kernel.api.KernelEventAware;
import com.hybris.kernel.api.KernelEventType;
import com.hybris.kernel.api.KernelLifecycleEvent;
import com.hybris.kernel.api.KernelUpToDateEvent;
import com.hybris.kernel.api.TenantsCreatedEvent;
import com.hybris.kernel.api.TenantsRemovingEvent;
import com.hybris.kernel.api.annotations.KernelEventHandler;
import com.hybris.kernel.persistence.event.DefaultKernelEvent;
import com.hybris.kernel.persistence.initialization.InitializationService;
import com.hybris.oms.service.health.collector.EventStatisticsType;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;


/**
 * Listener for kernel lifecycle events to add/remove dynamically new tenant statistics for new/removed tenants and
 * listener for data events for statistics based on data creation.
 */
public class KernelEventListener implements KernelEventAware, ApplicationListener<KernelLifecycleEvent>
{
	private MetricCollector metricCollector;

	@Autowired
	private InitializationService initializationService;

	@Override
	public void onApplicationEvent(final KernelLifecycleEvent event)
	{
		if (event instanceof TenantsCreatedEvent)
		{
			final Set<String> tenants = ((TenantsCreatedEvent) event).getTenants();
			for (final String tenantId : tenants)
			{
				metricCollector.initializeTenant(tenantId);
			}
		}
		else if (event instanceof KernelUpToDateEvent)
		{
			metricCollector.removeAllMetrics();
			// as events are asynchronous we might receive this event after the tenant created event,
			// so we have to re-initialize the statistics again just to be sure of not missing a tenant creation
			for (final String tenantId : initializationService.getInitializedTenants())
			{
				metricCollector.initializeTenant(tenantId);
			}
		}
		else if (event instanceof TenantsRemovingEvent)
		{
			final Set<String> tenants = ((TenantsRemovingEvent) event).getTenants();
			for (final String tenantId : tenants)
			{
				metricCollector.removeAllMetricsOfTenant(tenantId);
			}
		}
	}

	/**
	 * Method invoked on object create.
	 * 
	 * @param event the event
	 */
	@KernelEventHandler(events = {KernelEventType.CREATE}, typeCodes = {"OrderData", "CurrentItemQuantityData"})
	public void onCreateEvent(final DefaultKernelEvent event)
	{
		final String typecode = event.getHybrisId().getTypeCode();
		final String tenant = event.getHybrisId().getTenant();

		incrementMetric(tenant, typecode, EventStatisticsType.CREATE);
		incrementMetric(tenant, typecode, EventStatisticsType.CREATE_UPDATE);
	}

	/**
	 * Method invoked on object update.
	 * 
	 * @param event the event
	 */
	@KernelEventHandler(events = {KernelEventType.UPDATE}, typeCodes = {"CurrentItemQuantityData"})
	public void onUpdateEvent(final DefaultKernelEvent event)
	{
		final String typecode = event.getHybrisId().getTypeCode();
		final String tenant = event.getHybrisId().getTenant();

		incrementMetric(tenant, typecode, EventStatisticsType.UPDATE);
		incrementMetric(tenant, typecode, EventStatisticsType.CREATE_UPDATE);
	}

	private void incrementMetric(final String tenant, final String typeCode, final EventStatisticsType eventType)
	{
		metricCollector.increment(tenant, typeCode + eventType.name());
	}

	public void setInitializationService(final InitializationService initializationService)
	{
		this.initializationService = initializationService;
	}

	public void setMetricCollector(final MetricCollector metricCollector)
	{
		this.metricCollector = metricCollector;
	}
}
