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
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.shipment.ShipmentService;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultShipmentServiceTest
{
	public static final String STATUS_PACKED = "PACKED";
	public static final String STATUS_PICKED = "PICKED";
	public static final String STATUS_PAYMENT_CAPTURED = "PAYMENT_CAPTURED";
	public static final String STATUS_TAX_INVOICED = "TAX_INVOICED";
	public static final String AMOUNT_CAPTURED_CURRENCY = "USD";
	public static final double AMOUNT_CAPTURED_VALUE = 7.5;

	private static final String ORDER_HYBRIS_ID = "single|OrderData|5";
	private static final String SHIPMENT_HYBRIS_ID = "single|ShipmentData|5";
	private static final String SHIPMENT_WITH_OLQS_HYBRIS_ID = "single|ShipmentData|1";

	@Autowired
	private ShipmentService shipmentService;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private ImportService importService;

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/inventory/test-inventory-data-import.mcsv")[0]);
		this.importService
		.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void testCreateShipmentsByOrderId()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
		final List<ShipmentData> listOfShipment = this.shipmentService.createShipmentsForOrder(order);
		Assert.assertNotNull(listOfShipment);
		Assert.assertEquals(3, listOfShipment.size());
	}

	@Test
	@Transactional
	public void testCapturePayment()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(SHIPMENT_HYBRIS_ID));
		this.shipmentService.capturePayment(shipment);

		Assert.assertEquals(shipment.getAmountCapturedCurrencyCode(), AMOUNT_CAPTURED_CURRENCY);
		Assert.assertTrue(shipment.getAmountCapturedValue() == AMOUNT_CAPTURED_VALUE);
		Assert.assertEquals(shipment.getOlqsStatus(), STATUS_PAYMENT_CAPTURED);
	}

	@Test
	@Transactional
	public void testInvoiceTaxes()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(SHIPMENT_WITH_OLQS_HYBRIS_ID));
		this.shipmentService.invoiceTaxes(shipment);

		Assert.assertTrue(shipment.getMerchandisePrice().getSubTotalValue() == 1000.0d);
		Assert.assertTrue(shipment.getShippingAndHandling().getShippingPrice().getSubTotalValue() == 4.5d);
		Assert.assertEquals(shipment.getOlqsStatus(), STATUS_TAX_INVOICED);
	}

	@Test
	@Transactional
	public void shouldComputeZeroAmountsWhenShipmentHasNoOlqs()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(SHIPMENT_WITH_OLQS_HYBRIS_ID));
		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment);

		Assert.assertTrue(1000.0d == shipment.getMerchandisePrice().getSubTotalValue().doubleValue());
		Assert.assertTrue(1100.0d == shipment.getMerchandisePrice().getTaxCommittedValue().doubleValue());
		Assert.assertTrue(100.0d == shipment.getMerchandisePrice().getTaxValue().doubleValue());

		for (final OrderLineQuantityData olq : olqs)
		{
			olq.setShipment(null);
		}
		this.shipmentService.computeShipmentPrices(shipment);

		Assert.assertTrue(0.0d == shipment.getMerchandisePrice().getSubTotalValue().doubleValue());
		Assert.assertTrue(0.0d == shipment.getMerchandisePrice().getTaxCommittedValue().doubleValue());
		Assert.assertTrue(0.0d == shipment.getMerchandisePrice().getTaxValue().doubleValue());
	}

	@Test
	@Transactional
	public void shouldComputeShipmentPricesWhenShipmentHasOlqs()
	{
		final ShipmentData shipmentWithOlqs = this.persistenceManager.get(HybrisId.valueOf(SHIPMENT_WITH_OLQS_HYBRIS_ID));
		final PriceVT initialShipmentMerchandisePrice = shipmentWithOlqs.getMerchandisePrice();
		final int initialTotalGoodsValue = shipmentWithOlqs.getTotalGoodsItemQuantityValue();

		Assert.assertTrue(1000.0d == initialShipmentMerchandisePrice.getSubTotalValue().doubleValue());
		Assert.assertTrue(100.0d == initialShipmentMerchandisePrice.getTaxValue().doubleValue());
		Assert.assertTrue(10 == initialTotalGoodsValue);

		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipmentWithOlqs);
		olqs.get(0).setShipment(null);
		this.shipmentService.computeShipmentPrices(shipmentWithOlqs);
		final PriceVT finalShipmentMerchandisePrice = shipmentWithOlqs.getMerchandisePrice();
		final int finalTotalGoodsValue = shipmentWithOlqs.getTotalGoodsItemQuantityValue();

		Assert.assertTrue(30.0d == finalShipmentMerchandisePrice.getSubTotalValue().doubleValue());
		Assert.assertTrue(0.0d == finalShipmentMerchandisePrice.getTaxValue().doubleValue());
		Assert.assertTrue(3 == finalTotalGoodsValue);
	}

	@Test
	@Transactional
	public void shouldDeleteShipment()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(SHIPMENT_HYBRIS_ID));

		this.shipmentService.deleteShipment(shipment);
		this.persistenceManager.flush();

		try
		{
			this.shipmentService.getShipmentById(shipment.getShipmentId());
			Assert.fail("Shipment should have been deleted.");
		}
		catch (final EntityNotFoundException e)
		{
			// Shipment was deleted
		}
	}

}
