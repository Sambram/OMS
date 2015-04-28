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
package com.hybris.oms.facade.inventory;

import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.JobSchedulerService;
import com.hybris.kernel.api.JobWorkerBean;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.exceptions.ValidationException;
import com.hybris.oms.api.PageInfo;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.PagedResults;
import com.hybris.oms.api.inventory.InventoryFacade;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.QueryObject;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.exception.RemoteRequestException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.ItemLocation;
import com.hybris.oms.domain.inventory.ItemLocationCurrent;
import com.hybris.oms.domain.inventory.ItemLocationFuture;
import com.hybris.oms.domain.inventory.ItemLocationsQueryObject;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.LocationQueryObject;
import com.hybris.oms.domain.remote.exception.InvalidGeolocationResponseException;
import com.hybris.oms.facade.validation.FailureHandler;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.inventory.impl.LocationDataStaticUtils;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.util.ValidationUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;


/**
 * Default implementation of {@link InventoryFacade}.
 */

@SuppressWarnings({"PMD.TooManyPublicMethods", "PMD.ExcessiveImports"})
public class DefaultInventoryFacade implements InventoryFacade
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInventoryFacade.class);

	private static final int HOURS_OF_DAY = 23;
	private static final int MINUTES = 59;
	private static final int SECONDS = 59;
	private static final int MILLISECOND = 999;


	private CisService cisService;

	private JobWorkerBean inventoryRolloverWorker;

	private InventoryService inventoryService;

	private JobSchedulerService jobSchedulerService;

	private Converter<ItemStatusData, ItemStatus> itemStatusConverter;

	private Converter<ItemStatus, ItemStatusData> itemStatusReverseConverter;

	private Converter<StockroomLocationData, Location> locationConverter;

	private Converter<Location, StockroomLocationData> locationReverseConverter;

	private Populator<Location, StockroomLocationData> locationReversePopulator;

	private Converter<ItemLocationData, ItemLocationCurrent> itemLocationCurrentConverter;

	private Converter<ItemLocationData, ItemLocationFuture> itemLocationFutureConverter;

	private Converter<BinData, Bin> binConverter;

	private Converter<ItemQuantityData, OmsInventory> omsInventoryConverter;

	private Converter<Bin, BinData> binReverseConverter;

	private Populator<Bin, BinData> binReversePopulator;

	private Converters converters;

	private Validator<QueryObject<?>> queryObjectValidator;

	private Validator<ItemStatus> itemStatusValidator;

	private Validator<OmsInventory> omsInventoryValidator;

	private Validator<Location> locationValidator;

	private Validator<Bin> binValidator;

	private FailureHandler entityValidationHandler;

	@Override
	@Transactional
	public OmsInventory createInventory(final OmsInventory inventory)
	{
		LOGGER.trace("createInventory");
		this.omsInventoryValidator.validate("OmsInventory", inventory, this.entityValidationHandler);

		final ItemQuantityData itemQuantityData = inventoryService.createItemQuantity(inventory.getSkuId(),
				inventory.getLocationId(), inventory.getBinCode(), inventory.getStatus(), inventory.getUnitCode(),
				inventory.getQuantity(), inventory.getDeliveryDate());
		return omsInventoryConverter.convert(itemQuantityData);
	}

	@Override
	@Transactional
	public OmsInventory createUpdateInventory(final OmsInventory inventory)
	{
		LOGGER.trace("createUpdateInventory");
		this.omsInventoryValidator.validate("OmsInventory", inventory, entityValidationHandler);
		final ItemQuantityData itemQuantityData = inventoryService.createUpdateItemQuantity(inventory.getSkuId(),
				inventory.getLocationId(), inventory.getBinCode(), inventory.getStatus(), inventory.getUnitCode(),
				inventory.getQuantity(), inventory.getDeliveryDate());
		return omsInventoryConverter.convert(itemQuantityData);
	}

	@Transactional
	@Override
	public void deleteInventory(final OmsInventory inventory)
	{
		LOGGER.debug("deleting inventory: " + inventory);
		this.omsInventoryValidator.validate("OmsInventory", inventory, entityValidationHandler);
		this.inventoryService.deleteItemQuantity(inventory.getSkuId(), inventory.getLocationId(), inventory.getBinCode(),
				inventory.getStatus(), inventory.getDeliveryDate());
	}

	@Transactional
	@Override
	public ItemStatus createItemStatus(final ItemStatus itemStatus)
	{
		LOGGER.trace("createItemStatus");
		this.itemStatusValidator.validate("ItemStatus", itemStatus, entityValidationHandler);
		final ItemStatusData itemStatusData = this.itemStatusReverseConverter.convert(itemStatus);
		this.inventoryService.createItemStatus(itemStatusData);
		return itemStatus;
	}

	@Transactional
	@Override
	public Location createStockRoomLocation(final Location location)
	{
		LOGGER.trace("createStockRoomLocation");
		this.locationValidator.validate("OmsInventory", location, this.entityValidationHandler);

		// Convert to managed object
		final StockroomLocationData locationData = this.locationReverseConverter.convert(location);

		// TODO: This business logic should be the responsibility of Service Layer.
		// If the geocodes are invalid in the location address, then get them
		if (!LocationDataStaticUtils.hasValidAddressGeocodes(locationData))
		{
			try
			{
				locationData.setAddress(this.cisService.geocodeAddress(locationData.getAddress()));
			}
			catch (final InvalidGeolocationResponseException e)
			{
				LOGGER.warn("Invalid CIS geolocation response: " + e.getMessage());
			}
			catch (final RemoteRequestException e)
			{
				LOGGER.warn("Unable to obtain geo codes from CIS: " + e.getMessage());
			}
		}
		// Save the new location
		final StockroomLocationData fromDB = this.inventoryService.createLocation(locationData);
		return this.locationConverter.convert(fromDB);
	}

	@Transactional
	@Override
	public Location createUpdateStockRoomLocation(final Location location)
	{
		LOGGER.trace("createUpdateStockRoomLocation");
		this.locationValidator.validate("Location", location, this.entityValidationHandler);
		StockroomLocationData fromDB = null;
		try
		{
			final StockroomLocationData temporaryLocationData = this.inventoryService.getLocationByLocationId(location
					.getLocationId());
			// update
			this.locationReversePopulator.populate(location, temporaryLocationData);
			fromDB = this.inventoryService.updateLocation(temporaryLocationData);
		}
		catch (final EntityNotFoundException e)
		{
			fromDB = this.inventoryService.createLocation(this.locationReverseConverter.convert(location));
		}
		return this.locationConverter.convert(fromDB);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemStatus> findAllItemStatuses()
	{
		LOGGER.trace("findAllItemStatuses");
		final List<ItemStatusData> itemStatusesData = this.inventoryService.findAllItemStatuses();
		return this.converters.convertAll(itemStatusesData, this.itemStatusConverter);
	}

	@Override
	@Transactional(readOnly = true)
	public Pageable<ItemLocation> findItemLocationsByQuery(final ItemLocationsQueryObject queryObject)
	{
		this.queryObjectValidator.validate("ItemLocationsQueryObject", queryObject, this.entityValidationHandler);

		final Page<ItemLocationData> pagedItemLocationDatas = this.inventoryService.findPagedItemLocationsByQuery(queryObject);

		final List<ItemLocation> itemLocations = new ArrayList<>();
		for (final ItemLocationData itemLocationData : pagedItemLocationDatas.getContent())
		{
			final ItemLocation itemLocation = itemLocationData.isFuture() ? this.itemLocationFutureConverter
					.convert(itemLocationData) : this.itemLocationCurrentConverter.convert(itemLocationData);

			itemLocations.add(itemLocation);
		}

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedItemLocationDatas.getTotalPages());
		pageInfo.setTotalResults(pagedItemLocationDatas.getTotalElements());
		pageInfo.setPageNumber(pagedItemLocationDatas.getNumber());

		return new PagedResults<ItemLocation>(itemLocations, pageInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public Pageable<Location> findStockRoomLocationsByQuery(final LocationQueryObject queryObject)
	{
		this.queryObjectValidator.validate("LocationQueryObject", queryObject, this.entityValidationHandler);

		final Page<StockroomLocationData> pagedLocationDatas = this.inventoryService.findPagedLocations(queryObject);
		final List<Location> stockroomLocations = this.converters.convertAll(pagedLocationDatas.getContent(),
				this.locationConverter);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedLocationDatas.getTotalPages());
		pageInfo.setTotalResults(pagedLocationDatas.getTotalElements());
		pageInfo.setPageNumber(pagedLocationDatas.getNumber());

		return new PagedResults<Location>(stockroomLocations, pageInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public ItemStatus getItemStatusByStatusCode(final String statusCode)
	{
		LOGGER.trace("getItemStatusByStatusCode");
		final ItemStatusData itemStatusData = this.inventoryService.getItemStatusByStatusCode(statusCode);
		return this.itemStatusConverter.convert(itemStatusData);
	}

	@Override
	@Transactional(readOnly = true)
	public Location getStockRoomLocationByLocationId(final String locationId)
	{
		LOGGER.trace("getLocationByLocationId");
		final StockroomLocationData locationData = this.inventoryService.getLocationByLocationId(locationId);
		return this.locationConverter.convert(locationData);
	}

	@Transactional
	@Override
	public List<OmsInventory> updateFutureInventoryDate(final List<OmsInventory> omsInventories, final Date newExpectedDeliveryDate)
	{
		LOGGER.trace("updateFutureInventory");

		final Calendar today = GregorianCalendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, HOURS_OF_DAY);
		today.set(Calendar.MINUTE, MINUTES);
		today.set(Calendar.SECOND, SECONDS);
		today.set(Calendar.MILLISECOND, MILLISECOND);

		final List<OmsInventory> updatedOmsInventories = new ArrayList<>();

		for (final OmsInventory omsInventory : omsInventories)
		{
			if (omsInventory.getDeliveryDate() != null)
			{


				final OmsInventory newOmsInventory = OmsInventory.builder().setDeliveryDate(newExpectedDeliveryDate)
						.setLocationId(omsInventory.getLocationId()).setSkuId(omsInventory.getSkuId())
						.setStatus(omsInventory.getStatus()).build();

				newOmsInventory.setQuantity(omsInventory.getQuantity());

				final OmsInventory updatedOmsInventory = this.createUpdateInventory(newOmsInventory);
				updatedOmsInventories.add(updatedOmsInventory);

				this.inventoryService.deleteItemQuantity(omsInventory.getSkuId(), omsInventory.getLocationId(),
						omsInventory.getBinCode(), omsInventory.getStatus(), omsInventory.getDeliveryDate());
			}
		}
		if (newExpectedDeliveryDate.before(today.getTime()) && !updatedOmsInventories.isEmpty())
		{
			// This scenario changes from FutureInventory to CurrentInventory since the new expected deliveryDate is
			// currentDate
			this.jobSchedulerService.runOnce(this.inventoryRolloverWorker);
		}
		return updatedOmsInventories;
	}

	@Override
	@Transactional
	public OmsInventory updateIncrementalInventory(final OmsInventory inventory)
	{
		LOGGER.trace("updateIncrementalInventory");
		this.omsInventoryValidator.validate("OmsInventory", inventory, entityValidationHandler);

		ItemQuantityData result = null;

		try
		{
			// if it is future
			if (inventory.getDeliveryDate() != null)
			{
				result = this.inventoryService.updateFutureItemQuantityIncremental(inventory.getLocationId(), inventory.getBinCode(),
						inventory.getSkuId(), inventory.getStatus(), inventory.getQuantity(), inventory.getDeliveryDate());
			} // else it is current
			else
			{
				result = this.inventoryService.updateCurrentItemQuantityIncremental(inventory.getLocationId(),
						inventory.getBinCode(), inventory.getSkuId(), inventory.getStatus(), inventory.getQuantity());
			}
		}
		catch (final EntityNotFoundException e)
		{
			throw new EntityNotFoundException("Cannot update. Inventory not found for: " + inventory, e);
		}

		return omsInventoryConverter.convert(result);
	}

	@Override
	@Transactional
	public OmsInventory updateInventory(final OmsInventory inventory)
	{
		LOGGER.trace("updateInventory");
		this.omsInventoryValidator.validate("OmsInventory", inventory, entityValidationHandler);

		ItemQuantityData result = null;

		try
		{
			// if it is future
			if (inventory.getDeliveryDate() != null)
			{
				result = this.inventoryService.updateFutureItemQuantity(inventory.getLocationId(), inventory.getBinCode(),
						inventory.getSkuId(), inventory.getStatus(), inventory.getQuantity(), inventory.getDeliveryDate());
			} // else it is current
			else
			{
				result = this.inventoryService.updateCurrentItemQuantity(inventory.getLocationId(), inventory.getBinCode(),
						inventory.getSkuId(), inventory.getStatus(), inventory.getQuantity());
			}
		}
		catch (final EntityNotFoundException e)
		{
			throw new EntityNotFoundException("Cannot update. Inventory not found for: " + inventory, e);
		}

		return omsInventoryConverter.convert(result);
	}

	@Transactional
	@Override
	public Location updateStockRoomLocation(final Location location)
	{
		LOGGER.trace("updateStockRoomLocation");
		this.locationValidator.validate("Location", location, this.entityValidationHandler);

		final StockroomLocationData locationData = this.inventoryService.getLocationByLocationId(location.getLocationId());
		// update fields
		this.locationReversePopulator.populate(location, locationData);

		// Validate that the location passes all JSR-303 constraints
		try
		{
			inventoryService.flush();
		}
		catch (final ValidationException e)
		{
			throw new EntityValidationException(ValidationUtils.getValidationMessage(e.getConstraintViolations()), e);
		}
		this.inventoryService.updateLocation(locationData);

		return this.locationConverter.convert(locationData);
	}

	@Transactional
	@Override
	public Bin createBin(final Bin bin)
	{
		LOGGER.trace("createBin");
		this.binValidator.validate("Bin", bin, this.entityValidationHandler);
		final BinData binData = this.binReverseConverter.convert(bin);
		return this.binConverter.convert(inventoryService.createBin(binData));
	}

	@Transactional
	@Override
	public Bin updateBin(final Bin bin)
	{
		LOGGER.trace("updateBin");
		this.binValidator.validate("Bin", bin, this.entityValidationHandler);

		final BinQueryObject binQueryObject = new BinQueryObject();
		binQueryObject.setBinCode(bin.getBinCode());
		binQueryObject.setLocationId(bin.getLocationId());

		final BinData binData = this.inventoryService.getBinByBinCodeLocationId(bin.getBinCode(), bin.getLocationId());
		this.binReversePopulator.populate(bin, binData);
		return this.binConverter.convert(inventoryService.updateBin(binData));
	}

	@Transactional
	@Override
	public void deleteBinByBinCodeLocationId(final String binCode, final String locationId)
	{
		LOGGER.trace("deleteBin");
		final BinData binData = this.inventoryService.getBinByBinCodeLocationId(binCode, locationId);
		this.inventoryService.deleteBin(binData);
	}

	@Transactional(readOnly = true)
	@Override
	public Bin getBinByBinCodeLocationId(final String binCode, final String locationId)
	{
		LOGGER.trace("getBin");
		return this.binConverter.convert(this.inventoryService.getBinByBinCodeLocationId(binCode, locationId));
	}

	@Override
	@Transactional(readOnly = true)
	public Pageable<Bin> findBinsByQuery(final BinQueryObject queryObject)
	{
		this.queryObjectValidator.validate("BinQueryObject", queryObject, this.entityValidationHandler);

		final Page<BinData> pagedBinsDatas = this.inventoryService.findPagedBinsByQuery(queryObject);
		final List<BinData> binsData = pagedBinsDatas.getContent();
		final List<Bin> bins = this.converters.convertAll(binsData, this.binConverter);

		final PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalPages(pagedBinsDatas.getTotalPages());
		pageInfo.setTotalResults(pagedBinsDatas.getTotalElements());
		pageInfo.setPageNumber(pagedBinsDatas.getNumber());

		return new PagedResults<Bin>(bins, pageInfo);
	}

	@Required
	public void setCisService(final CisService cisService)
	{
		this.cisService = cisService;
	}

	@Required
	public void setInventoryRolloverWorker(final JobWorkerBean inventoryRolloverWorker)
	{
		this.inventoryRolloverWorker = inventoryRolloverWorker;
	}

	@Required
	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	@Required
	public void setJobSchedulerService(final JobSchedulerService jobSchedulerService)
	{
		this.jobSchedulerService = jobSchedulerService;
	}

	@Required
	public void setItemStatusConverter(final Converter<ItemStatusData, ItemStatus> itemStatusConverter)
	{
		this.itemStatusConverter = itemStatusConverter;
	}

	@Required
	public void setItemStatusReverseConverter(final Converter<ItemStatus, ItemStatusData> itemStatusReverseConverter)
	{
		this.itemStatusReverseConverter = itemStatusReverseConverter;
	}

	@Required
	public void setLocationConverter(final Converter<StockroomLocationData, Location> locationConverter)
	{
		this.locationConverter = locationConverter;
	}

	@Required
	public void setLocationReverseConverter(final Converter<Location, StockroomLocationData> locationReverseConverter)
	{
		this.locationReverseConverter = locationReverseConverter;
	}

	@Required
	public void setLocationReversePopulator(final Populator<Location, StockroomLocationData> locationReversePopulator)
	{
		this.locationReversePopulator = locationReversePopulator;
	}

	@Required
	public void setItemLocationCurrentConverter(final Converter<ItemLocationData, ItemLocationCurrent> itemLocationCurrentConverter)
	{
		this.itemLocationCurrentConverter = itemLocationCurrentConverter;
	}

	@Required
	public void setItemLocationFutureConverter(final Converter<ItemLocationData, ItemLocationFuture> itemLocationFutureConverter)
	{
		this.itemLocationFutureConverter = itemLocationFutureConverter;
	}

	@Required
	public void setBinConverter(final Converter<BinData, Bin> binConverter)
	{
		this.binConverter = binConverter;
	}

	@Required
	public void setOmsInventoryConverter(final Converter<ItemQuantityData, OmsInventory> omsInventoryConverter)
	{
		this.omsInventoryConverter = omsInventoryConverter;
	}

	@Required
	public void setBinReverseConverter(final Converter<Bin, BinData> binReverseConverter)
	{
		this.binReverseConverter = binReverseConverter;
	}

	@Required
	public void setBinReversePopulator(final Populator<Bin, BinData> binReversePopulator)
	{
		this.binReversePopulator = binReversePopulator;
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setQueryObjectValidator(final Validator<QueryObject<?>> queryObjectValidator)
	{
		this.queryObjectValidator = queryObjectValidator;
	}

	@Required
	public void setItemStatusValidator(final Validator<ItemStatus> itemStatusValidator)
	{
		this.itemStatusValidator = itemStatusValidator;
	}

	@Required
	public void setOmsInventoryValidator(final Validator<OmsInventory> omsInventoryValidator)
	{
		this.omsInventoryValidator = omsInventoryValidator;
	}

	@Required
	public void setLocationValidator(final Validator<Location> locationValidator)
	{
		this.locationValidator = locationValidator;
	}

	@Required
	public void setBinValidator(final Validator<Bin> binValidator)
	{
		this.binValidator = binValidator;
	}

	@Required
	public void setEntityValidationHandler(final FailureHandler entityValidationHandler)
	{
		this.entityValidationHandler = entityValidationHandler;
	}

	protected CisService getCisService()
	{
		return cisService;
	}

	protected FailureHandler getEntityValidationHandler()
	{
		return entityValidationHandler;
	}

	protected JobWorkerBean getInventoryRolloverWorker()
	{
		return inventoryRolloverWorker;
	}

	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}

	protected JobSchedulerService getJobSchedulerService()
	{
		return jobSchedulerService;
	}

	protected Converter<ItemStatusData, ItemStatus> getItemStatusConverter()
	{
		return itemStatusConverter;
	}

	protected Converter<ItemStatus, ItemStatusData> getItemStatusReverseConverter()
	{
		return itemStatusReverseConverter;
	}

	protected Converter<StockroomLocationData, Location> getLocationConverter()
	{
		return locationConverter;
	}

	protected Converter<Location, StockroomLocationData> getLocationReverseConverter()
	{
		return locationReverseConverter;
	}

	protected Populator<Location, StockroomLocationData> getLocationReversePopulator()
	{
		return locationReversePopulator;
	}

	protected Converter<ItemLocationData, ItemLocationCurrent> getItemLocationCurrentConverter()
	{
		return itemLocationCurrentConverter;
	}

	protected Converter<ItemLocationData, ItemLocationFuture> getItemLocationFutureConverter()
	{
		return itemLocationFutureConverter;
	}

	protected Converter<BinData, Bin> getBinConverter()
	{
		return binConverter;
	}

	protected Converter<ItemQuantityData, OmsInventory> getOmsInventoryConverter()
	{
		return omsInventoryConverter;
	}

	protected Converter<Bin, BinData> getBinReverseConverter()
	{
		return binReverseConverter;
	}

	protected Populator<Bin, BinData> getBinReversePopulator()
	{
		return binReversePopulator;
	}

	protected Converters getConverters()
	{
		return converters;
	}

	protected Validator<QueryObject<?>> getQueryObjectValidator()
	{
		return queryObjectValidator;
	}

	protected Validator<ItemStatus> getItemStatusValidator()
	{
		return itemStatusValidator;
	}

	protected Validator<OmsInventory> getOmsInventoryValidator()
	{
		return omsInventoryValidator;
	}

	protected Validator<Location> getLocationValidator()
	{
		return locationValidator;
	}

	protected Validator<Bin> getBinValidator()
	{
		return binValidator;
	}
}
