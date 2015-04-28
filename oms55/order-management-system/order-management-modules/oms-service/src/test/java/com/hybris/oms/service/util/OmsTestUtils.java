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
package com.hybris.oms.service.util;

import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.JobDetails;
import com.hybris.kernel.api.JobSchedulerService;
import com.hybris.kernel.api.ManagedObject;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.kernel.regioncache.CacheController;
import com.hybris.oms.service.inventory.impl.dataaccess.AggItemQuantityByItemIdLocationStatus;
import com.hybris.oms.service.inventory.impl.dataaccess.AggItemQuantityByItemIdStatus;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.i18n.CurrencyData;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData;
import com.hybris.oms.service.managedobjects.returns.ReturnShipmentData;
import com.hybris.oms.service.managedobjects.rule.RuleData;
import com.hybris.oms.service.managedobjects.rule.RuleElementData;
import com.hybris.oms.service.managedobjects.rule.RuleParameterData;
import com.hybris.oms.service.managedobjects.shipment.DeliveryData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.order.impl.dataaccess.AggOrderLineQuantityBySkuLocationStatus;
import com.hybris.oms.service.order.impl.dataaccess.AggOrderLineQuantityBySkuStatus;
import com.hybris.oms.service.order.impl.dataaccess.AggQuantityUnassignedBySku;
import com.hybris.oms.service.workflow.WorkflowException;

import java.util.List;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility methods for tests.
 */
public final class OmsTestUtils
{

	private static final Logger LOG = LoggerFactory.getLogger(OmsTestUtils.class);

	private static final String SINGLE = "single";

	private static final int DELAY_MILLIS = 100;
	private static final int TIMEOUT_FOR_RETRY_MILLIS = 10000;

	private static final int RETRIES = 50;

	private static final Class<?>[] AGGREGATES = new Class<?>[]{AggOrderLineQuantityBySkuStatus.class,
		AggOrderLineQuantityBySkuLocationStatus.class, AggItemQuantityByItemIdStatus.class,
		AggItemQuantityByItemIdLocationStatus.class, AggQuantityUnassignedBySku.class};

	private OmsTestUtils()
	{
		// do not instantiate
	}

	/**
	 * Clears all caches.
	 */
	public static void clearCaches(final CacheController cacheController)
	{
		cacheController.clearCache();
	}

	/**
	 * Clears all Aggregates.
	 */
	public static void clearAggregates(final AggregationService aggregationService)
	{
		for (final Class<?> clazz : AGGREGATES)
		{
			aggregationService.clearAggregates(clazz);
		}
	}

	/**
	 * Recalculate all Aggregates.
	 */
	public static void recalculateAggregates(final AggregationService aggregationService)
	{
		for (final Class<?> clazz : AGGREGATES)
		{
			aggregationService.recalculateAggregation(clazz);
		}
	}

	/**
	 * Waits until inventory for sku is available.
	 */
	public static void waitForInventory(final AggregationService aggregationService, final String sku)
	{
		waitForAggregates(aggregationService, sku, AggItemQuantityByItemIdStatus.class, AggItemQuantityByItemIdLocationStatus.class);
	}

	public static void waitForAllAggregates(final AggregationService aggregationService, final String... skus)
	{
		for (final String sku : skus)
		{
			waitForAggregates(aggregationService, sku, AGGREGATES);
		}
	}

	/**
	 * Waits until inventory for sku is available.
	 */
	@SafeVarargs
	private static void waitForAggregates(final AggregationService aggregationService, final String sku,
			final Class<?>... aggregateClazz)
	{
		for (final Class<?> clazz : aggregateClazz)
		{
			for (int i = 0; i < RETRIES; i++)
			{
				if (!aggregationService.getAggregates(clazz, sku).isEmpty())
				{
					break;
				}
				OmsTestUtils.delay();
			}
		}
	}

	/**
	 * Wait for no active Activiti process.</br> Timeout by {@link #TIMEOUT_FOR_RETRY_MILLIS}
	 *
	 * @param runtimeService
	 * @return false if timeout, else true
	 */
	public static boolean waitForProcesses(final RuntimeService runtimeService)
	{
		final long start = System.currentTimeMillis();

		boolean result = false;
		int retry = 0;

		while (!result && (System.currentTimeMillis() - start < TIMEOUT_FOR_RETRY_MILLIS))
		{

			if (retry > 0)
			{
				OmsTestUtils.delay();
				LOG.debug("waitForProcesses::retry({})", retry);
			}

			result = (runtimeService.createProcessInstanceQuery().active().count() <= 0);

			retry++;
		}

		if (!result)
		{
			LOG.warn("waitForProcesses::timeout!");
		}

		return result;
	}

	/**
	 * Wait until the workflow representing the given {@link ManagedObject} arrives at a given task.
	 *
	 * @param taskId
	 *           - the task's activity id in the BPMN diagram
	 * @param businessKey
	 *           - the workflow's unique business key
	 * @param runtimeService
	 *
	 * @return false if timeout, else true
	 */
	public static boolean waitForWorkflowTask(final String taskId, final String businessKey, final RuntimeService runtimeService)
	{

		final long start = System.currentTimeMillis();

		boolean result = false;
		int retry = 0;

		while (!result && (System.currentTimeMillis() - start < TIMEOUT_FOR_RETRY_MILLIS))
		{

			if (retry > 0)
			{
				OmsTestUtils.delay();
				LOG.debug("waitForWorkflowTask::retry({})", retry);
			}

			final Execution execution = runtimeService.createExecutionQuery().processInstanceBusinessKey(businessKey).singleResult();
			result = (execution != null && StringUtils.equals(execution.getActivityId(), taskId));

			// Check for child executions
			if (!result)
			{
				final String processInstanceId = execution.getProcessInstanceId();
				final List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
				if (CollectionUtils.isEmpty(executions))
				{
					throw new WorkflowException("No executions found for processinstance id [" + processInstanceId + "].");
				}
				for (final Execution executionInternal : executions)
				{
					if (executionInternal != null && StringUtils.equals(executionInternal.getActivityId(), taskId))
					{
						result = Boolean.TRUE;
						break;
					}
				}
			}

			retry++;
		}

		if (!result)
		{
			LOG.warn("waitForWorkflowTask::timeout!");
		}

		return result;

	}

	/**
	 * Delays execution.
	 */
	public static void delay()
	{
		delay(DELAY_MILLIS);
	}

	/**
	 * Delays execution by millis milliseconds.
	 */
	public static void delay(final int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (@SuppressWarnings("unused") final InterruptedException ignored)
		{
			// ignore
		}
	}

	/**
	 * Clean up the data in JDBC.
	 */
	public static void cleanUp(final JdbcPersistenceEngine persistenceEngine)
	{
		persistenceEngine.removeAll(ShippingAndHandlingData._TYPECODE);
		persistenceEngine.removeAll(PaymentInfoData._TYPECODE);
		persistenceEngine.removeAll(OrderLineQuantityData._TYPECODE);
		persistenceEngine.removeAll(OrderLineData._TYPECODE);
		persistenceEngine.removeAll(OrderLineAttributeData._TYPECODE);
		persistenceEngine.removeAll(OrderData._TYPECODE);
		persistenceEngine.removeAll(OrderLineQuantityStatusData._TYPECODE);
		persistenceEngine.removeAll(ItemStatusData._TYPECODE);
		persistenceEngine.removeAll(DeliveryData._TYPECODE);
		persistenceEngine.removeAll(ShipmentData._TYPECODE);
		persistenceEngine.removeAll(AtsFormulaData._TYPECODE);
		persistenceEngine.removeAll(StockroomLocationData._TYPECODE);
		persistenceEngine.removeAll(ItemQuantityData._TYPECODE);
		persistenceEngine.removeAll(FutureItemQuantityData._TYPECODE);
		persistenceEngine.removeAll(CurrentItemQuantityData._TYPECODE);
		persistenceEngine.removeAll(ItemLocationData._TYPECODE);
		persistenceEngine.removeAll(TenantPreferenceData._TYPECODE);
		persistenceEngine.removeAll(RuleParameterData._TYPECODE);
		persistenceEngine.removeAll(RuleElementData._TYPECODE);
		persistenceEngine.removeAll(RuleData._TYPECODE);
		persistenceEngine.removeAll(CountryData._TYPECODE);
		persistenceEngine.removeAll(CurrencyData._TYPECODE);
		persistenceEngine.removeAll(BaseStoreData._TYPECODE);
		persistenceEngine.removeAll(BinData._TYPECODE);
		persistenceEngine.removeAll(ReturnData._TYPECODE);
		persistenceEngine.removeAll(ReturnPaymentInfoData._TYPECODE);
		persistenceEngine.removeAll(ReturnOrderLineData._TYPECODE);
		persistenceEngine.removeAll(ReturnShipmentData._TYPECODE);
	}

	public static void unscheduleJobs(final JobSchedulerService scheduler)
	{
		for (final JobDetails job : scheduler.getAll(SINGLE))
		{
			scheduler.delete(job);
		}
	}

}
