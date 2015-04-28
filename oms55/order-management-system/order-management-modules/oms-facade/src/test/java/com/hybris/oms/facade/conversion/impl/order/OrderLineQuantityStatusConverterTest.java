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
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OrderLineQuantityStatusConverterTest
{
	@Autowired
	private Converter<OrderLineQuantityStatusData, OrderLineQuantityStatus> orderLineQuantityStatusConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void convertingNotFlushedOrderLineQuantityStatusData()
	{
		// given
		final OrderLineQuantityStatusData orderLineQuantityStatusData = this.persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData.setActive(Boolean.TRUE);
		orderLineQuantityStatusData.setDescription("description");
		orderLineQuantityStatusData.setStatusCode("statusCode");

		// when
		final OrderLineQuantityStatus orderLineQuantityStatus = this.orderLineQuantityStatusConverter
				.convert(orderLineQuantityStatusData);

		// then
		Assert.assertEquals(true, orderLineQuantityStatus.getActive());
		Assert.assertEquals("description", orderLineQuantityStatus.getDescription());
		Assert.assertEquals("statusCode", orderLineQuantityStatus.getStatusCode());
	}

	@Transactional
	@Test
	public void convertingFlushedOrderLineQuantityStatusData()
	{
		// given
		final OrderLineQuantityStatusData orderLineQuantityStatusData = this.persistenceManager
				.create(OrderLineQuantityStatusData.class);
		orderLineQuantityStatusData.setActive(Boolean.TRUE);
		orderLineQuantityStatusData.setDescription("description");
		orderLineQuantityStatusData.setStatusCode("statusCode");

		this.persistenceManager.flush();

		// when
		final OrderLineQuantityStatus orderLineQuantityStatus = this.orderLineQuantityStatusConverter
				.convert(orderLineQuantityStatusData);

		// then
		Assert.assertEquals(true, orderLineQuantityStatus.getActive());
		Assert.assertEquals("description", orderLineQuantityStatus.getDescription());
		Assert.assertEquals("statusCode", orderLineQuantityStatus.getStatusCode());
	}
}
