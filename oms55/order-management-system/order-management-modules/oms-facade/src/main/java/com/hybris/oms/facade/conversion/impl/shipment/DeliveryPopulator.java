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
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.shipping.Delivery;
import com.hybris.oms.service.managedobjects.shipment.DeliveryData;
import com.hybris.oms.service.managedobjects.types.AddressVT;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts {@link DeliveryData} Managed Object into {@link Delivery} DTO.
 */
public class DeliveryPopulator extends AbstractPopulator<DeliveryData, Delivery>
{

	private Converter<AddressVT, Address> addressConverter;

	@Override
	public void populate(final DeliveryData source, final Delivery target) throws ConversionException
	{
		target.setDeliveryId(Long.toString(source.getDeliveryId()));

		target.setQuantityUnitCode(null);

		target.setQuantityValue(0);

		target.setActualDeliveryDate(source.getActualDeliveryDate());

		target.setLatestDeliveryDate(null);

		target.setTrackingID(source.getTrackingID());

		target.setTrackingUrl(source.getTrackingUrl());

		target.setLabelUrl(source.getLabelUrl());

		target.setDeliveryAddress(this.addressConverter.convert(source.getDeliveryAddress()));

		target.setDeliveryLocationId(null);
	}

	@Required
	public void setAddressConverter(final Converter<AddressVT, Address> addressConverter)
	{
		this.addressConverter = addressConverter;
	}
}
