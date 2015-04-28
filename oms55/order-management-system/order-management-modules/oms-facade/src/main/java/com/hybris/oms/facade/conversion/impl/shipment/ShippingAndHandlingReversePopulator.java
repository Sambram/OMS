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
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.PriceVT;


/**
 * Converts {@link ShippingAndHandling} DTO into {@link ShippingAndHandlingData} Managed Object.
 */
public class ShippingAndHandlingReversePopulator implements Populator<ShippingAndHandling, ShippingAndHandlingData>
{
	private Converter<Price, PriceVT> priceReverseConverter;

	@Override
	public void populate(final ShippingAndHandling source, final ShippingAndHandlingData target) throws ConversionException
	{
		target.setFirstShipmentId(source.getFirstShipmentId());

		final Price price;

		if (source.getShippingPrice() == null)
		{
			price = null;
		}
		else
		{
			price = new Price(source.getShippingPrice().getSubTotal(), source.getShippingPrice().getTax(), source.getShippingPrice()
					.getTaxCommitted());
		}

		target.setShippingPrice(this.priceReverseConverter.convert(price));
	}

	@Override
	public void populateFinals(final ShippingAndHandling source, final ShippingAndHandlingData target) throws ConversionException
	{
		target.setOrderId(source.getOrderId());
	}

	public void setPriceReverseConverter(final Converter<Price, PriceVT> priceReverseConverter)
	{
		this.priceReverseConverter = priceReverseConverter;
	}
}
