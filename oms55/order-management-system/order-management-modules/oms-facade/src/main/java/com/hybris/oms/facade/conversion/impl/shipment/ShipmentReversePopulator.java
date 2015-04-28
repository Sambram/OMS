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
import com.hybris.commons.conversion.impl.EnumToEnumConverter;
import com.hybris.oms.domain.address.Address;
import com.hybris.oms.domain.shipping.Delivery;
import com.hybris.oms.domain.shipping.Shipment;
import com.hybris.oms.domain.types.Price;
import com.hybris.oms.service.managedobjects.shipment.DeliveryData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converts a shipment dto to shipment data.
 */
public class ShipmentReversePopulator implements Populator<Shipment, ShipmentData>
{

	private Converter<Address, AddressVT> addressReverseConverter;

	private Converter<Delivery, DeliveryData> deliveryReverseConverter;

	private Converter<Price, PriceVT> priceReverseConverter;

	private final EnumToEnumConverter<com.hybris.oms.domain.shipping.OrderlinesFulfillmentType, com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType> orderLineFlTypeConverter = new EnumToEnumConverter<com.hybris.oms.domain.shipping.OrderlinesFulfillmentType, com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType>()
	{
		@Override
		public Class<com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType> getTargetClass()
		{
			return com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType.class;
		}
	};

	protected EnumToEnumConverter<com.hybris.oms.domain.shipping.OrderlinesFulfillmentType, com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType> getOrderLineFlTypeConverter()
	{
		return this.orderLineFlTypeConverter;
	}

	@Override
	public void populateFinals(final Shipment source, final ShipmentData target) throws ConversionException
	{
		target.setShipmentId(Long.parseLong(source.getShipmentId()));
	}

	@Override
	public void populate(final Shipment source, final ShipmentData target) throws ConversionException
	{
		this.populateAmountAndAddress(source, target);
		this.populateQuantitiesAndHandling(source, target);
		this.poupulateMerchandiseAndShipping(source, target);
		if (source.getWidth() != null)
		{
			target.setWidthUnitCode(source.getWidth().getUnitCode());
			target.setWidthValue(source.getWidth().getValue());
		}
		if (source.getTotalGoodsItemQuantity() != null)
		{
			target.setTotalGoodsItemQuantityUnitCode(source.getTotalGoodsItemQuantity().getUnitCode());
			target.setTotalGoodsItemQuantityValue(source.getTotalGoodsItemQuantity().getValue());
		}
		target.setTaxCategory(source.getTaxCategory());

		target.setOrderLinesFulfillmentType(this.getOrderLineFlTypeConverter().convert(source.getOrderLinesFulfillmentType()) != null ? this
				.getOrderLineFlTypeConverter().convert(source.getOrderLinesFulfillmentType())
						: com.hybris.oms.service.managedobjects.shipment.OrderlinesFulfillmentType.REGULAR);

	}

	protected void populateAmountAndAddress(final Shipment source, final ShipmentData target)
	{
		if (source.getAmountCaptured() != null)
		{
			target.setAmountCapturedCurrencyCode(source.getAmountCaptured().getCurrencyCode());
			target.setAmountCapturedValue(source.getAmountCaptured().getValue());
		}

		target.setAuthUrls(source.getAuthUrls());
		target.setCurrencyCode(source.getCurrencyCode());
		target.setDelivery(this.deliveryReverseConverter.convert(source.getDelivery()));
	}

	protected void populateQuantitiesAndHandling(final Shipment source, final ShipmentData target)
	{
		if (source.getGrossVolume() != null)
		{
			target.setGrossVolumeUnitCode(source.getGrossVolume().getUnitCode());
			target.setGrossVolumeValue(source.getGrossVolume().getValue());
		}

		if (source.getGrossWeight() != null)
		{
			target.setGrossWeightUnitCode(source.getGrossWeight().getUnitCode());
			target.setGrossWeightValue(source.getGrossWeight().getValue());
		}

		if (source.getHeight() != null)
		{
			target.setHeightUnitCode(source.getHeight().getUnitCode());
			target.setHeightValue(source.getHeight().getValue());
		}

		if (source.getInsuranceValueAmount() != null)
		{
			target.setInsuranceValueAmountCurrencyCode(source.getInsuranceValueAmount().getCurrencyCode());
			target.setInsuranceValueAmountValue(source.getInsuranceValueAmount().getValue());
		}

		if (source.getLength() != null)
		{
			target.setLengthUnitCode(source.getLength().getUnitCode());
			target.setLengthValue(source.getLength().getValue());
		}

		target.setFirstArrivalStockroomLocationId(source.getFirstArrivalLocationId());
		target.setLastExitStockroomLocationId(source.getLastExitLocationId());
	}

	protected void poupulateMerchandiseAndShipping(final Shipment source, final ShipmentData target)
	{
		if (source.getNetWeight() != null)
		{
			target.setNetWeightUnitCode(source.getNetWeight().getUnitCode());
			target.setNetWeightValue(source.getNetWeight().getValue());
		}

		target.setStockroomLocationId(source.getLocation());
		target.setOlqsStatus(source.getOlqsStatus());
		target.setPickupInStore(source.getPickupInStore());
		target.setPriorityLevelCode(source.getPriorityLevelCode());
		target.setMerchandisePrice(this.priceReverseConverter.convert(source.getMerchandisePrice()));
		target.setShipFrom(this.addressReverseConverter.convert(source.getShipFrom()));
		target.setShippingMethod(source.getShippingMethod());
	}

	@Required
	public void setDeliveryReverseConverter(final Converter<Delivery, DeliveryData> deliveryReverseConverter)
	{
		this.deliveryReverseConverter = deliveryReverseConverter;
	}

	@Required
	public void setPriceReverseConverter(final Converter<Price, PriceVT> priceReverseConverter)
	{
		this.priceReverseConverter = priceReverseConverter;
	}

	@Required
	public void setAddressReverseConverter(final Converter<Address, AddressVT> addressReverseConverter)
	{
		this.addressReverseConverter = addressReverseConverter;
	}

}
