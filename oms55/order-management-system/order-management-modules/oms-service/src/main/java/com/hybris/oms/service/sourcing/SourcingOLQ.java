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


import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Immutable object containing OLQ data from a previous sourcing operation.
 */
public class SourcingOLQ extends PropertiesSupport
{
	private final String sku;
	private final int quantity;
	private final String locationId;
	private final String lineId;
	private final String strategyName;

	public SourcingOLQ(final String sku, final int quantity, final String locationId, final String lineId)
	{
		this(sku, quantity, locationId, lineId, null, null);
	}

	public SourcingOLQ(final String sku, final int quantity, final String locationId, final String lineId,
			final Class<?> strategyClass, final Map<String, Object> properties)
	{
		super(properties);
		this.sku = sku;
		this.quantity = quantity;
		this.locationId = locationId;
		this.lineId = lineId;
		if (strategyClass != null)
		{
			this.strategyName = strategyClass.getSimpleName();
		}
		else
		{
			strategyName = null;
		}
	}

	public String getSku()
	{
		return this.sku;
	}

	public int getQuantity()
	{
		return this.quantity;
	}

	public String getLocationId()
	{
		return this.locationId;
	}

	public String getLineId()
	{
		return this.lineId;
	}

	public String getStrategyName()
	{
		return this.strategyName;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		final SourcingOLQ other = (SourcingOLQ) obj;
		return Objects.equals(other.sku, this.sku) && Objects.equals(other.locationId, this.locationId)
				&& Objects.equals(other.quantity, this.quantity) && Objects.equals(other.lineId, this.lineId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.sku, this.locationId, this.quantity, this.lineId);
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("lineId", this.lineId).append("sku", this.sku)
				.append("qty", this.quantity).append("locId", this.locationId).append("strategy", this.strategyName).toString();
	}

}
