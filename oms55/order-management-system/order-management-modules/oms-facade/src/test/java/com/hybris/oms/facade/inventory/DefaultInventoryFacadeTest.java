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
import com.hybris.commons.conversion.util.Converters;
import com.hybris.kernel.api.HybrisId;
import com.hybris.oms.api.inventory.OmsInventory;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Bin;
import com.hybris.oms.domain.inventory.BinQueryObject;
import com.hybris.oms.domain.inventory.ItemStatus;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.facade.conversion.impl.inventory.BinReversePopulator;
import com.hybris.oms.facade.conversion.impl.inventory.LocationReversePopulator;
import com.hybris.oms.facade.validation.Validator;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.inventory.InventoryServiceConstants;
import com.hybris.oms.service.inventory.impl.LocationDataStaticUtils;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.ItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(LocationDataStaticUtils.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "ch.qos.logback.*", "org.slf4j.*"})
public class DefaultInventoryFacadeTest
{

	@Mock
	private InventoryService inventoryService;

	@Mock
	private Converter<StockroomLocationData, Location> locationConverter;

	@Mock
	private Converter<Location, StockroomLocationData> locationReverseConverter;

	@Mock
	private LocationReversePopulator locationReversePopulator;

	@Mock
	private Converter<ItemStatusData, ItemStatus> itemStatusConverter;

	@Mock
	private Converter<ItemStatus, ItemStatusData> itemStatusReverseConverter;

	@Mock
	private Converter<BinData, Bin> binConverter;

	@Mock
	private Converter<Bin, BinData> binReverseConverter;

	@Mock
	private BinReversePopulator binReversePopulator;

	@Mock
	private Converters converters;

	@Mock
	private Validator<ItemStatus> itemStatusValidator;

	@Mock
	private Validator<OmsInventory> omsInventoryValidator;

	@Mock
	private Validator<Location> locationValidator;

	@Mock
	private Validator<Bin> binValidator;

	@Mock
	private Converter<ItemQuantityData, OmsInventory> omsInventoryConverter;

	@InjectMocks
	private final DefaultInventoryFacade facade = new DefaultInventoryFacade();

	@Before
	public final void prepareMocks()
	{
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(LocationDataStaticUtils.class);
	}

	@Test
	public void testCreateItemStatus()
	{
		this.facade.createItemStatus(null);
		Mockito.verify(this.inventoryService).createItemStatus(null);
	}

	@Test
	@PowerMockIgnore({"ch.qos.logback.*", "org.slf4j.*", "com.sun.org.*"})
	public void testCreateStockRoomLocation()
	{
		final StockroomLocationData binData = Mockito.mock(StockroomLocationData.class);
		Mockito.when(LocationDataStaticUtils.hasValidAddressGeocodes(Mockito.any(StockroomLocationData.class))).thenReturn(true);
		Mockito.when(inventoryService.createLocation(Mockito.any(StockroomLocationData.class))).thenReturn(binData);

		final Location location = new Location();
		location.setLocationId("testCreateStockRoomLocation");
		this.facade.createStockRoomLocation(location);
		Mockito.verify(this.inventoryService).createLocation(Mockito.any(StockroomLocationData.class));
	}

	@Test
	public void testFindAllItemStatuses()
	{
		this.facade.findAllItemStatuses();
		Mockito.verify(this.inventoryService).findAllItemStatuses();
	}

	@Test
	public void testGetItemStatusByStatusCode()
	{
		this.facade.getItemStatusByStatusCode(null);
		Mockito.verify(this.inventoryService).getItemStatusByStatusCode(null);
	}

	@Test
	public void testGetLocationByLocationId()
	{
		this.facade.getStockRoomLocationByLocationId(null);
		Mockito.verify(this.inventoryService).getLocationByLocationId(null);
	}

	@Test
	public void testUpdateStockRoomLocation()
	{
		final Location location = new Location();
		location.setLocationId("locationId");

		// call method in test
		this.facade.updateStockRoomLocation(location);
		// verify if service was called
		Mockito.verify(this.inventoryService).updateLocation(Mockito.any(StockroomLocationData.class));
	}

	@Test(expected = EntityNotFoundException.class)
	public void updateInexistentStockRoomLocation()
	{
		final String locationId = "locationId";
		final Location location = new Location();
		location.setLocationId(locationId);

		Mockito.when(this.inventoryService.getLocationByLocationId(locationId)).thenThrow(new EntityNotFoundException(""));

		// call method in test
		this.facade.updateStockRoomLocation(location);
	}

	@Test
	public void createUpdateExistentStockRoomLocation()
	{
		final HybrisId hybrisId = new HybrisId("tenant", "typeCode", "key");
		final String locationId = "locationId";
		final StockroomLocationData mockedLocationData = Mockito.mock(StockroomLocationData.class);
		final Location location = new Location();
		location.setLocationId(locationId);

		Mockito.when(mockedLocationData.getId()).thenReturn(hybrisId);
		Mockito.when(this.inventoryService.getLocationByLocationId(locationId)).thenReturn(mockedLocationData);
		Mockito.when(this.inventoryService.updateLocation(mockedLocationData)).thenReturn(mockedLocationData);
		// Mockito.when(this.locationReversePopulator.populate(location)).thenReturn(mockedLocationData);
		Mockito.when(this.locationConverter.convert(mockedLocationData)).thenReturn(location);

		// call method in test
		this.facade.createUpdateStockRoomLocation(location);
		// verify if update service was called
		Mockito.verify(this.locationReversePopulator).populate(location, mockedLocationData);
		Mockito.verify(this.inventoryService).updateLocation(Mockito.any(StockroomLocationData.class));
		Mockito.verify(this.locationConverter).convert(mockedLocationData);
	}

	@Test
	public void createUpdateNewStockRoomLocation()
	{
		final String locationId = "locationId";
		final StockroomLocationData mockedLocationData = Mockito.mock(StockroomLocationData.class);
		final Location location = new Location();
		location.setLocationId(locationId);

		Mockito.when(this.inventoryService.getLocationByLocationId(locationId)).thenThrow(new EntityNotFoundException(""));
		Mockito.when(this.inventoryService.createLocation(Mockito.any(StockroomLocationData.class))).thenReturn(mockedLocationData);
		Mockito.when(this.locationReverseConverter.convert(location)).thenReturn(mockedLocationData);
		Mockito.when(this.locationConverter.convert(mockedLocationData)).thenReturn(location);
		Mockito.when(mockedLocationData.getId()).thenReturn(new HybrisId("tenant", "typeCode", "key"));

		// call method in test
		this.facade.createUpdateStockRoomLocation(location);
		// verify if create service was called
		Mockito.verify(this.inventoryService).createLocation(Mockito.any(StockroomLocationData.class));
		Mockito.verify(this.locationReverseConverter).convert(location);
		Mockito.verify(this.locationConverter).convert(mockedLocationData);
	}

	private OmsInventory buildCurrentInventory()
	{
		final OmsInventory inventory = new OmsInventory();
		inventory.setSkuId("itemId");
		inventory.setLocationId("locationId");
		inventory.setStatus("statusCode");
		inventory.setUnitCode("unitCode");
		inventory.setBinCode(InventoryServiceConstants.DEFAULT_BIN);
		inventory.setQuantity(1);
		return inventory;
	}

	private OmsInventory buildFutureInventory()
	{
		final OmsInventory inventory = new OmsInventory();
		inventory.setSkuId("itemId");
		inventory.setLocationId("locationId");
		inventory.setStatus("statusCode");
		inventory.setUnitCode("unitCode");
		inventory.setQuantity(1);
		inventory.setDeliveryDate(new Date());
		return inventory;
	}

	@Test
	public void testCreateCurrentInventory()
	{
		final OmsInventory inventory = buildCurrentInventory();
		inventory.setBinCode("binCode");

		final ItemQuantityData iqd = Mockito.mock(ItemQuantityData.class);
		Mockito.when(
				inventoryService.createItemQuantity(inventory.getSkuId(), inventory.getLocationId(), inventory.getBinCode(),
						inventory.getStatus(), inventory.getUnitCode(), inventory.getQuantity(), null)).thenReturn(iqd);
		this.facade.createInventory(inventory);
		Mockito.verify(omsInventoryValidator).validate("OmsInventory", inventory, null);
		Mockito.verify(this.inventoryService).createItemQuantity(inventory.getSkuId(), inventory.getLocationId(),
				inventory.getBinCode(), inventory.getStatus(), inventory.getUnitCode(), inventory.getQuantity(), null);
		Mockito.verify(omsInventoryConverter).convert(iqd);
	}

	@Test
	public void testCreateFutureInventory()
	{
		final OmsInventory inventory = buildCurrentInventory();
		this.facade.createInventory(inventory);
		Mockito.verify(this.inventoryService).createItemQuantity(inventory.getSkuId(), inventory.getLocationId(),
				inventory.getBinCode(), inventory.getStatus(), inventory.getUnitCode(), inventory.getQuantity(),
				inventory.getDeliveryDate());
	}

	@Test
	public void testUpdateCurrentInventory()
	{
		// call method in test
		this.facade.updateInventory(this.buildCurrentInventory());
		// verify if inventoryService was called
		Mockito.verify(this.inventoryService).updateCurrentItemQuantity(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
	}

	@Test
	public void testUpdateFutureInventory()
	{
		// call method in test
		this.facade.updateInventory(this.buildFutureInventory());
		// verify if inventoryService was called
		Mockito.verify(this.inventoryService).updateFutureItemQuantity(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Date.class));
	}

	@Test
	public void testUpdateCurrentInventoryIncremental()
	{
		// call method in test
		this.facade.updateIncrementalInventory(this.buildCurrentInventory());
		// verify if inventoryService was called
		Mockito.verify(this.inventoryService).updateCurrentItemQuantityIncremental(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
	}

	@Test
	public void testUpdateFutureInventoryIncremental()
	{
		// call method in test
		this.facade.updateIncrementalInventory(this.buildFutureInventory());
		// verify if inventoryService was called
		Mockito.verify(this.inventoryService).updateFutureItemQuantityIncremental(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Date.class));
	}

	@Test
	public void testCreateUpdateExistentCurrentInventory()
	{
		final OmsInventory inventory = buildCurrentInventory();
		this.facade.createUpdateInventory(inventory);
		Mockito.verify(this.inventoryService).createUpdateItemQuantity(inventory.getSkuId(), inventory.getLocationId(),
				inventory.getBinCode(), inventory.getStatus(), inventory.getUnitCode(), inventory.getQuantity(), null);
	}

	@Test
	public void updateFutureInventoryDate() throws ParseException
	{

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final Calendar calendar = Calendar.getInstance();

		calendar.add(Calendar.DAY_OF_YEAR, 1);
		final Date tomorrowDate = sdf.parse(sdf.format(calendar.getTime()));

		calendar.add(Calendar.DAY_OF_YEAR, 2);
		final Date oldDeliveryDate = sdf.parse(sdf.format(calendar.getTime()));

		final List<OmsInventory> inventory = new ArrayList<>();
		final OmsInventory inventory1 = new OmsInventory();
		final OmsInventory inventory2 = new OmsInventory();

		inventory1.setSkuId("itemId1");
		inventory1.setLocationId("locationId1");
		inventory1.setStatus("statusCode1");
		inventory1.setUnitCode("unitCode1");
		inventory1.setQuantity(1);
		inventory.add(inventory1);

		inventory2.setSkuId("itemId2");
		inventory2.setLocationId("locationId2");
		inventory2.setStatus("statusCode2");
		inventory2.setUnitCode("unitCode2");
		inventory2.setQuantity(2);
		inventory.add(inventory2);

		// This condition is oldExpectedDeliveryDate is not equal to newly entered date, and new date is not equal to
		// current date
		inventory.get(0).setDeliveryDate(tomorrowDate);
		inventory.get(1).setDeliveryDate(tomorrowDate);

		this.facade.updateFutureInventoryDate(inventory, oldDeliveryDate);

		this.inventoryService.findItemLocationFutureBySkuLocation("skuId", "locationId");
	}

	/**
	 * Test facade.createBin and verify. Verifies if:
	 * binReverseConverter.convert
	 * binConverter.convert
	 * inventoryService.createBin
	 * are called
	 */
	@Test
	public void testCreateBin()
	{
		final Bin bin = new Bin();
		bin.setBinCode("testCreateBin");
		bin.setLocationId("testCreateBinLocationId");

		final BinData binData = Mockito.mock(BinData.class);

		Mockito.when(binReverseConverter.convert(bin)).thenReturn(binData);
		Mockito.when(binConverter.convert(Mockito.any(BinData.class))).thenReturn(bin);
		Mockito.when(inventoryService.createBin(binData)).thenReturn(binData);

		this.facade.createBin(bin);

		Mockito.verify(this.binValidator, Mockito.times(1)).validate("Bin", bin, null);
		Mockito.verify(this.binReverseConverter).convert(bin);
		Mockito.verify(this.inventoryService).createBin(Mockito.any(BinData.class));
		Mockito.verify(this.binConverter).convert(Mockito.any(BinData.class));

	}

	@Test
	public void testDeleteBin()
	{
		final BinQueryObject binQueryObject = new BinQueryObject();
		binQueryObject.setBinCode("testDeleteBin");
		binQueryObject.setLocationId("testDeleteBinLocationId");

		final Bin bin = new Bin();
		bin.setBinCode("testDeleteBin");
		bin.setLocationId("testDeleteBinLocationId");

		Mockito.when(binConverter.convert(Mockito.any(BinData.class))).thenReturn(bin);

		this.facade.deleteBinByBinCodeLocationId("testDeleteBin", "testDeleteBinLocationId");

		Mockito.verify(this.inventoryService).deleteBin(null);
		Mockito.verify(this.inventoryService).getBinByBinCodeLocationId(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testGetBin()
	{
		final BinQueryObject binQueryObject = new BinQueryObject();
		binQueryObject.setBinCode("testGetBin");
		binQueryObject.setLocationId("testGetBinLocationId");

		final Bin bin = new Bin();
		bin.setBinCode("testGetBin");
		bin.setLocationId("testGetBinLocationId");

		Mockito.when(binConverter.convert(Mockito.any(BinData.class))).thenReturn(bin);
		Mockito.when(inventoryService.getBinByBinCodeLocationId(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

		this.facade.getBinByBinCodeLocationId("testGetBin", "testGetBinLocationId");

		Mockito.verify(this.inventoryService).getBinByBinCodeLocationId(Mockito.anyString(), Mockito.anyString());
		Mockito.verify(this.binConverter).convert(Mockito.any(BinData.class));
	}

	@Test
	public void testUpdateBin()
	{
		final Bin bin = new Bin();
		bin.setBinCode("testUpdateBin");
		bin.setLocationId("testUpdateBinLocationId");

		final BinData binData = Mockito.mock(BinData.class);

		Mockito.when(inventoryService.createBin(binData)).thenReturn(binData);

		this.facade.updateBin(bin);

		Mockito.verify(this.binValidator, Mockito.times(1)).validate("Bin", bin, null);
		Mockito.verify(this.inventoryService).updateBin(Mockito.any(BinData.class));
		Mockito.verify(this.binReversePopulator).populate(Mockito.any(Bin.class), Mockito.any(BinData.class));
	}

	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("DefaultInventoryFacadeTest [inventoryService=").append(inventoryService).append(", validationService=")
				.append(", locationConverter=").append(locationConverter).append(", locationReverseConverter=")
				.append(locationReverseConverter).append(", itemStatusConverter=").append(itemStatusConverter)
				.append(", itemStatusReverseConverter=").append(itemStatusReverseConverter).append(", converters=")
				.append(converters).append(", itemStatusValidator=").append(itemStatusValidator).append(", omsInventoryValidator=")
				.append(omsInventoryValidator).append(", locationValidator=").append(locationValidator).append(", facade=")
				.append(facade).append("]");
		return builder.toString();
	}
}
