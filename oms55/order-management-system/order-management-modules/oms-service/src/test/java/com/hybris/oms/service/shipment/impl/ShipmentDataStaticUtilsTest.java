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
package com.hybris.oms.service.shipment.impl;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AmountVT;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class ShipmentDataStaticUtilsTest
{

	private static final String FIRST_SHIPMENT_HYBRIS_ID = "single|ShipmentData|5";
	private static final String OLQ_HYBRIS_ID_1 = "single|OrderLineQuantityData|6";
	private static final String OLQ_HYBRIS_ID_2 = "single|OrderLineQuantityData|7";
	private static final String SHIPMENT_HYBRIS_ID = "single|ShipmentData|6";
	private static final String BOPIS_SHIPMENT_HYBRIS_ID = "single|ShipmentData|13";

	private List<OrderLineQuantityData> emptyList;
	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	@Autowired
	private ImportService importService;

	private List<OrderLineQuantityData> olqs;

	@Autowired
	private PersistenceManager pmgr;

	@Test
	@Transactional
	public void calculateShippingTotalAmount()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(SHIPMENT_HYBRIS_ID));
		final AmountVT amount = ShipmentDataStaticUtils.calculateTotalShippingAmount(shipment);
		Assert.assertEquals(5d, amount.getValue(), 0.001d);
	}

	@Test
	@Transactional
	public void calculateShippingTotalAmountFirstShipment()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(FIRST_SHIPMENT_HYBRIS_ID));
		final AmountVT amount = ShipmentDataStaticUtils.calculateTotalShippingAmount(shipment);
		Assert.assertEquals(7.5d, amount.getValue(), 0.001d);
	}

	@Test
	@Transactional
	public void calculateShippingTotalAmountFirstShipmentWithPickuStoreId()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(FIRST_SHIPMENT_HYBRIS_ID));
		shipment.setPickupInStore(true);
		final AmountVT amount = ShipmentDataStaticUtils.calculateTotalShippingAmount(shipment);
		Assert.assertEquals(5d, amount.getValue(), 0.001d);
	}

	@Test
	@Transactional
	public void calculateShippingTotalAmountWithPickupStoreId()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(SHIPMENT_HYBRIS_ID));
		shipment.setPickupInStore(true);
		final AmountVT amount = ShipmentDataStaticUtils.calculateTotalShippingAmount(shipment);
		Assert.assertEquals(5d, amount.getValue(), 0.001d);
	}

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		final OrderLineQuantityData olq1 = this.pmgr.get(HybrisId.valueOf(OLQ_HYBRIS_ID_1));
		final OrderLineQuantityData olq2 = this.pmgr.get(HybrisId.valueOf(OLQ_HYBRIS_ID_2));
		this.olqs = Lists.newArrayList(olq1, olq2);
		this.emptyList = new ArrayList<>();
	}

	@Test
	@Transactional
	public void shouldCalculateShipmentSubtotalAmount()
	{
		final AmountVT amount = ShipmentDataStaticUtils.calculateShipmentSubtotalAmount(this.olqs);
		Assert.assertEquals(70d, amount.getValue(), 0.001d);
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void shipmentSubtotaShouldThrowExceptionWhenNoOlq()
	{
		ShipmentDataStaticUtils.calculateShipmentSubtotalAmount(this.emptyList);
	}

	@Test
	@Transactional
	public void shouldCalculateShipmentTaxAmount()
	{
		final AmountVT amount = ShipmentDataStaticUtils.calculateShipmentTaxAmount(this.olqs);
		Assert.assertEquals(7d, amount.getValue(), 0.001d);
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void shipmentTaxShouldThrowExceptionWhenNoOlq()
	{
		ShipmentDataStaticUtils.calculateShipmentTaxAmount(this.emptyList);
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void calculateShipmentSubtotalAmountShouldThrowExceptionWhenOlqListIsNull()
	{
		ShipmentDataStaticUtils.calculateShipmentSubtotalAmount(null);
	}

	@Test(expected = IllegalArgumentException.class)
	@Transactional
	public void calculateTotalGoodsItemQuantityShouldThrowExceptionWhenNoOlq()
	{
		ShipmentDataStaticUtils.calculateTotalGoodsItemQuantity(this.emptyList);
	}

	@Test
	@Transactional
	public void shouldNotBeABopisShipment()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(SHIPMENT_HYBRIS_ID));
		Assert.assertFalse(shipment.isPickupInStore());
	}

	@Test
	@Transactional
	public void shouldBeABopisShipment()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(BOPIS_SHIPMENT_HYBRIS_ID));
		Assert.assertTrue(shipment.isPickupInStore());
	}

	@Test
	@Transactional
	public void shouldHaveOLQsForShipment()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(BOPIS_SHIPMENT_HYBRIS_ID));
		Assert.assertTrue(ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment).size() > 0);
	}

	@Test
	@Transactional
	public void shouldNotHaveOLQsForShipment()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(SHIPMENT_HYBRIS_ID));
		Assert.assertEquals(0, ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment).size());
	}

	@Test
	@Transactional
	public void olqShouldNotBelongToShipment()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(BOPIS_SHIPMENT_HYBRIS_ID));
		Assert.assertFalse(ShipmentDataStaticUtils.checkOlqsBelongToShipment(shipment, this.olqs));
	}

	@Test
	@Transactional
	public void olqShouldBelongToShipment()
	{
		final ShipmentData shipment = this.pmgr.get(HybrisId.valueOf(BOPIS_SHIPMENT_HYBRIS_ID));
		final List<OrderLineQuantityData> shipmentOlqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment);
		Assert.assertTrue(ShipmentDataStaticUtils.checkOlqsBelongToShipment(shipment, shipmentOlqs));
	}

}
