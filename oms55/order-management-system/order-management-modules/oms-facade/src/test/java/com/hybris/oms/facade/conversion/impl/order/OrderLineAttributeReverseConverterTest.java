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
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class OrderLineAttributeReverseConverterTest
{
	private static final String ATTRIBUTE_ID = "attributeId";
	private static final String LOCATION_ID = "attribute_description";

	@Autowired
	private Converter<OrderLineAttribute, OrderLineAttributeData> orderLineAttributeReverseConverter;

	@Transactional
	@Test
	public void reverseConvertingNotFlushedOrderLineQuantityData()
	{
		// given
		final OrderLineAttribute orderLineAttribute = new OrderLineAttribute();
		orderLineAttribute.setDescription(LOCATION_ID);
		orderLineAttribute.setId(ATTRIBUTE_ID);

		// when
		final OrderLineAttributeData orderLineAttributeData = this.orderLineAttributeReverseConverter.convert(orderLineAttribute);

		// then
		Assert.assertTrue(orderLineAttributeData.getId() == null);
		Assert.assertEquals(ATTRIBUTE_ID, orderLineAttributeData.getAttributeId());
		Assert.assertEquals(LOCATION_ID, orderLineAttributeData.getDescription());
	}
}
