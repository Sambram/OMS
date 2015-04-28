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
package com.hybris.oms.service.cis.conversion;

import com.hybris.cis.api.model.CisAddress;
import com.hybris.cis.api.model.CisAddressType;
import com.hybris.cis.api.model.CisLineItem;
import com.hybris.cis.api.model.CisOrder;
import com.hybris.cis.api.payment.model.CisPaymentRequest;
import com.hybris.cis.api.shipping.model.CisPackage;
import com.hybris.cis.api.shipping.model.CisShipment;
import com.hybris.cis.api.shipping.model.CisShippingServiceLevel;
import com.hybris.cis.api.shipping.model.CisWeightUnitsType;
import com.hybris.oms.service.cis.CisService;
import com.hybris.oms.service.inventory.InventoryService;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.AmountVT;
import com.hybris.oms.service.shipment.impl.ShipmentDataStaticUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class CisShipmentConverter
{
	private InventoryService inventoryService;

	private CisAddressConverter cisAddressConverter;

	public CisOrder convertOmsShipmentToCisOrder(final ShipmentData shipment)
	{
		if (shipment == null)
		{
			throw new IllegalArgumentException("Shipment cannot be null.");
		}

		// Convert addresses
		final CisOrder cisOrder = new CisOrder();
		cisOrder.setId(shipment.getOrderFk().getOrderId().concat(".").concat(String.valueOf(shipment.getShipmentId())));
		cisOrder.setCurrency(shipment.getMerchandisePrice().getSubTotalCurrencyCode());
		cisOrder.getAddresses().add(convertOmsAddressToCisAddress(CisAddressType.SHIP_FROM, shipment.getShipFrom()));
		cisOrder.getAddresses().add(
				convertOmsAddressToCisAddress(CisAddressType.SHIP_TO, shipment.getDelivery().getDeliveryAddress()));

		// Get OLQs and set line items
		final List<OrderLineQuantityData> olqs = ShipmentDataStaticUtils.getShipmentOrderLineQuantities(shipment);
		final List<CisLineItem> cisLineItems = new ArrayList<>();
		CisLineItem cisLineItem;
		for (final OrderLineQuantityData olq : olqs)
		{
			cisLineItem = new CisLineItem();
			cisLineItem.setId((int) olq.getOlqId());
			cisLineItem.setItemCode(olq.getOrderLine().getSkuId());
			cisLineItem.setTaxCode(olq.getOrderLine().getTaxCategory());
			cisLineItem.setQuantity(olq.getQuantityValue());
			cisLineItem.setUnitPrice(BigDecimal.valueOf(olq.getOrderLine().getUnitPriceValue()));

			if (StringUtils.isBlank(olq.getOrderLine().getNote()))
			{
				cisLineItem.setProductDescription(olq.getOrderLine().getNote());
			}
			cisLineItems.add(cisLineItem);
		}

		// Set the shipping line item only if first shipment
		if (ShipmentDataStaticUtils.isFirstShipmentForOrder(shipment) && !shipment.isPickupInStore())
		{
			cisLineItem = new CisLineItem();
			cisLineItem.setId(CisService.INVOICE_TAX_SHIPPING_LINE_ITEM_ID);
			cisLineItem.setItemCode(CisService.INVOICE_TAX_SHIPPING_LINE_ITEM_CODE);
			cisLineItem.setTaxCode(olqs.get(0).getOrderLine().getMyOrder().getShippingTaxCategory());
			cisLineItem.setQuantity(1);
			cisLineItem.setUnitPrice(BigDecimal.valueOf(shipment.getShippingAndHandling().getShippingPrice().getSubTotalValue()));

			if (StringUtils.isBlank(olqs.get(0).getOrderLine().getNote()))
			{
				cisLineItem.setProductDescription(olqs.get(0).getOrderLine().getNote());
			}
			cisLineItems.add(cisLineItem);
		}

		cisOrder.setLineItems(cisLineItems);
		return cisOrder;
	}

	public CisPaymentRequest convertOmsShipmentToCisPaymentRequest(final ShipmentData shipment)
	{
		final AmountVT amount = ShipmentDataStaticUtils.calculateTotalShippingAmount(shipment);

		final CisPaymentRequest cisPaymentRequest = new CisPaymentRequest();
		cisPaymentRequest.setAmount(BigDecimal.valueOf(amount.getValue()));
		cisPaymentRequest.setCurrency(amount.getCurrencyCode());
		return cisPaymentRequest;
	}

	public CisShipment convertOmsShipmentToCisShipment(final OrderData order, final ShipmentData shipment)
	{
		final CisShipment cisShipment = new CisShipment();

		final CisAddress cisFromAddress = cisAddressConverter.convertOmsAddressToCisAddress(shipment.getShipFrom());
		cisFromAddress.setFirstName(order.getShippingFirstName());
		cisFromAddress.setLastName(order.getShippingLastName());

		this.convertShipFromAddress(shipment, cisFromAddress);

		cisFromAddress.setType(CisAddressType.SHIP_FROM);
		cisShipment.getAddresses().add(cisFromAddress);

		convertShipToAddress(order, cisShipment);

		cisShipment.setPackage(this.convertpackage(shipment));

		convertServiceLevel(shipment, cisShipment);

		cisShipment.setServiceMethod(shipment.getShippingMethod());
		return cisShipment;
	}

	protected void convertInsurance(final ShipmentData shipment, final CisPackage cisPackage)
	{
		if (shipment.getInsuranceValueAmountValue() > 0)
		{
			cisPackage.setInsuredValue(String.valueOf(shipment.getInsuranceValueAmountValue()));
		}
	}

	protected CisAddress convertOmsAddressToCisAddress(final CisAddressType addressType, final AddressVT address)
	{
		final CisAddress cisAddress = new CisAddress();
		cisAddress.setType(addressType);
		cisAddress.setAddressLine1(address.getAddressLine1());
		cisAddress.setAddressLine2(address.getAddressLine2());
		cisAddress.setZipCode(address.getPostalZone());
		cisAddress.setCity(address.getCityName());
		cisAddress.setState(address.getCountrySubentity());
		cisAddress.setCountry(address.getCountryIso3166Alpha2Code());
		return cisAddress;
	}

	protected CisPackage convertpackage(final ShipmentData shipment)
	{
		final CisPackage cisPackage = new CisPackage();
		cisPackage.setHeight(valueIfPositiveElseNull(shipment.getHeightValue()));
		cisPackage.setLength(valueIfPositiveElseNull(shipment.getLengthValue()));
		cisPackage.setWidth(valueIfPositiveElseNull(shipment.getWidthValue()));
		cisPackage.setUnit(valueIfNotNullElseNull(shipment.getHeightUnitCode()));
		cisPackage.setWeight(valueIfPositiveElseNull(shipment.getGrossWeightValue()));
		cisPackage.setWeightUnit(CisWeightUnitsType.fromValue(shipment.getGrossWeightUnitCode()));
		cisPackage.setDescription(valueIfNotNullElseNull(shipment.getPackageDescription()));
		convertWeight(shipment, cisPackage);
		convertInsurance(shipment, cisPackage);
		return cisPackage;
	}

	protected void convertServiceLevel(final ShipmentData shipment, final CisShipment cisShipment)
	{
		for (final CisShippingServiceLevel serviceLevel : CisShippingServiceLevel.values())
		{
			if (serviceLevel.name().equals(shipment.getPriorityLevelCode()))
			{
				cisShipment.setServiceLevel(CisShippingServiceLevel.valueOf(shipment.getPriorityLevelCode()));
			}
		}
	}

	protected void convertShipFromAddress(final ShipmentData shipment, final CisAddress cisFromAddress)
	{
		// ShipFrom from Stockroom locations
		final StockroomLocationData locationFrom = this.inventoryService.getLocationByLocationId(shipment.getStockroomLocationId());

		if (locationFrom.getAddress() != null)
		{
			if (locationFrom.getAddress().getPhoneNumber() != null)
			{
				cisFromAddress.setPhone(locationFrom.getAddress().getPhoneNumber());
			}
			if (locationFrom.getAddress().getName() != null)
			{
				cisFromAddress.setCompany(locationFrom.getAddress().getName());
			}
		}
	}

	protected void convertShipToAddress(final OrderData order, final CisShipment cisShipment)
	{
		final AddressVT addressTo = order.getShippingAddress();
		final CisAddress cisToAddress = cisAddressConverter.convertOmsAddressToCisAddress(addressTo);
		cisToAddress.setType(CisAddressType.SHIP_TO);
		cisToAddress.setFirstName(order.getFirstName());
		cisToAddress.setLastName(order.getLastName());

		if (order.getShippingAddress() != null)
		{
			if (order.getShippingAddress().getPhoneNumber() != null)
			{
				cisToAddress.setPhone(order.getShippingAddress().getPhoneNumber());
			}
			if (order.getShippingAddress().getName() != null)
			{
				cisToAddress.setCompany(order.getShippingAddress().getName());
			}
		}
		cisShipment.getAddresses().add(cisToAddress);
		// ShipTo from Shipping
	}

	protected void convertWeight(final ShipmentData shipment, final CisPackage cisPackage)
	{
		if ((shipment.getGrossWeightValue() > 0) && (shipment.getGrossWeightUnitCode() != null))
		{
			for (final CisWeightUnitsType weightUnit : CisWeightUnitsType.values())
			{
				if (weightUnit.name().equals(shipment.getGrossWeightUnitCode()))
				{
					cisPackage.setWeightUnit(CisWeightUnitsType.valueOf(shipment.getGrossWeightUnitCode()));
				}
			}
		}
	}

	protected String valueIfNotNullElseNull(final String input)
	{
		return input != null ? String.valueOf(input) : null;
	}

	protected String valueIfPositiveElseNull(final float input)
	{
		return input > 0.0f ? String.valueOf(input) : null;
	}

	@Required
	protected InventoryService getInventoryService()
	{
		return inventoryService;
	}

	public void setInventoryService(final InventoryService inventoryService)
	{
		this.inventoryService = inventoryService;
	}

	@Required
	protected CisAddressConverter getCisAddressConverter()
	{
		return cisAddressConverter;
	}

	public void setCisAddressConverter(final CisAddressConverter cisAddressConverter)
	{
		this.cisAddressConverter = cisAddressConverter;
	}

}
