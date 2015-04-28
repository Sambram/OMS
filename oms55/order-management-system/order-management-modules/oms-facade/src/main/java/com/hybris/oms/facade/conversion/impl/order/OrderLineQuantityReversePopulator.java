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
import com.hybris.commons.conversion.Populator;
import com.hybris.kernel.api.PersistenceManager;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts order line quantity dto to order line quantity data.
 */
public class OrderLineQuantityReversePopulator implements Populator<OrderLineQuantity, OrderLineQuantityData>
{

	private PersistenceManager persistenceManager;

	private Converter<Shipment, ShipmentData> shipmentReverseConverter;

	@Override
	public void populateFinals(final OrderLineQuantity source, final OrderLineQuantityData target) throws ConversionException
	{
		target.setOlqId(Long.parseLong(source.getOlqId()));
	}

	@Override
	public void populate(final OrderLineQuantity source, final OrderLineQuantityData target) throws ConversionException
	{
		target.setStockroomLocationId(source.getLocation());

		if (source.getQuantity() != null)
		{
			target.setQuantityUnitCode(source.getQuantity().getUnitCode());
			target.setQuantityValue(source.getQuantity().getValue());
		}

		this.populateShipment(source, target);
		this.populateStatus(source, target);
	}

	protected void populateShipment(final OrderLineQuantity source, final OrderLineQuantityData target)
	{
		target.setShipment(this.shipmentReverseConverter.convert(source.getShipment()));
	}

	protected void populateStatus(final OrderLineQuantity source, final OrderLineQuantityData target)
	{
		final OrderLineQuantityStatusData olqStatusData = this.persistenceManager.getByIndex(
				OrderLineQuantityStatusData.UX_ORDERLINEQUANTITYSTATUSES_STATUSCODE, source.getStatus().getStatusCode());
		target.setStatus(olqStatusData);
	}

	@Required
	public void setPersistenceManager(final PersistenceManager persistenceManager)
	{
		this.persistenceManager = persistenceManager;
	}

	@Required
	public void setShipmentReverseConverter(final Converter<Shipment, ShipmentData> shipmentReverseConverter)
	{
		this.shipmentReverseConverter = shipmentReverseConverter;
	}

}
