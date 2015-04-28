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
import com.hybris.oms.domain.returns.ReturnOrderLine;
import com.hybris.oms.service.managedobjects.returns.ReturnOrderLineData;

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
public class ReturnOrderLineConverterTest
{


	@Autowired
	private Converter<ReturnOrderLineData, ReturnOrderLine> returnOrderLineConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private ReturnTestUtils returnTestUtils;

	private ReturnOrderLineData returnOrderLineData;

	@Before
	public void setUp()
	{
		// given
		this.returnTestUtils = new ReturnTestUtils();
		this.returnTestUtils.createObjects(this.persistenceManager);
		this.returnOrderLineData = returnTestUtils.getReturnOrderLineData();
	}

	private void assertValid(final ReturnOrderLine returnOrderLine)
	{
		Assert.assertEquals(String.valueOf(ReturnTestUtils.RETURN_ORDER_LINE_ID), returnOrderLine.getReturnOrderLineId());
		Assert.assertEquals(ReturnTestUtils.SKU_ID, returnOrderLine.getOrderLine().getSkuId());
		Assert.assertEquals(ReturnTestUtils.STATUS, returnOrderLine.getReturnOrderLineStatus());

	}

	@Transactional
	@Test
	public void convertingFlushedOrderLineData()
	{
		persistenceManager.flush();
		// when
		final ReturnOrderLine returnOrderLine = this.returnOrderLineConverter.convert(this.returnOrderLineData);

		// then
		assertValid(returnOrderLine);
	}

}
