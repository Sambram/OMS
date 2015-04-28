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
import com.hybris.oms.service.managedobjects.basestore.BaseStoreData;
import com.hybris.oms.service.managedobjects.shipment.ShippingAndHandlingData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.ContactVT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class OrderDataPojo implements OrderData
{

	private String orderId;
	private List<OrderLineData> orderLines = new ArrayList<>();

	@Override
	public String getState()
	{
		return null;
	}

	@Override
	public ContactVT getContact()
	{
		return null;
	}

	@Override
	public String getCurrencyCode()
	{
		return null;
	}

	@Override
	public String getCustomerLocale()
	{
		return null;
	}

	@Override
	public String getEmailid()
	{
		return null;
	}

	@Override
	public String getFirstName()
	{
		return null;
	}

	@Override
	public Date getIssueDate()
	{
		return null;
	}

	@Override
	public Date getScheduledShippingDate()
	{
		return null;
	}

	@Override
	public String getLastName()
	{
		return null;
	}

	@Override
	public String getOrderId()
	{
		return orderId;
	}

	@Override
	public List<OrderLineData> getOrderLines()
	{
		return orderLines;
	}

	@Override
	public List<PaymentInfoData> getPaymentInfos()
	{
		return null;
	}

	@Override
	public String getPriorityLevelCode()
	{
		return null;
	}

	@Override
	public AddressVT getShippingAddress()
	{
		return null;
	}

	@Override
	public ShippingAndHandlingData getShippingAndHandling()
	{
		return null;
	}

	@Override
	public String getShippingFirstName()
	{
		return null;
	}

	@Override
	public String getShippingLastName()
	{
		return null;
	}

	@Override
	public String getShippingMethod()
	{
		return null;
	}

	@Override
	public String getShippingTaxCategory()
	{
		return null;
	}

	@Override
	public String getUsername()
	{
		return null;
	}

	@Override
	public List<String> getStockroomLocationIds()
	{
		return null;
	}

	@Override
	public BaseStoreData getBaseStore()
	{
		return null;
	}

	@Override
	public Long getVersion()
	{
		return null;
	}

	@Override
	public void setState(final String value)
	{

	}

	@Override
	public void setContact(final ContactVT value)
	{

	}

	@Override
	public void setCurrencyCode(final String value)
	{

	}

	@Override
	public void setCustomerLocale(final String value)
	{

	}

	@Override
	public void setEmailid(final String value)
	{

	}

	@Override
	public void setFirstName(final String value)
	{

	}

	@Override
	public void setIssueDate(final Date value)
	{

	}

	@Override
	public void setScheduledShippingDate(final Date value)
	{

	}

	@Override
	public void setLastName(final String value)
	{

	}

	@Override
	public void setOrderId(final String value)
	{
		this.orderId = value;
	}

	@Override
	public void setOrderLines(List<OrderLineData> value)
	{
		if (value != null)
		{
			value = new ArrayList<OrderLineData>(value);
		}

		this.orderLines = value;
	}

	@Override
	public void setPaymentInfos(final List<PaymentInfoData> value)
	{

	}

	@Override
	public void setPriorityLevelCode(final String value)
	{

	}

	@Override
	public void setShippingAddress(final AddressVT value)
	{

	}

	@Override
	public void setShippingAndHandling(final ShippingAndHandlingData value)
	{

	}

	@Override
	public void setShippingFirstName(final String value)
	{

	}

	@Override
	public void setShippingLastName(final String value)
	{

	}

	@Override
	public void setShippingMethod(final String value)
	{

	}

	@Override
	public void setShippingTaxCategory(final String value)
	{

	}

	@Override
	public void setUsername(final String value)
	{

	}

	@Override
	public void setStockroomLocationIds(final List<String> value)
	{

	}

	@Override
	public void setBaseStore(final BaseStoreData value)
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

}
