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
package com.hybris.oms.service.health;

import java.util.Map;


/**
 * Service for checking the system health by exposing methods via JMX.
 */

public interface HealthService
{

	/**
	 * Returns the number of orders which have been placed over the last hour,
	 * for all the tenants.
	 * 
	 * @return number of all orders
	 */
	long getNumberOfAllOrdersPlacedInLastHour();

	/**
	 * Returns the number of orders which have been placed over the last hour for given tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return number of orders for tenant
	 */
	long getNumberOfOrdersPlacedInLastHourForTenant(String tenantId);

	/**
	 * Returns the number of orders which have been placed over the last hour per each tenant.
	 * 
	 * @return number of orders per tenant
	 */
	Map<String, Long> getNumberOfOrdersPlacedInLastHourPerTenant();

	/**
	 * Returns the number of orders which have been placed over the last day (24 hours),
	 * for all the tenants.
	 * 
	 * @return number of all orders
	 */
	long getNumberOfAllOrdersPlacedInLastDay();

	/**
	 * Returns the number of orders which have been placed over the last day (24 hours) for given tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return number of orders for tenant
	 */
	long getNumberOfOrdersPlacedInLastDayForTenant(String tenantId);

	/**
	 * Returns the number of orders which have been placed over the last day (24 hours) per each tenant.
	 * 
	 * @return number of orders per tenant
	 */
	Map<String, Long> getNumberOfOrdersPlacedInLastDayPerTenant();

	/**
	 * Returns the current number of order quantity lines (OQLs) in each status for given tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return mapping between the order status and number of orders in this status for given tenant
	 */
	Map<String, Long> getStatusesOfOQLsForTenant(String tenantId);

	/**
	 * Returns the current number of order quantity lines (OQLs) in each status per each tenant.
	 * 
	 * @return mapping between the order status and number of orders in this status per tenant
	 */
	Map<String, Map<String, Long>> getStatusesOfOQLsPerTenant();

	/**
	 * Returns the current number of stockroom locations, for all the tenants.
	 * 
	 * @return number of all stockroom locations
	 */
	long getNumberOfAllStockRoomLocations();

	/**
	 * Returns the current number of stockroom locations for given tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return number of stockroom locations for given tenant
	 */
	long getNumberOfStockRoomLocationsForTenant(String tenantId);

	/**
	 * Returns the current number of stockroom locations per each tenant.
	 * 
	 * @return number of stockroom locations per tenant
	 */
	Map<String, Long> getNumberOfStockRoomLocationsPerTenant();

	/**
	 * Returns the current number of inventories in each status for given each tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return mapping between inventory statuses and number of inventories for given tenant
	 */
	Map<String, Long> getNumbersOfInventoriesForTenant(String tenantId);

	/**
	 * Returns the current number of inventories in each status per each tenant.
	 * 
	 * @return mapping between inventory statuses and number of inventories per tenant
	 */
	Map<String, Map<String, Long>> getNumbersOfInventoriesPerTenant();


	/**
	 * Returns the number of inventories over the last hour, for all the tenants.
	 * 
	 * @return number of inventories
	 */
	long getNumberOfAllInventoriesAddedInLastHour();

	/**
	 * Returns the number of inventories over the last hour for given tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return number inventories for given tenant
	 */
	long getNumberOfInventoriesAddedInLastHourForTenant(String tenantId);

	/**
	 * Returns the number of inventories over the last hour per each tenant.
	 * 
	 * @return number inventories per tenant
	 */
	Map<String, Long> getNumberOfInventoriesAddedInLastHourPerTenant();

	/**
	 * Returns the number of inventories over the last day (24 hours), for all the tenants.
	 * 
	 * @return number of inventories
	 */
	long getNumberOfAllInventoriesAddedInLastDay();

	/**
	 * Returns the number of inventories over the last day (24 hours) for given tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return number inventories for given tenant
	 */
	long getNumberOfInventoriesAddedInLastDayForTenant(String tenantId);

	/**
	 * Returns the number of inventories over the last day (24 hours) per each tenant.
	 * 
	 * @return number inventories per tenant
	 */
	Map<String, Long> getNumberOfInventoriesAddedInLastDayPerTenant();


	/**
	 * Returns the number of ATS values calculated over the last hour, for all the tenants.
	 * 
	 * @return the number of ATS values
	 */
	long getNumberOfAllATSValuesCalculatedInLastHour();

	/**
	 * Returns the number of ATS values calculated over the last hour for given tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return the number of ATS values
	 */
	long getNumberOfATSValuesCalculatedInLastHourForTenant(String tenantId);

	/**
	 * Returns the number of ATS values calculated over the last hour per tenant.
	 * 
	 * @return the number of ATS values
	 */
	Map<String, Long> getNumberOfATSValuesCalculatedInLastHourPerTenant();

	/**
	 * Returns the number of ATS values calculated over the last day (24 hours), for all the tenants.
	 * 
	 * @return the number of ATS values
	 */
	long getNumberOfAllATSValuesCalculatedInLastDay();

	/**
	 * Returns the number of ATS values calculated over the last day (24 hours) for given tenant.
	 * 
	 * @param tenantId the tenant id
	 * @return the number of ATS values
	 */
	long getNumberOfATSValuesCalculatedInLastDayForTenant(String tenantId);

	/**
	 * Returns the number of ATS values calculated over the last day (24 hours) per tenant.
	 * 
	 * @return the number of ATS values
	 */
	Map<String, Long> getNumberOfATSValuesCalculatedInLastDayPerTenant();
}
