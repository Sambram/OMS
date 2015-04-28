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
import com.hybris.commons.conversion.impl.EnumToEnumConverter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.domain.types.Amount;
import com.hybris.oms.domain.types.Quantity;
import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converter for order line data to dto.
 */
public class OrderLinePopulator extends AbstractPopulator<OrderLineData, OrderLine>
{
	private Converters converters;

	private Converter<OrderLineQuantityData, OrderLineQuantity> orderLineQuantityConverter;

	private Converter<OrderLineAttributeData, OrderLineAttribute> orderLineAttributeConverter;

	private final EnumToEnumConverter<com.hybris.oms.service.managedobjects.inventory.LocationRole, com.hybris.oms.domain.locationrole.LocationRole> locationRoleConverter = new EnumToEnumConverter<com.hybris.oms.service.managedobjects.inventory.LocationRole, com.hybris.oms.domain.locationrole.LocationRole>()
	{
		@Override
		public Class<com.hybris.oms.domain.locationrole.LocationRole> getTargetClass()
		{
			return com.hybris.oms.domain.locationrole.LocationRole.class;
		}
	};

	private final EnumToEnumConverter<com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType, com.hybris.oms.domain.order.OrderlineFulfillmentType> orderLineFulfillmentTypeConverter = new EnumToEnumConverter<com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType, com.hybris.oms.domain.order.OrderlineFulfillmentType>()
	{
		@Override
		public Class<com.hybris.oms.domain.order.OrderlineFulfillmentType> getTargetClass()
		{
			return com.hybris.oms.domain.order.OrderlineFulfillmentType.class;
		}
	};

	protected EnumToEnumConverter<com.hybris.oms.service.managedobjects.inventory.LocationRole, com.hybris.oms.domain.locationrole.LocationRole> getLocationRoleConverter()
	{
		return this.locationRoleConverter;
	}

	protected EnumToEnumConverter<com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType, com.hybris.oms.domain.order.OrderlineFulfillmentType> getOrderlineFulfillmentTypeConverter()
	{
		return this.orderLineFulfillmentTypeConverter;
	}

	@Override
	public void populate(final OrderLineData source, final OrderLine target) throws ConversionException
	{
		target.setNote(source.getNote());
		target.setOrderLineId(source.getOrderLineId());
		target.setOrderLineQuantities(this.converters.convertAll(source.getOrderLineQuantities(), this.orderLineQuantityConverter));
		target.setOrderLineAttributes(this.converters.convertAll(source.getOrderLineAttributes(), this.orderLineAttributeConverter));
		target.setOrderLineStatus(source.getOrderLineStatus());
		target.setQuantity(new Quantity(source.getQuantityUnitCode(), source.getQuantityValue()));
		target.setQuantityUnassigned(new Quantity(source.getQuantityUnassignedUnitCode(), source.getQuantityUnassignedValue()));
		target.setSkuId(source.getSkuId());
		target.setTaxCategory(source.getTaxCategory());
		target.setUnitPrice(new Amount(source.getUnitPriceCurrencyCode(), source.getUnitPriceValue()));
		target.setUnitTax(new Amount(source.getUnitTaxCurrencyCode(), source.getUnitTaxValue()));
		target.setPickupStoreId(source.getPickupStoreId());
		target.setFulfillmentType(this.getOrderlineFulfillmentTypeConverter().convert(source.getFulfillmentType()));
		final Set<LocationRole> roles = new HashSet<>();
		if (source.getLocationRoles() != null && !source.getLocationRoles().isEmpty())
		{
			for (final String role : source.getLocationRoles())
			{
				roles.add(LocationRole.valueOf(role));
			}
			target.setLocationRoles(roles);
		}
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setOrderLineQuantityConverter(final Converter<OrderLineQuantityData, OrderLineQuantity> orderLineQuantityConverter)
	{
		this.orderLineQuantityConverter = orderLineQuantityConverter;
	}

	@Required
	public void setOrderLineAttributeConverter(
			final Converter<OrderLineAttributeData, OrderLineAttribute> orderLineAttributeConverter)
	{
		this.orderLineAttributeConverter = orderLineAttributeConverter;
	}

}
