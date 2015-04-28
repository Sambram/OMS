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
import com.hybris.commons.monitoring.helper.CountingOperation;
import com.hybris.commons.monitoring.helper.TenantContextHelper;
import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.persistence.initialization.InitializationService;
import com.hybris.oms.service.health.HealthService;
import com.hybris.oms.service.health.TimeStatisticsType;
import com.hybris.oms.service.health.agg.AggCurrentItemQuantity;
import com.hybris.oms.service.health.agg.AggOrderLineQuantityStatus;
import com.hybris.oms.service.health.agg.AggStockroomLocation;
import com.hybris.oms.service.health.agg.QuantityCountingAggregation;
import com.hybris.oms.service.health.agg.StatusQuantityCountingAggregation;
import com.hybris.oms.service.health.collector.EventStatisticsType;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;


/**
 * Default implementation of {@link HealthService}.
 */
@ManagedResource(objectName = "mbeans:name=OMSHealthService", description = "OMS Health Service.")
public class DefaultHealthService implements HealthService
{
	private MetricCollector metricCollector;

	@Autowired
	private AggregationService aggregationService;

	@Autowired
	private InitializationService initializationService;

	@Autowired
	private TenantContextHelper tenantContextHelper;

	@ManagedAttribute(description = "Total number of orders placed in last hour.")
	@ManagedOperation(description = "Total number of orders placed in last hour.")
	@Override
	public long getNumberOfAllOrdersPlacedInLastHour()
	{
		return getTimeStatistics(OrderData.class, false, TimeStatisticsType.HOUR);
	}

	@ManagedOperation(description = "Number of orders placed in last hour for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public long getNumberOfOrdersPlacedInLastHourForTenant(final String tenantId)
	{
		return getTimeStatisticsForTenant(OrderData.class, false, TimeStatisticsType.HOUR, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their numbers of orders placed in last hour.")
	@ManagedOperation(description = "Mapping between all the tenants and their numbers of orders placed in last hour.")
	@Override
	public Map<String, Long> getNumberOfOrdersPlacedInLastHourPerTenant()
	{
		return getTimeStatisticsPerTenant(OrderData.class, false, TimeStatisticsType.HOUR);
	}

	@ManagedAttribute(description = "Total number of orders placed in last day.")
	@ManagedOperation(description = "Total number of orders placed in last day.")
	@Override
	public long getNumberOfAllOrdersPlacedInLastDay()
	{
		return getTimeStatistics(OrderData.class, false, TimeStatisticsType.DAY);
	}

	@ManagedOperation(description = "Number of orders placed in last day for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public long getNumberOfOrdersPlacedInLastDayForTenant(final String tenantId)
	{
		return getTimeStatisticsForTenant(OrderData.class, false, TimeStatisticsType.DAY, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their numbers of orders placed in last day.")
	@ManagedOperation(description = "Mapping between all the tenants and their numbers of orders placed in last day.")
	@Override
	public Map<String, Long> getNumberOfOrdersPlacedInLastDayPerTenant()
	{
		return getTimeStatisticsPerTenant(OrderData.class, false, TimeStatisticsType.DAY);
	}

	@ManagedAttribute(description = "Total number of inventories added in the last hour.")
	@ManagedOperation(description = "Total number of inventories added in the last hour.")
	@Override
	public long getNumberOfAllInventoriesAddedInLastHour()
	{
		return getTimeStatistics(CurrentItemQuantityData.class, false, TimeStatisticsType.HOUR);
	}

	@ManagedOperation(description = "Number of inventories added in the last hour for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public long getNumberOfInventoriesAddedInLastHourForTenant(final String tenantId)
	{
		return getTimeStatisticsForTenant(CurrentItemQuantityData.class, false, TimeStatisticsType.HOUR, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their numbers of inventories added in the last hour.")
	@ManagedOperation(description = "Mapping between all the tenants and their numbers of inventories added in the last hour.")
	@Override
	public Map<String, Long> getNumberOfInventoriesAddedInLastHourPerTenant()
	{
		return getTimeStatisticsPerTenant(CurrentItemQuantityData.class, false, TimeStatisticsType.HOUR);
	}

	@ManagedAttribute(description = "Total number of inventories added in the last day.")
	@ManagedOperation(description = "Total number of inventories added in the last day.")
	@Override
	public long getNumberOfAllInventoriesAddedInLastDay()
	{
		return getTimeStatistics(CurrentItemQuantityData.class, false, TimeStatisticsType.DAY);
	}

	@ManagedOperation(description = "Number of inventories added in the last day for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public long getNumberOfInventoriesAddedInLastDayForTenant(final String tenantId)
	{
		return getTimeStatisticsForTenant(CurrentItemQuantityData.class, false, TimeStatisticsType.DAY, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their numbers of inventories added in the last day.")
	@ManagedOperation(description = "Mapping between all the tenants and their numbers of inventories added in the last day.")
	@Override
	public Map<String, Long> getNumberOfInventoriesAddedInLastDayPerTenant()
	{
		return getTimeStatisticsPerTenant(CurrentItemQuantityData.class, false, TimeStatisticsType.DAY);
	}

	@ManagedAttribute(description = "Total number of ATS values calculated in last hour.")
	@ManagedOperation(description = "Total number of ATS values calculated in last hour.")
	@Override
	public long getNumberOfAllATSValuesCalculatedInLastHour()
	{
		return getTimeStatistics(CurrentItemQuantityData.class, true, TimeStatisticsType.HOUR);
	}

	@ManagedOperation(description = "Number of ATS values calculated in last hour for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public long getNumberOfATSValuesCalculatedInLastHourForTenant(final String tenantId)
	{
		return getTimeStatisticsForTenant(CurrentItemQuantityData.class, true, TimeStatisticsType.HOUR, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their numbers of ATS values calculated in last hour.")
	@ManagedOperation(description = "Mapping between all the tenants and their numbers of ATS values calculated in last hour.")
	@Override
	public Map<String, Long> getNumberOfATSValuesCalculatedInLastHourPerTenant()
	{
		return getTimeStatisticsPerTenant(CurrentItemQuantityData.class, true, TimeStatisticsType.HOUR);
	}

	@ManagedAttribute(description = "Total number of ATS values calculated in last day.")
	@ManagedOperation(description = "Total number of ATS values calculated in last day.")
	@Override
	public long getNumberOfAllATSValuesCalculatedInLastDay()
	{
		return getTimeStatistics(CurrentItemQuantityData.class, true, TimeStatisticsType.DAY);
	}

	@ManagedOperation(description = "Number of ATS values calculated in last day for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public long getNumberOfATSValuesCalculatedInLastDayForTenant(final String tenantId)
	{
		return getTimeStatisticsForTenant(CurrentItemQuantityData.class, true, TimeStatisticsType.DAY, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their numbers of ATS values calculated in last day.")
	@ManagedOperation(description = "Mapping between all the tenants and their numbers of ATS values calculated in last day.")
	@Override
	public Map<String, Long> getNumberOfATSValuesCalculatedInLastDayPerTenant()
	{
		return getTimeStatisticsPerTenant(CurrentItemQuantityData.class, true, TimeStatisticsType.DAY);
	}

	@ManagedOperation(description = "Order line quantity statuses for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public Map<String, Long> getStatusesOfOQLsForTenant(final String tenantId)
	{
		return getQuantityStatisticsPerStatusForTenant(AggOrderLineQuantityStatus.class, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their order line quantity statuses.")
	@ManagedOperation(description = "Mapping between all the tenants and their order line quantity statuses.")
	@Override
	public Map<String, Map<String, Long>> getStatusesOfOQLsPerTenant()
	{
		return getQuantityStatisticsPerStatusPerTenant(AggOrderLineQuantityStatus.class);
	}

	@ManagedAttribute(description = "Total number of stockroom locations.")
	@ManagedOperation(description = "Total number of stockroom locations.")
	@Override
	public long getNumberOfAllStockRoomLocations()
	{
		return getQuantityStatistics(AggStockroomLocation.class);
	}

	@ManagedOperation(description = "Number of stockroom locations for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public long getNumberOfStockRoomLocationsForTenant(final String tenantId)
	{
		return getQuantityStatisticsForTenant(AggStockroomLocation.class, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their numbers of stockroom locations.")
	@ManagedOperation(description = "Mapping between all the tenants and their numbers of stockroom locations.")
	@Override
	public Map<String, Long> getNumberOfStockRoomLocationsPerTenant()
	{
		return getQuantityStatisticsPerTenant(AggStockroomLocation.class);
	}

	@ManagedOperation(description = "Number of inventories for given tenant.")
	@ManagedOperationParameters({@ManagedOperationParameter(description = "Tenant id", name = "tenantId")})
	@Override
	public Map<String, Long> getNumbersOfInventoriesForTenant(final String tenantId)
	{
		return getQuantityStatisticsPerStatusForTenant(AggCurrentItemQuantity.class, tenantId);
	}

	@ManagedAttribute(description = "Mapping between all the tenants and their numbers of inventories.")
	@ManagedOperation(description = "Mapping between all the tenants and their numbers of inventories.")
	@Override
	public Map<String, Map<String, Long>> getNumbersOfInventoriesPerTenant()
	{
		return getQuantityStatisticsPerStatusPerTenant(AggCurrentItemQuantity.class);
	}

	private long getTimeStatistics(final Class<? extends ManagedObject> mClass, final boolean update,
			final TimeStatisticsType timeStatisticsType)
	{
		return metricCollector.getMetricValueOverAllTenants(
				mClass.getSimpleName() + (update ? EventStatisticsType.CREATE_UPDATE.name() : EventStatisticsType.CREATE.name()),
				timeStatisticsType.getTimeCode());
	}

	private Long getTimeStatisticsForTenant(final Class<? extends ManagedObject> mClass, final boolean update,
			final TimeStatisticsType statisticsType, final String tenantId)
	{
		return metricCollector.getMetric(tenantId, mClass.getSimpleName()
				+ (update ? EventStatisticsType.CREATE_UPDATE.name() : EventStatisticsType.CREATE.name()),
				statisticsType.getTimeCode());
	}

	private Map<String, Long> getTimeStatisticsPerTenant(final Class<? extends ManagedObject> mClass, final boolean update,
			final TimeStatisticsType statisticsType)
	{
		return metricCollector.getMetricForAllTenants(mClass.getSimpleName()
				+ (update ? EventStatisticsType.CREATE_UPDATE.name() : EventStatisticsType.CREATE.name()),
				statisticsType.getTimeCode());
	}

	private long getQuantityStatistics(final Class<? extends QuantityCountingAggregation> aggregationClass)
	{
		return tenantContextHelper.getSumForTenants(new CountingOperation<Long>()
		{
			@Override
			public Long count()
			{
				return DefaultHealthService.this.aggregationService.getAggregate(aggregationClass).getQuantity();
			}
		}, initializationService.getInitializedTenants());
	}

	private Long getQuantityStatisticsForTenant(final Class<? extends QuantityCountingAggregation> aggregationClass,
			final String tenantId)
	{
		return tenantContextHelper.getForTenant(new CountingOperation<Long>()
		{
			@Override
			public Long count()
			{
				return DefaultHealthService.this.aggregationService.getAggregate(aggregationClass).getQuantity();
			}
		}, tenantId);
	}

	private Map<String, Long> getQuantityStatisticsPerTenant(final Class<? extends QuantityCountingAggregation> aggregationClass)
	{
		return tenantContextHelper.getPerTenant(new CountingOperation<Long>()
		{
			@Override
			public Long count()
			{
				return DefaultHealthService.this.aggregationService.getAggregate(aggregationClass).getQuantity();
			}
		}, initializationService.getInitializedTenants());
	}

	private Map<String, Long> getQuantityStatisticsPerStatusForTenant(
			final Class<? extends StatusQuantityCountingAggregation> aggregationClass, final String tenantId)
	{
		return tenantContextHelper.getForTenant(new CountingOperation<Map<String, Long>>()
		{
			@Override
			public Map<String, Long> count()
			{
				final Map<String, Long> result = new HashMap<>();
				final Collection<? extends StatusQuantityCountingAggregation> statuses = //
				DefaultHealthService.this.aggregationService.getAggregates(aggregationClass);

				for (final StatusQuantityCountingAggregation status : statuses)
				{
					result.put(status.getStatusCode(), status.getQuantity());
				}
				return result;
			}
		}, tenantId);

	}

	private Map<String, Map<String, Long>> getQuantityStatisticsPerStatusPerTenant(
			final Class<? extends StatusQuantityCountingAggregation> aggregationClass)
	{
		return tenantContextHelper.getPerTenant(new CountingOperation<Map<String, Long>>()
		{
			@Override
			public Map<String, Long> count()
			{
				final Map<String, Long> result = new HashMap<>();
				final Collection<? extends StatusQuantityCountingAggregation> statuses = //
				DefaultHealthService.this.aggregationService.getAggregates(aggregationClass);

				for (final StatusQuantityCountingAggregation status : statuses)
				{
					result.put(status.getStatusCode(), status.getQuantity());
				}
				return result;
			}
		}, initializationService.getInitializedTenants());

	}

	public void setAggregationService(final AggregationService aggregationService)
	{
		this.aggregationService = aggregationService;
	}

	public void setTenantContextHelper(final TenantContextHelper tenantContextHelper)
	{
		this.tenantContextHelper = tenantContextHelper;
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
