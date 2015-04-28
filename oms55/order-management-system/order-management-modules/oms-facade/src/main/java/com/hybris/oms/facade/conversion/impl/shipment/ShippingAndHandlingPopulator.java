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
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.PriceVT;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts {@link ShippingAndHandlingData} Managed Object into {@link ShippingAndHandling} DTO.
 */
public class ShippingAndHandlingPopulator extends AbstractPopulator<ShippingAndHandlingData, ShippingAndHandling>
{

	private Converter<PriceVT, Price> priceConverter;

	@Override
	public void populate(final ShippingAndHandlingData source, final ShippingAndHandling target) throws ConversionException
	{
		target.setFirstShipmentId(source.getFirstShipmentId());
		target.setOrderId(source.getOrderId());
		final Price price = this.priceConverter.convert(source.getShippingPrice());
		if (price != null)
		{
			target.setShippingPrice(new Price(price.getSubTotal(), price.getTax(), price.getTaxCommitted()));
		}
	}

	@Required
	public void setPriceConverter(final Converter<PriceVT, Price> priceConverter)
	{
		this.priceConverter = priceConverter;
	}
}
