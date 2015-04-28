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

import org.springframework.beans.factory.annotation.Required;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.order.PaymentInfo;
import com.hybris.oms.service.managedobjects.order.PaymentInfoData;
import com.hybris.oms.service.managedobjects.types.AddressVT;


/**
 * Converter for payment info dto to data.
 */
public class PaymentInfoReversePopulator extends AbstractPopulator<PaymentInfo, PaymentInfoData>
{

	private Converter<Address, AddressVT> addressReverseConverter;

	@Override
	public void populate(final PaymentInfo source, final PaymentInfoData target) throws ConversionException
	{
		target.setAuthUrl(source.getAuthUrl());
		target.setPaymentInfoType(source.getPaymentInfoType());
		target.setBillingAddress(this.addressReverseConverter.convert(source.getBillingAddress()));
		target.setCaptureId(source.getCaptureId());
	}

	@Required
	public void setAddressReverseConverter(final Converter<Address, AddressVT> addressReverseConverter)
	{
		this.addressReverseConverter = addressReverseConverter;
	}
}
