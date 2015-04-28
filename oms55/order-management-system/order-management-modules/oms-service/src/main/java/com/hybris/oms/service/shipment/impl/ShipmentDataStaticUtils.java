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
package com.hybris.oms.service.shipment.impl;

import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.managedobjects.types.QuantityVT;
import com.hybris.oms.service.order.impl.OlqDataStaticUtils;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;
import com.hybris.oms.service.util.AmountVtUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


public final class ShipmentDataStaticUtils
{

	private ShipmentDataStaticUtils()
	{
		//
	}

	/**
	 * Shipment Subtotal Amount= Sum(Shipment Order Line Quantity -> Subtotal
	 * Amount).
	 */
	public static AmountVT calculateShipmentSubtotalAmount(final List<OrderLineQuantityData> olqs)
	{
		if (CollectionUtils.isEmpty(olqs))
		{
			throw new IllegalArgumentException("List of olqs cannot be null or empty.");
		}

		AmountVT shipmentSubtotalAmount = null;
		for (final OrderLineQuantityData olq : olqs)
		{
			// Calculate subtotal amount for each olq and sum them up
			final AmountVT olqMerchandiseTotal = OlqDataStaticUtils.calculateOrderLineQuantitySubtotalAmount(olq);
			if (shipmentSubtotalAmount == null)
			{
				shipmentSubtotalAmount = olqMerchandiseTotal;
			}
			else
			{
				shipmentSubtotalAmount = AmountVtUtils.add(olqMerchandiseTotal, shipmentSubtotalAmount);
			}
		}
		return shipmentSubtotalAmount;
	}

	/**
	 * Shipment merchandise tax Amount = Sum(Order line quantity tax).
	 */
	public static AmountVT calculateShipmentTaxAmount(final List<OrderLineQuantityData> olqs)
	{
		if (CollectionUtils.isEmpty(olqs))
		{
			throw new IllegalArgumentException("List of olqs cannot be null or empty.");
		}

		AmountVT shipmentTaxAmount = null;
		for (final OrderLineQuantityData olq : olqs)
		{
			// Calculate subtotal amount for each olq and sum them up
			final AmountVT olqTaxTotal = OlqDataStaticUtils.calculateOrderLineQuantityTaxAmount(olq);
			if (shipmentTaxAmount == null)
			{
				shipmentTaxAmount = olqTaxTotal;
			}
			else
			{
				shipmentTaxAmount = AmountVtUtils.add(olqTaxTotal, shipmentTaxAmount);
			}
		}
		return shipmentTaxAmount;
	}

	/**
	 * Total Goods Item Quantity = Sum(Shipment Order Line Quantity ->
	 * Quantity).
	 * 
	 * @param olqs
	 *           the olqs
	 * @return Quantity
	 */
	public static QuantityVT calculateTotalGoodsItemQuantity(final List<OrderLineQuantityData> olqs)
	{
		if (CollectionUtils.isEmpty(olqs))
		{
			throw new IllegalArgumentException("List of olqs cannot be null or empty.");
		}

		int quantity = 0;
		String unitCode = null;
		for (final OrderLineQuantityData olq : olqs)
		{
			quantity += olq.getQuantityValue();
			if (olq.getQuantityUnitCode() != null)
			{
				if (unitCode == null)
				{
					unitCode = olq.getQuantityUnitCode();
				}
				else if (!unitCode.equals(olq.getQuantityUnitCode()))
				{
					throw new IllegalArgumentException("Adding quantities with different unitcodes is not supported");
				}
			}
		}
		return new QuantityVT(unitCode, quantity);
	}

	/**
	 * Calculate the total shipping cost calculated: Shipment merchandisePrice
	 * calculated: Shipment merchandisePrice subTotal + merchandisePrice tax + shippingPrice subTotal + shippingPrice
	 * tax.
	 * 
	 * @param shipment the shipment
	 * @return the total amount for the shipment
	 */
	public static AmountVT calculateTotalShippingAmount(final ShipmentData shipment)
	{
		final PriceVT merchandisePrice = shipment.getMerchandisePrice();

		AmountVT amount = new AmountVT(merchandisePrice.getSubTotalCurrencyCode(), merchandisePrice.getSubTotalValue());
		amount = AmountVtUtils.add(new AmountVT(merchandisePrice.getTaxCurrencyCode(), merchandisePrice.getTaxValue()), amount);

		// We only add shipping price and shipping tax for the first shipment and only if it is not for pickupInStore
		if (isFirstShipmentForOrder(shipment) && !shipment.isPickupInStore())
		{

			final PriceVT shippingPrice = shipment.getShippingAndHandling().getShippingPrice();

			amount = AmountVtUtils.add(new AmountVT(shippingPrice.getSubTotalCurrencyCode(), shippingPrice.getSubTotalValue()),
					amount);
			amount = AmountVtUtils.add(new AmountVT(shippingPrice.getTaxCurrencyCode(), shippingPrice.getTaxValue()), amount);
		}
		return amount;
	}

	/**
	 * Checks if a shipment is the first shipment for an order.
	 * 
	 * @param shipment
	 * @return boolean
	 */
	public static boolean isFirstShipmentForOrder(final ShipmentData shipment)
	{
		if (shipment.getShippingAndHandling() != null && shipment.getShippingAndHandling().getFirstShipmentId() != null
				&& Long.parseLong(shipment.getShippingAndHandling().getFirstShipmentId()) == shipment.getShipmentId())
		{
			return true;
		}

		return false;
	}

	/**
	 * Gets the order line quantities for this shipment.
	 * 
	 * @param shipment
	 * @return the order line quantities list, never null
	 */
	public static List<OrderLineQuantityData> getShipmentOrderLineQuantities(final ShipmentData shipment)
	{
		final List<OrderLineQuantityData> shipmentOlqs = new ArrayList<>();
		final List<OrderLineQuantityData> orderOlqs = OrderDataStaticUtils.getOrderLineQuantities(shipment.getOrderFk());

		for (final OrderLineQuantityData olq : orderOlqs)
		{
			if (olq.getShipment() != null && (olq.getShipment().getShipmentId() == shipment.getShipmentId()))
			{
				shipmentOlqs.add(olq);
			}
		}

		return shipmentOlqs;
	}

	/**
	 * Checks whether the olqs belong to the shipment
	 * 
	 * @deprecated use ShipmentOlqValidator to check for this in facade
	 * 
	 * @param shipment
	 * @param olqs
	 * @return true if all olqs belong to the given shipment, false otherwise
	 */
	@Deprecated
	public static boolean checkOlqsBelongToShipment(final ShipmentData shipment, final List<OrderLineQuantityData> olqs)
	{
		final List<OrderLineQuantityData> shipmentOlqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment);

		for (final OrderLineQuantityData olq : olqs)
		{
			if (!shipmentOlqs.contains(olq))
			{
				return false;
			}
		}

		return true;
	}
}
