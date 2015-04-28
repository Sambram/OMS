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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/oms-facade-spring-test.xml"})
public class PaymentInfoReverseConverterTest
{

	@Autowired
	private Converter<PaymentInfo, PaymentInfoData> paymentInfoReverseConverter;

	@Autowired
	private PersistenceManager persistenceManager;

	@Transactional
	@Test
	public void reverseConvertingNotFlushedPaymentInfoData()
	{
		// given
		final AddressVT address = AddressBuilder.anAddress().buildAddressVT();

		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setAuthUrl("authUrl");
		paymentInfo.setPaymentInfoType("type");
		paymentInfo.setBillingAddress(AddressBuilder.anAddress().buildAddressDTO());

		// when
		final PaymentInfoData paymentInfoData = this.paymentInfoReverseConverter.convert(paymentInfo);

		// then
		Assert.assertTrue(paymentInfoData.getId() == null);
		Assert.assertEquals("authUrl", paymentInfoData.getAuthUrl());
		Assert.assertEquals("type", paymentInfoData.getPaymentInfoType());
		Assert.assertEquals(address, paymentInfoData.getBillingAddress());
	}

	@Transactional
	@Test
	public void reverseConvertingFlushedPaymentInfoData()
	{
		// given
		final AddressVT address = AddressBuilder.anAddress().buildAddressVT();

		final PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setAuthUrl("authUrl");
		paymentInfo.setPaymentInfoType("type");
		paymentInfo.setBillingAddress(AddressBuilder.anAddress().buildAddressDTO());

		// when
		final PaymentInfoData paymentInfoData = this.paymentInfoReverseConverter.convert(paymentInfo);

		this.persistenceManager.flush();

		// then
		Assert.assertTrue(paymentInfoData.getId() != null);
		Assert.assertEquals("authUrl", paymentInfoData.getAuthUrl());
		Assert.assertEquals("type", paymentInfoData.getPaymentInfoType());
		Assert.assertEquals(address, paymentInfoData.getBillingAddress());
	}

}
