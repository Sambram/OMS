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
import com.hybris.oms.domain.returns.ReturnPaymentInfo;
import com.hybris.oms.service.managedobjects.returns.ReturnPaymentInfoData;

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
public class ReturnPaymentInfoConverterTest
{
	@Autowired
	private Converter<ReturnPaymentInfoData, ReturnPaymentInfo> returnPaymentInfoConverter;
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
	public void convertingNotFlushedReturnPaymentInfoData()
	{
		final ReturnPaymentInfo returnPaymentInfo = this.returnPaymentInfoConverter.convert(this.returnTestUtils
				.getReturnPaymentInfoData());
		AssertValid(returnPaymentInfo);
	}

	private void AssertValid(final ReturnPaymentInfo returnPaymentInfo)
	{
		Assert.assertNotNull(returnPaymentInfo);
		Assert.assertEquals(Long.toString(ReturnTestUtils.RETURN_PAYMENT_INFO_DATA_ID), returnPaymentInfo.getReturnPaymentInfoId());
		Assert.assertEquals(ReturnTestUtils.RETURN_PAYMENT_TYPE, returnPaymentInfo.getReturnPaymentType());
		Assert.assertEquals(ReturnTestUtils.UNIT, returnPaymentInfo.getReturnPaymentAmount().getCurrencyCode());
		Assert.assertEquals(Double.valueOf(5.0), returnPaymentInfo.getReturnPaymentAmount().getValue());
	}

}
