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
package com.hybris.oms.service.order.procs;

import com.hybris.kernel.api.HybrisId;
import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;

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
public class OrderDataProcsTest
{

	private static final Long OLQ_ID = 600L;
	private static final String ORDER_HYBRIS_ID = "single|OrderData|1";
	private static final String ORDER_HYBRIS_ID2 = "single|OrderData|2";

	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();

	@Autowired
	private ImportService importService;

	private OrderData order;

	@Autowired
	private PersistenceManager persistenceManager;

	@Before
	public void setUp()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.order = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID));
	}

	@Transactional
	@Test
	public void testGetOlqIds()
	{
		final List<Long> olqIds = OrderDataStaticUtils.getOlqIds(this.order);
		Assert.assertEquals(4, olqIds.size());
		Assert.assertEquals(OLQ_ID, olqIds.get(0));
	}

	@Transactional
	@Test
	public void testGetOrderLinesQuantities()
	{
		Assert.assertEquals(4, OrderDataStaticUtils.getOrderLineQuantities(this.order).size());
	}

	@Transactional
	@Test
	public void testHasValidShippingAddressGeocodesFalse()
	{
		this.order.setShippingAddress(this.changeLatLonValues(this.order.getShippingAddress(), 30.5, -500d));
		boolean result = OrderDataStaticUtils.hasValidShippingAddressGeocodes(this.order);
		Assert.assertFalse(result);

		this.order.setShippingAddress(this.changeLatLonValues(this.order.getShippingAddress(), null, -500d));
		result = OrderDataStaticUtils.hasValidShippingAddressGeocodes(this.order);
		Assert.assertFalse(result);

		this.order.setShippingAddress(this.changeLatLonValues(this.order.getShippingAddress(), 30.5, null));
		result = OrderDataStaticUtils.hasValidShippingAddressGeocodes(this.order);
		Assert.assertFalse(result);
	}

	@Transactional
	@Test
	public void testHasValidShippingAddressGeocodesTrue()
	{
		this.order.setShippingAddress(this.changeLatLonValues(this.order.getShippingAddress(), 45.45, 75.65));
		final boolean result = OrderDataStaticUtils.hasValidShippingAddressGeocodes(this.order);
		Assert.assertTrue(result);
	}

	private AddressVT changeLatLonValues(final AddressVT source, final Double latitudeValue, final Double longitudeValue)
	{
		return new AddressVT(source.getAddressLine1(), source.getAddressLine2(), source.getCityName(),
				source.getCountrySubentity(), source.getPostalZone(), latitudeValue, longitudeValue,
				source.getCountryIso3166Alpha2Code(), source.getCountryName(), source.getName(), source.getPhoneNumber());
	}

	@Transactional
	@Test
	public void testIsCompletelySourcedTrue()
	{
		for (final OrderLineData orderLine : this.order.getOrderLines())
		{
			orderLine.setQuantityUnassignedValue(0);
		}

		final boolean result = OrderDataStaticUtils.isCompletelySourced(this.order);
		Assert.assertTrue(result);
	}

	@Transactional
	@Test
	public void testIsCompletelySourcedFalse()
	{
		for (final OrderLineData orderLine : this.order.getOrderLines())
		{
			orderLine.setQuantityUnassignedValue(5);
		}

		final boolean result = OrderDataStaticUtils.isCompletelySourced(this.order);
		Assert.assertFalse(result);
	}

	@Transactional
	@Test
	public void shouldBeCompletelyPickupOrder()
	{
		final boolean result = OrderDataStaticUtils.isCompletelyPickup(this.order);
		Assert.assertTrue(result);
	}

	@Transactional
	@Test
	public void shouldNotBeCompletelyPickupOrder()
	{
		final OrderData orderData = this.persistenceManager.get(HybrisId.valueOf(ORDER_HYBRIS_ID2));
		final boolean result = OrderDataStaticUtils.isCompletelyPickup(orderData);
		Assert.assertFalse(result);
	}
}
