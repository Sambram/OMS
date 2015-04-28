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
package com.hybris.oms.service.order.impl;

import com.hybris.oms.service.common.AddressStaticUtils;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * The Class OrderUtil.
 * 
 * @author otheriault
 *         <p/>
 *         Utility class for OrderData. This class contains convenience methods that should be encapsulated in the
 *         Managed Object, but cannot since it is an interface.
 */
public final class OrderDataStaticUtils
{
	private OrderDataStaticUtils()
	{
		//
	}

	public static void addOlqHavingShipment(final List<OrderLineQuantityData> olqs, final OrderLineData orderLine)
	{
		for (final OrderLineQuantityData orderLineQuantity : orderLine.getOrderLineQuantities())
		{
			if (orderLineQuantity.getShipment() == null)
			{
				olqs.add(orderLineQuantity);
			}
		}
	}

	/**
	 * Get all of an order's order line quantities.
	 * 
	 * @param order
	 *           the order
	 * @return List of OrderLineQuantity
	 */
	public static List<OrderLineQuantityData> getAllOrderLineQuantitiesUnassignedToShipments(final OrderData order)
	{

		final List<OrderLineQuantityData> olqs = new ArrayList<>();

		if (order.getOrderLines() != null)
		{
			for (final OrderLineData orderLine : order.getOrderLines())
			{

				if (orderLine.getOrderLineQuantities() != null)
				{
					addOlqHavingShipment(olqs, orderLine);
				}
			}
		}
		return olqs;
	}

	/**
	 * Get an array of all order line quantity ids found in this order.
	 * 
	 * @param order
	 *           the order
	 * @return List of olqIds
	 */
	public static List<Long> getOlqIds(final OrderData order)
	{
		final List<Long> olqIds = new ArrayList<>();
		for (final OrderLineQuantityData olq : getOrderLineQuantities(order))
		{
			olqIds.add(olq.getOlqId());
		}
		return olqIds;
	}

	/**
	 * Get an array of all order line quantity ids found in this shipment.
	 * 
	 * @param shipment
	 *           the shipment
	 * @return List of olqIds
	 */
	public static List<Long> getOlqIdsForShipment(final ShipmentData shipment)
	{
		final List<Long> olqIds = new ArrayList<>();
		for (final OrderLineQuantityData olq : getOrderLineQuantities(shipment.getOrderFk()))
		{
			if (olq.getShipment() != null && olq.getShipment().getShipmentId() == shipment.getShipmentId())
			{
				olqIds.add(olq.getOlqId());
			}
		}
		return olqIds;
	}

	/**
	 * Get all of an order's order line quantities.
	 * 
	 * @param order
	 *           the order
	 * @return list of OrderLineQuantity, never null
	 */
	public static List<OrderLineQuantityData> getOrderLineQuantities(final OrderData order)
	{
		final List<OrderLineQuantityData> olqs = new ArrayList<>();
		for (final OrderLineData orderLine : order.getOrderLines())
		{
			for (final OrderLineQuantityData orderLineQuantity : orderLine.getOrderLineQuantities())
			{
				olqs.add(orderLineQuantity);
			}
		}
		return olqs;
	}

	/**
	 * Check if the order is completely BOPIS.
	 * 
	 * @param order
	 * @return true if all order lines have pickup store id, false otherwise
	 */
	public static boolean isCompletelyPickup(final OrderData order)
	{
		boolean allBOPIS = true;
		for (final OrderLineData orderLine : order.getOrderLines())
		{
			if (StringUtils.isEmpty(orderLine.getPickupStoreId()))
			{
				allBOPIS = false;
				break;
			}
		}
		return allBOPIS;
	}


	/**
	 * Check if the order's has a shipping address with valid geocodes.
	 * 
	 * @param order
	 * @return true of order has shipping address with valid coordinates, false
	 *         otherwise
	 */
	public static boolean hasValidShippingAddressGeocodes(final OrderData order)
	{
		return ((order.getShippingAddress() != null) && (AddressStaticUtils.hasValidCoordinates(order.getShippingAddress())));
	}

	/**
	 * Check if this order was completely sourced.
	 * 
	 * @param order
	 * @return true is all order lines have quantity unassigned to 0, false otherwise
	 */
	public static boolean isCompletelySourced(final OrderData order)
	{
		boolean complete = true;

		for (final OrderLineData orderLine : order.getOrderLines())
		{
			if (orderLine.getQuantityUnassignedValue() > 0)
			{
				complete = false;
			}
		}
		return complete;
	}



}
