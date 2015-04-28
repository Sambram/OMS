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
package com.hybris.oms.ui.facade.conversion.impl.shipment;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.commons.conversion.impl.AbstractPopulator;
import com.hybris.oms.domain.exception.EntityNotFoundException;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.ui.api.shipment.UIShipment;

import org.apache.commons.lang3.StringUtils;


public class UIShipmentPopulator extends AbstractPopulator<ShipmentData, UIShipment>
{
	private InventoryService inventoryService;

	@Override
	public void populate(final ShipmentData source, final UIShipment target) throws ConversionException
	{
		final OrderData orderData = source.getOrderFk();
		String storeName = StringUtils.EMPTY;
		try
		{
			if (StringUtils.isNotBlank(source.getStockroomLocationId()))
			{
				final StockroomLocationData stockroomLocationData = this.inventoryService.getLocationByLocationId(source
						.getStockroomLocationId());
				storeName = stockroomLocationData.getStoreName();
			}
		}
		catch (final EntityNotFoundException exception)
		{
			// Do Nothing
		}

		target.setOrderId(orderData.getOrderId());
		target.setShipmentId(source.getShipmentId());
		target.setFirstName(orderData.getFirstName());
		target.setLastName(orderData.getLastName());
		target.setLocationName(storeName);
		target.setOrderDate(orderData.getIssueDate());
		target.setScheduledShippingDate(orderData.getScheduledShippingDate());
		target.setShippingMethod(source.getShippingMethod());
		target.setStatus(source.getOlqsStatus());
	}

	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}
}
