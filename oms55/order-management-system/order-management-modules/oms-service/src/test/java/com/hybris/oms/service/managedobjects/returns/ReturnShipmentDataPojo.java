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

import com.hybris.kernel.api.HybrisId;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 *
 */
public class ReturnShipmentDataPojo implements ReturnShipmentData
{

	@Override
	public HybrisId getId()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getCreationTime()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getModifiedTime()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProperty(final String attributeName)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getProperty(final String attributeName, final Locale locale)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getProperty(final String attributeName, final Class<T> clazz)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getProperty(final String attributeName, final Locale locale, final Class<T> clazz)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProperty(final String attributeName, final Object value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setProperty(final String attributeName, final Object value, final Locale locale)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getAllProperties()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getAllPropertyAttributeNames()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@NotNull
	public long getReturnShipmentId()
	{
		// YTODO Auto-generated method stub
		return 0;
	}

	@Override
	public ReturnData getMyReturn()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Size(max = 255)
	public String getShippingMethod()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Size(max = 255)
	public String getLabelUrl()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Size(max = 255)
	public String getTrackingId()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Size(max = 255)
	public String getTrackingUrl()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Size(max = 255)
	public String getPackageDescription()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Size(max = 255)
	public String getNote()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Size(max = 255)
	public String getInsuranceValueAmountCurrencyCode()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public double getInsuranceValueAmountValue()
	{
		// YTODO Auto-generated method stub
		return 0;
	}

	@Override
	@Size(max = 255)
	public String getHeightUnitCode()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public float getHeightValue()
	{
		// YTODO Auto-generated method stub
		return 0;
	}

	@Override
	@Size(max = 255)
	public String getLengthUnitCode()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public float getLengthValue()
	{
		// YTODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getWidthValue()
	{
		// YTODO Auto-generated method stub
		return 0;
	}

	@Override
	@Size(max = 255)
	public String getWidthUnitCode()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	@Size(max = 255)
	public String getGrossWeightUnitCode()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public float getGrossWeightValue()
	{
		// YTODO Auto-generated method stub
		return 0;
	}

	@Override
	public Long getVersion()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReturnShipmentId(final long value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setMyReturn(final ReturnData value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setShippingMethod(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setLabelUrl(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setTrackingId(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setTrackingUrl(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setPackageDescription(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setNote(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setInsuranceValueAmountCurrencyCode(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setInsuranceValueAmountValue(final double value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setHeightUnitCode(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setHeightValue(final float value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setLengthUnitCode(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setLengthValue(final float value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setWidthValue(final float value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setWidthUnitCode(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setGrossWeightUnitCode(final String value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setGrossWeightValue(final float value)
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public void setVersion(final Long value)
	{
		// YTODO Auto-generated method stub

	}

}
