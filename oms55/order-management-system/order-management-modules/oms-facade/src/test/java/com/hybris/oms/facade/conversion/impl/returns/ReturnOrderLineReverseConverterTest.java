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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
{ "/oms-facade-spring-test.xml" })
public class ReturnOrderLineReverseConverterTest
{
	@Autowired
	private Converter<ReturnOrderLine, ReturnOrderLineData> returnOrderLineReverseConverter;
	@Autowired
	private PersistenceManager persistenceManager;


	@Transactional
	@Test
	public void reverseConvertingNotFlushedOrderLineData()
	{
		// given
		final ReturnOrderLine returnOrderLine = ReturnTestUtils.createReturnOrderLineDto();
		//when
		final ReturnOrderLineData returnOrderLineData = this.returnOrderLineReverseConverter.convert(returnOrderLine);
		Assert.assertNotNull(returnOrderLineData);
	}

	@Transactional
	@Test
	public void reverseConvertingFlushedOrderLineData()
	{
		// given
		final ReturnOrderLine returnOrderLine = ReturnTestUtils.createReturnOrderLineDto();
		persistenceManager.flush();
		//when
		final ReturnOrderLineData returnOrderLineData = this.returnOrderLineReverseConverter.convert(returnOrderLine);
		Assert.assertNotNull(returnOrderLineData);
	}
}
