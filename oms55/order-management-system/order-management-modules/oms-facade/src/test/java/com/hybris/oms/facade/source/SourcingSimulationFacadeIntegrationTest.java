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
package com.hybris.oms.facade.source;

import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.api.fulfillment.SourceSimulationFacade;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.inventory.Location;
import com.hybris.oms.domain.inventory.SkuQuantity;
import com.hybris.oms.domain.order.jaxb.SourceSimulationParameter;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.inventory.impl.InventoryTestHelper;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableSet;


/**
 * Integration test for {@link com.hybris.oms.facade.fulfillment.DefaultSourcingSimulationFacade}.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class SourcingSimulationFacadeIntegrationTest
{
	private static final String LOC2 = "loc2";
	private static final String LOC1 = "loc1";
	private static final String LOC3 = "loc3";
	private static final String SKU2 = "sku2";
	private static final String SKU1 = "sku1";
	private static final String ON_HAND = "ON_HAND";
	private static final String ATS_ID = "WEB";
	private static final double LATITUDE = 44d;
	private static final double LONGITUDE_LOC = 44d;
	private static final double LONGITUDE_LOC_2 = 50d;
	private static final double LONGITUDE_LOC_3 = 55d;
	private static final String COUNTRY_CODE = "DE";
	private static final String COUNTRY_CODE_3 = "CA";

	private static final Address address = new Address(null, null, null, null, null, LATITUDE, LONGITUDE_LOC, COUNTRY_CODE, null,
			null, null);
	private static final Address address3 = new Address(null, null, null, null, null, LATITUDE, LONGITUDE_LOC_3, COUNTRY_CODE_3,
			null, null, null);

	@Autowired
	private PersistenceManager pmgr;

	@Autowired
	private AtsService atsService;

	@Autowired
	private AggregationService aggregationService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Resource
	private JdbcPersistenceEngine persistenceEngine;

	@Autowired
	@Qualifier("defaultSourcingSimulationFacade")
	private SourceSimulationFacade sourceSimulationFacade;

	private CountryData country1;

	private CountryData country3;

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

	@Test
	public void shouldFilterLocation()
	{
		this.init();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final List<SkuQuantity> lines = new ArrayList<>();
				lines.add(new SkuQuantity(SKU1, new Quantity("", 10)));
				final SourceSimulationParameter param = new SourceSimulationParameter();
				param.setAtsId(ATS_ID);
				param.setSkuQuantities(lines);
				param.setLocationIds(Collections.singletonList(LOC2));
				param.setAddress(address);
				final Location location = sourceSimulationFacade.findBestSourcingLocation(param);
				Assert.assertEquals(LOC2, location.getLocationId());
				return null;
			}
		});
	}

	@Test
	public void shouldFilterCountry()
	{
		this.init();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final List<SkuQuantity> lines = new ArrayList<>();
				lines.add(new SkuQuantity(SKU1, new Quantity("", 10)));
				final SourceSimulationParameter param = new SourceSimulationParameter();
				param.setAtsId(ATS_ID);
				param.setSkuQuantities(lines);
				param.setAddress(address3);
				final Location location = sourceSimulationFacade.findBestSourcingLocation(param);
				Assert.assertEquals(LOC3, location.getLocationId());
				return null;
			}
		});
	}


	@Test
	public void shouldRetrieveLocationByHighestAts()
	{
		this.init();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final List<SkuQuantity> lines = new ArrayList<>();
				lines.add(new SkuQuantity(SKU1, new Quantity("", 10)));
				final SourceSimulationParameter param = new SourceSimulationParameter();
				param.setAtsId(ATS_ID);
				param.setSkuQuantities(lines);
				param.setAddress(address);
				final Location location = sourceSimulationFacade.findBestSourcingLocation(param);
				Assert.assertEquals(LOC1, location.getLocationId());
				return null;
			}
		});
	}

	@Test
	public void shouldRetrieveLocationBySequence()
	{
		this.init();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final List<SkuQuantity> lines = new ArrayList<>();
				lines.add(new SkuQuantity(SKU1, new Quantity("", 10)));
				final SourceSimulationParameter param = new SourceSimulationParameter();
				param.setSkuQuantities(lines);
				final Location location = sourceSimulationFacade.findBestSourcingLocation(param);
				Assert.assertEquals(LOC2, location.getLocationId());
				return null;
			}
		});
	}

	@Test
	public void shouldRetrieveLocationByDistance()
	{
		this.init();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final List<SkuQuantity> lines = new ArrayList<>();
				lines.add(new SkuQuantity(SKU1, new Quantity("", 10)));
				final SourceSimulationParameter param = new SourceSimulationParameter();
				param.setSkuQuantities(lines);
				final Address addr = new Address();
				addr.setLatitudeValue(Double.valueOf(LATITUDE));
				addr.setLongitudeValue(Double.valueOf(48d));
				addr.setPostalZone("12345");
				param.setAddress(addr);
				Location location = sourceSimulationFacade.findBestSourcingLocation(param);
				Assert.assertEquals(LOC2, location.getLocationId());
				addr.setLongitudeValue(Double.valueOf(42d));
				param.setAddress(addr);
				location = sourceSimulationFacade.findBestSourcingLocation(param);
				Assert.assertEquals(LOC1, location.getLocationId());
				return null;
			}
		});
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowLocationNotFound()
	{
		this.init();
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final List<SkuQuantity> lines = new ArrayList<>();
				lines.add(new SkuQuantity("Unknown", new Quantity("", 10)));
				final SourceSimulationParameter param = new SourceSimulationParameter();
				param.setSkuQuantities(lines);
				param.setAddress(address);
				sourceSimulationFacade.findBestSourcingLocation(param);
				return null;
			}
		});
	}

	private void createItemStatus()
	{
		final ItemStatusData ist = this.pmgr.create(ItemStatusData.class);
		ist.setStatusCode(ON_HAND);
		ist.setDescription("on hand");
		final ItemStatusData ist2 = this.pmgr.create(ItemStatusData.class);
		ist2.setStatusCode("RESERVED");
		ist2.setDescription("reserved");
	}

	private void createItemQuantities()
	{
		final InventoryTestHelper inventoryTestHelper = new InventoryTestHelper(pmgr);
		final StockroomLocationData loc = this.pmgr.create(StockroomLocationData.class);
		loc.setLocationId(LOC1);
		loc.setPriority(2);
		loc.setAddress(this.createAddress(LOC1));
		loc.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		loc.setShipToCountries(ImmutableSet.of(country1));
		final BinData binData1 = inventoryTestHelper.createDefaultBin(loc);

		final StockroomLocationData loc2 = this.pmgr.create(StockroomLocationData.class);
		loc2.setLocationId(LOC2);
		loc2.setPriority(1);
		loc2.setAddress(this.createAddress(LOC2));
		loc2.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		loc2.setShipToCountries(ImmutableSet.of(country1));
		final BinData binData2 = inventoryTestHelper.createDefaultBin(loc2);

		final StockroomLocationData loc3 = this.pmgr.create(StockroomLocationData.class);
		loc3.setLocationId(LOC3);
		loc3.setPriority(3);
		loc3.setAddress(this.createAddress(LOC3));
		loc3.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		loc3.setShipToCountries(ImmutableSet.of(country3));
		final BinData binData3 = inventoryTestHelper.createDefaultBin(loc3);

		final ItemLocationData il1 = this.pmgr.create(ItemLocationData.class);
		il1.setItemId(SKU1);
		il1.setStockroomLocation(loc);
		il1.setFuture(false);
		il1.setBin(binData1);
		final ItemLocationData il2 = this.pmgr.create(ItemLocationData.class);
		il2.setItemId(SKU1);
		il2.setStockroomLocation(loc2);
		il2.setFuture(false);
		il2.setBin(binData2);
		final CurrentItemQuantityData ci = this.pmgr.create(CurrentItemQuantityData.class);
		ci.setOwner(il1);
		ci.setStatusCode(ON_HAND);
		setQuantity(ci, new Quantity("", 111));
		final CurrentItemQuantityData ci2 = this.pmgr.create(CurrentItemQuantityData.class);
		ci2.setOwner(il2);
		ci2.setStatusCode(ON_HAND);
		setQuantity(ci2, new Quantity("", 44));
		final ItemLocationData il3 = this.pmgr.create(ItemLocationData.class);
		il3.setItemId(SKU2);
		il3.setStockroomLocation(loc);
		il3.setFuture(false);
		il3.setBin(binData1);
		final CurrentItemQuantityData ci3 = this.pmgr.create(CurrentItemQuantityData.class);
		ci3.setOwner(il3);
		ci3.setStatusCode(ON_HAND);
		setQuantity(ci3, new Quantity("", 10));

		final ItemLocationData il4 = this.pmgr.create(ItemLocationData.class);
		il4.setItemId(SKU1);
		il4.setStockroomLocation(loc3);
		il4.setFuture(false);
		il4.setBin(binData3);
		final CurrentItemQuantityData ci4 = this.pmgr.create(CurrentItemQuantityData.class);
		ci4.setOwner(il4);
		ci4.setStatusCode(ON_HAND);
		setQuantity(ci4, new Quantity("", 44));
	}

	private AddressVT createAddress(final String locationId)
	{
		final AddressVT adr;
		if (LOC1.equals(locationId))
		{
			adr = new AddressVT(null, null, null, null, null, Double.valueOf(LATITUDE), Double.valueOf(LONGITUDE_LOC), COUNTRY_CODE,
					null, null, null);
		}
		else if (LOC2.equals(locationId))
		{
			adr = new AddressVT(null, null, null, null, null, Double.valueOf(LATITUDE), Double.valueOf(LONGITUDE_LOC_2),
					COUNTRY_CODE, null, null, null);
		}
		else
		{
			adr = new AddressVT(null, null, null, null, null, Double.valueOf(LATITUDE), Double.valueOf(LONGITUDE_LOC_3),
					COUNTRY_CODE_3, null, null, null);
		}
		return adr;
	}

	public static void setQuantity(final CurrentItemQuantityData ci, final Quantity quantity)
	{
		ci.setQuantityUnitCode(quantity.getUnitCode());
		ci.setQuantityValue(quantity.getValue());
	}

	private void createTenantPreference()
	{
		TenantPreferenceData tp = this.pmgr.create(TenantPreferenceData.class);
		tp.setProperty(TenantPreferenceConstants.PREF_KEY_ATS_CALCULATOR);
		tp.setValue(ATS_ID);

		tp = this.pmgr.create(TenantPreferenceData.class);
		tp.setProperty(TenantPreferenceConstants.PREF_KEY_SOURCING_ORDER_LEVEL);
		tp.setValue("ATS,DISTANCE,SEQUENCE");
	}

	private void createFormulas()
	{
		this.atsService.createFormula(ATS_ID, "I[ON_HAND]", "name", null);
	}

	private void createCountry()
	{
		CountryData country = this.pmgr.create(CountryData.class);
		country.setCode(COUNTRY_CODE);
		country.setName("Germany");
		country1 = country;
		country = this.pmgr.create(CountryData.class);
		country.setCode(COUNTRY_CODE_3);
		country.setName("Canada");
		country3 = country;
	}

	private void init()
	{
		OmsTestUtils.clearAggregates(aggregationService);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				createCountry();
				createItemStatus();
				createItemQuantities();
				pmgr.flush();
				createFormulas();
				createTenantPreference();
				return null;
			}
		});
		OmsTestUtils.waitForInventory(aggregationService, SKU1);
		OmsTestUtils.waitForInventory(aggregationService, SKU2);
	}

}
