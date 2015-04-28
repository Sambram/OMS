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
package com.hybris.oms.service.inventory.impl;

import com.hybris.kernel.api.CriteriaQuery;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.api.exceptions.PrimaryKeyViolationException;
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.exception.DuplicateEntityException;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.BinQuerySupport;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.FutureItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.preference.TenantPreferenceService;
import com.hybris.oms.service.service.AbstractHybrisService;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.ImmutableSet;


public class DefaultInventoryService extends AbstractHybrisService implements InventoryService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultInventoryService.class);

	private static final String EXPECTED_DATE = ", expectedDate=";
	private static final String LOCATION_ID = ", locationId=";
	private static final String STATUS_CODE = ", statusCode=";

	private InventoryQueryFactory inventoryQueries;

	private TenantPreferenceService tenantPreferenceService;

	@Override
	public ItemQuantityData createItemQuantity(final String sku, final String locationId, final String binCode,
			final String statusCode, final String unitCode, final int quantity, final Date expectedDeliveryDate)
	{
		try
		{
			final String validBinCode = StringUtils.isEmpty(binCode) ? InventoryServiceConstants.DEFAULT_BIN : binCode;
			final StockroomLocationData location = getLocationByLocationId(locationId);
			final BinData bin = getBinByBinCodeLocationId(validBinCode, locationId);
			final ItemLocationData itemLocation = getOrCreateItemLocation(sku, location, bin, expectedDeliveryDate);
			return internalCreateItemQuantity(statusCode, unitCode, quantity, expectedDeliveryDate, itemLocation);
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateEntityException(String.format(
					"ItemQuantity already exists (sku=%s, locationId=%s, statusCode=%s, future=%s)", sku, locationId, statusCode,
					expectedDeliveryDate != null), e);
		}
		catch (final ValidationException e)
		{
			throw new EntityValidationException(String.format(
					"ItemQuantity invalid (sku=%s, locationId=%s, statusCode=%s, future=%s) due to %s", sku, locationId, statusCode,
					expectedDeliveryDate != null, e.getMessage()), e);
		}
	}

	@Override
	public ItemQuantityData createUpdateItemQuantity(final String sku, final String locationId, final String binCode,
			final String statusCode, final String unitCode, final int quantity, final Date expectedDeliveryDate)
	{
		ItemQuantityData result = null;
		final String validBinCode = StringUtils.isEmpty(binCode) ? InventoryServiceConstants.DEFAULT_BIN : binCode;
		final StockroomLocationData location = getLocationByLocationId(locationId);
		final BinData bin = getBinByBinCodeLocationId(validBinCode, locationId);
		final ItemLocationData itemLocation = getOrCreateItemLocation(sku, location, bin, expectedDeliveryDate);
		ItemQuantityData iqdToUpdate = findOptionalItemQuantityBySkuLocationBinCodeStatus(sku, locationId, validBinCode,
				statusCode, expectedDeliveryDate);
		if (iqdToUpdate == null)
		{
			try
			{
				result = internalCreateItemQuantity(statusCode, unitCode, quantity, expectedDeliveryDate, itemLocation);
			}
			catch (final PrimaryKeyViolationException e)
			{
				iqdToUpdate = findItemQuantityBySkuLocationBinCodeStatus(sku, locationId, validBinCode, statusCode,
						expectedDeliveryDate);
			}
			catch (final ValidationException e)
			{
				throw new EntityValidationException(String.format(
						"ItemQuantity invalid (sku=%s, locationId=%s, statusCode=%s, future=%s) due to %s", sku, locationId,
						statusCode, expectedDeliveryDate != null, e.getMessage()), e);
			}
		}
		if (iqdToUpdate != null)
		{
			iqdToUpdate.setQuantityValue(quantity + iqdToUpdate.getQuantityValue());
            iqdToUpdate.getOwner().setBanned(false);
			result = iqdToUpdate;
		}
		return result;
	}

	@Override
	public void deleteItemQuantity(final String sku, final String locationId, final String binCode, final String statusCode,
			final Date expectedDeliveryDate)
	{
		try
		{
			final String validBinCode = StringUtils.isEmpty(binCode) ? InventoryServiceConstants.DEFAULT_BIN : binCode;
			final ItemQuantityData iqd = findItemQuantityBySkuLocationBinCodeStatus(sku, locationId, validBinCode, statusCode,
					expectedDeliveryDate);
			getPersistenceManager().remove(iqd);
			getPersistenceManager().flush();
			final ItemLocationData itemLocation = internalFindItemLocation(sku, locationId, validBinCode,
					expectedDeliveryDate != null);
			if (itemLocation.getItemQuantities().isEmpty())
			{
				getPersistenceManager().remove(itemLocation);
				getPersistenceManager().flush();
			}
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(String.format(
					"ItemQuantity not found (sku=%s, locationId=%s, statusCode=%s, future=%s)", sku, locationId, statusCode,
					expectedDeliveryDate != null), e);
		}
	}

	protected ItemQuantityData internalCreateItemQuantity(final String statusCode, final String unitCode, final int quantity,
			final Date expectedDeliveryDate, final ItemLocationData itemLocation)
	{
		final ItemQuantityData iqd = this.getPersistenceManager().create(
				expectedDeliveryDate == null ? CurrentItemQuantityData.class : FutureItemQuantityData.class);
		iqd.setQuantityUnitCode(unitCode);
		iqd.setQuantityValue(quantity);
		iqd.setStatusCode(statusCode);
		iqd.setOwner(itemLocation);
		if (expectedDeliveryDate != null)
		{
			iqd.setExpectedDeliveryDate(DateUtils.truncate(expectedDeliveryDate, Calendar.DATE));
		}
		flush();
		return iqd;
	}

	protected ItemLocationData getOrCreateItemLocation(final String sku, final StockroomLocationData location, final BinData bin,
			final Date expectedDeliveryDate)
	{
		ItemLocationData itemLocation = internalFindOptionalItemLocation(sku, location.getLocationId(), bin.getBinCode(),
				expectedDeliveryDate != null);
		if (itemLocation == null)
		{
			try
			{
				itemLocation = getPersistenceManager().create(ItemLocationData.class);
				itemLocation.setItemId(sku);
				itemLocation.setStockroomLocation(location);
				itemLocation.setBin(bin);
				itemLocation.setFuture(expectedDeliveryDate != null);
                itemLocation.setBanned(false);
				flush();
			}
			catch (final PrimaryKeyViolationException ignore)
			{
				LOG.info("Concurrent insert of ItemLocation sku={}, locationId={}", sku, location.getLocationId());
				itemLocation = internalFindItemLocation(sku, location.getLocationId(), bin.getBinCode(), expectedDeliveryDate != null);
			}
		}
		return itemLocation;
	}

	@Override
	public ItemStatusData createItemStatus(final ItemStatusData itemStatus)
	{
		try
		{
			this.flush();
			return itemStatus;
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateEntityException("Item status " + itemStatus.getStatusCode() + " already exists.", e);
		}
	}

	@Override
	public StockroomLocationData createLocation(final StockroomLocationData location) throws EntityValidationException,
			EntityValidationException
	{
		try
		{
			if (location != null && (location.getLocationRoles() == null || location.getLocationRoles().isEmpty()))
			{
				location.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
			}
			final BinData bin = this.getPersistenceManager().create(BinData.class);
			bin.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
			bin.setStockroomLocation(location);
			this.flush();
		}
		catch (final ValidationException e)
		{
			throw new EntityValidationException("Validation error for location with id '" + location.getLocationId() + "': "
					+ e.getMessage(), e);
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateEntityException("Stockroom location " + location.getLocationId() + " already exists.", e);
		}
		return location;
	}

	/**
	 * Get all future item quantities where the expected delivery date is before today's date.
	 *
	 * @return list of future item quantities
	 */
	public List<FutureItemQuantityData> findAllExpectedDeliveryDate()
	{
		return this.findAll(this.inventoryQueries.getAllExpectedDeliveryDatesQuery());
	}

	/**
	 * @UsedBy("InventoryRolloverJobTest")
	 */
	public List<ItemLocationData> findAllFutureItemLocationsByDate(final Date date)
	{
		return this.findAll(this.inventoryQueries.getAllItemLocationFuturesByDateQuery(date));
	}

	/**
	 * @UsedBy("InventoryRolloverJobTest")
	 */
	public List<ItemLocationData> findAllItemLocations()
	{
		return this.findAll(this.inventoryQueries.getAllItemLocationsQuery());
	}

	@Override
	public List<ItemStatusData> findAllItemStatuses()
	{
		return this.findAll(this.inventoryQueries.getAllItemStatusesQuery());
	}

	@Override
	public List<StockroomLocationData> findLocationsByLocationIds(final Set<String> locationIds)
	{
		return inventoryQueries.findLocationsByLocationIds(locationIds).resultList();
	}

	@Override
	public CurrentItemQuantityData findCurrentItemQuantityBySkuLocationBinCodeStatus(final String skuId, final String locationId,
			final String binCode, final String statusCode) throws EntityNotFoundException
	{
		try
		{
			return this.inventoryQueries.getCurrentItemQuantityBySkuLocationBinCodeStatusQuery(skuId, locationId, binCode,
					statusCode).uniqueResult();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(String.format("Item Quantity not found for: skuId=%s,locationId=%s,statusCode=%s",
					skuId, locationId, statusCode), e);
		}
	}

	@Override
	public FutureItemQuantityData findFutureItemQuantityBySkuLocationBinCodeStatusDate(final String skuId,
			final String locationId, final String binCode, final String statusCode, final Date expectedDate)
			throws EntityNotFoundException
	{
		try
		{
			return this.inventoryQueries.getFutureItemQuantityBySkuLocationBinCodeStatusDateQuery(skuId, locationId, binCode,
					statusCode, expectedDate).uniqueResult();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Item Quantity not found for: skuId=" + skuId + LOCATION_ID + locationId + STATUS_CODE
					+ statusCode + EXPECTED_DATE + expectedDate, e);
		}
	}

	@Override
	public ItemLocationData findItemLocationCurrentBySkuIdAndLocationId(final String skuId, final String locationId)
			throws EntityNotFoundException
	{
		try
		{
			return this.findOneSingle(this.inventoryQueries.getAllItemLocationBySkuIdAndLocationIdQuery(skuId, locationId, false));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Item location current not found. Sku id: " + skuId + ", Location Id: " + locationId,
					e);
		}
	}

	@Override
	public ItemLocationData findItemLocationFutureBySkuLocation(final String skuId, final String locationId)
			throws EntityNotFoundException
	{
		try
		{
			return this.findOneSingle(this.inventoryQueries.getAllItemLocationBySkuIdAndLocationIdQuery(skuId, locationId, true));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException(String.format("Item location future not found: %s;%s", skuId, locationId), e);
		}
	}

	/**
	 * Get an optional unique item location current by sku id and location id.
	 *
	 * @param skuId
	 *           the sku id
	 * @param locationId
	 *           the location id
	 * @return current item location or null
	 *
	 */
	public ItemLocationData findOptionalItemLocationCurrentBySkuAndLocationIdAndBinCode(final String skuId,
			final String locationId, final String binCode)
	{
		return internalFindOptionalItemLocation(skuId, locationId, binCode, false);
	}

	@Override
	public Page<ItemLocationData> findPagedItemLocationsByQuery(final ItemLocationsQueryObject queryObject)
	{
		final int[] pageNumberAndSize = this.getPageNumberAndSize(queryObject, 0, this.getQueryPageSizeDefault());

		return this.findPaged(this.inventoryQueries.findItemLocationByQuery(queryObject), pageNumberAndSize[0],
				pageNumberAndSize[1]);
	}

	@Override
	public List<BinData> findBinsBySkuAndLocationId(final String skuId, final String locationId)
	{
		final SortDirection direction = SortDirection.valueOf(retrieveTenantPreferenceValueWithDefault(
				TenantPreferenceConstants.PREF_KEY_BIN_DIRECTION, SortDirection.ASC.toString()));

		final BinQuerySupport orderBy = BinQuerySupport.valueOf(retrieveTenantPreferenceValueWithDefault(
				TenantPreferenceConstants.PREF_KEY_BIN_SEQUENCING, BinQuerySupport.BIN_PRIORITY.toString()));

		final String number = retrieveTenantPreferenceValueWithDefault(TenantPreferenceConstants.PREF_KEY_BIN_NUMBER,
				String.valueOf(this.getQueryPageSizeDefault()));

		final Page<BinData> binPage = this.findPaged(
				inventoryQueries.getAllBinsBySkuIdAndLocationIdQuery(skuId, locationId, false, orderBy, direction), 0,
				Integer.valueOf(number));

		return binPage.getContent();
	}

	protected String retrieveTenantPreferenceValueWithDefault(final String key, final String defaultValue)
	{
		String result = defaultValue;
		final TenantPreferenceData tenantPreferenceData = tenantPreferenceService.getOptionalTenantPreferenceByKey(key);
		if (tenantPreferenceData != null && !StringUtils.isEmpty(tenantPreferenceData.getValue()))
		{
			result = tenantPreferenceData.getValue();
		}
		return result;
	}

	// TODO: Why do we have two methods that search by LocationQueryObject?
	@Override
	public Page<StockroomLocationData> findPagedLocations(final LocationQueryObject queryObject)
	{
		final CriteriaQuery<StockroomLocationData> criteriaQuery = this.inventoryQueries.findLocationsByQueryObject(queryObject,
				true);

		if (criteriaQuery == null)
		{
			// Return "empty result" page...
			return new Page<StockroomLocationData>()
			{
				@Override
				public List<StockroomLocationData> getContent()
				{
					return Collections.emptyList();
				}

				@Override
				public int getNumber()
				{
					return 0;
				}

				@Override
				public int getNumberOfElements()
				{
					return 0;
				}

				@Override
				public int getSize()
				{
					return 0;
				}

				@Override
				public long getTotalElements()
				{
					return 0;
				}

				@Override
				public int getTotalPages()
				{
					return 0;
				}
			};
		}
		else
		{
			final int[] pageNumberAndSize = this.getPageNumberAndSize(queryObject, 0, this.getQueryPageSizeDefault());

			return this.findPaged(this.inventoryQueries.findLocationsByQueryObject(queryObject, true), pageNumberAndSize[0],
					pageNumberAndSize[1]);
		}
	}

	@Override
	public List<StockroomLocationData> findAllLocationsByQueryObject(final LocationQueryObject queryObject,
			final boolean searchByWildcardAllowed)
	{
		List<StockroomLocationData> result = Collections.emptyList();
		final CriteriaQuery<StockroomLocationData> query = this.inventoryQueries.findLocationsByQueryObject(queryObject,
				searchByWildcardAllowed);
		if (query != null)
		{
			result = query.resultList();
		}
		return result;
	}

	/**
	 * @UsedBy("OrderTriggersTest")
	 */
	public CurrentItemQuantityData getItemQuantityBySkuAndStatusCode(final String sku, final String statusCode)
	{
		try
		{
			return this.findOneSingle(this.inventoryQueries.getItemQuantityByStatusCode(sku, statusCode));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("CurrentItemQuantityData not found. (sku=" + sku + STATUS_CODE + statusCode + ')', e);
		}
	}

	@Override
	public ItemStatusData getItemStatusByStatusCode(final String statusCode)
	{
		try
		{
			return this.findOneSingle(this.inventoryQueries.getInventoryStatusByStatusCodeQuery(statusCode));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Item status not found. Status Code: " + statusCode, e);
		}
	}

	@Override
	public StockroomLocationData getLocationByLocationId(final String locationId) throws EntityNotFoundException
	{
		try
		{
			return this.getPersistenceManager().getByIndex(StockroomLocationData.UX_STOCKROOMLOCATIONS_LOCATIONID, locationId);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("Location not found. Location Id: " + locationId, e);
		}
	}

	@Override
	public CurrentItemQuantityData updateCurrentItemQuantity(final String locationId, final String binCode, final String skuId,
			final String statusCode, final int quantity) throws EntityNotFoundException, EntityValidationException
	{
		final String validBinCode = StringUtils.isEmpty(binCode) ? InventoryServiceConstants.DEFAULT_BIN : binCode;
		final CurrentItemQuantityData iqData = this.findCurrentItemQuantityBySkuLocationBinCodeStatus(skuId, locationId,
				validBinCode, statusCode);
		iqData.setQuantityValue(quantity);
		return iqData;
	}

    @Override
    public List<ItemLocationData> banItemLocations(final String skuId, final String locationId) throws EntityNotFoundException
    {
        final List<ItemLocationData> itemLocationDataList = findAllItemLocationsBySkuAndLocation(skuId, locationId);
        for (ItemLocationData itemLocationData : itemLocationDataList)
        {
            itemLocationData.setBanned(true);
        }
        return itemLocationDataList;
    }

	@Override
	public CurrentItemQuantityData updateCurrentItemQuantityIncremental(final String locationId, final String binCode,
			final String skuId, final String statusCode, final int quantity) throws EntityNotFoundException,
			EntityValidationException
	{
		final String validBinCode = StringUtils.isEmpty(binCode) ? InventoryServiceConstants.DEFAULT_BIN : binCode;
		final CurrentItemQuantityData iqData = this.findCurrentItemQuantityBySkuLocationBinCodeStatus(skuId, locationId,
				validBinCode, statusCode);
		iqData.setQuantityValue(quantity + iqData.getQuantityValue());
		return iqData;
	}

	@Override
	public FutureItemQuantityData updateFutureItemQuantity(final String locationId, final String binCode, final String skuId,
			final String statusCode, final int quantity, final Date expectedDate) throws EntityNotFoundException,
			EntityValidationException
	{
		final String validBinCode = StringUtils.isEmpty(binCode) ? InventoryServiceConstants.DEFAULT_BIN : binCode;
		final FutureItemQuantityData iqData = this.findFutureItemQuantityBySkuLocationBinCodeStatusDate(skuId, locationId,
				validBinCode, statusCode, expectedDate);
		iqData.setQuantityValue(quantity);
		return iqData;
	}

	@Override
	public FutureItemQuantityData updateFutureItemQuantityIncremental(final String locationId, final String binCode,
			final String skuId, final String statusCode, final int quantity, final Date expectedDate)
			throws EntityNotFoundException, EntityValidationException
	{
		final String validBinCode = StringUtils.isEmpty(binCode) ? InventoryServiceConstants.DEFAULT_BIN : binCode;
		final FutureItemQuantityData iqData = this.findFutureItemQuantityBySkuLocationBinCodeStatusDate(skuId, locationId,
				validBinCode, statusCode, expectedDate);
		iqData.setQuantityValue(quantity + iqData.getQuantityValue());
		return iqData;
	}

	@Override
	public StockroomLocationData updateLocation(final StockroomLocationData location) throws EntityValidationException
	{
		try
		{
			this.flush();
		}
		catch (final ValidationException e)
		{
			throw new EntityValidationException("Validation error for location with id '" + location.getLocationId() + "': "
					+ e.getMessage(), e);
		}
		return location;
	}

	@Override
	public BinData createBin(final BinData bin) throws EntityValidationException
	{
		try
		{
			this.flush();
		}
		catch (final ValidationException e)
		{
			throw new EntityValidationException("Validation error for bin with bin code '" + bin.getBinCode() + "': "
					+ e.getMessage(), e);
		}
		catch (final PrimaryKeyViolationException e)
		{
			throw new DuplicateEntityException("Bin with bin code " + bin.getBinCode() + " already exists.", e);
		}
		return bin;
	}

	@Override
	public BinData updateBin(final BinData bin) throws EntityValidationException
	{
		try
		{
			this.flush();
		}
		catch (final ValidationException e)
		{
			throw new EntityValidationException("Validation error for bin with bin code '" + bin.getBinCode() + "': "
					+ e.getMessage(), e);
		}
		return bin;
	}

	@Override
	public void deleteBin(final BinData bin) throws EntityValidationException
	{
		try
		{
			this.getPersistenceManager().remove(bin);
			this.deleteInventoryForLocationIdBinCode(bin.getStockroomLocation().getLocationId(), bin.getBinCode());
			this.flush();
		}
		catch (final ValidationException e)
		{
			throw new EntityValidationException("Validation error for bin with bin code '" + bin.getBinCode() + "': "
					+ e.getMessage(), e);
		}
		catch (final ManagedObjectNotFoundException m)
		{
			throw new EntityNotFoundException("Cannot delete. Bin not found for binCode: " + bin.getBinCode() + ", locationId: "
					+ bin.getStockroomLocation().getLocationId(), m);
		}
	}

	@Override
	public Page<BinData> findPagedBinsByQuery(final BinQueryObject queryObject)
	{
		final int[] pageNumberAndSize = this.getPageNumberAndSize(queryObject, 0, this.getQueryPageSizeDefault());

		return this.findPaged(this.inventoryQueries.findBinsByQuery(queryObject), pageNumberAndSize[0], pageNumberAndSize[1]);
	}

	@Override
	public BinData getBinByBinCodeLocationId(final String binCode, final String locationId)
	{
		try
		{
			final StockroomLocationData location = this.getPersistenceManager().getByIndex(
					StockroomLocationData.UX_STOCKROOMLOCATIONS_LOCATIONID, locationId);
			return this.getPersistenceManager().getByIndex(BinData.UQ_BIN_BINCODESKULOC, binCode.toLowerCase(Locale.US), location);
		}
		catch (final ManagedObjectNotFoundException e)
		{
			throw new EntityNotFoundException("No bin found for bin code: " + binCode.toLowerCase(Locale.US) + ", location id: "
					+ locationId, e);
		}
	}

	@Override
	public List<ItemLocationData> findAllItemLocationsBySkuAndLocation(String skuId,
			String locationId) throws EntityNotFoundException

	{
		try
		{
			return this.findAll(
					this.inventoryQueries.getAllItemLocationsBySkuAndLocationQuery(skuId, locationId));
		}
		catch (final ManagedObjectNotFoundException e)
		{
			return new ArrayList<>();
		}
	}


	/**
	 * Delete all {@ItemLocationData} associated with the given location id + bin code. This should
	 * also delete all {@link CurrentItemQuantityData} and all {@link FutureItemQuantityData} associated with the item
	 * locations to be removed.
	 *
	 * @param locationId
	 * @param binCode
	 */
	protected void deleteInventoryForLocationIdBinCode(final String locationId, final String binCode)
	{
		final List<ItemLocationData> itemLocations = this.findItemlocationsByLocationIdBinCode(locationId, binCode);

		for (final ItemLocationData itemLocation : itemLocations)
		{
			this.getPersistenceManager().remove(itemLocation);
		}
	}

	/**
	 * Find all {@link ItemLocationData} by bin code. Includes current and future inventory.
	 *
	 * @param locationId
	 * @param binCode
	 * @return list of item locations
	 */
	protected List<ItemLocationData> findItemlocationsByLocationIdBinCode(final String locationId, final String binCode)
	{
		return this.inventoryQueries.findItemLocationByLocationIdBinCode(locationId, binCode).resultList();
	}

	/**
	 * Checks if there is already an ItemStatus with the given status.
	 *
	 * @param newItemStatus
	 * @return true if ItemStatus already exist and false if it doesn't
	 */
	protected boolean isItemStatusAlreadyExist(final ItemStatusData newItemStatus)
	{
		final ItemStatusData itemStatusData = this.findOneOptional(this.inventoryQueries
				.getInventoryStatusByStatusCodeQuery(newItemStatus.getStatusCode()));

		return itemStatusData != null;
	}

	protected ItemQuantityData findItemQuantityBySkuLocationBinCodeStatus(final String skuId, final String locationId,
			final String binCode, final String statusCode, final Date expectedDeliveryDate)
	{
		return this.inventoryQueries.getItemQuantityBySkuLocationBinCodeStatusQuery(skuId, locationId, binCode, statusCode,
				expectedDeliveryDate).uniqueResult();
	}

	protected ItemQuantityData findOptionalItemQuantityBySkuLocationBinCodeStatus(final String skuId, final String locationId,
			final String binCode, final String statusCode, final Date expectedDeliveryDate)
	{
		return this.inventoryQueries.getItemQuantityBySkuLocationBinCodeStatusQuery(skuId, locationId, binCode, statusCode,
				expectedDeliveryDate).optionalUniqueResult();
	}

	protected ItemLocationData internalFindOptionalItemLocation(final String skuId, final String locationId, final String binCode,
			final boolean future)
	{
		return this.inventoryQueries.getAllItemLocationBySkuIdAndLocationIdAndBinCodeQuery(skuId, locationId, binCode, future)
				.optionalUniqueResult();
	}

	protected ItemLocationData internalFindItemLocation(final String skuId, final String locationId, final String binCode,
			final boolean future)
	{
		return this.inventoryQueries.getAllItemLocationBySkuIdAndLocationIdAndBinCodeQuery(skuId, locationId, binCode, future)
				.uniqueResult();
	}

	protected InventoryQueryFactory getInventoryQueries()
	{
		return inventoryQueries;
	}

	@Required
	public void setInventoryQueries(final InventoryQueryFactory inventoryQueries)
	{
		this.inventoryQueries = inventoryQueries;
	}

	protected TenantPreferenceService getTenantPreferenceService()
	{
		return tenantPreferenceService;
	}

	@Required
	public void setTenantPreferenceService(final TenantPreferenceService tenantPreferenceService)
	{
		this.tenantPreferenceService = tenantPreferenceService;
	}

}
