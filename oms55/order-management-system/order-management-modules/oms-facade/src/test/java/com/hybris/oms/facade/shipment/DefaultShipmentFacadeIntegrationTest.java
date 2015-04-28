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
package com.hybris.oms.facade.shipment;

import static org.junit.Assert.assertEquals;

import com.hybris.kernel.api.ImportService;
import com.hybris.kernel.utils.ClasspathScanningResourceFetcher;
import com.hybris.kernel.utils.ResourceFetcher;
import com.hybris.oms.api.Pageable;
import com.hybris.oms.api.shipment.ShipmentFacade;
import com.hybris.oms.domain.SortDirection;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.domain.exception.EntityValidationException;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShipmentQueryObject;
import com.hybris.oms.domain.shipping.ShipmentQuerySupport;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/oms-facade-spring-test.xml")
public class DefaultShipmentFacadeIntegrationTest
{
	private static final String OMS141 = "OMS141";
	private static final int FIVE_HUNDRED = 500;
	private static final int ONE_THOUSAND = 1000;
	private final ResourceFetcher fetcher = new ClasspathScanningResourceFetcher();
	@Autowired
	private ImportService importService;
	@Autowired
	private ShipmentFacade shipmentFacade;

	@Before
	public void setUpMcsvResources()
	{
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/order/test-order-data-import.mcsv")[0]);
		this.importService.loadMcsvResource(this.fetcher.fetchResources("/shipment/test-shipment-data-import-standalone.mcsv")[0]);
		this.importService
				.loadMcsvResource(this.fetcher.fetchResources("/tenantPreference/test-tenantPreference-data-import.mcsv")[0]);
	}

	@Test
	@Transactional
	public void testFindShipmentsByQuery()
	{
		// given
		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		queryObject.setPageNumber(0);
		queryObject.setPageSize(2);
		queryObject.setOrderIds(Lists.newArrayList(OMS141));
		queryObject.setPreOrder(true);
		queryObject.setSorting(ShipmentQuerySupport.SHIPMENT_ID, SortDirection.ASC);

		// Should return page 1 of 2 with shipments 1004 + 1005
		Pageable<Shipment> pageable = this.shipmentFacade.findShipmentsByQuery(queryObject);
		List<Shipment> result = pageable.getResults();

		assertEquals(Integer.valueOf(2), pageable.getTotalPages());
		assertEquals(Long.valueOf(4), pageable.getTotalRecords());
		assertEquals(2, result.size());
		assertEquals("1004", result.get(0).getShipmentId());
		assertEquals("1005", result.get(1).getShipmentId());

		queryObject.setPageNumber(1);

		// Should return page 2 of 2 with shipments 1006 + 1007
		pageable = this.shipmentFacade.findShipmentsByQuery(queryObject);
		result = pageable.getResults();

		assertEquals(Integer.valueOf(2), pageable.getTotalPages());
		assertEquals(Long.valueOf(4), pageable.getTotalRecords());
		assertEquals(2, result.size());
		assertEquals("1006", result.get(0).getShipmentId());
		assertEquals("1007", result.get(1).getShipmentId());
	}


	@Test
	@Transactional
	public void testIncludePreOrderShipmentsByQuery()
	{
		// given
		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		queryObject.setOrderIds(Lists.newArrayList(OMS141));
		queryObject.setPreOrder(true);
		queryObject.setSorting(ShipmentQuerySupport.SHIPMENT_ID, SortDirection.ASC);

		// Should return shipments 1004, 1005, 1006 & 1007
		final Pageable<Shipment> pageable = this.shipmentFacade.findShipmentsByQuery(queryObject);
		final List<Shipment> result = pageable.getResults();

		assertEquals(Integer.valueOf(1), pageable.getTotalPages());
		assertEquals(Long.valueOf(4), pageable.getTotalRecords());
		assertEquals(4, result.size());
		assertEquals("1004", result.get(0).getShipmentId());
		assertEquals("1005", result.get(1).getShipmentId());
		assertEquals("1006", result.get(2).getShipmentId());
		assertEquals("1007", result.get(3).getShipmentId());
	}

	@Test
	@Transactional
	public void testExcludePreOrderShipmentsByQuery()
	{
		// given
		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		queryObject.setOrderIds(Lists.newArrayList(OMS141));
		queryObject.setPreOrder(false);
		queryObject.setSorting(ShipmentQuerySupport.SHIPMENT_ID, SortDirection.ASC);

		// Should return shipments 1004 & 1007
		final Pageable<Shipment> pageable = this.shipmentFacade.findShipmentsByQuery(queryObject);
		final List<Shipment> result = pageable.getResults();

		assertEquals(Integer.valueOf(1), pageable.getTotalPages());
		assertEquals(Long.valueOf(2), pageable.getTotalRecords());
		assertEquals(2, result.size());
		assertEquals("1004", result.get(0).getShipmentId());
		assertEquals("1007", result.get(1).getShipmentId());
	}


	@Test
	@Transactional
	public void testFindShipmentsByQueryWithNullPageParams()
	{
		// Null Params
		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		queryObject.setPageNumber(null);
		queryObject.setPageSize(null);
		queryObject.setPreOrder(true);
		queryObject.setOrderIds(Lists.newArrayList(OMS141));

		// Should return 1 page with all 4 shipments
		final Pageable<Shipment> pageable = this.shipmentFacade.findShipmentsByQuery(queryObject);
		final List<Shipment> result = pageable.getResults();
		assertEquals(Integer.valueOf(1), pageable.getTotalPages());
		assertEquals(Long.valueOf(4), pageable.getTotalRecords());
		assertEquals(4, result.size());
	}

	@Test(expected = EntityValidationException.class)
	@Transactional
	public void testFindShipmentsByQueryWithInvalidParams()
	{
		// Invalid Params
		final ShipmentQueryObject queryObject = new ShipmentQueryObject();
		queryObject.setPageNumber(-5);
		queryObject.setPageSize(FIVE_HUNDRED * ONE_THOUSAND);
		queryObject.setOrderIds(Lists.newArrayList(OMS141));

		// Should throw validation exception
		this.shipmentFacade.findShipmentsByQuery(queryObject);
	}

	@Transactional
	@Test
	public void shouldSuccessfullyGetShipmentsByOrderId()
	{
		final Collection<Shipment> shipments = this.shipmentFacade.getShipmentsByOrderId(OMS141);
		assertEquals(shipments.size(), 4);
		for (final Shipment shipment : shipments)
		{
			assertEquals(shipment.getOrderId(), OMS141);
		}
	}

	@Transactional
	@Test(expected = EntityNotFoundException.class)
	public void shouldFailGetShipmentsByOrderIdWhenOrderDoesNotExist()
	{
		this.shipmentFacade.getShipmentsByOrderId("bad id");
	}

}
