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
package com.hybris.oms.service.order.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.Page;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.order.OrderQueryObject;
import com.hybris.oms.domain.preference.TenantPreferenceConstants;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.order.*;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.order.business.builders.OrderLineBuilder;
import com.hybris.oms.service.shipment.impl.DefaultShipmentService;
import com.hybris.oms.service.shipment.impl.ShipmentDataStaticUtils;
import com.hybris.oms.service.util.OmsTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-service-spring-test.xml")
@SuppressWarnings({"PMD.ExcessiveImports"})
public class DefaultOrderServiceIntegrationTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultOrderServiceIntegrationTest.class);
	private static final String INVALID = "INVALID";
	private static final long INVALID_LONG = 9999999999L;
	private static final long OLQ_ID_1 = 100;
	private static final long OLQ_ID_2 = 200;
	private static final String ORDER_HYBRIS_ID = "single|OrderData|1";
	private static final String ORDER_HYBRIS_ID_NEW_SHIPMENT = "single|OrderData|3";
	private static final String ORDER_ID = "12";
	private static final String STATUS_CODE = "OPEN";

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	@Autowired
	private final DefaultOrderService serviceUnderTest = new DefaultOrderService();
	@Autowired
	private final DefaultShipmentService shipmentService = new DefaultShipmentService();
	@Autowired
	private ImportService importService;
	@Autowired
	private PersistenceManager persistenceManager;

	@Test
	@Transactional
	public void getOrderLineQuantityStatusByTenantPreferenceKey()
	{
		final OrderLineQuantityStatusData olqStatus = this.serviceUnderTest
				.getOrderLineQuantityStatusByTenantPreferenceKey(TenantPreferenceConstants.PREF_KEY_OLQSTATUS_PICKED);
		Assert.assertNotNull(olqStatus);
	}

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import-standalone.mcsv")[0]);
		this.importService
				.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void testCreateOrderWithJapaneseChar() throws UnsupportedEncodingException
	{
		final OrderData order = this.persistenceManager.create(OrderData.class);
		order.setOrderId(UUID.randomUUID().toString());


		final byte[] expectedBytes = "アメリカ合衆国".getBytes();
		final String japaneseString = "アメリカ合衆国";
		order.setUsername(new String(expectedBytes));
		order.setFirstName(new String(expectedBytes));
		order.setLastName(japaneseString);
		order.setEmailid("test@test.de");

		this.completeOrder(order);

		this.serviceUnderTest.flush();
		this.persistenceManager.refresh(order);
		Assert.assertNotNull(order.getId());
		final byte[] userNameBytes = order.getUsername().getBytes();
		final byte[] firstNameBytes = order.getFirstName().getBytes();
		final byte[] lastNameBytes = order.getLastName().getBytes();
		for (int i = 0; i < expectedBytes.length; i++)
		{
			LOG.info("Expected Byte: {}; User: {}; First: {}; Last: {}", new Object[]{expectedBytes[i], userNameBytes[i],
					firstNameBytes[i], lastNameBytes[i]});
			Assert.assertEquals(expectedBytes[i], userNameBytes[i]);
			Assert.assertEquals(expectedBytes[i], firstNameBytes[i]);
			Assert.assertEquals(expectedBytes[i], lastNameBytes[i]);
		}
	}

	private void completeOrder(final OrderData order)
	{
		order.setIssueDate(Calendar.getInstance().getTime());
		order.setShippingAddress(new AddressVT("test", "test", "test", "test", "test", 0d, 0d, "test", "test", null, null));
		order.setShippingMethod("test");

		final OrderLineData orderLine = this.persistenceManager.create(OrderLineData.class);
		orderLine.setMyOrder(order);
		orderLine.setOrderLineId(UUID.randomUUID().toString());
		orderLine.setSkuId("test");
		OrderLineBuilder.setQuantitiesAndUnits(orderLine, "test", 0);
		orderLine.setTaxCategory("test");
		orderLine.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));
		order.setOrderLines(Lists.newArrayList(orderLine));

		final ShippingAndHandlingData shippingAndHandlingData = this.persistenceManager.create(ShippingAndHandlingData.class);
		shippingAndHandlingData.setOrderId(order.getOrderId());
		shippingAndHandlingData.setShippingPrice(new PriceVT("test", 0d, "test", 0d, "test", 0d));
		order.setShippingAndHandling(shippingAndHandlingData);


		final PaymentInfoData paymentInfo = this.persistenceManager.create(PaymentInfoData.class);
		paymentInfo.setBillingAddress(new AddressVT("test", "test", "test", "test", "test", 0d, 0d, "test", "test", null, null));
		paymentInfo.setMyOrder(order);
		paymentInfo.setPaymentInfoType("test");
		paymentInfo.setAuthUrl("test");
		order.setPaymentInfos(Lists.newArrayList(paymentInfo));
	}

	/**
	 * Test for DefaultOrderService.getAllOrderLineQuantityStatuses.
	 */
	@Test
	@Transactional
	public void testGetAllOrderLineQuantityStatuses()
	{
		final Collection<OrderLineQuantityStatusData> allStatuses = this.serviceUnderTest.getAllOrderLineQuantityStatuses();
		Assert.assertEquals(19, allStatuses.size());
	}

	/**
	 * Test for existences of orderline attributes.
	 */
	@Test
	@Transactional
	public void orderLineHasAttributes()
	{
		final OrderLineData orderLine = this.serviceUnderTest.getOrderLineByOlqId(OLQ_ID_1);
		final List<OrderLineAttributeData> orderLineAttributeDatas = orderLine.getOrderLineAttributes();
		Assert.assertEquals(3, orderLineAttributeDatas.size());
	}

	@Test
	@Transactional
	public void testGetOrderByOlqId()
	{
		final OrderData order = this.serviceUnderTest.getOrderByOlqId(OLQ_ID_1);
		Assert.assertNotNull(order);
	}

	@Test
	@Transactional
	public void testGetOrderLineByOlqId()
	{
		final OrderLineData orderLine = this.serviceUnderTest.getOrderLineByOlqId(OLQ_ID_1);
		Assert.assertNotNull(orderLine);
	}

	/**
	 * Test for DefaultOrderService.getOrderLineQuantitiesByOlqIds.
	 */
	@Test
	@Transactional
	public void testGetOrderLineQuantitiesByOlqIds()
	{
		final Collection<OrderLineQuantityData> result = this.serviceUnderTest.getOrderLineQuantitiesByOlqIds(Arrays.asList(
				OLQ_ID_1, OLQ_ID_2));
		Assert.assertEquals(2, result.size());
	}

	/**
	 * Test for DefaultOrderService.getOrderLineQuantitiesByShipment.
	 */
	@Test
	@Transactional
	public void testGetOrderLineQuantitiesByShipment()
	{
		final ShipmentData shipment = this.persistenceManager.getByIndex(ShipmentData.UX_SHIPMENTS_SHIPMENTID, 998L);
		final Collection<OrderLineQuantityData> result = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment);
		Assert.assertEquals(3, result.size());
	}

	/**
	 * Test for DefaultOrderService.getOrderLineQuantityByOlqId.
	 */
	@Test
	@Transactional
	public void testGetOrderLineQuantityByOlqId()
	{
		final OrderLineQuantityData result = this.serviceUnderTest.getOrderLineQuantityByOlqId(OLQ_ID_1);
		Assert.assertEquals(OLQ_ID_1, result.getOlqId());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetOrderLineQuantityByOlqIdNotFound()
	{
		this.serviceUnderTest.getOrderLineQuantityByOlqId(INVALID_LONG);
	}

	/**
	 * Test for DefaultOrderService.getOrderLineQuantityStatusByStatusCode.
	 */
	@Test
	@Transactional
	public void testGetOrderLineQuantityStatusByStatusCode()
	{
		final OrderLineQuantityStatusData result = this.serviceUnderTest.getOrderLineQuantityStatusByStatusCode(STATUS_CODE);
		Assert.assertEquals(STATUS_CODE, result.getStatusCode());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetOrderLineQuantityStatusByStatusCodeNotFound()
	{
		this.serviceUnderTest.getOrderLineQuantityStatusByStatusCode(INVALID);
	}

	/**
	 * Test for DefaultOrderService.getOrderWithOrderId.
	 */
	@Test
	@Transactional
	public void testGetOrderWithOrderId()
	{
		final OrderData result = this.serviceUnderTest.getOrderByOrderId(ORDER_ID);
		Assert.assertEquals(ORDER_ID, result.getOrderId());
	}

	@Test(expected = EntityNotFoundException.class)
	@Transactional
	public void testGetOrderWithOrderIdNotFound()
	{
		this.serviceUnderTest.getOrderByOrderId(INVALID);
	}

	@Test
	@Transactional
	public void testGetOrderLinesByShipment()
	{
		final ShipmentData shipment = this.persistenceManager.getByIndex(ShipmentData.UX_SHIPMENTS_SHIPMENTID, 998L);
		final List<OrderLineData> result = this.serviceUnderTest.getOrderLinesByShipment(shipment);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals("23", result.get(0).getMyOrder().getOrderId());
		Assert.assertEquals("3", result.get(0).getOrderLineId());
		Assert.assertEquals(7, result.get(0).getQuantityValue());
		Assert.assertEquals(10.0d, result.get(0).getUnitPriceValue(), 0.001d);
	}

	@Test
	@Transactional
	public void testGetOrderLinesByShipmentNoOrderLines()
	{
		final ShipmentData shipment = this.persistenceManager.getByIndex(ShipmentData.UX_SHIPMENTS_SHIPMENTID, 1004L);
		final List<OrderLineData> result = this.serviceUnderTest.getOrderLinesByShipment(shipment);
		Assert.assertEquals(0, result.size());
	}
	
	@Test
	@Transactional
	public void shouldNotFindOrdersUpdatedAfterWhenDateIsAfter()
	{
		final OrderData orderData = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
		orderData.setState("dummy");
		this.persistenceManager.flush();
		OmsTestUtils.delay();
		
		final Date now = new Date();
		Assert.assertTrue(now.getTime() > orderData.getModifiedTime().getTime());

		Page<OrderData> orders = this.serviceUnderTest.findOrdersUpdatedAfter(now);
		Assert.assertTrue(orders.getContent().isEmpty());
	}
	
	@Test
	@Transactional
	public void shouldFindOrdersUpdatedAfter()
	{
		final OrderData orderData1 = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		orderData1.setState("dummy");
		this.persistenceManager.flush();
		OmsTestUtils.delay();
		
		final Date now = new Date();
		Assert.assertTrue(now.getTime() >= orderData1.getModifiedTime().getTime());
		
		final OrderData orderData2 = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
		orderData2.setState("dummy");
		this.persistenceManager.flush();
		Assert.assertTrue(now.getTime() <= orderData2.getModifiedTime().getTime());

		Page<OrderData> orders = this.serviceUnderTest.findOrdersUpdatedAfter(now);

		Assert.assertEquals(1, orders.getContent().size());
	}

	@Test
	@Transactional
	public void shouldFindOrdersUpdatedAfterOrderNShipmentUpdates()
	{
		final OrderData orderData1 = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		orderData1.setState("dummy");
		this.persistenceManager.flush();
		OmsTestUtils.delay();

		final Date now = new Date();
		Assert.assertTrue(now.getTime() >= orderData1.getModifiedTime().getTime());

		Page<OrderData> orders = this.serviceUnderTest.findOrdersUpdatedAfter(now);
		Assert.assertEquals(0, orders.getContent().size());

		final OrderData orderData2 = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
		final List<ShipmentData> shipment1 = this.shipmentService.findAllShipmentsByOrderId(orderData1.getOrderId());
		orderData2.setState("dummy");
		shipment1.get(0).setOlqsStatus("ALLOCATED");
		this.persistenceManager.flush();

		Assert.assertTrue(now.getTime() <= orderData2.getModifiedTime().getTime());

		final Page<ShipmentData> shipments = this.shipmentService.findAllShipmentsUpdatedAfter(now);
		final Set<String> orderIdsFromShipments = new HashSet<>();
		for (final ShipmentData shipment : shipments.getContent())
		{
			orderIdsFromShipments.add(shipment.getOrderFk().getOrderId());
		}
		orders = this.serviceUnderTest.findOrdersUpdatedAfter(now);
		Assert.assertEquals(2, orders.getContent().size() + orderIdsFromShipments.size());
	}
	
	@Test
	@Transactional
	public void shouldFindOrdersByShipmentStatus()
	{
		final OrderQueryObject queryObject = new OrderQueryObject();
		queryObject.setShipmentStatusIds(Lists.newArrayList("ALLOCATED", "PICKED", "PACKED", "CANCELLED"));
		
		final Page<OrderData> pagedResult = this.serviceUnderTest.findPagedOrdersByQuery(queryObject);

		Assert.assertEquals(2, pagedResult.getContent().size());
	}
	
	@Test
	@Transactional
	public void shouldNotFindOrdersWhenInvalidShipmentStatus()
	{
		final OrderQueryObject queryObject = new OrderQueryObject();
		queryObject.setShipmentStatusIds(Lists.newArrayList("INVALID"));
		
		final Page<OrderData> pagedResult = this.serviceUnderTest.findPagedOrdersByQuery(queryObject);

		Assert.assertEquals(0, pagedResult.getContent().size());
	}
	
	@Test
	@Transactional
	public void shouldFindOrdersByShipmentStockroomLocationId()
	{
		final OrderQueryObject queryObject = new OrderQueryObject();
		queryObject.setLocationIds(Lists.newArrayList("1","2"));
		
		final Page<OrderData> pagedResult = this.serviceUnderTest.findPagedOrdersByQuery(queryObject);

		Assert.assertEquals(3, pagedResult.getContent().size());
	}
	
	@Test
	@Transactional
	public void shouldNotFindOrdersWhenInvalidShipmentStockroomLocationId()
	{
		final OrderQueryObject queryObject = new OrderQueryObject();
		queryObject.setLocationIds(Lists.newArrayList("-1"));
		
		final Page<OrderData> pagedResult = this.serviceUnderTest.findPagedOrdersByQuery(queryObject);

		Assert.assertEquals(0, pagedResult.getContent().size());
	}
	
	@Test
	@Transactional
	public void shouldDeleteOrderLineQuantities()
	{
		final OrderData order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID_NEW_SHIPMENT));
		final List<OrderLineQuantityData> olqsToBeDeleted = OrderDataStaticUtils.getOrderLineQuantities(order); 
		Assert.assertEquals(3, olqsToBeDeleted.size());
		
		this.serviceUnderTest.deleteOrderLineQuantities(olqsToBeDeleted);
		this.persistenceManager.flush();
		
		Assert.assertEquals(0, this.serviceUnderTest.getOrderLineQuantitiesByOlqIds(OrderDataStaticUtils.getOlqIds(order)).size());
	}
}
