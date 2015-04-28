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
import com.hybris.commons.conversion.impl.EnumToEnumConverter;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.shipping.Delivery;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.shipping.ShippingAndHandling;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Measure;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.shipment.DeliveryData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;
import com.hybris.oms.service.order.impl.OrderDataStaticUtils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts {@link ShipmentData} Managed Object into {@link Shipment} DTO.
 */
public class ShipmentPopulator extends AbstractPopulator<ShipmentData, Shipment>
{

	private Converter<DeliveryData, Delivery> deliveryConverter;
	private Converter<ShippingAndHandlingData, ShippingAndHandling> shippingAndHandlingConverter;
	private Converter<AddressVT, Address> addressConverter;
	private Converter<PriceVT, Price> priceConverter;

	private final EnumToEnumConverter<com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType, com.hybris.oms.domain.shipping.OrderlinesFulfillmentType> orderLinesFlTypeConverter = new EnumToEnumConverter<com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType, com.hybris.oms.domain.shipping.OrderlinesFulfillmentType>()
	{
		@Override
		public Class<com.hybris.oms.domain.shipping.OrderlinesFulfillmentType> getTargetClass()
		{
			return com.hybris.oms.domain.shipping.OrderlinesFulfillmentType.class;
		}
	};

	protected EnumToEnumConverter<com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType, com.hybris.oms.domain.shipping.OrderlinesFulfillmentType> getOrderLineFlTypeConverter()
	{
		return this.orderLinesFlTypeConverter;
	}

	@Override
	public void populate(final ShipmentData source, final Shipment target) throws ConversionException
	{
		this.populateAmountAndAddress(source, target);

		this.populateQuantitiesAndHandling(source, target);

		this.poupulateMerchandiseAndShipping(source, target);

		target.setState(source.getState());
		target.setTaxCategory(source.getTaxCategory());

		target.setTotalGoodsItemQuantity(new Quantity(source.getTotalGoodsItemQuantityUnitCode(), source
						.getTotalGoodsItemQuantityValue()));

		target.setWidth(new Measure(source.getWidthUnitCode(), source.getWidthValue()));
		target.setOrderLinesFulfillmentType(this.getOrderLineFlTypeConverter().convert(source.getOrderLinesFulfillmentType()));
	}

	protected void populateAmountAndAddress(final ShipmentData source, final Shipment target)
	{
		target.setCurrencyCode(source.getCurrencyCode());
		target.setAmountCaptured(new Amount(source.getAmountCapturedCurrencyCode(), source.getAmountCapturedValue()));

		target.setAuthUrls(source.getAuthUrls());

		target.setDelivery(this.deliveryConverter.convert(source.getDelivery()));
	}

	protected void populateQuantitiesAndHandling(final ShipmentData source, final Shipment target)
	{
		target.setFirstArrivalLocationId(source.getFirstArrivalStockroomLocationId());

		target.setGrossVolume(new Measure(source.getGrossVolumeUnitCode(), source.getGrossVolumeValue()));

		target.setGrossWeight(new Measure(source.getGrossWeightUnitCode(), source.getGrossWeightValue()));

		target.setHandlingCode(null); // !!!

		target.setHandlingInstructions(null); // !!!

		target.setHeight(new Measure(source.getHeightUnitCode(), source.getHeightValue()));

		target.setInformation(null); // !!!

		target.setInsuranceValueAmount(new Amount(source.getInsuranceValueAmountCurrencyCode(), source
						.getInsuranceValueAmountValue()));

		target.setLastExitLocationId(source.getLastExitStockroomLocationId());

		target.setLength(new Measure(source.getLengthUnitCode(), source.getLengthValue()));
	}

	protected void poupulateMerchandiseAndShipping(final ShipmentData source, final Shipment target)
	{
		target.setLocation(source.getStockroomLocationId());

		target.setMerchandisePrice(this.priceConverter.convert(source.getMerchandisePrice()));

		target.setNetWeight(new Measure(source.getNetWeightUnitCode(), source.getNetWeightValue()));

		target.setOlqIds(this.getOlqIdsAsString(OrderDataStaticUtils.getOlqIdsForShipment(source)));

		target.setOlqsStatus(source.getOlqsStatus());

		if (source.getOrderFk() == null)
		{
			target.setOrderId(null);
		}
		else
		{
			target.setOrderId(source.getOrderFk().getOrderId());
		}

		target.setPickupInStore(source.isPickupInStore());

		target.setPriorityLevelCode(source.getPriorityLevelCode());

		target.setShipFrom(this.addressConverter.convert(source.getShipFrom()));

		target.setShipmentId(Long.toString(source.getShipmentId()));

		target.setShippingAndHandling(this.shippingAndHandlingConverter.convert(source.getShippingAndHandling()));

		target.setShippingMethod(source.getShippingMethod());
	}

	protected List<String> getOlqIdsAsString(final List<Long> olqIds)
	{
		final List<String> olqIdsString = new ArrayList<>();
		for (final Long olqId : olqIds)
		{
			olqIdsString.add(olqId.toString());
		}

		return olqIdsString;
	}

	@Required
	public void setDeliveryConverter(final Converter<DeliveryData, Delivery> deliveryConverter)
	{
		this.deliveryConverter = deliveryConverter;
	}

	@Required
	public void setShippingAndHandlingConverter(
			final Converter<ShippingAndHandlingData, ShippingAndHandling> shippingAndHandlingConverter)
	{
		this.shippingAndHandlingConverter = shippingAndHandlingConverter;
	}

	@Required
	public void setPriceConverter(final Converter<PriceVT, Price> priceConverter)
	{
		this.priceConverter = priceConverter;
	}

	@Required
	public void setAddressConverter(final Converter<AddressVT, Address> addressConverter)
	{
		this.addressConverter = addressConverter;
	}
}
