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
package com.hybris.oms.service.ats.impl;


import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.hybris.kernel.api.AggregationService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.ats.AtsResult;
import com.hybris.oms.service.ats.AtsService;
import com.hybris.oms.service.ats.DuplicateFormulaException;
import com.hybris.oms.service.builders.OrderLineQuantityBuilder;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.ats.AtsFormulaData;
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
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.util.OmsTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


/**
 * Integration test for {@link DefaultAtsService}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
@SuppressWarnings({"PMD.ExcessiveImports"})
public class DefaultAtsServiceIntegrationTest
{
	private static final String ON_HAND_FORMULA = "I[ON_HAND]";
	private static final String WEB_FORMULA = "I[ON_HAND]-O[ALLOCATED]-O[PICKED]";

	private static final String LOC2 = "loc2";
	private static final String LOC1 = "loc1";
	private static final String SKU2 = "sku2";
	private static final String SKU1 = "sku1";
	private static final String ON_HAND = "ON_HAND";
	private static final String BIN_CODE_1 = "binCode1";
	private static final Set<String> FILTER_SKUS = Sets.newHashSet(SKU1, SKU2);
	private static final Long MILLISTIME = null;

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

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

	@Test
	@Transactional
	public void shouldMaintainFormula()
	{
		this.createItemStatus();
		this.pmgr.flush();
		this.atsService.createFormula("test", ON_HAND_FORMULA, "name", "description");
		this.pmgr.flush();
		final AtsFormulaData formula = this.atsService.getFormulaById("test");
		assertThat(this.atsService.findFormulas(null)).containsOnly(formula);
		assertThat(this.atsService.findFormulas(Collections.<String>emptySet())).containsOnly(formula);
		assertThat(this.atsService.findFormulas(Collections.singleton("test"))).containsOnly(formula);
		assertThat(this.atsService.findFormulas(Collections.singleton("invalud"))).isEmpty();
		assertEquals(ON_HAND_FORMULA, formula.getFormula());
		assertEquals("name", formula.getName());
		assertEquals("description", formula.getDescription());
		this.atsService.updateFormula("test", "I[RESERVED]", "name2", "description2");
		final AtsFormulaData formula2 = this.atsService.getFormulaById("test");
		assertEquals("I[RESERVED]", formula2.getFormula());
		assertEquals("name2", formula.getName());
		assertEquals("description2", formula2.getDescription());
		this.atsService.deleteFormula("test");
		this.pmgr.flush();
		try
		{
			this.atsService.getFormulaById("test");
			fail("Formula should be deleted");
		}
		catch (@SuppressWarnings("unused") final EntityNotFoundException e)
		{
			// success
		}
	}

	@Test(expected = DuplicateFormulaException.class)
	@Transactional
	public void shouldThrowDuplicateFormula()
	{
		this.createItemStatus();
		this.pmgr.flush();
		this.atsService.createFormula("test", ON_HAND_FORMULA, "name", "description");
		this.atsService.createFormula("test", ON_HAND_FORMULA, "name", "description");
		this.pmgr.flush();
	}

	@Test
	public void shouldCalculateLocalAts()
	{
		this.init(true, false);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final AtsResult result = atsService.getLocalAts(FILTER_SKUS, null, Sets.newHashSet("f1", "f2"));
				assertEquals(Integer.valueOf(106), result.getResult(SKU1, "f1", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(41), result.getResult(SKU1, "f1", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(8), result.getResult(SKU2, "f1", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU2, "f1", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(111), result.getResult(SKU1, "f2", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(44), result.getResult(SKU1, "f2", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(10), result.getResult(SKU2, "f2", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU2, "f2", LOC2, MILLISTIME));
				assertThat(result.getResults()).hasSize(6);
				return null;
			}
				});
	}

	@Test
	public void shouldFilterLocalAts()
	{
		this.init(true, false);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final AtsResult result = atsService.getLocalAts(Collections.singleton(SKU1), Collections.singleton(LOC1),
						Collections.singleton("f1"));
				assertEquals(Integer.valueOf(106), result.getResult(SKU1, "f1", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU1, "f1", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU2, "f1", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU2, "f1", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU1, "f2", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU1, "f2", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU2, "f2", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU2, "f2", LOC2, MILLISTIME));
				assertThat(result.getResults()).hasSize(1);
				return null;
			}
				});
	}

	@Test
	public void shouldSubtractQuantitiesUnassignedGlobalAts()
	{
		this.init(true, false);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final AtsResult result = atsService.getGlobalAts(FILTER_SKUS, null);
				assertEquals(Integer.valueOf(145), result.getResult(SKU1, "f1", MILLISTIME));
				assertEquals(Integer.valueOf(7), result.getResult(SKU2, "f1", MILLISTIME));
				assertEquals(Integer.valueOf(153), result.getResult(SKU1, "f2", MILLISTIME));
				assertEquals(Integer.valueOf(9), result.getResult(SKU2, "f2", MILLISTIME));
				assertThat(result.getResults()).hasSize(4);
				return null;
			}
				});
	}

	/**
	 * Test case for OMSWSTHREE-470.
	 */
	@Test
	public void shouldSubtractQuantitiesUnassignedWithoutStockGlobalAts()
	{
		this.init(true, true, false, false);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final AtsResult result = atsService.getGlobalAts(FILTER_SKUS, null);

				assertEquals(Integer.valueOf(-7), result.getResult(SKU1, "f1", MILLISTIME));
				assertEquals(Integer.valueOf(-6), result.getResult(SKU2, "f1", MILLISTIME));
				assertEquals(Integer.valueOf(-7), result.getResult(SKU1, "f2", MILLISTIME));
				assertEquals(Integer.valueOf(-6), result.getResult(SKU2, "f2", MILLISTIME));
				assertThat(result.getResults()).hasSize(4);
				return null;
			}
				});
	}

	@Test
	public void shouldCalculateGlobalAts()
	{
		this.init(false, false);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final AtsResult result = atsService.getGlobalAts(FILTER_SKUS, null);
				assertEquals(Integer.valueOf(147), result.getResult(SKU1, "f1", MILLISTIME));
				assertEquals(Integer.valueOf(8), result.getResult(SKU2, "f1", MILLISTIME));
				assertEquals(Integer.valueOf(155), result.getResult(SKU1, "f2", MILLISTIME));
				assertEquals(Integer.valueOf(10), result.getResult(SKU2, "f2", MILLISTIME));
				assertThat(result.getResults()).hasSize(4);
				return null;
			}
				});
	}

	@Test
	public void shouldSubtractThresholdGlobalAts()
	{
		this.init(false, true);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final AtsResult result = atsService.getGlobalAts(FILTER_SKUS, Collections.singleton("f2"));
				assertEquals(Integer.valueOf(150), result.getResult(SKU1, "f2", MILLISTIME));
				assertEquals(Integer.valueOf(5), result.getResult(SKU2, "f2", MILLISTIME));
				assertThat(result.getResults()).hasSize(2);
				return null;
			}
				});
	}

	@Test(expected = EntityNotFoundException.class)
	public void shouldThrowNoSuchFormulaGlobalAts()
	{
		this.init(true, false);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				atsService.getGlobalAts(FILTER_SKUS, Collections.singleton("INVALID"));
				return null;
			}
				});
	}

	@Test
	public void shouldSubtractThresholdLocalAts()
	{
		this.init(true, true);
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallback<Integer>()
				{
			@Override
			public Integer doInTransaction(final TransactionStatus status)
			{
				final AtsResult result = atsService.getLocalAts(FILTER_SKUS, null, Sets.newHashSet("f1", "f2"));
				assertEquals(Integer.valueOf(99), result.getResult(SKU1, "f1", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(1), result.getResult(SKU2, "f1", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(104), result.getResult(SKU1, "f2", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(3), result.getResult(SKU2, "f2", LOC1, MILLISTIME));
				assertEquals(Integer.valueOf(36), result.getResult(SKU1, "f1", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU2, "f1", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(39), result.getResult(SKU1, "f2", LOC2, MILLISTIME));
				assertEquals(Integer.valueOf(0), result.getResult(SKU2, "f2", LOC2, MILLISTIME));
				assertThat(result.getResults()).hasSize(6);
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

	private void createItemQuantities(final boolean threshold, final boolean insertStock)
	{
		final StockroomLocationData loc = this.pmgr.create(StockroomLocationData.class);
		loc.setLocationId(LOC1);
		loc.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		final StockroomLocationData loc2 = this.pmgr.create(StockroomLocationData.class);
		loc2.setLocationId(LOC2);
		loc2.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		if (threshold)
		{
			loc.setAbsoluteInventoryThreshold(7);
			loc2.setPercentageInventoryThreshold(10);
			loc2.setUsePercentageThreshold(true);
		}
		final BinData bin1 = this.pmgr.create(BinData.class);
		bin1.setBinCode(BIN_CODE_1);
		bin1.setStockroomLocation(loc);
		bin1.setPriority(1);
		bin1.setDescription("Bin Description");
		final ItemLocationData il1 = createStockroomLocation(loc, bin1);
		final ItemLocationData il2 = createStockroomLocation(loc2, bin1);
		final CurrentItemQuantityData ci1 = this.pmgr.create(CurrentItemQuantityData.class);
		ci1.setOwner(il1);
		ci1.setStatusCode(ON_HAND);
		final CurrentItemQuantityData ci2 = this.pmgr.create(CurrentItemQuantityData.class);
		ci2.setOwner(il2);
		ci2.setStatusCode(ON_HAND);
		final ItemLocationData il3 = this.pmgr.create(ItemLocationData.class);
		il3.setItemId(SKU2);
		il3.setStockroomLocation(loc);
		il3.setBin(bin1);
		il3.setFuture(false);
		final CurrentItemQuantityData ci3 = this.pmgr.create(CurrentItemQuantityData.class);
		ci3.setOwner(il3);
		ci3.setStatusCode(ON_HAND);
		if (insertStock)
		{
			ci1.setQuantityValue(111);
			ci2.setQuantityValue(44);
			ci3.setQuantityValue(10);
		}
		else
		{
			ci1.setQuantityValue(0);
			ci2.setQuantityValue(0);
			ci3.setQuantityValue(0);
		}
	}

	/**
	 * @param loc
	 * @param bin1
	 * @return
	 */
	private ItemLocationData createStockroomLocation(final StockroomLocationData loc, final BinData bin1)
	{
		final ItemLocationData il1 = this.pmgr.create(ItemLocationData.class);
		il1.setItemId(SKU1);
		il1.setStockroomLocation(loc);
		il1.setBin(bin1);
		il1.setFuture(false);
		return il1;
	}

	private void createOrder(final boolean quantityUnassigned, final boolean olqs)
	{
		final OrderLineQuantityStatusData st1 = this.pmgr.create(OrderLineQuantityStatusData.class);
		st1.setStatusCode("ALLOCATED");
		final OrderLineQuantityStatusData st2 = this.pmgr.create(OrderLineQuantityStatusData.class);
		st2.setStatusCode("PICKED");

		final OrderData order1 = this.createOrder("o1");
		final ShippingAndHandlingData shippingAndHandling = this.createShippingAndHandling(order1);
		final OrderLineData ol1 = this.createOrderLine("ol1", order1, 10, quantityUnassigned ? 2 : 0, SKU1);
		final OrderLineData ol2 = this.createOrderLine("ol2", order1, 10, quantityUnassigned ? 1 : 0, SKU2);

		order1.setShippingAndHandling(shippingAndHandling);

		final List<OrderLineData> orderLines = new ArrayList<OrderLineData>();
		orderLines.add(ol1);
		orderLines.add(ol2);

		order1.setOrderLines(orderLines);

		final PaymentInfoData paymentInfo = this.createPaymentInfo(order1);
		final List<PaymentInfoData> paymentInfos = new ArrayList<PaymentInfoData>();
		paymentInfos.add(paymentInfo);
		order1.setPaymentInfos(paymentInfos);
		if (olqs)
		{
			createOrderLineQuantities(st1, st2, ol1, ol2);
		}
	}

	private OrderData createOrder(final String id)
	{
		final OrderData result = this.pmgr.create(OrderData.class);
		result.setOrderId(id);
		result.setEmailid("user");
		result.setFirstName("first");
		result.setLastName("last");
		result.setIssueDate(new Date());
		result.setShippingAddress(AddressBuilder.anAddress().buildAddressVT());
		result.setShippingMethod("NA");
		return result;
	}

	private OrderLineData createOrderLine(final String orderLineId, final OrderData order, final int quantity,
			final int qtyUnassigned, final String sku)
	{
		final OrderLineData result = this.pmgr.create(OrderLineData.class);
		result.setMyOrder(order);
		result.setOrderLineId(orderLineId);
		result.setSkuId(sku);
		result.setUnitPriceCurrencyCode("EUR");
		result.setUnitPriceValue(0);
		result.setUnitTaxValue(0);
		result.setUnitTaxCurrencyCode("EUR");
		result.setQuantityValue(quantity);
		result.setQuantityUnassignedValue(qtyUnassigned);
		result.setTaxCategory("NA");
		result.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		return result;
	}

	private void createOrderLineQuantities(final OrderLineQuantityStatusData st1, final OrderLineQuantityStatusData st2,
			final OrderLineData ol1, final OrderLineData ol2)
	{
		final OrderLineQuantityData olq1 = this.pmgr.create(OrderLineQuantityData.class);
		olq1.setOlqId(1);
		olq1.setOrderLine(ol1);
		OrderLineQuantityBuilder.setQuantity(olq1, new QuantityVT("", 5));
		olq1.setStatus(st1);
		olq1.setStockroomLocationId(LOC1);
		olq1.setQuantityValue(5);
		final OrderLineQuantityData olq2 = this.pmgr.create(OrderLineQuantityData.class);
		olq2.setOlqId(2);
		olq2.setOrderLine(ol1);
		olq2.setStatus(st2);
		olq2.setStockroomLocationId(LOC2);
		olq2.setQuantityValue(3);
		final OrderLineQuantityData olq3 = this.pmgr.create(OrderLineQuantityData.class);
		olq3.setOlqId(3);
		olq3.setOrderLine(ol2);
		olq3.setQuantityValue(2);
		olq3.setStatus(st1);
		olq3.setStockroomLocationId(LOC1);
	}

	private PaymentInfoData createPaymentInfo(final OrderData order)
	{
		final PaymentInfoData paymentInfo = this.pmgr.create(PaymentInfoData.class);
		paymentInfo.setAuthUrl("test");
		paymentInfo.setPaymentInfoType("test");
		paymentInfo.setBillingAddress(AddressBuilder.anAddress().buildAddressVT());
		paymentInfo.setMyOrder(order);

		return paymentInfo;
	}

	private ShippingAndHandlingData createShippingAndHandling(final OrderData order)
	{
		final PriceVT shippingPrice = new PriceVT("EUR", 10d, null, 10d, null, 10d);

		final ShippingAndHandlingData shippingAndHandlingData = this.pmgr.create(ShippingAndHandlingData.class);
		shippingAndHandlingData.setOrderId(order.getOrderId());
		shippingAndHandlingData.setShippingPrice(shippingPrice);

		return shippingAndHandlingData;
	}

	private void createTenantPreference()
	{
		final TenantPreferenceData tp1 = this.pmgr.create(TenantPreferenceData.class);
		tp1.setType(TenantPreferenceConstants.PREF_TYPE_ATS_GLOBAL_THRHOLD);
		tp1.setProperty(TenantPreferenceConstants.PREF_KEY_ATS_GLOBAL_THRHOLD);
		tp1.setValue("5");
	}

	private void createFormulas(final boolean threshold)
	{
		final String suffix = threshold ? "-T" : "";
		atsService.createFormula("f1", WEB_FORMULA + suffix, "name", null);
		atsService.createFormula("f2", ON_HAND_FORMULA + suffix, "name", null);
	}

	private void init(final boolean quantityUnassigned, final boolean threshold)
	{
		init(quantityUnassigned, threshold, true, true);
	}

	private void init(final boolean quantityUnassigned, final boolean threshold, final boolean insertStock, final boolean olqs)
	{
		new TransactionTemplate(this.transactionManager).execute(new TransactionCallbackWithoutResult()
		{

			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus arg0)
			{
				createFormulas(threshold);
				createTenantPreference();
				pmgr.flush();

				createItemStatus();
				createItemQuantities(threshold, insertStock);
				createOrder(quantityUnassigned, olqs);
				pmgr.flush();
			}
		});
	}

}
