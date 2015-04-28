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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hybris.commons.conversion.Converter;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.returns.Return;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.facade.conversion.impl.order.OrderTestUtils;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.returns.ReturnData;
import com.hybris.oms.service.order.OrderService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class ReturnReverseConverterTest
{
	private static final String SKU_ID = "dummySkuId";

	@Autowired
	private Converter<Return, ReturnData> returnReverseConverter;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PersistenceManager persistenceManager;


	@Before
	public void setUp()
	{
		// make sure that there is an order in the system for our Return to link to
		final OrderTestUtils orderUtil = new OrderTestUtils();
		orderUtil.createObjects(persistenceManager);
		persistenceManager.flush();
	}

	@Transactional
	@Test
	public void reverseConvertingNotFlushedReturnData()
	{
		// given
		final Return aReturn = ReturnTestUtils.createOnlineReturnDto();
		aReturn.getReturnOrderLines().get(0).getOrderLine().setSkuId(SKU_ID);
		aReturn.setOrderId(OrderTestUtils.ORDER_ID);
		// when
		final ReturnData returnData = this.returnReverseConverter.convert(aReturn);
		// then
		AssertValid(returnData);
	}

	@Transactional
	@Test
	public void reverseConvertingFlushedReturnData()
	{
		// given
		final Return aReturn = ReturnTestUtils.createOnlineReturnDto();
		aReturn.getReturnOrderLines().get(0).getOrderLine().setSkuId(SKU_ID);
		aReturn.setOrderId(OrderTestUtils.ORDER_ID);
		persistenceManager.flush();
		// when
		final ReturnData returnData = this.returnReverseConverter.convert(aReturn);

		AssertValid(returnData);
	}

	private void AssertValid(final ReturnData returnData)
	{
		assertTrue(returnData.getId() == null);
		assertEquals(ReturnTestUtils.RETURN_ID, returnData.getReturnId());
		assertEquals(ReturnTestUtils.UNIT, returnData.getCustomRefundAmount().getCurrencyCode());

		assertNotNull(returnData.getReturnOrderLines());
		final OrderLineData orderLineData = orderService.getOrderLineByOrderIdAndOrderLineId(returnData.getOrder().getOrderId(),
				returnData.getReturnOrderLines().get(0).getOrderLineId());
		assertEquals(ReturnTestUtils.SKU_ID, orderLineData.getSkuId());
		assertEquals(ReturnTestUtils.STATUS, returnData.getReturnOrderLines().get(0).getReturnOrderLineStatus());
		assertNotNull(returnData.getReturnPaymentInfos());
		assertEquals(ReturnTestUtils.RETURN_PAYMENT_INFO_ID, returnData.getReturnPaymentInfos().getReturnPaymentInfoId());
		assertEquals(false, returnData.isShippingRefunded());

		final int value = orderService.getOrderLineByOrderIdAndOrderLineId(returnData.getOrder().getOrderId(),
				returnData.getReturnOrderLines().get(0).getOrderLineId()).getQuantityValue();
		assertEquals(2, value);

		assertNotNull(returnData.getReturnShipment());
		assertTrue(returnData.getReturnShipment().getReturnShipmentId() > 0);
	}


	@Transactional
	@Test
	public void reverseConvertingFlushedReturnDataWithOneZeroQuantity()
	{
		// given
		final Return aReturn = ReturnTestUtils.createOnlineReturnDto();
		aReturn.getReturnOrderLines().get(0).getOrderLine().setSkuId(SKU_ID);
		aReturn.setOrderId(OrderTestUtils.ORDER_ID);
		aReturn.getReturnOrderLines().get(0).setQuantity(new Quantity("", 0));

		// when
		final ReturnData returnData = this.returnReverseConverter.convert(aReturn);

		Assert.assertEquals(0, returnData.getReturnOrderLines().size());
	}
}
