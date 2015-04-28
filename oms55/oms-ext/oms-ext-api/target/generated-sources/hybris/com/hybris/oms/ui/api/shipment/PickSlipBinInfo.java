

/*
 * [y] hybris Core+ Platform
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

package com.hybris.oms.ui.api.shipment;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;


import com.hybris.oms.ui.api.shipment.OrderLineShipmentPickSlipBinInfo;
import java.util.List;
import com.hybris.commons.dto.Dto;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
/**
* PickSlipBinInfo
* Generated automatically
* @author: dto-generator, [y] hybris Platform
*/
@XmlRootElement
public class PickSlipBinInfo  implements Serializable, Dto
{

	public final static long serialVersionUID = 756077686L;

	private String shipmentId;

	private List<OrderLineShipmentPickSlipBinInfo> orderLineBins;

	
	public PickSlipBinInfo(){}

	protected PickSlipBinInfo(Builder builder)
	{
	
		setShipmentId(builder.getShipmentId());
		setOrderLineBins(builder.getOrderLineBins());
	
	}


	/**
	* gets 
	*
	* @returns String
	*/
	public String getShipmentId()
	{
		return shipmentId;
	}

	/**
	* sets 
	*
	*/
	public void setShipmentId(String shipmentId)
	{
		this.shipmentId = shipmentId;
	}

	/**
	* gets 
	*
	* @returns List<OrderLineShipmentPickSlipBinInfo>
	*/
	public List<OrderLineShipmentPickSlipBinInfo> getOrderLineBins()
	{
		return orderLineBins;
	}

	/**
	* sets 
	*
	*/
	public void setOrderLineBins(List<OrderLineShipmentPickSlipBinInfo> orderLineBins)
	{
		this.orderLineBins = orderLineBins;
	}


	@Override
	public String toString()
	{
		return reflectionToString(this, SHORT_PREFIX_STYLE);
	}

	private static class Builder2 extends Builder<Builder2>
	{
		@Override
		protected Builder2 self()
		{
			return this;
		}
	}

	public static Builder<?> builder()
	{
		return new Builder2();
	}

	public abstract static class Builder<T extends Builder<T>> 
	{
		private String shipmentId;
		private List<OrderLineShipmentPickSlipBinInfo> orderLineBins;
		
	
		/**
		* sets 
		*
		*/
		public T setShipmentId(String shipmentId)
		{
			this.shipmentId = shipmentId;
			return self();
		}

		private String getShipmentId()
		{
			return shipmentId;
		}
	
		/**
		* sets 
		*
		*/
		public T setOrderLineBins(List<OrderLineShipmentPickSlipBinInfo> orderLineBins)
		{
			this.orderLineBins = orderLineBins;
			return self();
		}

		private List<OrderLineShipmentPickSlipBinInfo> getOrderLineBins()
		{
			return orderLineBins;
		}
	
		protected abstract T self();

		public PickSlipBinInfo build(){
			return new PickSlipBinInfo(this);
		}
	}
}

