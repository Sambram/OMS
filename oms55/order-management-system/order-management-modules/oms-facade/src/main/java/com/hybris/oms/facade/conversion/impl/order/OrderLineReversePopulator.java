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
import com.hybris.commons.conversion.impl.EnumToEnumConverter;
import com.hybris.commons.conversion.util.Converters;
import com.hybris.oms.domain.locationrole.LocationRole;
import com.hybris.oms.domain.order.OrderLine;
import com.hybris.oms.domain.order.OrderLineAttribute;
import com.hybris.oms.domain.order.OrderLineQuantity;
import com.hybris.oms.service.managedobjects.order.OrderLineAttributeData;
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Converter for order line dto to data.
 */
public class OrderLineReversePopulator implements Populator<OrderLine, OrderLineData>
{

	private Converters converters;

	private Converter<OrderLineQuantity, OrderLineQuantityData> orderLineQuantityReverseConverter;

	private Converter<OrderLineAttribute, OrderLineAttributeData> orderLineAttributeReverseConverter;

	private final EnumToEnumConverter<com.hybris.oms.domain.locationrole.LocationRole, //
	com.hybris.oms.service.managedobjects.inventory.LocationRole> locationRoleConverter = //
	new EnumToEnumConverter<com.hybris.oms.domain.locationrole.LocationRole, com.hybris.oms.service.managedobjects.inventory.LocationRole>()
	{
		@Override
		public Class<com.hybris.oms.service.managedobjects.inventory.LocationRole> getTargetClass()
		{
			return com.hybris.oms.service.managedobjects.inventory.LocationRole.class;
		}
	};

	protected EnumToEnumConverter<com.hybris.oms.domain.locationrole.LocationRole, //
	com.hybris.oms.service.managedobjects.inventory.LocationRole> getLocationRoleConverter()
	{
		return this.locationRoleConverter;
	}

	private final EnumToEnumConverter<com.hybris.oms.domain.order.OrderlineFulfillmentType, com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType> orderLineFulfillmentTypeConverter = new EnumToEnumConverter<com.hybris.oms.domain.order.OrderlineFulfillmentType, com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType>()
	{
		@Override
		public Class<com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType> getTargetClass()
		{
			return com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType.class;
		}
	};

	protected EnumToEnumConverter<com.hybris.oms.domain.order.OrderlineFulfillmentType, com.hybris.oms.service.managedobjects.order.OrderlineFulfillmentType> getOrderlineFulfillmentType()
	{
		return this.orderLineFulfillmentTypeConverter;
	}

	@Override
	public void populateFinals(final OrderLine source, final OrderLineData target) throws ConversionException
	{
		target.setSkuId(source.getSkuId());
		target.setOrderLineId(source.getOrderLineId());
	}

	@Override
	public void populate(final OrderLine source, final OrderLineData target) throws ConversionException
	{
		target.setNote(source.getNote());
		target.setOrderLineStatus(source.getOrderLineStatus());
		if (source.getQuantityUnassigned() != null)
		{
			target.setQuantityUnassignedUnitCode(source.getQuantityUnassigned().getUnitCode());
			target.setQuantityUnassignedValue(source.getQuantityUnassigned().getValue());
		}
		if (source.getQuantity() != null)
		{
			target.setQuantityUnitCode(source.getQuantity().getUnitCode());
			target.setQuantityValue(source.getQuantity().getValue());
		}
		target.setTaxCategory(source.getTaxCategory());
		if (source.getUnitPrice() != null)
		{
			target.setUnitPriceCurrencyCode(source.getUnitPrice().getCurrencyCode());
			target.setUnitPriceValue(source.getUnitPrice().getValue());
		}
		if (source.getUnitTax() != null)
		{
			target.setUnitTaxCurrencyCode(source.getUnitTax().getCurrencyCode());
			target.setUnitTaxValue(source.getUnitTax().getValue());
		}
		target.setPickupStoreId(source.getPickupStoreId());
		final Set<String> roles = new HashSet<>();
		if (source.getLocationRoles() != null && !source.getLocationRoles().isEmpty())
		{
			for (final LocationRole role : source.getLocationRoles())
			{
				roles.add(role.name());
			}
			target.setLocationRoles(roles);
		}
		if (source.getFulfillmentType() != null)
		{
			target.setFulfillmentType(this.getOrderlineFulfillmentType().convert(source.getFulfillmentType()));
		}
		else
		{
			target.setFulfillmentType(OrderlineFulfillmentType.REGULAR);
		}
		this.populateOrderLineQuantities(source, target);

		this.populateOrderLineAttributes(source, target);

	}

	protected void populateOrderLineQuantities(final OrderLine source, final OrderLineData target)
	{
		target.setOrderLineQuantities(this.converters.convertAll(source.getOrderLineQuantities(),
				this.orderLineQuantityReverseConverter));
		for (final OrderLineQuantityData olqData : target.getOrderLineQuantities())
		{
			olqData.setOrderLine(target);
		}
	}

	protected void populateOrderLineAttributes(final OrderLine source, final OrderLineData target)
	{
		target.setOrderLineAttributes(this.converters.convertAll(source.getOrderLineAttributes(),
				this.orderLineAttributeReverseConverter));
		for (final OrderLineAttributeData olaData : target.getOrderLineAttributes())
		{
			olaData.setOrderLine(target);
		}
	}

	@Required
	public void setConverters(final Converters converters)
	{
		this.converters = converters;
	}

	@Required
	public void setOrderLineQuantityReverseConverter(
			final Converter<OrderLineQuantity, OrderLineQuantityData> orderLineQuantityReverseConverter)
	{
		this.orderLineQuantityReverseConverter = orderLineQuantityReverseConverter;
	}

	@Required
	public void setOrderLineAttributeReverseConverter(
			final Converter<OrderLineAttribute, OrderLineAttributeData> orderLineAttributeReverseConverter)
	{
		this.orderLineAttributeReverseConverter = orderLineAttributeReverseConverter;
	}
}
