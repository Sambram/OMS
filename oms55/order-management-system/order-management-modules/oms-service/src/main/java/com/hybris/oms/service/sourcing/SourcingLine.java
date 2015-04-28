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
package com.hybris.oms.service.sourcing;

import com.hybris.oms.service.managedobjects.inventory.LocationRole;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;


/**
 * Provides the order line data required for sourcing. The optional pickupLocationId is used for assigning the line to a
 * store directly and filtering them for the the regular sourcing process.
 */
public class SourcingLine extends PropertiesSupport
{
	private final String lineId;
	private final String pickupLocationId;
	private final Set<String> locationRoles;
	private final String country;
	private final int quantity;
	private final String sku;
	private final String orderLineId;

	/**
	 * Creates a new {@link SourcingLine}.
	 *
	 * @param sku sku, must not be blank.
	 * @param quantity quantity to be assigned, cannot be <= 0
	 * @param lineId lineId, must not be blank
	 * @throws IllegalArgumentException if the sku or lineId is blank or the quantity is <= 0
	 */
	public SourcingLine(final String sku, final int quantity, final String lineId)
	{
		this(sku, quantity, lineId, null, null, null);
	}

	/**
	 * Creates a new {@link SourcingLine}.
	 *
	 * @param sku sku, must not be blank.
	 * @param quantity quantity to be assigned, cannot be <= 0
	 * @param lineId lineId, must not be blank
	 * @param pickupLocationId locationId that has to be used
	 * @param locationRoles location roles that has to be used
	 * @param country country that has to be used
	 * @throws IllegalArgumentException if the sku or lineId is blank or the quantity is <= 0
	 */
	public SourcingLine(final String sku, final int quantity, final String lineId, final String pickupLocationId,
			final Set<String> locationRoles, final String country)
	{
		this(sku, quantity, lineId, pickupLocationId, locationRoles, country, lineId, null);
	}

	/**
	 * Creates a new {@link SourcingLine}.
	 *
	 * @param sku sku, must not be blank.
	 * @param quantity quantity to be assigned, cannot be <= 0
	 * @param lineId lineId, must not be blank
	 * @param pickupLocationId locationId that has to be used
	 * @param locationRoles location roles that has to be used
	 * @param country country that has to be used
	 * @param orderLineId orderLineId, must not be blank
	 * @param properties additional properties
	 * @throws IllegalArgumentException if the sku, lineId or orderLineId is blank or if the quantity is <= 0
	 */
	@SuppressWarnings({"PMD.ExcessiveParameterList"})
	public SourcingLine(final String sku, final int quantity, final String lineId, final String pickupLocationId,
			final Set<String> locationRoles, final String country, final String orderLineId, final Map<String, Object> properties)
			throws IllegalArgumentException
	{
		super(properties);
		Preconditions.checkArgument(StringUtils.isNotBlank(lineId), "lineId cannot be blank for sku %s", sku);
		Preconditions.checkArgument(StringUtils.isNotBlank(orderLineId), "orderLineId cannot be blank for sku %s", sku);
		Preconditions.checkArgument(StringUtils.isNotBlank(sku), "sku cannot be blank for lineId %s", lineId);
		Preconditions.checkArgument(quantity > 0, "Quantity has to be greater zero for lineId %s, sku %s", lineId, sku);
		this.sku = sku;
		this.lineId = lineId;
		this.quantity = quantity;
		this.pickupLocationId = pickupLocationId;
		this.locationRoles = CollectionUtils.isNotEmpty(locationRoles) ? locationRoles : pickupLocationId == null ? ImmutableSet
				.of(LocationRole.SHIPPING.getCode()) : ImmutableSet.of(LocationRole.PICKUP.getCode());
		this.country = country;
		this.orderLineId = orderLineId;
	}

	public String getLineId()
	{
		return this.lineId;
	}

	public String getPickupLocationId()
	{
		return this.pickupLocationId;
	}

	public int getQuantity()
	{
		return this.quantity;
	}

	public String getSku()
	{
		return this.sku;
	}

	public Set<String> getLocationRoles()
	{
		return this.locationRoles;
	}

	public String getCountry()
	{
		return country;
	}

	public String getOrderLineId()
	{
		return orderLineId;
	}

	@Override
	public String toString()
	{
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("sku", this.sku)
				.append("qty", this.quantity).append("lineId", this.lineId);
		if (this.pickupLocationId != null)
		{
			builder.append("pickLocId", this.pickupLocationId);
		}
		if (this.locationRoles != null)
		{
			builder.append("locationRoles", this.locationRoles);
		}
		if (this.country != null)
		{
			builder.append("country", this.country);
		}
		builder.append("properties", getProperties());
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(lineId);
	}

	@Override
	public boolean equals(final Object other)
	{
		if (this == other)
		{
			return true;
		}
		if (other == null || this.getClass() != other.getClass())
		{
			return false;
		}
		return Objects.equals(lineId, ((SourcingLine) other).getLineId());
	}

}
