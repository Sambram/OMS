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
package com.hybris.oms.ui.facade.conversion.impl.shipment;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.kernel.engine.jdbc.impl.JdbcPersistenceEngine;
import com.hybris.oms.service.managedobjects.inventory.LocationRole;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.util.OmsTestUtils;
import com.hybris.oms.service.util.ShipmentTestUtils;
import com.hybris.oms.ui.api.shipment.UIShipment;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-ui-facade-spring-test.xml"})
public class UIShipmentConverterTest
{
	@Autowired
	private Converter<ShipmentData, UIShipment> uiShipmentConverter;
	@Autowired
	private PersistenceManager persistenceManager;
	@Resource
	private JdbcPersistenceEngine persistenceEngine;
	private ShipmentData shipmentData;

	@Before
	public void setUp()
	{
		final StockroomLocationData stockroomLocationData = persistenceManager.create(StockroomLocationData.class);
		stockroomLocationData.setLocationId(ShipmentTestUtils.LOCATION_ID);
		stockroomLocationData.setStoreName(ShipmentTestUtils.LOCATION_NAME);
		stockroomLocationData.setLocationRoles(ImmutableSet.of(LocationRole.SHIPPING.getCode()));

		this.shipmentData = ShipmentTestUtils.createShipmentData(persistenceManager);

		persistenceManager.flush();
	}

	@Transactional
	@Test
	public void testXmlMarshalling() throws JAXBException
	{
		final JAXBContext jaxbContext = JAXBContext.newInstance(UIShipment.class);
		final Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this.uiShipmentConverter.convert(this.shipmentData), System.out);
	}

	@Transactional
	@Test
	public void convertingShipmentData()
	{
		final UIShipment UIShipment = this.uiShipmentConverter.convert(this.shipmentData);
		assertValid(UIShipment);
	}

	private void assertValid(final UIShipment UIShipment)
	{
		Assert.assertEquals(UIShipment.getOrderId(), ShipmentTestUtils.ORDER_ID);
		Assert.assertEquals(UIShipment.getShipmentId(), (Long) ShipmentTestUtils.SHIPMENT_ID);
		Assert.assertEquals(UIShipment.getFirstName(), ShipmentTestUtils.FIRST_NAME);
		Assert.assertEquals(UIShipment.getLastName(), ShipmentTestUtils.LAST_NAME);
		Assert.assertEquals(UIShipment.getLocationName(), ShipmentTestUtils.LOCATION_NAME);
		Assert.assertEquals(UIShipment.getShippingMethod(), ShipmentTestUtils.SHIPPING_METHOD);
		Assert.assertEquals(UIShipment.getStatus(), ShipmentTestUtils.OLQS_STATUS);
		Assert.assertEquals(UIShipment.getOrderDate(), ShipmentTestUtils.ISSUE_DATE);
	}

	@After
	public void tearDown()
	{
		OmsTestUtils.cleanUp(persistenceEngine);
	}

}
