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
package com.hybris.oms.facade.conversion.impl.returns;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.facade.conversion.impl.order.OrderTestUtils;
import com.hybris.oms.service.managedobjects.returns.ReturnData;

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
public class ReturnConverterTest
{

	@Autowired
	private Converter<ReturnData, Return> returnConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private ReturnTestUtils returnTestUtils;

	@Before
	public void setUp()
	{
		this.returnTestUtils = new ReturnTestUtils();
		this.returnTestUtils.createObjects(this.persistenceManager);
	}

	@Transactional
	@Test
	public void convertingFlushedReturnData()
	{
		this.persistenceManager.flush();
		// when
		final Return aReturn = this.returnConverter.convert(returnTestUtils.getReturnData());
		// then
		validateReturn(aReturn);
		Assert.assertEquals(ReturnTestUtils.SKU_ID, aReturn.getReturnOrderLines().get(0).getOrderLine().getSkuId());
	}

	@Transactional
	@Test
	public void convertingNotFlushedReturnData()
	{
		// when
		final Return aReturn = this.returnConverter.convert(returnTestUtils.getReturnData());

		// then
		validateReturn(aReturn);
	}

	private void validateReturn(final Return aReturn)
	{
		Assert.assertNotNull(aReturn.getReturnOrderLines());
		Assert.assertEquals(1, aReturn.getReturnOrderLines().size());
		Assert.assertNotNull(aReturn.getReturnPaymentInfos());
		Assert.assertNotNull(aReturn.getReturnPaymentInfos());
		Assert.assertEquals(OrderTestUtils.ORDER_ID, aReturn.getOrderId());
		Assert.assertEquals(String.valueOf(ReturnTestUtils.RETURN_ID), aReturn.getReturnId());
		Assert.assertEquals(ReturnTestUtils.LOCATION_ID, aReturn.getReturnLocationId());
		Assert.assertNotNull(aReturn.getReturnShipment());
		Assert.assertEquals(Long.valueOf(ReturnTestUtils.RETURN_SHIPMENT_ID),
				Long.valueOf(aReturn.getReturnShipment().getReturnShipmentId()));
	}
}
