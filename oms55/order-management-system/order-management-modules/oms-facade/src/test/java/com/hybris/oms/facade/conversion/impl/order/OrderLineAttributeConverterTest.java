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
package com.hybris.oms.facade.conversion.impl.order;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OrderLineAttributeConverterTest
{

	@Autowired
	private Converter<OrderLineAttributeData, OrderLineAttribute> orderLineAttributeConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private OrderTestUtils orderTestUtils;
	private OrderLineAttributeData orderLineAttributeData;

	@Before
	public void setUp()
	{
		// given
		this.orderTestUtils = new OrderTestUtils();
		this.orderTestUtils.createObjects(this.persistenceManager);
		this.orderLineAttributeData = this.prepareData();
	}

	@Transactional
	@Test
	public void convertingNotFlushedOrderLineQuantityData()
	{
		// when
		final OrderLineAttribute orderLineAttribute = this.orderLineAttributeConverter.convert(this.orderLineAttributeData);

		// then
		assertValid(orderLineAttribute);
	}

	@Transactional
	@Test
	public void convertingFlushedOrderLineAttributeData()
	{
		this.persistenceManager.flush();

		// when
		final OrderLineAttribute orderLineQuantity = this.orderLineAttributeConverter.convert(this.orderLineAttributeData);

		// then
		assertValid(orderLineQuantity);
	}

	private OrderLineAttributeData prepareData()
	{
		final OrderLineData orderLineData = this.orderTestUtils.getOrderLineData();

		final OrderLineAttributeData ola = this.persistenceManager.create(OrderLineAttributeData.class);
		ola.setDescription("description");
		ola.setAttributeId("attributeId");
		ola.setOrderLine(orderLineData);




		return ola;
	}

	private void assertValid(final OrderLineAttribute orderLineAttribute)
	{
		Assert.assertEquals("description", orderLineAttribute.getDescription());
		Assert.assertEquals("attributeId", orderLineAttribute.getId());
	}

}
