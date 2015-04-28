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
package com.hybris.oms.api.inventory;

import java.util.Date;
import java.util.List;

import com.hybris.oms.api.Pageable;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.LocationQueryObject;


/**
 * Facade for managing inventory related data.
 */
@SuppressWarnings("PMD.TooManyPublicMethods")
public interface InventoryFacade
{

	/**
	 * Creates a new inventory entry. The entry is based on sku + location + statusCode (+expectedDate, if it is a future
	 * inventory).
	 * 
	 * @category DATA ONBOARDING
	 * 
	 * @param inventory the inventory to be created <dt><b>Preconditions:</b>
	 *           <dd>
	 *           inventory must not be null.
	 *           <dd>
	 *           inventory.skuId must not be empty.
	 *           <dd>
	 *           inventory.locationId must exist in OMS.
	 *           <dd>
	 *           inventory.status must not be empty.
	 *           <dd>
	 *           inventory.status must exist in OMS.
	 * @return the created inventory
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws DuplicateEntityException if the inventory item already exists
	 */
	OmsInventory createInventory(OmsInventory inventory) throws EntityValidationException, DuplicateEntityException;

	/**
	 * Create new item status.
	 * 
	 * @category OMS-UI
	 * 
	 * @param itemStatus the item status to be created <dt><b>Preconditions:</b>
	 *           <dd>
	 *           ItemStatus.statusCode must not be empty
	 *           <dd>
	 *           ItemStatus.description must not be empty
	 * 
	 * @return created item status
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws DuplicateEntityException if the item status already exists
	 */
	ItemStatus createItemStatus(ItemStatus itemStatus) throws EntityValidationException, DuplicateEntityException;

	/**
	 * Create new location.
	 * 
	 * @category DATA ONBOARDING
	 * 
	 * @param location the location to create.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           location must not be null.
	 *           <dd>
	 *           location.locationId must not be empty.
	 *           <dd>
	 *           location.percentageInventoryThreshold must be equal or greater than zero.
	 *           <dd>
	 *           location.percentageInventoryThreshold must be less than 100.
	 *           <dd>
	 *           location.inventoryThreshold must be equal or greater than zero.
	 * 
	 * @return created location
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws DuplicateEntityException if the stockroom location already exists
	 */
	Location createStockRoomLocation(final Location location) throws EntityValidationException, DuplicateEntityException;

	/**
	 * Updates an inventory entry or create a new one if it doesn't exist.
	 * 
	 * @category DATA ONBOARDING
	 * @category OMS-UI
	 * 
	 * @param inventory to create or update.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           inventory must not be null.
	 *           <dd>
	 *           inventory.skuId must not be empty.
	 *           <dd>
	 *           inventory.locationId must exist in OMS.
	 *           <dd>
	 *           inventory.status must not be empty.
	 *           <dd>
	 *           inventory.status must exist in OMS.
	 * @return the created/updated inventory
	 * @throws EntityValidationException if preconditions are not met.
	 */
	OmsInventory createUpdateInventory(OmsInventory inventory) throws EntityValidationException;

	/**
	 * Creates or updates a location.
	 * 
	 * @category DATA ONBOARDING
	 * @category OMS-UI
	 * 
	 * @param location the location to create.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           location must not be null.
	 *           <dd>
	 *           location.locationId must not be empty.
	 *           <dd>
	 *           location.percentageInventoryThreshold must be equal or greater than zero.
	 *           <dd>
	 *           location.percentageInventoryThreshold must be less than 100.
	 *           <dd>
	 *           location.inventoryThreshold must be equal or greater than zero.
	 * 
	 * @return the inserted or updated location
	 * @throws EntityValidationException if the flow validations are not respected.
	 */
	Location createUpdateStockRoomLocation(final Location location) throws EntityValidationException;

	/**
	 * Deletes an inventory entry (ItemQuantity) for one status or the whole inventory, if there is only one entry.
	 * 
	 * @category DATA ONBOARDING
	 * 
	 * @param inventory the Oms inventory <dt><b>Preconditions:</b>
	 *           <dd>
	 *           inventory must not be null.
	 *           <dd>
	 *           inventory.skuId must not be empty.
	 *           <dd>
	 *           inventory.locationId must exist in OMS.
	 *           <dd>
	 *           inventory.status must not be empty.
	 *           <dd>
	 *           inventory.status must exist in OMS.
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws EntityNotFoundException if no inventory was found
	 */
	void deleteInventory(OmsInventory inventory) throws EntityValidationException, EntityNotFoundException;

	/**
	 * Find all item statuses.
	 * 
	 * @category OMS-UI
	 * 
	 * @return list of item status
	 */
	List<ItemStatus> findAllItemStatuses();

	/**
	 * Search {@link ItemLocation}s regarding how the query object ({@link ItemLocationsQueryObject}) is populated and
	 * return a
	 * pageable list. See {@link ItemLocationsQueryObject} to find out available features.
	 * 
	 * @category PLATFORM EXTENSION - omsats
	 * @category OMS-UI
	 * 
	 * @param queryObject The {@link com.hybris.oms.domain.inventory.ItemLocationsQueryObject} that determines how
	 *           the search should be performed.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           queryObject.skuId list entries must not be an empty String.
	 *           <dd>
	 *           queryObject.locationId list entries must not be an empty String.
	 *           <dd>
	 *           queryObject.pageNumber must not be null.
	 *           <dd>
	 *           queryObject.pageNumber must not be less than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be null.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than max allowed page size.
	 * @return A pageable list of {@link ItemLocation}
	 * @throws EntityValidationException if preconditions are not met.
	 */
	Pageable<ItemLocation> findItemLocationsByQuery(final ItemLocationsQueryObject queryObject) throws EntityValidationException;

	/**
	 * Find all locations in pages. See LocationQuery to find out available features.
	 * 
	 * @category OMS-UI
	 * 
	 * @param queryObject The {@link com.hybris.oms.domain.QueryObject} that determines how
	 *           the search should be performed.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           queryObject.locationId must not be null
	 *           <dd>
	 *           queryObject.locationName must not be null
	 *           <dd>
	 *           queryObject.priority must not be null
	 *           <dd>
	 *           queryObject.pageNumber must not be null.
	 *           <dd>
	 *           queryObject.pageNumber must not be less than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be null.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than max allowed page size.
	 * @return pageable list of locations
	 * @throws EntityValidationException if preconditions are not met.
	 */
	Pageable<Location> findStockRoomLocationsByQuery(final LocationQueryObject queryObject) throws EntityValidationException;

	/**
	 * Get item status by status code.
	 * 
	 * @category OMS-UI
	 * 
	 * @param statusCode the status code
	 * @return item status
	 * @throws EntityNotFoundException if item status was not found
	 */
	ItemStatus getItemStatusByStatusCode(final String statusCode) throws EntityNotFoundException;

	/**
	 * Get location by location id.
	 * 
	 * @category OMS-UI
	 * 
	 * @param locationId the location id
	 * @return location
	 * @throws EntityNotFoundException if location was not found
	 */
	Location getStockRoomLocationByLocationId(final String locationId) throws EntityNotFoundException;

	/**
	 * Updates an inventory entry. First deliverydate is updated and then the quantity.
	 * The entry is based on sku + location + statusCode (+expectedDate, if it is a future
	 * inventory). If found, updated. If that's not the expected behavior, then use the incrementInventory call.
	 * 
	 * @category OMS-UI
	 * 
	 * @param newExpectedDelivaryDate new date expected for delivery
	 * @param inventory {@link com.hybris.oms.api.inventory.OmsInventory} list of inventory to update <dt>
	 *           <b>Preconditions:</b>
	 *           <dd>
	 *           OmsInventory.skuId is not empty or null
	 *           <dd>
	 *           OmsInventory.itemStatusCode is not empty or null
	 *           <dd>
	 *           OmsInventory.locationId exists
	 * 
	 * @return list of updated inventory
	 * @throws EntityValidationException if no preconditions were satisfied
	 */
	List<OmsInventory> updateFutureInventoryDate(List<OmsInventory> inventory, Date newExpectedDelivaryDate)
			throws EntityValidationException;

	/**
	 * Incrementally updates an inventory entry. The entry is based on sku + location + statusCode (+expectedDate, if it
	 * is a future inventory).
	 * 
	 * @category DATA ONBOARDING
	 * 
	 * @param inventory the inventory to be incremented <dt><b>Preconditions:</b>
	 *           <dd>
	 *           inventory must not be null.
	 *           <dd>
	 *           inventory.skuId must not be empty.
	 *           <dd>
	 *           inventory.locationId must exist in OMS.
	 *           <dd>
	 *           inventory.status must not be empty.
	 *           <dd>
	 *           inventory.status must exist in OMS.
	 * @return the updated inventory
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws EntityNotFoundException if inventory could not be found
	 */
	OmsInventory updateIncrementalInventory(OmsInventory inventory) throws EntityValidationException, EntityNotFoundException;

	/**
	 * Updates (non-incremental) an inventory entry. The entry is based on sku + location + statusCode (+expectedDate, if
	 * it is a future inventory).
	 * 
	 * @category OMS-UI
	 * 
	 * @param inventory the inventory to be updated <dt><b>Preconditions:</b>
	 *           <dd>
	 *           inventory must not be null.
	 *           <dd>
	 *           inventory.skuId must not be empty.
	 *           <dd>
	 *           inventory.locationId must exist in OMS.
	 *           <dd>
	 *           inventory.status must not be empty.
	 *           <dd>
	 *           inventory.status must exist in OMS.
	 * @return the updated inventory
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws EntityNotFoundException if inventory could not be found
	 */
	OmsInventory updateInventory(OmsInventory inventory) throws EntityValidationException, EntityNotFoundException;

	/**
	 * Update a location.
	 * 
	 * @category DATA ONBOARDING
	 * 
	 * @param location the location to update.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           location must not be null.
	 *           <dd>
	 *           location.locationId must not be empty.
	 * @return updated location
	 * @throws EntityNotFoundException if location could not be found.
	 * @throws EntityValidationException if preconditions are not met.
	 */
	Location updateStockRoomLocation(final Location location) throws EntityNotFoundException, EntityValidationException;

	/**
	 * Creates a new bin entry. The entry is based on bin code + location + description + priority.
	 * 
	 * @category DATA ONBOARDING
	 * @category OMS-UI
	 * 
	 * @param bin the bin to be created <dt><b>Preconditions:</b>
	 *           <dd>
	 *           bin must not be null.
	 *           <dd>
	 *           bin.binCode must not be empty.
	 *           <dd>
	 *           bin.locationId must exist in OMS.
	 * @return the created bin
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws DuplicateEntityException if the bin already exists.
	 */
	Bin createBin(Bin bin) throws EntityValidationException, DuplicateEntityException;

	/**
	 * Find all bins in pages. See BinQuery to find out available features.
	 * 
	 * @category OMS-UI
	 * 
	 * @param queryObject The {@link com.hybris.oms.domain.QueryObject} that determines how
	 *           the search should be performed.
	 * 
	 *           <dt><b>Preconditions:</b>
	 *           <dd>
	 *           queryObject.pageNumber must not be null.
	 *           <dd>
	 *           queryObject.pageNumber must not be less than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be null.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than zero.
	 *           <dd>
	 *           queryObject.pageSize must not be greater than max allowed page size.
	 * @return pageable list of Bins
	 * @throws EntityValidationException if preconditions are not met.
	 */
	Pageable<Bin> findBinsByQuery(final BinQueryObject queryObject) throws EntityValidationException;

	/**
	 * Updates an bin entry. The entry is based on binCode + sku + location.
	 * 
	 * @category DATA ONBOARDING
	 * @category OMS-UI
	 * 
	 * @param bin the bin you want to update <dt><b>Preconditions:</b>
	 *           <dd>
	 *           bin must not be null.
	 *           <dd>
	 *           bin.binCode must not be empty.
	 *           <dd>
	 *           bin.locationId must exist in OMS.
	 * @return the updated bin
	 * @throws EntityValidationException if preconditions are not met.
	 * @throws EntityNotFoundException if bin with the provided binCode is not found
	 */
	Bin updateBin(Bin bin) throws EntityValidationException, EntityNotFoundException;

	/**
	 * Deletes a bin entry. The entry is based on binCode + location.
	 * 
	 * @category DATA ONBOARDING
	 * @category OMS-UI
	 * 
	 * @param binCode the code of the bin <dt><b>Preconditions:</b>
	 *           <dd>
	 *           binCode must not be empty.
	 * @param locationId the id of the location <dt><b>Preconditions:</b> <dd> bin.locationId must exist in OMS.
	 * @throws EntityNotFoundException if bin with the provided binCode/locationId is not found
	 */
	void deleteBinByBinCodeLocationId(final String binCode, final String locationId) throws EntityNotFoundException;

	/**
	 * Get a bin entry. The entry is based on binCode + location.
	 * 
	 * @param binCode The code you are looking for <dt><b>Preconditions:</b>
	 *           <dd>
	 *           binCode must not be empty.
	 * @param locationId the location id you are looking for <dt><b>Preconditions:</b> <dd> bin.locationId must exist in
	 *           OMS.
	 * @return the bin that was found
	 * @throws EntityNotFoundException if bin with the provided binCode and locationId is not found
	 */
	Bin getBinByBinCodeLocationId(final String binCode, final String locationId) throws EntityNotFoundException;
}
