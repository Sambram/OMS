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

import static com.hybris.oms.service.workflow.WorkflowConstants.STATE_QUEUED;
import static junit.framework.Assert.assertTrue;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.api.exceptions.ManagedObjectNotFoundException;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.order.OrderService;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.util.OmsTestUtils;
import com.hybris.oms.service.workflow.executor.shipment.ShipmentWorkflowExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-service-spring-test.xml"})
public class DefaultShipmentServiceIntegrationTest
{
	private static final String NEW_STATE = "NEW_STATE";

	public static final String DESCRIPTION = "OTHER GOODS";

	private static final String HYBRIS_ID_CONFIRMED_SHIPMENT = "single|ShipmentData|6";

	private static final String HYBRIS_ID_OLQ_STATUS = "single|OrderLineQuantityStatusData|13";

	private static final String HYBRIS_ID_SHIPMENT = "single|ShipmentData|1";

	private static final String ORDER_ID = "23";

	private static final String ORDER_HYBRIS_ID = "single|OrderData|2";

	private static final String ORDER_HYBRIS_ID_NEW_SHIPMENT = "single|OrderData|3";

	private static final String ORDER_HID_NEW_SHIPMENT_W_PPSTRE_ID = "single|OrderData|4";

	private static final String ORDER_HID_NEW_SHIPMNT_WO_PPSTRE_ID = "single|OrderData|5";

	private static final String LOCATION_ID = "single|StockroomLocationData|202";

	private static final Long SHIPMENT_ID = 999L;

	private static final Long SHIPMENT_ID_INVALID = 100L;

	private static final String INVALID = "INVALID";

	private static final String DEFAULT_GROSS_WEIGHT_UNIT_CODE = "KG";

	private static final Long DEFAULT_GROSS_WEIGHT_VALUE = 15L;

	private static final String DEFAULT_HEIGH_UNIT_CODE = "cm";

	private static final Long DEFAULT_HEIGH_VALUE = 1L;

	private static final Long DEFAULT_INSURANCE_VALUE = 100L;

	private static final Long DEFAULT_LENGHT_VALUE = 1L;

	private static final Long DEFAULT_WIDHT_VALUE = 1L;

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Autowired
	private ImportService importService;

	@Autowired
	private JdbcPersistenceEngine persistenceEngine;

	@Autowired
	private PlatformTransactionManager txManager;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PersistenceManager persistenceManager;

	@Autowired
	private DefaultShipmentService shipmentService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ShipmentWorkflowExecutor shipmentWorkflowExecutor;

	@Before
	public void setUp()
	{

		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import-standalone.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService
				.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

	@Test
	@Transactional
	public void shouldThrowExceptionWhileCreatingShipmentsForAlreadyAssignedOLQs()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		final List<OrderLineQuantityData> orderLineQuantityDatas = new ArrayList<>();
		final OrderLineData ol = order.getOrderLines().get(0);
		final OrderLineQuantityData olq = ol.getOrderLineQuantities().get(0);
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		olq.setShipment(shipment);

		for (final OrderLineData orderLineData : order.getOrderLines())
		{
			for (final OrderLineQuantityData orderLineQuantityData : orderLineData.getOrderLineQuantities())
			{
				orderLineQuantityDatas.add(orderLineQuantityData);
			}
		}

		persistenceManager.flush();

		try
		{
			this.shipmentService.createShipmentsByOLQs(orderLineQuantityDatas);
			Assert.fail("One or more ols assigned to shipment already");
		}
		catch (final IllegalArgumentException exception)
		{
			// Do Nothing
		}

	}

	@Test
	@Transactional
	public void shouldDeleteDeliveryWhenShipmentIsDeleted()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		Assert.assertNotNull(shipment);
		Assert.assertNotNull(shipment.getDelivery());

		this.shipmentService.deleteShipment(shipment);

		try
		{
			this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
			Assert.fail("Shipment was supposed to be deleted.");
		}
		catch (final ManagedObjectNotFoundException e)
		{
			// Shipment was deleted
		}

		try
		{
			this.persistenceManager.get(shipment.getDelivery().getId());
			Assert.fail("Delivery was supposed to be cascade deleted with parent shipment.");
		}
		catch (final ManagedObjectNotFoundException e)
		{
			// Delivery was deleted
		}
	}

	@Test
	@Transactional
	public void createShipmentsByOLQs()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		final List<OrderLineQuantityData> orderLineQuantityDatas = new ArrayList<>();
		for (final OrderLineData orderLineData : order.getOrderLines())
		{
			for (final OrderLineQuantityData orderLineQuantityData : orderLineData.getOrderLineQuantities())
			{
				orderLineQuantityDatas.add(orderLineQuantityData);
			}
		}

		final List<ShipmentData> shipments = this.shipmentService.createShipmentsByOLQs(orderLineQuantityDatas);
		Assert.assertEquals(2, shipments.size());
		Assert.assertFalse(StringUtils.isEmpty(shipments.get(0).getShippingMethod()));
		Assert.assertFalse(StringUtils.isEmpty(shipments.get(0).getTaxCategory()));
		Assert.assertTrue(shipments.get(0).getMerchandisePrice().getSubTotalCurrencyCode() != null);
		Assert.assertTrue(shipments.get(0).getMerchandisePrice().getTaxCurrencyCode() != null);

		// Verify that shipment process has been created
		persistenceManager.flush();
	}

	@Test
	@Transactional
	public void testCreateShipments()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));

		final List<ShipmentData> shipments = this.shipmentService.createShipmentsForOrder(order);
		Assert.assertEquals(2, shipments.size());
		Assert.assertFalse(StringUtils.isEmpty(shipments.get(0).getShippingMethod()));
		Assert.assertFalse(StringUtils.isEmpty(shipments.get(0).getTaxCategory()));

		final Long insurance = (long) shipments.get(0).getInsuranceValueAmountValue();
		final Long widhtValue = (long) shipments.get(0).getWidthValue();
		final Long lengthValue = (long) shipments.get(0).getLengthValue();
		final Long heightValue = (long) shipments.get(0).getHeightValue();
		final Long grossWeightValue = (long) shipments.get(0).getGrossWeightValue();

		Assert.assertEquals(insurance, DEFAULT_INSURANCE_VALUE);
		Assert.assertEquals(widhtValue, DEFAULT_WIDHT_VALUE);
		Assert.assertEquals(lengthValue, DEFAULT_LENGHT_VALUE);
		Assert.assertEquals(heightValue, DEFAULT_HEIGH_VALUE);
		Assert.assertEquals(shipments.get(0).getHeightUnitCode(), DEFAULT_HEIGH_UNIT_CODE);
		Assert.assertEquals(grossWeightValue, DEFAULT_GROSS_WEIGHT_VALUE);
		Assert.assertEquals(shipments.get(0).getGrossWeightUnitCode(), DEFAULT_GROSS_WEIGHT_UNIT_CODE);
		Assert.assertEquals(shipments.get(0).getPackageDescription(), DESCRIPTION);

		// Verify that shipment process has been created
		persistenceManager.flush();

		for (final ShipmentData shipment : shipments)
		{
			Assert.assertEquals(STATE_QUEUED, shipment.getState());
		}
	}

	// case 2: Order has order lines with pickup store id and without
	@Test
	@Transactional
	public void testCreateShipmentsWithAndWithouPickupOrderId()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HID_NEW_SHIPMNT_WO_PPSTRE_ID));

		final List<ShipmentData> shipments = this.shipmentService.createShipmentsForOrder(order);
		Assert.assertEquals(3, shipments.size());
		Assert.assertFalse(StringUtils.isEmpty(shipments.get(0).getShippingMethod()));
		Assert.assertFalse(StringUtils.isEmpty(shipments.get(0).getTaxCategory()));
		Assert.assertEquals(OrderDataStaticUtils.getOlqIdsForShipment(shipments.get(0)).size(), 1);
		Assert.assertEquals(OrderDataStaticUtils.getOlqIdsForShipment(shipments.get(1)).size(), 1);

		final Long insurance = (long) shipments.get(0).getInsuranceValueAmountValue();
		final Long widhtValue = (long) shipments.get(0).getWidthValue();
		final Long lengthValue = (long) shipments.get(0).getLengthValue();
		final Long heightValue = (long) shipments.get(0).getHeightValue();
		final Long grossWeightValue = (long) shipments.get(0).getGrossWeightValue();

		Assert.assertEquals(insurance, DEFAULT_INSURANCE_VALUE);
		Assert.assertEquals(widhtValue, DEFAULT_WIDHT_VALUE);
		Assert.assertEquals(lengthValue, DEFAULT_LENGHT_VALUE);
		Assert.assertEquals(heightValue, DEFAULT_HEIGH_VALUE);
		Assert.assertEquals(shipments.get(0).getHeightUnitCode(), DEFAULT_HEIGH_UNIT_CODE);
		Assert.assertEquals(grossWeightValue, DEFAULT_GROSS_WEIGHT_VALUE);
		Assert.assertEquals(shipments.get(0).getGrossWeightUnitCode(), DEFAULT_GROSS_WEIGHT_UNIT_CODE);
		Assert.assertEquals(shipments.get(0).getPackageDescription(), DESCRIPTION);
	}

	// case 1. Order has all order lines with pickup store id
	@Test
	@Transactional
	public void testCreateShipmentsWithPickupOrderId()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HID_NEW_SHIPMENT_W_PPSTRE_ID));

		final List<ShipmentData> shipments = this.shipmentService.createShipmentsForOrder(order);

		final Long insurance = (long) shipments.get(0).getInsuranceValueAmountValue();
		final Long widhtValue = (long) shipments.get(0).getWidthValue();
		final Long lengthValue = (long) shipments.get(0).getLengthValue();
		final Long heightValue = (long) shipments.get(0).getHeightValue();
		final Long grossWeightValue = (long) shipments.get(0).getGrossWeightValue();

		Assert.assertEquals(2, shipments.size());
		Assert.assertFalse(StringUtils.isEmpty(shipments.get(0).getShippingMethod()));
		Assert.assertFalse(StringUtils.isEmpty(shipments.get(0).getTaxCategory()));

		Assert.assertEquals(OrderDataStaticUtils.getOlqIdsForShipment(shipments.get(0)).size(), 1);
		Assert.assertEquals(OrderDataStaticUtils.getOlqIdsForShipment(shipments.get(1)).size(), 2);

		Assert.assertEquals(insurance, DEFAULT_INSURANCE_VALUE);
		Assert.assertEquals(widhtValue, DEFAULT_WIDHT_VALUE);
		Assert.assertEquals(lengthValue, DEFAULT_LENGHT_VALUE);
		Assert.assertEquals(heightValue, DEFAULT_HEIGH_VALUE);
		Assert.assertEquals(shipments.get(0).getHeightUnitCode(), DEFAULT_HEIGH_UNIT_CODE);
		Assert.assertEquals(grossWeightValue, DEFAULT_GROSS_WEIGHT_VALUE);
		Assert.assertEquals(shipments.get(0).getGrossWeightUnitCode(), DEFAULT_GROSS_WEIGHT_UNIT_CODE);
		Assert.assertEquals(shipments.get(0).getPackageDescription(), DESCRIPTION);
	}

	@Test(expected = ManagedObjectNotFoundException.class)
	@Transactional
	public void testDeleteShipment()
	{
		final ShipmentData shipmentData = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		Assert.assertNotNull(shipmentData);
		this.shipmentService.deleteShipment(shipmentData);
		this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
	}

	@Test
	@Transactional
	public void testFindAllShipmentsByOrder()
	{
		final OrderData orderData = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
		final List<ShipmentData> shipments = this.shipmentService.findAllShipmentsByOrderId(orderData.getOrderId());
		Assert.assertEquals(1, shipments.size());
	}

	@Test
	@Transactional
	public void testFindShipmentsByOrder()
	{
		final OrderData orderData = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
		final List<ShipmentData> shipments = this.shipmentService.findShipmentsByOrder(orderData);
		Assert.assertEquals(1, shipments.size());
	}

	@Test
	@Transactional
	public void testGenerateShipmentId()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
		final ShipmentData shipment = this.persistenceManager.create(ShipmentData.class);
		shipment.setOrderFk(order);
		shipment.setShippingMethod("test");
		this.persistenceManager.flush();
		assertTrue(shipment.getShipmentId() > 0L);
	}

	@Test
	@Transactional
	public void testGetShipmentById()
	{
		final ShipmentData shipmentData = this.shipmentService.getShipmentById(SHIPMENT_ID);
		Assert.assertEquals(SHIPMENT_ID.longValue(), shipmentData.getShipmentId());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetShipmentByIdNotFound()
	{
		this.shipmentService.getShipmentById(SHIPMENT_ID_INVALID);
	}

	@Test
	@Transactional
	public void testIsShipmentConfirmedFalse()
	{
		final ShipmentData confirmedShipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		this.shipmentService.checkIfShipmentWorkflowAlreadyStarted(confirmedShipment);
	}

	@Test
	@Transactional
	public void testIsShipmentConfirmedTrue()
	{
		final ShipmentData confirmedShipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_CONFIRMED_SHIPMENT));
		this.shipmentService.checkIfShipmentWorkflowAlreadyStarted(confirmedShipment);
	}

	/**
	 * Test to make sure that the kernel properly saves value types.
	 */
	@Test
	@Transactional
	public void testSaveValueType()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		Assert.assertEquals(1100d, shipment.getMerchandisePrice().getTaxCommittedValue(), 0.001d);
		Assert.assertEquals("CAD", shipment.getMerchandisePrice().getTaxCommittedCurrencyCode());
		final PriceVT merchandisePrice = this.changeTaxCommitedForPrice(shipment.getMerchandisePrice(), new AmountVT("USD", 50d));
		shipment.setMerchandisePrice(merchandisePrice);
		this.persistenceManager.flush();
		this.persistenceManager.detachAll();
		final ShipmentData shipment2 = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		Assert.assertEquals(50d, shipment2.getMerchandisePrice().getTaxCommittedValue(), 0.001d);
		Assert.assertEquals("USD", shipment2.getMerchandisePrice().getTaxCommittedCurrencyCode());
	}

	private PriceVT changeTaxCommitedForPrice(final PriceVT source, final AmountVT taxCommited)
	{
		return new PriceVT(source.getSubTotalCurrencyCode(), source.getSubTotalValue(), source.getTaxCurrencyCode(),
				source.getTaxValue(), taxCommited.getCurrencyCode(), taxCommited.getValue());
	}

	@Test
	@Transactional
	public void testShipmentGetOrderFk()
	{
		final ShipmentData shipment = this.persistenceManager.create(ShipmentData.class);
		shipment.setOrderFk((OrderData) this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID)));
		Assert.assertNotNull(shipment.getOrderFk());

		final ShipmentData shipment2 = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		Assert.assertNotNull(shipment2.getOrderFk());

	}

	@Test
	@Transactional
	public void testUpdateShipmentStatus()
	{
		final ShipmentData shipmentData = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_SHIPMENT));
		final OrderLineQuantityStatusData olqStatus = this.persistenceManager.get(HybrisId.valueOf(HYBRIS_ID_OLQ_STATUS));


		final List<Long> olqIds = OrderDataStaticUtils.getOlqIdsForShipment(shipmentData);
		List<OrderLineQuantityData> olqs = new ArrayList<OrderLineQuantityData>();

		if (olqIds != null && !olqIds.isEmpty())
		{
			olqs = this.orderService.getOrderLineQuantitiesByOlqIds(olqIds);
		}

		Assert.assertNotSame(olqs.get(0).getStatus().getStatusCode(), "PICKED");

		this.shipmentService.updateShipmentStatus(shipmentData, olqStatus);
		Assert.assertEquals(olqs.get(0).getStatus().getStatusCode(), "PICKED");
	}

	@Test
	@Transactional
	public void findAllShipmentsByOrderId()
	{
		final List<ShipmentData> shipments = this.shipmentService.findAllShipmentsByOrderId(ORDER_ID);
		Assert.assertEquals(1, shipments.size());
	}

	@Test
	@Transactional
	public void findAllShipmentsByOrderIdWithInvalidOrderId()
	{
		final List<ShipmentData> shipments = this.shipmentService.findAllShipmentsByOrderId(INVALID);
		Assert.assertTrue(shipments.isEmpty());
	}

	@Test
	@Transactional
	public void testReallocateOlqsToNewShipmentsAndDeleteInitialShipment()
	{
		final ShipmentData shipment = this.persistenceManager.get(HybrisId.valueOf("ShipmentData|1", "single"));
		final Map<Long, String> reallocMap = new HashMap<>();
		reallocMap.put(Long.valueOf(1), "2");
		this.shipmentService.reallocateOlqsToNewShipmentsAndDeleteInitialShipment(reallocMap, shipment);
		try
		{
			this.persistenceManager.get(HybrisId.valueOf("ShipmentData|1", "single"));
			Assert.fail();
		}
		catch (final ManagedObjectNotFoundException e)
		{
			// shipment was deleted
		}
	}

	@Test
	@Transactional
	public void shouldReallocateShipment()
	{
		final StockroomLocationData location = this.persistenceManager.get(HybrisId.valueOf(LOCATION_ID));
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		final List<ShipmentData> shipments = this.shipmentService.createShipmentsByOLQs(OrderDataStaticUtils
				.getOrderLineQuantities(order));

		final ShipmentData shipment1 = shipments.get(0);
		shipmentService.updateShipmentStatus(shipment1, TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PICKED);
		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment1);
		for (final OrderLineQuantityData olq : olqs)
		{
			Assert.assertFalse(location.getLocationId().equals(olq.getStockroomLocationId()));
			Assert.assertFalse("ALLOCATED".equals(olq.getStatus().getStatusCode()));
		}
		this.shipmentService.reallocateShipment(shipment1, location);

		for (final OrderLineQuantityData olq : olqs)
		{
			Assert.assertEquals(location.getLocationId(), olq.getStockroomLocationId());
			Assert.assertEquals("ALLOCATED", olq.getStatus().getStatusCode());
		}
	}

	@Test(expected = IllegalStateException.class)
	@Transactional
	public void shouldFailReallocateShipmentStateChanged()
	{
		final StockroomLocationData location = this.persistenceManager.get(HybrisId.valueOf(LOCATION_ID));
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		final List<ShipmentData> shipments = this.shipmentService.createShipmentsByOLQs(OrderDataStaticUtils
				.getOrderLineQuantities(order));

		final ShipmentData shipment1 = shipments.get(0);
		shipment1.setState(NEW_STATE);
		this.shipmentService.reallocateShipment(shipment1, location);
	}

	@Test
	@Transactional
	public void shouldSplitShipmentByOlqs()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		final List<ShipmentData> shipments = this.shipmentService.createShipmentsByOLQs(OrderDataStaticUtils
				.getOrderLineQuantities(order));
		final ShipmentData originalShipment = shipments.get(0);
		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(originalShipment);

		Assert.assertTrue(2 == olqs.size());

		Assert.assertTrue(3 == originalShipment.getTotalGoodsItemQuantityValue());
		Assert.assertTrue(30.0d == originalShipment.getMerchandisePrice().getSubTotalValue().doubleValue());
		Assert.assertTrue(6.0d == originalShipment.getMerchandisePrice().getTaxValue().doubleValue());

		final OrderLineQuantityData olqToRemove = olqs.get(0);
		final ShipmentData newShipment = this.shipmentService.splitShipmentByOlqs(originalShipment,
				Collections.singletonList(olqToRemove));
		final List<OrderLineQuantityData> olqsAfterSplit = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(originalShipment);
		final List<OrderLineQuantityData> newShipmentOlqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(newShipment);

		Assert.assertTrue(1 == olqsAfterSplit.size());
		Assert.assertTrue(1 == newShipmentOlqs.size());

		Assert.assertTrue(originalShipment.getOlqsStatus() == newShipment.getOlqsStatus());
		Assert.assertTrue(originalShipment.getOrderFk() == newShipment.getOrderFk());
		Assert.assertTrue(originalShipment.getShipFrom() == newShipment.getShipFrom());

		Assert.assertTrue(1 == originalShipment.getTotalGoodsItemQuantityValue());
		Assert.assertTrue(10.0d == originalShipment.getMerchandisePrice().getSubTotalValue().doubleValue());
		Assert.assertTrue(2.0d == originalShipment.getMerchandisePrice().getTaxValue().doubleValue());

		Assert.assertTrue(olqToRemove == newShipmentOlqs.get(0));
		Assert.assertTrue(olqToRemove.getOrderLine() == newShipmentOlqs.get(0).getOrderLine());
		Assert.assertTrue(olqToRemove.getStatus() == newShipmentOlqs.get(0).getStatus());
		Assert.assertTrue(olqToRemove.getStockroomLocationId().equals(newShipmentOlqs.get(0).getStockroomLocationId()));
	}

	@Test
	@Transactional
	public void shouldSplitShipmentByOlqQuantities()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		final List<ShipmentData> shipments = this.shipmentService.createShipmentsByOLQs(OrderDataStaticUtils
				.getOrderLineQuantities(order));
		final ShipmentData originalShipment = shipments.get(0);
		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(originalShipment);
		final OrderLineQuantityData olqToRemoveQuantityFrom = olqs.get(0);
		final Quantity quantityToRemove = new Quantity();
		quantityToRemove.setValue(1);

		Assert.assertTrue(2 == olqs.size());
		Assert.assertTrue(2 == olqToRemoveQuantityFrom.getQuantityValue());

		Assert.assertTrue(3 == originalShipment.getTotalGoodsItemQuantityValue());
		Assert.assertTrue(30.0d == originalShipment.getMerchandisePrice().getSubTotalValue().doubleValue());
		Assert.assertTrue(6.0d == originalShipment.getMerchandisePrice().getTaxValue().doubleValue());

		final Map<OrderLineQuantityData, Quantity> quantitiesMap = new HashMap<>();
		quantitiesMap.put(olqToRemoveQuantityFrom, quantityToRemove);
		final ShipmentData newShipment = this.shipmentService.splitShipmentByOlqQuantities(originalShipment, quantitiesMap);
		final List<OrderLineQuantityData> olqsAfterSplit = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(originalShipment);
		final List<OrderLineQuantityData> newShipmentOlqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(newShipment);

		Assert.assertTrue(2 == olqsAfterSplit.size());
		Assert.assertTrue(1 == olqToRemoveQuantityFrom.getQuantityValue());
		Assert.assertTrue(1 == newShipmentOlqs.size());

		Assert.assertTrue(originalShipment.getOlqsStatus() == newShipment.getOlqsStatus());
		Assert.assertTrue(originalShipment.getOrderFk() == newShipment.getOrderFk());
		Assert.assertTrue(originalShipment.getShipFrom() == newShipment.getShipFrom());

		Assert.assertTrue(2 == originalShipment.getTotalGoodsItemQuantityValue());
		Assert.assertTrue(20.0d == originalShipment.getMerchandisePrice().getSubTotalValue().doubleValue());
		Assert.assertTrue(4.0d == originalShipment.getMerchandisePrice().getTaxValue().doubleValue());

		Assert.assertTrue(olqToRemoveQuantityFrom != newShipmentOlqs.get(0));
		Assert.assertTrue(olqToRemoveQuantityFrom.getOrderLine() == newShipmentOlqs.get(0).getOrderLine());
		Assert.assertTrue(olqToRemoveQuantityFrom.getStatus() == newShipmentOlqs.get(0).getStatus());
		Assert.assertTrue(olqToRemoveQuantityFrom.getStockroomLocationId().equals(newShipmentOlqs.get(0).getStockroomLocationId()));
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void shouldFailSplitShipmentByOlqNotEnoughQuantity()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		final List<ShipmentData> shipments = this.shipmentService.createShipmentsByOLQs(OrderDataStaticUtils
				.getOrderLineQuantities(order));
		final ShipmentData originalShipment = shipments.get(0);
		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(originalShipment);
		final OrderLineQuantityData olqToRemoveQuantityFrom = olqs.get(0);
		final Quantity quantityToRemove = new Quantity();
		quantityToRemove.setValue(10);

		final Map<OrderLineQuantityData, Quantity> quantitiesMap = new HashMap<>();
		quantitiesMap.put(olqToRemoveQuantityFrom, quantityToRemove);
		this.shipmentService.splitShipmentByOlqQuantities(originalShipment, quantitiesMap);
	}
}
