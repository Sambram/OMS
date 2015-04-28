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
import com.hybris.oms.service.managedobjects.order.OrderLineData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityData;
import com.hybris.oms.service.managedobjects.order.OrderLineQuantityStatusData;
import com.hybris.oms.service.managedobjects.shipment.ShipmentData;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


public class OrderLineQuantityDataPojo implements OrderLineQuantityData
{
	@Override public String getStockroomLocationId()
	{
		return null;
	}

	@Override public long getOlqId()
	{
		return 0;
	}

	@Override public OrderLineData getOrderLine()
	{
		return null;
	}

	@Override public String getQuantityUnitCode()
	{
		return null;
	}

	@Override public int getQuantityValue()
	{
		return quantityValue;
	}

	@Override public ShipmentData getShipment()
	{
		return shipment;
	}

	@Override public OrderLineQuantityStatusData getStatus()
	{
		return olqStatus;
	}

	@Override public Long getVersion()
	{
		return null;
	}

	@Override public void setStockroomLocationId(String value)
	{

	}

	@Override public void setOlqId(long value)
	{

	}

	@Override public void setOrderLine(OrderLineData value)
	{

	}

	@Override public void setQuantityUnitCode(String value)
	{

	}

	int quantityValue;
	@Override public void setQuantityValue(int value)
	{
		this.quantityValue = value;
	}

	ShipmentData shipment;
	@Override public void setShipment(ShipmentData value)
	{
		this.shipment = value;
	}

	OrderLineQuantityStatusData olqStatus;
	@Override public void setStatus(OrderLineQuantityStatusData value)
	{
		this.olqStatus = value;
	}

	@Override public void setVersion(Long value)
	{

	}

	@Override public HybrisId getId()
	{
		return null;
	}

	@Override public Date getCreationTime()
	{
		return null;
	}

	@Override public Date getModifiedTime()
	{
		return null;
	}

	@Override public Object getProperty(String attributeName)
	{
		return null;
	}

	@Override public Object getProperty(String attributeName, Locale locale)
	{
		return null;
	}

	@Override public <T> T getProperty(String attributeName, Class<T> clazz)
	{
		return null;
	}

	@Override public <T> T getProperty(String attributeName, Locale locale, Class<T> clazz)
	{
		return null;
	}

	@Override public void setProperty(String attributeName, Object value)
	{

	}

	@Override public void setProperty(String attributeName, Object value, Locale locale)
	{

	}

	@Override public Map<String, Object> getAllProperties()
	{
		return null;
	}

	@Override public Collection<String> getAllPropertyAttributeNames()
	{
		return null;
	}
}
