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
package com.hybris.oms.facade.conversion.impl.shipment;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.Populator;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.shipping.Delivery;
import com.hybris.oms.service.managedobjects.shipment.DeliveryData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts {@link Delivery} DTO into {@link DeliveryData} Managed Object.
 */
public class DeliveryReversePopulator implements Populator<Delivery, DeliveryData>
{

	private Converter<Address, AddressVT> addressReverseConverter;

	@Override
	public void populate(final Delivery source, final DeliveryData target) throws ConversionException
	{
		target.setActualDeliveryDate(source.getActualDeliveryDate());
		target.setDeliveryAddress(this.addressReverseConverter.convert(source.getDeliveryAddress()));
		target.setLabelUrl(source.getLabelUrl());
		target.setTrackingID(source.getTrackingID());
		target.setTrackingUrl(source.getTrackingUrl());
	}

	@Override
	public void populateFinals(final Delivery source, final DeliveryData target) throws ConversionException
	{
		try
		{
			target.setDeliveryId(Long.parseLong(source.getDeliveryId()));
		}
		catch (final NumberFormatException e)
		{
			throw new ConversionException("Delivery#deliveryId should represent long value, but was: " + source.getDeliveryId(), e);
		}
	}

	@Required
	public void setAddressReverseConverter(final Converter<Address, AddressVT> addressReverseConverter)
	{
		this.addressReverseConverter = addressReverseConverter;
	}
}
