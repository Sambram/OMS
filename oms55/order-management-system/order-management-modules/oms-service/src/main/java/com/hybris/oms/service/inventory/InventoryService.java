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
package com.hybris.oms.service.inventory;

import com.hybris.kernel.api.Page;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.service.Flushable;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * The Interface InventoryService.
 */
public interface InventoryService extends Flushable
{


	/**
	 * Creates a new Item Quantity. {@link ItemLocationData} is created implicitly. If expectedDeliveryDate is
	 * null, {@link CurrentItemQuantityData} is created, otherwise {@link FutureItemQuantityData}.
	 *
	 * @return the ItemQuantityData created
	 *
	 * @throws EntityValidationException if the quantity exists already or contains validation errors
	 */
	ItemQuantityData createItemQuantity(final String sku, final String locationId, final String binCode, final String statusCode,
			final String unitCode, final int quantity, final Date expectedDeliveryDate) throws EntityValidationException;

	/**
	 * Creates or updates an item quantity. {@link ItemLocationData} is created implicitely. If expectedDeliveryDate is
	 * null, {@link CurrentItemQuantityData} is created, otherwise {@link FutureItemQuantityData}.
	 *
	 * @return the ItemQuantityData created or updated
	 *
	 * @throws EntityValidationException contains validation errors
	 */
	ItemQuantityData createUpdateItemQuantity(final String sku, final String locationId, final String binCode,
			final String statusCode, final String unitCode, final int quantity, final Date expectedDeliveryDate)
					throws EntityValidationException;

	/**
	 * Create a new item status.
	 *
	 * @param inventoryStatus
	 *           the inventory status
	 * @return created item status
	 */
	ItemStatusData createItemStatus(final ItemStatusData inventoryStatus);

	/**
	 * Create a new location.
	 *
	 * @param location
	 *           the location
	 * @return created location
	 * @throws EntityValidationException
	 */
	StockroomLocationData createLocation(final StockroomLocationData location) throws EntityValidationException;

	/**
	 * Deletes a single ItemQuantity, and also the ItemLocation, if it is the only ItemQuantity inside it.
	 *
	 * @throws EntityNotFoundException if no matching item quantity was found
	 */
	void deleteItemQuantity(final String sku, final String locationId, final String binCode, final String statusCode,
			final Date expectedDeliveryDate) throws EntityNotFoundException;

	/**
	 * Get a list of all item statuses.
	 *
	 * @return item statuses list
	 */
	List<ItemStatusData> findAllItemStatuses();

	/**
	 * Get a current item quantity by skuId+locationId+statusCode.
	 *
	 * @param skuId
	 * @param locationId
	 * @param binCode
	 * @param statusCode
	 * @return a current item quantity
	 * @throws EntityNotFoundException
	 */
	CurrentItemQuantityData findCurrentItemQuantityBySkuLocationBinCodeStatus(final String skuId, final String locationId,
			final String binCode, final String statusCode) throws EntityNotFoundException;

	/**
	 * Get a future item quantity by skuId+locationId+statusCode+expectedDate.
	 *
	 * @param skuId
	 * @param locationId
	 * @param binCode
	 * @param statusCode
	 * @param expectedDate
	 * @return a future item quantity
	 * @throws EntityNotFoundException
	 */
	FutureItemQuantityData findFutureItemQuantityBySkuLocationBinCodeStatusDate(final String skuId, final String locationId,
			final String binCode, final String statusCode, final Date expectedDate) throws EntityNotFoundException;

	/**
	 * Get an item location current by sku id and location id.
	 *
	 * @param skuId
	 *           the sku id
	 * @param locationId
	 *           the location id
	 * @return current item location
	 * @throws EntityNotFoundException
	 */
	ItemLocationData findItemLocationCurrentBySkuIdAndLocationId(final String skuId, final String locationId)
			throws EntityNotFoundException;

	/**
	 * Get an item location future by sku id and location id.
	 *
	 * @param skuId
	 * @param locationId
	 * @return
	 * @throws EntityNotFoundException
	 */
	ItemLocationData findItemLocationFutureBySkuLocation(final String skuId, final String locationId)
			throws EntityNotFoundException;

	/**
	 * Find {@link ItemLocationData}s for the given query restrictions.
	 *
	 * @param queryObject The query object with the restrictions.
	 * @return A list of {@link ItemLocationData} in a paged format.
	 */
	Page<ItemLocationData> findPagedItemLocationsByQuery(final ItemLocationsQueryObject queryObject);

	/**
	 * Finds bins by Sku and locationId.
	 *
	 * @param skuId
	 * @param locationId
	 * @return list of {@link BinData}
	 */
	List<BinData> findBinsBySkuAndLocationId(final String skuId, final String locationId);

	/**
	 * Get a list of all locations in pages.
	 *
	 * @return locations list
	 */
	Page<StockroomLocationData> findPagedLocations(final LocationQueryObject queryObject);

	/**
	 * Get a list of locations by queryObject.
	 *
	 * @param queryObject
	 * @param searchByWildcardAllowed allow for searching with wildcards
	 * @return list with locations
	 */
	List<StockroomLocationData> findAllLocationsByQueryObject(final LocationQueryObject queryObject,
			boolean searchByWildcardAllowed);

	/**
	 * Get an item status by status code.
	 *
	 * @param statusCode
	 *           the status code
	 * @return item status
	 */
	ItemStatusData getItemStatusByStatusCode(final String statusCode);

	/**
	 * Get a location by location id.
	 *
	 * @param locationId
	 *           the location id
	 * @return the location
	 * @throws EntityNotFoundException
	 */
	StockroomLocationData getLocationByLocationId(final String locationId) throws EntityNotFoundException;

	/**
	 * Update the quantity of a Current Item Quantity. The update is not incremental, i.e., if old value was 3,
	 * and param quantity=5, the resulting value will be 5.
	 *
	 * @return the updated item quantity
	 * @throws EntityNotFoundException if CurrentItemQuantity doesn't exists in DB
	 * @throws EntityValidationException
	 */
	CurrentItemQuantityData updateCurrentItemQuantity(final String locationId, final String binCode, final String skuId,
			final String statusCode, final int quantity) throws EntityNotFoundException, EntityValidationException;

    /**
     * Ban all item locations with the same skuId + locationId to avoid source from this place,
     * until an update arrives and removes the banned property automatically,
     * allowing the sourcing to use this item location again.
     *
     * @param skuId
     * @param locationId
     * @return List of ItemLocationData
     * @throws EntityNotFoundException
     */
    List<ItemLocationData> banItemLocations(final String skuId, final String locationId) throws EntityNotFoundException;

	/**
	 * Incrementally update the quantity of a Current Item Quantity. The update is incremental, i.e., if old value was 3,
	 * and param quantity=5, the resulting value will be 8.
	 *
	 * @param locationId
	 * @param binCode
	 * @param skuId
	 * @param statusCode
	 * @param quantity
	 * @return the updated item quantity
	 * @throws EntityNotFoundException if CurrentItemQuantity doesn't exists in DB
	 * @throws EntityValidationException
	 */
	CurrentItemQuantityData updateCurrentItemQuantityIncremental(final String locationId, final String binCode,
			final String skuId, final String statusCode, final int quantity) throws EntityNotFoundException,
			EntityValidationException;


	/**
	 * Update the quantity of a Future Item Quantity. The update is not incremental, i.e., if old value was 3,
	 * and param quantity=5, the resulting value will be 5.
	 *
	 * @return the updated item quantity
	 * @throws EntityNotFoundException if FutureItemQuantity doesn't exists in DB
	 * @throws EntityValidationException
	 */
	FutureItemQuantityData updateFutureItemQuantity(final String locationId, final String binCode, final String skuId,
			final String statusCode, final int quantity, final Date expectedDate) throws EntityNotFoundException,
			EntityValidationException;

	/**
	 * Incrementally update the quantity of a Future Item Quantity. The update is incremental, i.e., if old value was 3,
	 * and param quantity=5, the resulting value will be 8.
	 *
	 * @param locationId
	 * @param binCode
	 * @param skuId
	 * @param statusCode
	 * @param quantity
	 * @param expectedDate
	 * @return the updated item quantity
	 * @throws EntityNotFoundException if FutureItemQuantity doesn't exists in DB
	 * @throws EntityValidationException
	 */
	FutureItemQuantityData updateFutureItemQuantityIncremental(final String locationId, final String binCode, final String skuId,
			final String statusCode, final int quantity, final Date expectedDate) throws EntityNotFoundException,
			EntityValidationException;

	/**
	 * Update a location (stock room location).
	 *
	 * @param location
	 *           the location
	 * @return updated location
	 * @throws EntityValidationException
	 */
	StockroomLocationData updateLocation(final StockroomLocationData location) throws EntityValidationException;

	/**
	 * Get a list of all locations for the given locationIds.
	 *
	 * @return locations list
	 */
	List<StockroomLocationData> findLocationsByLocationIds(Set<String> locationIds);

	/**
	 * Creates a bin.
	 *
	 * @param bin the bin data to persist
	 * @return the persisted bin
	 * @throws EntityValidationException if the bin is not valid or if it already exists
	 */
	BinData createBin(final BinData bin) throws EntityValidationException;

	/**
	 * Updates a bin.
	 *
	 * @param bin the bin data to update
	 * @return the update bin
	 * @throws EntityValidationException if the bin is not valid
	 */
	BinData updateBin(final BinData bin) throws EntityValidationException;

	/**
	 * Deletes a bin.
	 *
	 * @param bin the bin data to delete
	 * @throws EntityValidationException if the bin is not valid
	 */
	void deleteBin(final BinData bin) throws EntityValidationException;


	/**
	 * Finds Bins by query object.
	 *
	 * @param queryObject
	 * @return page of bins
	 *
	 */
	Page<BinData> findPagedBinsByQuery(BinQueryObject queryObject);

	/**
	 * Gets a bin.
	 *
	 * @param binCode
	 * @param locationId
	 * @return the bin that was found
	 * @throws EntityValidationException if the bin is not valid
	 */
	BinData getBinByBinCodeLocationId(final String binCode, final String locationId) throws EntityValidationException;


	/**
	 * Get all item locations current and future by sku id and location id.
	 *
	 * @param skuId      the sku id
	 * @param locationId the location id
	 * @return list of item locations
	 * @throws EntityNotFoundException
	 */
	List<ItemLocationData> findAllItemLocationsBySkuAndLocation(final String skuId, final String locationId)
			throws EntityNotFoundException;
}
