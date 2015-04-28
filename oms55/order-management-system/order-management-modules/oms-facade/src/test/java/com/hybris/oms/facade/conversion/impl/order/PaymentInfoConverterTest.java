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
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.service.itemlocation.builders.AddressBuilder;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

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
public class PaymentInfoConverterTest
{

	@Autowired
	private Converter<PaymentInfoData, PaymentInfo> paymentInfoConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	private AddressVT address;
	private PaymentInfoData paymentInfoData;

	@Before
	public void setUp()
	{
		// given
		this.address = AddressBuilder.anAddress().buildAddressVT();
		this.paymentInfoData = this.prepareData();
	}

	@Transactional
	@Test
	public void convertingNotFlushedPaymentInfoData()
	{
		// when
		final PaymentInfo paymentInfo = this.paymentInfoConverter.convert(this.paymentInfoData);

		// then
		assertValid(paymentInfo);
	}

	@Transactional
	@Test
	public void convertingFlushedPaymentInfoData()
	{
		this.persistenceManager.flush();

		// when
		final PaymentInfo paymentInfo = this.paymentInfoConverter.convert(this.paymentInfoData);

		// then
		assertValid(paymentInfo);
	}

	private PaymentInfoData prepareData()
	{
		final PaymentInfoData piData = this.persistenceManager.create(PaymentInfoData.class);
		piData.setAuthUrl("authUrl");
		piData.setPaymentInfoType("type");
		piData.setBillingAddress(this.address);
		persistenceManager.flush();
		return piData;
	}

	private void assertValid(final PaymentInfo paymentInfo)
	{
		Assert.assertEquals("authUrl", paymentInfo.getAuthUrl());
		Assert.assertEquals("type", paymentInfo.getPaymentInfoType());
		Assert.assertEquals(AddressBuilder.anAddress().buildAddressDTO(), paymentInfo.getBillingAddress());
		Assert.assertEquals(Long.toString(paymentInfoData.getPaymentInfoId()), paymentInfo.getId());
	}

}
