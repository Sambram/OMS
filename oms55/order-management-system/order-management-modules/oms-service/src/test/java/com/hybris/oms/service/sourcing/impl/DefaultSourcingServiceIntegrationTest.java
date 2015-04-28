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
package com.hybris.oms.service.sourcing.impl;

import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_SOURCING_PREFIX;

import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.kernel.regioncache.CacheController;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.i18n.CountryData;
import com.hybris.oms.service.managedobjects.inventory.BinData;
import com.hybris.oms.service.managedobjects.inventory.CurrentItemQuantityData;
import com.hybris.oms.service.managedobjects.inventory.ItemLocationData;
import com.hybris.oms.service.managedobjects.inventory.ItemStatusData;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.preference.TenantPreferenceData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.sourcing.RadianCoordinates;
import com.hybris.oms.service.sourcing.SourcingLine;
import com.hybris.oms.service.sourcing.SourcingOLQ;
import com.hybris.oms.service.sourcing.SourcingResult;
import com.hybris.oms.service.sourcing.SourcingResult.ResultEnum;
import com.hybris.oms.service.sourcing.builder.SourcingComparatorFactory;
import com.hybris.oms.service.sourcing.context.LocationInfo;
import com.hybris.oms.service.sourcing.context.SourcingConfiguration;
import com.hybris.oms.service.sourcing.context.SourcingLocation;
import com.hybris.oms.service.sourcing.context.SourcingSplitOption;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.fest.assertions.Assertions;
import org.fest.assertions.MapAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/**
 * Integration test for {@link DefaultSourcingService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
@SuppressWarnings("PMD.TooManyPublicMethods")
public class DefaultSourcingServiceIntegrationTest
{
	private static final String PICKUP_IN_STORE_STRATEGY = "PickupInStoreStrategy";
	private static final String ITEM_GROUPING_STRATEGY = "ItemGroupingStrategy";
	private static final String WHOLE_ORDER_STRATEGY = "WholeOrderStrategy";
	private static final String SPLIT_ORDER_LINE_STRATEGY = "SplitOrderLineStrategy";
	private static final String WHOLE_ORDER_LINE_STRATEGY = "WholeOrderLineStrategy";
	private static final String LOC_BASESTORE = "LOCBASESTORE";
	private static final String BASE_STORE_NAME = "BaseStoreName";

	private static final String EUR = "EUR";

	private static final String ATS_ID = "myAts";
	private static final String ON_HAND = "ON_HAND";
	private static final String SOURCED = "SOURCED";

	private static final String LOC_ID = "loc1";
	private static final String LOC_ID_2 = "loc2";
	private static final String LOC_ID_3 = "loc3";
	private static final String LOC_ID_4 = "loc4";
	private static final String LOC_PICKUP_1 = "locPickUp1";
	private static final String LOC_PICKUP_2 = "locPickUp2";
	private static final String SKU = "sku1";
	private static final String SKU_2 = "sku2";
	private static final String ORDER_ID = "o1";
	private static final String OL_ID = "ol1";
	private static final String OL_ID_2 = "ol2";
	private static final double LATITUDE = 44d;
	private static final double LONGITUDE_LOC = 44d;
	private static final double LONGITUDE_LOC_2 = 50d;
	private static final String ORDER_BASESTORE_ID = "order2BaseStore";
	private static final String INVALID_COUNTRY = "INVALID";
	private static final String CANCELLED = "CANCELLED";

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private DefaultSourcingService sourcingService;

	@Autowired
	private AtsService atsService;

	@Autowired
	private AggregationService aggregationService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Resource
	private JdbcPersistenceEngine persistenceEngine;
	@Resource
	private CacheController cacheController;

	private final LocationInfo info1 = new LocationInfo(LOC_ID);

	private final LocationInfo info2 = new LocationInfo(LOC_ID_2);

	private final LocationInfo infoPickUp1 = new LocationInfo(0, LOC_PICKUP_1, Double.MAX_VALUE, ImmutableSet.of("PICKUP"));

	private final LocationInfo infoPickUp2 = new LocationInfo(0, LOC_PICKUP_2, Double.MAX_VALUE, ImmutableSet.of("PICKUP"));

	private final LocationInfo infoBS = new LocationInfo(LOC_BASESTORE);

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
		OmsTestUtils.clearCaches(cacheController);
	}

	/*
	 * order: [sku=sku1,qty=1,olId=ol1] [sku=sku2,qty=1,olId=ol2]]
	 *
	 * OverRidelocationIds = null BaseStore: LOCBASESTORE --- inv: [sku = sku1 { loc= LOCBASESTORE, Q= 1} ] [sku = sku2 {
	 * loc= LOCBASESTORE, Q= 1} ] ----- expected result: both Lines sourced from LOCBASESTORE
	 */
	@Test
	public void shouldSourceFromBaseStoreLocation()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, null, null, null), new SourcingLine(
				SKU_2, 1, OL_ID_2, null, null, null));
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.infoBS, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.infoBS, 1)));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);

		@SuppressWarnings("unchecked")
		final Set<String> locationIds = Collections.EMPTY_SET;

		this.initBaseStore(matrix, lines, config, locationIds, null);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_BASESTORE_ID);

				final OrderLineData orderLine = order.getOrderLines().get(0);
				final OrderLineQuantityData olq = orderLine.getOrderLineQuantities().get(0);

				final OrderLineData orderLine2 = order.getOrderLines().get(1);
				final OrderLineQuantityData olq2 = orderLine2.getOrderLineQuantities().get(0);

				Assert.assertEquals(LOC_BASESTORE, olq.getStockroomLocationId());
				Assert.assertEquals(LOC_BASESTORE, olq2.getStockroomLocationId());

				return null;
			}
				});
	}

	/*
	 * order: [sku=sku1,qty=1,olId=ol1,pickLocId=loc1] [sku=sku2,qty=1,olId=ol2]]
	 *
	 * OverRidelocationIds = null BaseStore: LOCBASESTORE --- inv: [sku = sku1 { loc= LOCBASESTORE, Q= 1}, {locId=loc1,
	 * Q=1} ] [sku = sku2 { loc= LOCBASESTORE, Q= 1} ] ----- expected result: first line shall be sourced from Loc1 since
	 * it has a pickup store id: Loc1 second line shall be sourced from LOCBASESTORE
	 */

	@Test
	public void shouldSourceFromBaseStoreLocationsForOneLineAndPickUpStoreForTheOther()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, LOC_PICKUP_1, null, null),
				new SourcingLine(SKU_2, 1, OL_ID_2, null, null, null));
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.infoPickUp1, 1), new SourcingLocation(this.infoBS, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.infoBS, 1)));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		@SuppressWarnings("unchecked")
		final Set<String> locationIds = Collections.EMPTY_SET;
		this.initBaseStore(matrix, lines, config, locationIds, null);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_BASESTORE_ID);

				final OrderLineData orderLine = order.getOrderLines().get(0);
				final OrderLineQuantityData olq = orderLine.getOrderLineQuantities().get(0);

				final OrderLineData orderLine2 = order.getOrderLines().get(1);
				final OrderLineQuantityData olq2 = orderLine2.getOrderLineQuantities().get(0);

				if (LOC_BASESTORE.equals(olq.getStockroomLocationId()))
				{
					Assert.assertEquals(LOC_BASESTORE, olq.getStockroomLocationId());
					Assert.assertEquals(LOC_PICKUP_1, olq2.getStockroomLocationId());
				}
				else
				{
					Assert.assertEquals(LOC_PICKUP_1, olq.getStockroomLocationId());
					Assert.assertEquals(LOC_BASESTORE, olq2.getStockroomLocationId());
				}


				return null;
			}
				});
	}

	/*
	 * order: [sku=sku1,qty=1,olId=ol1,locationRole=PickUP] [sku=sku2,qty=1,olId=ol2,locationRole=SHIPPING]]
	 *
	 * OverRidelocationIds = null BaseStore: LOCBASESTORE --- inv: [sku = sku1 { loc= LOCBASESTORE, Q= 1,
	 * locationRole=SHIPPING}, {locId=loc1, Q=1} ] [sku = sku2 { loc= LOCBASESTORE, Q= 1, locationRole=SHIPPING},
	 * {locId=loc2, Q=1} ] ----- expected result:
	 *
	 * only second orderLine will be sourced from LOCBASESTORE first orderLine is Pickup location and shouldn't be
	 * sourced from LOCBASESTORE since its role is Shippnig
	 */
	@Test
	public void shouldSourceFromBaseStoreWithProperLocationRole()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, null, ImmutableSet.of("PICKUP"), null),
				new SourcingLine(SKU_2, 1, OL_ID_2, null, ImmutableSet.of("SHIPPING"), null));

		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.infoBS, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.infoBS, 1)));

		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);

		@SuppressWarnings("unchecked")
		final Set<String> locationIds = Collections.EMPTY_SET;

		this.initBaseStore(matrix, lines, config, locationIds, null);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_BASESTORE_ID);

				final OrderLineData orderLine = order.getOrderLines().get(0);
				final List<OrderLineQuantityData> olqs1 = orderLine.getOrderLineQuantities();

				final OrderLineData orderLine2 = order.getOrderLines().get(1);
				final List<OrderLineQuantityData> olqs2 = orderLine2.getOrderLineQuantities();

				if (olqs1.isEmpty())
				{
					Assert.assertEquals(LOC_BASESTORE, olqs2.get(0).getStockroomLocationId());
				}
				else
				{
					Assert.assertEquals(LOC_BASESTORE, olqs1.get(0).getStockroomLocationId());
				}

				return null;
			}
				});
	}


	/*
	 * order: [sku=sku1,qty=1,olId=ol1,pickupstore= Loc1, locationRole=SHIPPING]
	 * [sku=sku2,qty=1,olId=ol2,locationRole=SHIPPING]]
	 *
	 * OverRidelocationIds = loc2 BaseStore: LOCBASESTORE --- inv: [sku = sku1 { loc= LOCBASESTORE, Q= 1,
	 * locationRole=SHIPPING}, {locId=loc1, Q=1} ] [sku = sku2 { loc= LOCBASESTORE, Q= 1, locationRole=SHIPPING},
	 * {locId=loc2, Q=1} ] ----- expected result: orderLine1 will be sourced from Loc1 as it is a pickupInStore Id is
	 * provided for it orderLine2 will be sourced from Loc2 as there is an over ride Location Id
	 */
	@Test
	public void shouldSourceFromBaseStoreWithProperLocationRoleAndOverRideLocations()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(
				new SourcingLine(SKU, 1, OL_ID, LOC_PICKUP_1, ImmutableSet.of("SHIPPING"), null), new SourcingLine(SKU_2, 1, OL_ID_2,
						null, ImmutableSet.of("SHIPPING"), null));

		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.infoPickUp1, 1), new SourcingLocation(this.infoBS, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.infoBS, 1)));

		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);

		final Set<String> locationIds = Sets.newHashSet(LOC_ID_2);

		this.initBaseStore(matrix, lines, config, locationIds, null);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_BASESTORE_ID);

				final OrderLineData orderLine = order.getOrderLines().get(0);
				final OrderLineQuantityData olq = orderLine.getOrderLineQuantities().get(0);

				final OrderLineData orderLine2 = order.getOrderLines().get(1);
				final OrderLineQuantityData olq2 = orderLine2.getOrderLineQuantities().get(0);

				if (LOC_PICKUP_1.equals(olq.getStockroomLocationId()))
				{
					Assert.assertEquals(LOC_PICKUP_1, olq.getStockroomLocationId());
					Assert.assertEquals(LOC_ID_2, olq2.getStockroomLocationId());
				}
				else
				{
					Assert.assertEquals(LOC_ID_2, olq.getStockroomLocationId());
					Assert.assertEquals(LOC_PICKUP_1, olq2.getStockroomLocationId());
				}

				return null;
			}
				});
	}

	/*
	 * order: [sku=sku1,qty=1,olId=ol1,pickLocId=loc1]
	 *
	 *
	 * OverRidelocationIds = loc1 BaseStore: LOCBASESTORE --- inv: [sku = sku1 { loc= LOCBASESTORE, Q= 1}, {locId=loc1,
	 * Q=1} ] ----- expected result: the only line shall be sourced from Loc1 (even though LOCBASESTORE have enough
	 * inventory)
	 */
	@Test
	public void shouldSourceFromOverRideLocationIdsNotBaseStore()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, LOC_PICKUP_1, null, null));

		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.infoPickUp1, 1), new SourcingLocation(this.infoBS, 1)));

		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);

		final Set<String> locationIds = Sets.newHashSet(LOC_PICKUP_1);

		this.initBaseStore(matrix, lines, config, locationIds, null);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_BASESTORE_ID);

				final OrderLineData orderLine = order.getOrderLines().get(0);
				final OrderLineQuantityData olq = orderLine.getOrderLineQuantities().get(0);

				Assert.assertEquals(LOC_PICKUP_1, olq.getStockroomLocationId());

				return null;
			}
				});
	}


	/*
	 * order: [sku=sku1,qty=1,olId=ol1]
	 *
	 *
	 * OverRidelocationIds = null BaseStore: LOCBASESTORE shipToCountry: INVALID --- inv: [sku = sku1 { loc=
	 * LOCBASESTORE, Q= 1} ]
	 *
	 * ----- expected result: the line won't be sourced because INVALID country doesn't exist
	 */
	@Test
	public void shouldFailSourcingFromInvalidCountry()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, null, null, INVALID_COUNTRY));
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.infoBS, 1)));

		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);

		@SuppressWarnings("unchecked")
		final Set<String> locationIds = Collections.EMPTY_SET;
		// init data with invalid country
		this.initBaseStore(matrix, lines, config, locationIds, INVALID_COUNTRY);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_BASESTORE_ID);

				final OrderLineData orderLine = order.getOrderLines().get(0);
				Assert.assertEquals(0, orderLine.getOrderLineQuantities().size());
				return null;
			}
				});
	}



	/*
	 * order: [sku=sku1,qty=1,olId=ol1]
	 *
	 *
	 * OverRidelocationIds = null BaseStore: LOCBASESTORE shipToCountry: CA --- inv: [sku = sku1 { loc= LOCBASESTORE, Q=
	 * 1} ]
	 *
	 * ----- expected result: the line will be sourced from LOC_BASESTORE
	 */
	@Test
	public void shouldSourceFromValidCountry()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, null, null, null));
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.infoBS, 1)));

		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);

		@SuppressWarnings("unchecked")
		final Set<String> locationIds = Collections.EMPTY_SET;
		// init data with CA as country code for shipping address
		this.initBaseStore(matrix, lines, config, locationIds, "CA");
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_BASESTORE_ID);

				final OrderLineData orderLine = order.getOrderLines().get(0);
				final OrderLineQuantityData olq = orderLine.getOrderLineQuantities().get(0);
				// we expect the line to be sorced from LOC_BASESTORE
				Assert.assertEquals(LOC_BASESTORE, olq.getStockroomLocationId());
				return null;
			}
				});
	}

	/*
	 * order: [sku=sku1,qty=1,olId=ol1, locationRole=PICKUP] [sku=sku2,qty=1,olId=ol2,locationRole=PICKUP]]
	 *
	 * OverRidelocationIds = null BaseStore: LOCBASESTORE --- inv: [sku = sku1 { loc= LOCBASESTORE, Q= 1,
	 * locationRole=SHIPPING} ] [sku = sku2 { loc= LOCBASESTORE, Q= 1, locationRole=SHIPPING}] ----- expected result:
	 * sourcing will result in no olqs
	 */
	@Test
	public void shouldSourceFromBaseStoreWithProperLocationRolePickUp()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, null, ImmutableSet.of("PICKUP"), null),
				new SourcingLine(SKU_2, 1, OL_ID_2, null, ImmutableSet.of("PICKUP"), null));

		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.infoBS, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.infoBS, 1)));

		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);

		@SuppressWarnings("unchecked")
		final Set<String> locationIds = Collections.EMPTY_SET;
		this.initBaseStore(matrix, lines, config, locationIds, null);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_BASESTORE_ID);

				final OrderLineData orderLine = order.getOrderLines().get(0);
				// we expect 0 results since the asked orderLines are of location Role Pickup and the default LocationRole
				// for ORDER_BASESTORE_ID is Shipping
				Assert.assertEquals(0, orderLine.getOrderLineQuantities().size());
				return null;
			}
				});
	}

	@Test
	public void shouldSourceByOrderId()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_ID);
				final OrderLineData orderLine = order.getOrderLines().get(0);
				final OrderLineQuantityData olq = orderLine.getOrderLineQuantities().get(0);
				Assert.assertEquals(SKU, orderLine.getSkuId());
				Assert.assertEquals(LOC_ID, olq.getStockroomLocationId());
				Assert.assertEquals(OL_ID, orderLine.getOrderLineId());
				Assert.assertEquals(0, orderLine.getQuantityUnassignedValue());
				Assert.assertEquals(1, olq.getQuantityValue());
				Assert.assertEquals(SOURCED, olq.getStatus().getStatusCode());
				return null;
			}
				});
	}

	@Test
	public void shouldSourceOrderAndSetOrderOnHold()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false, SourcingSplitOption.ON_HOLD,
				SourcingSplitOption.CANCELLED);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_ID);
				final OrderLineData orderLine = order.getOrderLines().get(0);
				Assert.assertEquals(SourcingSplitOption.ON_HOLD.getAction(), orderLine.getOrderLineStatus());
				Assertions.assertThat(orderLine.getOrderLineQuantities()).isEmpty();
				return null;
			}
				});
	}

	@Test
	public void shouldPartiallySourceOrder()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false, SourcingSplitOption.SPLIT,
				SourcingSplitOption.SPLIT);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_ID);
				final OrderLineData orderLine = order.getOrderLines().get(0);
				Assert.assertNull(orderLine.getOrderLineStatus());
				Assert.assertEquals(1, orderLine.getOrderLineQuantities().get(0).getQuantityValue());
				Assert.assertEquals(LOC_ID_2, orderLine.getOrderLineQuantities().get(0).getStockroomLocationId());
				return null;
			}
				});
	}

	@Test
	public void shouldOnlySourceQuantityUnassigned()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 100)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false, SourcingSplitOption.SPLIT,
				SourcingSplitOption.SPLIT);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = persistenceManager.getByIndex(OrderData.UX_ORDERS_ORDERID, ORDER_ID);
				order.getOrderLines().get(0).setQuantityUnassignedValue(1);
				return null;
			}
				});
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_ID);
				final OrderLineData orderLine = order.getOrderLines().get(0);
				Assert.assertNull(orderLine.getOrderLineStatus());
				Assert.assertEquals(1, orderLine.getOrderLineQuantities().get(0).getQuantityValue());
				Assert.assertEquals(0, orderLine.getQuantityUnassignedValue());
				Assert.assertEquals(LOC_ID_2, orderLine.getOrderLineQuantities().get(0).getStockroomLocationId());
				return null;
			}
				});
	}

	@Test
	public void shouldSkipQuantityUnassignedZero()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 100)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false, SourcingSplitOption.SPLIT,
				SourcingSplitOption.SPLIT);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = persistenceManager.getByIndex(OrderData.UX_ORDERS_ORDERID, ORDER_ID);
				order.getOrderLines().get(0).setQuantityUnassignedValue(0);
				return null;
			}
				});
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_ID);
				final OrderLineData orderLine = order.getOrderLines().get(0);
				Assert.assertNull(orderLine.getOrderLineStatus());
				Assert.assertTrue("No OLQs generated", orderLine.getOrderLineQuantities().isEmpty());
				Assert.assertEquals(0, orderLine.getQuantityUnassignedValue());
				return null;
			}
				});
	}

	@Test
	public void shouldSourceOrderAndSetOrderLineCancelled()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false, SourcingSplitOption.SPLIT,
				SourcingSplitOption.CANCELLED);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_ID);
				final OrderLineData orderLine = order.getOrderLines().get(0);
				Assert.assertEquals(SourcingSplitOption.CANCELLED.getAction(), orderLine.getOrderLineStatus());
				Assertions.assertThat(orderLine.getOrderLineQuantities()).isEmpty();
				return null;
			}
				});
	}

	@Test
	public void shouldFilterLocations()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 2)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU_2, 2, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines, Collections.singleton(LOC_ID_2), null);
				Assert.assertEquals(SourcingResult.ResultEnum.PARTIAL, result.getStatus());
				Assert.assertEquals(SPLIT_ORDER_LINE_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS),
						MapAssert.entry(OL_ID_2, SourcingResult.ResultEnum.PARTIAL));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID),
						new SourcingOLQ(SKU_2, 1, LOC_ID_2, OL_ID_2));
				return null;
			}
				});
	}

	@Test
	public void shouldSourcePickupLocation()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.infoPickUp2, 1), new SourcingLocation(this.infoPickUp1, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.infoPickUp2, 1), new SourcingLocation(this.infoPickUp1, 2)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID, LOC_PICKUP_1, null, null),
				new SourcingLine(SKU_2, 1, OL_ID_2, LOC_PICKUP_2, null, null));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(PICKUP_IN_STORE_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS),
						MapAssert.entry(OL_ID_2, SourcingResult.ResultEnum.SUCCESS));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_PICKUP_1, OL_ID),
						new SourcingOLQ(SKU_2, 1, LOC_PICKUP_2, OL_ID_2));
				return null;
			}
				});
	}

	@Test
	public void shouldSimulateSourcingByAts()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 2)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU_2, 2, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final StockroomLocationData location = sourcingService.simulateSourcing(lines, ATS_ID, null, null);
				Assert.assertEquals(LOC_ID, location.getLocationId());
				return null;
			}
				});
	}

	@Test
	public void shouldSimulateSourcingByDistance()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info1, 2), new SourcingLocation(this.info2, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU_2, 1, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final StockroomLocationData location = sourcingService.simulateSourcing(lines, null, null,
						RadianCoordinates.fromDegrees(LATITUDE, 48d));
				Assert.assertEquals(LOC_ID_2, location.getLocationId());
				return null;
			}
				});
	}

	@Test
	public void shouldSimulateSourcingByPriority()
	{
		final LocationInfo infoPrio1 = new LocationInfo(1, LOC_ID_2, 0d);
		final LocationInfo infoPrio2 = new LocationInfo(2, LOC_ID, 0d);
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(infoPrio2, 1), new SourcingLocation(infoPrio1, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(infoPrio2, 2), new SourcingLocation(infoPrio1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU_2, 1, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final StockroomLocationData location = sourcingService.simulateSourcing(lines, null, null, null);
				Assert.assertEquals(LOC_ID_2, location.getLocationId());
				return null;
			}
				});
	}

	@Test
	public void shouldFilterSimulatedSourcing()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 2), new SourcingLocation(this.info1, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info2, 2), new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final StockroomLocationData location = sourcingService.simulateSourcing(lines, ATS_ID, Collections.singleton(LOC_ID),
						null);
				Assert.assertEquals(LOC_ID, location.getLocationId());
				return null;
			}
				});
	}

	@Test
	public void shouldSimulateSourcingAndReturnNull()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final StockroomLocationData location = sourcingService.simulateSourcing(lines, ATS_ID, null, null);
				Assert.assertNull("Location is null", location);
				return null;
			}
				});
	}

	@Test
	public void shouldSourceWholeOrder()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 2)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU_2, 2, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(WHOLE_ORDER_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS),
						MapAssert.entry(OL_ID_2, SourcingResult.ResultEnum.SUCCESS));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID),
						new SourcingOLQ(SKU_2, 2, LOC_ID, OL_ID_2));
				return null;
			}
				});
	}


	@Test
	public void shouldFailSourcingWholeOrderAsCancelIfSplitIsTrue()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false, SourcingSplitOption.CANCELLED,
				SourcingSplitOption.CANCELLED);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
		{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final OrderData order = sourcingService.sourceOrder(ORDER_ID);
				Assert.assertEquals(order.getOrderLines().get(0).getOrderLineStatus(), CANCELLED);
				return null;
			}
		});
	}

	@Test
	public void shouldFailSourcingOrderlinesAsCancelIfSplitIsTrue()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false, SourcingSplitOption.CANCELLED,
				SourcingSplitOption.CANCELLED);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(ResultEnum.FAILURE, result.getStatus());
				Assert.assertEquals(WHOLE_ORDER_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, ResultEnum.FAILURE));
				Assertions.assertThat(result.getSourcingOlqs().isEmpty());
				return null;
			}
				});
	}

	@Test
	public void shouldSourceWholeOrderLine()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info1, 2)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU_2, 2, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(WHOLE_ORDER_LINE_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS),
						MapAssert.entry(OL_ID_2, SourcingResult.ResultEnum.SUCCESS));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID),
						new SourcingOLQ(SKU_2, 2, LOC_ID, OL_ID_2));
				return null;
			}
				});
	}

	@Test
	/**
	 * Testcase for https://jira.hybris.com/browse/OMSWSTHREE-402
	 */
	public void shouldSourceByAtsOrderAndOrderLine()
	{
		final LocationInfo info3 = new LocationInfo(3, LOC_ID_3);
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 2), new SourcingLocation(this.info2, 3),
				new SourcingLocation(info3, 2)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 5),
				new SourcingLocation(info3, 7)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 4, OL_ID), new SourcingLine(SKU_2, 4, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config, "DISTANCE,ATS");
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines, null,
						RadianCoordinates.fromDegrees(LATITUDE, LONGITUDE_LOC_2));
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(SPLIT_ORDER_LINE_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS),
						MapAssert.entry(OL_ID_2, SourcingResult.ResultEnum.SUCCESS));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 3, LOC_ID_2, OL_ID),
						new SourcingOLQ(SKU, 1, LOC_ID_3, OL_ID), new SourcingOLQ(SKU_2, 4, LOC_ID_3, OL_ID_2));
				return null;
			}
				});
	}

	@Test
	public void shouldSplitOrderLine()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(SPLIT_ORDER_LINE_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID),
						new SourcingOLQ(SKU, 1, LOC_ID, OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldAssignWholeOrderLineAndSplit()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 2)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID), new SourcingLine(SKU_2, 1, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(SPLIT_ORDER_LINE_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID),
						new SourcingOLQ(SKU, 1, LOC_ID, OL_ID), new SourcingOLQ(SKU_2, 1, LOC_ID, OL_ID_2));
				for (final SourcingOLQ olq : result.getSourcingOlqs())
				{
					if (olq.getSku().equals(SKU))
					{
						Assert.assertEquals(SPLIT_ORDER_LINE_STRATEGY, olq.getStrategyName());
					}
					else
					{
						Assert.assertEquals(WHOLE_ORDER_LINE_STRATEGY, olq.getStrategyName());
					}
				}
				return null;
			}
				});
	}

	@Test
	public void shouldAssignByShortestDistance()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info2, 1), new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines, null, RadianCoordinates.fromDegrees(LATITUDE, 48d));
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(WHOLE_ORDER_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldAssignByLowestPriority()
	{
		final LocationInfo infoPrio1 = new LocationInfo(1, LOC_ID_2, 0d);
		final LocationInfo infoPrio2 = new LocationInfo(2, LOC_ID, 0d);
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(infoPrio2, 1), new SourcingLocation(infoPrio1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(WHOLE_ORDER_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldAssignAndMarkLineAsFailure()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, false);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.PARTIAL, result.getStatus());
				Assert.assertEquals(SPLIT_ORDER_LINE_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.PARTIAL));
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldAssignByHighestATSWithItemGrouping()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info1, 1), new SourcingLocation(this.info2, 100)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 1, OL_ID), new SourcingLine(SKU_2, 2, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS),
						MapAssert.entry(OL_ID_2, SourcingResult.ResultEnum.SUCCESS));
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(ITEM_GROUPING_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU_2, 2, LOC_ID_2, OL_ID_2),
						new SourcingOLQ(SKU, 1, LOC_ID, OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldAssignByHighestATSAndDefaultWithItemGrouping()
	{
		final LocationInfo info3 = new LocationInfo(LOC_ID_3);
		final LocationInfo info4 = new LocationInfo(LOC_ID_4);

		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 8), new SourcingLocation(this.info2, 7),
				new SourcingLocation(info3, 6), new SourcingLocation(info4, 10)));
		matrix.put(SKU_2, Lists.newArrayList(new SourcingLocation(this.info1, 3), new SourcingLocation(this.info2, 4),
				new SourcingLocation(info3, 5), new SourcingLocation(info4, 6)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 15, OL_ID), new SourcingLine(SKU_2, 8, OL_ID_2));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS),
						MapAssert.entry(OL_ID_2, SourcingResult.ResultEnum.SUCCESS));
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(ITEM_GROUPING_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 10, LOC_ID_4, OL_ID),
						new SourcingOLQ(SKU, 5, LOC_ID, OL_ID), new SourcingOLQ(SKU_2, 6, LOC_ID_4, OL_ID_2),
						new SourcingOLQ(SKU_2, 2, LOC_ID, OL_ID_2));
				return null;
			}
				});
	}

	@Test
	public void shouldAssignPartiallyWithItemGrouping()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 100, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, ResultEnum.PARTIAL));
				Assert.assertEquals(SourcingResult.ResultEnum.PARTIAL, result.getStatus());
				Assert.assertEquals(ITEM_GROUPING_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldNotSplitOrderWithItemGrouping()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 100, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true, SourcingSplitOption.ON_HOLD,
				SourcingSplitOption.CANCELLED);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.FAILURE, result.getStatus());
				Assert.assertEquals(WHOLE_ORDER_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getSourcingOlqs()).isEmpty();
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.FAILURE));
				Assert.assertEquals(SourcingSplitOption.ON_HOLD.getAction(), result.getActionForLineId(OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldNotSplitOrderLineWithItemGrouping()
	{
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(this.info1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 100, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true, SourcingSplitOption.SPLIT,
				SourcingSplitOption.CANCELLED);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assert.assertEquals(SourcingResult.ResultEnum.FAILURE, result.getStatus());
				Assert.assertEquals(ITEM_GROUPING_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getSourcingOlqs()).isEmpty();
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.FAILURE));
				Assert.assertEquals(SourcingSplitOption.CANCELLED.getAction(), result.getActionForLineId(OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldAssignByLocationIdWithItemGrouping()
	{
		final LocationInfo infoPrio1 = new LocationInfo(1, LOC_ID_2);
		final LocationInfo infoPrio2 = new LocationInfo(2, LOC_ID);
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(infoPrio2, 1), new SourcingLocation(infoPrio1, 1)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 2, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS));
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(ITEM_GROUPING_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 1, LOC_ID, OL_ID),
						new SourcingOLQ(SKU, 1, LOC_ID_2, OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldAssignByATSAndLocationIdWithItemGrouping()
	{
		final LocationInfo infoPrio1 = new LocationInfo(1, LOC_ID_2);
		final LocationInfo infoPrio2 = new LocationInfo(2, LOC_ID);
		final LocationInfo infoPrio3 = new LocationInfo(3, LOC_ID_3);
		final Map<String, List<SourcingLocation>> matrix = new HashMap<>();
		matrix.put(SKU, Lists.newArrayList(new SourcingLocation(infoPrio1, 1), new SourcingLocation(infoPrio2, 1),
				new SourcingLocation(infoPrio3, 2)));
		final List<SourcingLine> lines = Lists.newArrayList(new SourcingLine(SKU, 3, OL_ID));
		final SourcingConfiguration config = new SourcingConfiguration(ATS_ID, true);
		this.init(matrix, lines, config);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final SourcingResult result = sourcingService.sourceInput(lines);
				Assertions.assertThat(result.getLineStatus()).includes(MapAssert.entry(OL_ID, SourcingResult.ResultEnum.SUCCESS));
				Assert.assertEquals(SourcingResult.ResultEnum.SUCCESS, result.getStatus());
				Assert.assertEquals(ITEM_GROUPING_STRATEGY, result.getStrategyName());
				Assertions.assertThat(result.getSourcingOlqs()).containsOnly(new SourcingOLQ(SKU, 2, LOC_ID_3, OL_ID),
						new SourcingOLQ(SKU, 1, LOC_ID, OL_ID));
				return null;
			}
				});
	}

	@Test
	public void shouldIgnoreEmptyLines()
	{
		Assert.assertTrue("ProcessStatus is empty", this.sourcingService.sourceInput(Collections.<SourcingLine>emptyList())
				.isEmpty());
	}

	@Test
	public void shouldFindAllSourcingSplitOptions()
	{
		final Set<String> options = this.sourcingService.findAllSourcingSplitOptions();
		Assert.assertEquals(3, options.size());
	}

	@Test
	public void shouldFindAllSourcingLocationComparators()
	{
		final Set<String> options = this.sourcingService.findAllSourcingLocationComparators();
		Assert.assertEquals(3, options.size());
		Assert.assertFalse(options.contains(SourcingComparatorFactory.getDefaultComparatorId()));
	}

	private void initBaseStore(final Map<String, List<SourcingLocation>> skuMatrix, final List<SourcingLine> lines,
			final SourcingConfiguration config, final Set<String> locationIds, final String countryCode)
	{
		OmsTestUtils.clearAggregates(aggregationService);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				initConfig(config, null);
				createItemStatus();
				createItemQuantities(skuMatrix);
				createOlqStatus();
				final BaseStoreData baseStore = createBaseStore();
				createOrderWithBaseStore(lines, baseStore, locationIds, countryCode);
				persistenceManager.flush();
				createFormula();
				return null;
			}
				});
		if (skuMatrix != null)
		{
			for (final String sku : skuMatrix.keySet())
			{
				OmsTestUtils.waitForInventory(aggregationService, sku);
			}
		}
	}

	private void init(final Map<String, List<SourcingLocation>> skuMatrix, final List<SourcingLine> lines,
			final SourcingConfiguration config)
	{
		this.init(skuMatrix, lines, config, null);
	}

	private void init(final Map<String, List<SourcingLocation>> skuMatrix, final List<SourcingLine> lines,
			final SourcingConfiguration config, final String descriptor)
	{
		OmsTestUtils.clearAggregates(aggregationService);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				initConfig(config, descriptor);
				createItemStatus();
				createItemQuantities(skuMatrix);
				createOlqStatus();
				createOrder(lines);
				persistenceManager.flush();
				createFormula();
				return null;
			}
				});
		if (skuMatrix != null)
		{
			for (final String sku : skuMatrix.keySet())
			{
				OmsTestUtils.waitForInventory(aggregationService, sku);
			}
		}
	}

	private void initConfig(final SourcingConfiguration config, final String descriptor)
	{
		if (config != null)
		{
			TenantPreferenceData tenantPrefs = this.persistenceManager.create(TenantPreferenceData.class);
			tenantPrefs.setProperty(TenantPreferenceConstants.PREF_KEY_ATS_CALCULATOR);
			tenantPrefs.setValue(config.getAtsId());
			tenantPrefs = this.persistenceManager.create(TenantPreferenceData.class);
			tenantPrefs.setProperty(TenantPreferenceConstants.PREF_KEY_ITEM_GROUPING);
			tenantPrefs.setValue(Boolean.toString(config.isMinimizeShipments()));
			tenantPrefs = this.persistenceManager.create(TenantPreferenceData.class);
			tenantPrefs.setProperty(TenantPreferenceConstants.PREF_KEY_SOURCING_ORDER_LEVEL);
			tenantPrefs.setValue(descriptor != null ? descriptor : "DISTANCE,SEQUENCE");
			tenantPrefs = this.persistenceManager.create(TenantPreferenceData.class);
			tenantPrefs.setProperty(TenantPreferenceConstants.PREF_KEY_SOURCING_OLINE_LVL);
			tenantPrefs.setValue(descriptor != null ? descriptor : "DISTANCE,SEQUENCE");
			tenantPrefs = this.persistenceManager.create(TenantPreferenceData.class);
			tenantPrefs.setProperty(TenantPreferenceConstants.PREF_KEY_SOURCING_OLQ_LEVEL);
			tenantPrefs.setValue(descriptor != null ? descriptor : "ATS,DISTANCE,SEQUENCE");
			tenantPrefs = this.persistenceManager.create(TenantPreferenceData.class);
			tenantPrefs.setProperty(TenantPreferenceConstants.PREF_KEY_ORDER_SPLITSTRATEGY);
			tenantPrefs.setValue(config.getOrderSplit().getAction());
			tenantPrefs = this.persistenceManager.create(TenantPreferenceData.class);
			tenantPrefs.setProperty(TenantPreferenceConstants.PREF_KEY_ORDERLINE_SPLITSTRATEGY);
			tenantPrefs.setValue(config.getOrderLineSplit().getAction());
		}
	}

	private void createItemStatus()
	{
		final ItemStatusData ist = this.persistenceManager.create(ItemStatusData.class);
		ist.setStatusCode(ON_HAND);
		ist.setDescription("on hand");
	}

	private void createOlqStatus()
	{
		final OrderLineQuantityStatusData olqStatus = this.persistenceManager.create(OrderLineQuantityStatusData.class);
		olqStatus.setStatusCode(SOURCED);
		final TenantPreferenceData tenantPrefs = this.persistenceManager.create(TenantPreferenceData.class);
		tenantPrefs.setProperty(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_SOURCED);
		tenantPrefs.setValue(SOURCED);
	}

	private Map<String, StockroomLocationData> createItemQuantities(final Map<String, List<SourcingLocation>> skuMatrix)
	{
		if (MapUtils.isNotEmpty(skuMatrix))
		{
			final Map<String, StockroomLocationData> locations = new HashMap<>();
			for (final Entry<String, List<SourcingLocation>> entry : skuMatrix.entrySet())
			{
				for (final SourcingLocation sourcingLocation : entry.getValue())
				{
					final String locationId = sourcingLocation.getInfo().getLocationId();
					if (!locations.containsKey(locationId))
					{
						final StockroomLocationData loc = this.persistenceManager.create(StockroomLocationData.class);
						loc.setLocationId(locationId);
						if (ImmutableSet.of(LOC_PICKUP_1, LOC_PICKUP_2).contains(locationId))
						{
							loc.setLocationRoles(ImmutableSet.of(LocationRole.PICKUP.getCode()));
						}
						else
						{
							loc.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
						}
						loc.getShipToCountries().add(getOrCreateCountry("Canada", "CA"));
						loc.setPriority(sourcingLocation.getInfo().getPriority());
						final AddressVT adr = this.createAddress(locationId);
						loc.setAddress(adr);
						locations.put(locationId, loc);
					}
					// To manage the unique constraint in ItemLocation
					final BinData binData = this.persistenceManager.create(BinData.class);
					binData.setBinCode(RandomStringUtils.randomAlphabetic(3));
					binData.setPriority(1);
					binData.setStockroomLocation(locations.get(locationId));

					final ItemLocationData itemLoc = this.persistenceManager.create(ItemLocationData.class);
					itemLoc.setItemId(entry.getKey());
					itemLoc.setStockroomLocation(locations.get(locationId));
					itemLoc.setBin(binData);
					final CurrentItemQuantityData currentQuantity = this.persistenceManager.create(CurrentItemQuantityData.class);
					currentQuantity.setOwner(itemLoc);
					currentQuantity.setStatusCode(ON_HAND);
					currentQuantity.setQuantityValue(sourcingLocation.getAts());
				}
			}

			return locations;
		}

		return null;
	}

	private AddressVT createAddress(final String locationId)
	{
		final AddressVT adr;
		if (LOC_ID.equals(locationId))
		{
			adr = new AddressVT(null, null, null, null, null, LATITUDE, LONGITUDE_LOC, null, null, null, null);
		}
		else
		{
			adr = new AddressVT(null, null, null, null, null, LATITUDE, LONGITUDE_LOC_2, null, null, null, null);
		}
		return adr;
	}

	private CountryData getOrCreateCountry(final String countryName, final String countryCode)
	{
		try
		{
			return this.persistenceManager.getByIndex(CountryData.UX_COUNTRIES_CODE, countryCode);
		}
		catch (final ManagedObjectNotFoundException ex)
		{
			// Not found, let's create a new one...
		}

		final CountryData result = this.persistenceManager.create(CountryData.class);
		result.setName(countryName);
		result.setCode(countryCode);
		this.persistenceManager.flush();
		return result;
	}

	private void createOrderWithBaseStore(final List<SourcingLine> lines, final BaseStoreData baseStore,
			final Set<String> locationIds, final String country)
	{
		if (CollectionUtils.isNotEmpty(lines))
		{
			final OrderData order = this.createOrderWithBaseStore(baseStore, locationIds, country);
			for (final SourcingLine line : lines)
			{
				order.getOrderLines().add(createOrderLine(order, line));
			}
			order.getPaymentInfos().add(this.createPaymentInfo(order));
			order.setShippingAndHandling(this.createShippingAndHandling(order));
		}
	}

	private OrderData createOrderWithBaseStore(final BaseStoreData baseStore, final Set<String> locationIds, final String country)
	{
		final OrderData result = this.persistenceManager.create(OrderData.class);
		result.setOrderId(ORDER_BASESTORE_ID);
		result.setEmailid("user@test.de");
		result.setFirstName("first");
		result.setLastName("last");
		result.setBaseStore(baseStore);
		result.setStockroomLocationIds(new ArrayList<>(locationIds));
		result.setIssueDate(new Date());
		result.setShippingAddress(AddressBuilder.anAddress().withCountryCode(country).buildAddressVT());
		result.setShippingMethod("NA");
		return result;
	}

	private void createOrder(final List<SourcingLine> lines)
	{
		if (CollectionUtils.isNotEmpty(lines))
		{
			final OrderData order = this.createOrder();
			for (final SourcingLine line : lines)
			{
				final OrderLineData orderLine = this.createOrderLine(order, line);
				order.getOrderLines().add(orderLine);
			}
			order.getPaymentInfos().add(this.createPaymentInfo(order));
			order.setShippingAndHandling(this.createShippingAndHandling(order));
		}
	}

	private BaseStoreData createBaseStore()
	{
		final BaseStoreData baseStore = this.persistenceManager.create(BaseStoreData.class);
		baseStore.setName(BASE_STORE_NAME);

		return baseStore;
	}

	private OrderData createOrder()
	{
		final OrderData result = this.persistenceManager.create(OrderData.class);
		result.setOrderId(ORDER_ID);
		result.setEmailid("user@test.de");
		result.setFirstName("first");
		result.setLastName("last");
		result.setIssueDate(new Date());
		result.setShippingAddress(AddressBuilder.anAddress().buildAddressVT());
		result.setShippingMethod("NA");
		return result;
	}

	private OrderLineData createOrderLine(final OrderData order, final SourcingLine line)
	{
		final OrderLineData result = this.persistenceManager.create(OrderLineData.class);
		result.setMyOrder(order);
		result.setOrderLineId(line.getLineId());
		result.setSkuId(line.getSku());
		result.setUnitPriceCurrencyCode(EUR);
		result.setUnitPriceValue(0);
		result.setUnitTaxValue(0);
		result.setUnitTaxCurrencyCode(EUR);
		result.setQuantityValue(line.getQuantity());
		result.setQuantityUnassignedValue(line.getQuantity());
		result.setTaxCategory("NA");
		result.setLocationRoles(line.getLocationRoles());
		result.setPickupStoreId(line.getPickupLocationId());
		return result;
	}

	private PaymentInfoData createPaymentInfo(final OrderData order)
	{
		final PaymentInfoData paymentInfo = this.persistenceManager.create(PaymentInfoData.class);
		paymentInfo.setAuthUrl("test");
		paymentInfo.setPaymentInfoType("test");
		paymentInfo.setBillingAddress(AddressBuilder.anAddress().buildAddressVT());
		paymentInfo.setMyOrder(order);
		return paymentInfo;
	}

	private ShippingAndHandlingData createShippingAndHandling(final OrderData order)
	{
		final PriceVT shippingPrice = new PriceVT("EUR", 10d, null, 10d, null, 10d);

		final ShippingAndHandlingData shippingAndHandlingData = this.persistenceManager.create(ShippingAndHandlingData.class);
		shippingAndHandlingData.setOrderId(order.getOrderId());
		shippingAndHandlingData.setShippingPrice(shippingPrice);

		return shippingAndHandlingData;
	}

	private void createFormula()
	{
		atsService.createFormula(ATS_ID, "I[" + ON_HAND + "]", "name", null);
	}

}
