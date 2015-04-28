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
package com.hybris.oms.service.managedobjects.order;


import com.hybris.kernel.api.HybrisId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class OrderLineDataPojo implements OrderLineData
{

	private List<OrderLineQuantityData> orderLineQuantities = new ArrayList<>();
	private int quantityValue;
	private int quantityUnassignedValue;
	private String skuId;

	@Override
	public String getNote()
	{
		return null;
	}

	@Override
	public OrderData getMyOrder()
	{
		return null;
	}

	@Override
	public String getOrderLineId()
	{
		return null;
	}

	@Override
	public List<OrderLineQuantityData> getOrderLineQuantities()
	{
		return orderLineQuantities;
	}

	@Override
	public String getOrderLineStatus()
	{
		return null;
	}

	@Override
	public String getQuantityUnassignedUnitCode()
	{
		return null;
	}

	@Override
	public int getQuantityUnassignedValue()
	{
		return quantityUnassignedValue;
	}

	@Override
	public String getQuantityUnitCode()
	{
		return null;
	}

	@Override
	public int getQuantityValue()
	{
		return quantityValue;
	}

	@Override
	public String getSkuId()
	{
		return skuId;
	}

	@Override
	public String getTaxCategory()
	{
		return null;
	}

	@Override
	public String getUnitPriceCurrencyCode()
	{
		return null;
	}

	@Override
	public double getUnitPriceValue()
	{
		return 0;
	}

	@Override
	public String getUnitTaxCurrencyCode()
	{
		return null;
	}

	@Override
	public double getUnitTaxValue()
	{
		return 0;
	}

	@Override
	public String getPickupStoreId()
	{
		return null;
	}

	@Override
	public Set<String> getLocationRoles()
	{
		return null;
	}

	@Override
	public List<OrderLineAttributeData> getOrderLineAttributes()
	{
		return null;
	}

	@Override
	public Long getVersion()
	{
		return null;
	}

	@Override
	public void setNote(final String value)
	{

	}

	@Override
	public void setMyOrder(final OrderData value)
	{

	}

	@Override
	public void setOrderLineId(final String value)
	{

	}

	@Override
	public void setOrderLineQuantities(final List<OrderLineQuantityData> value)
	{
		this.orderLineQuantities = value;
	}

	@Override
	public void setOrderLineStatus(final String value)
	{

	}

	@Override
	public void setQuantityUnassignedUnitCode(final String value)
	{

	}

	@Override
	public void setQuantityUnassignedValue(final int value)
	{
		this.quantityUnassignedValue = value;
	}

	@Override
	public void setQuantityUnitCode(final String value)
	{

	}

	@Override
	public void setQuantityValue(final int value)
	{
		this.quantityValue = value;
	}

	@Override
	public void setSkuId(final String value)
	{
		this.skuId = value;
	}

	@Override
	public void setTaxCategory(final String value)
	{

	}

	@Override
	public void setUnitPriceCurrencyCode(final String value)
	{

	}

	@Override
	public void setUnitPriceValue(final double value)
	{

	}

	@Override
	public void setUnitTaxCurrencyCode(final String value)
	{

	}

	@Override
	public void setUnitTaxValue(final double value)
	{

	}

	@Override
	public void setPickupStoreId(final String value)
	{

	}

	@Override
	public void setLocationRoles(final Set<String> value)
	{

	}

	@Override
	public void setOrderLineAttributes(final List<OrderLineAttributeData> value)
	{

	}

	@Override
	public void setVersion(final Long value)
	{

	}

	@Override
	public HybrisId getId()
	{
		return null;
	}

	@Override
	public Date getCreationTime()
	{
		return null;
	}

	@Override
	public Date getModifiedTime()
	{
		return null;
	}

	@Override
	public Object getProperty(final String attributeName)
	{
		return null;
	}

	@Override
	public Object getProperty(final String attributeName, final Locale locale)
	{
		return null;
	}

	@Override
	public <T> T getProperty(final String attributeName, final Class<T> clazz)
	{
		return null;
	}

	@Override
	public <T> T getProperty(final String attributeName, final Locale locale, final Class<T> clazz)
	{
		return null;
	}

	@Override
	public void setProperty(final String attributeName, final Object value)
	{

	}

	@Override
	public void setProperty(final String attributeName, final Object value, final Locale locale)
	{

	}

	@Override
	public Map<String, Object> getAllProperties()
	{
		return null;
	}

	@Override
	public Collection<String> getAllPropertyAttributeNames()
	{
		return null;
	}

	@Override
	public OrderlineFulfillmentType getFulfillmentType()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFulfillmentType(final OrderlineFulfillmentType value)
	{
		// YTODO Auto-generated method stub

	}
}
