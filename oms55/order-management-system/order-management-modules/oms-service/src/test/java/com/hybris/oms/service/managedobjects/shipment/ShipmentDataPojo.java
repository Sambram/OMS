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
package com.hybris.oms.service.managedobjects.shipment;

import com.hybris.kernel.api.HybrisId;
import com.hybris.oms.service.managedobjects.order.OrderData;
import com.hybris.oms.service.managedobjects.types.AddressVT;
import com.hybris.oms.service.managedobjects.types.PriceVT;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;



public class ShipmentDataPojo implements ShipmentData
{

	@Override
	public String getState()
	{
		return null;
	}

	@Override
	public String getAmountCapturedCurrencyCode()
	{
		return null;
	}

	@Override
	public double getAmountCapturedValue()
	{
		return 0;
	}

	@Override
	public List<String> getAuthUrls()
	{
		return null;
	}

	@Override
	public String getCurrencyCode()
	{
		return null;
	}

	@Override
	public DeliveryData getDelivery()
	{
		return null;
	}

	@Override
	public String getFirstArrivalStockroomLocationId()
	{
		return null;
	}

	@Override
	public String getGrossVolumeUnitCode()
	{
		return null;
	}

	@Override
	public float getGrossVolumeValue()
	{
		return 0;
	}

	@Override
	public String getGrossWeightUnitCode()
	{
		return null;
	}

	@Override
	public float getGrossWeightValue()
	{
		return 0;
	}

	@Override
	public String getHeightUnitCode()
	{
		return null;
	}

	@Override
	public float getHeightValue()
	{
		return 0;
	}

	@Override
	public String getInsuranceValueAmountCurrencyCode()
	{
		return null;
	}

	@Override
	public double getInsuranceValueAmountValue()
	{
		return 0;
	}

	@Override
	public String getLastExitStockroomLocationId()
	{
		return null;
	}

	@Override
	public String getLengthUnitCode()
	{
		return null;
	}

	@Override
	public float getLengthValue()
	{
		return 0;
	}

	@Override
	public String getStockroomLocationId()
	{
		return null;
	}

	@Override
	public PriceVT getMerchandisePrice()
	{
		return null;
	}

	@Override
	public String getNetWeightUnitCode()
	{
		return null;
	}

	@Override
	public float getNetWeightValue()
	{
		return 0;
	}

	@Override
	public String getOlqsStatus()
	{
		return olqStatus;
	}


	OrderData orderD;

	@Override
	public OrderData getOrderFk()
	{
		return orderD;
	}

	@Override
	public String getPriorityLevelCode()
	{
		return null;
	}

	@Override
	public AddressVT getShipFrom()
	{
		return null;
	}

	@Override
	public long getShipmentId()
	{
		return 0;
	}

	@Override
	public long getOriginalShipmentId()
	{
		return 0;
	}

	@Override
	public ShippingAndHandlingData getShippingAndHandling()
	{
		return null;
	}

	@Override
	public String getShippingMethod()
	{
		return null;
	}

	@Override
	public String getTaxCategory()
	{
		return null;
	}

	@Override
	public String getTotalGoodsItemQuantityUnitCode()
	{
		return null;
	}

	@Override
	public int getTotalGoodsItemQuantityValue()
	{
		return 0;
	}

	@Override
	public String getWidthUnitCode()
	{
		return null;
	}

	@Override
	public float getWidthValue()
	{
		return 0;
	}

	@Override
	public boolean isPickupInStore()
	{
		return false;
	}

	@Override
	public String getPackageDescription()
	{
		return null;
	}

	@Override
	public String getLocation()
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
	public void setAmountCapturedCurrencyCode(final String value)
	{

	}

	@Override
	public void setAmountCapturedValue(final double value)
	{

	}

	@Override
	public void setAuthUrls(final List<String> value)
	{

	}

	@Override
	public void setCurrencyCode(final String value)
	{

	}

	@Override
	public void setDelivery(final DeliveryData value)
	{

	}

	@Override
	public void setFirstArrivalStockroomLocationId(final String value)
	{

	}

	@Override
	public void setGrossVolumeUnitCode(final String value)
	{

	}

	@Override
	public void setGrossVolumeValue(final float value)
	{

	}

	@Override
	public void setGrossWeightUnitCode(final String value)
	{

	}

	@Override
	public void setGrossWeightValue(final float value)
	{

	}

	@Override
	public void setHeightUnitCode(final String value)
	{

	}

	@Override
	public void setHeightValue(final float value)
	{

	}

	@Override
	public void setInsuranceValueAmountCurrencyCode(final String value)
	{

	}

	@Override
	public void setInsuranceValueAmountValue(final double value)
	{

	}

	@Override
	public void setLastExitStockroomLocationId(final String value)
	{

	}

	@Override
	public void setLengthUnitCode(final String value)
	{

	}

	@Override
	public void setLengthValue(final float value)
	{

	}

	@Override
	public void setStockroomLocationId(final String value)
	{

	}

	@Override
	public void setMerchandisePrice(final PriceVT value)
	{

	}

	@Override
	public void setNetWeightUnitCode(final String value)
	{

	}

	@Override
	public void setNetWeightValue(final float value)
	{

	}

	String olqStatus;

	@Override
	public void setOlqsStatus(final String value)
	{
		this.olqStatus = value;
	}

	@Override
	public void setOrderFk(final OrderData value)
	{
		this.orderD = value;
	}

	@Override
	public void setPriorityLevelCode(final String value)
	{

	}

	@Override
	public void setShipFrom(final AddressVT value)
	{

	}

	@Override
	public void setShipmentId(final long value)
	{

	}

	@Override
	public void setOriginalShipmentId(final long value)
	{

	}

	@Override
	public void setShippingAndHandling(final ShippingAndHandlingData value)
	{

	}

	@Override
	public void setShippingMethod(final String value)
	{

	}

	@Override
	public void setTaxCategory(final String value)
	{

	}

	@Override
	public void setTotalGoodsItemQuantityUnitCode(final String value)
	{

	}

	@Override
	public void setTotalGoodsItemQuantityValue(final int value)
	{

	}

	@Override
	public void setWidthUnitCode(final String value)
	{

	}

	@Override
	public void setWidthValue(final float value)
	{

	}

	@Override
	public void setPickupInStore(final boolean value)
	{

	}

	@Override
	public void setPackageDescription(final String value)
	{

	}

	@Override
	public void setLocation(final String value)
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
	public OrderlinesFulfillmentType getOrderLinesFulfillmentType()
	{
		return null;
	}

	@Override
	public void setOrderLinesFulfillmentType(final OrderlinesFulfillmentType value)
	{

	}
}
