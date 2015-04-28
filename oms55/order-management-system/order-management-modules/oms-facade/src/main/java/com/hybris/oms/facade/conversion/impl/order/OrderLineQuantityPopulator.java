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

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.Converter;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.order.OrderLineQuantityStatus;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converter for order line quantity data to dto.
 */
public class OrderLineQuantityPopulator extends AbstractPopulator<OrderLineQuantityData, OrderLineQuantity>
{

	private Converter<ShipmentData, Shipment> shipmentConverter;

	private Converter<OrderLineQuantityStatusData, OrderLineQuantityStatus> orderLineQuantityStatusConverter;

	@Override
	public void populate(final OrderLineQuantityData source, final OrderLineQuantity target) throws ConversionException
	{
		target.setLocation(source.getStockroomLocationId());
		target.setOlqId(Long.toString(source.getOlqId()));
		target.setQuantity(new Quantity(source.getQuantityUnitCode(), source.getQuantityValue()));
		target.setShipment(this.shipmentConverter.convert(source.getShipment()));
		target.setStatus(this.orderLineQuantityStatusConverter.convert(source.getStatus()));
	}

	@Required
	public void setShipmentConverter(final Converter<ShipmentData, Shipment> shipmentConverter)
	{
		this.shipmentConverter = shipmentConverter;
	}

	@Required
	public void setOrderLineQuantityStatusConverter(
			final Converter<OrderLineQuantityStatusData, OrderLineQuantityStatus> orderLineQuantityStatusConverter)
	{
		this.orderLineQuantityStatusConverter = orderLineQuantityStatusConverter;
	}

}
