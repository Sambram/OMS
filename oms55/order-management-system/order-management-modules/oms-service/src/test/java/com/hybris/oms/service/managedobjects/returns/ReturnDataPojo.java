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
package com.hybris.oms.service.managedobjects.returns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.hybris.kernel.api.HybrisId;
import com.hybris.oms.service.managedobjects.inventory.StockroomLocationData;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.types.AmountVT;


public class ReturnDataPojo implements ReturnData
{

	private long returnId;
	private String state;
	private String returnReasonCode;
	private AmountVT totalReturn;
	private OrderData order;
	private ReturnShipmentData returnShipment;
	private List<ReturnOrderLineData> returnOrderLines = new ArrayList<>();

	@Override
	public long getReturnId()
	{
		return returnId;
	}

	@Override
	public void setReturnId(final long returnId)
	{
		this.returnId = returnId;
	}

	@Override
	public String getState()
	{
		return state;
	}

	@Override
	public void setState(final String state)
	{
		this.state = state;
	}

	public AmountVT getTotalReturn()
	{
		return totalReturn;
	}

	public void setTotalReturn(final AmountVT totalReturn)
	{
		this.totalReturn = totalReturn;
	}

	@Override
	public String getReturnReasonCode()
	{
		return returnReasonCode;
	}

	@Override
	public void setReturnReasonCode(final String value)
	{
		this.returnReasonCode = value;
	}

	@Override
	public OrderData getOrder()
	{
		return order;
	}


	@Override
	public List<ReturnOrderLineData> getReturnOrderLines()
	{
		return returnOrderLines;
	}

	@Override
	public ReturnPaymentInfoData getReturnPaymentInfos()
	{
		return null;
	}


	@Override
	public boolean isShippingRefunded()
	{
		return false;
	}

	@Override
	public Long getVersion()
	{
		return null;
	}

	@Override
	public void setOrder(final OrderData value)
	{
		this.order = value;
	}


	@Override
	public void setReturnOrderLines(final List<ReturnOrderLineData> value)
	{
		this.returnOrderLines = value;
	}

	@Override
	public void setReturnPaymentInfos(final ReturnPaymentInfoData value)
	{
		// Empty code block
	}

	@Override
	public void setShippingRefunded(final boolean value)
	{
		// Empty code block
	}

	@Override
	public void setVersion(final Long value)
	{
		// Empty code block
	}

	@Override
	public HybrisId getId()
	{
		return HybrisId.valueOf("single|ReturnData|" + getReturnId());
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
		// Empty code block
	}

	@Override
	public void setProperty(final String attributeName, final Object value, final Locale locale)
	{
		// Empty code block
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
	public StockroomLocationData getReturnLocation()
	{
		return null;
	}

	@Override
	public ReturnShipmentData getReturnShipment()
	{
		return returnShipment;
	}

	@Override
	public AmountVT getCustomRefundAmount()
	{
		return null;
	}

	@Override
	public AmountVT getCalculatedRefundAmount()
	{
		return null;
	}

	@Override
	public void setReturnLocation(final StockroomLocationData value)
	{
		// Empty code block
	}

	@Override
	public void setReturnShipment(final ReturnShipmentData value)
	{
		this.returnShipment = value;
	}

	@Override
	public void setCustomRefundAmount(final AmountVT value)
	{
		// Empty code block
	}

	@Override
	public void setCalculatedRefundAmount(final AmountVT value)
	{
		// Empty code block
	}
}
